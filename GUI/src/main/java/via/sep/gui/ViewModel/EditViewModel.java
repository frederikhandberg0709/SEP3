package via.sep.gui.ViewModel;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import via.sep.gui.Model.Property;

public class EditViewModel {
    private Property currentProperty;

    private final StringProperty address = new SimpleStringProperty();
    private final StringProperty propertyType = new SimpleStringProperty();
    private final StringProperty bathroomNum = new SimpleStringProperty();
    private final StringProperty roomsNum = new SimpleStringProperty();
    private final StringProperty fullName = new SimpleStringProperty();
    private final StringProperty floorNum = new SimpleStringProperty();
    private final StringProperty status = new SimpleStringProperty();
    private final StringProperty size = new SimpleStringProperty();
    private final StringProperty price = new SimpleStringProperty();

    public void loadProperty(Property property) {
        this.currentProperty = property;
        address.set(property.getAddress());
        propertyType.set(property.getPropertyType());
        bathroomNum.set(String.valueOf(property.getBathroomNum()));
        roomsNum.set(String.valueOf(property.getRoomsNum()));
        fullName.set(property.getFullName());
        floorNum.set(String.valueOf(property.getFloorNum()));
        status.set(property.getStatus());
        size.set(String.valueOf(property.getSize()));
        price.set(String.valueOf(property.getPrice()));
    }

    public StringProperty addressProperty() {
        return address;
    }

    public StringProperty propertyTypeProperty() {
        return propertyType;
    }

    public StringProperty bathroomNumProperty() {
        return bathroomNum;
    }

    public StringProperty roomsNumProperty() {
        return roomsNum;
    }

    public StringProperty fullNameProperty() {
        return fullName;
    }

    public StringProperty floorNumProperty() {
        return floorNum;
    }

    public StringProperty statusProperty() {
        return status;
    }

    public StringProperty sizeProperty() {
        return size;
    }

    public StringProperty priceProperty() {
        return price;
    }

   public Property applyChanges() {
        if (currentProperty == null) {
            throw new IllegalStateException("No property loaded for editing");
        }

        String addressValue = address.get();
        String propertyTypeValue = propertyType.get();
        int bathroomNumValue = Integer.parseInt(bathroomNum.get());
        int roomsNumValue = Integer.parseInt(roomsNum.get());
        String fullNameValue = fullName.get();
        int floorNumValue = Integer.parseInt(floorNum.get());
        String statusValue = status.get();
        double sizeValue = Double.parseDouble(size.get());
        double priceValue = Double.parseDouble(price.get());

        return Property.updateProperty(
                currentProperty.getId(),
                addressValue,
                propertyTypeValue,
                bathroomNumValue,
                roomsNumValue,
                fullNameValue,
                floorNumValue,
                statusValue,
                sizeValue,
                priceValue
        );
    }

    public void cancelEdit() {
        if (currentProperty != null) {
            loadProperty(currentProperty);
        } else {
            address.set("");
            propertyType.set("");
            bathroomNum.set("");
            roomsNum.set("");
            fullName.set("");
            floorNum.set("");
            status.set("");
            size.set("");
            price.set("");
        }
    }
}
