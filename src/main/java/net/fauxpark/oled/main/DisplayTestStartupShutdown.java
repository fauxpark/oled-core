package net.fauxpark.oled.main;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.i2c.I2CBus;
import net.fauxpark.oled.SSDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class DisplayTestStartupShutdown {
    private static final Logger logger = LoggerFactory.getLogger(DisplayTestStartupShutdown.class);

    public static final int I2C_BUS = I2CBus.BUS_1;
    public static final int I2C_ADDRESS = 0x3C;

    // public static final Pin RST_PIN = RaspiPin.GPIO_24;
    public static final Pin RST_PIN = null;


    static SSDisplay display;


    static final int width = 128;
    static final int height = 32;

    public DisplayTestStartupShutdown(SSDisplay display) {
        this.display = display;
    }

    public void run() throws Exception {
        setUp();



        shutdown();
    }

    public void setUp() throws IOException {
        logger.info(">> setUp - startup display");
        logger.info("   type: {} -connected-via-> {}", display, display.getDspConn());

        display.startup(false);

        logger.debug("<< setUp");
    }

    public void shutdown() throws IOException {
        logger.info("-- shutdown");
        display.shutdown();
        logger.info("<< shutdown");
    }

}
