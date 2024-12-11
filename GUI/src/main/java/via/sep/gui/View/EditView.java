package via.sep.gui.View;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import via.sep.gui.ViewModel.EditViewModel;

import java.util.function.Consumer;

public class EditView {
    @FXML private TextField addressField;
    @FXML private ComboBox<String> propertyTypeField;
    @FXML private TextField numBathroomsField;
    @FXML private TextField numBedroomsField;
    @FXML private TextField numFloorsField;
    @FXML private TextField floorAreaField;
    @FXML private TextField priceField;
    @FXML private TextField yearBuiltField;
    @FXML private TextArea descriptionField;

    // House specific fields
    @FXML private VBox houseFields;
    @FXML private TextField lotSizeField;
    @FXML private CheckBox hasGarageField;

    // Apartment specific fields
    @FXML private VBox apartmentFields;
    @FXML private TextField floorNumberField;
    @FXML private TextField buildingNameField;
    @FXML private CheckBox hasElevatorField;
    @FXML private CheckBox hasBalconyField;

    // UI elements
    @FXML private Button applyChangesButton;
    @FXML private Button cancelButton;
    @FXML private Label errorLabel;

    private EditViewModel viewModel;

    @FXML
    private void initialize() {
        setupNumericValidation(numBathroomsField);
        setupNumericValidation(numBedroomsField);
        setupNumericValidation(numFloorsField);
        setupNumericValidation(yearBuiltField);
        setupDecimalValidation(floorAreaField);
        setupDecimalValidation(priceField);
        setupDecimalValidation(lotSizeField);
        setupNumericValidation(floorNumberField);

        propertyTypeField.getItems().addAll("House", "Apartment");
        propertyTypeField.valueProperty().addListener((obs, oldVal, newVal) -> updatePropertyTypeFields(newVal));

        applyChangesButton.setOnAction(event -> saveChanges());
        cancelButton.setOnAction(event -> viewModel.clearFields());
    }

    private void updatePropertyTypeFields(String propertyType) {
        if ("House".equals(propertyType)) {
            houseFields.setVisible(true);
            apartmentFields.setVisible(false);
        } else if ("Apartment".equals(propertyType)) {
            houseFields.setVisible(false);
            apartmentFields.setVisible(true);
        }
    }

    private void setupNumericValidation(TextField field) {
        field.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.matches("\\d*")) {
                field.setText(oldVal);
            }
        });
    }

    private void setupDecimalValidation(TextField field) {
        field.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.matches("\\d*\\.?\\d*")) {
                field.setText(oldVal);
            }
        });
    }

    private void saveChanges() {
        boolean success = viewModel.saveChanges();
        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Property updated successfully");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setViewModel(EditViewModel viewModel) {
        this.viewModel = viewModel;

        // Bind basic property fields
        addressField.textProperty().bindBidirectional(viewModel.addressProperty());
        propertyTypeField.valueProperty().bindBidirectional(viewModel.propertyTypeProperty());
        numBathroomsField.textProperty().bindBidirectional(viewModel.numBathroomsProperty());
        numBedroomsField.textProperty().bindBidirectional(viewModel.numBedroomsProperty());
        numFloorsField.textProperty().bindBidirectional(viewModel.numFloorsProperty());
        floorAreaField.textProperty().bindBidirectional(viewModel.floorAreaProperty());
        priceField.textProperty().bindBidirectional(viewModel.priceProperty());
        yearBuiltField.textProperty().bindBidirectional(viewModel.yearBuiltProperty());
        descriptionField.textProperty().bindBidirectional(viewModel.descriptionProperty());

        // Bind house specific fields
        lotSizeField.textProperty().bindBidirectional(viewModel.lotSizeProperty());
        hasGarageField.selectedProperty().bindBidirectional(viewModel.hasGarageProperty());

        // Bind apartment specific fields
        floorNumberField.textProperty().bindBidirectional(viewModel.floorNumberProperty());
        buildingNameField.textProperty().bindBidirectional(viewModel.buildingNameProperty());
        hasElevatorField.selectedProperty().bindBidirectional(viewModel.hasElevatorProperty());
        hasBalconyField.selectedProperty().bindBidirectional(viewModel.hasBalconyProperty());

        // Bind error message
        errorLabel.textProperty().bind(viewModel.errorMessageProperty());
    }
}
