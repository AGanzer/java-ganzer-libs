package com.example.uitests.swingdv.doc.thumbnail;

import com.example.uitests.swingdv.Thumbnail;
import de.ganzer.dv.Document;
import de.ganzer.dv.View;
import de.ganzer.dv.ViewCreationInfo;
import de.ganzer.dv.ViewTemplate;
import de.ganzer.swing.dv.DVManager;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ThumbnailView<D extends Document> extends JPanel implements View<D> {
    private final ViewTemplate<?, ?> template;
    private final D document;
    private final ThumbnailPanel ownerPanel;
    private final JLabel title;
    private final JLabel thumbnail;

    @SuppressWarnings("unchecked")
    public ThumbnailView(ViewCreationInfo<?, ?> info, ThumbnailPanel ownerPanel) {
        super(new BorderLayout());

        this.template = info.getTemplate();
        this.document = (D) info.getDocument();
        this.ownerPanel = ownerPanel;

        this.title = new JLabel(getTitle());
        this.thumbnail = new JLabel();
        this.thumbnail.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        this.add(title, BorderLayout.NORTH);
        this.add(thumbnail, BorderLayout.CENTER);

        SwingUtilities.invokeLater(this::updateThumbnail);

        updateBorder();

        DVManager.addPropertyChangeListener(DVManager.ACTIVE_DOCUMENT_PROPERTY, evt -> {
            if (evt.getNewValue() == document || evt.getOldValue() == document)
                updateBorder();
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON1)
                    DVManager.activateDocument(document);
            }
        });
    }

    @Override
    public ViewTemplate<D, ?> getTemplate() {
        //noinspection unchecked
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
        ownerPanel.revalidate();
        ownerPanel.repaint();
    }

    public void setSize(int size) {
        var insets = getBorder().getBorderInsets(this);
        int innerSize = size - insets.left - insets.right;

        title.setPreferredSize(new Dimension(innerSize, title.getPreferredSize().height));
        thumbnail.setPreferredSize(new Dimension(innerSize, innerSize));
        var dim = new Dimension(size, title.getPreferredSize().height + size);
        setPreferredSize(dim);
        setMaximumSize(dim);

        revalidate();
        updateThumbnail();
        repaint();
    }

    private void updateBorder() {
        setBorder(DVManager.getActiveDocument() == document
                          ? BorderFactory.createLineBorder(UIManager.getColor("Component.focusColor"), 2)
                          : BorderFactory.createEmptyBorder(2, 2, 2, 2));
    }

    private void updateThumbnail() {
        var mainView = document.getViews().stream()
                .filter(v -> v.getTemplate().isDefault())
                .findFirst()
                .orElse(document.getViews().get(0));
        var borderInsets = thumbnail.getBorder().getBorderInsets(thumbnail);
        var borderWidth = borderInsets.left + borderInsets.right;
        var borderHeight = borderInsets.top + borderInsets.bottom;
        var image = Thumbnail.create((JComponent) mainView,
                                     thumbnail.getPreferredSize().width - borderWidth,
                                     thumbnail.getPreferredSize().height - borderHeight);
        thumbnail.setIcon(image != null ? new ImageIcon(image) : null);
    }
}
