package via.sep.gui.ViewModel;

import javafx.beans.InvalidationListener;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;


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

    private final Property propertyService = new Property() {
        @Override
        public void addListener(InvalidationListener invalidationListener) {

        }

        @Override
        public void removeListener(InvalidationListener invalidationListener) {

        }

        @Override
        public void addListener(ChangeListener changeListener) {

        }

        @Override
        public void removeListener(ChangeListener changeListener) {

        }

        @Override
        public Object getValue() {
            return null;
        }

        @Override
        public void setValue(Object o) {

        }

        @Override
        public Object getBean() {
            return null;
        }

        @Override
        public String getName() {
            return "";
        }

        @Override
        public void bind(ObservableValue observableValue) {

        }

        @Override
        public void unbind() {

        }

        @Override
        public boolean isBound() {
            return false;
        }

        @Override
        public void bindBidirectional(Property property) {

        }

        @Override
        public void unbindBidirectional(Property property) {

        }
    };

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

    public void createProperty() {
        String addressValue = address.get();
        String propertyTypeValue = propertyType.get();
        int bathroomNumValue = Integer.parseInt(bathroomNum.get());
        int roomsNumValue = Integer.parseInt(roomsNum.get());
        String fullNameValue = fullName.get();
        int floorNumValue = Integer.parseInt(floorNum.get());
        String statusValue = status.get();
        double sizeValue = Double.parseDouble(size.get());
        double priceValue = Double.parseDouble(price.get());

        propertyService.createProperty(addressValue, propertyTypeValue, bathroomNumValue, roomsNumValue, fullNameValue, floorNumValue, statusValue, sizeValue, priceValue);
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