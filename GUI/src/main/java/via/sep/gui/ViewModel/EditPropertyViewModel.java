package via.sep.gui.ViewModel;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import via.sep.gui.Model.ImageService;
import via.sep.gui.Model.PropertyService;
import via.sep.gui.Model.domain.Property;
import via.sep.gui.Model.dto.PropertyDTO;

import java.io.Console;
import java.io.File;
import java.math.BigDecimal;
import java.util.List;

public class EditPropertyViewModel {
    private PropertyDTO originalPropertyDTO;

    private final StringProperty address = new SimpleStringProperty();
    private final StringProperty propertyType = new SimpleStringProperty();
    private final StringProperty numBathrooms = new SimpleStringProperty();
    private final StringProperty numBedrooms = new SimpleStringProperty();
    private final StringProperty numFloors = new SimpleStringProperty();
    private final StringProperty floorArea = new SimpleStringProperty();
    private final StringProperty price = new SimpleStringProperty();
    private final StringProperty yearBuilt = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();

    // House specific
    private final StringProperty lotSize = new SimpleStringProperty();
    private final BooleanProperty hasGarage = new SimpleBooleanProperty();

    // Apartment specific
    private final StringProperty floorNumber = new SimpleStringProperty();
    private final StringProperty buildingName = new SimpleStringProperty();
    private final BooleanProperty hasElevator = new SimpleBooleanProperty();
    private final BooleanProperty hasBalcony = new SimpleBooleanProperty();

    private final StringProperty errorMessage = new SimpleStringProperty();
    private final PropertyService propertyService;
    private final ImageService imageService;
    private Long propertyId;

    private final ImageUploadViewModel imageUploadViewModel;

    public EditPropertyViewModel(PropertyService propertyService, ImageService imageService) {
        this.propertyService = propertyService;
        this.imageService = imageService;
        this.imageUploadViewModel = new ImageUploadViewModel(imageService);
    }

    public ImageUploadViewModel getImageUploadViewModel() {
        return imageUploadViewModel;
    }

    public StringProperty addressProperty() { return address; }
    public StringProperty propertyTypeProperty() { return propertyType; }
    public StringProperty numBathroomsProperty() { return numBathrooms; }
    public StringProperty numBedroomsProperty() { return numBedrooms; }
    public StringProperty numFloorsProperty() { return numFloors; }
    public StringProperty floorAreaProperty() { return floorArea; }
    public StringProperty priceProperty() { return price; }
    public StringProperty yearBuiltProperty() { return yearBuilt; }
    public StringProperty descriptionProperty() { return description; }
    public StringProperty lotSizeProperty() { return lotSize; }
    public BooleanProperty hasGarageProperty() { return hasGarage; }
    public StringProperty floorNumberProperty() { return floorNumber; }
    public StringProperty buildingNameProperty() { return buildingName; }
    public BooleanProperty hasElevatorProperty() { return hasElevator; }
    public BooleanProperty hasBalconyProperty() { return hasBalcony; }
    public StringProperty errorMessageProperty() { return errorMessage; }

    public void loadProperty(Property property) {
        try {
            PropertyDTO propertyDTO = propertyService.getPropertyById(property.getPropertyId());

            this.originalPropertyDTO = propertyDTO;
            this.propertyId = propertyDTO.getPropertyId();

            loadFieldsFromDTO(propertyDTO);

            imageUploadViewModel.loadImagesForProperty(propertyId);
        } catch (Exception e) {
            errorMessage.set("Failed to load property: " + e.getMessage());
        }
    }

    private void loadFieldsFromDTO(PropertyDTO dto) {
        address.set(dto.getAddress());
        propertyType.set(dto.getPropertyType());
        numBathrooms.set(String.valueOf(dto.getNumBathrooms()));
        numBedrooms.set(String.valueOf(dto.getNumBedrooms()));
        floorArea.set(dto.getFloorArea().toString());
        price.set(dto.getPrice().toString());
        yearBuilt.set(String.valueOf(dto.getYearBuilt()));
        description.set(dto.getDescription());

        if ("House".equals(dto.getPropertyType())) {
            numFloors.set(String.valueOf(dto.getNumFloors()));
            lotSize.set(dto.getLotSize() != null ? dto.getLotSize().toString() : "");
            hasGarage.set(dto.getHasGarage() != null && dto.getHasGarage());
        } else if ("Apartment".equals(dto.getPropertyType())) {
            numFloors.set(String.valueOf(dto.getNumFloors()));
            floorNumber.set(dto.getFloorNumber() != null ? dto.getFloorNumber().toString() : "");
            buildingName.set(dto.getBuildingName());
            hasElevator.set(dto.getHasElevator() != null && dto.getHasElevator());
            hasBalcony.set(dto.getHasBalcony() != null && dto.getHasBalcony());
        }
    }

