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
public class BookmarkNotificationDTO {
    private String bookmarkId;
    private String propertyId;
    private String accountId;
    private String propertyAddress;
    private BigDecimal propertyPrice;
    private String propertyType;
    private Map<String, Object> propertyDetails;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
}
