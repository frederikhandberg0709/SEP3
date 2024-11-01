package via.sep.restful_server.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "houses", schema = "properties")
@Data
public class House {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "house_id")
    private Long houseId;

    @OneToOne
    @JoinColumn(name = "property_id")
    private Property property;

    @Column(name = "lot_size", precision = 10, scale = 2)
    private BigDecimal lotSize;

    @Column(name = "has_garage")
    private Boolean hasGarage = false;

    @Column(name = "num_floors")
    private Integer numFloors;
}
