package net.fauxpark.oled;

import com.pi4j.io.gpio.Pin;
import net.fauxpark.oled.conn.DisplayConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A base class for defining implementations of the SSDisplay OLED display.
 *
 * @author fauxpark
 */
public abstract class SSDisplay {
	private static final Logger logger = LoggerFactory.getLogger(SSDisplay.class);

	protected DisplayConnection dspConn;



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
	 * The number of pages in the display.
	 */
	protected int pages;

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

		pages = height / 8;
		buffer = new byte[width * pages];
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
	public void startup(boolean externalVcc) {
		setDisplayOn(true);
		setInverted(false);
		clear();
		display();

		initialised = true;
	}

	/**
	 * Start the power off procedure for the display.
	 */
	public void shutdown() {
		clear();
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
	 * NOTE: This does not clear the display, you must manually call {@link#display()}.
	 */
	public void clear() {
		buffer = new byte[width * pages];
	}

	/**
	 * Send the buffer to the display.
	 */
	public synchronized void display() {
		dspConn.command(Command.SET_COLUMN_ADDRESS, 0, width - 1);
		dspConn.command(Command.SET_PAGE_ADDRESS, 0, pages - 1);
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
	public void setDisplayOn(boolean displayOn) {
		if (displayOn) {
			dspConn.command(Command.DISPLAY_ON);
		} else {
			dspConn.command(Command.DISPLAY_OFF);
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
	public void setInverted(boolean inverted) {
		dspConn.command(inverted ? Command.INVERT_DISPLAY : Command.NORMAL_DISPLAY);
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
	public void setContrast(int contrast) {
		if (contrast < 0 || contrast > 255) {
			return;
		}

		dspConn.command(Command.SET_CONTRAST, contrast);
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
	public void setOffset(int offset) {
		dspConn.command(Command.SET_DISPLAY_OFFSET, offset);
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
	public void scrollHorizontally(boolean direction, int start, int end, int speed) {
		dspConn.command(direction ? Command.LEFT_HORIZONTAL_SCROLL : Command.RIGHT_HORIZONTAL_SCROLL, Constant.DUMMY_BYTE_00, start, speed, end, Constant.DUMMY_BYTE_00, Constant.DUMMY_BYTE_FF);
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
	public void scrollDiagonally(boolean direction, int start, int end, int offset, int rows, int speed, int step) {
		dspConn.command(Command.SET_VERTICAL_SCROLL_AREA, offset, rows);
		dspConn.command(direction ? Command.VERTICAL_AND_LEFT_HORIZONTAL_SCROLL : Command.VERTICAL_AND_RIGHT_HORIZONTAL_SCROLL, Constant.DUMMY_BYTE_00, start, speed, end, step);
	}
	/**
	 * Start scrolling the display.
	 */
	public void startScroll() {
		dspConn.command(Command.ACTIVATE_SCROLL);
		scrolling = true;
	}

	/**
	 * Stop scrolling the display.
	 */
	public void stopScroll() {
		dspConn.command(Command.DEACTIVATE_SCROLL);
		scrolling = false;
	}

	/**
	 * No operation.
	 */
	public void noOp() {
		dspConn.command(Command.NOOP);
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
	public void setHFlipped(boolean hFlipped) {
		if(hFlipped) {
			dspConn.command(Command.SET_SEGMENT_REMAP);
		} else {
			dspConn.command(Command.SET_SEGMENT_REMAP_REVERSE);
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
	public void setVFlipped(boolean vFlipped) {
		if (vFlipped) {
			dspConn.command(Command.SET_COM_SCAN_INC);
		} else {
			dspConn.command(Command.SET_COM_SCAN_DEC);
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
	public boolean setPixel(int x, int y, boolean on) {
		if(x < 0 || x >= width || y < 0 || y >= height) {
			return false;
		}

		if(on) {
			buffer[x + (y / 8) * width] |= (1 << (y & 7));
		} else {
			buffer[x + (y / 8) * width] &= ~(1 << (y & 7));
		}

		return true;
	}

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

	/*public GpioController getGpio() {
		return gpio;
	}

	public void setGpio(GpioController gpio) {
		this.gpio = gpio;
	}*/
}
