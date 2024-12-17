/*package via.sep.restful_server.notification.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import via.sep.restful_server.dto.AgentDTO;
import via.sep.restful_server.dto.BookingDTO;
import via.sep.restful_server.dto.BookmarkDTO;
import via.sep.restful_server.dto.PropertyDTO;
//import via.sep.restful_server.model.Forms;
import via.sep.restful_server.model.Bookmark;
import via.sep.restful_server.model.Notification;
import via.sep.restful_server.notification.dto.NotificationDTO;
import via.sep.restful_server.notification.dto.PriceChangeNotificationDTO;
import via.sep.restful_server.notification.dto.PropertyNotificationDTO;
import via.sep.restful_server.notification.dto.PropertyUpdateNotificationDTO;
import via.sep.restful_server.notification.mapper.NotificationMapper;
import via.sep.restful_server.repository.BookmarkRepository;
import via.sep.restful_server.repository.NotificationRepository;
import via.sep.restful_server.service.JwtService;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Slf4j
public class NotificationService {
    private final WebClient webClient;
    private final NotificationMapper notificationMapper;
    private final JwtService jwtService;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private BookmarkRepository bookmarkRepository;
    @Autowired
    private ObjectMapper objectMapper;

    public NotificationService(
            @Value("${notification.server.url}") String notificationBaseUrl,
            NotificationMapper notificationMapper,
            JwtService jwtService
    ) {
        this.notificationMapper = notificationMapper;
        this.webClient = WebClient.builder()
                .baseUrl(notificationBaseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.jwtService = jwtService;
    }

    public List<Notification> getNotificationHistory(Long accountId) {
        return notificationRepository.findByAccountIdOrderByTimestampDesc(accountId);
    }

    public void notifyPropertyCreated(PropertyDTO propertyDTO, String propertyId) {
        NotificationDTO notification = notificationMapper
                .toPropertyNotification(propertyDTO, "CREATED", propertyId);
        sendNotification(notification);
    }

    public void notifyPropertyUpdated(PropertyUpdateNotificationDTO notificationData) {
        NotificationDTO notification = new NotificationDTO(
                "PROPERTY",
                "UPDATED",
                notificationData.getPropertyId(),
                notificationData.getTimestamp(),
                notificationData
        );
        sendNotification(notification);
    }

    public void notifyBookingCreated(BookingDTO bookingDTO, String bookingId) {
        NotificationDTO notification = notificationMapper
                .toBookingNotification(bookingDTO, "CREATED", bookingId);
        sendNotification(notification);
    }

    public void notifyAgentCreated(AgentDTO agentDTO, String agentId) {
        NotificationDTO notification = notificationMapper
                .toAgentNotification(agentDTO, "CREATED", agentId);
        sendNotification(notification);
    }

    public void notifyPriceChange(PriceChangeNotificationDTO priceChangeDTO) {
        String token = jwtService.generateToken("system", "ADMIN", 0L);

        try {
            List<Bookmark> bookmarks = bookmarkRepository.findByProperty_PropertyId(Long.parseLong(priceChangeDTO.getPropertyId()));

            for (Bookmark bookmark : bookmarks) {
                Notification notification = new Notification();
                notification.setAccountId(bookmark.getAccountId());
                notification.setType("PRICE_CHANGE");
                notification.setMessage(String.format("Price changed from %s to %s for property at %s",
                        priceChangeDTO.getOldPrice(),
                        priceChangeDTO.getNewPrice(),
                        priceChangeDTO.getAddress()));
                notification.setDetails(objectMapper.writeValueAsString(priceChangeDTO));
                notification.setTimestamp(priceChangeDTO.getTimestamp());
                notification.setRead(false);
                notification.setReferenceId(priceChangeDTO.getPropertyId());

                notificationRepository.save(notification);
            }

            Notification notification = new Notification();
            notification.setAccountId(0L);
            notification.setType("PRICE_CHANGE");
            notification.setMessage(String.format("Price changed from %s to %s for property at %s",
                    priceChangeDTO.getOldPrice(),
                    priceChangeDTO.getNewPrice(),
                    priceChangeDTO.getAddress()));
            notification.setDetails(objectMapper.writeValueAsString(priceChangeDTO));
            notification.setTimestamp(priceChangeDTO.getTimestamp());
            notification.setRead(false);
            notification.setReferenceId(priceChangeDTO.getPropertyId());

            priceChangeDTO.setTimestamp(priceChangeDTO.getTimestamp().truncatedTo(ChronoUnit.SECONDS));

            log.debug("Sending notification: {}", priceChangeDTO);

            webClient.post()
                    .uri("/api/notification/property/price-change")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .bodyValue(priceChangeDTO)
                    .retrieve()
                    .toBodilessEntity()
                    .subscribe(
                            response -> log.info("Price change notification sent successfully"),
                            error -> {
                                log.error("Failed to send price change notification: {} - Body: {}",
                                        error.getMessage(),
                                        error instanceof WebClientResponseException ?
                                                ((WebClientResponseException) error).getResponseBodyAsString() : "No response body");
                            }
                    );
        } catch (Exception e) {
            log.error("Error sending price change notification", e);
        }
    }

    public void notifyBookmarkCreated(BookmarkDTO bookmarkDTO, PropertyDTO propertyDTO, String bookmarkId) {
        NotificationDTO notification = notificationMapper
                .toBookmarkNotification(bookmarkDTO, propertyDTO, "CREATED", bookmarkId);
        sendNotification(notification);
    }

    public void notifyBookmarkDeleted(BookmarkDTO bookmarkDTO, PropertyDTO propertyDTO, String bookmarkId) {
        NotificationDTO notification = notificationMapper
                .toBookmarkNotification(bookmarkDTO, propertyDTO, "DELETED", bookmarkId);
        sendNotification(notification);
    }

    private void sendNotification(NotificationDTO notification) {
        String token = jwtService.generateToken("system", "ADMIN", 0L);

        String action = notification.getAction().toLowerCase().replace("_", "-");
        String endpoint = String.format("/api/notification/%s/%s",
                notification.getType().toLowerCase(), action);

        log.info("Sending notification to endpoint: {}", endpoint);
        log.info("Notification data: {}", notification);

        try {
            webClient.post()
                    .uri(endpoint)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .bodyValue(notification.getData())
                    .retrieve()
                    .toBodilessEntity()
                    .subscribe(
                            response -> log.info("Notification sent successfully: {}", notification.getType()),
                            error -> log.error("Failed to send notification: {}", error.getMessage())
                    );
        } catch (Exception e) {
            log.error("Failed to send notification", e);
        }
    }
}
*/