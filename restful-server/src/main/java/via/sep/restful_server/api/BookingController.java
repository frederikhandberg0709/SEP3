package via.sep.restful_server.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import via.sep.restful_server.dto.BookingDTO;
import via.sep.restful_server.model.Agent;
import via.sep.restful_server.model.Booking;
import via.sep.restful_server.model.Property;
import via.sep.restful_server.repository.AgentRepository;
import via.sep.restful_server.repository.BookingRepository;
import via.sep.restful_server.repository.PropertyRepository;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingRepository bookingRepository;
    private final PropertyRepository propertyRepository;
    private final AgentRepository agentRepository;

    @Autowired
    public BookingController(BookingRepository bookingRepository,
                             PropertyRepository propertyRepository,
                             AgentRepository agentRepository) {
        this.bookingRepository = bookingRepository;
        this.propertyRepository = propertyRepository;
        this.agentRepository = agentRepository;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createBooking(@RequestBody BookingDTO bookingDTO) {
        Property property = propertyRepository.findById(bookingDTO.getPropertyId())
                .orElseThrow(() -> new RuntimeException("Property not found"));

        Agent agent = agentRepository.findById(bookingDTO.getAgentId())
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        Booking booking = new Booking();
        booking.setProperty(property);
        booking.setAgent(agent);
        booking.setBookingDate(bookingDTO.getBookingDate());

        Booking savedBooking = bookingRepository.save(booking);
        return ResponseEntity.ok(savedBooking);
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getBookings(@RequestParam(required = false) LocalDate date) {
        if (date != null) {
            return ResponseEntity.ok(bookingRepository.findByBookingDate(date));
        }
        return ResponseEntity.ok(bookingRepository.findAll());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id) {
        return bookingRepository.findById(id)
                .map(booking -> {
                    bookingRepository.delete(booking);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
