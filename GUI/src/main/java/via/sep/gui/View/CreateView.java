package via.sep.gui.View;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import via.sep.gui.ViewModel.CreateViewModel;

import java.util.function.Consumer;

public class CreateView {

    @FXML private TextField addressField;
    @FXML private TextField propertyTypeField;
    @FXML private TextField bathroomNumField;
    @FXML private TextField roomsNumField;
    @FXML private TextField fullNameField;
    @FXML private TextField floorNumField;
    @FXML private TextField statusField;
    @FXML private TextField sizeField;
    @FXML private TextField priceField;
    @FXML private Button createButton;
    @FXML private Button cancelButton;

    private Consumer<PropertyData> onSaveCallback;
    private Runnable onCancelCallback;

    @FXML
    private void initialize() {
        setupNumericValidation(bathroomNumField);
        setupNumericValidation(roomsNumField);
        setupNumericValidation(floorNumField);
        setupNumericValidation(sizeField);
        setupNumericValidation(priceField);

        createButton.setOnAction(event -> createProperty());
        cancelButton.setOnAction(event -> cancelCreation());
    }

    public void setCallbacks(Consumer<PropertyData> onSave, Runnable onCancel) {
        this.onSaveCallback = onSave;
        this.onCancelCallback = onCancel;
    }

    private void setupNumericValidation(TextField field) {
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                field.setText(oldValue);
            }
        });
    }

    private void createProperty() {
        try {
            PropertyData data = validateAndCollectData();
            if (data != null && onSaveCallback != null) {
                onSaveCallback.accept(data);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Property created successfully.");
            }
        } catch (ValidationException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", e.getMessage());
        }
    }

    private PropertyData validateAndCollectData() throws ValidationException {
        String address = getValidatedField(addressField, "Address");
        String propertyType = getValidatedField(propertyTypeField, "Property Type");
        int bathroomNum = getValidatedIntField(bathroomNumField, "Number of Bathrooms");
        int roomsNum = getValidatedIntField(roomsNumField, "Number of Rooms");
        String fullName = getValidatedField(fullNameField, "Full Name");
        int floorNum = getValidatedIntField(floorNumField, "Floor Number");
        String status = getValidatedField(statusField, "Status");
        double size = getValidatedDoubleField(sizeField, "Size");
        double price = getValidatedDoubleField(priceField, "Price");

        return new PropertyData(address, propertyType, bathroomNum, roomsNum,
                              fullName, floorNum, status, size, price);
    }

    private String getValidatedField(TextField field, String fieldName) throws ValidationException {
        String value = field.getText().trim();
        if (value.isEmpty()) {
            throw new ValidationException(fieldName + " cannot be empty");
        }
        return value;
    }

    private int getValidatedIntField(TextField field, String fieldName) throws ValidationException {
        try {
            return Integer.parseInt(getValidatedField(field, fieldName));
        } catch (NumberFormatException e) {
            throw new ValidationException(fieldName + " must be a valid number");
        }
    }

    private double getValidatedDoubleField(TextField field, String fieldName) throws ValidationException {
        try {
            return Double.parseDouble(getValidatedField(field, fieldName));
        } catch (NumberFormatException e) {
            throw new ValidationException(fieldName + " must be a valid decimal number");
        }
    }

    private void cancelCreation() {
        clearAllFields();
        if (onCancelCallback != null) {
            onCancelCallback.run();
        }
    }

    private void clearAllFields() {
        addressField.clear();
        propertyTypeField.clear();
        bathroomNumField.clear();
        roomsNumField.clear();
        fullNameField.clear();
        floorNumField.clear();
        statusField.clear();
        sizeField.clear();
        priceField.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Helper classes
    public static class PropertyData {
        private final String address;
        private final String propertyType;
        private final int bathroomNum;
        private final int roomsNum;
        private final String fullName;
        private final int floorNum;
        private final String status;
        private final double size;
        private final double price;

        public PropertyData(String address, String propertyType, int bathroomNum,
                          int roomsNum, String fullName, int floorNum,
                          String status, double size, double price) {
            this.address = address;
            this.propertyType = propertyType;
            this.bathroomNum = bathroomNum;
            this.roomsNum = roomsNum;
            this.fullName = fullName;
            this.floorNum = floorNum;
            this.status = status;
            this.size = size;
            this.price = price;
        }

        // Add getters for all fields
        public String getAddress() { return address; }
        public String getPropertyType() { return propertyType; }
        public int getBathroomNum() { return bathroomNum; }
        public int getRoomsNum() { return roomsNum; }
        public String getFullName() { return fullName; }
        public int getFloorNum() { return floorNum; }
        public String getStatus() { return status; }
        public double getSize() { return size; }
        public double getPrice() { return price; }
    }

    private static class ValidationException extends Exception {
        public ValidationException(String message) {
            super(message);
        }
    }


    public void setViewModel(CreateViewModel viewModel) {

    }
}