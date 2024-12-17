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
        try {
            Property property = propertyRepository.findById(propertyId)
                    .orElseThrow(() -> new RuntimeException("Property not found"));

            Image image = new Image();
            image.setProperty(property);
            image.setImageData(imageData);

            Image savedImage = imageRepository.save(image);
            log.debug("Successfully saved image with ID: {}", savedImage.getId());

            return savedImage;
        } catch (Exception e) {
            log.error("Failed to save image: {}", e.getMessage());
            throw e;
        }
    }

    public ImageDTO getImage(Long id) {
        log.debug("Service: Fetching image with id: {}", id);
        try {
            Image image = imageRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Image not found"));

            ImageDTO dto = convertToDTO(image);
            log.debug("ImageService: Successfully converted image {} to DTO", id);

            return dto;
        } catch (Exception e) {
            log.error("Error processing image {}: {}", id, e.getMessage());
            throw e;
        }
    }

    public List<ImageDTO> getImagesForProperty(Long propertyId) {
        return imageRepository.findByProperty_PropertyId(propertyId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ImageDTO convertToDTO(Image image) {
        try {
            ImageDTO dto = new ImageDTO();
            dto.setId(image.getId());
            dto.setPropertyId(image.getProperty().getPropertyId());
            String base64Data = Base64.getEncoder().encodeToString(image.getImageData());
            dto.setBase64ImageData(base64Data);
            log.debug("Successfully converted image {} to DTO with base64 length: {}",
                    image.getId(),
                    base64Data.length());
            return dto;
        } catch (Exception e) {
            log.error("Failed to convert image {} to DTO: {}", image.getId(), e.getMessage());
            throw e;
        }
    }

    public void deleteImage(Long id) {
        imageRepository.deleteById(id);
    }
}
