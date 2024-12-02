package via.sep.restful_server.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import via.sep.restful_server.dto.ImageDTO;
import via.sep.restful_server.model.Image;
import via.sep.restful_server.repository.ImageRepository;
import via.sep.restful_server.service.ImageService;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/images")
@CrossOrigin(origins = "*")
@Slf4j
public class ImageController {
    private final ImageService imageService;
    private final ImageRepository imageRepository;

    public ImageController(ImageService imageService, ImageRepository imageRepository) {
        this.imageService = imageService;
        this.imageRepository = imageRepository;
    }

    @PostMapping("/property/{propertyId}")
    public ResponseEntity<ImageDTO> uploadImage(
            @PathVariable Long propertyId,
            @RequestParam("image") MultipartFile file) {
        try {
            byte[] imageData = file.getBytes();

            Image savedImage = imageService.saveImage(propertyId, imageData);
            return ResponseEntity.ok(imageService.getImage(savedImage.getId()));
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImageDTO> getImage(@PathVariable Long id) {
        try {
            log.debug("Controller: Received request for image id: {}", id);

            ImageDTO dto = imageService.getImage(id);

            if (dto != null) {
                log.debug("Successfully retrieved image. ID: {}, Base64 length: {}",
                        dto.getId(),
                        dto.getBase64ImageData() != null ? dto.getBase64ImageData().length() : 0);
                return ResponseEntity.ok(dto);
            } else {
                log.debug("No image found for ID: {}", id);
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            log.error("Error fetching image {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<ImageDTO>> getPropertyImages(@PathVariable Long propertyId) {
        try {
            List<ImageDTO> images = imageService.getImagesForProperty(propertyId);
            return ResponseEntity.ok(images);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/raw")
    public ResponseEntity<byte[]> getImageRaw(@PathVariable Long id) {
        try {
            Image image = imageRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Image not found"));
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(image.getImageData());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        try {
            imageService.deleteImage(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
