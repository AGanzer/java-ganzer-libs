package com.example.uitests.swing;

import javax.swing.*;
import java.net.URL;

public final class Images {
    public static Icon load(String id) {
        URL imageUrl = Images.class.getResource("images/" + id + ".png");

        if (imageUrl == null)
            return null;

        return new ImageIcon(imageUrl);
    }
}
