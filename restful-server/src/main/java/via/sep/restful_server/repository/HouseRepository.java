package via.sep.restful_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import via.sep.restful_server.model.House;

import java.util.Optional;

@Repository
public interface HouseRepository extends JpaRepository<House, Long> {
    @Query("SELECT h FROM House h JOIN FETCH h.property p WHERE p.propertyId = :propertyId")
    Optional<House> findByProperty_PropertyId(@Param("propertyId") Long propertyId);
}
