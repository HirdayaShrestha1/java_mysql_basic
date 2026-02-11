package pages;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class ThemeManager {
    public static class Theme {
        public final Color bg;
        public final Color surface;
        public final Color text;
        public final Color textMuted;
        public final Color primary;
        public final Color primaryDark;
        public final Color border;
        public final Color danger;

        public Theme(Color bg, Color surface, Color text, Color textMuted,
                     Color primary, Color primaryDark, Color border, Color danger) {
            this.bg = bg;
            this.surface = surface;
            this.text = text;
            this.textMuted = textMuted;
            this.primary = primary;
            this.primaryDark = primaryDark;
            this.border = border;
            this.danger = danger;
        }
    }

    private static final Theme LIGHT = new Theme(
        new Color(248, 250, 252),
        Color.WHITE,
        new Color(45, 55, 72),
        new Color(107, 114, 128),
        new Color(74, 112, 227),
        new Color(58, 95, 205),
        new Color(206, 212, 218),
        new Color(220, 38, 38)
    );

    public static Theme getTheme() {
        return LIGHT;
    }

    public static void applyTheme(Component component) {
        Theme theme = getTheme();
        applyRecursive(component, theme);
        component.revalidate();
        component.repaint();
    }

    private static void applyRecursive(Component component, Theme theme) {
        if (component instanceof JComponent) {
            JComponent jc = (JComponent) component;
            String name = jc.getName();

            if (jc instanceof JPanel) {
                if (jc.isOpaque()) {
                    if ("card".equals(name)) {
                        jc.setBackground(theme.surface);
                        jc.setBorder(BorderFactory.createCompoundBorder(
                            new LineBorder(theme.border, 1, true),
                            BorderFactory.createEmptyBorder(18, 18, 18, 18)
                        ));
                    } else if ("surface".equals(name)) {
                        jc.setBackground(theme.surface);
                    } else if ("bg".equals(name)) {
                        jc.setBackground(theme.bg);
                    } else {
                        jc.setBackground(theme.bg);
                    }
                }
            }

            if (jc instanceof JLabel) {
                if ("muted".equals(name)) {
                    jc.setForeground(theme.textMuted);
                } else {
                    jc.setForeground(theme.text);
                }
            }

            if (jc instanceof JTextField || jc instanceof JPasswordField) {
                jc.setBackground(theme.surface);
                jc.setForeground(theme.text);
                jc.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(theme.border, 1, true),
                    BorderFactory.createEmptyBorder(12, 15, 12, 15)
                ));
            }

            if (jc instanceof JButton || jc instanceof JToggleButton) {
                if ("primary".equals(name)) {
                    jc.setBackground(theme.primary);
                    jc.setForeground(Color.WHITE);
                    jc.setBorder(new LineBorder(theme.primary, 1, true));
                } else if ("danger".equals(name)) {
                    jc.setBackground(theme.surface);
                    jc.setForeground(theme.danger);
                    jc.setBorder(new LineBorder(theme.border, 1, true));
                } else if ("link".equals(name)) {
                    jc.setForeground(theme.primary);
                } else {
                    jc.setBackground(theme.surface);
                    jc.setForeground(theme.text);
                    jc.setBorder(new LineBorder(theme.border, 1, true));
                }
            }
        }

        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                applyRecursive(child, theme);
            }
        }
    }
}