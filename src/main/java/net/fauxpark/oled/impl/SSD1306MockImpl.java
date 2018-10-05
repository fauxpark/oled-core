package net.fauxpark.oled.impl;

import net.fauxpark.oled.Command;
import net.fauxpark.oled.Constant;
import net.fauxpark.oled.SSD1306;

/**
 * A mock SSD1306 driver, for use when testing on platforms other than the Raspberry Pi.
 *
 * @author fauxpark
 */
public class SSD1306MockImpl extends SSD1306 {
	/**
	 * SSD1306MockImpl constructor.
	 *
	 * @param width The width of the display in pixels.
	 * @param height The height of the display in pixels.
	 */
	public SSD1306MockImpl(int width, int height) {
		super(width, height);
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
		super.shutdown();
	}

	@Override
	public void reset() {}

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
		super.setDisplayOn(displayOn);

		if(displayOn) {
			command(Command.DISPLAY_ON);
		} else {
			command(Command.DISPLAY_OFF);
		}
	}

	@Override
	public void setInverted(boolean inverted) {
		super.setInverted(inverted);
		command(inverted ? Command.INVERT_DISPLAY : Command.NORMAL_DISPLAY);
	}

	@Override
	public void setContrast(int contrast) {
		super.setContrast(contrast);
		command(Command.SET_CONTRAST, getContrast());
	}

	@Override
	public void setOffset(int offset) {
		super.setOffset(offset);
		command(Command.SET_DISPLAY_OFFSET, getOffset());
	}

	@Override
	public void setHFlipped(boolean hFlipped) {
		super.setHFlipped(hFlipped);

		if(hFlipped) {
			command(Command.SET_SEGMENT_REMAP);
		} else {
			command(Command.SET_SEGMENT_REMAP_REVERSE);
		}

		// Horizontal flipping is not immediate
		display();
	}

	@Override
	public void setVFlipped(boolean vFlipped) {
		super.setVFlipped(vFlipped);

		if(vFlipped) {
			command(Command.SET_COM_SCAN_INC);
		} else {
			command(Command.SET_COM_SCAN_DEC);
		}
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
		super.startScroll();
		command(Command.ACTIVATE_SCROLL);
	}

	@Override
	public void stopScroll() {
		super.stopScroll();
		command(Command.DEACTIVATE_SCROLL);
	}

	@Override
	public void noOp() {
		command(Command.NOOP);
	}

	@Override
	public void command(int command, int... params) {}

	@Override
	public void data(byte[] data) {}
}
