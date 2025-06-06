package via.sep.restful_server.notification.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
public class PropertyUpdateNotificationDTO {
    private String propertyId;
    private String address;
    private BigDecimal oldPrice;
    private BigDecimal newPrice;
    private String propertyType;
    private Map<String, Object> updatedFields;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
}
