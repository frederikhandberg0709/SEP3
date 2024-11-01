package via.sep.restful_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import via.sep.restful_server.model.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

}
