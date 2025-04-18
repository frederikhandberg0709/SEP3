package via.sep.gui.Model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import via.sep.gui.Model.domain.Property;
import via.sep.gui.Model.dto.PropertyDTO;
import via.sep.gui.Server.ServerConnection;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.List;

public class PropertyService {
    private final ServerConnection serverConnection;
    private final Gson gson;
    private static final String BASE_PATH = "/properties";

    public PropertyService(ServerConnection serverConnection, Gson gson) {
        this.serverConnection = serverConnection;
        this.gson = gson;
    }

    public Property createProperty(String address, String propertyType,
            Integer numBathrooms, Integer numBedrooms,
            Integer numFloors, BigDecimal floorArea,
            BigDecimal price, Integer yearBuilt,
            String description,
            // House-specific fields
            BigDecimal lotSize,
            Boolean hasGarage,
            // Apartment-specific fields
            Integer floorNumber,
            String buildingName,
            Boolean hasElevator,
            Boolean hasBalcony) throws Exception {
        PropertyDTO propertyDTO = new PropertyDTO();

        propertyDTO.setAddress(address);
        propertyDTO.setPropertyType(propertyType);
        propertyDTO.setNumBathrooms(numBathrooms);
        propertyDTO.setNumBedrooms(numBedrooms);
        propertyDTO.setNumFloors(numFloors);
        propertyDTO.setFloorArea(floorArea);
        propertyDTO.setPrice(price);
        propertyDTO.setYearBuilt(yearBuilt);
        propertyDTO.setDescription(description);

        if ("House".equals(propertyType)) {
            propertyDTO.setLotSize(lotSize);
            propertyDTO.setHasGarage(hasGarage);
        } else if ("Apartment".equals(propertyType)) {
            propertyDTO.setFloorNumber(floorNumber);
            propertyDTO.setBuildingName(buildingName);
            propertyDTO.setHasElevator(hasElevator);
            propertyDTO.setHasBalcony(hasBalcony);
        }

        String json = gson.toJson(propertyDTO);
        String response = serverConnection.sendPostRequest(BASE_PATH, json);
        return gson.fromJson(response, Property.class);
    }

    public PropertyDTO getPropertyById(Long id) throws Exception {
        String response = serverConnection.sendGetRequest(BASE_PATH + "/" + id);
        return gson.fromJson(response, PropertyDTO.class);
    }

    public List<PropertyDTO> getAllProperties() throws Exception {
        String response = serverConnection.sendGetRequest(BASE_PATH);
        Type listType = new TypeToken<List<PropertyDTO>>(){}.getType();
        return gson.fromJson(response, listType);
    }

    public Property updateProperty(Long propertyId, PropertyDTO updatedProperty) throws Exception {
        PropertyDTO existingProperty = getPropertyById(propertyId);

        String propertyType = existingProperty.getPropertyType();
        updatedProperty.setPropertyType(propertyType);

        if ("House".equals(propertyType)) {
            if (updatedProperty.getLotSize() == null) {
                updatedProperty.setLotSize(existingProperty.getLotSize());
            }
            if (updatedProperty.getHasGarage() == null) {
                updatedProperty.setHasGarage(existingProperty.getHasGarage());
            }
            if (updatedProperty.getNumFloors() == null) {
                updatedProperty.setNumFloors(existingProperty.getNumFloors());
            }
        } else if ("Apartment".equals(propertyType)) {
            if (updatedProperty.getFloorNumber() == null) {
                updatedProperty.setFloorNumber(existingProperty.getFloorNumber());
            }
            if (updatedProperty.getBuildingName() == null) {
                updatedProperty.setBuildingName(existingProperty.getBuildingName());
            }
            if (updatedProperty.getHasElevator() == null) {
                updatedProperty.setHasElevator(existingProperty.getHasElevator());
            }
            if (updatedProperty.getHasBalcony() == null) {
                updatedProperty.setHasBalcony(existingProperty.getHasBalcony());
            }
            if (updatedProperty.getNumFloors() == null) {
                updatedProperty.setNumFloors(existingProperty.getNumFloors());
            }
        }

        String json = gson.toJson(updatedProperty);

        try {
            String response = serverConnection.sendPutRequest(BASE_PATH + "/" + propertyId, json);

            Gson customGson = new GsonBuilder()
                    .setLenient()
                    .create();

            return customGson.fromJson(response, Property.class);
        } catch (JsonSyntaxException e) {
            PropertyDTO updatedDTO = getPropertyById(propertyId);
            return gson.fromJson(gson.toJson(updatedDTO), Property.class);
        } catch (Exception e) {
            throw new Exception("Failed to update property: " + e.getMessage());
        }
    }

    public void deleteProperty(Long id) throws Exception {
        serverConnection.sendDeleteRequest(BASE_PATH + "/" + id);
    }
}