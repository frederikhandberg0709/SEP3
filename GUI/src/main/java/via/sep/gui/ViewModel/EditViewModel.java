package via.sep.gui.ViewModel;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import via.sep.gui.Model.domain.Property;

public class EditViewModel {
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

    public void applyChanges() {
        String addressValue = address.get();
        String propertyTypeValue = propertyType.get();
        int bathroomNumValue = Integer.parseInt(bathroomNum.get());
        int roomsNumValue = Integer.parseInt(roomsNum.get());
        String fullNameValue = fullName.get();
        int floorNumValue = Integer.parseInt(floorNum.get());
        String statusValue = status.get();
        double sizeValue = Double.parseDouble(size.get());
        double priceValue = Double.parseDouble(price.get());


        Property.updateProperty(addressValue, propertyTypeValue, bathroomNumValue, roomsNumValue, fullNameValue, floorNumValue, statusValue, sizeValue, priceValue);
    }

    public void cancelEdit() {
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

    public void setPropertyToEdit(Property selectedProperty) {
        // Populate your properties with the selected property's data
        address.set(selectedProperty.getAddress());
        propertyType.set(selectedProperty.getPropertyType());

        // Convert BigDecimal and Integer to String for your StringProperty
        bathroomNum.set(String.valueOf(selectedProperty.getNumBathrooms()));
        roomsNum.set(String.valueOf(selectedProperty.getNumBedrooms()));

        // You might need to adjust how you get the full name
        // (this depends on how you're storing the client/owner name)
        // For now, using an empty string or a placeholder
        fullName.set("");

        floorNum.set(String.valueOf(selectedProperty.getFloorNumber()));

        // You might need to define how to set the status
        status.set(""); // or some default/derived value

        size.set(String.valueOf(selectedProperty.getFloorArea()));
        price.set(String.valueOf(selectedProperty.getPrice()));
    }
}
