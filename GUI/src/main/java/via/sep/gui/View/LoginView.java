package via.sep.gui.View;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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


    /**
     * Default constructor required by FXML.
     */
    public LoginView() {
        // Default constructor
    }


    /**
     * Constructs a LoginView with the specified {@link LoginViewModel}.
     *
     * @param loginViewModel the {@link LoginViewModel} to use for login operations
     */
    public LoginView(LoginViewModel loginViewModel) {
        this.loginViewModel = loginViewModel;
    }

    /**
     * Initializes the View. Binds the UI fields to the view model properties and sets up event handlers.
     */
    @FXML
    private void initialize() {

        if (loginViewModel == null) {
            loginViewModel = new LoginViewModel();
        }
        // Bind text fields to ViewModel properties
        usernameField.textProperty().bindBidirectional(loginViewModel.usernameProperty());
        passwordField.textProperty().bindBidirectional(loginViewModel.passwordProperty());

        registerButton.setOnAction(event -> loginViewModel.showRegistration());
        loginButton.setOnAction(event -> handleLogin());
    }

    /**
     * Handles the login action. Calls the view model to authenticate the user and shows an alert if authentication fails.
     */
    private void handleLogin() {
        boolean success = loginViewModel.authenticate();
        if (!success) {
            showLoginAlert("Login Failed", "Invalid username or password");
        }
    }

    /**
     * Shows an alert dialog for login errors.
     *
     * @param title   the title of the alert
     * @param message the message of the alert
     */
    private void showLoginAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setViewModel(LoginViewModel loginViewModel) {
        this.loginViewModel = loginViewModel;
    }
}
