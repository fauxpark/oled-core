package net.fauxpark.oled.impl;

import java.io.IOException;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;

import net.fauxpark.oled.Command;
import net.fauxpark.oled.Constant;
import net.fauxpark.oled.SSD1306Display;
import net.fauxpark.oled.SSDisplay;

/**
 * A simple SPI driver for the Adafruit SSDisplay OLED display.
 *
 * @author fauxpark
 */
public class SSD1306SPIImpl extends SSD1306Display {
	public static final int SPI_SPEED = 8000000;

	/**
	 * The GPIO pin corresponding to the D/C line on the display.
	 */
	private GpioPinDigitalOutput dcPin;

	/**
	 * The internal SPI device.
	 */
	private SpiDevice spi;

	/**
	 * SSD1306SPIImpl constructor.
	 *
	 * @param width The width of the display in pixels.
	 * @param height The height of the display in pixels.
	 * @param channel The SPI channel to use.
	 * @param rstPin The GPIO pin to use for the RST line.
	 * @param dcPin The GPIO pin to use for the D/C line.
	 */
	public SSD1306SPIImpl(int width, int height, SpiChannel channel, Pin rstPin, Pin dcPin) throws IOException {
		super(width, height, rstPin);

		this.dcPin = getGpio().provisionDigitalOutputPin(dcPin);
		spi = SpiFactory.getInstance(channel, SPI_SPEED);
	}






	@Override
	public void setInverted(boolean inverted) {
		command(inverted ? Command.INVERT_DISPLAY : Command.NORMAL_DISPLAY);
		super.setInverted(inverted);
	}

	@Override
	public void setContrast(int contrast) {
		if(contrast < 0 || contrast > 255) {
			return;
		}

		command(Command.SET_CONTRAST, contrast);
		super.setContrast(contrast);
	}

	@Override
	public void setOffset(int offset) {
		command(Command.SET_DISPLAY_OFFSET, offset);
		super.setOffset(offset);
	}

	@Override
	public void setHFlipped(boolean hFlipped) {
		if(hFlipped) {
			command(Command.SET_SEGMENT_REMAP);
		} else {
			command(Command.SET_SEGMENT_REMAP_REVERSE);
		}

		// Horizontal flipping is not immediate
		display();
		super.setHFlipped(hFlipped);
	}

	@Override
	public void setVFlipped(boolean vFlipped) {
		if(vFlipped) {
			command(Command.SET_COM_SCAN_INC);
		} else {
			command(Command.SET_COM_SCAN_DEC);
		}

		super.setVFlipped(vFlipped);
	}

	@Override
	public void scrollHorizontally(boolean direction, int start, int end, int speed) {
		command(direction ? Command.LEFT_HORIZONTAL_SCROLL : Command.RIGHT_HORIZONTAL_SCROLL, Constant.DUMMY_BYTE_00, start, speed, end, Constant.DUMMY_BYTE_00, Constant.DUMMY_BYTE_FF);
	}

	@Override
	public void scrollDiagonally(boolean direction, int start, int end, int offset, int rows, int speed, int step) {
		command(Command.SET_VERTICAL_SCROLL_AREA, offset, rows);
		command(direction ? Command.VERTICAL_AND_LEFT_HORIZONTAL_SCROLL : Command.VERTICAL_AND_RIGHT_HORIZONTAL_SCROLL, Constant.DUMMY_BYTE_00, start, speed, end, step);
	}

	@Override
	public void startScroll() {
		command(Command.ACTIVATE_SCROLL);
		super.startScroll();
	}

	@Override
	public void stopScroll() {
		command(Command.DEACTIVATE_SCROLL);
		super.stopScroll();
	}

	@Override
	public void noOp() {
		command(Command.NOOP);
	}

	@Override
	public void command(int command, int... params) {
		dcPin.setState(false);

		try {
			spi.write((byte) command);
		} catch(IOException e) {
			e.printStackTrace();
		}

		for(int param : params) {
			try {
				spi.write((byte) param);
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void data(byte[] data) {
		dcPin.setState(true);

		try {
			spi.write(data);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
