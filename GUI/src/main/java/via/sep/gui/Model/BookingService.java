package via.sep.gui.Model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import via.sep.gui.Model.domain.Booking;
import via.sep.gui.Server.ServerConnection;

import java.lang.reflect.Type;
import java.util.List;

public class BookingService {
    private final ServerConnection serverConnection;
    private final Gson gson;
    private static final String BASE_PATH = "/bookings";

    public BookingService(ServerConnection serverConnection, Gson gson) {
        this.serverConnection = serverConnection;
        this.gson = gson;
    }

    public List<Booking> getAllBookings() throws Exception {
        String jsonResponse = serverConnection.sendGetRequest(BASE_PATH);
        System.out.println("Server response for bookings: " + jsonResponse);

        Type listType = new TypeToken<List<Booking>>() {}.getType();
        List<Booking> bookings = gson.fromJson(jsonResponse, listType);
        System.out.println("Parsed bookings: " + bookings.size());
        return bookings;
    }

    public void deleteBooking(Long bookingId) throws Exception {
        serverConnection.sendDeleteRequest(BASE_PATH + "/" + bookingId);
    }
}
