namespace RealEstateBlazor.Data.Models;

public class NotificationHistory
{
    public long NotificationId { get; set; }
    public long AccountId { get; set; }
    public string Type { get; set; }
    public string Message { get; set; }
    public string Details { get; set; }
    public DateTime Timestamp { get; set; }
    public bool Read { get; set; }
    public string ReferenceId { get; set; }
}
