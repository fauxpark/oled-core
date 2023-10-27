package net.fauxpark.oled;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import net.fauxpark.oled.transport.MockTransport;

public class SSD1306Tests {
    private SSD1306 underTest;

    @BeforeEach
    void setUp() {
        underTest = new SSD1306(128, 64, new MockTransport());
        underTest.startup(false);
    }

    @Test
    public void whenInitialState_thenShouldMatch() {
        assertThat(underTest.isInitialised()).isTrue();
        assertThat(underTest.isDisplayOn()).isTrue();
        assertThat(underTest.isHFlipped()).isFalse();
        assertThat(underTest.isVFlipped()).isFalse();
        assertThat(underTest.isInverted()).isFalse();
        assertThat(underTest.isScrolling()).isFalse();
    }

    @Test
    public void givenNewBufferElementValue_whenSetBuffer_thenChangedBuffer() {
        // given
        byte[] initialBuffer = underTest.getBuffer().clone();
        byte[] newBuffer = underTest.getBuffer().clone();
        byte newBufferValue = 0x55;

        // when
        newBuffer[0] = newBufferValue;
        underTest.setBuffer(newBuffer);

        // then
        assertThat(underTest.getBuffer()).isNotEqualTo(initialBuffer);
    }

    @Test
    public void givenPixelCoords_whenSetPixel_thenPixelOn() {
        // given
        int x = 0;
        int y = 0;
        boolean on = true;

        // when
        boolean drawn = underTest.setPixel(x, y, on);

        // then
        assertThat(drawn).isTrue();
        assertThat(underTest.getPixel(x, y)).isEqualTo(on);
    }

    @Test
    public void givenOffScreenPixel_whenDrawn_thenUnchangedBuffer() {
        // given
        int x = -1;
        int y = -1;
        byte[] initialBuffer = underTest.getBuffer().clone();

        // when
        boolean drawn = underTest.setPixel(x, y, true);

        // then
        assertThat(drawn).isFalse();
        assertThat(underTest.getBuffer()).isEqualTo(initialBuffer);
    }

    @Test
    public void givenPixelStates_whenInverted_thenUnchangedPixelStates() {
        // given
        boolean inverted = true;

        int offPixelX = 0;
        int offPixelY = 0;
        boolean offPixelState = false;

        int onPixelX = 1;
        int onPixelY = 1;
        boolean onPixelState = true;

        // when
        underTest.setPixel(offPixelX, offPixelY, offPixelState);
        underTest.setPixel(onPixelX, onPixelY, onPixelState);
        underTest.setInverted(inverted);

        // then
        assertThat(underTest.isInverted()).isTrue();
        assertThat(underTest.getPixel(offPixelX, offPixelY)).isEqualTo(offPixelState);
        assertThat(underTest.getPixel(onPixelX, onPixelY)).isEqualTo(onPixelState);
    }

    @Test
    public void givenPixelStates_whenDisplayOff_thenUnchangedPixelStates() {
        // given
        boolean displayState = false;

        int offPixelX = 0;
        int offPixelY = 0;
        boolean offPixelState = false;

        int onPixelX = 1;
        int onPixelY = 1;
        boolean onPixelState = true;

        // when
        underTest.setPixel(offPixelX, offPixelY, offPixelState);
        underTest.setPixel(onPixelX, onPixelY, onPixelState);
        underTest.setDisplayOn(displayState);

        // then
        assertThat(underTest.isDisplayOn()).isFalse();
        assertThat(underTest.getPixel(offPixelX, offPixelY)).isEqualTo(offPixelState);
        assertThat(underTest.getPixel(onPixelX, onPixelY)).isEqualTo(onPixelState);
    }

    @Test
    public void givenPixelStates_whenHFlipped_thenUnchangedPixelStates() {
        // given
        int topLeftPixelX = 0;
        int topLeftPixelY = 0;

        int topRightPixelX = underTest.getWidth() - topLeftPixelX;
        int topRightPixelY = 0;

        // when
        underTest.setPixel(topLeftPixelX, topLeftPixelY, true);
        underTest.setPixel(topRightPixelX, topRightPixelY, true);
        underTest.setHFlipped(true);

        // then
        assertThat(underTest.isHFlipped()).isTrue();
        assertThat(underTest.getPixel(topLeftPixelX, topLeftPixelY)).isTrue();
        assertThat(underTest.getPixel(topRightPixelX, topRightPixelY)).isFalse();
    }

    @Test
    public void givenPixelStates_whenVFlipped_thenUnchangedPixelStates() {
        // given
        int topLeftPixelX = 0;
        int topLeftPixelY = 0;

        int bottomLeftPixelX = 0;
        int bottomLeftPixelY = underTest.getHeight() - topLeftPixelY;

        // when
        underTest.setPixel(topLeftPixelX, topLeftPixelY, true);
        underTest.setPixel(bottomLeftPixelX, bottomLeftPixelY, true);
        underTest.setVFlipped(true);

        // then
        assertThat(underTest.isVFlipped()).isTrue();
        assertThat(underTest.getPixel(topLeftPixelX, topLeftPixelY)).isTrue();
        assertThat(underTest.getPixel(bottomLeftPixelX, bottomLeftPixelY)).isFalse();
    }

    @Test
    public void givenPixelStates_whenScreenCleared_thenChangedPixelStates() {
        // given
        int offPixelX = 0;
        int offPixelY = 0;

        int onPixelX = 1;
        int onPixelY = 1;

        // when
        underTest.setPixel(offPixelX, offPixelY, false);
        underTest.setPixel(onPixelX, onPixelY, true);
        underTest.clear();

        // then
        assertThat(underTest.getPixel(offPixelX, offPixelY)).isFalse();
        assertThat(underTest.getPixel(onPixelX, onPixelY)).isFalse();
    }

    @Test
    public void givenBrightnessLevel_whenSetBrightness_thenChangedBrightness() {
        // given
        int newBrightnessLevel = 40;

        // when
        underTest.setContrast(newBrightnessLevel);

        // then
        assertThat(underTest.getContrast()).isEqualTo(newBrightnessLevel);
    }

    @Test
    public void givenBrightnessLevelGreaterThanMax_whenSetBrightness_thenMaxBrightnessLevel() {
        // given
        int invalidBrightnessLevel = 256;
        int clampedBrightnessLevel = 255;

        // when
        underTest.setContrast(invalidBrightnessLevel);

        // then
        assertThat(underTest.getContrast()).isEqualTo(clampedBrightnessLevel);
    }

    @Test
    public void givenBrightnessLevelLessThanMin_whenSetBrightness_thenMinBrightnessLevel() {
        // given
        int invalidBrightnessLevel = -1;
        int clampedBrightnessLevel = 0;

        // when
        underTest.setContrast(invalidBrightnessLevel);

        // then
        assertThat(underTest.getContrast()).isEqualTo(clampedBrightnessLevel);
    }

    @AfterEach
    public void tearDown() {
        underTest.shutdown();
    }
}
