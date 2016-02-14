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
import net.fauxpark.oled.SSD1306;

/**
 * A simple SPI driver for the Adafruit SSD1306 OLED display.
 *
 * @author fauxpark
 */
public class SSD1306Impl extends SSD1306 {
	/**
	 * The internal GPIO instance.
	 */
	private GpioController gpio;

	/**
	 * The GPIO pin corresponding to the RST line on the display.
	 */
	private GpioPinDigitalOutput rstPin;

	/**
	 * The GPIO pin corresponding to the D/C line on the display.
	 */
	private GpioPinDigitalOutput dcPin;

	/**
	 * The internal SPI device.
	 */
	private SpiDevice spi;

	/**
	 * SSD1306Impl constructor.
	 *
	 * @param width The width of the display in pixels.
	 * @param height The height of the display in pixels.
	 * @param channel The SPI channel to use.
	 * @param rstPin The GPIO pin to use for the RST line.
	 * @param dcPin The GPIO pin to use for the D/C line.
	 */
	public SSD1306Impl(int width, int height, SpiChannel channel, Pin rstPin, Pin dcPin) {
		super(width, height);
		gpio = GpioFactory.getInstance();
		this.rstPin = gpio.provisionDigitalOutputPin(rstPin);
		this.dcPin = gpio.provisionDigitalOutputPin(dcPin);

		try {
			spi = SpiFactory.getInstance(channel, 8000000);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void startup(boolean externalVcc) {
		reset();
		setDisplayOn(false);
		command(Command.SET_DISPLAY_CLOCK_DIV, width);
		command(Command.SET_MULTIPLEX_RATIO, width - 1);
		command(Command.SET_DISPLAY_OFFSET, 0);
		command(Command.SET_START_LINE_40);
		command(Command.SET_CHARGE_PUMP, externalVcc ? Constant.CHARGE_PUMP_DISABLE : Constant.CHARGE_PUMP_ENABLE);
		command(Command.SET_MEMORY_MODE, Constant.MEMORY_MODE_HORIZONTAL);
		command(Command.SET_SEGMENT_REMAP_REVERSE);
		command(Command.SET_COM_SCAN_DEC);
		command(Command.SET_COM_PINS, height == 64 ? 0x12 : 0x02);
		setContrast(externalVcc ? 0x9F : 0xCF);
		command(Command.SET_PRECHARGE_PERIOD, externalVcc ? 0x22 : 0xF1);
		command(Command.SET_VCOM_DESELECT, 0x40);
		command(Command.DISPLAY_ALL_ON_RESUME);
		setInverted(false);
		setDisplayOn(true);
		clear();
		display();
	}

	@Override
	public void shutdown() {
		clear();
		display();
		setDisplayOn(false);
		reset();
		gpio.shutdown();
	}

	@Override
	public void reset() {
		try {
			rstPin.setState(true);
			Thread.sleep(1);
			rstPin.setState(false);
			Thread.sleep(10);
			rstPin.setState(true);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public synchronized void display() {
		command(Command.SET_COLUMN_ADDRESS, 0, width - 1);
		command(Command.SET_PAGE_ADDRESS, 0, pages - 1);
		data(buffer);
	}

	@Override
	public void setDisplayOn(boolean displayOn) {
		if(displayOn) {
			command(Command.DISPLAY_ON);
		} else {
			command(Command.DISPLAY_OFF);
		}

		super.setDisplayOn(displayOn);
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
	public void setHFlipped(boolean hFlipped) {
		// TODO

		if(hFlipped) {

		} else {

		}

		super.setHFlipped(hFlipped);
	}

	@Override
	public void setVFlipped(boolean vFlipped) {
		if(vFlipped) {
			command(Command.SET_COM_SCAN_INC);
			command(Command.SET_SEGMENT_REMAP);
		} else {
			command(Command.SET_COM_SCAN_DEC);
			command(Command.SET_COM_PINS, 0x02);
		}

		super.setVFlipped(vFlipped);
	}

	@Override
	public void scrollHorizontally(boolean direction, int start, int end, int step) {
		command(direction ? Command.LEFT_HORIZONTAL_SCROLL : Command.RIGHT_HORIZONTAL_SCROLL, Constant.DUMMY_BYTE_00, start, step, end, 0x01, Constant.DUMMY_BYTE_FF);
	}

	@Override
	public void scrollDiagonally(boolean direction, int start, int end, int step) {
		command(Command.SET_VERTICAL_SCROLL_AREA, 0, height);
		command(direction ? Command.VERTICAL_AND_LEFT_HORIZONTAL_SCROLL : Command.VERTICAL_AND_RIGHT_HORIZONTAL_SCROLL, Constant.DUMMY_BYTE_00, start, step, end, 0x01);
	}

	@Override
	public void startScroll() {
		command(Command.ACTIVATE_SCROLL);
	}

	@Override
	public void stopScroll() {
		command(Command.DEACTIVATE_SCROLL);
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
