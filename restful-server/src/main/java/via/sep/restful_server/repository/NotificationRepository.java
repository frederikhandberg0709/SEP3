package via.sep.restful_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import via.sep.restful_server.model.Notification;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByAccountIdOrderByTimestampDesc(Long accountId);
    List<Notification> findByReferenceIdOrderByTimestampDesc(String referenceId);
}
