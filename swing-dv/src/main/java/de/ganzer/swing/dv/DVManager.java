package de.ganzer.swing.dv;

import de.ganzer.core.OS;
import de.ganzer.core.Services;
import de.ganzer.core.util.Settings;
import de.ganzer.dv.*;
import de.ganzer.dv.services.DVNavigationService;
import de.ganzer.swing.actions.GAction;
import de.ganzer.swing.actions.GActionGroup;
import de.ganzer.swing.actions.GToggleActionGroup;
import de.ganzer.swing.dv.internals.SwingDVMessages;
import de.ganzer.swing.util.EditorTracer;

import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

/**
 * A document manager for Swing-based GUI applications.
 * <p>
 * The manager needs some support to perform certain operations that depend
 * on the used UI framework. This support should be installed once at
 * application startup.
 * <p>
 * The manager provides all actions of type {@link GAction} required to manage
 * documents. The only actions that are not updated automatically are the edit
 * actions. To update this, clients have to call {@link #updateEditActions()}
 * whenever the event queue enters the idle mode.
 * <p>
 * An Example of how the actions of this manager can be integrated into an
 * application's actions:
 * <pre>{@code
 * public final class Actions {
 *     public static final GActionGroup allActions;
 *     public static final GActionGroup fileActions;
 *     public static final GActionGroup editActions;
 *     public static final GActionGroup windowActions;
 *     public static final GActionGroup settingsActions;
 *     public static final GActionGroup helpActions;
 *
 *     public static final GAction newAction;
 *     public static final GAction openAction;
 *     public static final GAction exitAction;
 *
 *     public static final GAction optionsAction;
 *
 *     public static final GAction helpAction;
 *     public static final GAction aboutAction;
 *
 *     static {
 *         var defaultModifier = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
 *         var quitAccel = OS.isMac()
 *                 ? KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.META_DOWN_MASK)
 *                 : KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_DOWN_MASK);
 *
 *         allActions = new GActionGroup().addAll(
 *                 fileActions = new GActionGroup("File").addAll(
 *                         newAction = new GAction("New...")
 *                                 .shortDescription("Create a new document")
 *                                 .accelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, defaultModifier))
 *                                 .smallIcon(SVGProvider.get("document_new", 16))
 *                                 .largeIcon(SVGProvider.get("document_new", 32))
 *                                 .onAction(e -> DVManager.createDocument(null)),
 *                         openAction = new GAction("Open...")
 *                                 .shortDescription("Open an existing document")
 *                                 .accelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, defaultModifier))
 *                                 .smallIcon(SVGProvider.get("document_open", 16))
 *                                 .largeIcon(SVGProvider.get("document_open", 32))
 *                                 .onAction(e -> {
 *                                     try {
 *                                         DVManager.openDocuments(null, false);
 *                                     } catch (DVLoadException ex) {
 *                                         NavigationService.getInstance().showError(ex.getLocalizedMessage(), ex);
 *                                     }
 *                                 }),
 *                         new GSeparatorAction(),
 *                         DVManager.recentDocsActions,
 *                         new GSeparatorAction(),
 *                         DVManager.saveAction
 *                                 .smallIcon(SVGProvider.get("save", 16))
 *                                 .largeIcon(SVGProvider.get("save", 32)),
 *                         DVManager.saveAllAction
 *                                 .smallIcon(SVGProvider.get("save_all", 16))
 *                                 .largeIcon(SVGProvider.get("save_all", 32)),
 *                         DVManager.saveAsAction
 *                                 .smallIcon(SVGProvider.get("save_as", 16))
 *                                 .largeIcon(SVGProvider.get("save_as", 32)),
 *                         new GSeparatorAction(),
 *                         exitAction = new GAction(OS.isMac() ? "Quit" : "Exit")
 *                                 .shortDescription("Exit the application")
 *                                 .accelerator(quitAccel)
 *                                 .onAction(e -> SwingDVApp.exit())
 *                 ),
 *                 editActions = new GActionGroup("Edit").addAll(
 *                         DVManager.undoAction
 *                                 .smallIcon(SVGProvider.get("undo", 16))
 *                                 .largeIcon(SVGProvider.get("undo", 32)),
 *                         DVManager.redoAction
 *                                 .smallIcon(SVGProvider.get("redo", 16))
 *                                 .largeIcon(SVGProvider.get("redo", 32)),
 *                         new GSeparatorAction(),
 *                         DVManager.cutAction
 *                                 .smallIcon(SVGProvider.get("cut", 16))
 *                                 .largeIcon(SVGProvider.get("cut", 32)),
 *                         DVManager.copyAction
 *                                 .smallIcon(SVGProvider.get("copy", 16))
 *                                 .largeIcon(SVGProvider.get("copy", 32)),
 *                         DVManager.pasteAction
 *                                 .smallIcon(SVGProvider.get("paste", 16))
 *                                 .largeIcon(SVGProvider.get("paste", 32)),
 *                         DVManager.deleteAction
 *                                 .smallIcon(SVGProvider.get("delete", 16))
 *                                 .largeIcon(SVGProvider.get("delete", 32))
 *                 ),
 *                 windowActions = new GActionGroup("Window").addAll(
 *                         DVManager.closeWindowAction
 *                                 .smallIcon(SVGProvider.get("window_close", 16))
 *                                 .largeIcon(SVGProvider.get("window_close", 32)),
 *                         DVManager.closeAllWindowsAction
 *                                 .smallIcon(SVGProvider.get("windows_close", 16))
 *                                 .largeIcon(SVGProvider.get("windows_close", 32))
 *                 ),
 *                 settingsActions = new GActionGroup("Settings").addAll(
 *                         optionsAction = new GAction("Options")
 *                                 .shortDescription("Open options-dialog")
 *                                 .onAction(e -> {
 *                                 })
 *                 ),
 *                 helpActions = new GActionGroup("Help").addAll(
 *                         helpAction = new GAction("Help")
 *                                 .shortDescription("Show help")
 *                                 .accelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0))
 *                                 .smallIcon(SVGProvider.get("help", 16))
 *                                 .largeIcon(SVGProvider.get("help", 32))
 *                                 .onAction(e -> {
 *                                 }),
 *                         new GSeparatorAction(),
 *                         aboutAction = new GAction("About " + SwingDVApp.TITLE)
 *                                 .shortDescription("Show about-dialog")
 *                                 .smallIcon(SVGProvider.get("about", 16))
 *                                 .largeIcon(SVGProvider.get("about", 32))
 *                                 .onAction(e -> NavigationService.getInstance().showAboutInfo())
 *                 )
 *         );
 *     }
 * }
 * }</pre>
 *
 * @see #registerSupport(DVManagerSupport)
 * @see DVManagerSupport
 * @see BasicDVManager
 *
 * @since 6.0.0
 */
