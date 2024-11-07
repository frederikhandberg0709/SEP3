package via.sep.restful_server.notification.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import via.sep.restful_server.dto.AgentDTO;
import via.sep.restful_server.dto.BookingDTO;
import via.sep.restful_server.dto.PropertyDTO;
import via.sep.restful_server.notification.dto.NotificationDTO;
import via.sep.restful_server.notification.mapper.NotificationMapper;

@Service
@Slf4j
public class NotificationService {
    private final WebClient webClient;
    private final NotificationMapper notificationMapper;

    public NotificationService(
            @Value("${notification.server.url}") String notificationBaseUrl,
            NotificationMapper notificationMapper
    ) {
        this.notificationMapper = notificationMapper;
        this.webClient = WebClient.builder()
                .baseUrl(notificationBaseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public void notifyPropertyCreated(PropertyDTO propertyDTO, String propertyId) {
        NotificationDTO notification = notificationMapper
                .toPropertyNotification(propertyDTO, "CREATED", propertyId);
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

    private void sendNotification(NotificationDTO notification) {
        webClient.post()
                .uri("/api/notification/" + notification.getType().toLowerCase())
                .bodyValue(notification)
                .retrieve()
                .toBodilessEntity()
                .subscribe(
                        response -> log.info("Notification sent successfully: {}", notification.getType()),
                        error -> log.error("Failed to send notification: {}", error.getMessage())
                );
    }
}
