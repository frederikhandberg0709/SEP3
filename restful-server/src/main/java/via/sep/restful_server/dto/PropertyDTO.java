package via.sep.restful_server.dto;

import lombok.Data;

import java.math.BigDecimal;

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
    private Integer numFloors;
    private BigDecimal lotSize;
    private Boolean hasGarage;

    // Apartment-specific
    private Integer floorNumber;
    private String buildingName;
    private Boolean hasElevator;
    private Boolean hasBalcony;
}
