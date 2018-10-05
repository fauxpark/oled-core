package net.fauxpark.oled;

/**
 * This class defines the commands that can be sent to the SSDisplay.
 * Some of them are standalone commands and others require arguments following them.
 * <br/>
 * Please refer to <a href="https://www.adafruit.com/datasheets/SSD1306.pdf">the SSDisplay datasheet</a>
 * for more information.
 *
 * @author fauxpark
 */
public class CommandSSD {
	/**
	 * Set the lower column start address for page addressing mode.
	 */
	public final int SET_LOWER_COL_START_0                = 0x00;
	public final int SET_LOWER_COL_START_1                = 0x01;
	public final int SET_LOWER_COL_START_2                = 0x02;
	public final int SET_LOWER_COL_START_3                = 0x03;
	public final int SET_LOWER_COL_START_4                = 0x04;
	public final int SET_LOWER_COL_START_5                = 0x05;
	public final int SET_LOWER_COL_START_6                = 0x06;
	public final int SET_LOWER_COL_START_7                = 0x07;
	public final int SET_LOWER_COL_START_8                = 0x08;
	public final int SET_LOWER_COL_START_9                = 0x09;
	public final int SET_LOWER_COL_START_A                = 0x0A;
	public final int SET_LOWER_COL_START_B                = 0x0B;
	public final int SET_LOWER_COL_START_C                = 0x0C;
	public final int SET_LOWER_COL_START_D                = 0x0D;
	public final int SET_LOWER_COL_START_E                = 0x0E;
	public final int SET_LOWER_COL_START_F                = 0x0F;

	/**
	 * Set the higher column start address for page addressing mode.
	 */
	public final int SET_HIGHER_COL_START_0               = 0x10;
	public final int SET_HIGHER_COL_START_1               = 0x11;
	public final int SET_HIGHER_COL_START_2               = 0x12;
	public final int SET_HIGHER_COL_START_3               = 0x13;
	public final int SET_HIGHER_COL_START_4               = 0x14;
	public final int SET_HIGHER_COL_START_5               = 0x15;
	public final int SET_HIGHER_COL_START_6               = 0x16;
	public final int SET_HIGHER_COL_START_7               = 0x17;
	public final int SET_HIGHER_COL_START_8               = 0x18;
	public final int SET_HIGHER_COL_START_9               = 0x19;
	public final int SET_HIGHER_COL_START_A               = 0x1A;
	public final int SET_HIGHER_COL_START_B               = 0x1B;
	public final int SET_HIGHER_COL_START_C               = 0x1C;
	public final int SET_HIGHER_COL_START_D               = 0x1D;
	public final int SET_HIGHER_COL_START_E               = 0x1E;
	public final int SET_HIGHER_COL_START_F               = 0x1F;

	/**
	 * Set the memory addressing mode.
	 *
	 * @see Constant#MEMORY_MODE_HORIZONTAL
	 * @see Constant#MEMORY_MODE_VERTICAL
	 * @see Constant#MEMORY_MODE_PAGE
	 */
	public final int SET_MEMORY_MODE                      = 0x20;


	/**
	 * Set the display to scroll to the right.
	 */
	public final int RIGHT_HORIZONTAL_SCROLL              = 0x26;

	/**
	 * Set the display to scroll to the left.
	 */
	public final int LEFT_HORIZONTAL_SCROLL               = 0x27;

	/**
	 * Set the display to scroll vertically and to the right.
	 */
	public final int VERTICAL_AND_RIGHT_HORIZONTAL_SCROLL = 0x29;

	/**
	 * Set the display to scroll vertically and to the left.
	 */
	public final int VERTICAL_AND_LEFT_HORIZONTAL_SCROLL  = 0x2A;

	/**
	 * Turn off scrolling of the display.
	 */
	public final int DEACTIVATE_SCROLL                    = 0x2E;

	/**
	 * Turn on scrolling of the display.
	 */
	public final int ACTIVATE_SCROLL                      = 0x2F;



	/**
	 * Set the contrast of the display.
	 */
	public final int SET_CONTRAST                         = 0x81;

	/**
	 * Sets the charge pump regulator state.
	 *
	 * @see Constant#CHARGE_PUMP_DISABLE
	 * @see Constant#CHARGE_PUMP_ENABLE
	 */
	public final int SET_CHARGE_PUMP                      = 0x8D;

	/**
	 * Map column address 0 to SEG0.
	 * This command is used for horizontally flipping the display.
	 */
	public final int SET_SEGMENT_REMAP                    = 0xA0;

	/**
	 * Map column address 127 to SEG0.
	 * This command is used for horizontally flipping the display.
	 */
	public final int SET_SEGMENT_REMAP_REVERSE            = 0xA1;

	/**
	 * Set the offset and number of rows in the vertical scrolling area.
	 */
	public final int SET_VERTICAL_SCROLL_AREA             = 0xA3;

	/**
	 * Turn on the display with the buffer contents.
	 */
	public final int DISPLAY_MODE_NORMAL = 0xA4;

	/**
	 * Turn on the entire display, ignoring the buffer contents.
	 */
	public final int DISPLAY_MODE_ALL_ON = 0xA5;

	/**
	 * Set the display to normal mode, where a 1 in the buffer represents a lit pixel.
	 */
	public final int DISPLAY_MODE_ALL_OFF = 0xA6;

	/**
	 * Set the display to inverse mode, where a 1 in the buffer represents an unlit pixel.
	 */
	public final int DISPLAY_MODE_INVERT = 0xA7;

	/**
	 * Set the multiplex ratio of the display.
	 */
	public final int SET_MULTIPLEX_RATIO                  = 0xA8;


	/**
	 * Turn the display off (sleep mode).
	 */
	public final int DISPLAY_OFF                          = 0xAE;

	/**
	 * Turn the display on.
	 */
	public final int DISPLAY_ON                           = 0xAF;



	/**
	 * Set the row output scan direction from COM0 to COM63.
	 * This command is used for vertically flipping the display.
	 */
	public final int SET_COM_SCAN_INC                     = 0xC0;

	/**
	 * Set the row output scan direction from COM63 to COM0.
	 * This command is used for vertically flipping the display.
	 */
	public final int SET_COM_SCAN_DEC                     = 0xC8;

	/**
	 * Set the display offset.
	 * Maps the display start line to the specified row.
	 */
	public final int SET_DISPLAY_OFFSET                   = 0xD3;

	/**
	 * Set the display clock divide ratio and oscillator frequency.
	 * The divide ratio makes up the lower four bits.
	 */
	public final int SET_DISPLAY_CLOCK_DIV                = 0xD5;

	/**
	 * Set the duration of the pre-charge period.
	 */
	public final int SET_PRECHARGE_PERIOD                 = 0xD9;

	/**
	 * Set hardware configuration of COM pins.
	 */
	public final int SET_COM_PINS                         = 0xDA;

	/**
	 * Adjust the <code>V<sub>COMH</sub></code> regulator output.
	 *
	 * @see Constant#VCOMH_DESELECT_LEVEL_00
	 * @see Constant#VCOMH_DESELECT_LEVEL_20
	 * @see Constant#VCOMH_DESELECT_LEVEL_30
	 */
	public final int SET_VCOMH_DESELECT                   = 0xDB;

	/**
	 * No operation.
	 */
	public final int NOOP                                 = 0xE3;
}
