package via.sep.restful_server.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FormDTO {
    private String formName;
    private String address;
    private String propertyType;
    private int floorArea;
    private BigDecimal price;
    private int bedrooms;
    private int bathrooms;
    private int yearBuilt;
    private Integer numFloors;
    private Boolean hasGarage;
    private String buildingName;
    private Integer floorNumber;
    private Boolean hasElevator;
    private Boolean hasBalcony;
}