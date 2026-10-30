package com.example.uitests.swingdv.doc.image;

import com.example.uitests.swingdv.doc.MDISubView;
import de.ganzer.dv.ViewCreationInfo;
import de.ganzer.swing.controls.ClosableTabsPane;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;

public class ImageView extends MDISubView<ImageDocument> {
    public ImageView(ViewCreationInfo<ImageDocument, ImageView> info, ClosableTabsPane tabPane) {
        super(info, tabPane, false);

        var label = new JLabel(new ImageIcon(getDocument().getImage()));
        var scroller = new JScrollPane(label);

        add(scroller, BorderLayout.CENTER);
    }

    @Override
    public void documentDataChanged(Object context) {
    }
}
