package net.fauxpark.oled.font;

/**
 * A basic interface to facilitate font selection in text drawing methods.
 * <br/>
 * These fonts generally include 256 glyphs, comprised of <i>columns</i> integer values containing <i>rows</i> bits of information.
 * An "on" bit in the value represents an "on" bit in the display RAM (and thus, in normal display mode, a lit pixel).
 * The top of a glyph is the least significant bit of each column.
 *
 * @author fauxpark
 */
public interface Font {
	/**
	 * Get the font name.
	 *
	 * @return The font name, in the format "{name}-{columns}x{rows}".
	 */
	public String getName();
	/**
	 * Get the number of columns in the font.
	 *
	 * @return The font's column count.
	 */
	public int getColumns();

	/**
	 * Get the number of rows in the font.
	 *
	 * @return The font's row count.
	 */
	public int getRows();

	/**
	 * Get the glyph data for the font.
	 *
	 * @return An array of ints representing the columns for each glyph.
	 */
	public int[] getGlyphs();
}
