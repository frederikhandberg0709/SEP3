package via.sep.gui.Model;

public class Property {
    private final String address;
    private final String propertyType;
    private final int bathroomNum;
    private final int roomsNum;
    private final String fullName;
    private final int floorNum;
    private final String status;
    private final double size;
    private final double price;

    // Constructor, getters, and setters
    public Property(String address, String propertyType, int bathroomNum, int roomsNum, String fullName, int floorNum, String status, double size, double price) {
        this.address = address;
        this.propertyType = propertyType;
        this.bathroomNum = bathroomNum;
        this.roomsNum = roomsNum;
        this.fullName = fullName;
        this.floorNum = floorNum;
        this.status = status;
        this.size = size;
        this.price = price;
    }

    public String getAddress() { return address; }
    public String getPropertyType() { return propertyType; }
    public int getBathroomNum() { return bathroomNum; }
    public int getRoomsNum() { return roomsNum; }
    public String getFullName() { return fullName; }
    public int getFloorNum() { return floorNum; }
    public String getStatus() { return status; }
    public double getSize() { return size; }
    public double getPrice() { return price; }

    public Object getId() {
        return getId();
    }
}