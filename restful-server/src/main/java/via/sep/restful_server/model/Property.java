package via.sep.restful_server.model;

import jakarta.persistence.*;
import lombok.Data;

import java.awt.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Data
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String description;
    private BigDecimal price;
    private Integer squareMeters;
    private Integer bedrooms;
    private Integer bathrooms;
    private String address;
    private String city;
    private String postalCode;

    @Enumerated(EnumType.STRING)
    private PropertyType type;

    @Enumerated(EnumType.STRING)
    private ListingType listingType;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
    private Set<Image> images;

    @ManyToOne
    private Agent agent;

    @OneToMany(mappedBy = "property")
    private Set<Viewing> viewings;
}
