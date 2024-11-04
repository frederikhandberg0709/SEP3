package via.sep.logic.View;

import javafx.beans.property.Property;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.Optional;

public class DashBoardView {

    @FXML
    private Button createPropertyButton;

    @FXML
    private Button deletePropertyButton;

    @FXML
    private Button editPropertyButton;

    @FXML
    private TableView<Property> propertyTableView;

    private ObservableList<Property> propertyList;

    @FXML
    private void initialize() {
        propertyList = FXCollections.observableArrayList();
        propertyTableView.setItems(propertyList);

        // Assuming Property class has these fields
        TableColumn<Property, String> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<Property, String> clientNameColumn = new TableColumn<>("Client Name");
        clientNameColumn.setCellValueFactory(new PropertyValueFactory<>("clientName"));

        TableColumn<Property, String> phoneNumberColumn = new TableColumn<>("Phone Number");
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        TableColumn<Property, String> leasePeriodColumn = new TableColumn<>("Lease Period");
        leasePeriodColumn.setCellValueFactory(new PropertyValueFactory<>("leasePeriod"));

        TableColumn<Property, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Property, String> propertyTypeColumn = new TableColumn<>("Property Type");
        propertyTypeColumn.setCellValueFactory(new PropertyValueFactory<>("propertyType"));

        TableColumn<Property, String> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        propertyTableView.getColumns().addAll(addressColumn, clientNameColumn, phoneNumberColumn, leasePeriodColumn, emailColumn, propertyTypeColumn, priceColumn);
    }

    @FXML
    private void handleCreateProperty() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create Property");
        dialog.setHeaderText("Enter property details");

        // Collect property details
        dialog.setContentText("Address:");
        Optional<String> address = dialog.showAndWait();
        if (!address.isPresent()) return;

        dialog.setContentText("Client Name:");
        Optional<String> clientName = dialog.showAndWait();
        if (!clientName.isPresent()) return;

        dialog.setContentText("Phone Number:");
        Optional<String> phoneNumber = dialog.showAndWait();
        if (!phoneNumber.isPresent()) return;

        dialog.setContentText("Lease Period:");
        Optional<String> leasePeriod = dialog.showAndWait();
        if (!leasePeriod.isPresent()) return;

        dialog.setContentText("Email:");
        Optional<String> email = dialog.showAndWait();
        if (!email.isPresent()) return;

        dialog.setContentText("Property Type:");
        Optional<String> propertyType = dialog.showAndWait();
        if (!propertyType.isPresent()) return;

        dialog.setContentText("Price:");
        Optional<String> price = dialog.showAndWait();
        if (!price.isPresent()) return;

        Property newProperty = new Property(address.get(), clientName.get(), phoneNumber.get(), leasePeriod.get(), email.get(), propertyType.get(), price.get());
        propertyList.add(newProperty);
    }

    @FXML
    private void handleDeleteProperty() {
        Property selectedProperty = propertyTableView.getSelectionModel().getSelectedItem();
        if (selectedProperty != null) {
            propertyList.remove(selectedProperty);
        } else {
            showAlert("No Selection", "No Property Selected", "Please select a property in the table.");
        }
    }

    @FXML
    private void handleEditProperty() {
        Property selectedProperty = propertyTableView.getSelectionModel().getSelectedItem();
        if (selectedProperty != null) {
            TextInputDialog dialog = new TextInputDialog(selectedProperty.getAddress());
            dialog.setTitle("Edit Property");
            dialog.setHeaderText("Edit property details");

            dialog.setContentText("Address:");
            Optional<String> address = dialog.showAndWait();
            if (!address.isPresent()) return;

            dialog.setContentText("Client Name:");
            Optional<String> clientName = dialog.showAndWait();
            if (!clientName.isPresent()) return;

            dialog.setContentText("Phone Number:");
            Optional<String> phoneNumber = dialog.showAndWait();
            if (!phoneNumber.isPresent()) return;

            dialog.setContentText("Lease Period:");
            Optional<String> leasePeriod = dialog.showAndWait();
            if (!leasePeriod.isPresent()) return;

            dialog.setContentText("Email:");
            Optional<String> email = dialog.showAndWait();
            if (!email.isPresent()) return;

            dialog.setContentText("Property Type:");
            Optional<String> propertyType = dialog.showAndWait();
            if (!propertyType.isPresent()) return;

            dialog.setContentText("Price:");
            Optional<String> price = dialog.showAndWait();
            if (!price.isPresent()) return;

            selectedProperty.setAddress(address.get());
            selectedProperty.setClientName(clientName.get());
            selectedProperty.setPhoneNumber(phoneNumber.get());
            selectedProperty.setLeasePeriod(leasePeriod.get());
            selectedProperty.setEmail(email.get());
            selectedProperty.setPropertyType(propertyType.get());
            selectedProperty.setPrice(price.get());

            propertyTableView.refresh();
        } else {
            showAlert("No Selection", "No Property Selected", "Please select a property in the table.");
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}