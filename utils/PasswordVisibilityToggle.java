package utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class PasswordVisibilityToggle extends JLabel {
    private boolean isVisible;
    private JPasswordField passwordField;

    public PasswordVisibilityToggle(JPasswordField passwordField) {
        this.passwordField = passwordField;
        this.isVisible = false;

        setPreferredSize(new Dimension(32, 32));
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        updateIcon();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                toggleVisibility();
            }
        });
    }

    private void toggleVisibility() {
        isVisible = !isVisible;
        updateIcon();
        updateFieldVisibility();
    }

    private void updateIcon() {
        String iconPath = isVisible ? "resources/oeye.png" : "resources/ceye.png";
        ImageIcon icon = loadIcon(iconPath);
        if (icon != null && icon.getIconWidth() > 0) {
            setIcon(toWhiteIcon(new ImageIcon(icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)), 20));
        }
    }

    private ImageIcon toWhiteIcon(ImageIcon icon, int size) {
        Image img = icon.getImage();

        BufferedImage bi = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bi.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        g.drawImage(img, 0, 0, size, size, null);

        g.setComposite(AlphaComposite.SrcAtop);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, size, size);

        g.dispose();
        return new ImageIcon(bi);
    }

    private void updateFieldVisibility() {
        if (isVisible) {
            // Show password as plain text
            String password = new String(passwordField.getPassword());
            JTextField textField = new JTextField(password);
            textField.setFont(passwordField.getFont());
            textField.setBackground(passwordField.getBackground());
            textField.setForeground(passwordField.getForeground());
            textField.setBorder(passwordField.getBorder());
            textField.setMargin(passwordField.getMargin());
            textField.setCaretColor(passwordField.getCaretColor());

            // Replace with text field - this is complex, so we'll use a different approach
            // We'll store the original text and modify the field's echo character
            passwordField.setEchoChar((char) 0);
        } else {
            // Hide password
            passwordField.setEchoChar('•');
        }
    }

    private ImageIcon loadIcon(String path) {
        // Try classpath first
        java.net.URL iconUrl = getClass().getResource("/" + path.replace("resources/", ""));
        if (iconUrl != null) {
            return new ImageIcon(iconUrl);
        }
        // Try file path
        ImageIcon fileIcon = new ImageIcon(path);
        if (fileIcon.getIconWidth() > 0) {
            return fileIcon;
        }
        return null;
    }

    public boolean isPasswordVisible() {
        return isVisible;
    }

    public void reset() {
        isVisible = false;
        updateIcon();
        updateFieldVisibility();
    }
}
