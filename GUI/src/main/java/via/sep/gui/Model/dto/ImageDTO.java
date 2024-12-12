package via.sep.gui.Model.dto;

public class ImageDTO {
    private long id;
    private long propertyId;
    private String base64ImageData;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getPropertyId() { return propertyId; }
    public void setPropertyId(long propertyId) { this.propertyId = propertyId; }

    public String getBase64ImageData() { return base64ImageData; }
    public void setBase64ImageData(String base64ImageData) { this.base64ImageData = base64ImageData; }
}
