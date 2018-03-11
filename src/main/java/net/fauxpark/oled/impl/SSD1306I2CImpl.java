package net.fauxpark.oled.impl;

import java.io.IOException;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

import net.fauxpark.oled.Command;
import net.fauxpark.oled.Constant;
import net.fauxpark.oled.SSD1306;

/**
 * A simple I<sup>2</sup>C driver for the Adafruit SSD1306 OLED display.
 *
 * @author fauxpark
 */
public class SSD1306I2CImpl extends SSD1306 {
	/**
	 * The internal GPIO instance.
	 */
	private GpioController gpio;

	/**
	 * The GPIO pin corresponding to the RST line on the display.
	 */
	private GpioPinDigitalOutput rstPin;

	/**
	 * The internal I<sup>2</sup>C device.
	 */
	private I2CDevice i2c;

	/**
	 * The Data/Command bit position.
	 */
	private static final int DC_BIT = 6;

	/**
	 * SSD1306I2CImpl constructor.
	 *
	 * @param width The width of the display in pixels.
	 * @param height The height of the display in pixels.
	 * @param rstPin The GPIO pin to use for the RST line.
	 * @param bus The I<sup>2</sup>C bus to use.
	 * @param address The I<sup>2</sup>C address of the display.
	 */
	public SSD1306I2CImpl(int width, int height, Pin rstPin, int bus, int address) {
		super(width, height);
		gpio = GpioFactory.getInstance();
		this.rstPin = gpio.provisionDigitalOutputPin(rstPin);

		try {
			i2c = I2CFactory.getInstance(bus).getDevice(address);
		} catch(IOException | UnsupportedBusNumberException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void startup(boolean externalVcc) {
		reset();
		setDisplayOn(false);
		command(Command.SET_DISPLAY_CLOCK_DIV, width);
		command(Command.SET_MULTIPLEX_RATIO, width - 1);
		setOffset(0);
		command(Command.SET_START_LINE_00);
		command(Command.SET_CHARGE_PUMP, externalVcc ? Constant.CHARGE_PUMP_DISABLE : Constant.CHARGE_PUMP_ENABLE);
		command(Command.SET_MEMORY_MODE, Constant.MEMORY_MODE_HORIZONTAL);
		setHFlipped(false);
		setVFlipped(false);
		command(Command.SET_COM_PINS, height == 64 ? 0x12 : 0x02);
		setContrast(externalVcc ? 0x9F : 0xCF);
		command(Command.SET_PRECHARGE_PERIOD, externalVcc ? 0x22 : 0xF1);
		command(Command.SET_VCOMH_DESELECT, Constant.VCOMH_DESELECT_LEVEL_00);
		command(Command.DISPLAY_ALL_ON_RESUME);
		setInverted(false);
		setDisplayOn(true);
		clear();
		display();
		super.startup(externalVcc);
	}

	@Override
	public void shutdown() {
		clear();
		display();
		setDisplayOn(false);
		reset();
		gpio.shutdown();
		super.shutdown();
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

		// Jump start scrolling again if new data is written while enabled
		if(isScrolling()) {
			noOp();
		}
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
		byte[] commandBytes = new byte[params.length + 2];
		commandBytes[0] = (byte) (0 << DC_BIT);
		commandBytes[1] = (byte) command;

		for(int i = 0; i < params.length; i++) {
			commandBytes[i + 2] = (byte) params[i];
		}

		try {
			i2c.write(commandBytes);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void data(byte[] data) {
		byte[] dataBytes = new byte[data.length + 1];
		dataBytes[0] = (byte) (1 << DC_BIT);

		for(int i = 0; i < data.length; i++) {
			dataBytes[i + 1] = data[i];
		}

		try {
			i2c.write(dataBytes);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
