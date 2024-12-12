package via.sep.gui.View;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import via.sep.gui.Model.ImageService;
import via.sep.gui.Model.SceneManager;
import via.sep.gui.ViewModel.CreatePropertyViewModel;
import via.sep.gui.ViewModel.ImageUploadViewModel;

import java.io.File;
import java.util.List;

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

    @FXML private Button uploadImagesButton;
    @FXML private Button createButton;
    @FXML private Button cancelButton;
    @FXML private Button resetButton;
    @FXML private Label errorLabel;

    private CreatePropertyViewModel viewModel;
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

        uploadImagesButton.disableProperty().bind(
                viewModel.createdPropertyIdProperty().isNull()
        );
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
    private void handleCreateProperty() {
        createButton.setDisable(true);

        try {
            boolean success = viewModel.createProperty();
            if (success) {
                Long propertyId = viewModel.getCreatedPropertyId();
                if (imageUploadViewModel != null) {
                    List<File> selectedFiles = imageUploadViewModel.getSelectedFiles();
                    if (!selectedFiles.isEmpty()) {
                        for (File imageFile : selectedFiles) {
                            try {
                                imageService.uploadImage(propertyId, imageFile);
                            } catch (Exception e) {
                                System.err.println("Failed to upload image: " + imageFile.getName());
                                // Continue with other images even if one fails
                            }
                        }
                    }
                }

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
        if (imageUploadViewModel != null) {
            imageUploadViewModel.clearSelectedFiles();
        }
    }

    @FXML
    private void handleCancel() {
        viewModel.errorMessageProperty().set("");

        Stage currentStage = (Stage) cancelButton.getScene().getWindow();
        currentStage.close();

        SceneManager.showDashboard();
    }
}
