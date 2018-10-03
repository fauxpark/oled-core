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
public class Command {
	/**
	 * Set the lower column start address for page addressing mode.
	 */
	public static final int SET_LOWER_COL_START_0                = 0x00;
	public static final int SET_LOWER_COL_START_1                = 0x01;
	public static final int SET_LOWER_COL_START_2                = 0x02;
	public static final int SET_LOWER_COL_START_3                = 0x03;
	public static final int SET_LOWER_COL_START_4                = 0x04;
	public static final int SET_LOWER_COL_START_5                = 0x05;
	public static final int SET_LOWER_COL_START_6                = 0x06;
	public static final int SET_LOWER_COL_START_7                = 0x07;
	public static final int SET_LOWER_COL_START_8                = 0x08;
	public static final int SET_LOWER_COL_START_9                = 0x09;
	public static final int SET_LOWER_COL_START_A                = 0x0A;
	public static final int SET_LOWER_COL_START_B                = 0x0B;
	public static final int SET_LOWER_COL_START_C                = 0x0C;
	public static final int SET_LOWER_COL_START_D                = 0x0D;
	public static final int SET_LOWER_COL_START_E                = 0x0E;
	public static final int SET_LOWER_COL_START_F                = 0x0F;

	/**
	 * Set the higher column start address for page addressing mode.
	 */
	public static final int SET_HIGHER_COL_START_0               = 0x10;
	public static final int SET_HIGHER_COL_START_1               = 0x11;
	public static final int SET_HIGHER_COL_START_2               = 0x12;
	public static final int SET_HIGHER_COL_START_3               = 0x13;
	public static final int SET_HIGHER_COL_START_4               = 0x14;
	public static final int SET_HIGHER_COL_START_5               = 0x15;
	public static final int SET_HIGHER_COL_START_6               = 0x16;
	public static final int SET_HIGHER_COL_START_7               = 0x17;
	public static final int SET_HIGHER_COL_START_8               = 0x18;
	public static final int SET_HIGHER_COL_START_9               = 0x19;
	public static final int SET_HIGHER_COL_START_A               = 0x1A;
	public static final int SET_HIGHER_COL_START_B               = 0x1B;
	public static final int SET_HIGHER_COL_START_C               = 0x1C;
	public static final int SET_HIGHER_COL_START_D               = 0x1D;
	public static final int SET_HIGHER_COL_START_E               = 0x1E;
	public static final int SET_HIGHER_COL_START_F               = 0x1F;

	/**
	 * Set the memory addressing mode.
	 *
	 * @see Constant#MEMORY_MODE_HORIZONTAL
	 * @see Constant#MEMORY_MODE_VERTICAL
	 * @see Constant#MEMORY_MODE_PAGE
	 */
	public static final int SET_MEMORY_MODE                      = 0x20;


	/**
	 * Set the display to scroll to the right.
	 */
	public static final int RIGHT_HORIZONTAL_SCROLL              = 0x26;

	/**
	 * Set the display to scroll to the left.
	 */
	public static final int LEFT_HORIZONTAL_SCROLL               = 0x27;

	/**
	 * Set the display to scroll vertically and to the right.
	 */
	public static final int VERTICAL_AND_RIGHT_HORIZONTAL_SCROLL = 0x29;

	/**
	 * Set the display to scroll vertically and to the left.
	 */
	public static final int VERTICAL_AND_LEFT_HORIZONTAL_SCROLL  = 0x2A;

	/**
	 * Turn off scrolling of the display.
	 */
	public static final int DEACTIVATE_SCROLL                    = 0x2E;

	/**
	 * Turn on scrolling of the display.
	 */
	public static final int ACTIVATE_SCROLL                      = 0x2F;

