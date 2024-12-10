using RealEstateBlazor.Services;

namespace RealEstateBlazor.Data.DTOs;

public class PriceChangeNotificationDTO : INotification
{
    public string PropertyId { get; set; }
    public string Address { get; set; }
    public decimal OldPrice { get; set; }
    public decimal NewPrice { get; set; }
    public DateTime Timestamp { get; set; }
}