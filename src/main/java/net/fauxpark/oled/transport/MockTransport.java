package net.fauxpark.oled.transport;
/**
 * A {@link Transport} implementation that does nothing. This is useful for testing on platforms other than the Raspberry Pi.
 *
 * @author fauxpark
 */
public class MockTransport implements Transport {
	@Override
	public void reset() {}

	@Override
	public void shutdown() {}

	@Override
	public void command(int command, int... params) {}

	@Override
	public void data(byte[] data) {}
}
