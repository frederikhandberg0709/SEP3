package via.sep.gui.ViewModel;

import com.google.gson.Gson;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import via.sep.gui.Model.RegistrationService;
import via.sep.gui.Model.SceneManager;
import via.sep.gui.Server.ServerConnection;


public class RegisterViewModel {
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    private final StringProperty registrationStatus = new SimpleStringProperty();
    private final RegistrationService registrationService;

    public RegisterViewModel(ServerConnection serverConnection, Gson gson) {
        this.registrationService = new RegistrationService(serverConnection, gson);
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public StringProperty registrationStatusProperty() {
        return registrationStatus;
    }

    public void registerAccount() {
        if (username.get() == null || username.get().isEmpty() ||
                password.get() == null || password.get().isEmpty()) {
            registrationStatus.set("Error: Username and password cannot be empty.");
            return;
        }

        try {
            registrationService.register(username.get(), password.get());
            registrationStatus.set("Success: Account created successfully.");
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            if (errorMessage != null && !errorMessage.isEmpty()) {
                registrationStatus.set("Error: " + errorMessage);
            } else {
                registrationStatus.set("Error: Registration failed. Please try again.");
            }
        }
    }

    public void returnToLogin() {
        SceneManager.showLogin();
    }
}
