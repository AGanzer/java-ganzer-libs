package de.ganzer.swing.internals;

import javax.swing.ImageIcon;
import java.util.Objects;

public final class Images {
    public static ImageIcon load(String id) {
        Objects.requireNonNull(id, "id must not be null.");
        return new ImageIcon(Objects.requireNonNull(Images.class.getResource("/de/ganzer/swing/images/" + id + ".png")));
    }
}
