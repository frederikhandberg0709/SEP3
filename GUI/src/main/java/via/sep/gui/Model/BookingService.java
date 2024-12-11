package via.sep.gui.Model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import via.sep.gui.Model.dto.BookingDTO;
import via.sep.gui.Server.ServerConnection;

import java.lang.reflect.Type;
import java.util.List;

public class BookingService {
    private final ServerConnection serverConnection;
    private final Gson gson;
    private static final String BASE_PATH = "/api/bookings";

    public BookingService(ServerConnection serverConnection, Gson gson) {
        this.serverConnection = serverConnection;
        this.gson = gson;
    }

    public List<BookingDTO> getBookingsForProperty(Long propertyId) throws Exception {
        String response = serverConnection.sendGetRequest(BASE_PATH + "?propertyId=" + propertyId);
        Type listType = new TypeToken<List<BookingDTO>>(){}.getType();
        return gson.fromJson(response, listType);
    }

//    public BookingDTO createBooking(BookingDTO bookingDTO) throws Exception {
//        String json = gson.toJson(bookingDTO);
//        String response = serverConnection.sendPostRequest(BASE_PATH, json);
//        return gson.fromJson(response, BookingDTO.class);
//    }

    public void cancelBooking(Long bookingId) throws Exception {
        serverConnection.sendDeleteRequest(BASE_PATH + "/" + bookingId);
    }
}
