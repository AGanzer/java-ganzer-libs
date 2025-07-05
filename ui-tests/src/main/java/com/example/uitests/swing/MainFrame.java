package com.example.uitests.swing;

import com.example.uitests.swing.tests.LoginDialog;
import com.example.uitests.swing.tests.InputTestDialog;
import de.ganzer.swing.actions.*;
import de.ganzer.swing.controls.Accordion;
import de.ganzer.swing.controls.ClosableTabsPane;
import de.ganzer.swing.controls.TogglePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame {
    private static final String UI_KEY_FRAME = "MainFrame.frame";

    private GActionGroup mainMenu;
    private GActionGroup fileMenu;
    private GActionGroup buttonsMenu;
    private GActionGroup othersMenu;
    private GActionGroup testMenu;
    private GAction enableTabAction;
    private GAction colorTabAction;
    private JToolBar toolBar;
    private ClosableTabsPane tabPane;

    @Override
    protected void frameInit() {
        super.frameInit();

        setTitle("Swing Test Application");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initLookAndFeel();
        initLayout();
        initActions();
        initMenu();
        initToolBar();
        initAccorion();
        initTabPane();

        SwingTestApp.getUiSettings().apply(UI_KEY_FRAME, this);
    }

    @Override
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING)
            SwingTestApp.getUiSettings().write(UI_KEY_FRAME, this);

        super.processWindowEvent(e);
    }

    private void initLookAndFeel() {
        for (var laf: UIManager.getInstalledLookAndFeels())
            System.out.println(laf.getClassName());

        try {
//            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
//            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
//            UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
//            UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
//            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
//            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
        } catch (Exception e) {
            System.err.println("Cannot set look and feel. Using default.");
            e.printStackTrace(System.err);
        }
    }

    private void initLayout() {
        var pane = getContentPane();
        pane.setLayout(new BorderLayout());
    }

    private void initActions() {
        mainMenu = new GActionGroup().addAll(
                fileMenu = new GActionGroup("File").addAll(
                        new GAction("Exit")
                                .accelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.SHIFT_DOWN_MASK | InputEvent.CTRL_DOWN_MASK))
                                .smallIcon(Images.load("close-16"))
                                .largeIcon(Images.load("close-48"))
                                .shortDescription("Quits this application.")
                                .onAction(this::onExit)
                ),
                buttonsMenu = new GActionGroup("Buttons").addAll(
                        new GAction("Any Option")
                                .smallIcon(Images.load("stroller-16"))
                                .largeIcon(Images.load("stroller-48"))
                                .shortDescription("Sets the option 1.")
                                .selectable(true)
                                .onAction(this::onAnyOption),
                        new GAction("Another Option")
                                .smallIcon(Images.load("car_compact2-16"))
                                .largeIcon(Images.load("car_compact2-48"))
                                .shortDescription("Sets the option 2.")
                                .selectable(true)
                                .selected(true)
                                .onAction(this::onAnotherOption),
                        new GSeparatorAction(),
                        new GToggleActionGroup()
                                .onSelectedActionChanged(this::onChooseChanged)
                                .addAll(
                                        new GAction("Choose 1")
                                                .smallIcon(Images.load("calendar_1-16"))
                                                .largeIcon(Images.load("calendar_1-48"))
                                                .shortDescription("Choose the number 1.")
                                                .exclusivelySelectable(true)
                                                .selected(true),
                                        new GAction("Choose 2")
                                                .smallIcon(Images.load("hand_count_three-16"))
                                                .largeIcon(Images.load("hand_count_three-48"))
                                                .shortDescription("Choose the number 2.")
                                                .exclusivelySelectable(true),
                                        new GAction("Choose 3")
                                                .smallIcon(Images.load("calendar_3-16"))
                                                .largeIcon(Images.load("calendar_3-48"))
                                                .shortDescription("Choose the number 3.")
                                                .exclusivelySelectable(true)
                                )
                ),
                othersMenu = new GActionGroup("Others")
                        .largeIcon(Images.load("hamburger-48"))
                        .shortDescription("Further options.")
                        .addAll(
                                new GActionGroup("Sub Menu 1").addAll(
                                        new GAction("Sub Dummy 1"),
                                        new GAction("Sub Dummy 2")
                                ),
                                new GActionGroup("Sub Menu 2").addAll(
                                        new GAction("Sub Dummy 1"),
                                        new GAction("Sub Dummy 2"),
                                        new GAction("Sub Dummy 3"),
                                        new GAction("Sub Dummy 4")
                                ),
                                new GSeparatorAction(),
                                new GToggleActionGroup().addAll(
                                        new GAction("Dummy 1")
                                                .exclusivelySelectable(true)
                                                .selected(true),
                                        new GAction("Dummy 2")
                                                .exclusivelySelectable(true)
                                ),
                                new GSeparatorAction(),
                                new GAction("Dummy 3"),
                                new GAction("Dummy 4"),
                                new GAction("Dummy 5"),
                                new GAction("Dummy 6")
                        ),
                testMenu = new GActionGroup("Tests")
                        .largeIcon(Images.load("multimeter_analog-48"))
                        .shortDescription("Several Tests.")
                        .addAll(
                                new GAction("Input Dialog Test")
                                        .onAction(this::onInputTest),
                                new GAction("Login Dialog Test")
                                        .onAction(this::onLoginTest),
                                new GSeparatorAction(),
                                new GAction("New Tab")
                                        .accelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK))
                                        .onAction(this::onNewTab),
                                enableTabAction = new GAction("Enable/Disable Tab")
                                        .enabled(false)
                                        .onAction(this::onToggleTabEnabled),
                                colorTabAction = new GAction("Toggle Tab Colors")
                                        .enabled(false)
                                        .onAction(this::onChangeTabColor)
                        ),
                new GActionGroup("Extras").addAll(
                        new GAction("Show Text In Buttons")
                                .shortDescription("Shows/hides the toolbar's buttons texts.")
                                .selectable(true)
                                .onAction(this::onShowTexts),
                        new GAction("Small Buttons")
                                .shortDescription("Shows small buttons in the toolbar.")
                                .selectable(true)
                                .onAction(this::onSmallButtons),
                        new GAction("Hide  Others")
                                .shortDescription("Shows/hides the Others menu")
                                .selectable(true)
                                .onAction(this::hideOthers)
                )

        );
    }

    private void initMenu() {
        var menuBar = new JMenuBar();
        mainMenu.addMenus(menuBar);
        setJMenuBar(menuBar);
    }

    private void initToolBar() {
        toolBar = new JToolBar();

        fileMenu.addButtons(toolBar, CreateOptions.NO_BORDER);// | CreateOptions.SHOW_TEXT);// | CreateOptions.IMAGE_TRAILING);
        toolBar.addSeparator();
        buttonsMenu.addButtons(toolBar, CreateOptions.NO_BORDER);// | CreateOptions.SHOW_TEXT);// | CreateOptions.IMAGE_TRAILING);
        toolBar.addSeparator();
        toolBar.add(othersMenu.createButton(CreateOptions.NO_BORDER));// | CreateOptions.SHOW_TEXT));// | CreateOptions.IMAGE_TRAILING));
        toolBar.add(testMenu.createButton(CreateOptions.NO_BORDER));// | CreateOptions.SHOW_TEXT));// | CreateOptions.IMAGE_TRAILING));

        getContentPane().add(toolBar, BorderLayout.PAGE_START);
    }

    private void initAccorion() {
        Accordion accordion = new Accordion();

        for (int i = 0; i < 5; ++i) {
            var content = new JPanel();
            content.setBackground(Color.WHITE);
            content.setPreferredSize(new Dimension(200, 200));

            accordion.add(new TogglePanel("Panel " + (i + 1), content));
        }

        accordion.getPanels()[0].setCollapsed(false);

        getContentPane().add(accordion, BorderLayout.WEST);
    }

    private void initTabPane() {
        tabPane = new ClosableTabsPane();
        getContentPane().add(tabPane, BorderLayout.CENTER);
        addNewTab(false);

        tabPane.addCloseListener((i, c) -> {
            if (c != null)
                tabPane.remove(c);
        });
        tabPane.addChangeListener(e -> {
            enableTabAction.setEnabled(tabPane.getTabCount() > 1);
            colorTabAction.setEnabled(tabPane.getTabCount() > 2);
        });
    }

    private void onExit(ActionEvent event) {
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    private void onAnyOption(ActionEvent event) {
        System.out.format(
                "Any Option is %s!\n",
                ((GAction)event.getSource()).isSelected() ? "selected" : "deselected");
    }

    private void onAnotherOption(ActionEvent event) {
        System.out.format(
                "Another Option is %s!\n",
                ((GAction)event.getSource()).isSelected() ? "selected" : "deselected");
    }

    private void onChooseChanged(GSelectedActionChangedEvent event) {
        System.out.format(
                "%s action is selected!\n",
                event.getSelectedAction() == null ? "No" : event.getSelectedAction().getName());
    }

    private void onShowTexts(ActionEvent event) {
        boolean hide = !((GAction)event.getSource()).isSelected();

        for (int i = 0; i < toolBar.getComponentCount(); i++) {
            if (toolBar.getComponent(i) instanceof AbstractButton button)
                button.setHideActionText(hide);
        }
    }

    private void onSmallButtons(ActionEvent event) {
        boolean smallButtons = ((GAction)event.getSource()).isSelected();

        for (int i = 0; i < toolBar.getComponentCount(); i++) {
            if (toolBar.getComponent(i) instanceof AbstractButton button)
                setImage((GAction)button.getAction(), smallButtons);
        }
    }

    private void hideOthers(ActionEvent ignored) {
        othersMenu.setVisible(!othersMenu.isVisible());
    }

    private void setImage(GAction action, boolean small) {
        String size = small ? "-32" : "-48";
        String image;

        switch (action.getName()) {
            case "Exit":
                image = "close" + size;
                break;

            case "Any Option":
                image = "stroller" + size;
                break;

            case "Another Option":
                image = "car_compact2" + size;
                break;

            case "Choose 1":
                image = "calendar_1" + size;
                break;

            case "Choose 2":
                image = "hand_count_three" + size;
                break;

            case "Choose 3":
                image = "calendar_3" + size;
                break;

            case "Others":
                image = "hamburger" + size;
                break;

            case "Tests":
                image = "multimeter_analog" + size;
                break;

            default:
                System.out.println("Unknown action: " + action.getName());
                return;
        }

        action.largeIcon(Images.load(image));
    }

    private void onLoginTest(ActionEvent actionEvent) {
        var data = new LoginDialog.Data();

        if (LoginDialog.showModal(this, data))
            System.out.printf("Login with user %s successful!\n", data.name);
        else
            System.out.println("Login canceled.");
    }

    private void onInputTest(ActionEvent event) {
        var data = new InputTestDialog.Data();
        data.input = "abcd";

        if (InputTestDialog.showModal(this, data))
            System.out.println("Your input: " + data.input);
    }

    private static int tabCounter;

    private void addNewTab(boolean closable) {
        var panel = new JPanel();
        panel.setBorder(BorderFactory.createLoweredBevelBorder());
        tabPane.addTab("Tab " + ++tabCounter, Images.load("hand_count_three-16"), panel);
        tabPane.setSelectedComponent(panel);
        tabPane.setClosableAt(tabPane.getSelectedIndex(), closable);
    }

    private void onNewTab(ActionEvent event) {
        addNewTab(true);
    }

    private void onToggleTabEnabled(ActionEvent event) {
        if (tabPane.getTabCount() > 1)
            tabPane.setEnabledAt(1, !tabPane.isEnabledAt(1));
    }

    private Color lastTabBackgroundColor = new Color (128, 128, 255);
    private Color lastTabForegroundColor = new Color (0, 255, 0);

    private void onChangeTabColor(ActionEvent event) {
        if (tabPane.getTabCount() > 2) {
            Color backgroundColorToSet = lastTabBackgroundColor;
            Color foregroundColorToSet = lastTabForegroundColor;

            lastTabBackgroundColor = tabPane.getBackgroundAt(2);
            lastTabForegroundColor = tabPane.getForegroundAt(2);

            tabPane.setBackgroundAt(2, backgroundColorToSet);
            tabPane.setForegroundAt(2, foregroundColorToSet);
        }
    }
}
