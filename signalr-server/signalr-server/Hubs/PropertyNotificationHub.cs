using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.SignalR;
using signalr_server.Models;
using signalr_server.Services;

namespace signalr_server.Hubs;

[Authorize(Roles = "ADMIN")]
public class PropertyNotificationHub : Hub<IPropertyNotificationClient>
{
    private readonly ILogger<PropertyNotificationHub> _logger;
    
    public PropertyNotificationHub(ILogger<PropertyNotificationHub> logger)
    {
        _logger = logger;
    }

    public async Task NotifyNewProperty(PropertyNotification notification)
    {
        await Clients.All.OnNewProperty(notification);
        _logger.LogInformation("New property notification sent: {PropertyId}", notification.PropertyId);
    }

    public async Task NotifyPropertyUpdated(PropertyUpdateNotification notification)
    {
        await Clients.All.OnPropertyUpdated(notification);
        _logger.LogInformation("Property update notification sent: {PropertyId}", notification.PropertyId);
    }

    public async Task NotifyPropertySold(PropertyNotification notification)
    {
        await Clients.All.OnPropertySold(notification);
        _logger.LogInformation("Property sold notification sent: {PropertyId}", notification.PropertyId);
    }

    public async Task NotifyPriceChange(PriceChangeNotification notification)
    {
        await Clients.All.OnPriceChanged(notification);
        _logger.LogInformation("Price change notification sent: {PropertyId}", notification.PropertyId);
    }

    public async Task NotifyStatusChange(PropertyUpdateNotification notification)
    {
        await Clients.All.OnPropertyStatusChanged(notification);
        _logger.LogInformation("Status change notification sent: {PropertyId}", notification.PropertyId);
    }

    public override async Task OnConnectedAsync()
    {
        _logger.LogInformation("Client connected: {ConnectionId}", Context.ConnectionId);
        await base.OnConnectedAsync();
    }

    public override async Task OnDisconnectedAsync(Exception exception)
    {
        _logger.LogInformation("Client disconnected: {ConnectionId}", Context.ConnectionId);
        await base.OnDisconnectedAsync(exception);
    }
}