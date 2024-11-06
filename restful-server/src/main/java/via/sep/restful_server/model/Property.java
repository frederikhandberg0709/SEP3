package via.sep.restful_server.model;

import jakarta.persistence.*;
import lombok.Data;

import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "properties", schema = "properties")
@Data
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "property_id")
    private Long propertyId;

    @Column(name = "property_type", nullable = false, length = 20)
    private String propertyType;

    @Column(nullable = false)
    private String address;

    @Column(name = "floor_area", nullable = false, precision = 10, scale = 2)
    private BigDecimal floorArea;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal price;

    @Column(name = "num_bedrooms")
    private Integer numBedrooms;

    @Column(name = "num_bathrooms")
    private Integer numBathrooms;

    @Column(name = "year_built")
    private Integer yearBuilt;

    private String description;
}
