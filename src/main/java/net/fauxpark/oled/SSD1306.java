package net.fauxpark.oled;

/**
 * An interface for defining implementations of the SSD1306 OLED display.
 *
 * @author fauxpark
 */
public abstract class SSD1306 {
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
	 * Indicates whether the display is on or off.
	 */
	protected boolean displayState;

	/**
	 * Indicates whether the display is inverted.
	 */
	protected boolean inverted;

	/**
	 * Indicates whether the display is horizontally flipped.
	 */
	protected boolean hFlipped;

	/**
	 * Indicates whether the display is vertically flipped.
	 */
	protected boolean vFlipped;

	/**
	 * The current contrast of the display.
	 */
	protected int contrast;

	/**
	 * SSD1306 constructor.
	 *
	 * @param width The width of the display in pixels.
	 * @param height The height of the display in pixels.
	 */
	public SSD1306(int width, int height) {
		this.width = width;
		this.height = height;
		pages = height / 8;
		buffer = new byte[width * pages];
	}

	/**
	 * Start the power on procedure for the display.
	 *
	 * @param externalVcc Indicates whether the display is being driven by an external power source.
	 */
	public abstract void startup(boolean externalVcc);

	/**
	 * Start the power off procedure for the display.
	 */
	public abstract void shutdown();

	/**
	 * Reset the display.
	 */
	public abstract void reset();

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
	public abstract void display();

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
	public boolean getDisplayState() {
		return displayState;
	}

	/**
	 * Turn the display on or off.
	 *
	 * @param displayState Whether to turn the display on.
	 */
	public void setDisplayState(boolean displayState) {
		this.displayState = displayState;
	}

	/**
	 * Get the inverted state of the display.
	 *
	 * @return Whether the display is inverted or not.
	 */
	public boolean getInverted() {
		return inverted;
	}

	/**
	 * Invert the display.
	 * When inverted, an "on" bit in the buffer results in an unlit pixel.
	 *
	 * @param inverted Whether to invert the display or return to normal.
	 */
	public void setInverted(boolean inverted) {
		this.inverted = inverted;
	}

	/**
	 * Get the display contrast.
	 *
	 * @return The current contrast setting of the display.
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
		this.contrast = contrast;
	}


	/**
	 * Scroll the display horizontally.
	 *
	 * @param direction The direction to scroll, where a value of true results in the display scrolling to the left.
	 * @param start The start page address, from 0 to 7.
	 * @param end The end page address, from 0 to 7.
	 * @param step The scrolling speed.
	 *
	 * @see Constant#SCROLL_STEP_5
	 */
	public abstract void scrollHorizontally(boolean direction, int start, int end, int step);

	/**
	 * Scroll the display horizontally and vertically.
	 *
	 * @param direction The direction to scroll, where a value of true results in the display scrolling to the left.
	 * @param start The start page address, from 0 to 7.
	 * @param end The end page address, from 0 to 7.
	 * @param step The scrolling speed.
	 *
	 * @see Constant#SCROLL_STEP_5
	 */
	public abstract void scrollDiagonally(boolean direction, int start, int end, int step);

	/**
	 * Stop scrolling the display.
	 */
	public abstract void stopScroll();

	/**
	 * Get the horizontal flip state of the display.
	 *
	 * @return Whether the display is horizontally flipped.
	 */
	public boolean getHFlipped() {
		return hFlipped;
	}

	/**
	 * Flip the display horizontally.
	 *
	 * @param flip Whether to flip the display or return to normal.
	 */
	public void horizontalFlip(boolean flip) {
		this.hFlipped = flip;
	}

	/**
	 * Get the vertical flip state of the display.
	 *
	 * @return Whether the display is vertically flipped.
	 */
	public boolean getVFlipped() {
		return vFlipped;
	}

	/**
	 * Flip the display vertically.
	 *
	 * @param flip Whether to flip the display or return to normal.
	 */
	public void verticalFlip(boolean flip) {
		this.vFlipped = flip;
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
		if(x < 0 || x > width || y < 0 || y > height) {
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
		if(x < 0 || x > width || y < 0 || y > height) {
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
	 * Send a command over SPI.
	 *
	 * @param command The command to send.
	 * @param params Any parameters the command requires.
	 */
	public abstract void command(int command, int... params);

	/**
	 * Send display data over SPI.
	 *
	 * @param data The data to send.
	 */
	public abstract void data(byte[] data);
}
