package via.sep.restful_server.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(targetEntity = Image.class, mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Image> images = new ArrayList<>();

    public void addImage(Image image) {
        images.add(image);
        image.setProperty(this);
    }

    public void removeImage(Image image) {
        images.remove(image);
        image.setProperty(null);
    }
}
