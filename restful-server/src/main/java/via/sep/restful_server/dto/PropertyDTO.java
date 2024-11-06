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
