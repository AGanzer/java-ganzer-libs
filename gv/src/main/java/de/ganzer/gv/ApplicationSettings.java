package de.ganzer.gv;

import de.ganzer.gv.core.Size;

public final class ApplicationSettings {
    private static Size shadowSize = new Size(2, 1);

    public static Size getShadowSize() {
        return shadowSize;
    }

    public static void setShadowSize(Size shadowSize) {
        ApplicationSettings.shadowSize = shadowSize;
    }
}
