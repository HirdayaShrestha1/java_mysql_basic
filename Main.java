import pages.*;
import javax.swing.*;

public class Main {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      ApplicationFrame appFrame = new ApplicationFrame();

      SigninPage signinPage = new SigninPage(appFrame);
      SignupPage signupPage = new SignupPage(appFrame);

      appFrame.addPage("signin", signinPage);
      appFrame.addPage("signup", signupPage);
      appFrame.navigateTo("signin");
      appFrame.setVisible(true);
    });
  }
}
