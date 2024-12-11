package via.sep.restful_server.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class NotificationDTO {
    private Long notificationId;
    private String type;
    private String message;
    private Map<String, Object> details;
    private LocalDateTime timestamp;
    private boolean read;
    private String referenceId;
}
