package net.fauxpark.oled;

/**
 * This class defines some useful constants, such as memory addressing modes, scrolling speeds and dummy bytes.
 *
 * @author fauxpark
 */
public class Constant {
	/**
	 * A dummy byte consisting of all zeroes.
	 */
	public static final int DUMMY_BYTE_00           = 0x00;

	/**
	 * A dummy byte consisting of all ones.
	 */
	public static final int DUMMY_BYTE_FF           = 0xFF;

	/**
	 * Horizontal memory addressing mode.
	 * In this mode, after reading/writing the display RAM, the column address pointer is incremented.
	 * When the pointer reaches the end, it is reset to the start address on the next page.
	 */
	public static final int MEMORY_MODE_HORIZONTAL  = 0x00;

	/**
	 * Vertical memory addressing mode.
	 * In this mode, after reading/writing the display RAM, the page address pointer is incremented.
	 * When the pointer reaches the end, it is reset to the start address on the next column.
	 */
	public static final int MEMORY_MODE_VERTICAL    = 0x01;

	/**
	 * Page memory addressing mode.
	 * In this mode, after reading/writing the display RAM, the column address pointer is incremented.
	 * When the pointer reaches the end, it is reset to the start address on the same page.
	 */
	public static final int MEMORY_MODE_PAGE        = 0x02;

	/**
	 * Disable the charge pump regulator.
	 */
	public static final int CHARGE_PUMP_DISABLE     = 0x10;

	/**
	 * Enable the charge pump regulator.
	 */
	public static final int CHARGE_PUMP_ENABLE      = 0x14;

	/**
	 * A VCOMH deselect level of ~0.65 &times; <code>V<sub>CC</sub></code>.
	 */
	public static final int VCOMH_DESELECT_LEVEL_00 = 0x00;

	/**
	 * A VCOMH deselect level of ~0.77 &times; <code>V<sub>CC</sub></code>.
	 */
	public static final int VCOMH_DESELECT_LEVEL_20 = 0x20;

	/**
	 * A VCOMH deselect level of ~0.83 &times; <code>V<sub>CC</sub></code>.
	 */
	public static final int VCOMH_DESELECT_LEVEL_30 = 0x30;

	/**
	 * Scroll by one pixel every 5 frames.
	 */
	public static final int SCROLL_STEP_5           = 0x00;
	public static final int SCROLL_STEP_64          = 0x01;
	public static final int SCROLL_STEP_128         = 0x02;
	public static final int SCROLL_STEP_256         = 0x03;
	public static final int SCROLL_STEP_3           = 0x04;
	public static final int SCROLL_STEP_4           = 0x05;
	public static final int SCROLL_STEP_25          = 0x06;
	public static final int SCROLL_STEP_2           = 0x07;
}
