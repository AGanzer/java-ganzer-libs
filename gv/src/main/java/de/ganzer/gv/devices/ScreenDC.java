package de.ganzer.gv.devices;

import com.googlecode.lanterna.screen.Screen;

/**
 * The ScreenDC class defines a DC that enables writing into the whole screen
 * buffer.
 */
public class ScreenDC {
    private static Screen screen;
    private static int lockCount;
}
