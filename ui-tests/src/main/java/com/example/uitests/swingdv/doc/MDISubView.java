package com.example.uitests.swingdv.doc;

import de.ganzer.dv.Document;
import de.ganzer.dv.View;
import de.ganzer.dv.ViewCreationInfo;
import de.ganzer.dv.ViewTemplate;
import de.ganzer.swing.controls.ClosableTabsPane;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;

public abstract class MDISubView<D extends Document> extends JPanel implements View<D> {
    private final ChangeListener tabChangeListener = new TabChangeListener();
    private final ViewTemplate<D, ? extends View<D>> template;
    private final D document;
    private final ClosableTabsPane tabPane;

    protected MDISubView(ViewCreationInfo<D, ? extends View<D>> info, ClosableTabsPane tabPane, boolean forceFocus) {
        super(new BorderLayout());

        this.template = info.getTemplate();
        this.document = info.getDocument();
        this.tabPane = tabPane;

        if (forceFocus)
            tabPane.addChangeListener(tabChangeListener);
    }

    @Override
    public ViewTemplate<D, ? extends View<D>> getTemplate() {
        return template;
    }

    @Override
    public D getDocument() {
        return document;
    }

    @Override
    public void updateTitle() {
        int index = tabPane.indexOfComponent(this);
        var title = getTitle();

        tabPane.setTitleAt(index, title);
        tabPane.setToolTipTextAt(index, title.equals(getDocument().getName()) ? null : getDocument().getName());
    }

    @Override
    public void toFront() {
        tabPane.setSelectedComponent(this);
    }

    @Override
    public void forceClose() {
        tabPane.removeChangeListener(tabChangeListener);
        tabPane.remove(this);
    }

    protected void focusContent() {
    }

    private class TabChangeListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            if (tabPane.getSelectedComponent() == MDISubView.this)
                focusContent();
        }
    }
}
