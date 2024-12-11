package via.sep.gui.View;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import via.sep.gui.Model.domain.Booking;
import via.sep.gui.ViewModel.BookingListViewModel;

public class BookingListView {
    @FXML
    private Button removeButton;
    @FXML
    private Button closeButton;

    @FXML private TableView<Booking> bookingTable;
    @FXML private TableColumn<Booking, String> propertyColumn;
    @FXML private TableColumn<Booking, String> agentColumn;
    @FXML private TableColumn<Booking, String> bookingDateColumn;

    private BookingListViewModel viewModel;

    @FXML
    private void initialize() {
        propertyColumn.setCellValueFactory(new PropertyValueFactory<>("propertyName"));
        agentColumn.setCellValueFactory(new PropertyValueFactory<>("agentName"));
        bookingDateColumn.setCellValueFactory(new PropertyValueFactory<>("bookingDate"));

        removeButton.disableProperty().bind(bookingTable.getSelectionModel().selectedItemProperty().isNull());
    }

    @FXML
    public void setupBindings() {
        bookingTable.setItems(viewModel.getBookings());

        removeButton.disableProperty().bind(bookingTable.getSelectionModel().selectedItemProperty().isNull());
    }

    @FXML
    private void handleRemoveBooking() {
        Booking selectedBooking = bookingTable.getSelectionModel().getSelectedItem();
        if (selectedBooking != null) {
            try {
                viewModel.removeBooking(selectedBooking);

                viewModel.loadBookings();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to delete booking: " + e.getMessage());
                alert.showAndWait();
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleClose() {
        viewModel.closeBookingList();
    }

    public void setViewModel(BookingListViewModel viewModel) {
        this.viewModel = viewModel;
        bookingTable.setItems(viewModel.getBookings());

        try {
            viewModel.loadBookings();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
