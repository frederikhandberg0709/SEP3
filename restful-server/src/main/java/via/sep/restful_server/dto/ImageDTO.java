package via.sep.restful_server.dto;

import lombok.Data;

@Data
public class ImageDTO {
    private long id;
    private long propertyId;
    private String base64ImageData;
}
