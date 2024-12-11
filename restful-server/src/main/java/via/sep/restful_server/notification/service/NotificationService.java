package via.sep.restful_server.notification.service;

import lombok.extern.slf4j.Slf4j;
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
import via.sep.restful_server.model.Login;
import via.sep.restful_server.model.Notification;
import via.sep.restful_server.notification.dto.NotificationDTO;
import via.sep.restful_server.notification.dto.PriceChangeNotificationDTO;
import via.sep.restful_server.notification.dto.PropertyNotificationDTO;
import via.sep.restful_server.notification.dto.PropertyUpdateNotificationDTO;
import via.sep.restful_server.notification.mapper.NotificationMapper;
import via.sep.restful_server.repository.BookmarkRepository;
import via.sep.restful_server.repository.LoginRepository;
import via.sep.restful_server.repository.NotificationRepository;
import via.sep.restful_server.service.JwtService;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NotificationService {
    private final WebClient webClient;
    private final NotificationMapper notificationMapper;
    private final JwtService jwtService;
    private final NotificationRepository notificationRepository;
    private final LoginRepository loginRepository;
    private final BookmarkRepository bookmarkRepository;

    public NotificationService(
            @Value("${notification.server.url}") String notificationBaseUrl,
            NotificationMapper notificationMapper,
            JwtService jwtService,
            NotificationRepository notificationRepository,
            LoginRepository loginRepository,
            BookmarkRepository bookmarkRepository
    ) {
        this.notificationMapper = notificationMapper;
        this.webClient = WebClient.builder()
                .baseUrl(notificationBaseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.jwtService = jwtService;
        this.notificationRepository = notificationRepository;
        this.loginRepository = loginRepository;
        this.bookmarkRepository = bookmarkRepository;
    }

    public void notifyPropertyUpdated(PropertyUpdateNotificationDTO notificationData) {
        String token = jwtService.generateToken("system", "ADMIN", 0L);

        // Get all users who have bookmarked this property
//        List<Login> interestedUsers = bookmarkRepository.findByProperty_PropertyId(
//                        Long.parseLong(notificationData.getPropertyId()))
//                .stream()
//                .map(Bookmark::getAccount)
//                .distinct()
//                .collect(Collectors.toList());
//
//        // For each interested user, create and save notification
//        for (Login user : interestedUsers) {
//            // Save to database
//            Notification notification = new Notification();
//            notification.setAccount(user);
//            notification.setType("PROPERTY_UPDATE");
//            notification.setMessage("Property " + notificationData.getAddress() + " has been updated");
//            notification.setDetails(notificationData.getUpdatedFields());
//            notification.setTimestamp(notificationData.getTimestamp());
//            notification.setReferenceId(notificationData.getPropertyId());
//            notification.setRead(false);
//
//            Notification savedNotification = notificationRepository.save(notification);
//
//            // Send through SignalR
//            try {
//                webClient.post()
//                        .uri("/api/notification/property/updated")
//                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
//                        .bodyValue(notificationData)
//                        .retrieve()
//                        .toBodilessEntity()
//                        .subscribe(
//                                response -> log.info("Property update notification sent successfully"),
//                                error -> log.error("Failed to send notification: {}", error.getMessage())
//                        );
//            } catch (Exception e) {
//                log.error("Error sending notification", e);
//            }
//        }
    }

    // Similar pattern for price changes
    public void notifyPriceChange(PriceChangeNotificationDTO priceChangeDTO) {
        String token = jwtService.generateToken("system", "ADMIN", 0L);

        // Get interested users
        List<Login> interestedUsers = bookmarkRepository.findByProperty_PropertyId(
                        Long.parseLong(priceChangeDTO.getPropertyId()))
                .stream()
                .map(Bookmark::getAccount)
                .distinct()
                .collect(Collectors.toList());

        for (Login user : interestedUsers) {
            // Save to database
            Notification notification = new Notification();
            notification.setAccount(user);
            notification.setType("PRICE_CHANGE");
            notification.setMessage("Price changed for property " + priceChangeDTO.getAddress() +
                    " from " + priceChangeDTO.getOldPrice() + " to " + priceChangeDTO.getNewPrice());
            notification.setDetails(Map.of(
                    "oldPrice", priceChangeDTO.getOldPrice(),
                    "newPrice", priceChangeDTO.getNewPrice()
            ));
            notification.setTimestamp(priceChangeDTO.getTimestamp());
            notification.setReferenceId(priceChangeDTO.getPropertyId());
            notification.setRead(false);

            Notification savedNotification = notificationRepository.save(notification);

            // Send through SignalR
            try {
                webClient.post()
                        .uri("/api/notification/property/price-change")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .bodyValue(priceChangeDTO)
                        .retrieve()
                        .toBodilessEntity()
                        .subscribe(
                                response -> log.info("Price change notification sent successfully"),
                                error -> log.error("Failed to send notification: {}", error.getMessage())
                        );
            } catch (Exception e) {
                log.error("Error sending notification", e);
            }
        }
    }

//    private final WebClient webClient;
//    private final NotificationMapper notificationMapper;
//    private final JwtService jwtService;
//
//    public NotificationService(
//            @Value("${notification.server.url}") String notificationBaseUrl,
//            NotificationMapper notificationMapper,
//            JwtService jwtService
//    ) {
//        this.notificationMapper = notificationMapper;
//        this.webClient = WebClient.builder()
//                .baseUrl(notificationBaseUrl)
//                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .build();
//        this.jwtService = jwtService;
//    }
//
//    public void notifyPropertyCreated(PropertyDTO propertyDTO, String propertyId) {
//        NotificationDTO notification = notificationMapper
//                .toPropertyNotification(propertyDTO, "CREATED", propertyId);
//        sendNotification(notification);
//    }
//
//    public void notifyPropertyUpdated(PropertyUpdateNotificationDTO notificationData) {
//        NotificationDTO notification = new NotificationDTO(
//                "PROPERTY",
//                "UPDATED",
//                notificationData.getPropertyId(),
//                notificationData.getTimestamp(),
//                notificationData
//        );
//        sendNotification(notification);
//    }
//
//    public void notifyBookingCreated(BookingDTO bookingDTO, String bookingId) {
//        NotificationDTO notification = notificationMapper
//                .toBookingNotification(bookingDTO, "CREATED", bookingId);
//        sendNotification(notification);
//    }
//
//    public void notifyAgentCreated(AgentDTO agentDTO, String agentId) {
//        NotificationDTO notification = notificationMapper
//                .toAgentNotification(agentDTO, "CREATED", agentId);
//        sendNotification(notification);
//    }
//
//    public void notifyPriceChange(PriceChangeNotificationDTO priceChangeDTO) {
//        String token = jwtService.generateToken("system", "ADMIN", 0L);
//
//        try {
//            priceChangeDTO.setTimestamp(priceChangeDTO.getTimestamp().truncatedTo(ChronoUnit.SECONDS));
//
//            log.debug("Sending notification: {}", priceChangeDTO);
//
//            webClient.post()
//                    .uri("/api/notification/property/price-change")
//                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
//                    .bodyValue(priceChangeDTO)
//                    .retrieve()
//                    .toBodilessEntity()
//                    .subscribe(
//                            response -> log.info("Price change notification sent successfully"),
//                            error -> {
//                                log.error("Failed to send price change notification: {} - Body: {}",
//                                        error.getMessage(),
//                                        error instanceof WebClientResponseException ?
//                                                ((WebClientResponseException) error).getResponseBodyAsString() : "No response body");
//                            }
//                    );
//        } catch (Exception e) {
//            log.error("Error sending price change notification", e);
//        }
//    }
//
//    public void notifyBookmarkCreated(BookmarkDTO bookmarkDTO, PropertyDTO propertyDTO, String bookmarkId) {
//        NotificationDTO notification = notificationMapper
//                .toBookmarkNotification(bookmarkDTO, propertyDTO, "CREATED", bookmarkId);
//        sendNotification(notification);
//    }
//
//    public void notifyBookmarkDeleted(BookmarkDTO bookmarkDTO, PropertyDTO propertyDTO, String bookmarkId) {
//        NotificationDTO notification = notificationMapper
//                .toBookmarkNotification(bookmarkDTO, propertyDTO, "DELETED", bookmarkId);
//        sendNotification(notification);
//    }
//
//    private void sendNotification(NotificationDTO notification) {
//        String token = jwtService.generateToken("system", "ADMIN", 0L);
//
//        String action = notification.getAction().toLowerCase().replace("_", "-");
//        String endpoint = String.format("/api/notification/%s/%s",
//                notification.getType().toLowerCase(), action);
//
//        log.info("Sending notification to endpoint: {}", endpoint);
//        log.info("Notification data: {}", notification);
//
//        try {
//            webClient.post()
//                    .uri(endpoint)
//                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
//                    .bodyValue(notification.getData())
//                    .retrieve()
//                    .toBodilessEntity()
//                    .subscribe(
//                            response -> log.info("Notification sent successfully: {}", notification.getType()),
//                            error -> log.error("Failed to send notification: {}", error.getMessage())
//                    );
//        } catch (Exception e) {
//            log.error("Failed to send notification", e);
//        }
//    }
//    public void notifyFormSubmitted(Forms form, String formId) {
//        NotificationDTO notification = notificationMapper.toFormNotification(form, "SUBMITTED", formId);
//        sendNotification(notification);
//    }
}
