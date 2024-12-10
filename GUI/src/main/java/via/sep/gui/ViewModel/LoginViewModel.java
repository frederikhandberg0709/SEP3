package via.sep.gui.ViewModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import via.sep.gui.Server.ServerConnection;
import via.sep.gui.Model.SceneManager;

public class LoginViewModel {
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    private final ServerConnection serverConnection = new ServerConnection();

    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public boolean authenticate() {
        try {
            String endpoint = "/login";
            String jsonInputString = String.format("{\"username\":\"%s\", \"password\":\"%s\"}", username.get(), password.get());
            String response = serverConnection.sendPostRequest(endpoint, jsonInputString);

            // Assuming the server returns a JSON response with a field "authenticated" indicating success
            return response.contains("\"authenticated\":true");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void showRegistration() {
        SceneManager.showRegister();
    }
}