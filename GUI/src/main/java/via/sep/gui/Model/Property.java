package via.sep.gui.Model;

public class Property {
    private final String address;
    private final String clientName;
    private final String phoneNumber;
    private final String leasePeriod;
    private final String email;
    private final String propertyType;
    private final double price;

    // Constructor, getters, and setters
    public Property(String address, String clientName, String phoneNumber, String leasePeriod, String email, String propertyType, double price) {
        this.address = address;
        this.clientName = clientName;
        this.phoneNumber = phoneNumber;
        this.leasePeriod = leasePeriod;
        this.email = email;
        this.propertyType = propertyType;
        this.price = price;
    }

    public String getAddress() { return address; }
    public String getClientName() { return clientName; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getLeasePeriod() { return leasePeriod; }
    public String getEmail() { return email; }
    public String getPropertyType() { return propertyType; }
    public double getPrice() { return price; }
}
