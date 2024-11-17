using Microsoft.AspNetCore.SignalR;
using signalr_server.Hubs;
using signalr_server.Models;

namespace signalr_server.Services;

public class PropertyNotificationService : IPropertyNotificationService
{
    private readonly IHubContext<PropertyNotificationHub, IPropertyNotificationClient> _hubContext;
    private readonly ILogger<PropertyNotificationService> _logger;

    public PropertyNotificationService(
        IHubContext<PropertyNotificationHub, IPropertyNotificationClient> hubContext,
        ILogger<PropertyNotificationService> logger)
    {
        _hubContext = hubContext;
        _logger = logger;
    }

    public async Task NotifyPropertyCreatedAsync(PropertyNotification notification)
    {
        await _hubContext.Clients.All.OnNewProperty(notification);
        _logger.LogInformation("Property created notification sent: {PropertyId}", notification.PropertyId);
    }

    public async Task NotifyPropertyUpdatedAsync(PropertyUpdateNotification notification)
    {
        await _hubContext.Clients.All.OnPropertyUpdated(notification);
        _logger.LogInformation("Property updated notification sent: {PropertyId}", notification.PropertyId);
    }
    
    public async Task NotifyPropertySoldAsync(PropertyNotification notification)
    {
        await _hubContext.Clients.All.OnPropertySold(notification);
        _logger.LogInformation("Property sold notification sent: {PropertyId}", notification.PropertyId);
    }

    public async Task NotifyPriceChangeAsync(PriceChangeNotification notification)
    {
        await _hubContext.Clients.All.OnPriceChanged(notification);
        _logger.LogInformation("Price change notification sent: {PropertyId}", notification.PropertyId);
    }

    public async Task NotifyStatusChangeAsync(PropertyUpdateNotification notification)
    {
        await _hubContext.Clients.All.OnPropertyStatusChanged(notification);
        _logger.LogInformation("Status change notification sent: {PropertyId}", notification.PropertyId);
    }
}