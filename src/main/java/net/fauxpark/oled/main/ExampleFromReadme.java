package net.fauxpark.oled.main;

import net.fauxpark.oled.Graphics;
import net.fauxpark.oled.SSD1306;
import net.fauxpark.oled.SSD1327;
import net.fauxpark.oled.SSDisplay;
import net.fauxpark.oled.conn.DisplayConnection;
import net.fauxpark.oled.conn.DisplayConnectionI2C;
import net.fauxpark.oled.conn.DisplayConnectionMock;
import net.fauxpark.oled.conn.DisplayConnectionSPI;
import net.fauxpark.oled.font.CodePage1252;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;

/**
 * contains the quick start example from README.md
 *
 * Update README.md on changes
 */
public class ExampleFromReadme {
    private static final Logger logger = LoggerFactory.getLogger(ExampleFromReadme.class);

    public static void main(String... args) throws Exception {
        awtGraphicsExample();
    }
    protected static void simpleExample() throws IOException {
        DisplayConnection dspConn;
        SSDisplay display;

        // choose connection type SPI
        dspConn = new DisplayConnectionSPI();

        // or I2C
        dspConn = new DisplayConnectionI2C();


        // choose display type SSD1306
        display = new SSD1306(dspConn, 128, 64);

        // or SSD1327
        display = new SSD1327(dspConn);


        // call startup
        display.startup(false);

        // and start using it..

        // Turns the pixel in the top left corner on
        display.setPixel(0, 0, true);

        // Sends the internal buffer to the display
        display.display();

        // Inverts the display
        display.setInverted(true);
    }

    protected static void awtGraphicsExample() throws IOException {
        logger.info("awtGraphicsExample");


        // SSDisplay display = new SSD1306(dspConn, 128, 64);
        SSDisplay display = new SSD1327(new DisplayConnectionI2C());
        display.startup(false);

        // make use of Java AWT Graphics2D
        Graphics2D graphics = display.getGraphics2D();

        // Draws a line from the top left to the bottom right of the display
        graphics.drawRect(0, 0, display.getWidth()-1, display.getHeight()-1);

        // Draws an arc from (63,31) with a radius of 8 pixels and an angle of 15 degrees
        graphics.drawArc(display.getWidth() -25, 10, 30,30, 0, 360);

        // Writes "Hello world!" at (20,20) using the Windows-1252 charset
        Font font = new Font("Serif", Font.BOLD, 18);
        graphics.setFont(font);
        graphics.drawString( "Hello world!", 5, 20);

        display.rasterGraphics2DImage(true);
    }

    @SuppressWarnings("deprecation")
    protected static void oldSchoolGraphicsExample() throws IOException {
        DisplayConnection dspConn = new DisplayConnectionSPI();
        SSD1306 ssd1306 = new SSD1306(dspConn, 128, 64);
        ssd1306.startup(false);

        Graphics graphics = ssd1306.getGraphics();


        // Draws a line from the top left to the bottom right of the display
        graphics.line(0, 0, 127, 63);

        // Draws an arc from (63,31) with a radius of 8 pixels and an angle of 15 degrees
        graphics.arc(63, 31, 8, 0, 15);

        // Writes "Hello world!" at (20,20) using the Windows-1252 charset
        graphics.text(20, 20, new CodePage1252(), "Hello world!");
    }
}
