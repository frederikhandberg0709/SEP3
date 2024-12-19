package via.sep.restful_server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import via.sep.restful_server.dto.BookmarkDTO;
import via.sep.restful_server.dto.PropertyDTO;
import via.sep.restful_server.model.Bookmark;
import via.sep.restful_server.model.Login;
import via.sep.restful_server.model.Notification;
import via.sep.restful_server.model.Property;
import via.sep.restful_server.notification.dto.NotificationDTO;
import via.sep.restful_server.notification.dto.PriceChangeNotificationDTO;
import via.sep.restful_server.notification.mapper.NotificationMapper;
import via.sep.restful_server.notification.service.NotificationService;
import via.sep.restful_server.repository.BookmarkRepository;
import via.sep.restful_server.repository.NotificationRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {
    @Mock
    private NotificationMapper notificationMapper;

    @Mock
    private JwtService jwtService;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private BookmarkRepository bookmarkRepository;

    @Mock
    private ObjectMapper objectMapper;

    private static WebClient webClient;
    private static WebClient.RequestBodyUriSpec requestBodyUriSpec;
    private static WebClient.RequestHeadersSpec<?> requestHeadersSpec;
    private static WebClient.ResponseSpec responseSpec;

    private NotificationService notificationService;

    private final String TEST_TOKEN = "test.jwt.token";

    @BeforeAll
    static void init() {
        webClient = mock(WebClient.class);
        requestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
        requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        responseSpec = mock(WebClient.ResponseSpec.class);

        doReturn(requestBodyUriSpec).when(webClient).post();
        doReturn(requestBodyUriSpec).when(requestBodyUriSpec).uri(any(String.class));
        doReturn(requestBodyUriSpec).when(requestBodyUriSpec).header(any(String.class), any(String.class));
        doReturn(requestHeadersSpec).when(requestBodyUriSpec).bodyValue(any());
        doReturn(responseSpec).when(requestHeadersSpec).retrieve();
        doReturn(Mono.empty()).when(responseSpec).toBodilessEntity();
    }

    @BeforeEach
    void setUp() {
        notificationService = new NotificationService(
                "http://test-url",
                notificationMapper,
                jwtService
        );

        ReflectionTestUtils.setField(notificationService, "notificationRepository", notificationRepository);
        ReflectionTestUtils.setField(notificationService, "bookmarkRepository", bookmarkRepository);
        ReflectionTestUtils.setField(notificationService, "objectMapper", objectMapper);
        ReflectionTestUtils.setField(notificationService, "webClient", webClient);
    }

    @Test
    void getNotificationHistory_Success() {
        Long accountId = 1L;

        Notification notification1 = new Notification();
        notification1.setAccountId(accountId);
        notification1.setType("TEST");
        notification1.setMessage("Test message 1");
        notification1.setTimestamp(LocalDateTime.now());

        Notification notification2 = new Notification();
        notification2.setAccountId(accountId);
        notification2.setType("TEST");
        notification2.setMessage("Test message 2");
        notification2.setTimestamp(LocalDateTime.now());

        List<Notification> expectedNotifications = Arrays.asList(notification1, notification2);

        when(notificationRepository.findByAccountIdOrderByTimestampDesc(accountId))
                .thenReturn(expectedNotifications);

        List<Notification> result = notificationService.getNotificationHistory(accountId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(accountId, result.get(0).getAccountId());
        assertEquals(accountId, result.get(1).getAccountId());
    }

    @Test
    void notifyPropertyCreated_Success() throws Exception {
        PropertyDTO propertyDTO = new PropertyDTO();
        propertyDTO.setAddress("123 Test St");
        String propertyId = "1";
        NotificationDTO expectedNotification = new NotificationDTO(
                "PROPERTY",
                "CREATED",
                propertyId,
                LocalDateTime.now(),
                propertyDTO
        );

        when(notificationMapper.toPropertyNotification(propertyDTO, "CREATED", propertyId))
                .thenReturn(expectedNotification);

        notificationService.notifyPropertyCreated(propertyDTO, propertyId);

        verify(notificationMapper).toPropertyNotification(propertyDTO, "CREATED", propertyId);
        verify(jwtService).generateToken("system", "ADMIN", 0L);
    }

    @Test
    void notifyPriceChange_Success() throws Exception {
        PriceChangeNotificationDTO priceChangeDTO = new PriceChangeNotificationDTO(
                "1",
                "123 Test St",
                BigDecimal.valueOf(100000),
                BigDecimal.valueOf(110000),
                LocalDateTime.now()
        );

        when(jwtService.generateToken(eq("system"), eq("ADMIN"), eq(0L)))
                .thenReturn(TEST_TOKEN);

        Login account = new Login();
        account.setAccountId(1L);

        Bookmark bookmark = new Bookmark();
        bookmark.setAccount(account);
        bookmark.setProperty(new Property());

        when(bookmarkRepository.findByProperty_PropertyId(1L))
                .thenReturn(Arrays.asList(bookmark));
        when(objectMapper.writeValueAsString(any()))
                .thenReturn("{}");

        notificationService.notifyPriceChange(priceChangeDTO);

        verify(bookmarkRepository).findByProperty_PropertyId(1L);
        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void notifyBookmarkCreated_Success() {
        BookmarkDTO bookmarkDTO = new BookmarkDTO();
        PropertyDTO propertyDTO = new PropertyDTO();
        String bookmarkId = "1";
        NotificationDTO expectedNotification = new NotificationDTO(
                "BOOKMARK",
                "CREATED",
                bookmarkId,
                LocalDateTime.now(),
                bookmarkDTO
        );

        when(notificationMapper.toBookmarkNotification(bookmarkDTO, propertyDTO, "CREATED", bookmarkId))
                .thenReturn(expectedNotification);

        notificationService.notifyBookmarkCreated(bookmarkDTO, propertyDTO, bookmarkId);

        verify(notificationMapper).toBookmarkNotification(bookmarkDTO, propertyDTO, "CREATED", bookmarkId);
        verify(jwtService).generateToken("system", "ADMIN", 0L);
    }

    @Test
    void notifyBookmarkDeleted_Success() {
        BookmarkDTO bookmarkDTO = new BookmarkDTO();
        PropertyDTO propertyDTO = new PropertyDTO();
        String bookmarkId = "1";
        NotificationDTO expectedNotification = new NotificationDTO(
                "BOOKMARK",
                "DELETED",
                bookmarkId,
                LocalDateTime.now(),
                bookmarkDTO
        );

        when(notificationMapper.toBookmarkNotification(bookmarkDTO, propertyDTO, "DELETED", bookmarkId))
                .thenReturn(expectedNotification);

        notificationService.notifyBookmarkDeleted(bookmarkDTO, propertyDTO, bookmarkId);

        verify(notificationMapper).toBookmarkNotification(bookmarkDTO, propertyDTO, "DELETED", bookmarkId);
        verify(jwtService).generateToken("system", "ADMIN", 0L);
    }
}
