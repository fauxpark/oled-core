package net.fauxpark.oled;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.IOException;
import java.nio.charset.Charset;

import net.fauxpark.oled.font.Font;

/**
 * A wrapper around the SSD1306 with some basic graphics methods.
 *
 * @author fauxpark
 */
public class Graphics {
	/**
	 * The SSD1306 OLED display.
	 */
	private SSD1306 ssd1306;

	/**
	 * Graphics constructor.
	 *
	 * @param ssd1306 The SSD1306 object to use.
	 */
	Graphics(SSD1306 ssd1306) {
		this.ssd1306 = ssd1306;
	}

	/**
	 * Draw text onto the display.
	 *
	 * @param x The X position to start drawing at.
	 * @param y The Y position to start drawing at.
	 * @param font The font to use.
	 * @param text The text to draw.
	 */
	public void text(int x, int y, Font font, String text) {
		int rows = font.getRows();
		int cols = font.getColumns();
		int[] glyphs = font.getGlyphs();
		byte[] bytes = text.getBytes(Charset.forName(font.getName()));

		for(int i = 0; i < text.length(); i++) {
			int p = (bytes[i] & 0xFF) * cols;

			for(int col = 0; col < cols; col++) {
				int mask = glyphs[p++];

				for(int row = 0; row < rows; row++) {
					ssd1306.setPixel(x, y + row, (mask & 1) == 1);
					mask >>= 1;
				}

				x++;
			}

			x++;
		}
	}

	/**
	 * Draw an image onto the display.
	 *
	 * @param image The image to draw.
	 * @param x The X position of the image.
	 * @param y The Y position of the image.
	 * @param width The width to resize the image to.
	 * @param height The height to resize the image to.
	 */
	public void image(BufferedImage image, int x, int y, int width, int height) throws IOException {
		BufferedImage mono = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
		mono.createGraphics().drawImage(image, 0, 0, width, height, null);
		Raster r = mono.getRaster();

		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				ssd1306.setPixel(x + j, y + i, r.getSample(j, i, 0) > 0);
			}
		}
	}

	/**
	 * Draw a line from one point to another.
	 *
	 * @param x0 The X position of the first point.
	 * @param y0 The Y position of the first point.
	 * @param x1 The X position of the second point.
	 * @param y1 The Y position of the second point.
	 */
	public void line(int x0, int y0, int x1, int y1) {
		int dx = x1 - x0;
		int dy = y1 - y0;

		if(dx == 0 && dy == 0) {
			ssd1306.setPixel(x0, y0, true);

			return;
		}

		if(dx == 0) {
			for(int y = Math.min(y0, y1); y <= Math.max(y1, y0); y++) {
				ssd1306.setPixel(x0, y, true);
			}
		} else if(dy == 0) {
			for(int x = Math.min(x0, x1); x <= Math.max(x1, x0); x++) {
				ssd1306.setPixel(x, y0, true);
			}
		} else if(Math.abs(dx) >= Math.abs(dy)) {
			if(dx < 0) {
				int ox = x0;
				int oy = y0;
				x0 = x1;
				y0 = y1;
				x1 = ox;
				y1 = oy;
				dx = x1 - x0;
				dy = y1 - y0;
			}

			double coeff = (double) dy / (double) dx;

			for(int x = 0; x <= dx; x++) {
				ssd1306.setPixel(x + x0, y0 + (int) Math.round(x * coeff), true);
			}
		} else if(Math.abs(dx) < Math.abs(dy)) {
			if(dy < 0) {
				int ox = x0;
				int oy = y0;
				x0 = x1;
				y0 = y1;
				x1 = ox;
				y1 = oy;
				dx = x1 - x0;
				dy = y1 - y0;
			}

			double coeff = (double) dx / (double) dy;

			for(int y = 0; y <= dy; y++) {
				ssd1306.setPixel(x0 + (int) Math.round(y * coeff), y + y0, true);
			}
		}
	}

	/**
	 * Draw a rectangle.
	 *
	 * @param x The X position of the rectangle.
	 * @param y The Y position of the rectangle.
	 * @param width The width of the rectangle in pixels.
	 * @param height The height of the rectangle in pixels.
	 * @param fill Whether to draw a filled rectangle.
	 */
	public void rectangle(int x, int y, int width, int height, boolean fill) {
		if(fill) {
			for(int i = 0; i < width; i++) {
				for(int j = 0; j < height; j++) {
					ssd1306.setPixel(x + i, y + j, true);
				}
			}
		} else if(width > 0 && height > 0) {
			line(x, y, x, y + height - 1);
			line(x, y + height - 1, x + width - 1, y + height - 1);
			line(x + width - 1, y + height - 1, x + width - 1, y);
			line(x + width - 1, y, x, y);
		}
	}

	/**
	 * Draw an arc.
	 *
	 * @param x The X position of the center of the arc.
	 * @param y The Y position of the center of the arc.
	 * @param radius The radius of the arc in pixels.
	 * @param startAngle The starting angle of the arc in degrees.
	 * @param endAngle The ending angle of the arc in degrees.
	 */
	public void arc(int x, int y, int radius, int startAngle, int endAngle) {
		for(int i = startAngle; i <= endAngle; i++) {
			ssd1306.setPixel(x + (int) Math.round(radius * Math.sin(Math.toRadians(i))), y + (int) Math.round(radius * Math.cos(Math.toRadians(i))), true);
		}
	}

	/**
	 * Draw a circle.
	 * This is the same as calling arc() with a start and end angle of 0 and 360 respectively.
	 *
	 * @param x The X position of the center of the circle.
	 * @param y The Y position of the center of the circle.
	 * @param radius The radius of the circle in pixels.
	 */
	public void circle(int x, int y, int radius) {
		arc(x, y, radius, 0, 360);
	}
}
