package net.fauxpark.oled.conn;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.IOException;

public class DisplayConnectionSPI extends DisplayConnection {
    public static final int DEFAULT_SPI_SPEED = 8000000;

    /**
     * The internal SPI device.
     */
    private SpiDevice spi;

    protected SpiChannel spiChannel;

    protected int spiSpeed = DEFAULT_SPI_SPEED;


    private GpioPinDigitalOutput dcOutputPin;
    private Pin dcPin;

    /**
     * minimal constructor
     * @param spiChannel
     */
    public DisplayConnectionSPI(SpiChannel spiChannel) throws IOException {
        this.spiChannel = spiChannel;
        init();
    }

    /**
     * only obligatory param is spiChannel
     *
     * @param gpioInstance
     * @param spiChannel
     * @param spiSpeed
     * @param rstPin
     * @param dcPin
     */
    public DisplayConnectionSPI(GpioController gpioInstance, SpiChannel spiChannel, int spiSpeed, Pin rstPin, Pin dcPin) throws IOException {
        super(gpioInstance, rstPin);
        this.spiChannel = spiChannel;
        this.spiSpeed = spiSpeed;
        this.dcPin = dcPin;
        init();
    }


    protected void init() throws IOException {
        this.spi = SpiFactory.getInstance(spiChannel, spiSpeed);
        if (dcPin != null) {
            this.dcOutputPin = gpio.provisionDigitalOutputPin(dcPin);
        }
    }

    @Override
    public void command(int command, int... params) {
        dcOutputPin.setState(false);

        try {
            spi.write((byte) command);
        } catch(IOException e) {
            e.printStackTrace();
        }

        for(int param : params) {
            try {
                spi.write((byte) param);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void data(byte[] data) {
        dcOutputPin.setState(true);

        try {
            spi.write(data);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
