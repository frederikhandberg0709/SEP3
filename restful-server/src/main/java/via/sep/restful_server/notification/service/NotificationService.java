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
import via.sep.restful_server.dto.PropertyDTO;
import via.sep.restful_server.model.Forms;
import via.sep.restful_server.notification.dto.NotificationDTO;
import via.sep.restful_server.notification.dto.PriceChangeNotificationDTO;
import via.sep.restful_server.notification.dto.PropertyNotificationDTO;
import via.sep.restful_server.notification.dto.PropertyUpdateNotificationDTO;
import via.sep.restful_server.notification.mapper.NotificationMapper;
import via.sep.restful_server.service.JwtService;

import java.time.temporal.ChronoUnit;

@Service
@Slf4j
public class NotificationService {
    private final WebClient webClient;
    private final NotificationMapper notificationMapper;
    private final JwtService jwtService;

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
    public void notifyFormSubmitted(Forms form, String formId) {
        NotificationDTO notification = notificationMapper.toFormNotification(form, "SUBMITTED", formId);
        sendNotification(notification);
    }
}
