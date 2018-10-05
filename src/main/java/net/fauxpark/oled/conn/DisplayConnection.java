package net.fauxpark.oled.conn;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * skeleton of basic DisplayConnection with no reference to GPIO / PI4J
 *
 * useful for debugging without PI4J
 */
public abstract class DisplayConnection {
    private static final Logger logger = LoggerFactory.getLogger(DisplayConnection.class);

    /**
     * sends a command
     *
     * @param command
     * @param params
     */
    abstract public void command(int command, int... params) throws IOException ;

    /**
     * sends data
     *
     * @param data
     */
    abstract public void data(byte[] data) throws IOException;

    /** outputs parts of array */
    abstract public void data(byte[] data, int start, int len) throws IOException;

    /**
     * reset the display - should be overridden
     */
    public void reset() {
        logger.warn("no reset implementation");
    }

    /**
     * shutdown display (and IO?) - should be overridden
     */
    public void shutdown() {
        logger.warn("no shutdown implementation");
    }
}
