package via.sep.restful_server.notification.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingNotificationDTO {
    private String bookingId;
    private String propertyId;
    private String agentId;
    private LocalDateTime bookingDate;
    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
}
