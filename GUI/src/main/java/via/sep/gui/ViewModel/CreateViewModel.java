package via.sep.gui.ViewModel;

import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import via.sep.gui.Model.PropertyService;
import via.sep.gui.Model.SceneManager;
import via.sep.gui.Server.ServerConnection;
import com.google.gson.Gson;

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

    private final PropertyService propertyService;

    public CreateViewModel() {
        ServerConnection serverConnection = new ServerConnection();
        Gson gson = new Gson();
        this.propertyService = new PropertyService(serverConnection, gson);
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

    public void createProperty() throws Exception {
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

    public void addListener(InvalidationListener listener) {
        address.addListener(listener);
        propertyType.addListener(listener);
        bathroomNum.addListener(listener);
        roomsNum.addListener(listener);
        fullName.addListener(listener);
        floorNum.addListener(listener);
        status.addListener(listener);
        size.addListener(listener);
        price.addListener(listener);
    }

    public void removeListener(InvalidationListener listener) {
        address.removeListener(listener);
        propertyType.removeListener(listener);
        bathroomNum.removeListener(listener);
        roomsNum.removeListener(listener);
        fullName.removeListener(listener);
        floorNum.removeListener(listener);
        status.removeListener(listener);
        size.removeListener(listener);
        price.removeListener(listener);
    }

    public void addListener(ChangeListener<? super String> listener) {
        address.addListener(listener);
        propertyType.addListener(listener);
        bathroomNum.addListener(listener);
        roomsNum.addListener(listener);
        fullName.addListener(listener);
        floorNum.addListener(listener);
        status.addListener(listener);
        size.addListener(listener);
        price.addListener(listener);
    }

    public void removeListener(ChangeListener<? super String> listener) {
        address.removeListener(listener);
        propertyType.removeListener(listener);
        bathroomNum.removeListener(listener);
        roomsNum.removeListener(listener);
        fullName.removeListener(listener);
        floorNum.removeListener(listener);
        status.removeListener(listener);
        size.removeListener(listener);
        price.removeListener(listener);
    }

    public void showCreate() {
        SceneManager.showCreate();
    }
}