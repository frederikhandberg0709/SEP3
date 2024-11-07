package via.sep.restful_server.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
public class BookingNotificationData {
    private String bookingId;
    private String propertyId;
    private String agentId;
    private LocalDateTime bookingDate;
    private String status;
}
