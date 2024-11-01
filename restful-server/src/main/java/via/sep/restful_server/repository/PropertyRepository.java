package via.sep.restful_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import via.sep.restful_server.model.Property;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    List<Property> findByPropertyType(String propertyType);
    List<Property> findByPriceLessThanEqual(BigDecimal maxPrice);
    List<Property> findByNumBedroomsGreaterThanEqual(Integer minBedrooms);
}
