package net.fauxpark.oled;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SSD1327DisplayTest {
    private static final Logger logger = LoggerFactory.getLogger(SSD1327DisplayTest.class);
    SSD1327 display;

    @Before
    public void setup() {
        display = new SSD1327(null);
    }

    @Test
    public void test_getBufferArrayElementForPixel() {
        int el = 0;

        el = checkGetBufferArrayElement(0, 0);
        Assert.assertEquals(0, el);

        el = checkGetBufferArrayElement(1, 0);
        Assert.assertEquals(0, el);

        el = checkGetBufferArrayElement(2, 0);
        Assert.assertEquals(1, el);

        el = checkGetBufferArrayElement(3, 0);
        Assert.assertEquals(1, el);


        el = checkGetBufferArrayElement(SSD1327.WIDTH - 1, 0);
        Assert.assertEquals(SSD1327.WIDTH/2 -1, el);

        el = checkGetBufferArrayElement(0, 1);
        Assert.assertEquals(SSD1327.WIDTH/2, el);



    }

    private int checkGetBufferArrayElement(int i, int i2) {
        int el;
        el = SSD1327.getBufferArrayElementForPixel(i, i2);
        logger.debug("checkGetBufferArrayElement: x{}, y{} -> {}", i, i2, el);
        return el;
    }
}
