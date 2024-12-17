package via.sep.gui.ViewModel;

import javafx.beans.property.*;
import via.sep.gui.Model.ImageService;
import via.sep.gui.Model.PropertyService;
import via.sep.gui.Model.domain.Property;

import java.math.BigDecimal;

public class CreatePropertyViewModel {
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
    private final BooleanProperty hasGarage = new SimpleBooleanProperty(false);

    // Apartment specific
    private final StringProperty floorNumber = new SimpleStringProperty();
    private final StringProperty buildingName = new SimpleStringProperty();
    private final BooleanProperty hasElevator = new SimpleBooleanProperty(false);
    private final BooleanProperty hasBalcony = new SimpleBooleanProperty(false);

    private final StringProperty errorMessage = new SimpleStringProperty();
    private final PropertyService propertyService;
    private final ImageUploadViewModel imageUploadViewModel;
    private final ObjectProperty<Long> createdPropertyId = new SimpleObjectProperty<>(0L);

    public CreatePropertyViewModel(PropertyService propertyService, ImageService imageService) {
        this.propertyService = propertyService;
        this.imageUploadViewModel = new ImageUploadViewModel(imageService);

        propertyType.set("Apartment");
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

    public boolean createProperty() {
        try {
            if (!validateRequiredFields()) {
                System.out.println("Validation failed");
                return false;
            }

            Integer bathrooms = parseInteger(numBathrooms.get(), "Number of bathrooms");
            Integer bedrooms = parseInteger(numBedrooms.get(), "Number of bedrooms");
            Integer floors = parseInteger(numFloors.get(), "Number of floors");
            BigDecimal area = parseBigDecimal(floorArea.get(), "Floor area");
            BigDecimal propertyPrice = parseBigDecimal(price.get(), "Price");
            Integer year = parseInteger(yearBuilt.get(), "Year built");

            Property createdProperty = propertyService.createProperty(
                    address.get(),
                    propertyType.get(),
                    bathrooms,
                    bedrooms,
                    floors,
                    area,
                    propertyPrice,
                    year,
                    description.get()
            );

            if (createdProperty != null && createdProperty.getPropertyId() != null) {
                clearFields();
                errorMessage.set("Property created successfully!");
                createdPropertyId.set(createdProperty.getPropertyId());
                return true;
            } else {
                errorMessage.set("Failed to create property");
                return false;
            }

        } catch (NumberFormatException e) {
            System.out.println("Number format error: " + e.getMessage());
            errorMessage.set("Please enter valid numbers for numeric fields");
            return false;
        } catch (Exception e) {
            System.out.println("Error creating property: " + e.getMessage());
            errorMessage.set("Error creating property: " + e.getMessage());
            return false;
        }
    }

    public ObjectProperty<Long> createdPropertyIdProperty() {
        return createdPropertyId;
    }

    public Long getCreatedPropertyId() {
        return createdPropertyId.get();
    }

    public ImageUploadViewModel getImageUploadViewModel() {
        return imageUploadViewModel;
    }

    private boolean validateRequiredFields() {
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
        return true;
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    private Integer parseInteger(String value, String fieldName) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Invalid " + fieldName);
        }
    }

    private BigDecimal parseBigDecimal(String value, String fieldName) {
        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Invalid " + fieldName);
        }
    }

    public void clearFields() {
        address.set("");
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
        createdPropertyId.set(null);
    }
}
