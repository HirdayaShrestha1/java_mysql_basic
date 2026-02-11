package pages;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;

import db.DBConnection;
import utils.HyperlinkText;
import utils.PasswordVisibilityToggle;
import utils.RoundedButton;
import utils.TextFieldPassword;
import utils.TextFieldUsername;
import utils.UIUtils;

public class SigninPage extends ResponsivePageBase {

    private final NavigationManager navigationManager;
    private TextFieldUsername usernameField;
    private TextFieldPassword passwordField;

    public SigninPage(NavigationManager navigationManager) {
        super();
        this.navigationManager = navigationManager;
        initializeLayout();
    }

    @Override
    protected JComponent createContentPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setOpaque(false);
        formPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 16, 0);

        JLabel title = new JLabel("Sign in");
        title.setFont(UIUtils.FONT_GENERAL_UI.deriveFont(Font.BOLD, 20f));
        title.setForeground(Color.white);
        formPanel.add(title, gbc);

        gbc.gridy++;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 12, 0);
        usernameField = new TextFieldUsername();
        configureUsernameField(usernameField);
        usernameField.setPreferredSize(new Dimension(260, 32));
        formPanel.add(usernameField, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 16, 0);
        passwordField = new TextFieldPassword();
        configurePasswordField(passwordField);
        PasswordVisibilityToggle passwordToggle = new PasswordVisibilityToggle(passwordField);
        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.setOpaque(false);
        passwordPanel.add(passwordField, BorderLayout.CENTER);
        passwordPanel.add(passwordToggle, BorderLayout.EAST);
        passwordField.setPreferredSize(new Dimension(260, 32));
        formPanel.add(passwordPanel, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 12, 0);
        RoundedButton loginButton = new RoundedButton(
            UIUtils.BUTTON_TEXT_LOGIN,
            UIUtils.COLOR_INTERACTIVE,
            Color.white,
            UIUtils.COLOR_INTERACTIVE_DARKER,
            UIUtils.OFFWHITE
        );
        loginButton.setPreferredSize(new Dimension(260, 44));
        loginButton.setOnClick(this::loginEventHandler);
        formPanel.add(loginButton, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 0, 0);
        JPanel links = new JPanel(new BorderLayout());
        links.setOpaque(false);
        HyperlinkText forgot = new HyperlinkText(UIUtils.BUTTON_TEXT_FORGOT_PASS, () -> {
            // toaster.error("Forgot password event");
        });
        HyperlinkText register = new HyperlinkText(UIUtils.BUTTON_TEXT_REGISTER, () ->
            navigationManager.navigateTo("signup")
        );
        links.add(forgot, BorderLayout.WEST);
        links.add(register, BorderLayout.EAST);
        formPanel.add(links, gbc);

        return formPanel;
    }

    private void configureUsernameField(TextFieldUsername field) {
        field.setText(UIUtils.PLACEHOLDER_TEXT_USERNAME);
        field.setForeground(UIUtils.COLOR_OUTLINE);
        field.setBorderColor(UIUtils.COLOR_OUTLINE);
        field.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(UIUtils.PLACEHOLDER_TEXT_USERNAME)) {
                    field.setText("");
                }
                field.setForeground(Color.white);
                field.setBorderColor(UIUtils.COLOR_INTERACTIVE);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(UIUtils.PLACEHOLDER_TEXT_USERNAME);
                }
                field.setForeground(UIUtils.COLOR_OUTLINE);
                field.setBorderColor(UIUtils.COLOR_OUTLINE);
            }
        });
    }

    private void configurePasswordField(TextFieldPassword field) {
        field.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setForeground(Color.white);
                field.setBorderColor(UIUtils.COLOR_INTERACTIVE);
            }

            @Override
            public void focusLost(FocusEvent e) {
                field.setForeground(UIUtils.COLOR_OUTLINE);
                field.setBorderColor(UIUtils.COLOR_OUTLINE);
            }
        });

        field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    loginEventHandler();
                }
            }
        });
    }

    private void loginEventHandler() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty() || username.equals(UIUtils.PLACEHOLDER_TEXT_USERNAME)) {
            JOptionPane.showMessageDialog(this, "Username and password are required", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM users WHERE username=? AND password=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                navigationManager.navigateTo("home", username);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
                passwordField.requestFocus();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}