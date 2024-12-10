package via.sep.gui.ViewModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import via.sep.gui.Model.domain.Property;
import via.sep.gui.Model.PropertyService;

public class DashboardViewModel {
    private final ObservableList<Property> propertyList = FXCollections.observableArrayList();

    public ObservableList<Property> getPropertyList() {
        return propertyList;
    }

    public void addProperty(Property property) {
        propertyList.add(property);
    }

    public void removeProperty(Property property) {
        propertyList.remove(property);
    }

    public void editProperty(Property property) {
        for (int i = 0; i < propertyList.size(); i++) {
            if (propertyList.get(i).getId().equals(property.getId())) {
                propertyList.set(i, property);
                break;
            }
        }
    }
}