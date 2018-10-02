package net.fauxpark.oled.impl;

import java.io.IOException;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

import net.fauxpark.oled.Command;
import net.fauxpark.oled.Constant;
import net.fauxpark.oled.SSD1306Display;
import net.fauxpark.oled.SSDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple I<sup>2</sup>C driver for the Adafruit SSDisplay OLED display.
 *
 * @author fauxpark
 */
public class SSD1306I2CImpl extends SSD1306Display {
    private static final Logger logger = LoggerFactory.getLogger(SSD1306I2CImpl.class);

    /**
     * The internal I<sup>2</sup>C device.
     */
    private I2CDevice i2c;

    /**
     * The Data/Command bit position.
     */
    private static final int DC_BIT = 6;

    /**
     * SSD1306I2CImpl constructor.
     *
     * @param width   The width of the display in pixels.
     * @param height  The height of the display in pixels.
     * @param rstPin  The GPIO pin to use for the RST line. - or null if none
     * @param bus     The I<sup>2</sup>C bus to use.
     * @param address The I<sup>2</sup>C address of the display.
     */
    public SSD1306I2CImpl(int width, int height, Pin rstPin, int bus, int address) throws IOException {
        super(width, height, rstPin);

        try {
            i2c = I2CFactory.getInstance(bus).getDevice(address);
        } catch (UnsupportedBusNumberException e) {
            // rethrow as IOException
            throw new IOException(e);
        }
    }

    @Override
    public void command(int command, int... params) {
        byte[] commandBytes = new byte[params.length + 2];
        commandBytes[0] = (byte) (0 << DC_BIT);
        commandBytes[1] = (byte) command;

        for (int i = 0; i < params.length; i++) {
            commandBytes[i + 2] = (byte) params[i];
        }

        try {
            i2c.write(commandBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void data(byte[] data) {
        byte[] dataBytes = new byte[data.length + 1];
        dataBytes[0] = (byte) (1 << DC_BIT);

        for (int i = 0; i < data.length; i++) {
            dataBytes[i + 1] = data[i];
        }

        try {
            i2c.write(dataBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
