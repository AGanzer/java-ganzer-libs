package de.ganzer.gv;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import de.ganzer.gv.core.Size;
import de.ganzer.gv.events.PropertyChangedEvent;
import de.ganzer.gv.events.PropertyChangedListener;
import de.ganzer.gv.events.PropertyChangedSupport;

import java.util.Objects;

public final class ApplicationSettings {
    private static final PropertyChangedSupport<ApplicationSettings> propertyChangedSupport = new PropertyChangedSupport<>();

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
    private static int scrollDelay = 250;
    private static int scrollRepeat = 50;
    private static int mouseWheelLines = 3;

    public static Size getShadowSize() {
        return shadowSize;
    }

    public static void setShadowSize(Size shadowSize) {
        Objects.requireNonNull(shadowSize, "shadowSize must not be null.");

        if (ApplicationSettings.shadowSize.equals(shadowSize))
            return;

        ApplicationSettings.shadowSize = shadowSize;
        propertyChangedSupport.fireEvent(new PropertyChangedEvent<>(null, "shadowSize"));
    }

    public static TextCharacter getShadowAttributes() {
        return shadowAttributes;
    }

    public static void setShadowAttributes(TextCharacter shadowAttributes) {
        Objects.requireNonNull(shadowAttributes, "Shadow attributes must not be null.");

        if (ApplicationSettings.shadowAttributes.equals(shadowAttributes))
            return;

        ApplicationSettings.shadowAttributes = shadowAttributes;
        propertyChangedSupport.fireEvent(new PropertyChangedEvent<>(null, "shadowAttributes"));
    }

    public static TextCharacter getErrorAttributes() {
        return ErrorAttributes;
    }

    public static void setErrorAttributes(TextCharacter errorAttributes) {
        Objects.requireNonNull(errorAttributes, "Error attributes must not be null.");

        if (ApplicationSettings.ErrorAttributes.equals(errorAttributes))
            return;

        ErrorAttributes = errorAttributes;
        propertyChangedSupport.fireEvent(new PropertyChangedEvent<>(null, "ErrorAttributes"));
    }

    public static int getScrollDelay() {
        return scrollDelay;
    }

    public static void setScrollDelay(int scrollDelay) {
        if (ApplicationSettings.scrollDelay == scrollDelay)
            return;

        ApplicationSettings.scrollDelay = scrollDelay;
        propertyChangedSupport.fireEvent(new PropertyChangedEvent<>(null, "scrollDelay"));
    }

    public static int getScrollRepeat() {
        return scrollRepeat;
    }

    public static void setScrollRepeat(int scrollRepeat) {
        if (ApplicationSettings.scrollRepeat == scrollRepeat)
            return;

        ApplicationSettings.scrollRepeat = scrollRepeat;
        propertyChangedSupport.fireEvent(new PropertyChangedEvent<>(null, "scrollRepeat"));
    }

    public static int getMouseWheelLines() {
        return mouseWheelLines;
    }

    public static void setMouseWheelLines(int mouseWheelLines) {
        if (ApplicationSettings.mouseWheelLines == mouseWheelLines)
            return;

        ApplicationSettings.mouseWheelLines = mouseWheelLines;
        propertyChangedSupport.fireEvent(new PropertyChangedEvent<>(null, "mouseWheelLines"));
    }

    public void addPropertyChangedListener(PropertyChangedListener<ApplicationSettings> listener) {
        propertyChangedSupport.addListener(listener);
    }

    public void removePropertyChangedListener(PropertyChangedListener<ApplicationSettings> listener) {
        propertyChangedSupport.removeListener(listener);
    }
}
