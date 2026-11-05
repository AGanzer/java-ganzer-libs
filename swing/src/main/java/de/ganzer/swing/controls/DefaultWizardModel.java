package de.ganzer.swing.controls;

import java.awt.Component;

/**
 * A default implementation of {@link AbstractWizardModel} that selects the next
 * page in the order they were added.
 * <p>
 * This class works with pages that implement {@link WizardPage}.
 * <p>
 * If it is necessary to sip pages in some cases, an implementor may derive
 * from {@link AbstractWizardModel} or from this class. In the latter case,
 * {@link #addPage(WizardPage)} can be overloaded or overridden to put own
 * identifiers to {@link #addPage(Object, WizardPage)}. It is then most
 * important to override {@link #getNextPage(Object)} too, to work with the
 * correct identifiers and to return the right identifier for the next page.
 * <p>
 * An example of how the deafult model can be used to show each page in order
 * they were inserted is in the following, where each page dos implement the
 * {@link WizardPage} interface:
 *
 * <pre>{@code
 * public class DefaultWizardDialog extends JDialog {
 *     private boolean finished;
 *
 *     public static boolean showModel(Window owner, WizardData data) {
 *         var dialog = new DefaultWizardDialog(owner, data);
 *         dialog.setVisible(true);
 *
 *         return dialog.finished;
 *     }
 *
 *     public DefaultWizardDialog(Window owner, WizardData data) {
 *         super(owner, "Default Wizard Test", Dialog.ModalityType.APPLICATION_MODAL);
 *
 *         var wizard = new WizardPane();
 *         wizard.setBorder(new EmptyBorder(10, 10, 10, 10));
 *
 *         var model = (DefaultWizardModel) wizard.getModel();
 *         model.addPage(new WelcomePage());
 *         model.addPage(new NamePage(data));
 *         model.addPage(new PrivatePage(data));
 *         model.addPage(new SummaryPage(data));
 *
 *         var prevButton = wizard.getPrevAction().createButton();
 *         var nextButton = wizard.getNextAction().createButton();
 *         var finishButton = wizard.getFinishAction().onAction(e -> {
 *             finished = true;
 *             setVisible(false);
 *         }).createButton();
 *         var cancelButton = new GAction("Cancel").onAction(e -> setVisible(false)).createButton();
 *
 *         var buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
 *         buttonPanel.setBorder(new EmptyBorder(0, 10, 10, 10));
 *         buttonPanel.add(prevButton);
 *         buttonPanel.add(nextButton);
 *         buttonPanel.add(finishButton);
 *         buttonPanel.add(cancelButton);
 *
 *         getContentPane().setLayout(new BorderLayout());
 *         getContentPane().add(wizard, BorderLayout.CENTER);
 *         getContentPane().add(buttonPanel, BorderLayout.SOUTH);
 *
 *         pack();
 *         setLocationRelativeTo(owner);
 *     }
 * }
 * }</pre>
 *
 * In the following is the same example, but with an extended model to dismiss
 * a page that does currently not fit the users input:
 *
 * <pre>{@code
 * public class ExtendedWizardDialog extends JDialog {
 *     private boolean finished;
 *
 *     public static boolean showModel(Window owner, WizardData data) {
 *         var dialog = new ExtendedWizardDialog(owner, data);
 *         dialog.setVisible(true);
 *
 *         return dialog.finished;
 *     }
 *
 *     public ExtendedWizardDialog(Window owner, WizardData data) {
 *         super(owner, "Extended Wizard Test", ModalityType.APPLICATION_MODAL);
 *
 *         var wizard = new WizardPane(new Model(data));
 *         wizard.setBorder(new EmptyBorder(10, 10, 10, 10));
 *
 *         var model = (Model) wizard.getModel();
 *         model.addPage(Model.WELCOME, new WelcomePage());
 *         model.addPage(Model.NAME, new NamePage(data));
 *         model.addPage(Model.PRIVATE, new PrivatePage(data));
 *         model.addPage(Model.EMPLOYEE, new EmployeePage(data));
 *         model.addPage(Model.SUMMARY, new SummaryPage(data));
 *
 *         var prevButton = wizard.getPrevAction().createButton();
 *         var nextButton = wizard.getNextAction().createButton();
 *         var finishButton = wizard.getFinishAction().onAction(e -> {
 *             finished = true;
 *             setVisible(false);
 *         }).createButton();
 *         var cancelButton = new GAction("Cancel").onAction(e -> setVisible(false)).createButton();
 *
 *         var buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
 *         buttonPanel.setBorder(new EmptyBorder(0, 10, 10, 10));
 *         buttonPanel.add(prevButton);
 *         buttonPanel.add(nextButton);
 *         buttonPanel.add(finishButton);
 *         buttonPanel.add(cancelButton);
 *
 *         getContentPane().setLayout(new BorderLayout());
 *         getContentPane().add(wizard, BorderLayout.CENTER);
 *         getContentPane().add(buttonPanel, BorderLayout.SOUTH);
 *
 *         pack();
 *         setLocationRelativeTo(owner);
 *     }
 *
 *     private static class Model extends DefaultWizardModel {
 *         public static final int WELCOME = 0;
 *         public static final int NAME = 1;
 *         public static final int PRIVATE = 2;
 *         public static final int EMPLOYEE = 3;
 *         public static final int SUMMARY = 4;
 *
 *         private final WizardData data;
 *
 *         private Model(WizardData data) {
 *             this.data = data;
 *         }
 *
 *         public void addPage(Object pageId, WizardPage page) {
 *             super.addPage(pageId, page);
 *         }
 *
 *         @Override
 *         public Object getNextPage(Object pageId) {
 *             return switch (Integer.parseInt(pageId.toString())) {
 *                 case WELCOME -> NAME;
 *                 case NAME -> data.role == Role.PRIVATE ? PRIVATE : EMPLOYEE;
 *                 case PRIVATE, EMPLOYEE -> SUMMARY;
 *                 default -> null;
 *             };
 *         }
 *     }
 * }
 * }</pre>
 *
 * @see #addPage(WizardPage)
 * @see #addPage(Object, WizardPage)
 * @see #getNextPage(Object)
 *
 * @since 6.3.0
 */
