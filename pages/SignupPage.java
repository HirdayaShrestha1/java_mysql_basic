package pages;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import javax.swing.*;

import db.DBConnection;
import utils.HyperlinkText;
import utils.PasswordVisibilityToggle;
import utils.RoundedButton;
import utils.TextFieldPassword;
import utils.TextFieldUsername;
import utils.UIUtils;

public class SignupPage extends ResponsivePageBase {

    private static final String PLACEHOLDER_TEXT_FULLNAME = "Full name";

    private final NavigationManager navigationManager;
    private TextFieldUsername usernameField;
    private TextFieldUsername fullnameField;
    private TextFieldPassword passwordField;

    public SignupPage(NavigationManager navigationManager) {
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

        JLabel title = new JLabel("Create account");
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
        fullnameField = new TextFieldUsername();
        configureFullnameField(fullnameField);
        fullnameField.setPreferredSize(new Dimension(260, 32));
        formPanel.add(fullnameField, gbc);

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
        RoundedButton registerButton = new RoundedButton(
            UIUtils.BUTTON_TEXT_REGISTER,
            UIUtils.COLOR_INTERACTIVE,
            Color.white,
            UIUtils.COLOR_INTERACTIVE_DARKER,
            UIUtils.OFFWHITE
        );
        registerButton.setPreferredSize(new Dimension(260, 44));
        registerButton.setOnClick(this::registerEventHandler);
        formPanel.add(registerButton, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 0, 0);
        JPanel links = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        links.setOpaque(false);
        JLabel alreadyLabel = new JLabel("Already have an account?");
        alreadyLabel.setFont(UIUtils.FONT_GENERAL_UI.deriveFont(Font.PLAIN, 12f));
        alreadyLabel.setForeground(UIUtils.COLOR_OUTLINE);
        HyperlinkText login = new HyperlinkText(UIUtils.BUTTON_TEXT_LOGIN, () ->
            navigationManager.navigateTo("signin")
        );
        links.add(alreadyLabel);
        links.add(login);
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

    private void configureFullnameField(TextFieldUsername field) {
        field.setText(PLACEHOLDER_TEXT_FULLNAME);
        field.setForeground(UIUtils.COLOR_OUTLINE);
        field.setBorderColor(UIUtils.COLOR_OUTLINE);
        field.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(PLACEHOLDER_TEXT_FULLNAME)) {
                    field.setText("");
                }
                field.setForeground(Color.white);
                field.setBorderColor(UIUtils.COLOR_INTERACTIVE);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(PLACEHOLDER_TEXT_FULLNAME);
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
                    registerEventHandler();
                }
            }
        });
    }

    private void registerEventHandler() {
        String username = usernameField.getText().trim();
        String fullname = fullnameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || fullname.isEmpty() || password.isEmpty()
            || username.equals(UIUtils.PLACEHOLDER_TEXT_USERNAME)
            || fullname.equals(PLACEHOLDER_TEXT_FULLNAME)) {
            JOptionPane.showMessageDialog(this, "All fields are required", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection con = DBConnection.getConnection()) {
            String sql = "INSERT INTO users (username, fullname, password) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, fullname);
            ps.setString(3, password);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Account created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            navigationManager.navigateTo("signin");
        } catch (SQLIntegrityConstraintViolationException ex) {
            JOptionPane.showMessageDialog(this, "Username already exists. Please choose another.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
