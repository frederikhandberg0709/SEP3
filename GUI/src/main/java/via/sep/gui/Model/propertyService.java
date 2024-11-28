package via.sep.gui.Model;
import via.sep.gui.Model.Property;




public class propertyService {
    public Property createProperty(String address, String propertyType, int bathroomNum, int roomsNum, String fullName, int floorNum, String status, double size, double price) {
        return new Property(address, propertyType, bathroomNum, roomsNum, fullName, floorNum, status, size, price);
    }

}