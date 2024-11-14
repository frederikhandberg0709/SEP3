package via.sep.restful_server.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingNotificationDTO {
    private String bookingId;
    private String propertyId;
    private String agentId;
    private LocalDateTime bookingDate;
    private String status;
}
