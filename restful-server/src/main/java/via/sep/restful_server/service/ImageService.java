package via.sep.restful_server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import via.sep.restful_server.dto.ImageDTO;
import via.sep.restful_server.model.Image;
import via.sep.restful_server.model.Property;
import via.sep.restful_server.repository.ImageRepository;
import via.sep.restful_server.repository.PropertyRepository;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ImageService {
    private final ImageRepository imageRepository;
    private final PropertyRepository propertyRepository;

    public ImageService(ImageRepository imageRepository, PropertyRepository propertyRepository) {
        this.imageRepository = imageRepository;
        this.propertyRepository = propertyRepository;
    }

    public Image saveImage(Long propertyId, byte[] imageData) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        Image image = new Image();
        image.setProperty(property);
        image.setImageData(imageData);
        return imageRepository.save(image);
    }

    public ImageDTO getImage(Long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        return convertToDTO(image);
    }

    public List<ImageDTO> getImagesForProperty(Long propertyId) {
        return imageRepository.findByProperty_PropertyId(propertyId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ImageDTO convertToDTO(Image image) {
        ImageDTO dto = new ImageDTO();
        dto.setId(image.getId());
        dto.setPropertyId(image.getProperty().getPropertyId());
        dto.setBase64ImageData(Base64.getEncoder().encodeToString(image.getImageData()));
        return dto;
    }

    public void deleteImage(Long id) {
        imageRepository.deleteById(id);
    }
}
