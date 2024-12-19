package via.sep.gui.ViewModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import via.sep.gui.Model.PropertyService;
import via.sep.gui.Model.domain.Property;
import via.sep.gui.Model.dto.PropertyDTO;

import java.util.List;
import java.util.stream.Collectors;

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
        try {
            SceneManager.showCreateProperty();
            statusMessage.set("");
        } catch (Exception e) {
            errorMessage.set("Error opening create property form: " + e.getMessage());
        }
    }

    public void showEditProperty(Property property) {
        SceneManager.showEditProperty(property);
    }

    public void showBookingList() {
        SceneManager.showBookingList();
    }

    public void showAgentList() {
        SceneManager.showAgentList();
    }

    public void deleteProperty(Property property) {
        removeProperty(property);
    }


    public void loadProperties() {
        try {
            List<PropertyDTO> dtos = propertyService.getAllProperties();
            List<Property> properties = dtos.stream()
                    .map(this::convertDTOToProperty)
                    .collect(Collectors.toList());

            propertyList.clear();
            propertyList.addAll(properties);
            errorMessage.set("");
        } catch (Exception e) {
            e.printStackTrace();
            errorMessage.set("Failed to load properties: " + e.getMessage());
        }
    }

    private Property convertDTOToProperty(PropertyDTO dto) {
        Property property = new Property();
        property.setPropertyId(dto.getPropertyId());
        property.setAddress(dto.getAddress());
        property.setPropertyType(dto.getPropertyType());
        property.setNumBathrooms(dto.getNumBathrooms());
        property.setNumBedrooms(dto.getNumBedrooms());
        property.setFloorArea(dto.getFloorArea());
        property.setPrice(dto.getPrice());
        property.setYearBuilt(dto.getYearBuilt());
        property.setDescription(dto.getDescription());

        return property;
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
                    propertyDTO.getDescription(),
                    // House-specific fields
                    propertyDTO.getLotSize(),
                    propertyDTO.getHasGarage(),
                    // Apartment-specific fields
                    propertyDTO.getFloorNumber(),
                    propertyDTO.getBuildingName(),
                    propertyDTO.getHasElevator(),
                    propertyDTO.getHasBalcony()
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
        dto.setFloorArea(property.getFloorArea());
        dto.setPrice(property.getPrice());
        dto.setYearBuilt(property.getYearBuilt());
        dto.setDescription(property.getDescription());

        return dto;
    }

    public void refreshProperties() {
        loadProperties();
    }
}
