package pages;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class ApplicationFrame extends JFrame implements NavigationManager {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private String currentPage;
    private String currentUsername;
    private boolean isAuthenticated;
    private Set<String> addedPages;
    
    public ApplicationFrame() {
        setTitle("Authentication App");
        setSize(800, 400);
        setMinimumSize(new Dimension(300, 400));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        
        addedPages = new HashSet<>();
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        currentUsername = null;
        isAuthenticated = false;
        
        add(mainPanel);
    }
    
    public void addPage(String pageName, JPanel panel) {
        if (!addedPages.contains(pageName)) {
            mainPanel.add(panel, pageName);
            addedPages.add(pageName);
        }
    }
    
    @Override
    public void navigateTo(String page, Object... params) {
        // Handle dynamic page creation
        if (page.equals("home") && params.length > 0) {
            String username = (String) params[0];
            currentUsername = username;
            isAuthenticated = true;
            currentPage = "home_" + username;
            HomePage homePage = new HomePage(this, username);
            addPage(currentPage, homePage);
            cardLayout.show(mainPanel, currentPage);
        } else if (page.equals("viewdetails") && params.length > 0) {
            String username = (String) params[0];
            currentPage = "viewdetails_" + username;
            ViewDetailsPage viewDetailsPage = new ViewDetailsPage(this, username);
            addPage(currentPage, viewDetailsPage);
            cardLayout.show(mainPanel, currentPage);
        } else if (page.equals("editdetails") && params.length > 0) {
            String username = (String) params[0];
            currentPage = "editdetails_" + username;
            EditDetailsPage editDetailsPage = new EditDetailsPage(this, username);
            addPage(currentPage, editDetailsPage);
            cardLayout.show(mainPanel, currentPage);
        } else {
            currentPage = page;
            cardLayout.show(mainPanel, page);
        }
    }
    
    @Override
    public void goBack() {
        // If authenticated, go back to home page
        if (isAuthenticated && currentUsername != null) {
            navigateTo("home", currentUsername);
        }
    }
    
    public void logout() {
        currentUsername = null;
        isAuthenticated = false;
        currentPage = "signin";
        cardLayout.show(mainPanel, "signin");
    }
    
    public String getCurrentUsername() {
        return currentUsername;
    }
    
    public boolean isAuthenticated() {
        return isAuthenticated;
    }
}
