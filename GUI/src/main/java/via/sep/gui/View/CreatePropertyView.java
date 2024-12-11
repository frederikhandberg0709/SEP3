package via.sep.gui.View;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import via.sep.gui.Model.SceneManager;
import via.sep.gui.ViewModel.CreatePropertyViewModel;

public class CreatePropertyView {
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

    @FXML private Button createButton;
    @FXML private Button cancelButton;
    @FXML private Button resetButton;
    @FXML private Label errorLabel;

    private CreatePropertyViewModel viewModel;

    @FXML
    private void initialize() {
        setupNumericValidation(numBathroomsField);
        setupNumericValidation(numBedroomsField);
        setupNumericValidation(numFloorsField);
        setupNumericValidation(yearBuiltField);
        setupDecimalValidation(floorAreaField);
        setupDecimalValidation(priceField);
        setupDecimalValidation(lotSizeField);

        propertyTypeField.getItems().addAll("House", "Apartment");
        propertyTypeField.valueProperty().addListener((obs, oldVal, newVal) -> updatePropertyTypeFields(newVal));

        createButton.setOnAction(event -> handleCreateProperty());
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

    public void setViewModel(CreatePropertyViewModel viewModel) {
        this.viewModel = viewModel;

        // Bind all fields
        addressField.textProperty().bindBidirectional(viewModel.addressProperty());
        propertyTypeField.valueProperty().bindBidirectional(viewModel.propertyTypeProperty());
        numBathroomsField.textProperty().bindBidirectional(viewModel.numBathroomsProperty());
        numBedroomsField.textProperty().bindBidirectional(viewModel.numBedroomsProperty());
        numFloorsField.textProperty().bindBidirectional(viewModel.numFloorsProperty());
        floorAreaField.textProperty().bindBidirectional(viewModel.floorAreaProperty());
        priceField.textProperty().bindBidirectional(viewModel.priceProperty());
        yearBuiltField.textProperty().bindBidirectional(viewModel.yearBuiltProperty());
        descriptionField.textProperty().bindBidirectional(viewModel.descriptionProperty());

        // House specific bindings
        lotSizeField.textProperty().bindBidirectional(viewModel.lotSizeProperty());
        hasGarageField.selectedProperty().bindBidirectional(viewModel.hasGarageProperty());

        // Apartment specific bindings
        floorNumberField.textProperty().bindBidirectional(viewModel.floorNumberProperty());
        buildingNameField.textProperty().bindBidirectional(viewModel.buildingNameProperty());
        hasElevatorField.selectedProperty().bindBidirectional(viewModel.hasElevatorProperty());
        hasBalconyField.selectedProperty().bindBidirectional(viewModel.hasBalconyProperty());

        // Error message binding
        errorLabel.textProperty().bind(viewModel.errorMessageProperty());
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

    @FXML
    private void handleCreateProperty() {
        createButton.setDisable(true);

        try {
            boolean success = viewModel.createProperty();
            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Property created successfully!");
                alert.showAndWait();

                Stage currentStage = (Stage) createButton.getScene().getWindow();
                currentStage.close();

                SceneManager.showDashboard();
            }
        } catch (Exception e) {
            viewModel.errorMessageProperty().set("Error creating property: " + e.getMessage());
        } finally {
            createButton.setDisable(false);
        }
    }

    @FXML
    private void resetFields() {
        viewModel.clearFields();
        propertyTypeField.setValue(null);
        houseFields.setVisible(false);
        apartmentFields.setVisible(false);
        viewModel.errorMessageProperty().set("");
    }

    @FXML
    private void handleCancel() {
        viewModel.errorMessageProperty().set("");

        Stage currentStage = (Stage) cancelButton.getScene().getWindow();
        currentStage.close();

        SceneManager.showDashboard();
    }
}
