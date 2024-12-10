package via.sep.gui.Model.domain;

import java.math.BigDecimal;

public class Property {
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

    // House-specific
    private BigDecimal lotSize;
    private Boolean hasGarage;

    // Apartment-specific
    private Integer floorNumber;
    private String buildingName;
    private Boolean hasElevator;
    private Boolean hasBalcony;
    private Object id;

    public static void updateProperty(String addressValue, String propertyTypeValue, int bathroomNumValue, int roomsNumValue, String fullNameValue, int floorNumValue, String statusValue, double sizeValue, double priceValue) {

    }

    public boolean isHouse() {
        return "House".equalsIgnoreCase(propertyType);
    }

    public boolean isApartment() {
        return "Apartment".equalsIgnoreCase(propertyType);
    }

    public Long getPropertyId() { return propertyId; }
    public void setPropertyId(Long propertyId) { this.propertyId = propertyId; }

    public String getPropertyType() { return propertyType; }
    public void setPropertyType(String propertyType) { this.propertyType = propertyType; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public BigDecimal getFloorArea() { return floorArea; }
    public void setFloorArea(BigDecimal floorArea) { this.floorArea = floorArea; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getNumBedrooms() { return numBedrooms; }
    public void setNumBedrooms(Integer numBedrooms) { this.numBedrooms = numBedrooms; }

    public Integer getNumBathrooms() { return numBathrooms; }
    public void setNumBathrooms(Integer numBathrooms) { this.numBathrooms = numBathrooms; }

    public Integer getYearBuilt() { return yearBuilt; }
    public void setYearBuilt(Integer yearBuilt) { this.yearBuilt = yearBuilt; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getNumFloors() { return numFloors; }
    public void setNumFloors(Integer numFloors) { this.numFloors = numFloors; }

    public BigDecimal getLotSize() { return lotSize; }
    public void setLotSize(BigDecimal lotSize) { this.lotSize = lotSize; }

    public Boolean getHasGarage() { return hasGarage; }
    public void setHasGarage(Boolean hasGarage) { this.hasGarage = hasGarage; }

    public Integer getFloorNumber() { return floorNumber; }
    public void setFloorNumber(Integer floorNumber) { this.floorNumber = floorNumber; }

    public String getBuildingName() { return buildingName; }
    public void setBuildingName(String buildingName) { this.buildingName = buildingName; }

    public Boolean getHasElevator() { return hasElevator; }
    public void setHasElevator(Boolean hasElevator) { this.hasElevator = hasElevator; }

    public Boolean getHasBalcony() { return hasBalcony; }
    public void setHasBalcony(Boolean hasBalcony) { this.hasBalcony = hasBalcony; }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }
}
