package via.sep.gui.Model;

import com.google.gson.Gson;
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
                                   String description) throws Exception {
        PropertyDTO propertyDTO = new PropertyDTO();
        ;
        propertyDTO.setAddress(address);
        propertyDTO.setPropertyType(propertyType);
        propertyDTO.setNumBathrooms(numBathrooms);
        propertyDTO.setNumBedrooms(numBedrooms);
        propertyDTO.setNumFloors(numFloors);
        propertyDTO.setFloorArea(floorArea);
        propertyDTO.setPrice(price);
        propertyDTO.setYearBuilt(yearBuilt);
        propertyDTO.setDescription(description);

        String json = gson.toJson(propertyDTO);
        String response = serverConnection.sendPostRequest(BASE_PATH, json);
        return gson.fromJson(response, Property.class);
    }

    public Property getPropertyById(Long id) throws Exception {
        String response = serverConnection.sendGetRequest(BASE_PATH + "/" + id);
        return gson.fromJson(response, Property.class);
    }

    public List<Property> getAllProperties() throws Exception {
        String response = serverConnection.sendGetRequest(BASE_PATH);
        Type listType = new TypeToken<List<Property>>(){}.getType();
        return gson.fromJson(response, listType);
    }

    public Property updateProperty(Long propertyId, PropertyDTO updatedProperty) throws Exception {
        String json = gson.toJson(updatedProperty);
        String response = serverConnection.sendPutRequest(BASE_PATH + "/" + propertyId, json);
        return gson.fromJson(response, Property.class);
    }

    public void deleteProperty(Long id) throws Exception {
        serverConnection.sendDeleteRequest(BASE_PATH + "/" + id);
    }
}