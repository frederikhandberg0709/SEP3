namespace RealEstateBlazor.Data.DTOs;

public class BookmarkNotificationDTO
{
    public string BookmarkId { get; set; }
    public string PropertyId { get; set; }
    public string AccountId { get; set; }
    public string PropertyAddress { get; set; }
    public decimal PropertyPrice { get; set; }
    public string PropertyType { get; set; }
    public Dictionary<string, object> PropertyDetails { get; set; }
    public DateTime Timestamp { get; set; }
}
