using RealEstateBlazor.Services;

namespace RealEstateBlazor.Data.DTOs;

public class PropertyNotificationDTO : INotification
{
    public string PropertyId { get; set; }
    public string Address { get; set; }
    public decimal Price { get; set; }
    public string PropertyType { get; set; }
    public Dictionary<string, object> Details { get; set; }
    public DateTime Timestamp { get; set; }
}
