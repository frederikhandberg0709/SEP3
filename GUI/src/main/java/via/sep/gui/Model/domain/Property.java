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
}
