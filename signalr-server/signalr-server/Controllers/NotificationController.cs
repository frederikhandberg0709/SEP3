using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using signalr_server.Models;
using signalr_server.Services;

namespace signalr_server.Controllers;

[ApiController]
[Route("api/[controller]")]
[Authorize(Roles = "ADMIN")]
public class NotificationController : ControllerBase
{
    private readonly IPropertyNotificationService _notificationService;
    private readonly ILogger<NotificationController> _logger;

    public NotificationController(
        IPropertyNotificationService notificationService,
        ILogger<NotificationController> logger)
    {
        _notificationService = notificationService;
        _logger = logger;
    }

    [HttpPost("property/created")]
    public async Task<IActionResult> NotifyPropertyCreated([FromBody] PropertyNotification notification)
    {
        try
        {
            await _notificationService.NotifyPropertyCreatedAsync(new PropertyNotification(
                PropertyId: notification.PropertyId,
                Address: notification.Address,
                Price: notification.Price,
                PropertyType: notification.PropertyType,
                Details: new Dictionary<string, object>
                {
                    { "numBedrooms", notification.Details["numBedrooms"] },
                    { "numBathrooms", notification.Details["numBathrooms"] },
                    { "floorArea", notification.Details["floorArea"] },
                    { "yearBuilt", notification.Details["yearBuilt"] }
                },
                Timestamp: DateTime.UtcNow
            ));
            
            return Ok();
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, "Failed to send property created notification");
            return StatusCode(500, "Failed to send notification");
        }
    }
    
    [HttpPost("property/updated")]
    public async Task<IActionResult> NotifyPropertyUpdated([FromBody] PropertyUpdateNotification notification)
    {
        try
        {
            await _notificationService.NotifyPropertyUpdatedAsync(notification);
            return Ok();
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, "Failed to send property update notification");
            return StatusCode(500, "Failed to send notification");
        }
    }
    
    [HttpPost("property/sold")]
    public async Task<IActionResult> NotifyPropertySold([FromBody] PropertyNotification notification)
    {
        try
        {
            await _notificationService.NotifyPropertySoldAsync(notification);
            return Ok();
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, "Failed to send property sold notification");
            return StatusCode(500, "Failed to send notification");
        }
    }

    [HttpPost("property/price-change")]
    public async Task<IActionResult> NotifyPriceChange([FromBody] PriceChangeNotification notification)
    {
        try
        {
            await _notificationService.NotifyPriceChangeAsync(notification);
            return Ok();
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, "Failed to send price change notification");
            return StatusCode(500, "Failed to send notification");
        }
    }
    
    [HttpPost("property/status-change")]
    public async Task<IActionResult> NotifyStatusChange([FromBody] PropertyUpdateNotification notification)
    {
        try
        {
            await _notificationService.NotifyStatusChangeAsync(notification);
            return Ok();
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, "Failed to send status change notification");
            return StatusCode(500, "Failed to send notification");
        }
    }
    
    [HttpPost("property/deleted")]
    public async Task<IActionResult> NotifyPropertyDeleted([FromBody] PropertyDeletedNotification notification)
    {
        try
        {
            await _notificationService.NotifyPropertyDeletedAsync(notification);
            return Ok();
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, "Failed to send property deleted notification");
            return StatusCode(500, "Failed to send notification");
        }
    }
}
