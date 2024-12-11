package via.sep.gui.ViewModel;

import com.google.gson.Gson;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import via.sep.gui.Model.SessionManager;
import via.sep.gui.Model.UserService;
import via.sep.gui.Model.dto.LoginResponseDTO;
import via.sep.gui.Server.ServerConnection;
import via.sep.gui.Model.SceneManager;

public class LoginViewModel {
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    private final StringProperty loginStatus = new SimpleStringProperty();
    private final UserService userService;

    public LoginViewModel(ServerConnection serverConnection, Gson gson) {
        this.userService = new UserService(serverConnection, gson);
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public StringProperty loginStatusProperty() {
        return loginStatus;
    }

    public boolean authenticate() {
        if (username.get() == null || username.get().isEmpty() ||
                password.get() == null || password.get().isEmpty()) {
            loginStatus.set("Username and password cannot be empty");
            return false;
        }

        try {
            LoginResponseDTO response = userService.login(username.get(), password.get());
            if (response != null && response.getToken() != null) {
                // Store the token and user info for the session
                SessionManager.getInstance().setCurrentUser(response);
                loginStatus.set("Login successful");

                return true;
            } else {
                loginStatus.set("Invalid credentials");
                return false;
            }
        } catch (Exception e) {
            loginStatus.set(e.getMessage() != null ? e.getMessage() : "Login failed. Please try again.");
            return false;
        }
    }

    public void showRegistration() {
        SceneManager.showRegister();
    }
}
