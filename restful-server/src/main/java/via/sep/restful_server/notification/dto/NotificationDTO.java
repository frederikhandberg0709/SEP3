package via.sep.restful_server.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class NotificationDTO {
    private String type;
    private String action;
    private String entityId;
    private LocalDateTime timestamp;
    private Object data;
}
