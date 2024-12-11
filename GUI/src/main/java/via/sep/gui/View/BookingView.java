package via.sep.gui.View;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import via.sep.gui.Model.SceneManager;
import via.sep.gui.Model.dto.BookingDTO;
import via.sep.gui.ViewModel.BookingViewModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BookingView {
    @FXML
    private TableView<BookingDTO> bookingTableView;
    @FXML private TableColumn<BookingDTO, LocalDateTime> dateColumn;
    @FXML private TableColumn<BookingDTO, String> agentNameColumn;
    @FXML private TableColumn<BookingDTO, String> agentContactColumn;
    @FXML private TableColumn<BookingDTO, Void> actionsColumn;

    @FXML private DatePicker datePicker;
    @FXML private Button refreshButton;
    @FXML private Button backButton;
    @FXML private Label statusLabel;

    private BookingViewModel viewModel;

    @FXML
    public void initialize() {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("bookingDate"));
        agentNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getAgent().getName()));
        agentContactColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getAgent().getContactInfo()));

        dateColumn.setCellFactory(column -> new TableCell<BookingDTO, LocalDateTime>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            @Override
            protected void updateItem(LocalDateTime date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(formatter.format(date));
                }
            }
        });

        actionsColumn.setCellFactory(column -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    BookingDTO booking = getTableView().getItems().get(getIndex());
                    handleDeleteBooking(booking);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        refreshButton.setOnAction(event -> handleRefresh());
        backButton.setOnAction(event -> handleBack());

        datePicker.valueProperty().addListener((obs, oldVal, newVal) ->
                viewModel.filterDateProperty().set(newVal));
    }

    private void handleDeleteBooking(BookingDTO booking) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Delete");
        confirmation.setHeaderText("Delete Booking");
        confirmation.setContentText("Are you sure you want to delete this booking?");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                viewModel.deleteBooking(booking.getBookingId());
            }
        });
    }

    private void handleRefresh() {
        viewModel.loadBookings();
    }

    private void handleBack() {
        SceneManager.showDashboard();
    }

    public void setViewModel(BookingViewModel viewModel) {
        this.viewModel = viewModel;

        bookingTableView.setItems(viewModel.getBookings());

        datePicker.valueProperty().bindBidirectional(viewModel.filterDateProperty());

        statusLabel.textProperty().bind(viewModel.statusMessageProperty());

        viewModel.loadBookings();
    }
}
