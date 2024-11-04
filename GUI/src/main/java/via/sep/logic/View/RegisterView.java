package via.sep.logic.View;



import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class RegisterView {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button registerButton;

    @FXML
    private Button cancelButton;

    @FXML
    private void initialize() {
        registerButton.setOnAction(event -> handleRegister());
        cancelButton.setOnAction(event -> handleCancel());
    }

    private void handleRegister() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (isValidInput(username, email, password)) {
            showAlert("Registration Successful", "Welcome " + username + "!");
        } else {
            showAlert("Registration Failed", "Please fill in all fields correctly.");
        }
    }

    private void handleCancel() {
        usernameField.clear();
        emailField.clear();
        passwordField.clear();
    }

    private boolean isValidInput(String username, String email, String password) {
        // Replace with actual validation logic
        return !username.isEmpty() && !email.isEmpty() && !password.isEmpty();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
