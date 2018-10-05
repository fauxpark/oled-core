package net.fauxpark.oled;

import net.fauxpark.oled.conn.DisplayConnection;
import net.fauxpark.oled.conn.DisplayConnectionMock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SSD1327AwtMock extends SSD1327{
    private static final Logger logger = LoggerFactory.getLogger(SSD1327AwtMock.class);

    int paddingX=20;
    int paddingY=30;
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


    @Override
    public boolean setPixel(int x, int y, int grey) {
        if(x < 0 || x >= width || y < 0 || y >= height) {
            return false;
        }

        int[] rgb = {grey, grey, grey};
        bufferedImage.setRGB(x*scale + paddingX, y*scale + paddingY,
                scale, scale, rgb, 0, 0);


        return true;
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

    @Override
    public void fillBufferWithPattern(byte bPattern) {
        logger.info("fillBufferWithPattern");
        for (int x=0; x<height; x++) {
            for (int y=0; y<width; y++) {
                setPixel(y, x, bPattern);
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
                setPixel(x, y, super.bufferedImage.getRGB(x, y));
            }
        }

        if (display) {
            display();
        }
    }
}
