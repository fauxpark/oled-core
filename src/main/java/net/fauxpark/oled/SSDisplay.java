package net.fauxpark.oled;

import com.pi4j.io.gpio.Pin;
import net.fauxpark.oled.conn.DisplayConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.IOException;

/**
 * A base class for defining implementations of the SSDisplay OLED display.
 *
 * @author fauxpark
 */
public abstract class SSDisplay {
	private static final Logger logger = LoggerFactory.getLogger(SSDisplay.class);

	protected DisplayConnection dspConn;

	public CommandSSD commandset = new CommandSSD();

	protected BufferedImage bufferedImage;
	protected Graphics2D graphics2D;
	/**
	 * A helper class for drawing lines, shapes, text and images.
	 * @deprecated used anymore?
	 */
	private Graphics graphics;

	/**
	 * The width of the display in pixels.
	 */
	protected int width;

	/**
	 * The height of the display in pixels.
	 */
	protected int height;



	/**
	 * The display buffer.
	 */
	protected byte[] buffer;

	/**
	 * Indicates whether the display has been started up.
	 */
	private boolean initialised;

	/**
	 * Indicates whether the display is on or off.
	 */
	private boolean displayOn;

	/**
	 * Indicates whether the display is inverted.
	 */
	private boolean inverted;

	/**
	 * Indicates whether the display is horizontally flipped.
	 */
	private boolean hFlipped;

	/**
	 * Indicates whether the display is vertically flipped.
	 */
	private boolean vFlipped;

	/**
	 * Indicates whether the display is currently scrolling.
	 */
	private boolean scrolling;

	/**
	 * The current contrast level of the display.
	 */
	private int contrast;

	/**
	 * The current display offset.
	 */
	private int offset;

	/**
	 * SSDisplay constructor.
	 *
	 * @param width The width of the display in pixels.
	 * @param height The height of the display in pixels.
	 */
	public SSDisplay(DisplayConnection dspConn, int width, int height) {
		init(dspConn, width, height, null);
	}

	public SSDisplay(DisplayConnection dspConn, int width, int height, Pin rstPin) {
		init(dspConn, width, height, rstPin);
	}

	private void init(DisplayConnection dspConn, int width, int height, Pin rstPin) {
	    this.dspConn = dspConn;
		this.width = width;
		this.height = height;

		this.commandset = getCommandset();

		buffer = getNewBuffer();
	}

	/**
	 * Get the initialised state of the display.
	 */
	public boolean isInitialised() {
		return initialised;
	}

	/**
	 * Start the power on procedure for the display.
     * Generic for SSDisplays
	 *
	 * @param externalVcc Indicates whether the display is being driven by an external power source.
	 */
	public abstract void startup(boolean externalVcc) throws IOException;

	protected void basicStartup(boolean externalVcc) throws IOException {
		setDisplayOn(true);
		setInverted(false);
		clearBuffer();
		display();

		initialised = true;
	}

	/**
	 * Start the power off procedure for the display.
	 */
	public void shutdown() throws IOException {
		clearBuffer();
		display();
		reset();

		initialised = false;
		setInverted(false);
		setHFlipped(false);
		setVFlipped(false);
		// stopScroll();
		// setContrast(0);
		setOffset(0);

		setDisplayOn(false);

		dspConn.shutdown();
	}

	/**
	 * Reset the display.
	 */
	public void reset() {
        // TODO sw reset possible?!

        dspConn.reset();
	}
	/**
	 * Clear the buffer.
	 * <br/>
	 * NOTE: This does not clearBuffer the display, you must manually call {@link#display()}.
	 */
	public void clearBuffer() {
		buffer = getNewBuffer();
	}


	public void fillBufferWithPattern(byte bPattern) {
		for (int i=0; i<buffer.length; i++) {
			buffer[i] = bPattern;
		}
	}


	/**
	 * Send the buffer to the display.
	 */
	public synchronized void display() throws IOException {
		dspConn.data(buffer);

		// Jump start scrolling again if new data is written while enabled
		if(isScrolling()) {
			noOp();
		}
	}

	/**
	 * Get the width of the display.
	 *
	 * @return The display width in pixels.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Get the height of the display.
	 *
	 * @return The display height in pixels.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Get the display state.
	 *
	 * @return True if the display is on.
	 */
	public boolean isDisplayOn() {
		return displayOn;
	}

