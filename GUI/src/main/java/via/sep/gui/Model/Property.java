package via.sep.gui.Model;

public class Property {
    private Long id;
    private String address;
    private String propertyType;
    private int bathroomNum;
    private int roomsNum;
    private String fullName;
    private int floorNum;
    private String status;
    private double size;
    private double price;

    // Constructor
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

    // Getters
    public Long getId() { return id; }
    public String getAddress() { return address; }
    public String getPropertyType() { return propertyType; }
    public int getBathroomNum() { return bathroomNum; }
    public int getRoomsNum() { return roomsNum; }
    public String getFullName() { return fullName; }
    public int getFloorNum() { return floorNum; }
    public String getStatus() { return status; }
    public double getSize() { return size; }
    public double getPrice() { return price; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setAddress(String address) { this.address = address; }
    public void setPropertyType(String propertyType) { this.propertyType = propertyType; }
    public void setBathroomNum(int bathroomNum) { this.bathroomNum = bathroomNum; }
    public void setRoomsNum(int roomsNum) { this.roomsNum = roomsNum; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setFloorNum(int floorNum) { this.floorNum = floorNum; }
    public void setStatus(String status) { this.status = status; }
    public void setSize(double size) { this.size = size; }
    public void setPrice(double price) { this.price = price; }
}