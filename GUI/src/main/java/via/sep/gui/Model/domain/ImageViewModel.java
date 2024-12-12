package via.sep.gui.Model.domain;

import java.io.File;

public class ImageViewModel {
    private final Long id;
    private final Long propertyId;
    private final String base64Data;
    private final String previewUrl;
    private final boolean isPreview;

    public ImageViewModel(Long id, Long propertyId, String base64Data) {
        this.id = id;
        this.propertyId = propertyId;
        this.base64Data = base64Data;
        this.previewUrl = null;
        this.isPreview = false;
    }

    public ImageViewModel(File file) {
        this.id = null;
        this.propertyId = null;
        this.base64Data = null;
        this.previewUrl = file.toURI().toString();
        this.isPreview = true;
    }

    public Long getId() { return id; }
    public Long getPropertyId() { return propertyId; }
    public String getBase64Data() { return base64Data; }

    public String getPreviewUrl() {
        if (previewUrl != null) {
            return previewUrl;
        } else if (base64Data != null) {
            return "data:image/jpeg;base64," + base64Data;
        }
        return null;
    }

    public boolean isPreview() {
        return isPreview;
    }

    public String getImageUrl() {
        return isPreview ? previewUrl : "data:image/jpeg;base64," + base64Data;
    }
}
