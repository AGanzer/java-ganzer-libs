package com.example.uitests.swing;

import javax.swing.*;

public final class Images {
    public static Icon load(String id) {
        var imageUrl = Images.class.getResource("images/" + id + ".png");

        if (imageUrl == null)
            return null;

        return new ImageIcon(imageUrl);
    }
}
