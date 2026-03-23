package com.example.uitests.swing;

import com.example.uitests.swing.tests.FlatLaFBorderValidationHint;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.intellijthemes.FlatCyanLightIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatHighContrastIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatNordIJTheme;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import de.ganzer.swing.validaton.ValidationFilter;

import javax.swing.UIManager;

public class CommandLineParser {
    public static void parse(final String[] args) {
        for (var arg: args) {
            if (arg.startsWith("--theme=")) {
                adjustLaf(arg.substring(8));
            } else if (arg.equals("--version")) {
                System.out.println("Swing Test Application");
                System.out.println("Version 0.0.1");
                System.exit(0);
            }
        }
    }

    private static void adjustLaf(String arg) {
        try {
            switch (arg.toLowerCase()) {
                case "light": {
                    FlatLightLaf.setup();
                    ValidationFilter.setHintProvider(new FlatLaFBorderValidationHint());
                    break;
                }

                case "dark": {
                    FlatDarkLaf.setup();
                    ValidationFilter.setHintProvider(new FlatLaFBorderValidationHint());
                    break;
                }

                case "dracula": {
                    FlatDarculaLaf.setup();
                    ValidationFilter.setHintProvider(new FlatLaFBorderValidationHint());
                    break;
                }

                case "intellij": {
                    FlatIntelliJLaf.setup();
                    ValidationFilter.setHintProvider(new FlatLaFBorderValidationHint());
                    break;
                }

                case "highcontrast": {
                    FlatHighContrastIJTheme.setup();
                    ValidationFilter.setHintProvider(new FlatLaFBorderValidationHint());
                    break;
                }

                case "cyan": {
                    FlatCyanLightIJTheme.setup();
                    ValidationFilter.setHintProvider(new FlatLaFBorderValidationHint());
                    break;
                }

                case "nord": {
                    FlatNordIJTheme.setup();
                    ValidationFilter.setHintProvider(new FlatLaFBorderValidationHint());
                    break;
                }

                case "maclight": {
                    FlatMacLightLaf.setup();
                    ValidationFilter.setHintProvider(new FlatLaFBorderValidationHint());
                    break;
                }

                case "macdark": {
                    FlatMacDarkLaf.setup();
                    ValidationFilter.setHintProvider(new FlatLaFBorderValidationHint());
                    break;
                }

                case "windows": {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                    break;
                }

                case "windowsclassic": {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
                    break;
                }

                case "gtk": {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
                    break;
                }

                case "metal": {
                    UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                    break;
                }

                case "motif": {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
                    break;
                }

                case "nimbus": {
                    UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                    break;
                }

                default: {
                    UIManager.setLookAndFeel(arg);
                }
            }
        } catch(Exception e) {
            System.err.println("Look & Feel cannot be set.");
            e.printStackTrace(System.err);
        }
    }
}
