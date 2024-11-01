package via.sep.restful_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import via.sep.restful_server.model.Apartment;

import java.util.Optional;

public interface ApartmentRepository extends JpaRepository<Apartment, Long> {
    Optional<Apartment> findByProperty_PropertyId(Long propertyId);
}
