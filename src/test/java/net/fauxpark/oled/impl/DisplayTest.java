package net.fauxpark.oled.impl;

import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.i2c.I2CBus;
import net.fauxpark.oled.SSD1306;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class DisplayTest {

    private static final Logger logger = LoggerFactory.getLogger(DisplayTest.class);

    @Test
    public void logTest() {
            int width = 128;
            int height = 32;
            SSD1306 ssd1306 = new SSD1306I2CImpl(width, height, RaspiPin.GPIO_24, I2CBus.BUS_1, 0x3C);
            ssd1306.startup(false);

            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
            Graphics2D graphics = image.createGraphics();

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

            Raster r = image.getRaster();
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    ssd1306.setPixel(x, y, (r.getSample(x, y, 0) > 0));
                }
            }

            ssd1306.display();

    }
}