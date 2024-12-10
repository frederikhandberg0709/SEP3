using RealEstateBlazor.Data.DTOs;

namespace RealEstateBlazor.Services;

public static class NotificationExtensions
{
    public static string GetDescription(this INotification notification)
    {
        return notification switch
        {
            PropertyNotificationDTO prop => $"Property update for {prop.Address}",
            BookmarkNotificationDTO bookmark => $"Bookmark update for {bookmark.PropertyAddress}",
            PriceChangeNotificationDTO price =>
                $"Price changed for {price.Address} from {price.OldPrice:C} to {price.NewPrice:C}",
            _ => "Unknown notification type"
        };
    }
}
