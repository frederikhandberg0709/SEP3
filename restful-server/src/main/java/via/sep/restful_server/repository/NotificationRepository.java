package via.sep.restful_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import via.sep.restful_server.model.Notification;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByAccount_AccountIdOrderByTimestampDesc(Long accountId);
    List<Notification> findByAccount_AccountIdAndReadOrderByTimestampDesc(Long accountId, boolean read);
    long countByAccount_AccountIdAndRead(Long accountId, boolean read);
}
