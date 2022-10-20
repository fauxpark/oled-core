package net.fauxpark.oled.transport;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.i2c.I2C;
import com.pi4j.library.pigpio.PiGpio;
import com.pi4j.plugin.pigpio.provider.gpio.digital.PiGpioDigitalOutputProvider;
import com.pi4j.plugin.pigpio.provider.i2c.PiGpioI2CProvider;

/**
 * A {@link Transport} implementation that utilises I<sup>2</sup>C.
 *
 * @author fauxpark
 */
public class I2CTransport implements Transport {
	/**
	 * The Data/Command bit position.
	 */
	private static final int DC_BIT = 6;

	/**
	 * The internal Pi4J context.
	 */
	private final Context context;

	/**
	 * The GPIO pin corresponding to the RST line on the display.
	 */
	private final DigitalOutput rstPin;

	/**
	 * The internal I<sup>2</sup>C device.
	 */
	private final I2C i2c;

	/**
	 * I2CTransport constructor.
	 *
	 * @param rstPin The GPIO pin to use for the RST line.
	 * @param bus The I<sup>2</sup>C bus to use.
	 * @param address The I<sup>2</sup>C address of the display.
	 */
	public I2CTransport(int rstPin, int bus, int address) {
		var gpio = PiGpio.newNativeInstance();
		this.context = Pi4J.newContextBuilder()
			.noAutoDetectProviders()
			.add(
				PiGpioI2CProvider.newInstance(gpio),
				PiGpioDigitalOutputProvider.newInstance(gpio)
			)
			.build();

		var rstPinConfig = DigitalOutput.newConfigBuilder(context)
				.address(rstPin)
				.build();
		this.rstPin = context.dout().create(rstPinConfig);

		var i2cConfig = I2C.newConfigBuilder(context)
			.bus(bus)
			.device(address)
			.build();
		this.i2c = context.i2c().create(i2cConfig);
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
		byte[] commandBytes = new byte[params.length + 2];
		commandBytes[0] = (byte) (0 << DC_BIT);
		commandBytes[1] = (byte) command;

		for(int i = 0; i < params.length; i++) {
			commandBytes[i + 2] = (byte) params[i];
		}

		i2c.write(commandBytes);
	}

	@Override
	public void data(byte[] data) {
		byte[] dataBytes = new byte[data.length + 1];
		dataBytes[0] = (byte) (1 << DC_BIT);

		System.arraycopy(data, 0, dataBytes, 1, data.length);
		i2c.write(dataBytes);
	}
}
