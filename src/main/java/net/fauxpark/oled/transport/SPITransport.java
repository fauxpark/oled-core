package net.fauxpark.oled.transport;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.spi.Spi;
import com.pi4j.library.pigpio.PiGpio;
import com.pi4j.plugin.pigpio.provider.gpio.digital.PiGpioDigitalOutputProvider;
import com.pi4j.plugin.pigpio.provider.spi.PiGpioSpiProvider;

/**
 * A {@link Transport} implementation that utilises SPI.
 *
 * @author fauxpark
 */
public class SPITransport implements Transport {
    /**
     * The internal Pi4J context.
     */
    private final Context context;

    /**
     * The GPIO pin corresponding to the RST line on the display.
     */
    private final DigitalOutput rstPin;

    /**
     * The GPIO pin corresponding to the D/C line on the display.
     */
    private final DigitalOutput dcPin;

    /**
     * The internal SPI device.
     */
    private final Spi spi;

    /**
     * SPITransport constructor.
     *
     * @param channel The SPI channel to use.
     * @param rstPin The GPIO pin to use for the RST line.
     * @param dcPin The GPIO pin to use for the D/C line.
     */
    public SPITransport(int channel, int rstPin, int dcPin) {
        var gpio = PiGpio.newNativeInstance();
        this.context = Pi4J.newContextBuilder()
            .noAutoDetect()
            .add(
                PiGpioSpiProvider.newInstance(gpio),
                PiGpioDigitalOutputProvider.newInstance(gpio)
            )
            .build();

        var rstPinConfig = DigitalOutput.newConfigBuilder(context)
                .address(rstPin)
                .build();
        this.rstPin = context.dout().create(rstPinConfig);

        var dcPinConfig = DigitalOutput.newConfigBuilder(context)
                .address(dcPin)
                .build();
        this.dcPin = context.dout().create(dcPinConfig);

        var spiConfig = Spi.newConfigBuilder(context)
            .address(channel)
            .baud(8000000)
            .build();
        this.spi = context.spi().create(spiConfig);
    }

    @Override
    public void reset() {
        try {
            rstPin.high();
            Thread.sleep(1);
            rstPin.low();
            Thread.sleep(10);
            rstPin.high();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void shutdown() {
        context.shutdown();
    }

    @Override
    public void command(int command, int... params) {
        dcPin.low();
        spi.write((byte) command);
        for(int param : params) {
            spi.write((byte) param);
        }
    }

    @Override
    public void data(byte[] data) {
        dcPin.high();
        spi.write(data);
    }
}
