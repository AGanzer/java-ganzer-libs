package com.example.uitests.swing;

import de.ganzer.swing.actions.*;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame {
    GActionGroup mainMenu;
    GActionGroup fileMenu;
    GActionGroup buttonsMenu;

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
        try {
            UIManager.setLookAndFeel(NimbusLookAndFeel.class.getName());
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
                                .onAction(this::onExit)
                ),
                buttonsMenu = new GActionGroup("Buttons").addAll(
                        new GAction("Any Option")
                                .selectable(true)
                                .onAction(this::onAnyOption),
                        new GAction("Another Option")
                                .selectable(true)
                                .selected(true)
                                .onAction(this::onAnotherOption),
                        new GSeparatorAction(),
                        new GToggleActionGroup()
                                .onSelectedActionChanged(this::onChooseChanged)
                                .addAll(
                                        new GAction("Choose 1")
                                                .exclusivelySelectable(true)
                                                .selected(true),
                                        new GAction("Choose 2")
                                                .exclusivelySelectable(true),
                                        new GAction("Choose 3")
                                                .exclusivelySelectable(true)
                                )
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
        fileMenu.addButtons(toolBar, false);
        toolBar.addSeparator();
        buttonsMenu.addButtons(toolBar, false);
        getContentPane().add(toolBar, BorderLayout.PAGE_END);
    }
}
