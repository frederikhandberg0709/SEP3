package via.sep.gui.ViewModel;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import via.sep.gui.Model.BookingService;
import via.sep.gui.Model.domain.Property;
import via.sep.gui.Model.dto.BookingDTO;

import java.time.LocalDate;

public class BookingViewModel {
    private final BookingService bookingService;
    private final Property property;

    private final ObservableList<BookingDTO> bookings = FXCollections.observableArrayList();
    private final ObjectProperty<LocalDate> filterDate = new SimpleObjectProperty<>();
    private final StringProperty statusMessage = new SimpleStringProperty("");

    public BookingViewModel(BookingService bookingService, Property property) {
        this.bookingService = bookingService;
        this.property = property;

        // Listen for date filter changes
        filterDate.addListener((obs, oldDate, newDate) -> loadBookings());
    }

    public void loadBookings() {
        try {
            var propertyBookings = bookingService.getBookingsForProperty(property.getPropertyId());
            bookings.clear();

            if (filterDate.get() != null) {
                propertyBookings.removeIf(booking ->
                        !booking.getBookingDate().toLocalDate().equals(filterDate.get()));
            }

            bookings.addAll(propertyBookings);
            statusMessage.set("");
        } catch (Exception e) {
            statusMessage.set("Failed to load bookings: " + e.getMessage());
        }
    }

    public void deleteBooking(Long bookingId) {
        try {
            bookingService.cancelBooking(bookingId);
            loadBookings(); // Refresh the bookings list
            statusMessage.set("Booking cancelled successfully");
        } catch (Exception e) {
            statusMessage.set("Failed to cancel booking: " + e.getMessage());
        }
    }

    public ObservableList<BookingDTO> getBookings() { return bookings; }
    public ObjectProperty<LocalDate> filterDateProperty() { return filterDate; }
    public StringProperty statusMessageProperty() { return statusMessage; }
    public Property getProperty() { return property; }
}
