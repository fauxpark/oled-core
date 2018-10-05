package net.fauxpark.oled;

/**
 * commands specific to SSD1327
 */
public class CommandSSD1327 extends CommandSSD {

    //public final int SET_REMAP                              = 0xA0;



    public final int SET_DISPLAY_START_LINE                 = 0xA1;

    public final int SET_DISPLAY_OFFSET                     = 0xA2;

    // Setup Column start and end address
    public final int SETUP_COLUMN_START_END_ADDRESS         = 0x15;

    // Setup Row start and end address
    public final int SETUP_ROW_START_END_ADDRESS            = 0x75;
}
