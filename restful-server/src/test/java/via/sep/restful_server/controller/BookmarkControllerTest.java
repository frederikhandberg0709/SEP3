package via.sep.restful_server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;
import via.sep.restful_server.dto.BookmarkDTO;
import via.sep.restful_server.dto.CreateBookmarkDTO;
import via.sep.restful_server.model.Bookmark;
import via.sep.restful_server.model.Login;
import via.sep.restful_server.model.Property;
import via.sep.restful_server.model.UserProfile;
import via.sep.restful_server.notification.service.NotificationService;
import via.sep.restful_server.repository.BookmarkRepository;
import via.sep.restful_server.repository.PropertyRepository;
import via.sep.restful_server.service.BookmarkService;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BookmarkControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookmarkRepository bookmarkRepository;

    @MockBean
    private PropertyRepository propertyRepository;

    @MockBean
    private BookmarkService bookmarkService;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private JwtDecoder jwtDecoder;

    private Login testLogin;
    private UserProfile testUserProfile;
    private Bookmark testBookmark;
    private BookmarkDTO testBookmarkDTO;
    private Property testProperty;
    private String userToken;
    private Long testAccountId = 1L;

    @BeforeEach
    void setup() {
        testLogin = new Login();
        testLogin.setAccountId(testAccountId);
        testLogin.setUsername("testuser");
        testLogin.setRole("USER");

        testUserProfile = new UserProfile();
        testUserProfile.setLogin(testLogin);
        testUserProfile.setFullName("Test User");
        testUserProfile.setEmail("test@example.com");
        testLogin.setUserProfile(testUserProfile);

        testProperty = new Property();
        testProperty.setPropertyId(1L);
        testProperty.setPropertyType("Apartment");
        testProperty.setAddress("123 Test St");
        testProperty.setFloorArea(BigDecimal.valueOf(100.0));
        testProperty.setPrice(BigDecimal.valueOf(200000.0));
        testProperty.setNumBedrooms(2);
        testProperty.setNumBathrooms(1);
        testProperty.setYearBuilt(2020);
        testProperty.setDescription("Test Property");

        testBookmark = new Bookmark();
        testBookmark.setBookmarkId(1L);
        testBookmark.setProperty(testProperty);
        testBookmark.setAccount(testLogin);

        testBookmarkDTO = new BookmarkDTO(testBookmark);

        userToken = "user.token";
        Jwt jwt = Jwt.withTokenValue(userToken)
                .header("alg", "HS256")
                .subject("user")
                .claim("accountId", testAccountId)
                .claim("role", "USER")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        when(jwtDecoder.decode(anyString())).thenReturn(jwt);
    }

    @Test
    void createBookmark_Success() throws Exception {
        CreateBookmarkDTO createDTO = new CreateBookmarkDTO();
        createDTO.setPropertyId(testProperty.getPropertyId());

        when(bookmarkService.hasBookmarked(testAccountId, testProperty.getPropertyId()))
                .thenReturn(false);
        when(bookmarkService.createBookmark(testProperty.getPropertyId(), testAccountId))
                .thenReturn(testBookmarkDTO);
        when(propertyRepository.findById(testProperty.getPropertyId()))
                .thenReturn(Optional.of(testProperty));

        mockMvc.perform(post("/api/bookmarks")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookmarkId").value(testBookmark.getBookmarkId()));

        verify(notificationService).notifyBookmarkCreated(any(), any(), any());
    }

    @Test
    void createBookmark_AlreadyExists() throws Exception {
        CreateBookmarkDTO createDTO = new CreateBookmarkDTO();
        createDTO.setPropertyId(testProperty.getPropertyId());

        when(bookmarkService.hasBookmarked(testAccountId, testProperty.getPropertyId()))
                .thenReturn(true);

        mockMvc.perform(post("/api/bookmarks")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isConflict());
    }

    @Test
    void getUserBookmarks_Success() throws Exception {
        when(bookmarkService.getBookmarksByAccountId(testAccountId))
                .thenReturn(Arrays.asList(testBookmarkDTO));

        mockMvc.perform(get("/api/bookmarks")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookmarkId").value(testBookmark.getBookmarkId()));
    }

    @Test
    void deleteBookmark_Success() throws Exception {
        when(bookmarkService.isBookmarkOwner(testBookmark.getBookmarkId(), testAccountId))
                .thenReturn(true);
        when(bookmarkRepository.findById(testBookmark.getBookmarkId()))
                .thenReturn(Optional.of(testBookmark));
        when(propertyRepository.findById(testProperty.getPropertyId()))
                .thenReturn(Optional.of(testProperty));

        mockMvc.perform(delete("/api/bookmarks/" + testBookmark.getBookmarkId())
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isNoContent());

        verify(bookmarkService).deleteBookmark(testBookmark.getBookmarkId());
        verify(notificationService).notifyBookmarkDeleted(any(), any(), any());
    }

    @Test
    void deleteBookmark_NotOwner() throws Exception {
        when(bookmarkService.isBookmarkOwner(testBookmark.getBookmarkId(), testAccountId))
                .thenReturn(false);

        mockMvc.perform(delete("/api/bookmarks/" + testBookmark.getBookmarkId())
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void hasBookmarked_Success() throws Exception {
        when(bookmarkService.hasBookmarked(testAccountId, testProperty.getPropertyId()))
                .thenReturn(true);

        mockMvc.perform(get("/api/bookmarks/property/" + testProperty.getPropertyId())
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}
