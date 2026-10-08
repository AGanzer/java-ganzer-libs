package com.example.uitests.swingdv.doc.thumbnail;

import com.example.uitests.swingdv.Thumbnail;
import de.ganzer.dv.Document;
import de.ganzer.dv.View;
import de.ganzer.dv.ViewCreationInfo;
import de.ganzer.dv.ViewTemplate;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class ThumbnailView<D extends Document> extends JPanel implements View<D> {
    private final ViewTemplate<?, ?> template;
    private final D document;
    private final ThumbnailPanel ownerPanel;
    private final JLabel title;
    private final JLabel thumbnail;

    public ThumbnailView(ViewCreationInfo<?, ?> info, ThumbnailPanel ownerPanel) {
        super(new BorderLayout());

        this.template = info.getTemplate();
        this.document = (D) info.getDocument();
        this.ownerPanel = ownerPanel;

        this.title = new JLabel();
        this.thumbnail = new JLabel();
        this.thumbnail.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        this.add(title, BorderLayout.NORTH);
        this.add(thumbnail, BorderLayout.CENTER);
    }

    @Override
    public ViewTemplate<D, ?> getTemplate() {
        return (ViewTemplate<D, ?>) template;
    }

    @Override
    public D getDocument() {
        return document;
    }

    @Override
    public void updateTitle() {
        title.setText(getTitle());
    }

    @Override
    public void documentDataChanged(Object context) {
        updateThumbnail();
    }

    @Override
    public void toFront() {
    }

    @Override
    public void forceClose() {
        ownerPanel.remove(this);
    }

    public void setSize(int size) {
        title.setPreferredSize(new Dimension(size, title.getPreferredSize().height));
        title.setSize(size, title.getPreferredSize().height);

        thumbnail.setPreferredSize(new Dimension(size, size));
        thumbnail.setSize(size, size);

        updateThumbnail();
    }

    private void updateThumbnail() {
        var mainView = document.getViews().stream()
                .filter(v -> v.getTemplate().isDefault())
                .findFirst()
                .orElse(document.getViews().get(0));
        var borderInsets = getBorder().getBorderInsets(thumbnail);
        var borderWidth = borderInsets.left + borderInsets.right;
        var borderHeight = borderInsets.top + borderInsets.bottom;
        var image = Thumbnail.create((JComponent) mainView,
                                     thumbnail.getWidth() - borderWidth,
                                     thumbnail.getHeight() - borderHeight);
        thumbnail.setIcon(image != null ? new ImageIcon(image) : null);
    }
}
