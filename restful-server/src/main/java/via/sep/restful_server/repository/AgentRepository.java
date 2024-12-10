package via.sep.restful_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import via.sep.restful_server.model.Agent;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {
    Optional<Agent> findByName(String name);

    @Query("SELECT a FROM Agent a WHERE a NOT IN " +
            "(SELECT b.agent FROM Booking b WHERE b.bookingDate = :date)")
    List<Agent> findAvailableAgentsByDate(@Param("date") LocalDate date);
}