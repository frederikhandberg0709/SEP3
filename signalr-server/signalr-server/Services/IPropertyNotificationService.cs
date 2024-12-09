using signalr_server.Models;

namespace signalr_server.Services;

public interface IPropertyNotificationService
{
    Task NotifyPropertyCreatedAsync(PropertyNotification notification);
    Task NotifyPropertyUpdatedAsync(PropertyUpdateNotification notification);
    Task NotifyPropertySoldAsync(PropertyNotification notification);
    Task NotifyPriceChangeAsync(PriceChangeNotification notification);
    Task NotifyStatusChangeAsync(PropertyUpdateNotification notification);
}