public class DefaultWizardModel extends AbstractWizardModel<WizardPage> {
    /**
     * Adds a page to the available pages.
     * <p>
     * If the given page is the first added page, it is set as the currently
     * active page.
     *
     * @param page The page that represents the page.
     *
     * @throws NullPointerException if {@code page} is {@code null}.
     * @throws IllegalArgumentException if {@code page} is already registered.
     */
    public void addPage(WizardPage page) {
        addPage(getPages().size(), page);
    }

    /**
     * Gets the identifier of the next page to show after the given page.
     * <p>
     * This implementation does simply return the next Page identifier of the
     * given page in the order they were added.
     *
     * @param pageId The identifier of the page to get the next page for.
     *
     * @return The next page to show or {@code null} if no further page is
     *         available.
     */
    @Override
    public Object getNextPage(Object pageId) {
        int next = Integer.parseInt(pageId.toString()) + 1;
        return next < getPages().size() ? next : null;
    }

    /**
     * Invoked to validate the data the user input into the given page.
     *
     * @param pageId The page to validate.
     *
     * @return {@code true} if the page is valid, {@code false} otherwise.
     */
    @Override
    public boolean validatePage(Object pageId) {
        return getPage(pageId).validateInput();
    }

    /**
     * Invoked when the given page is going to be left.
     * <p>
     * Implementors should update the wizard's data with the user's input.
     *
     * @param pageId The page to leave.
     */
    @Override
    public void pageLeaving(Object pageId) {
        getPage(pageId).updateWizardData();
    }

    /**
     * Invoked when the given page is entered.
     * <p>
     * Implementors should update the page's controls with the wizard's data.
     *
     * @param pageId The page to enter.
     */
    @Override
    public void pageEntered(Object pageId) {
        getPage(pageId).updatePageControls();
    }

    /**
     * Returns the Swing component for the given page.
     *
     * @param pageId The identifier of the page where to get the component for.
     *
     * @return The Swing component for the given page. This should not return
     *         {@code null}.
     */
    @Override
    public Component getComponent(Object pageId) {
        return getPage(pageId).getComponent();
    }
}
