package via.sep.restful_server.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PropertyDTO {
    private String propertyType;
    private String address;
    private BigDecimal floorArea;
    private BigDecimal price;
    private Integer numBedrooms;
    private Integer numBathrooms;
    private Integer yearBuilt;
    private String description;
    private List<Long> imageIds;

    // House
    private BigDecimal lotSize;
    private Boolean hasGarage;
    private Integer numFloors;

    // Apartment
    private Integer floorNumber;
    private String buildingName;
    private Boolean hasElevator;
    private Boolean hasBalcony;

}
