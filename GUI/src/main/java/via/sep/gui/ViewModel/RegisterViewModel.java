package via.sep.gui.ViewModel;

import com.google.gson.Gson;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import via.sep.gui.Model.RegistrationService;
import via.sep.gui.Server.ServerConnection;


public class RegisterViewModel {
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    private final StringProperty fullName = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty phoneNumber = new SimpleStringProperty();
    private final StringProperty address = new SimpleStringProperty();
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

    public StringProperty fullNameProperty() {
        return fullName;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public StringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public StringProperty addressProperty() {
        return address;
    }

    public StringProperty registrationStatusProperty() {
        return registrationStatus;
    }

    public void registerAccount() {
        System.out.println("RegisterViewModel.registerAccount called");

        System.out.println("Username value: '" + username.getValue() + "'");
        System.out.println("Password value: '" + password.getValue() + "'");

        if (username.get() == null || username.get().isEmpty() ||
                password.get() == null || password.get().isEmpty()) {
            System.out.println("Username or password empty");
            registrationStatus.set("Error: Username and password cannot be empty.");
            return;
        }

        if (fullName.get() == null || fullName.get().isEmpty()) {
            registrationStatus.set("Error: Please enter your full name.");
            return;
        }

        if (email.get() == null || email.get().isEmpty()) {
            registrationStatus.set("Error: Please enter your email address.");
            return;
        }

        if (phoneNumber.get() == null || phoneNumber.get().isEmpty()) {
            registrationStatus.set("Error: Please enter your phone number.");
            return;
        }

        if (address.get() == null || address.get().isEmpty()) {
            registrationStatus.set("Error: Please enter your address.");
            return;
        }

        try {
            System.out.println("Attempting to register with username: " + username.get());
            registrationService.register(username.get(), password.get(), fullName.get(), email.get(), phoneNumber.get(), address.get());
            registrationStatus.set("Success: Account created successfully.");
        } catch (Exception e) {
            System.out.println("Registration failed: " + e.getMessage());
            String errorMessage = e.getMessage();
            if (errorMessage != null && !errorMessage.isEmpty()) {
                registrationStatus.set("Error: " + errorMessage);
            } else {
                registrationStatus.set("Error: Registration failed. Please try again.");
            }
            e.printStackTrace();
        }
    }
}
