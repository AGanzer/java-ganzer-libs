package com.example.uitests.swingdv.doc.thumbnail;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import java.awt.Component;

public class ThumbnailPanel extends JPanel {
    private final Component glue;

    public ThumbnailPanel() {
        super(null);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(glue = Box.createVerticalGlue());
    }

    public void addThumbnail(ThumbnailView<?> thumbnailView) {
        remove(glue);
        add(thumbnailView);
        add(glue);

        thumbnailView.setSize(getWidth());
    }

    public void setWidth(int width) {
        for (Component component : getComponents()) {
            if (component instanceof ThumbnailView<?> view)
                view.setSize(width);
        }

        revalidate();
        repaint();
    }
}
