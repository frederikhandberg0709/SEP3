package via.sep.gui.View;

import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert.AlertType;
import via.sep.gui.Model.domain.Property;
import via.sep.gui.ViewModel.DashboardViewModel;

import java.math.BigDecimal;

public class DashboardView {
    @FXML private Button createPropertyButton;
    @FXML private Button deletePropertyButton;
    @FXML private Button editPropertyButton;
    @FXML private Button bookingListButton;
    @FXML private Button refreshButton;

    @FXML private TextField searchField;
    @FXML private Label statusLabel;

    @FXML private TableView<Property> propertyTableView;
    @FXML private TableColumn<Property, String> addressColumn;
    @FXML private TableColumn<Property, String> propertyTypeColumn;
    @FXML private TableColumn<Property, BigDecimal> priceColumn;
    @FXML private TableColumn<Property, Integer> bedroomsColumn;
    @FXML private TableColumn<Property, Integer> bathroomsColumn;
    @FXML private TableColumn<Property, BigDecimal> floorAreaColumn;
    @FXML private TableColumn<Property, Integer> yearBuiltColumn;
    @FXML private TableColumn<Property, String> descriptionColumn;

    private DashboardViewModel viewModel;
    private FilteredList<Property> filteredProperties;

    @FXML
    public void initialize() {
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        propertyTypeColumn.setCellValueFactory(new PropertyValueFactory<>("propertyType"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        bedroomsColumn.setCellValueFactory(new PropertyValueFactory<>("numBedrooms"));
        bathroomsColumn.setCellValueFactory(new PropertyValueFactory<>("numBathrooms"));
        floorAreaColumn.setCellValueFactory(new PropertyValueFactory<>("floorArea"));
        yearBuiltColumn.setCellValueFactory(new PropertyValueFactory<>("yearBuilt"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        setupSearch();

        propertyTableView.setRowFactory(tv -> {
            TableRow<Property> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    handleEditProperty();
                }
            });
            return row;
        });
    }

    private void setupSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (filteredProperties != null) {
                filteredProperties.setPredicate(property -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    String lowerCaseFilter = newValue.toLowerCase();

                    return property.getAddress().toLowerCase().contains(lowerCaseFilter)
                            || property.getPropertyType().toLowerCase().contains(lowerCaseFilter)
                            || property.getDescription().toLowerCase().contains(lowerCaseFilter)
                            || property.getPrice().toString().contains(lowerCaseFilter);
                });
            }
        });
    }

    @FXML
    private void handleCreateProperty() {
        viewModel.statusMessageProperty().set("Opening create property form...");
        viewModel.showCreateProperty();
    }

    @FXML
    private void handleDeleteProperty() {
        Property selectedProperty = propertyTableView.getSelectionModel().getSelectedItem();
        if (selectedProperty != null) {
            Alert confirmation = new Alert(AlertType.CONFIRMATION);
            confirmation.setTitle("Confirm Delete");
            confirmation.setHeaderText("Delete Property");
            confirmation.setContentText("Are you sure you want to delete this property?");

            confirmation.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    viewModel.deleteProperty(selectedProperty);
                    updateStatusLabel("Property deleted successfully");
                }
            });
        } else {
            showAlert("No Selection", "Please select a property to delete.", AlertType.WARNING);
        }
    }

    @FXML
    private void handleEditProperty() {
        Property selectedProperty = propertyTableView.getSelectionModel().getSelectedItem();
        if (selectedProperty != null) {
            viewModel.showEditProperty(selectedProperty);
            updateStatusLabel("Opening edit form for selected property...");
        } else {
            showAlert("No Selection", "Please select a property to edit.", AlertType.WARNING);
        }
    }

    @FXML
    private void handleBookingList() {
        viewModel.showBookingList();
        updateStatusLabel("Opening booking list...");
    }

    @FXML
    private void handleRefresh() {
        viewModel.refreshProperties();
        updateStatusLabel("Property list refreshed");
    }

    private void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void updateStatusLabel(String message) {
        statusLabel.setText(message);
    }

    public void setViewModel(DashboardViewModel viewModel) {
        this.viewModel = viewModel;

        // Bind the table to the viewModel's property list
        filteredProperties = new FilteredList<>(viewModel.getPropertyList(), p -> true);
        propertyTableView.setItems(filteredProperties);

        // Bind status message
        statusLabel.textProperty().bind(viewModel.statusMessageProperty());
    }
}
