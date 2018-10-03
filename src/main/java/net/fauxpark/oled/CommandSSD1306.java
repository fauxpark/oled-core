package net.fauxpark.oled;

/**
 * commands specific to SSD1306
 */
public class CommandSSD1306 extends Command {
    /**
     * Set the column start and end address of the display.
     */
    public static final int SET_COLUMN_ADDRESS                   = 0x21;

    /**
     * Set the page start and end address of the display.
     */
    public static final int SET_PAGE_ADDRESS                     = 0x22;


    /**
     * Set the page start address for page addressing mode.
     */
    public static final int SET_PAGE_START_ADDR_0                = 0xB0;
    public static final int SET_PAGE_START_ADDR_1                = 0xB1;
    public static final int SET_PAGE_START_ADDR_2                = 0xB2;
    public static final int SET_PAGE_START_ADDR_3                = 0xB3;
    public static final int SET_PAGE_START_ADDR_4                = 0xB4;
    public static final int SET_PAGE_START_ADDR_5                = 0xB5;
    public static final int SET_PAGE_START_ADDR_6                = 0xB6;
    public static final int SET_PAGE_START_ADDR_7                = 0xB7;
}
