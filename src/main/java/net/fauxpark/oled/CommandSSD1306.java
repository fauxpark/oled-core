package net.fauxpark.oled;

/**
 * commands specific to SSD1306
 */
public class CommandSSD1306 extends CommandSSD {
    /**
     * Set the column start and end address of the display.
     */
    public final int SET_COLUMN_ADDRESS                   = 0x21;

    /**
     * Set the page start and end address of the display.
     */
    public final int SET_PAGE_ADDRESS                     = 0x22;


    /**
     * Set the page start address for page addressing mode.
     */
    public final int SET_PAGE_START_ADDR_0                = 0xB0;
    public final int SET_PAGE_START_ADDR_1                = 0xB1;
    public final int SET_PAGE_START_ADDR_2                = 0xB2;
    public final int SET_PAGE_START_ADDR_3                = 0xB3;
    public final int SET_PAGE_START_ADDR_4                = 0xB4;
    public final int SET_PAGE_START_ADDR_5                = 0xB5;
    public final int SET_PAGE_START_ADDR_6                = 0xB6;
    public final int SET_PAGE_START_ADDR_7                = 0xB7;



    /**
     * Set the starting row of the display buffer, from 0 to 63.
     */
    public final int SET_START_LINE_00                    = 0x40;
    public final int SET_START_LINE_01                    = 0x41;
    public final int SET_START_LINE_02                    = 0x42;
    public final int SET_START_LINE_03                    = 0x43;
    public final int SET_START_LINE_04                    = 0x44;
    public final int SET_START_LINE_05                    = 0x45;
    public final int SET_START_LINE_06                    = 0x46;
    public final int SET_START_LINE_07                    = 0x47;
    public final int SET_START_LINE_08                    = 0x48;
    public final int SET_START_LINE_09                    = 0x49;
    public final int SET_START_LINE_10                    = 0x4A;
    public final int SET_START_LINE_11                    = 0x4B;
    public final int SET_START_LINE_12                    = 0x4C;
    public final int SET_START_LINE_13                    = 0x4D;
    public final int SET_START_LINE_14                    = 0x4E;
    public final int SET_START_LINE_15                    = 0x4F;
    public final int SET_START_LINE_16                    = 0x50;
    public final int SET_START_LINE_17                    = 0x51;
    public final int SET_START_LINE_18                    = 0x52;
    public final int SET_START_LINE_19                    = 0x53;
    public final int SET_START_LINE_20                    = 0x54;
    public final int SET_START_LINE_21                    = 0x55;
    public final int SET_START_LINE_22                    = 0x56;
    public final int SET_START_LINE_23                    = 0x57;
    public final int SET_START_LINE_24                    = 0x58;
    public final int SET_START_LINE_25                    = 0x59;
    public final int SET_START_LINE_26                    = 0x5A;
    public final int SET_START_LINE_27                    = 0x5B;
    public final int SET_START_LINE_28                    = 0x5C;
    public final int SET_START_LINE_29                    = 0x5D;
    public final int SET_START_LINE_30                    = 0x5E;
    public final int SET_START_LINE_31                    = 0x5F;
    public final int SET_START_LINE_32                    = 0x60;
    public final int SET_START_LINE_33                    = 0x61;
    public final int SET_START_LINE_34                    = 0x62;
    public final int SET_START_LINE_35                    = 0x63;
    public final int SET_START_LINE_36                    = 0x64;
    public final int SET_START_LINE_37                    = 0x65;
    public final int SET_START_LINE_38                    = 0x66;
    public final int SET_START_LINE_39                    = 0x67;
    public final int SET_START_LINE_40                    = 0x68;
    public final int SET_START_LINE_41                    = 0x69;
    public final int SET_START_LINE_42                    = 0x6A;
    public final int SET_START_LINE_43                    = 0x6B;
    public final int SET_START_LINE_44                    = 0x6C;
    public final int SET_START_LINE_45                    = 0x6D;
    public final int SET_START_LINE_46                    = 0x6E;
    public final int SET_START_LINE_47                    = 0x6F;
    public final int SET_START_LINE_48                    = 0x70;
    public final int SET_START_LINE_49                    = 0x71;
    public final int SET_START_LINE_50                    = 0x72;
    public final int SET_START_LINE_51                    = 0x73;
    public final int SET_START_LINE_52                    = 0x74;
    public final int SET_START_LINE_53                    = 0x75;
    public final int SET_START_LINE_54                    = 0x76;
    public final int SET_START_LINE_55                    = 0x77;
    public final int SET_START_LINE_56                    = 0x78;
    public final int SET_START_LINE_57                    = 0x79;
    public final int SET_START_LINE_58                    = 0x7A;
    public final int SET_START_LINE_59                    = 0x7B;
    public final int SET_START_LINE_60                    = 0x7C;
    public final int SET_START_LINE_61                    = 0x7D;
    public final int SET_START_LINE_62                    = 0x7E;
    public final int SET_START_LINE_63                    = 0x7F;
}
