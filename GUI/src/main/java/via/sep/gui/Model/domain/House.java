package via.sep.gui.Model.domain;

import java.math.BigDecimal;

public class House {
    private Long houseId;
    private Property property;
    private BigDecimal lotSize;
    private Boolean hasGarage;
    private Integer numFloors;

    public Long getHouseId() { return houseId; }

    public void setHouseId(Long houseId) { this.houseId = houseId; }

    public Property getProperty() { return property; }

    public void setProperty(Property property) { this.property = property; }

    public BigDecimal getLotSize() { return lotSize; }

    public void setLotSize(BigDecimal lotSize) { this.lotSize = lotSize; }

    public Boolean getHasGarage() { return hasGarage; }

    public void setHasGarage(Boolean hasGarage) { this.hasGarage = hasGarage; }

    public Integer getNumFloors() { return numFloors; }

    public void setNumFloors(Integer numFloors) { this.numFloors = numFloors; }
}
