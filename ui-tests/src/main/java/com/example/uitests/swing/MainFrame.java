package com.example.uitests.swing;

import de.ganzer.swing.actions.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame {
    GActionGroup mainMenu;
    GActionGroup fileMenu;
    GActionGroup buttonsMenu;
    GActionGroup subMenu;

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
    }

    private void initLookAndFeel() {
        for (var laf: UIManager.getInstalledLookAndFeels())
            System.out.println(laf.getClassName());

        try {
//            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
//            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
//            UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
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
                                .onAction(this::onExit)
                ),
                buttonsMenu = new GActionGroup("Buttons").addAll(
                        new GAction("Any Option")
                                .smallIcon(Images.load("stroller-16"))
                                .largeIcon(Images.load("stroller-48"))
                                .selectable(true)
                                .onAction(this::onAnyOption),
                        new GAction("Another Option")
                                .smallIcon(Images.load("car_compact2-16"))
                                .largeIcon(Images.load("car_compact2-48"))
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
                                                .exclusivelySelectable(true)
                                                .selected(true),
                                        new GAction("Choose 2")
                                                .smallIcon(Images.load("hand_count_three-16"))
                                                .largeIcon(Images.load("hand_count_three-48"))
                                                .exclusivelySelectable(true),
                                        new GAction("Choose 3")
                                                .smallIcon(Images.load("calendar_3-16"))
                                                .largeIcon(Images.load("calendar_3-48"))
                                                .exclusivelySelectable(true)
                                )
                ),
                subMenu = new GActionGroup("Others")
                        .largeIcon(Images.load("hamburger-48"))
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
                                new GAction("Dummy 1"),
                                new GAction("Dummy 2"),
                                new GSeparatorAction(),
                                new GAction("Dummy 3"),
                                new GAction("Dummy 4"),
                                new GAction("Dummy 5"),
                                new GAction("Dummy 6")
                        )
        );
    }

    private void onExit(GActionEvent event) {
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    private void onAnyOption(GActionEvent event) {
        System.out.format(
                "Any Option is %s!\n",
                event.getSource().isSelected() ? "selected" : "deselected");
    }

    private void onAnotherOption(GActionEvent event) {
        System.out.format(
                "Another Option is %s!\n",
                event.getSource().isSelected() ? "selected" : "deselected");
    }

    private void onChooseChanged(GSelectedActionChangedEvent event) {
        System.out.format(
                "%s action is selected!\n",
                event.getSelectedAction() == null ? "No" : event.getSelectedAction().getName());
    }

    private void initMenu() {
        var menuBar = new JMenuBar();
        mainMenu.addMenus(menuBar);
        getContentPane().add(menuBar, BorderLayout.PAGE_START);
    }

    private void initToolBar() {
        var toolBar = new JToolBar();
        fileMenu.addButtons(toolBar, CreateOptions.NO_BORDER);//, CreateOptions.SHOW_TEXT | CreateOptions.IMAGE_TOP);
        toolBar.addSeparator();
        buttonsMenu.addButtons(toolBar, CreateOptions.NO_BORDER);//, CreateOptions.SHOW_TEXT | CreateOptions.IMAGE_TOP);
        getContentPane().add(toolBar, BorderLayout.PAGE_END);
        toolBar.addSeparator();
        toolBar.add(subMenu.createButton(CreateOptions.NO_BORDER));//, CreateOptions.SHOW_TEXT | CreateOptions.IMAGE_TOP));
    }
}
