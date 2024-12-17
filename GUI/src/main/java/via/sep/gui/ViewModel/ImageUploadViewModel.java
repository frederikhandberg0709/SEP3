package via.sep.gui.ViewModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import via.sep.gui.Model.ImageService;
import via.sep.gui.Model.domain.ImageViewModel;
import via.sep.gui.Model.dto.ImageDTO;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageUploadViewModel {
    private final ImageService imageService;
    private final ObservableList<ImageViewModel> images;
    private final StringProperty statusMessage = new SimpleStringProperty();
    private final List<File> selectedFiles = new ArrayList<>();
    private final List<Long> imagesToDelete = new ArrayList<>();

    public ImageUploadViewModel(ImageService imageService) {
        this.imageService = imageService;
        this.images = FXCollections.observableArrayList();
    }

    public ObservableList<ImageViewModel> getImages() {
        return images;
    }

    public List<File> getSelectedFiles() {
        return selectedFiles;
    }

    public StringProperty statusMessageProperty() {
        return statusMessage;
    }

    public void loadImagesForProperty(Long propertyId) {
        try {
            List<ImageDTO> imageDTOs = imageService.getImagesForProperty(propertyId);

            images.clear();
            imageDTOs.forEach(dto ->
                    images.add(new ImageViewModel(dto.getId(), dto.getPropertyId(), dto.getBase64ImageData()))
            );

            statusMessage.set("Images loaded successfully");
        } catch (Exception e) {
            statusMessage.set("Error loading images: " + e.getMessage());
        }
    }

    public void addImages(List<File> files) {
        selectedFiles.addAll(files);

        for(File file : files) {
            try {
                images.add(new ImageViewModel(file));
            } catch(Exception e) {
                statusMessage.set("Error previewing image: " + file.getName());
                e.printStackTrace();
            }
        }
    }

    public void removePreviewImage(ImageViewModel image) {
        images.remove(image);
        selectedFiles.removeIf(file -> file.toURI().toString().equals(image.getPreviewUrl()));
    }

    public void deleteImage(Long imageId) {
        try {
            //imageService.deleteImage(imageId);
            imagesToDelete.add(imageId);
            images.removeIf(img -> img.getId().equals(imageId));
            statusMessage.set("Image marked for deletion");
        } catch (Exception e) {
            statusMessage.set("Error marking image for deletion: " + e.getMessage());
        }
    }

    public List<Long> getImagesToDelete() {
        return imagesToDelete;
    }


    public void clearSelectedFiles() {
        selectedFiles.clear();
        images.clear();
        imagesToDelete.clear();
        statusMessage.set("");
    }
}
