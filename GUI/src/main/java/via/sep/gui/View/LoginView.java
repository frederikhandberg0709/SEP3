package via.sep.gui.View;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import via.sep.gui.Server.ServerConnection;
import via.sep.gui.ViewModel.LoginViewModel;
import via.sep.gui.Model.SceneManager;

public class LoginView {
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;

    private LoginViewModel viewModel;
    private ServerConnection serverConnection;
    private Gson gson;

    public LoginView() {}

    public void setViewModel(LoginViewModel viewModel) {
        this.viewModel = viewModel;

        setupBindings();
    }

    private void setupBindings() {
        if (usernameField != null && passwordField != null) {
            usernameField.textProperty().bindBidirectional(viewModel.usernameProperty());
            passwordField.textProperty().bindBidirectional(viewModel.passwordProperty());

            registerButton.setOnAction(event -> viewModel.showRegistration());

            viewModel.loginStatusProperty().addListener((observableValue, oldStatus, newStatus) -> {
                if (newStatus != null && !newStatus.isEmpty()) {
                    showLoginAlert(newStatus.startsWith("Success") ? "Success" : "Error", newStatus);
                    if (newStatus.startsWith("Success")) {
                        SceneManager.showDashboard();
                    }
                    viewModel.loginStatusProperty().set("");
                }
            });
        }
    }

    @FXML
    private void initialize() {
        this.serverConnection = SceneManager.getServerConnection();
        this.gson = SceneManager.getGson();

        loginButton.setOnAction(event -> {
            if (viewModel != null) {
                handleLogin();
            } else {
                System.out.println("ViewModel is null!");
            }
        });

        if (viewModel != null) {
            setupBindings();
        }
    }

    private void handleLogin() {
        if (viewModel.authenticate()) {
            SceneManager.showDashboard();
        } else {
            showLoginAlert("Error", viewModel.loginStatusProperty().get());
        }
    }

    private void showLoginAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
