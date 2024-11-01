package via.sep.restful_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import via.sep.restful_server.model.Login;

import java.util.Optional;

public interface LoginRepository extends JpaRepository<Login, Long> {
    boolean existsByUsername(String username);
    Optional<Login> findByUsername(String username);
}
