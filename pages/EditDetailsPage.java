package pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.*;

import db.DBConnection;
import utils.HyperlinkText;
import utils.RoundedButton;
import utils.TextFieldUsername;
import utils.UIUtils;

public class EditDetailsPage extends ResponsivePageBase {
    private static final String PLACEHOLDER_FULLNAME = "Full name";
    private static final String PLACEHOLDER_ADDRESS = "Address";

    private final NavigationManager navigationManager;
    private final String username;
    private TextFieldUsername fullnameField;
    private TextFieldUsername addressField;

    public EditDetailsPage(NavigationManager navigationManager, String username) {
        super();
        this.navigationManager = navigationManager;
        this.username = username;
        initializeLayout();
    }

    @Override
    protected JComponent createContentPanel() {
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("Edit Your Details");
        title.setFont(UIUtils.FONT_GENERAL_UI.deriveFont(Font.BOLD, 20f));
        title.setForeground(Color.white);
        header.add(title, BorderLayout.WEST);
        header.add(new HyperlinkText("Back", () -> navigationManager.navigateTo("home", username)), BorderLayout.EAST);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(header);
        content.add(Box.createVerticalStrut(16));

        fullnameField = new TextFieldUsername();
        addFieldBehavior(fullnameField, PLACEHOLDER_FULLNAME);
        fullnameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        fullnameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        content.add(fullnameField);
        content.add(Box.createVerticalStrut(12));

        addressField = new TextFieldUsername();
        addFieldBehavior(addressField, PLACEHOLDER_ADDRESS);
        addressField.setAlignmentX(Component.LEFT_ALIGNMENT);
        addressField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        content.add(addressField);
        content.add(Box.createVerticalStrut(16));

        RoundedButton saveBtn = new RoundedButton(
            "Save Changes",
            UIUtils.COLOR_INTERACTIVE,
            Color.white,
            UIUtils.COLOR_INTERACTIVE_DARKER,
            UIUtils.OFFWHITE
        );
        saveBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        saveBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        saveBtn.setPreferredSize(new Dimension(260, 44));
        saveBtn.setOnClick(this::saveData);
        content.add(saveBtn);
        content.add(Box.createVerticalStrut(10));

        RoundedButton cancelBtn = new RoundedButton(
            "Cancel",
            UIUtils.COLOR_OUTLINE,
            Color.white,
            UIUtils.COLOR_OUTLINE.darker(),
            UIUtils.OFFWHITE
        );
        cancelBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        cancelBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        cancelBtn.setPreferredSize(new Dimension(260, 44));
        cancelBtn.setOnClick(() -> navigationManager.navigateTo("home", username));
        content.add(cancelBtn);

        SwingUtilities.invokeLater(this::loadData);
        return content;
    }

    private void addFieldBehavior(TextFieldUsername field, String placeholder) {
        field.setText(placeholder);
        field.setForeground(UIUtils.COLOR_OUTLINE);
        field.setBorderColor(UIUtils.COLOR_OUTLINE);
        field.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                }
                field.setForeground(Color.white);
                field.setBorderColor(UIUtils.COLOR_INTERACTIVE);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                }
                field.setForeground(UIUtils.COLOR_OUTLINE);
                field.setBorderColor(UIUtils.COLOR_OUTLINE);
            }
        });
    }

    private void loadData() {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT fullname, address FROM users WHERE username=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String fullname = rs.getString("fullname");
                String address = rs.getString("address");

                if (fullname != null && !fullname.isBlank()) {
                    fullnameField.setText(fullname);
                    fullnameField.setForeground(Color.white);
                    fullnameField.setBorderColor(UIUtils.COLOR_INTERACTIVE);
                }

                if (address != null && !address.isBlank()) {
                    addressField.setText(address);
                    addressField.setForeground(Color.white);
                    addressField.setBorderColor(UIUtils.COLOR_INTERACTIVE);
                }
                
                fullnameField.revalidate();
                addressField.revalidate();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveData() {
        String fullname = fullnameField.getText().trim();
        String address = addressField.getText().trim();

        if (fullname.isEmpty() || fullname.equals(PLACEHOLDER_FULLNAME)) {
            JOptionPane.showMessageDialog(this, "Full name is required", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (address.equals(PLACEHOLDER_ADDRESS)) {
            address = "";
        }

        try (Connection con = DBConnection.getConnection()) {
            String sql = "UPDATE users SET fullname=?, address=? WHERE username=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, fullname);
            ps.setString(2, address);
            ps.setString(3, username);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Details updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            navigationManager.navigateTo("home", username);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}


