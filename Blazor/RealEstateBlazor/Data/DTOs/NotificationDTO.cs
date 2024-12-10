namespace RealEstateBlazor.Data.DTOs;

public class NotificationDTO
{
    public string Type { get; set; }
    public string Action { get; set; }
    public string Id { get; set; }
    public DateTime Timestamp { get; set; }
    public object Data { get; set; }
}