	/**
	 * Turn the display on or off.
	 *
	 * @param displayOn Whether to turn the display on.
	 */
	public void setDisplayOn(boolean displayOn) throws IOException {
		if (displayOn) {
			dspConn.command(commandset.DISPLAY_ON);
		} else {
			dspConn.command(commandset.DISPLAY_OFF);
		}

		this.displayOn = displayOn;
	}
	/**
	 * Get the inverted state of the display.
	 *
	 * @return Whether the display is inverted or not.
	 */
	public boolean isInverted() {
		return inverted;
	}

	/**
	 * Invert the display.
	 * When inverted, an "on" bit in the buffer results in an unlit pixel.
	 *
	 * @param inverted Whether to invert the display or return to normal.
	 */
	public void setInverted(boolean inverted) throws IOException {
		dspConn.command(inverted ? commandset.DISPLAY_MODE_INVERT : commandset.DISPLAY_MODE_NORMAL);
		this.inverted = inverted;
	}

	/**
	 * Get the display contrast.
	 *
	 * @return The current contrast level of the display.
	 */
	public int getContrast() {
		return contrast;
	}

	/**
	 * Set the display contrast.
	 *
	 * @param contrast The contrast to set, from 0 to 255.
	 */
	public void setContrast(int contrast) throws IOException {
		if (contrast < 0 || contrast > 255) {
			return;
		}

		dspConn.command(commandset.SET_CONTRAST, contrast);
		this.contrast = contrast;
	}

	/**
	 * Get the display offset.
	 *
	 * @return The number of rows the display is offset by.
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * Set the display offset.
	 *
	 * @param offset The number of rows to offset the display by.
	 */
	public void setOffset(int offset) throws IOException {
		dspConn.command(commandset.SET_DISPLAY_OFFSET, offset);
		this.offset = offset;
	}

	/**
	 * Get the scrolling state of the display.
	 *
	 * @return Whether the display is scrolling.
	 */
	public boolean isScrolling() {
		return scrolling;
	}

	/**
	 * Scroll the display horizontally.
	 *
	 * @param direction The direction to scroll, where a value of true results in the display scrolling to the left.
	 * @param start The start page address, from 0 to 7.
	 * @param end The end page address, from 0 to 7.
	 * @param speed The scrolling speed (scroll step).
	 *
	 * @see Constant#SCROLL_STEP_5
	 */
	public void scrollHorizontally(boolean direction, int start, int end, int speed) throws IOException {
		dspConn.command(direction ? commandset.LEFT_HORIZONTAL_SCROLL : commandset.RIGHT_HORIZONTAL_SCROLL, Constant.DUMMY_BYTE_00, start, speed, end, Constant.DUMMY_BYTE_00, Constant.DUMMY_BYTE_FF);
	}
	/**
	 * Scroll the display horizontally and vertically.
	 *
	 * @param direction The direction to scroll, where a value of true results in the display scrolling to the left.
	 * @param start The start page address, from 0 to 7.
	 * @param end The end page address, from 0 to 7.
	 * @param offset The number of rows from the top to start the vertical scroll area at.
	 * @param rows The number of rows in the vertical scroll area.
	 * @param speed The scrolling speed (scroll step).
	 * @param step The number of rows to scroll vertically each frame.
	 *
	 * @see Constant#SCROLL_STEP_5
	 */
	public void scrollDiagonally(boolean direction, int start, int end, int offset, int rows, int speed, int step) throws IOException {
		dspConn.command(commandset.SET_VERTICAL_SCROLL_AREA, offset, rows);
		dspConn.command(direction ? commandset.VERTICAL_AND_LEFT_HORIZONTAL_SCROLL : commandset.VERTICAL_AND_RIGHT_HORIZONTAL_SCROLL, Constant.DUMMY_BYTE_00, start, speed, end, step);
	}
	/**
	 * Start scrolling the display.
	 */
	public void startScroll() throws IOException {
		dspConn.command(commandset.ACTIVATE_SCROLL);
		scrolling = true;
	}

	/**
	 * Stop scrolling the display.
	 */
	public void stopScroll() throws IOException {
		dspConn.command(commandset.DEACTIVATE_SCROLL);
		scrolling = false;
	}

