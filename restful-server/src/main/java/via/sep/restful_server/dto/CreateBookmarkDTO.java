package via.sep.restful_server.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class CreateBookmarkDTO {
    @NotNull(message = "Property ID cannot be null")
    private Long propertyId;
}
