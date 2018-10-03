package net.fauxpark.oled;

import com.pi4j.io.gpio.Pin;
import net.fauxpark.oled.conn.DisplayConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SSD1327Display extends SSDisplay {
    private static final Logger logger = LoggerFactory.getLogger(SSD1327Display.class);

    public static final int width = 128;
    public static final int height = 128;


    public SSD1327Display(DisplayConnection dspConn) {
        super(dspConn, width, height);
    }


    /**
     * Startup specific to 1306
     *
     * @param externalVcc Indicates whether the display is being driven by an external power source.
     */
    @Override
    public void startup(boolean externalVcc) {
        logger.debug("startup");
        reset();
        super.startup(externalVcc);

        setDisplayOn(false);
    }
}
