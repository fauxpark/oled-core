package net.fauxpark.oled.main;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.i2c.I2CBus;
import net.fauxpark.oled.conn.DisplayConnection;
import net.fauxpark.oled.conn.DisplayConnectionI2C;
import net.fauxpark.oled.SSD1306Display;
import net.fauxpark.oled.SSDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.IOException;

public class DisplayTest {
    private static final Logger logger = LoggerFactory.getLogger(DisplayTest.class);
    public static final int INVERSION_FLIP_SLEEP = 500;

    public static final int I2C_BUS = I2CBus.BUS_1;
    public static final int I2C_ADDRESS = 0x3C;

    // public static final Pin RST_PIN = RaspiPin.GPIO_24;
    public static final Pin RST_PIN = null;


    static SSDisplay display;

    static BufferedImage image;
    static Graphics2D graphics;


    static final int width = 128;
    static final int height = 32;

    public DisplayTest(SSDisplay display) {
        this.display = display;
    }

    public void run() throws Exception {
        setUp();
        testDrawing();
        testInversionFlipping();
        shutdown();
    }

    public void setUp() throws IOException {
        logger.info(">> setUp - startup display");
        logger.info("   type: {} -connected-via-> {}", display, display.getDspConn());

        display.startup(false);

        logger.info("-- headless awt setup");
        image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
        graphics = image.createGraphics();

        logger.debug("<< setUp");
    }

    public void shutdown() {
        logger.info("-- shutdown");
        display.shutdown();
        logger.info("<< shutdown");
    }

    public void testDrawing() {
        logger.debug(">> testDrawing");

        graphics.clearRect(0, 0, width, height);
        graphics.setColor(Color.WHITE);
        Font font = new Font("Monospaced", Font.BOLD, 30);
        graphics.setFont(font);
        graphics.drawString("232", 35, 27);

        Polygon check = new Polygon();
        check.addPoint(5, 15);
        check.addPoint(13, 23);
        check.addPoint(27, 9);
        check.addPoint(24, 6);
        check.addPoint(13, 17);
        check.addPoint(8, 12);
        graphics.fillPolygon(check);

        Polygon cross = new Polygon();
        cross.addPoint(4, 8);
        cross.addPoint(12, 16);
        cross.addPoint(4, 24);
        cross.addPoint(9, 29);
        cross.addPoint(17, 21);
        cross.addPoint(25, 29);
        cross.addPoint(29, 25);
        cross.addPoint(21, 17);
        cross.addPoint(29, 9);
        cross.addPoint(24, 4);
        cross.addPoint(16, 12);
        cross.addPoint(8, 4);
        graphics.fillPolygon(cross);

        logger.debug("-- raster");
        Raster r = image.getRaster();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                display.setPixel(x, y, (r.getSample(x, y, 0) > 0));
            }
        }

        logger.debug("-- display");
        display.display();

        logger.debug("<< testDrawing");
    }

    public void testInversionFlipping() throws Exception{
        // flip inversion some times
        // (helping with problem of bitmask interpretation)
        boolean displayInverted = false;

        for (int i = 0; i < 15; i++) {
            logger.info("{} - inverted: {}", i, displayInverted );
            display.setInverted(displayInverted);
            displayInverted = ! displayInverted;
            Thread.sleep(INVERSION_FLIP_SLEEP);
        }


    }
}