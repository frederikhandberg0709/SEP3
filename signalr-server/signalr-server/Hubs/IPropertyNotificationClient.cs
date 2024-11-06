using signalr_server.Models;

namespace signalr_server.Hubs;

public interface IPropertyNotificationClient
{
    Task OnNewProperty(PropertyNotification notification);
    Task OnPropertyUpdated(PropertyUpdateNotification notification);
    Task OnPropertySold(PropertyNotification notification);
    Task OnPriceChanged(PriceChangeNotification notification);
    Task OnPropertyStatusChanged(PropertyUpdateNotification notification);
    Task OnPropertyDeleted(PropertyDeletedNotification notification);
}
