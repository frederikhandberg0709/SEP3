package via.sep.gui.View;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginView {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (isValidCredentials(username, password)) {
            showAlert("Login Successful", "Welcome " + username + "!");
        } else {
            showAlert("Login Failed", "Invalid username or password.");
        }
    }

    private boolean isValidCredentials(String username, String password) {
        // Replace with actual validation logic
        return "admin".equals(username) && "password".equals(password);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
