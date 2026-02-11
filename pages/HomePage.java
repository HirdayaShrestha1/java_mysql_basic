package pages;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import utils.HyperlinkText;
import utils.RoundedButton;
import utils.UIUtils;

public class HomePage extends ResponsivePageBase {
    private final String username;
    private final NavigationManager navigationManager;

    public HomePage(NavigationManager navigationManager, String username) {
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

        JLabel title = new JLabel("Welcome back, " + username);
        title.setFont(UIUtils.FONT_GENERAL_UI.deriveFont(Font.BOLD, 20f));
        title.setForeground(Color.white);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(title);

        JLabel subtitle = new JLabel("Manage your account and view your details");
        subtitle.setFont(UIUtils.FONT_GENERAL_UI.deriveFont(Font.PLAIN, 12f));
        subtitle.setForeground(UIUtils.COLOR_OUTLINE);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(subtitle);
        content.add(Box.createVerticalStrut(16));

        RoundedButton viewBtn = new RoundedButton(
            "View Details",
            UIUtils.COLOR_INTERACTIVE,
            Color.white,
            UIUtils.COLOR_INTERACTIVE_DARKER,
            UIUtils.OFFWHITE
        );
        viewBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        viewBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        viewBtn.setPreferredSize(new Dimension(260, 44));
        viewBtn.setOnClick(() -> navigationManager.navigateTo("viewdetails", username));
        content.add(viewBtn);
        content.add(Box.createVerticalStrut(10));

        RoundedButton editBtn = new RoundedButton(
            "Edit Details",
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

        RoundedButton logoutBtn = new RoundedButton(
            "Logout",
            UIUtils.COLOR_OUTLINE,
            Color.white,
            UIUtils.COLOR_OUTLINE.darker(),
            UIUtils.OFFWHITE
        );
        logoutBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoutBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        logoutBtn.setPreferredSize(new Dimension(260, 44));
        logoutBtn.setOnClick(() -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?", "Confirm Logout",
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                navigationManager.logout();
            }
        });
        content.add(logoutBtn);
        content.add(Box.createVerticalStrut(16));

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
        String timeText = "Last login: " + LocalDateTime.now().format(dtf);
        HyperlinkText footer = new HyperlinkText(timeText, () -> {});
        footer.setCursor(Cursor.getDefaultCursor());
        footer.setForeground(UIUtils.COLOR_OUTLINE);
        footer.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(footer);

        return content;
    }
}
