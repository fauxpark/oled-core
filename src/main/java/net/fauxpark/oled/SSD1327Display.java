package net.fauxpark.oled;

import net.fauxpark.oled.conn.DisplayConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SSD1327Display extends SSDisplay {
    private static final Logger logger = LoggerFactory.getLogger(SSD1327Display.class);

    public static final int WIDTH = 128;
    public static final int HEIGHT = 128;
    public static final int COLOR_BITS_PER_PIXEL = 4;

    public static int LOWER_NIBBLE_MASK = 0x0F;
    public static int HIGHER_NIBBLE_MASK = 0xF0;



    public SSD1327Display(DisplayConnection dspConn) {
        super(dspConn, WIDTH, HEIGHT);
    }


    /**
     * Startup specific to 1306
     *
     * @param externalVcc Indicates whether the display is being driven by an external power source.
     */
    @Override
    public void startup(boolean externalVcc) throws IOException {
        logger.debug("startup");
        reset();
        setDisplayOn(false);

        dspConn.command(Command.SET_DISPLAY_CLOCK_DIV, width);  // from SPI
        // command(Command.SET_DISPLAY_CLOCK_DIV, 0x80);    // from I2C

        // TODO what´s this for?
        // command(Command.SET_MULTIPLEX_RATIO, width - 1);// from SPI
        dspConn.command(Command.SET_MULTIPLEX_RATIO, height == 64 ? 0x3F : 0x1F);// from I2C

        setOffset(0);
        dspConn.command(Command.SET_START_LINE_00);
        dspConn.command(Command.SET_CHARGE_PUMP, externalVcc ? Constant.CHARGE_PUMP_DISABLE : Constant.CHARGE_PUMP_ENABLE);
        dspConn.command(Command.SET_MEMORY_MODE, Constant.MEMORY_MODE_HORIZONTAL);
        setHFlipped(false);
        setVFlipped(false);
        dspConn.command(Command.SET_COM_PINS, height == 64 ? 0x12 : 0x02);

        setContrast(externalVcc ? 0x9F : 0xCF); // from SPI
        setContrast(height == 64 ? 0x8F : externalVcc ? 0x9F : 0xCF);   // from I2C

        dspConn.command(Command.SET_PRECHARGE_PERIOD, externalVcc ? 0x22 : 0xF1);
        dspConn.command(Command.SET_VCOMH_DESELECT, Constant.VCOMH_DESELECT_LEVEL_00);
        dspConn.command(Command.DISPLAY_ALL_ON_RESUME);

        super.startup(externalVcc);
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

        int arrayElement = x / 2 + y * width;
        int nibble = x & 1;
        int shiftedNibble =  (0xF << nibble * 4);

        if(on) {
            buffer[arrayElement] |= shiftedNibble;
        } else {
            buffer[arrayElement] &= ~ shiftedNibble;
        }

        return true;
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
        int rowSize = width * getColorBitsPerPixel() / 8;
        //byte [] rowData = new byte[rowSize];
        for (int row = 0; row<height; row++) {
            int rowStart = row * rowSize;
            //int rowEnd = (row + 1) * rowSize - 1;
            dspConn.data(buffer, rowStart, rowSize);
        }
    }
}
