/*
package via.sep.restful_server.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Forms", schema = "properties")
@Data
@NoArgsConstructor
public class Forms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long formID;
   
    @Column(nullable = false, unique = true)
    private String formName;

    @Column(name = "property_id")
    private Long propertyId;

    @Column(name = "property_type", nullable = false, length = 20)
    private String propertyType;

    @Column(nullable = false)
    private String address;

    @Column(name = "building_name", length = 100)
    private String buildingName;

    @Column(name = "floor_area", nullable = false, precision = 10, scale = 2)
    private BigDecimal floorArea;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal price;

    @Column(name = "num_bedrooms")
    private Integer numBedrooms;

    @Column(name = "num_bathrooms")
    private Integer numBathrooms;

    @Column(name = "num_floors")
    private Integer numFloors = 1;

    @Column(name = "has_garage")
    private Boolean hasGarage = false;

    @Column(name = "has_elevator")
    private Boolean hasElevator = false;

    @Column(name = "has_balcony")
    private Boolean hasBalcony = false;

    @Column(name = "year_built")
    private Integer yearBuilt;

    @OneToMany(targetEntity = Image.class, mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Image> images = new ArrayList<>();

    private String description;
}
*/
