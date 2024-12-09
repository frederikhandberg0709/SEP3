package via.sep.restful_server.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "apartments", schema = "properties")
@Data
public class Apartment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "apartment_id")
    private Long apartmentId;

    @OneToOne
    @JoinColumn(name = "property_id")
    private Property property;

    @Column(name = "floor_number")
    private Integer floorNumber;

    @Column(name = "building_name", length = 100)
    private String buildingName;

    @Column(name = "has_elevator")
    private Boolean hasElevator = false;

    @Column(name = "has_balcony")
    private Boolean hasBalcony = false;

    @Column(name = "has_garage")
    private Boolean hasGarage = false;

    @Column(name = "lot_size", precision = 10, scale = 2)
    private BigDecimal lotSize;

    @Column(name = "num_floors")
    private Integer numFloors = 1;
}
