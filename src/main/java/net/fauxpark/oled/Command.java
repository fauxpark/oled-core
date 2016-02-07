package net.fauxpark.oled;

/**
 * This class defines the commands that can be sent to the SSD1306.
 * Some of them are standalone commands and others require arguments following them.
 * <br/>
 * Please refer to <a href="https://www.adafruit.com/datasheets/SSD1306.pdf">the SSD1306 datasheet</a>
 * for more information.
 *
 * @author fauxpark
 */
public class Command {
	/**
	 * Set the lower column start address for page addressing mode.
	 */
	public static final int SET_LOWER_COL_START_00               = 0x00;
	public static final int SET_LOWER_COL_START_01               = 0x01;
	public static final int SET_LOWER_COL_START_02               = 0x02;
	public static final int SET_LOWER_COL_START_03               = 0x03;
	public static final int SET_LOWER_COL_START_04               = 0x04;
	public static final int SET_LOWER_COL_START_05               = 0x05;
	public static final int SET_LOWER_COL_START_06               = 0x06;
	public static final int SET_LOWER_COL_START_07               = 0x07;
	public static final int SET_LOWER_COL_START_08               = 0x08;
	public static final int SET_LOWER_COL_START_09               = 0x09;
	public static final int SET_LOWER_COL_START_0A               = 0x0A;
	public static final int SET_LOWER_COL_START_0B               = 0x0B;
	public static final int SET_LOWER_COL_START_0C               = 0x0C;
	public static final int SET_LOWER_COL_START_0D               = 0x0D;
	public static final int SET_LOWER_COL_START_0E               = 0x0E;
	public static final int SET_LOWER_COL_START_0F               = 0x0F;

	/**
	 * Set the higher column start address for page addressing mode.
	 */
	public static final int SET_HIGHER_COL_START_10              = 0x10;
	public static final int SET_HIGHER_COL_START_11              = 0x11;
	public static final int SET_HIGHER_COL_START_12              = 0x12;
	public static final int SET_HIGHER_COL_START_13              = 0x13;
	public static final int SET_HIGHER_COL_START_14              = 0x14;
	public static final int SET_HIGHER_COL_START_15              = 0x15;
	public static final int SET_HIGHER_COL_START_16              = 0x16;
	public static final int SET_HIGHER_COL_START_17              = 0x17;
	public static final int SET_HIGHER_COL_START_18              = 0x18;
	public static final int SET_HIGHER_COL_START_19              = 0x19;
	public static final int SET_HIGHER_COL_START_1A              = 0x1A;
	public static final int SET_HIGHER_COL_START_1B              = 0x1B;
	public static final int SET_HIGHER_COL_START_1C              = 0x1C;
	public static final int SET_HIGHER_COL_START_1D              = 0x1D;
	public static final int SET_HIGHER_COL_START_1E              = 0x1E;
	public static final int SET_HIGHER_COL_START_1F              = 0x1F;

	/**
	 * Set the memory addressing mode.
	 *
	 * @see Constant#MEMORY_MODE_HORIZONTAL
	 * @see Constant#MEMORY_MODE_VERTICAL
	 * @see Constant#MEMORY_MODE_PAGE
	 */
	public static final int SET_MEMORY_MODE                      = 0x20;

	/**
	 * Set the column start and end address of the display.
	 */
	public static final int SET_COLUMN_ADDRESS                   = 0x21;

