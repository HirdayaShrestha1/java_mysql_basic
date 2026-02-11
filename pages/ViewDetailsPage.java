package pages;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

import db.DBConnection;
import utils.HyperlinkText;
import utils.RoundedButton;
import utils.UIUtils;

public class ViewDetailsPage extends ResponsivePageBase {
    private final NavigationManager navigationManager;
    private final String username;

    public ViewDetailsPage(NavigationManager navigationManager, String username) {
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
        JLabel title = new JLabel("Your Profile");
        title.setFont(UIUtils.FONT_GENERAL_UI.deriveFont(Font.BOLD, 20f));
        title.setForeground(Color.white);
        header.add(title, BorderLayout.WEST);
        header.add(new HyperlinkText("Back", () -> navigationManager.navigateTo("home", username)), BorderLayout.EAST);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(header);
        content.add(Box.createVerticalStrut(16));

        JLabel usernameLabel = new JLabel("Username: " + username);
        usernameLabel.setFont(UIUtils.FONT_GENERAL_UI.deriveFont(Font.PLAIN, 14f));
        usernameLabel.setForeground(UIUtils.OFFWHITE);
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(usernameLabel);
        content.add(Box.createVerticalStrut(8));

        JLabel fullnameLabel = new JLabel("Full name: Loading...");
        fullnameLabel.setFont(UIUtils.FONT_GENERAL_UI.deriveFont(Font.PLAIN, 14f));
        fullnameLabel.setForeground(UIUtils.OFFWHITE);
        fullnameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(fullnameLabel);
        content.add(Box.createVerticalStrut(8));

        JLabel addressLabel = new JLabel("Address: Loading...");
        addressLabel.setFont(UIUtils.FONT_GENERAL_UI.deriveFont(Font.PLAIN, 14f));
        addressLabel.setForeground(UIUtils.OFFWHITE);
        addressLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(addressLabel);
        content.add(Box.createVerticalStrut(16));

        RoundedButton editBtn = new RoundedButton(
            "Edit Profile",
            UIUtils.COLOR_INTERACTIVE,
            Color.white,
            UIUtils.COLOR_INTERACTIVE_DARKER,
            UIUtils.OFFWHITE
        );
        editBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        editBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        editBtn.setPreferredSize(new Dimension(260, 44));
        editBtn.setOnClick(() -> navigationManager.navigateTo("editdetails", username));
        content.add(editBtn);
        content.add(Box.createVerticalStrut(10));

        RoundedButton closeBtn = new RoundedButton(
            "Close",
            UIUtils.COLOR_OUTLINE,
            Color.white,
            UIUtils.COLOR_OUTLINE.darker(),
            UIUtils.OFFWHITE
        );
        closeBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        closeBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        closeBtn.setPreferredSize(new Dimension(260, 44));
        closeBtn.setOnClick(() -> navigationManager.navigateTo("home", username));
        content.add(closeBtn);

        SwingUtilities.invokeLater(() -> loadData(fullnameLabel, addressLabel));
        return content;
    }

    private void loadData(JLabel fullnameLabel, JLabel addressLabel) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT fullname, address FROM users WHERE username=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                fullnameLabel.setText("Full name: " + rs.getString("fullname"));
                String address = rs.getString("address");
                addressLabel.setText("Address: " + (address != null ? address : "Not specified"));
                fullnameLabel.revalidate();
                addressLabel.revalidate();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            fullnameLabel.setText("Full name: Error loading data");
            addressLabel.setText("Address: Error loading data");
        }
    }
}
