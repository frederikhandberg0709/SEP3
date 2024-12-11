package via.sep.gui.ViewModel;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import via.sep.gui.Model.PropertyService;
import via.sep.gui.Model.domain.Property;
import via.sep.gui.Model.dto.PropertyDTO;

import java.math.BigDecimal;

public class EditPropertyViewModel {
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
    private Long propertyId;

    public EditPropertyViewModel(PropertyService propertyService) {
        this.propertyService = propertyService;
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
        this.propertyId = property.getPropertyId();
        address.set(property.getAddress());
        propertyType.set(property.getPropertyType());
        numBathrooms.set(String.valueOf(property.getNumBathrooms()));
        numBedrooms.set(String.valueOf(property.getNumBedrooms()));
        numFloors.set(String.valueOf(property.getNumFloors()));
        floorArea.set(property.getFloorArea().toString());
        price.set(property.getPrice().toString());
        yearBuilt.set(String.valueOf(property.getYearBuilt()));
        description.set(property.getDescription());

        if (property.isHouse()) {
            lotSize.set(property.getLotSize() != null ? property.getLotSize().toString() : "");
            hasGarage.set(property.getHasGarage() != null && property.getHasGarage());
        } else if (property.isApartment()) {
            floorNumber.set(property.getFloorNumber() != null ? property.getFloorNumber().toString() : "");
            buildingName.set(property.getBuildingName());
            hasElevator.set(property.getHasElevator() != null && property.getHasElevator());
            hasBalcony.set(property.getHasBalcony() != null && property.getHasBalcony());
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
            } else if ("Apartment".equals(propertyType.get())) {
                propertyDTO.setFloorNumber(Integer.parseInt(floorNumber.get()));
                propertyDTO.setBuildingName(buildingName.get());
                propertyDTO.setHasElevator(hasElevator.get());
                propertyDTO.setHasBalcony(hasBalcony.get());
            }

            propertyService.updateProperty(propertyId, propertyDTO);
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
        return true;
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
