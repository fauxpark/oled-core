# oled-core: SSD1306 in Java

This is a driver for the [Adafruit SSD1306 OLED display](https://www.adafruit.com/categories/98) on the Raspberry Pi.
It makes use of the [Pi4J](https://github.com/Pi4J/pi4j) library, which does all the fiddly bits in native code to operate the GPIO pins and drive the SPI and I<sup>2</sup>C interfaces.

The aim of this project is to abstract away the low-level aspects of the SSD1306 and focus on manipulating the contents of the screen through a very simple API.

## Prerequisites

Firstly, enable the I<sup>2</sup>C and/or SPI peripherals in `raspi-config`.

Then, install PiGpio and Java. On Raspbian:

```
$ sudo apt install pigpio default-jre-headless
```

Note that any applications built with this library must be run with `sudo`, due to its use of PiGpio.

### GPIO Pinout

The pinout for the Raspberry Pi GPIO header is as follows:

|  Display Pin | Physical Pin (SPI)      | Physical Pin (I<sup>2</sup>C)  |
|-------------:|:------------------------|:-------------------------------|
|       Ground | 6                       | 6                              |
|   Voltage In | 1                       | 1                              |
|          3v3 | N/C                     | N/C                            |
|  Chip Select | 24 (`CS0`) / 26 (`CS1`) | N/C                            |
|        Reset | 8 (GPIO 14) / Any       | 8 (GPIO 14) / Any              |
| Data/Command | 10 (GPIO 15) / Any      | N/C (`0x3D`) / Ground (`0x3C`) |
|        Clock | 23 (GPIO 11)            | 5 (GPIO 3)                     |
|         Data | 19 (GPIO 10)            | 3 (GPIO 2)                     |

## Getting Started

To set up the display, simply create a new `Transport` object, and pass it to an `SSD1306` object, like so:

```java
Transport transport = new I2CTransport(14, 1, 0x3D);
// Or:
Transport transport = new SPITransport(0, 14, 15);

SSD1306 ssd1306 = new SSD1306(128, 64, transport);

// false indicates no external VCC
ssd1306.startup(false);

// Turns the pixel in the top left corner on
ssd1306.setPixel(0, 0, true);

// Sends the internal buffer to the display
ssd1306.display();

// Inverts the display
ssd1306.setInverted(true);

// Flips the display upside down
ssd1306.setVFlipped(true);
```

If you are testing on something other than a Raspberry Pi, you can use the `MockTransport` class instead to mostly simulate the display without Pi4J complaining about your platform. However, some features will not be available (such as scrolling, as it is done by the display itself).

Most properties of the display (eg. invertedness, display on/off) are reachable through getters and setters.
As the SSD1306 does not provide any information as to its state, these are implemented as fields in the `SSD1306` class.

## Basic Graphics

You can also do some basic line & shape drawing using the `Graphics` class.
Just call the `getGraphics()` method on the SSD1306 instance:

```java
Transport transport = new SPITransport(0, 14, 15);
SSD1306 ssd1306 = new SSD1306(128, 64, transport);
Graphics graphics = ssd1306.getGraphics();

ssd1306.startup(false);

// Draws a line from the top left to the bottom right of the display
graphics.line(0, 0, 127, 63);

// Draws an arc from (63,31) with a radius of 8 pixels and an angle of 15 degrees
graphics.arc(63, 31, 8, 0, 15);

// Writes "Hello world!" at (20,20) using the Windows-1252 charset
graphics.text(20, 20, new CodePage1252(), "Hello world!");
```

## Text Rendering

As you can see above, this library can draw text onto the screen. It is also possible to change the character set.

Currently three character sets are supported:

- [**CP437**](https://en.wikipedia.org/wiki/Code_page_437) (from the IBM PC)
- [**CP850**](https://en.wikipedia.org/wiki/Code_page_850) (MS-DOS, also used in the Windows Command Prompt)
- [**Windows-1252**](https://en.wikipedia.org/wiki/Windows-1252) (the first 256 codepoints of Unicode; use this for the closest mapping between entered text and display output)

In addition, it is possible to create your own character sets by implementing `Font` and specifying the number of rows and columns, and the array of glyph data. Refer to the `Font` JavaDoc for an explanation on how the glyphs are encoded.

### Credits

Some of this code has been borrowed from [py-gaugette](https://github.com/guyc/py-gaugette), [pi-ssd1306-java](https://github.com/ondryaso/pi-ssd1306-java), and [raspberry-pi4j-samples](https://github.com/OlivierLD/raspberry-pi4j-samples/).

The glyph data for the CP437 character set has been modified somewhat but appears to have originated from the [Adafruit GFX Library](https://github.com/adafruit/Adafruit-GFX-Library).
