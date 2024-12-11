package via.sep.gui.View;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import via.sep.gui.Server.ServerConnection;
import via.sep.gui.ViewModel.LoginViewModel;

/**
 * View for the login view. Handles user input and interactions for logging in.
 */
public class LoginView {
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;

    private LoginViewModel loginViewModel;
    private final ServerConnection serverConnection;
    private final Gson gson;

    public LoginView(ServerConnection serverConnection, Gson gson) {
        this.serverConnection = serverConnection;
        this.gson = gson;
    }

    @FXML
    private void initialize() {
        if (loginViewModel == null) {
            loginViewModel = new LoginViewModel(serverConnection, gson);
        }

        usernameField.textProperty().bindBidirectional(loginViewModel.usernameProperty());
        passwordField.textProperty().bindBidirectional(loginViewModel.passwordProperty());

        // Set up button handlers
        registerButton.setOnAction(event -> loginViewModel.showRegistration());
        loginButton.setOnAction(event -> handleLogin());

        // Listen for login status changes
        loginViewModel.loginStatusProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                boolean isError = !loginViewModel.authenticate();
                if (isError) {
                    showAlert("Login Error", newValue, Alert.AlertType.ERROR);
                } else {
                    SceneManager.showMainView();
                }
            }
        });
    }

    public LoginView() {
        if (loginViewModel.authenticate()) {
            SceneManager.showMainView();
        }
    }

    private void handleLogin() {
        if (loginViewModel.authenticate()) {
            SceneManager.showMainView();
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setViewModel(LoginViewModel loginViewModel) {
        this.loginViewModel = loginViewModel;
    }
}
