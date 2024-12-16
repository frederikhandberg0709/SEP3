package via.sep.restful_server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;
import via.sep.restful_server.dto.ImageDTO;
import via.sep.restful_server.model.Image;
import via.sep.restful_server.repository.ImageRepository;
import via.sep.restful_server.service.ImageService;

import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ImageControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ImageRepository imageRepository;

    @MockBean
    private ImageService imageService;

    @MockBean
    private JwtDecoder jwtDecoder;

    private Image testImage;
    private ImageDTO testImageDTO;
    private String adminToken;

    @BeforeEach
    void setUp() {
        testImage = new Image();
        testImage.setId(1L);
        testImage.setImageData("test image data".getBytes());

        testImageDTO = new ImageDTO();
        testImageDTO.setId(1L);
        testImageDTO.setBase64ImageData(Base64.getEncoder().encodeToString("test image data".getBytes()));

        adminToken = "test.admin.token";
        Jwt jwt = Jwt.withTokenValue(adminToken)
                .header("alg", "HS256")
                .subject("admin")
                .claim("role", "ADMIN")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        when(jwtDecoder.decode(anyString())).thenReturn(jwt);
    }

    @Test
    void uploadImage_Success() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "image",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image data".getBytes()
        );

        when(imageService.saveImage(eq(1L), any(byte[].class)))
                .thenReturn(testImage);
        when(imageService.getImage(1L))
                .thenReturn(testImageDTO);

        mockMvc.perform(multipart("/api/images/property/1")
                        .file(file)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.base64ImageData").exists());
    }

    @Test
    void getImage_Succes() throws Exception {
        when(imageService.getImage(1L))
                .thenReturn(testImageDTO);

        mockMvc.perform(get("/api/images/1")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.base64ImageData").exists());
    }

    @Test
    void getImage_NotFound() throws Exception {
        when(imageService.getImage(999L)).thenReturn(null);

        mockMvc.perform(get("/api/images/999")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void getPropertyImages_Success() throws Exception {
        when(imageService.getImagesForProperty(1L))
                .thenReturn(Arrays.asList(testImageDTO));

        mockMvc.perform(get("/api/images/property/1")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].base64ImageData").exists());
    }

    @Test
    void getImageRaw_Success() throws Exception {
        when(imageRepository.findById(1L))
                .thenReturn(Optional.of(testImage));

        mockMvc.perform(get("/api/images/1/raw")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG));
    }

    @Test
    void deleteImage_Success() throws Exception {
        doNothing().when(imageService).deleteImage(1L);

        mockMvc.perform(delete("/api/images/1")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteImage_NotFound() throws Exception {
        doThrow(new RuntimeException("Image not found"))
                .when(imageService).deleteImage(999L);

        mockMvc.perform(delete("/api/images/999")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }
}
