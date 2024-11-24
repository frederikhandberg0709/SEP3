package via.sep.gui.ViewModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import via.sep.gui.Model.Property;

import java.util.UUID;

public class CreateViewModel {
    private final StringProperty address = new SimpleStringProperty();
    private final StringProperty propertyType = new SimpleStringProperty();
    private final StringProperty bathroomNum = new SimpleStringProperty();
    private final StringProperty roomsNum = new SimpleStringProperty();
    private final StringProperty fullName = new SimpleStringProperty();
    private final StringProperty floorNum = new SimpleStringProperty();
    private final StringProperty status = new SimpleStringProperty();
    private final StringProperty size = new SimpleStringProperty();
    private final StringProperty price = new SimpleStringProperty();

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

    public Property createProperty() {
        UUID id = UUID.randomUUID();
        String addressValue = address.get();
        String propertyTypeValue = propertyType.get();
        int bathroomNumValue = Integer.parseInt(bathroomNum.get());
        int roomsNumValue = Integer.parseInt(roomsNum.get());
        String fullNameValue = fullName.get();
        int floorNumValue = Integer.parseInt(floorNum.get());
        String statusValue = status.get();
        double sizeValue = Double.parseDouble(size.get());
        double priceValue = Double.parseDouble(price.get());

        return  Property.createProperty(
                id,
                addressValue,
                propertyTypeValue,
                bathroomNumValue,
                roomsNumValue,
                fullNameValue,
                floorNumValue,
                statusValue,
                sizeValue,
                priceValue);
    }

    public void cancelCreation() {
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