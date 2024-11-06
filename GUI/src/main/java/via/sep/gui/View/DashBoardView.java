package via.sep.gui.View;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import via.sep.gui.Model.Property;

public class DashBoardView {

    @FXML
    private Button createPropertyButton;
    @FXML
    private Button deletePropertyButton;
    @FXML
    private Button editPropertyButton;
    @FXML
    private TableView<Property> propertyTableView;
    @FXML
    private TableColumn<Property, String> addressColumn;
    @FXML
    private TableColumn<Property, String> clientNameColumn;
    @FXML
    private TableColumn<Property, String> phoneNumberColumn;
    @FXML
    private TableColumn<Property, String> leasePeriodColumn;
    @FXML
    private TableColumn<Property, String> emailColumn;
    @FXML
    private TableColumn<Property, String> propertyTypeColumn;
    @FXML
    private TableColumn<Property, Double> priceColumn;

    private ObservableList<Property> propertyList;

    @FXML
    public void initialize() {
        // Initialize the columns with Property attributes
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        clientNameColumn.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        leasePeriodColumn.setCellValueFactory(new PropertyValueFactory<>("leasePeriod"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        propertyTypeColumn.setCellValueFactory(new PropertyValueFactory<>("propertyType"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Initialize the ObservableList
        propertyList = FXCollections.observableArrayList();
        propertyTableView.setItems(propertyList);
    }

    @FXML
    private void handleCreateProperty(MouseEvent event) {
        // Logic to create a new property (e.g., open a form to add property details)
        showAlert("Create Property", "This will open a form to create a new property.");
    }

    @FXML
    private void handleDeleteProperty(MouseEvent event) {
        // Logic to delete the selected property
        Property selectedProperty = propertyTableView.getSelectionModel().getSelectedItem();
        if (selectedProperty != null) {
            propertyList.remove(selectedProperty);
            showAlert("Delete Property", "Property has been deleted.");
        } else {
            showAlert("No Selection", "Please select a property to delete.");
        }
    }

    @FXML
    private void handleEditProperty(MouseEvent event) {
        // Logic to edit the selected property (e.g., open a form to edit property details)
        Property selectedProperty = propertyTableView.getSelectionModel().getSelectedItem();
        if (selectedProperty != null) {
            showAlert("Edit Property", "This will open a form to edit the selected property.");
        } else {
            showAlert("No Selection", "Please select a property to edit.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
