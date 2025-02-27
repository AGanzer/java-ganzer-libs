package de.ganzer.gv;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import de.ganzer.gv.core.Size;

public final class ApplicationSettings {
    private static Size shadowSize = new Size(2, 1);
    private static TextCharacter shadowAttributes =
            TextCharacter.fromString(" ",
                                     TextColor.ANSI.BLACK_BRIGHT,
                                     TextColor.ANSI.BLACK)[0];
    private static TextCharacter ErrorAttributes =
            TextCharacter.fromString(" ",
                                     TextColor.ANSI.WHITE,
                                     TextColor.ANSI.RED_BRIGHT,
                                     SGR.BLINK)[0];

    public static Size getShadowSize() {
        return shadowSize;
    }

    public static void setShadowSize(Size shadowSize) {
        ApplicationSettings.shadowSize = shadowSize;
    }

    public static TextCharacter getShadowAttributes() {
        return shadowAttributes;
    }

    public static void setShadowAttributes(TextCharacter shadowAttributes) {
        ApplicationSettings.shadowAttributes = shadowAttributes;
    }

    public static TextCharacter getErrorAttributes() {
        return ErrorAttributes;
    }

    public static void setErrorAttributes(TextCharacter errorAttributes) {
        ErrorAttributes = errorAttributes;
    }
}
