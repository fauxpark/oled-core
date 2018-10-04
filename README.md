# oled-core: SSDxxxx in Java

[![Build Status](https://travis-ci.org/cljk/oled-core.svg?branch=master)](https://travis-ci.org/cljk/oled-core)

This is a driver for Solomon Systech Displays (SSD1306, SSD1327 - f.e. [Adafruit SSD1306 OLED display](https://www.adafruit.com/categories/98) and others) on the Raspberry Pi, written (almost) completely in Java.
It makes use of the [Pi4J](https://github.com/Pi4J/pi4j) library, which does all the fiddly bits in native code to operate the GPIO pins and drive the SPI and I<sup>2</sup>C interfaces.

The aim of this project is to abstract away the low-level aspects of the SSD and focus on manipulating the contents of the screen through a very simple API.

![example of SSD1327](https://raw.githubusercontent.com/cljk/oled-core/master/src/doc/img/img_001.png)

### Display support

| Device                              | state                |
|-------------------------------------|----------------------|
| SSD1306                             | working SPI and I2C  |
| SSD1327 (128px * 128px * 4bit grey) | in progress          |

Other types should be easy to add if pixel-bitsize is the same


## GPIO Pinout

The pinout for the Raspberry Pi GPIO header is as follows:

| Display Pin  | Physical Pin (SPI)  | Physical Pin (I<sup>2</sup>C) |
| ------------:|:------------------- |:----------------------------- |
| Ground       | 6                   | 6                             |
| Voltage In   | 1                   | 1                             |
| 3v3          | N/C                 | N/C                           |
| Chip Select  | 24 (CS0) / 26 (CS1) | N/C                           |
| Reset        | 8 (GPIO_15) / Any   | 8 (GPIO_15) / Any (or N/C)    |
| Data/Command | 10 (GPIO_16) / Any  | N/C (0x3D) / GND (0x3C)       |
| Clock        | 23                  | 5                             |
| Data         | 19                  | 3                             |

## Getting Started 

### Start display test on RPi
Compile and show valid options (you have to be root to use PI4J GPIO) 
```bash
# sudo su
# mvn compile
# ./run.sh 
```
will (/should) display
```
assume classes are already build (with f.e. maven in target/classes)
usage:
        run.{sh|bat} RoutineName DisplayType [ConnectionType]

example:        run.sh dspTest SSD1327
Routine:        dspTest
DisplayType:    DSP1306_128_64 | SSD1327
ConnectionType: I2C | SPI - default is: I2C
```
start for example with
```bash
# ./run.sh dspTest SSD1327 I2C
```

### Java

To set up the display, simply create a new `SSD1306` object, like so:

```java
DisplayConnection dspConn;
SSDisplay display;

// choose connection type SPI
dspConn = new DisplayConnectionSPI();

// or I2C
dspConn = new DisplayConnectionI2C();


// choose display type SSD1306
display = new SSD1306(dspConn, 128, 64);

// or SSD1327
display = new SSD1327(dspConn);


// call startup
display.startup(false);

// and start using it..

// Turns the pixel in the top left corner on
display.setPixel(0, 0, true);

// Sends the internal buffer to the display
display.display();

// Inverts the display
display.setInverted(true);
```

Most properties of the display (eg. invertedness, display on/off) are reachable through getters and setters.
As the SSD controllers do not provide any information as to its state, these are implemented as fields in the `SSD1306` class.

## 2D Graphics with AWT

You can also do line & shape drawing using the `Graphics2D` class from `java.awt`.
Just call the `getGraphics2D()` method on the `SSDisplay` instance:

```java
// SSDisplay display = new SSD1306(dspConn, 128, 64);
SSDisplay display = new SSD1327(new DisplayConnectionI2C());
display.startup(false);

// make use of Java AWT Graphics2D
Graphics2D graphics = display.getGraphics2D();

// Draws a line from the top left to the bottom right of the display
graphics.drawRect(0, 0, display.getWidth()-1, display.getHeight()-1);

// Draws an arc from (63,31) with a radius of 8 pixels and an angle of 15 degrees
graphics.drawArc(display.getWidth() -25, 10, 30,30, 0, 360);

// Writes "Hello world!" at (20,20) using the Windows-1252 charset
Font font = new Font("Serif", Font.BOLD, 18);
graphics.setFont(font);
graphics.drawString( "Hello world!", 5, 20);

display.rasterGraphics2DImage(true);
```

## Legacy Text Rendering

This library can draw text onto the screen (wihtout using `java.awt`). It is also possible to change the character set.

Currently three character sets are supported:

- [**CP437**](https://en.wikipedia.org/wiki/Code_page_437) (from the IBM PC)
- [**CP850**](https://en.wikipedia.org/wiki/Code_page_850) (MS-DOS, also used in the Windows Command Prompt)
- [**Windows-1252**](https://en.wikipedia.org/wiki/Windows-1252) (the first 256 codepoints of Unicode; use this for the closest mapping between entered text and display output)

In addition, it is possible to create your own character sets by implementing `Font` and specifying the number of rows and columns, and the array of glyph data. Refer to the `Font` JavaDoc for an explanation on how the glyphs are encoded.

## Issues

### I2C Mode (& grey-scale displays - like SDD1327)

Because of the larger amounts of data needed to transfer on every display update
when using grayscale-displays
(f.e. the SDD1327 has 128 x 128 pixel x 4 bits per pixel = 8129 bytes), perhaps you may consider speeding up your I2C bus from default 100 kHz to 400 kHz.

Refer to [https://www.raspberrypi-spy.co.uk/2018/02/change-raspberry-pi-i2c-bus-speed/](https://www.raspberrypi-spy.co.uk/2018/02/change-raspberry-pi-i2c-bus-speed/)

This might not be such a huge problem using sw-displays but might be a limiting factor there too.
The display update of SSD1306 with 128 x 64 px x 1 BPP consumes 1024 bytes.

## Credits

Some of this code has been borrowed from 
* [py-gaugette](https://github.com/guyc/py-gaugette)
* [pi-ssd1306-java](https://github.com/ondryaso/pi-ssd1306-java)
* [raspberry-pi4j-samples](https://github.com/OlivierLD/raspberry-pi4j-samples/)

Essential basis from
* [PI4J](http://pi4j.com/)


The glyph data for the CP437 character set has been modified somewhat but appears to have originated from the [Adafruit GFX Library](https://github.com/adafruit/Adafruit-GFX-Library).
