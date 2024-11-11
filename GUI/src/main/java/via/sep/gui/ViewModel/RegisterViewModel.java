package via.sep.gui.ViewModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import via.sep.gui.DataBase.RegistrationService;


public class RegisterViewModel {
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    private final StringProperty registrationStatus = new SimpleStringProperty();
    private final RegistrationService registrationService = new RegistrationService();

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
        if (username.get() != null && !username.get().isEmpty() && password.get() != null && !password.get().isEmpty()) {
            boolean success = registrationService.register(username.get(), password.get());
            if (success) {
                registrationStatus.set("Success: Account created successfully.");
            } else {
                registrationStatus.set("Error: Registration failed. Please try again.");
            }
        } else {
            registrationStatus.set("Error: Username and password cannot be empty.");
        }
    }

}
