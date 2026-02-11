package pages;

public interface NavigationManager {
    void navigateTo(String page, Object... params);
    void goBack();
    void logout();
}