	/**
	 * No operation.
	 */
	public void noOp() throws IOException {
		dspConn.command(commandset.NOOP);
	}
	/**
	 * Get the horizontal flip state of the display.
	 *
	 * @return Whether the display is horizontally flipped.
	 */
	public boolean isHFlipped() {
		return hFlipped;
	}

	/**
	 * Flip the display horizontally.
	 *
	 * @param hFlipped Whether to flip the display or return to normal.
	 */
	public void setHFlipped(boolean hFlipped) throws IOException {
		if(hFlipped) {
			dspConn.command(commandset.SET_SEGMENT_REMAP);
		} else {
			dspConn.command(commandset.SET_SEGMENT_REMAP_REVERSE);
		}

		// Horizontal flipping is not immediate
		display();
		this.hFlipped = hFlipped;
	}

	/**
	 * Get the vertical flip state of the display.
	 *
	 * @return Whether the display is vertically flipped.
	 */
	public boolean isVFlipped() {
		return vFlipped;
	}

	/**
	 * Flip the display vertically.
	 *
	 * @param vFlipped Whether to flip the display or return to normal.
	 */
	public void setVFlipped(boolean vFlipped) throws IOException {
		if (vFlipped) {
			dspConn.command(commandset.SET_COM_SCAN_INC);
		} else {
			dspConn.command(commandset.SET_COM_SCAN_DEC);
		}
		this.vFlipped = vFlipped;
	}

	/**
	 * Get a pixel in the buffer.
	 *
	 * @param x The X position of the pixel to set.
	 * @param y The Y position of the pixel to set.
	 *
	 * @return False if the pixel is "off" or the given coordinates are out of bounds, true if the pixel is "on".
	 */
	public boolean getPixel(int x, int y) {
		if(x < 0 || x >= width || y < 0 || y >= height) {
			return false;
		}

		return (buffer[x + (y / 8) * width] & (1 << (y & 7))) != 0;
	}

	/**
	 * Set a pixel in the buffer.
	 *
	 * @param x The X position of the pixel to set.
	 * @param y The Y position of the pixel to set.
	 * @param on Whether to turn this pixel on or off.
	 *
	 * @return False if the given coordinates are out of bounds.
	 */
	public abstract boolean setPixel(int x, int y, boolean on);

	/**
	 * Get the display buffer.
	 *
	 * @return The display buffer.
	 */
	public byte[] getBuffer() {
		return buffer;
	}

	/**
	 * Set the display buffer.
	 *
	 * @param buffer The buffer to set.
	 */
	public void setBuffer(byte[] buffer) {
		this.buffer = buffer;
	}

	/**
	 * Send a dspConn.command to the display.
	 *
	 * @param dspConn.command The dspConn.command to send.
	 * @param params Any parameters the dspConn.command requires.
	 */
	//public abstract void dspConn.command(int dspConn.command, int... params);

	/**
	 * Send pixel data to the display.
	 *
	 * @param data The data to send.
	 */
	//public abstract void dspConn.data(byte[] data);

	/**
	 * Get the Graphics instance, creating it if necessary.
	 *
	 * @deprecated in use?!
	 * @return The Graphics instance.
	 */
	public final Graphics getGraphics() {
		if(graphics == null) {
			graphics = new Graphics(this);
		}

		return graphics;
	}

	public DisplayConnection getDspConn() {
		return dspConn;
	}

    /**
     * creates a new byte[] buffer dependent on display size and bits per pixel
     * (device specific)
     * @return
     */
	public abstract byte[] getNewBuffer();


	/*public GpioController getGpio() {
		return gpio;
	}

	public void setGpio(GpioController gpio) {
		this.gpio = gpio;
	}*/

	public abstract int getColorBitsPerPixel();

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "{" +
				"width=" + width +
				", height=" + height +
				", bitsPerPixel=" + getColorBitsPerPixel() +
				'}';
	}

	public CommandSSD getCommandset() {
	    return new CommandSSD();
    }

    public abstract Graphics2D getGraphics2D();

	public void rasterGraphics2DImage(boolean display) throws IOException {
		Raster r = bufferedImage.getRaster();
		int sample = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				sample = r.getSample(x, y, 0);
				setPixel(x, y, (sample > 0));
			}
		}

		if (display) {
			display();
		}
	}
}
