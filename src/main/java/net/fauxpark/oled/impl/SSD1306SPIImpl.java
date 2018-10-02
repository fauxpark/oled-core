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
