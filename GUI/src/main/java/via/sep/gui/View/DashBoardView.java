package via.sep.gui.View;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import via.sep.gui.Model.SceneManager;
import via.sep.gui.Model.domain.Property;
import via.sep.gui.ViewModel.DashboardViewModel;

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
    private void handleCreateProperty() {
        SceneManager.showCreate();
    }

    @FXML
    private void handleDeleteProperty() {
        // Logic to delete the selected property
        Property selectedProperty = propertyTableView.getSelectionModel().getSelectedItem();
        if (selectedProperty != null) {
            propertyList.remove(selectedProperty);
        } else {
            showAlert("No Selection", "Please select a property to delete.");
        }
    }



    @FXML
    private void handleEditProperty() {
        // Logic to edit the selected property (e.g., open a form to edit property details)
        Property selectedProperty = propertyTableView.getSelectionModel().getSelectedItem();
        // Show sceneManager to show edit view
        if (selectedProperty != null) {
            SceneManager.showEdit(selectedProperty);
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


    public void setViewModel(DashboardViewModel viewModel) {

    }
}
