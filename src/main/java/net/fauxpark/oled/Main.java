package net.fauxpark.oled;

import net.fauxpark.oled.conn.DisplayConnection;
import net.fauxpark.oled.conn.DisplayConnectionI2C;
import net.fauxpark.oled.conn.DisplayConnectionMock;
import net.fauxpark.oled.conn.DisplayConnectionSPI;
import net.fauxpark.oled.main.DisplayTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * just a dummy main for not always starting junit
 */
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static final String DEFAULT_CONNECTION_TYPE = "I2C";

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            showUsage();
            System.exit(0);
        }

        // read args
        String routine = args[0];
        String displayType = args[1];

        String connectionType = DEFAULT_CONNECTION_TYPE;
        if (args.length > 2) {
            connectionType = args[2];
        }


        // setup connection
        DisplayConnection dspConn = null;
        if ("SPI".equals(connectionType)) {
            dspConn = new DisplayConnectionSPI();
        } else if ("I2C".equals(connectionType)) {
            dspConn = new DisplayConnectionI2C();
        } else if ("Mock".equals(connectionType)) {
            dspConn = new DisplayConnectionMock();
        } else {
            System.err.println("unknown connection type: " + connectionType);
            System.exit(1);
        }

        // setup display
        SSDisplay display = null;
        if ("DSP1306_128_64".equals(displayType)) {
            display = new SSD1306Display(dspConn, 128, 64);
        } else if ("SSD1327".equals(displayType)) {
            display = new SSD1327Display(dspConn);
        } else {
            System.err.println("unknown display type: " + connectionType);
            System.exit(1);
        }


        // start selected routine
        if ("dspTest".equals(routine)) {
            DisplayTest dspTest = new DisplayTest(display);
            dspTest.run();
        } else {
            System.err.println("unknown routine type: " + routine);
            System.exit(1);
        }
    }


    public static void showUsage() {
        System.out.println("usage:");
        System.out.println("\trun.{sh|bat} RoutineName DisplayType [ConnectionType]");
        System.out.println("");
        System.out.println("example:        run.sh dspTest SSD1327");
        System.out.println("Routine:        dspTest");
        System.out.println("DisplayType:    DSP1306_128_64 | SSD1327");
        System.out.println("ConnectionType: I2C | SPI | Mock - default is: " + DEFAULT_CONNECTION_TYPE);
    }
}
