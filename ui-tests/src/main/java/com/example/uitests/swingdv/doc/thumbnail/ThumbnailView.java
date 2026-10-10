package com.example.uitests.swingdv.doc.thumbnail;

import com.example.uitests.swingdv.Thumbnail;
import de.ganzer.dv.Document;
import de.ganzer.dv.View;
import de.ganzer.dv.ViewCreationInfo;
import de.ganzer.dv.ViewTemplate;
import de.ganzer.swing.dv.DVManager;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ThumbnailView<D extends Document> extends JPanel implements View<D> {
    private final ViewTemplate<?, ?> template;
    private final D document;
    private final ThumbnailPanel ownerPanel;
    private final Title title;
    private final JLabel thumbnail;

    @SuppressWarnings("unchecked")
    public ThumbnailView(ViewCreationInfo<?, ?> info, ThumbnailPanel ownerPanel) {
        super(new BorderLayout());

        this.template = info.getTemplate();
        this.document = (D) info.getDocument();
        this.ownerPanel = ownerPanel;

        this.title = new Title(getTitle());
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

        document.addPropertyChangeListener(Document.VIEWS_PROPERTY, e -> updateThumbnail());
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
                .orElse(null);

        if (mainView == null){
            mainView = document.getViews().stream()
                    .filter(v -> !(v instanceof ThumbnailView))
                    .findFirst()
                    .orElse(null);
        }

        if (mainView == null)
            return;

        var borderInsets = thumbnail.getBorder().getBorderInsets(thumbnail);
        var borderWidth = borderInsets.left + borderInsets.right;
        var borderHeight = borderInsets.top + borderInsets.bottom;
        var tnWidth = thumbnail.getPreferredSize().width - borderWidth;
        var tnHeight = thumbnail.getPreferredSize().height - borderHeight;

        if (tnWidth <= 0 || tnHeight <= 0)
            return;

        var image = Thumbnail.create((JComponent) mainView, tnWidth, tnHeight);

        thumbnail.setIcon(image != null ? new ImageIcon(image) : null);
    }

    private class Title extends JPanel {
        private final JLabel label;

        private Title(String text) {
            super(new BorderLayout(5, 0));

            label = new JLabel(text);
            add(label, BorderLayout.CENTER);

            if (getTemplate().isClosable()) {
                var button = new Button();
                add(button, BorderLayout.EAST);
            }
        }

        public void setText(String text) {
            label.setText(text);
        }

        private class Button extends JButton implements ActionListener {
            public Button() {
                int size = 17;

                setPreferredSize(new Dimension(size, size));
                setToolTipText("Close");
                setUI(new BasicButtonUI());
                setContentAreaFilled(false);
                setFocusable(false);
                setBorder(BorderFactory.createEtchedBorder());
                setBorderPainted(false);
                setRolloverEnabled(true);

                addActionListener(this);
            }

            public void updateUI() {
                // Don't update UI for this button.
            }

            public void actionPerformed(ActionEvent e) {
                if (document.canClose())
                    document.close();
            }

            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g.create();

                if (getModel().isPressed())
                    g2.translate(1, 1);

                g2.setStroke(new BasicStroke(2));

                setVisible(isEnabled());

                if (getModel().isRollover())
                    g2.setColor(Color.MAGENTA);
                else
                    g2.setColor(getForeground());

                int delta = 6;

                g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
                g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);

                g2.dispose();
            }
        }
    }
}