public final class DVManager extends BasicDVManager {
    /**
     * The action where the recently opened documents are inserted into.
     */
    public static final GActionGroup recentFilesActions = new GActionGroup(SwingDVMessages.get("menu.recentDocs"));
    /**
     * The action that saves a document.
     */
    public static final GAction saveAction = new GAction(SwingDVMessages.get("menu.save"))
            .shortDescription(SwingDVMessages.get("menu.save.tooltip"))
            .accelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()))
            .enabled(false)
            .onAction(e -> {
                try {
                    saveActiveDocument();
                } catch (DVSaveException ex) {
                    ((DVNavigationService) Services.get(DVNavigationService.class)).showError(ex.getLocalizedMessage(), ex);
                }
            });
    /**
     * The action that saves all documents.
     */
    public static final GAction saveAllAction = new GAction(SwingDVMessages.get("menu.saveAll"))
            .shortDescription(SwingDVMessages.get("menu.saveAll.tooltip"))
            .accelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx() | InputEvent.SHIFT_DOWN_MASK))
            .enabled(false)
            .onAction(e -> {
                try {
                    saveAllDocuments();
                } catch (DVSaveException ex) {
                    ((DVNavigationService) Services.get(DVNavigationService.class)).showError(ex.getLocalizedMessage(), ex);
                }
            });
    /**
     * The action that saves the active document with a new name.
     */
    public static final GAction saveAsAction = new GAction(SwingDVMessages.get("menu.saveAs"))
            .shortDescription(SwingDVMessages.get("menu.saveAs.tooltip"))
            .enabled(false)
            .onAction(e -> {
                try {
                    saveActiveDocumentAs();
                } catch (DVSaveException ex) {
                    ((DVNavigationService) Services.get(DVNavigationService.class)).showError(ex.getLocalizedMessage(), ex);
                }
            });
    /**
     * The action that undoes the last action.
     */
    public static final GAction undoAction = new GAction(SwingDVMessages.get("menu.undo"))
            .shortDescription(SwingDVMessages.get("menu.undo.tooltip"))
            .accelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()))
            .enabled(false)
            .onAction(e -> {
                var doc = getActiveDocument();

                if (doc != null) {
                    doc.undo();
                }
            });
    /**
     * The action that redoes the last undone action.
     */
    public static final GAction redoAction = new GAction(SwingDVMessages.get("menu.redo"))
            .shortDescription(SwingDVMessages.get("menu.redo.tooltip"))
            .accelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()))
            .enabled(false)
            .onAction(e -> {
                var doc = getActiveDocument();

                if (doc != null) {
                    doc.redo();
                }
            });
    /**
     * The action that cuts the selected text.
     */
    public static final GAction cutAction = new GAction(SwingDVMessages.get("menu.cut"))
            .shortDescription(SwingDVMessages.get("menu.cut.tooltip"))
            .accelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()))
            .enabled(false)
            .onAction(e -> EditorTracer.getInstance().cut());
    /**
     * The action that copies the selected text.
     */
    public static final GAction copyAction = new GAction(SwingDVMessages.get("menu.copy"))
            .shortDescription(SwingDVMessages.get("menu.copy.tooltip"))
            .accelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()))
            .enabled(false)
            .onAction(e -> EditorTracer.getInstance().copy());
    /**
     * The action that pastes the text from the clipboard.
     */
    public static final GAction pasteAction = new GAction(SwingDVMessages.get("menu.paste"))
            .shortDescription(SwingDVMessages.get("menu.paste.tooltip"))
            .accelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()))
            .enabled(false)
            .onAction(e -> EditorTracer.getInstance().paste());
    /**
     * The action that deletes the selected text.
     */
    public static final GAction deleteAction = new GAction(SwingDVMessages.get("menu.delete"))
            .shortDescription(SwingDVMessages.get("menu.delete.tooltip"))
            .accelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0))
            .enabled(false)
            .onAction(e -> EditorTracer.getInstance().delete());
    /**
     * The action that closes the current window.
     */
    public static final GAction closeWindowAction = new GAction(SwingDVMessages.get("menu.close"))
            .shortDescription(SwingDVMessages.get("menu.close.tooltip"))
            .accelerator(OS.isMac()
                                 ? KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.META_DOWN_MASK)
                                 : OS.isWindows()
                    ? KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.CTRL_DOWN_MASK)
                    : KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK))
            .enabled(false)
            .onAction(e -> {
                var view = getSupport().getActiveMDISubView();

                if (view != null && view.getTemplate().isClosable() && view.getDocument().canCloseView(view)) {
                    view.getDocument().removeView(view);
                    view.forceClose();
                }
            });
    /**
     * The action that closes all windows.
     */
    public static final GAction closeAllWindowsAction = new GAction(SwingDVMessages.get("menu.closeAll"))
            .shortDescription(SwingDVMessages.get("menu.closeAll.tooltip"))
            .enabled(false)
            .onAction(e -> {
                var views = getSupport().getAllMDISubViews();

                for (var view : views) {
                    if (!view.getTemplate().isClosable())
                        continue;

                    if (!view.getDocument().canCloseView(view))
                        break;

                    view.getDocument().removeView(view);
                    view.forceClose();
                }
            });
    /**
     * The action that contains the open windows actions.
     */
    public static final GToggleActionGroup openDocumentsActions = new GToggleActionGroup();

    /**
     * Get the number of maximum recently used documents in the
     * {@link #recentFilesActions}.
     *
     * @return The maximum number of documents that can be stored in the
     *         {@link #recentFilesActions}. The default is 6.
     */
    public static int getMaxRecentFiles() {
        return recentFilesActions.getItemCount();
    }

    /**
     * Set the number of maximum recently used documents in the
     * {@link #recentFilesActions}.
     *
     * @param maxRecentFiles The maximum number of documents that can be
     *        stored in the {@link #recentFilesActions}.
     */
    public static void setMaxRecentFiles(int maxRecentFiles) {
        if (DVManager.recentFilesActions.getItemCount() == maxRecentFiles)
            return;

        var mi = recentFilesActions.iterator();
        int count = 0;

        while (mi.hasNext()) {
            ++count;
            mi.next();

            if (count > maxRecentFiles)
                break;
        }

        while (mi.hasNext()) {
            ++count;
            mi.next();
            mi.remove();
        }

        while (count <= maxRecentFiles) {
            ++count;
            var action = new GAction()
                    .visible(false)
                    .onAction(e -> {
                        try {
                            openDocument(null, e.getActionCommand(), false);
                        } catch (DVLoadException ex) {
                            ((DVNavigationService) Services.get(DVNavigationService.class)).showError(ex.getLocalizedMessage(), ex);
                        }
                    });
            recentFilesActions.addAll(action);
        }

        if (isSupportRegistered())
            getSupport().updateRecentFilesMenu();
    }

    /**
     * Writes the recently used files into the given settings.
     *
     * @param settings The settings to write the recently used files into.
     */
    public static void saveRecentFiles(Settings settings) {
        settings.write("recentFiles.maxItems", getMaxRecentFiles());

        var recent = new StringBuilder();

        for (int i = 0; i < recentFilesActions.getItemCount(); ++i) {
            var action = (GAction) recentFilesActions.getItemAt(i);

            if (action.isVisible()) {
                if (i > 0)
                    recent.append(";::;");

                recent.append(action.getCommand());
            }
        }

        if (!recent.isEmpty())
            settings.write("recentFiles", recent.toString());
    }

    /**
     * Restores the recently used files from the given settings.
     *
     * @param settings The settings to read the recently used files from.
     */
    public static void restoreRecentFiles(Settings settings) {
        int maxItems = settings.read("recentFiles.maxItems", 6);
        var recent = settings.read("recentFiles", (String) null);
        var recentFiles = recent != null ? recent.split(";::;") : null;

        initRecentFilesActions(maxItems);

        if (recentFiles != null) {
            for (int i = 0; i < recentFiles.length && i < maxItems; i++) {
                ((GAction) recentFilesActions.getItemAt(i))
                        .name(recentFiles[i])
                        .command(recentFiles[i])
                        .visible(true);
            }
        }

        recentFilesActions.setEnabled(recentFiles != null);
    }

    /**
     * Updates the edit actions.
     * <p>
     * This should be called once each time when the application enters the idle
     * mode.
     */
    @SuppressWarnings("DataFlowIssue")
    public static void updateEditActions() {
        var tracer = EditorTracer.getInstance();
        var doc = getActiveDocument();

        undoAction.setEnabled(doc != null && doc.canUndo());
        redoAction.setEnabled(doc != null && doc.canRedo());
        cutAction.setEnabled(tracer.canCut());
        copyAction.setEnabled(tracer.canCopy());
        pasteAction.setEnabled(tracer.canPaste());
        deleteAction.setEnabled(tracer.canDelete());

        undoAction.setName(undoAction.isEnabled()
                                   ? SwingDVMessages.get("menu.undo.format", doc.getUndoTitle())
                                   : SwingDVMessages.get("menu.undo"));
        undoAction.setShortDescription(undoAction.isEnabled()
                                               ? SwingDVMessages.get("menu.undo.format", doc.getUndoTitle())
                                               : SwingDVMessages.get("menu.undo.tooltip"));
        redoAction.setName(redoAction.isEnabled()
                                   ? SwingDVMessages.get("menu.redo.format", doc.getRedoTitle())
                                   : SwingDVMessages.get("menu.redo"));
        redoAction.setShortDescription(redoAction.isEnabled()
                                               ? SwingDVMessages.get("menu.redo.format", doc.getRedoTitle())
                                               : SwingDVMessages.get("menu.redo.tooltip"));
    }

    private static DVManagerSupport support;

    /**
     * Sets the support for the BasicDVManager.
     * <p>
     * The manager needs some support to perform certain operations that depend
     * on the used UI framework. This support should be installed once at
     * application startup.
     *
     * @param support The support to set.
     */
    public static void registerSupport(DVManagerSupport support) {
        DVManager.support = support;
    }

    /**
     * Checks if the support for the {@link DVManager} is registered.
     *
     * @return {@code true} if the support is registered, {@code false}
     *         otherwise.
     */
    public static boolean isSupportRegistered() {
        return support != null;
    }

    /**
     * Gets the registered support.
     *
     * @return The registered support.
     *
     * @throws IllegalStateException if no support is registered.
     * @see #registerSupport(DVManagerSupport)
     * @see #isSupportRegistered()
     */
    public static DVManagerSupport getSupport() {
        if (isSupportRegistered())
            return support;

        throw new IllegalStateException("DVManagerSupport is not registered.");
    }

    private static void updateSaveActions() {
        var doc = getActiveDocument();
        saveAction.setEnabled(doc != null && doc.isModified());
        saveAsAction.setEnabled(doc != null && doc.isSaveAsSupported());
        saveAllAction.setEnabled(getOpenDocuments().stream().anyMatch(Document::isModified));
    }

    private static final PropertyChangeListener documentModifiedListener = evt -> updateSaveActions();
    private static final PropertyChangeListener documentNameListener = evt -> {
        for (GAction action : openDocumentsActions) {
            if (Objects.equals(action.getTag(), evt.getSource())) {
                action.setName(evt.getNewValue().toString());
                break;
            }
        }

        // Invoke later because the document may be new
        // and this flag ist not removed yet:
        //
        SwingUtilities.invokeLater(() -> updateRecentDocumentsActions((Document) evt.getSource()));
    };

    private static void updateRecentDocumentsActions(Document document) {
        if (document.getParent() != null || document.isNewData())
            return;

        var newText = document.getName();

        for ( int i = 0; i < recentFilesActions.getItemCount(); i++) {
            var action = (GAction) recentFilesActions.getItemAt(i);
            var oldText = action.getCommand();

            action.name(newText).command(newText);

            if (Objects.equals(oldText, document.getName()))
                break;

            if (!action.isVisible()) {
                action.setVisible(true);
                break;
            }

            newText = oldText;
        }

        recentFilesActions.setEnabled(true);
    }

    private static void initRecentFilesActions(int maxItems) {
        recentFilesActions.clear();

        for (int i = 0; i < maxItems; i++) {
            var action = new GAction()
                    .visible(false)
                    .onAction(e -> {
                        try {
                            openDocument(null, e.getActionCommand(), false);
                        } catch (DVLoadException ex) {
                            ((DVNavigationService) Services.get(DVNavigationService.class)).showError(ex.getLocalizedMessage(), ex);
                        }
                    });
            recentFilesActions.addAll(action);
        }

        recentFilesActions.setEnabled(false);

        if (isSupportRegistered())
            getSupport().updateRecentFilesMenu();
    }

    static {
        initRecentFilesActions(6);

        addPropertyChangeListener(ACTIVE_DOCUMENT_PROPERTY, e -> {
            var docOld = (e.getOldValue() instanceof Document d) ? d : null;
            var docNew = (e.getNewValue() instanceof Document d) ? d : null;

            if (docOld != null) {
                docOld.removePropertyChangeListener(Document.NAME_PROPERTY, documentNameListener);
                docOld.removePropertyChangeListener(Document.MODIFIED_PROPERTY, documentModifiedListener);
            }

            if (docNew != null) {
                docNew.addPropertyChangeListener(Document.NAME_PROPERTY, documentNameListener);
                docNew.addPropertyChangeListener(Document.MODIFIED_PROPERTY, documentModifiedListener);

                for (GAction action : openDocumentsActions) {
                    if (action.getTag() == docNew) {
                        action.setSelected(true);
                        break;
                    }
                }
            }

            saveAction.setEnabled(docNew != null && docNew.isModified());
            saveAsAction.setEnabled(docNew != null && docNew.isSaveAsSupported());
            saveAllAction.setEnabled(getOpenDocuments().stream().anyMatch(Document::isModified));

            System.out.println("Active document changed: " + (docNew != null ? docNew.getName() : "no active one"));
        });

        addPropertyChangeListener(ACTIVE_VIEW_PROPERTY, e -> {
            var view = (e.getNewValue() instanceof View<?> v) ? v : null;
            System.out.println("Active view changed: " + (view != null ? view.getTitle() : "no active one"));
        });

        addPropertyChangeListener(OPEN_DOCUMENTS_PROPERTY, e -> {
            var mi = openDocumentsActions.iterator();
            var di = getOpenDocuments().iterator();
            var ai = 0;

            while(mi.hasNext() && di.hasNext()) {
                var doc = di.next();
                mi.next().name(doc.getName())
                        .selected(doc == getActiveDocument())
                        .tag(doc);
                ++ai;
            }

            while(mi.hasNext()) {
                mi.next();
                mi.remove();
            }

            while (di.hasNext()) {
                var doc = di.next();
                var action = new GAction()
                        .name(doc.getName())
                        .selectable(true)
                        .tag(doc)
                        .onAction(evt -> DVManager.activateDocument(doc));
                openDocumentsActions.addAll(action);

                action.setSelected(doc == getActiveDocument());

                if (ai < 10)
                    action.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0 + ai, InputEvent.ALT_DOWN_MASK));

                ++ai;
            }

            getSupport().updateOpenDocumentsMenu();

            var docOld = (e.getOldValue() instanceof Document d) ? d : null;
            var docNew = (e.getNewValue() instanceof Document d) ? d : null;

            if (docNew != null)
                updateRecentDocumentsActions(docNew);

            if (docOld != null)
                System.out.println("Document closed: " + docOld.getName());
            else if (docNew != null)
                System.out.println("Document opened: " + docNew.getName());
            else
                System.err.println("Documents changed but with no document.");
        });
    }
}