	/**
	 * Set the starting row of the display buffer, from 0 to 63.
	 */
	public static final int SET_START_LINE_00                    = 0x40;
	public static final int SET_START_LINE_01                    = 0x41;
	public static final int SET_START_LINE_02                    = 0x42;
	public static final int SET_START_LINE_03                    = 0x43;
	public static final int SET_START_LINE_04                    = 0x44;
	public static final int SET_START_LINE_05                    = 0x45;
	public static final int SET_START_LINE_06                    = 0x46;
	public static final int SET_START_LINE_07                    = 0x47;
	public static final int SET_START_LINE_08                    = 0x48;
	public static final int SET_START_LINE_09                    = 0x49;
	public static final int SET_START_LINE_10                    = 0x4A;
	public static final int SET_START_LINE_11                    = 0x4B;
	public static final int SET_START_LINE_12                    = 0x4C;
	public static final int SET_START_LINE_13                    = 0x4D;
	public static final int SET_START_LINE_14                    = 0x4E;
	public static final int SET_START_LINE_15                    = 0x4F;
	public static final int SET_START_LINE_16                    = 0x50;
	public static final int SET_START_LINE_17                    = 0x51;
	public static final int SET_START_LINE_18                    = 0x52;
	public static final int SET_START_LINE_19                    = 0x53;
	public static final int SET_START_LINE_20                    = 0x54;
	public static final int SET_START_LINE_21                    = 0x55;
	public static final int SET_START_LINE_22                    = 0x56;
	public static final int SET_START_LINE_23                    = 0x57;
	public static final int SET_START_LINE_24                    = 0x58;
	public static final int SET_START_LINE_25                    = 0x59;
	public static final int SET_START_LINE_26                    = 0x5A;
	public static final int SET_START_LINE_27                    = 0x5B;
	public static final int SET_START_LINE_28                    = 0x5C;
	public static final int SET_START_LINE_29                    = 0x5D;
	public static final int SET_START_LINE_30                    = 0x5E;
	public static final int SET_START_LINE_31                    = 0x5F;
	public static final int SET_START_LINE_32                    = 0x60;
	public static final int SET_START_LINE_33                    = 0x61;
	public static final int SET_START_LINE_34                    = 0x62;
	public static final int SET_START_LINE_35                    = 0x63;
	public static final int SET_START_LINE_36                    = 0x64;
	public static final int SET_START_LINE_37                    = 0x65;
	public static final int SET_START_LINE_38                    = 0x66;
	public static final int SET_START_LINE_39                    = 0x67;
	public static final int SET_START_LINE_40                    = 0x68;
	public static final int SET_START_LINE_41                    = 0x69;
	public static final int SET_START_LINE_42                    = 0x6A;
	public static final int SET_START_LINE_43                    = 0x6B;
	public static final int SET_START_LINE_44                    = 0x6C;
	public static final int SET_START_LINE_45                    = 0x6D;
	public static final int SET_START_LINE_46                    = 0x6E;
	public static final int SET_START_LINE_47                    = 0x6F;
	public static final int SET_START_LINE_48                    = 0x70;
	public static final int SET_START_LINE_49                    = 0x71;
	public static final int SET_START_LINE_50                    = 0x72;
	public static final int SET_START_LINE_51                    = 0x73;
	public static final int SET_START_LINE_52                    = 0x74;
	public static final int SET_START_LINE_53                    = 0x75;
	public static final int SET_START_LINE_54                    = 0x76;
	public static final int SET_START_LINE_55                    = 0x77;
	public static final int SET_START_LINE_56                    = 0x78;
	public static final int SET_START_LINE_57                    = 0x79;
	public static final int SET_START_LINE_58                    = 0x7A;
	public static final int SET_START_LINE_59                    = 0x7B;
	public static final int SET_START_LINE_60                    = 0x7C;
	public static final int SET_START_LINE_61                    = 0x7D;
	public static final int SET_START_LINE_62                    = 0x7E;
	public static final int SET_START_LINE_63                    = 0x7F;

	/**
	 * Set the contrast of the display.
	 */
	public static final int SET_CONTRAST                         = 0x81;

	/**
	 * Sets the charge pump regulator state.
	 *
	 * @see Constant#CHARGE_PUMP_DISABLE
	 * @see Constant#CHARGE_PUMP_ENABLE
	 */
	public static final int SET_CHARGE_PUMP                      = 0x8D;

	/**
	 * Map column address 0 to SEG0.
	 * This command is used for horizontally flipping the display.
	 */
	public static final int SET_SEGMENT_REMAP                    = 0xA0;

	/**
	 * Map column address 127 to SEG0.
	 * This command is used for horizontally flipping the display.
	 */
	public static final int SET_SEGMENT_REMAP_REVERSE            = 0xA1;

	/**
	 * Set the offset and number of rows in the vertical scrolling area.
	 */
	public static final int SET_VERTICAL_SCROLL_AREA             = 0xA3;

	/**
	 * Turn on the display with the buffer contents.
	 */
	public static final int DISPLAY_ALL_ON_RESUME                = 0xA4;

	/**
	 * Turn on the entire display, ignoring the buffer contents.
	 */
	public static final int DISPLAY_ALL_ON                       = 0xA5;

	/**
	 * Set the display to normal mode, where a 1 in the buffer represents a lit pixel.
	 */
	public static final int NORMAL_DISPLAY                       = 0xA6;

	/**
	 * Set the display to inverse mode, where a 1 in the buffer represents an unlit pixel.
	 */
	public static final int INVERT_DISPLAY                       = 0xA7;

	/**
	 * Set the multiplex ratio of the display.
	 */
	public static final int SET_MULTIPLEX_RATIO                  = 0xA8;

	/**
	 * Turn the display off (sleep mode).
	 */
	public static final int DISPLAY_OFF                          = 0xAE;

	/**
	 * Turn the display on.
	 */
	public static final int DISPLAY_ON                           = 0xAF;



	/**
	 * Set the row output scan direction from COM0 to COM63.
	 * This command is used for vertically flipping the display.
	 */
	public static final int SET_COM_SCAN_INC                     = 0xC0;

	/**
	 * Set the row output scan direction from COM63 to COM0.
	 * This command is used for vertically flipping the display.
	 */
	public static final int SET_COM_SCAN_DEC                     = 0xC8;

	/**
	 * Set the display offset.
	 * Maps the display start line to the specified row.
	 */
	public static final int SET_DISPLAY_OFFSET                   = 0xD3;

	/**
	 * Set the display clock divide ratio and oscillator frequency.
	 * The divide ratio makes up the lower four bits.
	 */
	public static final int SET_DISPLAY_CLOCK_DIV                = 0xD5;

	/**
	 * Set the duration of the pre-charge period.
	 */
	public static final int SET_PRECHARGE_PERIOD                 = 0xD9;

	/**
	 * Set hardware configuration of COM pins.
	 */
	public static final int SET_COM_PINS                         = 0xDA;

	/**
	 * Adjust the <code>V<sub>COMH</sub></code> regulator output.
	 *
	 * @see Constant#VCOMH_DESELECT_LEVEL_00
	 * @see Constant#VCOMH_DESELECT_LEVEL_20
	 * @see Constant#VCOMH_DESELECT_LEVEL_30
	 */
	public static final int SET_VCOMH_DESELECT                   = 0xDB;

	/**
	 * No operation.
	 */
	public static final int NOOP                                 = 0xE3;
}
