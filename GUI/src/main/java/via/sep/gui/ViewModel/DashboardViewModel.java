package via.sep.gui.ViewModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import via.sep.gui.Model.PropertyService;
import via.sep.gui.Model.SceneManager;
import via.sep.gui.Model.domain.Property;
import via.sep.gui.Model.dto.PropertyDTO;

public class DashboardViewModel {
    private final ObservableList<Property> propertyList = FXCollections.observableArrayList();
    private final StringProperty errorMessage = new SimpleStringProperty("");
    private final StringProperty statusMessage = new SimpleStringProperty("");
    private final PropertyService propertyService;

    public DashboardViewModel(PropertyService propertyService) {
        this.propertyService = propertyService;
        loadProperties();
    }

    public ObservableList<Property> getPropertyList() {
        return propertyList;
    }

    public StringProperty errorMessageProperty() {
        return errorMessage;
    }

    public StringProperty statusMessageProperty() {
        return statusMessage;
    }

    public void showCreateProperty() {
        SceneManager.showCreateProperty();
    }

    public void showEditProperty(Property property) {
        SceneManager.showEditProperty(property);
    }

    public void showBookingList() {
        SceneManager.showBookingList();
    }

    public void deleteProperty(Property property) {
        removeProperty(property);
    }


    public void loadProperties() {
        try {
            var properties = propertyService.getAllProperties();
            propertyList.clear();
            propertyList.addAll(properties);
            errorMessage.set("");
        } catch (Exception e) {
            errorMessage.set("Failed to load properties: " + e.getMessage());
        }
    }

    public void addProperty(Property property) {
        try {
            PropertyDTO propertyDTO = convertToDTO(property);
            Property savedProperty = propertyService.createProperty(
                    propertyDTO.getAddress(),
                    propertyDTO.getPropertyType(),
                    propertyDTO.getNumBathrooms(),
                    propertyDTO.getNumBedrooms(),
                    propertyDTO.getNumFloors(),
                    propertyDTO.getFloorArea(),
                    propertyDTO.getPrice(),
                    propertyDTO.getYearBuilt(),
                    propertyDTO.getDescription()
            );
            propertyList.add(savedProperty);
            errorMessage.set("");
        } catch (Exception e) {
            errorMessage.set("Failed to add property: " + e.getMessage());
        }
    }

    public void updateProperty(Property property) {
        try {
            PropertyDTO propertyDTO = convertToDTO(property);
            Property updatedProperty = propertyService.updateProperty(property.getPropertyId(), propertyDTO);
            int index = findPropertyIndex(property.getPropertyId());
            if (index >= 0) {
                propertyList.set(index, updatedProperty);
            }
            errorMessage.set("");
        } catch (Exception e) {
            errorMessage.set("Failed to update property: " + e.getMessage());
        }
    }

    public void removeProperty(Property property) {
        try {
            propertyService.deleteProperty(property.getPropertyId());
            propertyList.remove(property);
            errorMessage.set("");
        } catch (Exception e) {
            errorMessage.set("Failed to remove property: " + e.getMessage());
        }
    }

    private int findPropertyIndex(Long propertyId) {
        for (int i = 0; i < propertyList.size(); i++) {
            if (propertyList.get(i).getPropertyId().equals(propertyId)) {
                return i;
            }
        }
        return -1;
    }

    private PropertyDTO convertToDTO(Property property) {
        PropertyDTO dto = new PropertyDTO();
        dto.setPropertyId(property.getPropertyId());
        dto.setAddress(property.getAddress());
        dto.setPropertyType(property.getPropertyType());
        dto.setNumBathrooms(property.getNumBathrooms());
        dto.setNumBedrooms(property.getNumBedrooms());
        dto.setNumFloors(property.getNumFloors());
        dto.setFloorArea(property.getFloorArea());
        dto.setPrice(property.getPrice());
        dto.setYearBuilt(property.getYearBuilt());
        dto.setDescription(property.getDescription());

        // Set type-specific properties
        if (property.isHouse()) {
            dto.setLotSize(property.getLotSize());
            dto.setHasGarage(property.getHasGarage());
        } else if (property.isApartment()) {
            dto.setFloorNumber(property.getFloorNumber());
            dto.setBuildingName(property.getBuildingName());
            dto.setHasElevator(property.getHasElevator());
            dto.setHasBalcony(property.getHasBalcony());
        }

        return dto;
    }

    public void refreshProperties() {
        loadProperties();
    }
}
