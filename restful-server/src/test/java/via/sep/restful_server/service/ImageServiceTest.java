package via.sep.restful_server.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import via.sep.restful_server.dto.ImageDTO;
import via.sep.restful_server.model.Image;
import via.sep.restful_server.model.Property;
import via.sep.restful_server.repository.ImageRepository;
import via.sep.restful_server.repository.PropertyRepository;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {
    @Mock
    private ImageRepository imageRepository;

    @Mock
    private PropertyRepository propertyRepository;

    @InjectMocks
    private ImageService imageService;

    private Property testProperty;
    private Image testImage;
    private byte[] testImageData;
    private ImageDTO testImageDTO;

    @BeforeEach
    void setUp() {
        testProperty = new Property();
        testProperty.setPropertyId(1L);

        testImageData = "test image data".getBytes();

        testImage = new Image();
        testImage.setId(1L);
        testImage.setProperty(testProperty);
        testImage.setImageData(testImageData);

        testImageDTO = new ImageDTO();
        testImageDTO.setId(1L);
        testImageDTO.setPropertyId(1L);
        testImageDTO.setBase64ImageData(Base64.getEncoder().encodeToString(testImageData));
    }

    @Test
    void saveImage_Success() {
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(testProperty));
        when(imageRepository.save(any(Image.class))).thenReturn(testImage);

        Image savedImage = imageService.saveImage(1L, testImageData);

        assertNotNull(savedImage);
        assertEquals(testImage.getId(), savedImage.getId());
        assertEquals(testImage.getProperty(), savedImage.getProperty());
        assertArrayEquals(testImage.getImageData(), savedImage.getImageData());

        verify(propertyRepository).findById(1L);
        verify(imageRepository).save(any(Image.class));
    }

    @Test
    void saveImage_PropertyNotFound() {
        when(propertyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                imageService.saveImage(1L, testImageData)
        );

        verify(propertyRepository).findById(1L);
        verify(imageRepository, never()).save(any(Image.class));
    }

    @Test
    void getImage_Success() {
        when(imageRepository.findById(1L)).thenReturn(Optional.of(testImage));

        ImageDTO result = imageService.getImage(1L);

        assertNotNull(result);
        assertEquals(testImage.getId(), result.getId());
        assertEquals(testImage.getProperty().getPropertyId(), result.getPropertyId());
        assertEquals(
                Base64.getEncoder().encodeToString(testImage.getImageData()),
                result.getBase64ImageData()
        );

        verify(imageRepository).findById(1L);
    }

    @Test
    void getImage_NotFound() {
        when(imageRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                imageService.getImage(1L)
        );

        verify(imageRepository).findById(1L);
    }

    @Test
    void getImagesForProperty_Success() {
        List<Image> images = Arrays.asList(testImage);
        when(imageRepository.findByProperty_PropertyId(1L)).thenReturn(images);

        List<ImageDTO> results = imageService.getImagesForProperty(1L);

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());

        ImageDTO firstResult = results.get(0);
        assertEquals(testImage.getId(), firstResult.getId());
        assertEquals(testImage.getProperty().getPropertyId(), firstResult.getPropertyId());
        assertEquals(
                Base64.getEncoder().encodeToString(testImage.getImageData()),
                firstResult.getBase64ImageData()
        );

        verify(imageRepository).findByProperty_PropertyId(1L);
    }

    @Test
    void getImagesForProperty_NoImages() {
        when(imageRepository.findByProperty_PropertyId(1L)).thenReturn(Arrays.asList());

        List<ImageDTO> results = imageService.getImagesForProperty(1L);

        assertNotNull(results);
        assertTrue(results.isEmpty());

        verify(imageRepository).findByProperty_PropertyId(1L);
    }

    @Test
    void deleteImage_Success() {
        imageService.deleteImage(1L);

        verify(imageRepository).deleteById(1L);
    }

    @Test
    void convertToDTO_Success() {
        when(imageRepository.findById(1L)).thenReturn(Optional.of(testImage));

        ImageDTO result = imageService.getImage(1L);

        assertNotNull(result);
        assertEquals(testImage.getId(), result.getId());
        assertEquals(testImage.getProperty().getPropertyId(), result.getPropertyId());
        String expectedBase64 = Base64.getEncoder().encodeToString(testImage.getImageData());
        assertEquals(expectedBase64, result.getBase64ImageData());
    }

    @Test
    void convertToDTO_HandlesNullImage() {
        testImage.setImageData(null);
        when(imageRepository.findById(1L)).thenReturn(Optional.of(testImage));

        assertThrows(Exception.class, () ->
                imageService.getImage(1L)
        );
    }
}
