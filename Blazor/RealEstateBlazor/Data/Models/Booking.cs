namespace RealEstateBlazor.Data.Models;

public class Booking
{
    public long Id { get; set; }
    public string Username { get; set; }
    public string Email { get; set; }
    public string Description { get; set; }
    public DateTime Date { get; set; }
    public long AgentId { get; set; }
    public Agent Agent { get; set; }
}
