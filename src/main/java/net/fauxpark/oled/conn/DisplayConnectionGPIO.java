package net.fauxpark.oled.conn;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DisplayConnectionGPIO extends DisplayConnection {
    private static final Logger logger = LoggerFactory.getLogger(DisplayConnectionGPIO.class);
    /**
     * The internal GPIO instance.
     */
    protected GpioController gpio;

    boolean selfInstanciatedGpioController = false;

    /**
     * The GPIO pin corresponding to the RST line on the display.
     */
    protected GpioPinDigitalOutput rstOutputPin;
    protected Pin rstPin;


    public DisplayConnectionGPIO() {
        init();
    }

    public DisplayConnectionGPIO(GpioController gpioInstance, Pin rstPin) {
        this.gpio = gpioInstance;
        this.rstPin = rstPin;

        init();
    }

    private void init() {
        if (gpio == null) {
            gpio = GpioFactory.getInstance();
            selfInstanciatedGpioController = true;
        }
        if (rstPin != null) {
            this.rstOutputPin = gpio.provisionDigitalOutputPin(rstPin);
        }
    }

    /**
     * HW-Reset the display.
     */
    public void reset() {
        if (rstPin == null) {
            logger.info("reset - no effect without reset pin");
            return;
        }
        try {
            rstOutputPin.setState(true);
            Thread.sleep(1);
            rstOutputPin.setState(false);
            Thread.sleep(10);
            rstOutputPin.setState(true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        if (selfInstanciatedGpioController) {
            gpio.shutdown();
        }
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "selfInstanciatedGpioController=" + selfInstanciatedGpioController +
                '}';
    }
}
