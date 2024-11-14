package via.sep.restful_server.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
@AllArgsConstructor
public class PropertyNotificationDTO {
    private String propertyId;
    private String address;
    private BigDecimal price;
    private String propertyType;
    private Map<String, Object> details;
}
