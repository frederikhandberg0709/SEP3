using RealEstateBlazor.Data.Models;

namespace RealEstateBlazor.Services.Notifications;

public interface INotificationService
{
    Task<List<NotificationHistory>> GetNotificationHistoryAsync(long accountId);
}
