package via.sep.restful_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import via.sep.restful_server.model.Booking;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByProperty_PropertyId(Long propertyId);
    List<Booking> findByAgent_AgentId(Long agentId);
    List<Booking> findByBookingDate(LocalDate date);
}