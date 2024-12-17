package via.sep.gui.View;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import via.sep.gui.Model.SceneManager;
import via.sep.gui.Server.ServerConnection;
import via.sep.gui.ViewModel.RegisterViewModel;

public class RegisterView {
    @FXML
    private TextField usernameRegister;
    @FXML
    private TextField passwordRegister;
    @FXML
    private TextField fullNameRegister;
    @FXML
    private TextField emailRegister;
    @FXML
    private TextField phoneNumberRegister;
    @FXML
    private TextField addressRegister;
    @FXML
    private Button createAccountButton;
    @FXML
    private Button goBackToLoginButton;

    private RegisterViewModel viewModel;
    private ServerConnection serverConnection;
    private Gson gson;

    public RegisterView() {}

    public void setViewModel(RegisterViewModel viewModel) {
        this.viewModel = viewModel;

        setupBindings();
    }

    private void setupBindings() {
        if (usernameRegister != null && passwordRegister != null && fullNameRegister != null && emailRegister != null && phoneNumberRegister != null && addressRegister != null && viewModel != null) {
            usernameRegister.textProperty().bindBidirectional(viewModel.usernameProperty());
            passwordRegister.textProperty().bindBidirectional(viewModel.passwordProperty());
            fullNameRegister.textProperty().bindBidirectional(viewModel.fullNameProperty());
            emailRegister.textProperty().bindBidirectional(viewModel.emailProperty());
            phoneNumberRegister.textProperty().bindBidirectional(viewModel.phoneNumberProperty());
            addressRegister.textProperty().bindBidirectional(viewModel.addressProperty());

            viewModel.registrationStatusProperty().addListener((observableValue, oldStatus, newStatus) -> {
                if (newStatus != null && !newStatus.isEmpty()) {
                    showRegistrationAlert(newStatus.startsWith("Success") ? "Success" : "Error", newStatus);
                    if (newStatus.startsWith("Success")) {
                        goBackToLogin();
                    }
                    viewModel.registrationStatusProperty().set("");
                }
            });
        }
    }

    @FXML
    private void initialize() {
        this.serverConnection = SceneManager.getServerConnection();
        this.gson = SceneManager.getGson();

        createAccountButton.setOnAction(event -> {
            if (viewModel != null) {
                System.out.println("\nCreate Account clicked:");
                System.out.println("TextField username: '" + usernameRegister.getText() + "'");
                System.out.println("TextField password: '" + passwordRegister.getText() + "'");
                System.out.println("ViewModel username: '" + viewModel.usernameProperty().getValue() + "'");
                System.out.println("ViewModel password: '" + viewModel.passwordProperty().getValue() + "'");
                handleCreateAccount();
            } else {
                System.out.println("ViewModel is null!");
            }
        });

        goBackToLoginButton.setOnAction(event -> goBackToLogin());

        if (viewModel != null) {
            setupBindings();
        }
    }

    private void goBackToLogin() {
        SceneManager.showLogin();
    }

    private void handleCreateAccount() {
        if (usernameRegister.getText().trim().isEmpty() ||
                passwordRegister.getText().trim().isEmpty()) {
            showRegistrationAlert("Error", "Username and password cannot be empty");
            return;
        }
        viewModel.registerAccount();
    }

    private void showRegistrationAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
