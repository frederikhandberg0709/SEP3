package via.sep.restful_server.notification.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FormNotificationDTO {
    private String formId;
    private String address;
    private String propertyType;
    private BigDecimal price;
    private Map<String, Object> details;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

}
