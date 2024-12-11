package via.sep.restful_server.api;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import via.sep.restful_server.dto.NotificationDTO;
import via.sep.restful_server.model.Notification;
import via.sep.restful_server.repository.LoginRepository;
import via.sep.restful_server.repository.NotificationRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationRepository notificationRepository;
    private final LoginRepository loginRepository;

    public NotificationController(NotificationRepository notificationRepository, LoginRepository loginRepository) {
        this.notificationRepository = notificationRepository;
        this.loginRepository = loginRepository;
    }

    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getUserNotifications(
            @AuthenticationPrincipal Jwt jwt) {
        Long accountId = jwt.getClaim("accountId");
        List<Notification> notifications =
                notificationRepository.findByAccount_AccountIdOrderByTimestampDesc(accountId);

        return ResponseEntity.ok(notifications.stream()
                .map(this::toDTO)
                .collect(Collectors.toList()));
    }

    @GetMapping("/unread")
    public ResponseEntity<List<Notification>> getUnreadNotifications(
            @AuthenticationPrincipal Jwt jwt) {
        Long accountId = jwt.getClaim("accountId");
        return ResponseEntity.ok(
                notificationRepository.findByAccount_AccountIdAndReadOrderByTimestampDesc(accountId, false)
        );
    }

    @GetMapping("/unread/count")
    public ResponseEntity<Long> getUnreadCount(
            @AuthenticationPrincipal Jwt jwt) {
        Long accountId = jwt.getClaim("accountId");
        return ResponseEntity.ok(
                notificationRepository.countByAccount_AccountIdAndRead(accountId, false)
        );
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(@PathVariable Long id,
                                        @AuthenticationPrincipal Jwt jwt) {
        Long accountId = jwt.getClaim("accountId");
        return notificationRepository.findById(id)
                .filter(n -> n.getAccount().getAccountId().equals(accountId))
                .map(notification -> {
                    notification.setRead(true);
                    notificationRepository.save(notification);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/read-all")
    public ResponseEntity<?> markAllAsRead(@AuthenticationPrincipal Jwt jwt) {
        Long accountId = jwt.getClaim("accountId");
        List<Notification> unreadNotifications =
                notificationRepository.findByAccount_AccountIdAndReadOrderByTimestampDesc(accountId, false);

        unreadNotifications.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(unreadNotifications);

        return ResponseEntity.ok().build();
    }

    private NotificationDTO toDTO(Notification notification) {
        NotificationDTO dto = new NotificationDTO();
        dto.setNotificationId(notification.getNotificationId());
        dto.setType(notification.getType());
        dto.setMessage(notification.getMessage());
        dto.setDetails(notification.getDetails());
        dto.setTimestamp(notification.getTimestamp());
        dto.setRead(notification.isRead());
        dto.setReferenceId(notification.getReferenceId());
        return dto;
    }
}
