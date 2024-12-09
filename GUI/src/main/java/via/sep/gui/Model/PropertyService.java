package via.sep.gui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PropertyService {
    private final List<Property> properties = new ArrayList<>();

    public Property createProperty(String address, String propertyType, int bathroomNum, int roomsNum, String fullName, int floorNum, String status, double size, double price) {
        Property property = new Property(address, propertyType, bathroomNum, roomsNum, fullName, floorNum, status, size, price);
        properties.add(property);
        return property;
    }

    public Optional<Property> getPropertyById(Long id) {
        return properties.stream().filter(property -> property.getId().equals(id)).findFirst();
    }

    public List<Property> getAllProperties() {
        return new ArrayList<>(properties);
    }

    public boolean updateProperty(Long id, String address, String propertyType, int bathroomNum, int roomsNum, String fullName, int floorNum, String status, double size, double price) {
        Optional<Property> optionalProperty = getPropertyById(id);
        if (optionalProperty.isPresent()) {
            Property property = optionalProperty.get();
            property.setAddress(address);
            property.setPropertyType(propertyType);
            property.setBathroomNum(bathroomNum);
            property.setRoomsNum(roomsNum);
            property.setFullName(fullName);
            property.setFloorNum(floorNum);
            property.setStatus(status);
            property.setSize(size);
            property.setPrice(price);
            return true;
        }
        return false;
    }

    public boolean deleteProperty(Long id) {
        return properties.removeIf(property -> property.getId().equals(id));
    }
}