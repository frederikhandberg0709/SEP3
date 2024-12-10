namespace RealEstateBlazor.Data.Models;

public class Agent
{
    public long Id { get; set; }
    public string Name { get; set; }
    public string ContactInfo { get; set; }
    public List<Booking> Bookings { get; set; } = new();
}
