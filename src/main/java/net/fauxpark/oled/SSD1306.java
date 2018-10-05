package net.fauxpark.oled;

import com.pi4j.io.gpio.Pin;
import net.fauxpark.oled.conn.DisplayConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SSD1306 extends SSDisplay {
    private static final Logger logger = LoggerFactory.getLogger(SSD1306.class);
    public static final int COLOR_BITS_PER_PIXEL = 1;

    int pages = 0;

    public CommandSSD1306 commandset = new CommandSSD1306();



    public SSD1306(DisplayConnection dspConn, int width, int height) {
        super(dspConn, width, height);
        init();
    }

    public SSD1306(DisplayConnection dspConn, int width, int height, Pin rstPin) {
        super(dspConn, width, height, rstPin);
        init();
    }

    private void init() {
        pages = height / 8;
        super.commandset = this.commandset;
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

        dspConn.command(commandset.SET_DISPLAY_CLOCK_DIV, width);  // from SPI
        // command(commandset.SET_DISPLAY_CLOCK_DIV, 0x80);    // from I2C

        // TODO what´s this for?
        // command(commandset.SET_MULTIPLEX_RATIO, width - 1);// from SPI
        dspConn.command(commandset.SET_MULTIPLEX_RATIO, height == 64 ? 0x3F : 0x1F);// from I2C

        setOffset(0);
        dspConn.command(commandset.SET_START_LINE_00);
        dspConn.command(commandset.SET_CHARGE_PUMP, externalVcc ? Constant.CHARGE_PUMP_DISABLE : Constant.CHARGE_PUMP_ENABLE);
        dspConn.command(commandset.SET_MEMORY_MODE, Constant.MEMORY_MODE_HORIZONTAL);
        setHFlipped(false);
        setVFlipped(false);
        dspConn.command(commandset.SET_COM_PINS, height == 64 ? 0x12 : 0x02);

        setContrast(externalVcc ? 0x9F : 0xCF); // from SPI
        setContrast(height == 64 ? 0x8F : externalVcc ? 0x9F : 0xCF);   // from I2C

        dspConn.command(commandset.SET_PRECHARGE_PERIOD, externalVcc ? 0x22 : 0xF1);
        dspConn.command(commandset.SET_VCOMH_DESELECT, Constant.VCOMH_DESELECT_LEVEL_00);
        dspConn.command(commandset.DISPLAY_MODE_NORMAL);

        super.basicStartup(externalVcc);
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

        if(on) {
            buffer[x + (y / 8) * width] |= (1 << (y & 7));
        } else {
            buffer[x + (y / 8) * width] &= ~(1 << (y & 7));
        }

        return true;
    }


    public byte[] getNewBuffer() {
        /**
         * The number of pages in the display.
         */
        byte[] buffer = new byte[width * pages];
        return buffer;
    }

    @Override
    public synchronized void display() throws IOException {
        dspConn.command(commandset.SET_COLUMN_ADDRESS, 0, width - 1);
        dspConn.command(commandset.SET_PAGE_ADDRESS, 0, pages - 1);

        super.display();
    }

    public int getColorBitsPerPixel() {
        return COLOR_BITS_PER_PIXEL;
    }

    @Override
    public CommandSSD1306 getCommandset() {
        return new CommandSSD1306();
    }

    @Override
    public Graphics2D getGraphics2D() {
        if (bufferedImage == null) {
            bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
            graphics2D = bufferedImage.createGraphics();
            graphics2D.clearRect(0, 0, width, height);
            graphics2D.setColor(Color.WHITE);
        }
        return graphics2D;
    }
}
