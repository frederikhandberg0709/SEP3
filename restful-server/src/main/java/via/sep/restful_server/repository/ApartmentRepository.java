package via.sep.restful_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import via.sep.restful_server.model.Apartment;

import java.util.Optional;

public interface ApartmentRepository extends JpaRepository<Apartment, Long> {
    @Query("SELECT a FROM Apartment a JOIN FETCH a.property p WHERE p.propertyId = :propertyId")
    Optional<Apartment> findByProperty_PropertyId(@Param("propertyId") Long propertyId);
}
