package net.fauxpark.oled.conn;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class DisplayConnectionI2C extends DisplayConnection {
    private static final Logger logger = LoggerFactory.getLogger(DisplayConnectionI2C.class);

    /**
     * The internal I<sup>2</sup>C device.
     */
    private I2CDevice i2c;

    /**
     * The Data/Command bit position.
     */
    private static final int DC_BIT = 6;


    public static final int DEFAULT_I2C_BUS_ID = 1;
    public static final int DEFAULT_I2C_ADDRESS = 0x3c;

    private int i2cBusId = DEFAULT_I2C_BUS_ID;
    private int i2cAddress = DEFAULT_I2C_ADDRESS;

    /**
     * minimal constructor - will use DEFAULT_I2C_BUS_ID and DEFAULT_I2C_ADDRESS
     */
    public DisplayConnectionI2C() throws IOException {
        init();
    }


    public DisplayConnectionI2C(int i2cBusId, int i2cAddress) throws IOException {
        this.i2cBusId = i2cBusId;
        this.i2cAddress = i2cAddress;
        init();
    }

    /**
     * all parameters are optional
     *
     * @param gpioInstance
     * @param i2cBusId
     * @param i2cAddress
     * @param rstPin
     */
    public DisplayConnectionI2C(GpioController gpioInstance, int i2cBusId, int i2cAddress, Pin rstPin) throws IOException {
        super(gpioInstance, rstPin);
        this.i2cBusId = i2cBusId;
        this.i2cAddress = i2cAddress;
        init();
    }

    protected void init() throws IOException {
        if (i2cBusId <= 0) {
            i2cBusId = DEFAULT_I2C_BUS_ID;
        }
        if (i2cAddress <= 0) {
            i2cAddress = DEFAULT_I2C_ADDRESS;
        }
        try {
            i2c = I2CFactory.getInstance(i2cBusId).getDevice(i2cAddress);
        } catch (I2CFactory.UnsupportedBusNumberException e) {
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
