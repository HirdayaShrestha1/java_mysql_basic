package pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import utils.UIUtils;

public abstract class ResponsivePageBase extends JPanel {
    protected JPanel logoPanel;
    protected JComponent separator;
    protected JPanel root;
    private static final int LOGO_HIDE_WIDTH = 600;

    protected ResponsivePageBase() {
        // Empty constructor - subclasses must call initializeLayout()
    }

    protected void initializeLayout() {
        setLayout(new GridBagLayout());
        setBackground(UIUtils.COLOR_BACKGROUND);

        root = new JPanel(new GridBagLayout());
        root.setBackground(UIUtils.COLOR_BACKGROUND);
        root.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        logoPanel = createLogoPanel();
        separator = createSeparator();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx = 0.4;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        root.add(logoPanel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0;
        gbc.insets = new Insets(0, 20, 0, 20);
        root.add(separator, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0.6;
        gbc.insets = new Insets(0, 0, 0, 0);
        root.add(createContentPanel(), gbc);

        GridBagConstraints centerGbc = new GridBagConstraints();
        centerGbc.gridx = 0;
        centerGbc.gridy = 0;
        centerGbc.anchor = GridBagConstraints.CENTER;
        add(root, centerGbc);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateLayout();
            }
        });

        SwingUtilities.invokeLater(this::updateLayout);
    }

    private void updateLayout() {
        boolean showLogo = getWidth() >= LOGO_HIDE_WIDTH;
        logoPanel.setVisible(showLogo);
        separator.setVisible(showLogo);
        root.revalidate();
        root.repaint();
    }

    protected JPanel createLogoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        JLabel logo = new JLabel();
        logo.setFocusable(false);
        java.net.URL logoUrl = getClass().getResource("/logo.png");
        if (logoUrl != null) {
            logo.setIcon(new ImageIcon(logoUrl));
        } else {
            ImageIcon fileIcon = new ImageIcon("resources/logo.png");
            if (fileIcon.getIconWidth() > 0) {
                logo.setIcon(fileIcon);
            } else {
                logo.setText("Logo");
                logo.setForeground(UIUtils.COLOR_OUTLINE);
                logo.setFont(UIUtils.FONT_GENERAL_UI);
            }
        }
        panel.add(logo);
        return panel;
    }

    protected JComponent createSeparator() {
        JSeparator sep = new JSeparator();
        sep.setOrientation(SwingConstants.VERTICAL);
        sep.setForeground(UIUtils.COLOR_OUTLINE);
        sep.setPreferredSize(new Dimension(1, 240));
        return sep;
    }

    protected abstract JComponent createContentPanel();
}
