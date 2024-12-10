package via.sep.gui.Model.dto;

import java.math.BigDecimal;

public class PropertyDTO {
    private Long propertyId;
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

    public PropertyDTO() {}

    public Long getPropertyId() { return propertyId; }
    public String getPropertyType() { return propertyType; }
    public String getAddress() { return address; }
    public BigDecimal getFloorArea() { return floorArea; }
    public BigDecimal getPrice() { return price; }
    public Integer getNumBedrooms() { return numBedrooms; }
    public Integer getNumBathrooms() { return numBathrooms; }
    public Integer getYearBuilt() { return yearBuilt; }
    public String getDescription() { return description; }
    public Integer getNumFloors() { return numFloors; }
    public BigDecimal getLotSize() { return lotSize; }
    public Boolean getHasGarage() { return hasGarage; }
    public Integer getFloorNumber() { return floorNumber; }
    public String getBuildingName() { return buildingName; }
    public Boolean getHasElevator() { return hasElevator; }
    public Boolean getHasBalcony() { return hasBalcony; }

    public void setPropertyId(Long propertyId) { this.propertyId = propertyId; }
    public void setPropertyType(String propertyType) { this.propertyType = propertyType; }
    public void setAddress(String address) { this.address = address; }
    public void setFloorArea(BigDecimal floorArea) { this.floorArea = floorArea; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public void setNumBedrooms(Integer numBedrooms) { this.numBedrooms = numBedrooms; }
    public void setNumBathrooms(Integer numBathrooms) { this.numBathrooms = numBathrooms; }
    public void setYearBuilt(Integer yearBuilt) { this.yearBuilt = yearBuilt; }
    public void setDescription(String description) { this.description = description; }
    public void setNumFloors(Integer numFloors) { this.numFloors = numFloors; }
    public void setLotSize(BigDecimal lotSize) { this.lotSize = lotSize; }
    public void setHasGarage(Boolean hasGarage) { this.hasGarage = hasGarage; }
    public void setFloorNumber(Integer floorNumber) { this.floorNumber = floorNumber; }
    public void setBuildingName(String buildingName) { this.buildingName = buildingName; }
    public void setHasElevator(Boolean hasElevator) { this.hasElevator = hasElevator; }
    public void setHasBalcony(Boolean hasBalcony) { this.hasBalcony = hasBalcony; }
}
