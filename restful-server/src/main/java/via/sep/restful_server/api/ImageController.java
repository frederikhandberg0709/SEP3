package via.sep.restful_server.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import via.sep.restful_server.dto.ImageDTO;
import via.sep.restful_server.model.Image;
import via.sep.restful_server.service.ImageService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/images")
@CrossOrigin(origins = "*")
public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/property/{propertyId}")
    public ResponseEntity<ImageDTO> uploadImage(
            @PathVariable Long propertyId,
            @RequestParam("image")MultipartFile file) {
        try {
            Image savedImage = imageService.saveImage(propertyId, file.getBytes());
            return ResponseEntity.ok(imageService.getImage(savedImage.getId()));
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImageDTO> getImage(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(imageService.getImage(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<ImageDTO>> getPropertyImages(@PathVariable Long propertyId) {
        return ResponseEntity.ok(imageService.getImagesForProperty(propertyId));
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