	/**
	 * Set the page start and end address of the display.
	 */
	public static final int SET_PAGE_ADDRESS                     = 0x22;

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
	 * Set the starting address of the display buffer.
	 */
	public static final int SET_START_LINE_40                    = 0x40;
	public static final int SET_START_LINE_41                    = 0x41;
	public static final int SET_START_LINE_42                    = 0x42;
	public static final int SET_START_LINE_43                    = 0x43;
	public static final int SET_START_LINE_44                    = 0x44;
	public static final int SET_START_LINE_45                    = 0x45;
	public static final int SET_START_LINE_46                    = 0x46;
	public static final int SET_START_LINE_47                    = 0x47;
	public static final int SET_START_LINE_48                    = 0x48;
	public static final int SET_START_LINE_49                    = 0x49;
	public static final int SET_START_LINE_4A                    = 0x4A;
	public static final int SET_START_LINE_4B                    = 0x4B;
	public static final int SET_START_LINE_4C                    = 0x4C;
	public static final int SET_START_LINE_4D                    = 0x4D;
	public static final int SET_START_LINE_4E                    = 0x4E;
	public static final int SET_START_LINE_4F                    = 0x4F;
	public static final int SET_START_LINE_50                    = 0x50;
	public static final int SET_START_LINE_51                    = 0x51;
	public static final int SET_START_LINE_52                    = 0x52;
	public static final int SET_START_LINE_53                    = 0x53;
	public static final int SET_START_LINE_54                    = 0x54;
	public static final int SET_START_LINE_55                    = 0x55;
	public static final int SET_START_LINE_56                    = 0x56;
	public static final int SET_START_LINE_57                    = 0x57;
	public static final int SET_START_LINE_58                    = 0x58;
	public static final int SET_START_LINE_59                    = 0x59;
	public static final int SET_START_LINE_5A                    = 0x5A;
	public static final int SET_START_LINE_5B                    = 0x5B;
	public static final int SET_START_LINE_5C                    = 0x5C;
	public static final int SET_START_LINE_5D                    = 0x5D;
	public static final int SET_START_LINE_5E                    = 0x5E;
	public static final int SET_START_LINE_5F                    = 0x5F;
	public static final int SET_START_LINE_60                    = 0x60;
	public static final int SET_START_LINE_61                    = 0x61;
	public static final int SET_START_LINE_62                    = 0x62;
	public static final int SET_START_LINE_63                    = 0x63;
	public static final int SET_START_LINE_64                    = 0x64;
	public static final int SET_START_LINE_65                    = 0x65;
	public static final int SET_START_LINE_66                    = 0x66;
	public static final int SET_START_LINE_67                    = 0x67;
	public static final int SET_START_LINE_68                    = 0x68;
	public static final int SET_START_LINE_69                    = 0x69;
	public static final int SET_START_LINE_6A                    = 0x6A;
	public static final int SET_START_LINE_6B                    = 0x6B;
	public static final int SET_START_LINE_6C                    = 0x6C;
	public static final int SET_START_LINE_6D                    = 0x6D;
	public static final int SET_START_LINE_6E                    = 0x6E;
	public static final int SET_START_LINE_6F                    = 0x6F;
	public static final int SET_START_LINE_70                    = 0x70;
	public static final int SET_START_LINE_71                    = 0x71;
	public static final int SET_START_LINE_72                    = 0x72;
	public static final int SET_START_LINE_73                    = 0x73;
	public static final int SET_START_LINE_74                    = 0x74;
	public static final int SET_START_LINE_75                    = 0x75;
	public static final int SET_START_LINE_76                    = 0x76;
	public static final int SET_START_LINE_77                    = 0x77;
	public static final int SET_START_LINE_78                    = 0x78;
	public static final int SET_START_LINE_79                    = 0x79;
	public static final int SET_START_LINE_7A                    = 0x7A;
	public static final int SET_START_LINE_7B                    = 0x7B;
	public static final int SET_START_LINE_7C                    = 0x7C;
	public static final int SET_START_LINE_7D                    = 0x7D;
	public static final int SET_START_LINE_7E                    = 0x7E;
	public static final int SET_START_LINE_7F                    = 0x7F;

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
	 */
	public static final int SET_SEGMENT_REMAP                    = 0xA0;

	/**
	 * Map column address 127 to SEG0.
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
	 * Turn on the entire display.
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
	 * Set the page start address for page addressing mode.
	 */
	public static final int SET_PAGE_START_ADDR_B0               = 0xB0;
	public static final int SET_PAGE_START_ADDR_B1               = 0xB1;
	public static final int SET_PAGE_START_ADDR_B2               = 0xB2;
	public static final int SET_PAGE_START_ADDR_B3               = 0xB3;
	public static final int SET_PAGE_START_ADDR_B4               = 0xB4;
	public static final int SET_PAGE_START_ADDR_B5               = 0xB5;
	public static final int SET_PAGE_START_ADDR_B6               = 0xB6;
	public static final int SET_PAGE_START_ADDR_B7               = 0xB7;

	/**
	 * Set the COM output scan direction from COM0 to COM63.
	 * This command is used for vertically flipping the display.
	 */
	public static final int SET_COM_SCAN_INC                     = 0xC0;

	/**
	 * Set the COM output scan direction from COM63 to COM0.
	 * This command is used for vertically flipping the display.
	 */
	public static final int SET_COM_SCAN_DEC                     = 0xC8;

	/**
	 * Set the display offset.
	 * Maps the display start line to the specified common.
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
	public static final int SET_VCOM_DESELECT                    = 0xDB;

	/**
	 * No operation.
	 */
	public static final int NOOP                                 = 0xE3;
}