    public boolean saveChanges() {
        try {
            if (!validateFields()) {
                return false;
            }

            PropertyDTO propertyDTO = new PropertyDTO();
            propertyDTO.setPropertyId(propertyId);
            propertyDTO.setAddress(address.get());
            propertyDTO.setPropertyType(propertyType.get());
            propertyDTO.setNumBathrooms(Integer.parseInt(numBathrooms.get()));
            propertyDTO.setNumBedrooms(Integer.parseInt(numBedrooms.get()));
            propertyDTO.setNumFloors(Integer.parseInt(numFloors.get()));
            propertyDTO.setFloorArea(new BigDecimal(floorArea.get()));
            propertyDTO.setPrice(new BigDecimal(price.get()));
            propertyDTO.setYearBuilt(Integer.parseInt(yearBuilt.get()));
            propertyDTO.setDescription(description.get());

            if ("House".equals(propertyType.get())) {
                propertyDTO.setLotSize(new BigDecimal(lotSize.get()));
                propertyDTO.setHasGarage(hasGarage.get());
                propertyDTO.setNumFloors(Integer.parseInt(numFloors.get()));

                propertyDTO.setFloorNumber(null);
                propertyDTO.setBuildingName(null);
                propertyDTO.setHasElevator(null);
                propertyDTO.setHasBalcony(null);
            } else if ("Apartment".equals(propertyType.get())) {
                propertyDTO.setFloorNumber(Integer.parseInt(floorNumber.get()));
                propertyDTO.setBuildingName(buildingName.get());
                propertyDTO.setHasElevator(hasElevator.get());
                propertyDTO.setHasBalcony(hasBalcony.get());
                propertyDTO.setNumFloors(Integer.parseInt(numFloors.get()));

                propertyDTO.setLotSize(null);
                propertyDTO.setHasGarage(null);
            }

            System.out.println("Number of floors: " + propertyDTO.getNumFloors());
            System.out.println("Floor number: " + propertyDTO.getFloorNumber());
            System.out.println("Property type: " + propertyType.get());

            propertyService.updateProperty(propertyId, propertyDTO);

            List<Long> imagesToDelete = imageUploadViewModel.getImagesToDelete();
            for (Long imageId : imagesToDelete) {
                try {
                    imageService.deleteImage(imageId);
                } catch (Exception e) {
                    System.err.println("Failed to delete image: " + imageId);
                }
            }

            List<File> selectedFiles = imageUploadViewModel.getSelectedFiles();
            if (!selectedFiles.isEmpty()) {
                for (File imageFile : selectedFiles) {
                    try {
                        imageService.uploadImage(propertyId, imageFile);
                    } catch (Exception e) {
                        System.err.println("Failed to upload image: " + imageFile.getName());
                    }
                }
            }

            imageUploadViewModel.clearSelectedFiles();

            errorMessage.set("Property updated successfully");
            return true;

        } catch (NumberFormatException e) {
            errorMessage.set("Please enter valid numbers for numeric fields");
            return false;
        } catch (Exception e) {
            errorMessage.set("Error updating property: " + e.getMessage());
            return false;
        }
    }

    private boolean validateFields() {
        if (isNullOrEmpty(address.get()) ||
                isNullOrEmpty(propertyType.get()) ||
                isNullOrEmpty(numBathrooms.get()) ||
                isNullOrEmpty(numBedrooms.get()) ||
                isNullOrEmpty(numFloors.get()) ||
                isNullOrEmpty(floorArea.get()) ||
                isNullOrEmpty(price.get()) ||
                isNullOrEmpty(yearBuilt.get())) {

            errorMessage.set("Please fill in all required fields");
            return false;
        }

        if ("House".equals(propertyType.get())) {
            if (isNullOrEmpty(lotSize.get())) {
                errorMessage.set("Please fill in lot size for house");
                return false;
            }
        } else if ("Apartment".equals(propertyType.get())) {
            if (isNullOrEmpty(floorNumber.get()) || isNullOrEmpty(buildingName.get())) {
                errorMessage.set("Please fill in all apartment details");
                return false;
            }
        }

        return true;
    }

    public void resetToOriginal() {
        if (originalPropertyDTO != null) {
            loadFieldsFromDTO(originalPropertyDTO);
            errorMessage.set("Values reset to original");
        }
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    public void clearFields() {
        propertyId = null;
        address.set("");
        propertyType.set("");
        numBathrooms.set("");
        numBedrooms.set("");
        numFloors.set("");
        floorArea.set("");
        price.set("");
        yearBuilt.set("");
        description.set("");
        lotSize.set("");
        hasGarage.set(false);
        floorNumber.set("");
        buildingName.set("");
        hasElevator.set(false);
        hasBalcony.set(false);
        errorMessage.set("");
    }
}
