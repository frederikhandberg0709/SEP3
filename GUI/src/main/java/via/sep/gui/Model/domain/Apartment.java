package via.sep.gui.Model.domain;

import java.math.BigDecimal;

public class Apartment {
    private Long apartmentId;
    private Property property;
    private Integer floorNumber;
    private String buildingName;
    private Boolean hasElevator = false;
    private Boolean hasBalcony = false;
    private Boolean hasGarage = false;
    private BigDecimal lotSize;
    private Integer numFloors = 1;

    public Long getApartmentId() { return apartmentId; }

    public void setApartmentId(Long apartmentId) { this.apartmentId = apartmentId; }

    public Property getProperty() { return property; }

    public void setProperty(Property property) { this.property = property; }

    public Integer getFloorNumber() { return floorNumber; }

    public void setFloorNumber(Integer floorNumber) { this.floorNumber = floorNumber; }

    public String getBuildingName() { return buildingName; }

    public void setBuildingName(String buildingName) { this.buildingName = buildingName; }

    public Boolean getHasElevator() { return hasElevator; }

    public void setHasElevator(Boolean hasElevator) { this.hasElevator = hasElevator; }

    public Boolean getHasBalcony() { return hasBalcony; }

    public void setHasBalcony(Boolean hasBalcony) { this.hasBalcony = hasBalcony; }

    public Boolean getHasGarage() { return hasGarage; }

    public void setHasGarage(Boolean hasGarage) { this.hasGarage = hasGarage; }

    public BigDecimal getLotSize() { return lotSize; }

    public void setLotSize(BigDecimal lotSize) { this.lotSize = lotSize; }

    public Integer getNumFloors() { return numFloors; }

    public void setNumFloors(Integer numFloors) { this.numFloors = numFloors; }
}
