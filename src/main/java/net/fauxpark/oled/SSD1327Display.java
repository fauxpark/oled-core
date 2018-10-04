package net.fauxpark.oled;

import net.fauxpark.oled.conn.DisplayConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * specific methods for SSD1327 display
 *
 * 128 x 128 x 4 bit
 * buffer size is 8192 byte
 *
 * https://github.com/olikraus/u8g2/blob/master/csrc/u8x8_d_ssd1327.c
 */
public class SSD1327Display extends SSDisplay {
    private static final Logger logger = LoggerFactory.getLogger(SSD1327Display.class);

    public static final int WIDTH = 128;
    public static final int HEIGHT = 128;
    public static final int COLOR_BITS_PER_PIXEL = 4;

    int ROW_SIZE_IN_BYTES = WIDTH * COLOR_BITS_PER_PIXEL / 8;


    // public static final int DEFAULT_REMAP_CONFIG = 0b0;
    public static final int DEFAULT_REMAP_CONFIG = 0x51;

    public static int LOWER_NIBBLE_MASK = 0x0F;
    public static int HIGHER_NIBBLE_MASK = 0xF0;


    public CommandSSD1327 commandset = new CommandSSD1327();


    public SSD1327Display(DisplayConnection dspConn) {
        super(dspConn, WIDTH, HEIGHT);
        super.commandset = this.commandset;
    }


    /**
     * Startup specific to 1327
     *
     * @param externalVcc Indicates whether the display is being driven by an external power source.
     */
    public void startup(boolean externalVcc) throws IOException {
        logger.debug("startup");
        reset();

        setDisplayOn(false);
        setDisplayStartLine(0);
        setDisplayOffset(0x0);

        //setRemap(DEFAULT_REMAP_CONFIG);
        setRemap(false, false, false, false, false);

        resetColumnAndRowStartEndAddress();

        dspConn.command(commandset.DISPLAY_MODE_NORMAL);

        super.basicStartup(externalVcc);
    }

    private void resetColumnAndRowStartEndAddress() throws IOException {
        setColumnStartEndAddress(0, ROW_SIZE_IN_BYTES - 1);
        setRowStartEndAddress(0, HEIGHT - 1);
    }

    public void setDisplayStartLine(int line) throws IOException {
        if (line > 127) {
            throw new IllegalArgumentException("line not expected: " + line);
        }
        dspConn.command(commandset.SET_DISPLAY_START_LINE, line);
    }

    public void setDisplayOffset(int offset) throws IOException {
        dspConn.command(commandset.SET_DISPLAY_OFFSET, offset);
    }

    public void setColumnStartEndAddress(int start, int end) throws IOException {
        dspConn.command(commandset.SETUP_COLUMN_START_END_ADDRESS, start, end);
    }

    public void setRowStartEndAddress(int start, int end) throws IOException {
        dspConn.command(commandset.SETUP_ROW_START_END_ADDRESS, start, end);
    }

    /**
     * Column Address Remapping (A[0])
     * Nibble Remapping (A[1])
     * Address increment mode (A[2])
     * COM Remapping (A[4])
     * Splitting of Odd / Even COM Signals (A[6])
     *
     * 0x51 = 01010001
     * 0x42 = 01000010
     */
    public void setRemap(int remapConfig) throws IOException {
        dspConn.command(commandset.SET_SEGMENT_REMAP, remapConfig);
    }

    /**
     *
     * @param columnAddressRemapping
     * @param nibbleRemapping
     * @param addressIncrementMode vertical adress increment if true, horizontal (default) if false
     * @param comRemapping false: up to down, true: down to up
     * @param splittingOfOddEven
     * @throws IOException
     */
    public void setRemap(boolean columnAddressRemapping,
                         boolean nibbleRemapping,
                         boolean addressIncrementMode,
                         boolean comRemapping,
                         boolean splittingOfOddEven) throws IOException {

        int remapConfig = 0;
        if (columnAddressRemapping) {
            remapConfig |= (1 << 0);
        }
        if (nibbleRemapping) {
            remapConfig |= (1 << 1);
        }
        if (addressIncrementMode) {
            // vertical adress increment
            remapConfig |= (1 << 2);
        }
        if (comRemapping) {
            remapConfig |= (1 << 3);
        }
        if (splittingOfOddEven) {
            remapConfig |= (1 << 4);
        }

        setRemap(remapConfig);
    }


    /**
     * Set a pixel in the buffer.
     *
     * @param x The X position of the pixel to set.
     * @param y The Y position of the pixel to set.
     * @param on Whether to turn this pixel on or off.
     *
     * @return False if the given coordinates are out of bounds.
     */
    public boolean setPixel(int x, int y, boolean on) {
        if(x < 0 || x >= width || y < 0 || y >= height) {
            return false;
        }

        int arrayElement = getBufferArrayElementForPixel(x, y);
        int nibbleSelector = x & 1;
        int nibble = LOWER_NIBBLE_MASK;
        if (nibbleSelector > 0) {
            nibble = HIGHER_NIBBLE_MASK;
        }

        if(on) {
            buffer[arrayElement] |= nibble;
        } else {
            buffer[arrayElement] &= ~ nibble;
        }

        return true;
    }

    public static int getBufferArrayElementForPixel(int x, int y) {
        return x / 2 + y * WIDTH/2;
    }

    public byte[] getNewBuffer() {
        return new byte[width * height * COLOR_BITS_PER_PIXEL];
    }

    public int getColorBitsPerPixel() {
        return COLOR_BITS_PER_PIXEL;
    }

    @Override
    public CommandSSD1327 getCommandset() {
        return new CommandSSD1327();
    }

    /**
     * because of size have to output line by line
     * @throws IOException
     */
    @Override
    public synchronized void display() throws IOException {
        int tranferInSegments = 2;      // exception in PI4j if tranferring the whole bunch at once
        int rowsAtOneTime = height / tranferInSegments;

        resetColumnAndRowStartEndAddress();

        //byte [] rowData = new byte[rowSize];
        for (int row = 0; row<height; row+= rowsAtOneTime) {
            int rowStart = row * ROW_SIZE_IN_BYTES;
            //int rowEnd = (row + 1) * rowSize - 1;

            dspConn.data(buffer, rowStart, ROW_SIZE_IN_BYTES * rowsAtOneTime);
        }
    }
}
