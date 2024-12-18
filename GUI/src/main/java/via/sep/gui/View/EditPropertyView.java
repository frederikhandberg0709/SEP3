package via.sep.gui.View;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import via.sep.gui.Model.ImageService;
import via.sep.gui.Model.SceneManager;
import via.sep.gui.ViewModel.EditPropertyViewModel;
import via.sep.gui.ViewModel.ImageUploadViewModel;

public class EditPropertyView {
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
    @FXML private Button uploadImagesButton;
    @FXML private Button applyChangesButton;
    @FXML private Button cancelButton;
    @FXML private Button resetButton;
    @FXML private Label errorLabel;

    private EditPropertyViewModel viewModel;
    private ImageService imageService;
    private ImageUploadViewModel imageUploadViewModel;

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

        propertyTypeField.valueProperty().addListener((obs, oldVal, newVal) -> updatePropertyTypeFields(newVal));

        applyChangesButton.setOnAction(event -> saveChanges());
    }

    private void updatePropertyTypeFields(String propertyType) {
        if ("House".equals(propertyType)) {
            houseFields.setVisible(true);
            houseFields.setManaged(true);
            apartmentFields.setVisible(false);
            apartmentFields.setManaged(false);

            floorNumberField.clear();
            buildingNameField.clear();
            hasElevatorField.setSelected(false);
            hasBalconyField.setSelected(false);
        } else if ("Apartment".equals(propertyType)) {
            houseFields.setVisible(false);
            houseFields.setManaged(false);
            apartmentFields.setVisible(true);
            apartmentFields.setManaged(true);

            lotSizeField.clear();
            hasGarageField.setSelected(false);
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

    @FXML
    private void showAlert(Alert.AlertType type, String title, String message) {
        viewModel.errorMessageProperty().set(message);

        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setViewModel(EditPropertyViewModel viewModel) {
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

    public void setImageService(ImageService imageService) {
        this.imageService = imageService;
    }

    public void setImageUploadViewModel(ImageUploadViewModel imageUploadViewModel) {
        this.imageUploadViewModel = imageUploadViewModel;
    }

    @FXML
    private void handleShowImageUpload() {
        if (imageUploadViewModel == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Image upload service not initialized.");
            alert.showAndWait();
            return;
        }

        SceneManager.showImageUploadForProperty(imageUploadViewModel);
    }

    @FXML
    private void applyChangesButton() {
        applyChangesButton.setDisable(true);
        try {
            boolean success = viewModel.saveChanges();
            if (success) {
                Stage currentStage = (Stage) applyChangesButton.getScene().getWindow();
                currentStage.close();
                SceneManager.showDashboard();
            }
        } finally {
            applyChangesButton.setDisable(false);
        }
    }

    @FXML
    private void cancelButton() {
        viewModel.errorMessageProperty().set("");
        Stage currentStage = (Stage) cancelButton.getScene().getWindow();
        currentStage.close();

        SceneManager.showDashboard();
    }

    @FXML
    private void resetValues() {
        viewModel.resetToOriginal();
    }
}
