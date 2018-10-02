package net.fauxpark.oled;

import com.pi4j.io.gpio.Pin;
import net.fauxpark.oled.impl.SSD1306I2CImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class SSD1306Display extends SSDisplay {
    private static final Logger logger = LoggerFactory.getLogger(SSD1306Display.class);


    public SSD1306Display(int width, int height) {
        super(width, height);
    }

    public SSD1306Display(int width, int height, Pin rstPin) {
        super(width, height, rstPin);
    }

    @Override
    public void startup(boolean externalVcc) {
        reset();
        setDisplayOn(false);

        command(Command.SET_DISPLAY_CLOCK_DIV, width);  // from SPI
        // command(Command.SET_DISPLAY_CLOCK_DIV, 0x80);    // from I2C

        // TODO what´s this for?
        // command(Command.SET_MULTIPLEX_RATIO, width - 1);// from SPI
        command(Command.SET_MULTIPLEX_RATIO, height == 64 ? 0x3F : 0x1F);// from I2C

        setOffset(0);
        command(Command.SET_START_LINE_00);
        command(Command.SET_CHARGE_PUMP, externalVcc ? Constant.CHARGE_PUMP_DISABLE : Constant.CHARGE_PUMP_ENABLE);
        command(Command.SET_MEMORY_MODE, Constant.MEMORY_MODE_HORIZONTAL);
        setHFlipped(false);
        setVFlipped(false);
        command(Command.SET_COM_PINS, height == 64 ? 0x12 : 0x02);

        setContrast(externalVcc ? 0x9F : 0xCF); // from SPI
        setContrast(height == 64 ? 0x8F : externalVcc ? 0x9F : 0xCF);   // from I2C

        command(Command.SET_PRECHARGE_PERIOD, externalVcc ? 0x22 : 0xF1);
        command(Command.SET_VCOMH_DESELECT, Constant.VCOMH_DESELECT_LEVEL_00);
        command(Command.DISPLAY_ALL_ON_RESUME);


        super.startup(externalVcc);
    }
}
