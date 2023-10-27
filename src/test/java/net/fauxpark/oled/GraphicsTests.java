package net.fauxpark.oled;

import net.fauxpark.oled.font.CodePage437;
import net.fauxpark.oled.font.Font;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import net.fauxpark.oled.transport.MockTransport;

public class GraphicsTests {
    private SSD1306 ssd1306;

    private Graphics underTest;

    @BeforeEach
    void setUp() {
        ssd1306 = new SSD1306(128, 64, new MockTransport());
        ssd1306.startup(false);
        underTest = ssd1306.getGraphics();
    }

    @Test
    public void whenDrawArc_thenChangedBuffer() {
        // given
        int x = 63;
        int y = 31;
        int radius = 24;
        int startAngle = 0;
        int endAngle = 270;
        byte[] initialBuffer = ssd1306.getBuffer().clone();

        // when
        underTest.arc(x, y, radius, startAngle, endAngle);

        // then
        assertThat(ssd1306.getBuffer()).isNotEqualTo(initialBuffer);
    }

    @Test
    public void whenDrawCircle_thenChangedBuffer() {
        // given
        int x = 63;
        int y = 31;
        int radius = 24;
        byte[] initialBuffer = ssd1306.getBuffer().clone();

        // when
        underTest.circle(x, y, radius);

        // then
        assertThat(ssd1306.getBuffer()).isNotEqualTo(initialBuffer);
    }

    @Test
    public void whenDrawLine_thenChangedBuffer() {
        // given
        int x0 = 0;
        int y0 = 0;
        int x1 = 96;
        int y1 = 24;
        byte[] initialBuffer = ssd1306.getBuffer().clone();

        // when
        underTest.line(x0, y0, x1, y1);

        // then
        assertThat(ssd1306.getBuffer()).isNotEqualTo(initialBuffer);
    }

    @Test
    public void whenDrawRectangle_thenChangedBuffer() {
        // given
        int x0 = 16;
        int y0 = 16;
        int x1 = 96;
        int y1 = 24;
        byte[] initialBuffer = ssd1306.getBuffer().clone();

        // when
        underTest.rectangle(x0, y0, x1, y1, false);

        // then
        assertThat(ssd1306.getBuffer()).isNotEqualTo(initialBuffer);
    }

    @Test
    public void whenDrawFilledRectangle_thenDiffersFromUnfilled() {
        // given
        int x0 = 16;
        int y0 = 16;
        int x1 = 96;
        int y1 = 24;
        byte[] initialBuffer = ssd1306.getBuffer().clone();

        // when
        underTest.rectangle(x0, y0, x1, y1, false);
        byte[] unfilledRectBuffer = ssd1306.getBuffer().clone();
        underTest.rectangle(x0, y0, x1, y1, true);

        // then
        assertThat(ssd1306.getBuffer()).isNotEqualTo(initialBuffer);
        assertThat(ssd1306.getBuffer()).isNotEqualTo(unfilledRectBuffer);
    }

    @Test
    public void whenDrawText_thenChangedBuffer() {
        // given
        String text = "Hello world";
        Font font = new CodePage437();
        int x = 0;
        int y = 0;
        byte[] initialBuffer = ssd1306.getBuffer().clone();

        // when
        underTest.text(x, y, font, text);

        // then
        assertThat(ssd1306.getBuffer()).isNotEqualTo(initialBuffer);
    }

    @AfterEach
    public void tearDown() {
        ssd1306.shutdown();
    }
}
