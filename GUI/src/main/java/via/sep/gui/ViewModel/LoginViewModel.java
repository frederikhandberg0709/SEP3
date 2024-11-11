package via.sep.gui.ViewModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;

public class LoginViewModel {
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();

    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public boolean authenticate() {
        // Implement your authentication logic here
        // For demonstration, let's assume the username is "user" and the password is "pass"
        return "user".equals(username.get()) && "pass".equals(password.get());
    }

    public void showRegistration() {
        // Implement your registration logic here
        // For demonstration, let's show an alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registration");
        alert.setHeaderText(null);
        alert.setContentText("Registration process initiated.");
        alert.showAndWait();
    }
}