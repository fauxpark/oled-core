package net.fauxpark.oled;

import com.pi4j.io.gpio.Pin;
import net.fauxpark.oled.conn.DisplayConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SSD1306Display extends SSDisplay {
    private static final Logger logger = LoggerFactory.getLogger(SSD1306Display.class);


    public SSD1306Display(DisplayConnection dspConn, int width, int height) {
        super(dspConn, width, height);
    }

    public SSD1306Display(DisplayConnection dspConn, int width, int height, Pin rstPin) {
        super(dspConn, width, height, rstPin);
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
        setDisplayOn(false);

        dspConn.command(Command.SET_DISPLAY_CLOCK_DIV, width);  // from SPI
        // command(Command.SET_DISPLAY_CLOCK_DIV, 0x80);    // from I2C

        // TODO what´s this for?
        // command(Command.SET_MULTIPLEX_RATIO, width - 1);// from SPI
        dspConn.command(Command.SET_MULTIPLEX_RATIO, height == 64 ? 0x3F : 0x1F);// from I2C

        setOffset(0);
        dspConn.command(Command.SET_START_LINE_00);
        dspConn.command(Command.SET_CHARGE_PUMP, externalVcc ? Constant.CHARGE_PUMP_DISABLE : Constant.CHARGE_PUMP_ENABLE);
        dspConn.command(Command.SET_MEMORY_MODE, Constant.MEMORY_MODE_HORIZONTAL);
        setHFlipped(false);
        setVFlipped(false);
        dspConn.command(Command.SET_COM_PINS, height == 64 ? 0x12 : 0x02);

        setContrast(externalVcc ? 0x9F : 0xCF); // from SPI
        setContrast(height == 64 ? 0x8F : externalVcc ? 0x9F : 0xCF);   // from I2C

        dspConn.command(Command.SET_PRECHARGE_PERIOD, externalVcc ? 0x22 : 0xF1);
        dspConn.command(Command.SET_VCOMH_DESELECT, Constant.VCOMH_DESELECT_LEVEL_00);
        dspConn.command(Command.DISPLAY_ALL_ON_RESUME);

        super.startup(externalVcc);
    }
}
