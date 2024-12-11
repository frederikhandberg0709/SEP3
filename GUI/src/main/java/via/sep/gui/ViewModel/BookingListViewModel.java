package via.sep.gui.ViewModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import via.sep.gui.Model.BookingService;
import via.sep.gui.Model.SceneManager;
import via.sep.gui.Model.domain.Booking;
import java.util.List;

public class BookingListViewModel {
    private final ObservableList<Booking> bookings;
    private final BookingService bookingService;

    public BookingListViewModel(BookingService bookingService) {
        this.bookings = FXCollections.observableArrayList();
        this.bookingService = bookingService;
    }

    public void loadBookings() throws Exception {
        List<Booking> bookingList = bookingService.getAllBookings();
        bookings.setAll(bookingList);
    }

    public ObservableList<Booking> getBookings() {
        return bookings;
    }

    public void removeBooking(Booking booking) throws Exception {
        bookingService.deleteBooking(booking.getBookingId());
        bookings.remove(booking);
    }

    public void closeBookingList() {
        SceneManager.showDashboard();
    }
}
