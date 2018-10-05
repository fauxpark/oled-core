package net.fauxpark.oled;

import net.fauxpark.oled.conn.DisplayConnectionMock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SSD1327AwtMock extends SSD1327{
    private static final Logger logger = LoggerFactory.getLogger(SSD1327AwtMock.class);

    // TODO why is this needed?
    int paddingX=20;
    int paddingY=30;

    // display scaling
    int scale = 3;

    JFrame displayFrame;
    BufferedImage bufferedImage;
    //Graphics2D bufferAsGraphics;

    public SSD1327AwtMock() {
        // has to be used
        super(new DisplayConnectionMock());

        displayFrame = new JFrame(this.getClass().getSimpleName());
        displayFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        displayFrame.setSize(width * scale+ paddingX * scale, height * scale+ paddingY * scale);
        displayFrame.setVisible(true);
        displayFrame.setResizable(false);

        bufferedImage = new BufferedImage(width * scale + paddingX * scale, height * scale + paddingY * scale, BufferedImage.TYPE_BYTE_GRAY);
    }

    public boolean setPixel(int x, int y, boolean on) {
        return setPixel(x, y, 255);
    }
    @Override
    public boolean setPixel(int x, int y, int grey) {
        if(x < 0 || x >= width || y < 0 || y >= height) {
            return false;
        }

        Color col = (new Color(grey, grey, grey));

        int[] rgb = {col.getRGB(), col.getRGB(), col.getRGB()};
        setPixelRgb(x, y, rgb);


        return true;
    }

    private void setPixelRgb(int x, int y, int[] rgb) {
        bufferedImage.setRGB(x*scale + paddingX, y*scale + paddingY,
                scale, scale, rgb, 0, 0);
    }

    @Override
    public synchronized void display() throws IOException {
        logger.info("display");
        java.awt.Graphics g = displayFrame.getGraphics();
        g.drawImage(bufferedImage, 0,0, null);
        try {
            Thread.sleep(200); // slow down
        } catch (Exception ex) {}
    }

    @Override
    public void clearBuffer() {
        fillBufferWithPattern((byte) 0);
    }

    // TODO this seems not to work
    @Override
    public void fillBufferWithPattern(byte bPattern) {
        int grey = bPattern & 0xFF;
        logger.info("fillBufferWithPattern");
        for (int x=0; x<height; x++) {
            for (int y=0; y<width; y++) {
                setPixel(y, x, grey);
            }
        }
        try {
            Thread.sleep(200); // slow down
        } catch (Exception ex) {}
    }

    @Override
    public void rasterGraphics2DImage(boolean display) throws IOException {
        logger.info("rasterGraphics2DImage");
        for (int y=0; y<height; y++) {
            for (int x=0; x<width; x++) {
                int rgb = super.bufferedImage.getRGB(x, y);
                int[] rgbA = {rgb, rgb, rgb};

                setPixelRgb(x, y, rgbA);
            }
        }

        if (display) {
            display();
        }
    }
}
