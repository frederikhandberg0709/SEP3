using RealEstateBlazor.Data.DTOs;

namespace RealEstateBlazor.Services;

public static class NotificationExtensions
{
    public static string GetDescription(this INotification notification)
    {
        return notification switch
        {
            PriceChangeNotificationDTO price =>
                $"Price changed for {price.Address} from {price.OldPrice:C} to {price.NewPrice:C}",
            PropertyNotificationDTO prop => $"Property update for {prop.Address}",
            BookmarkNotificationDTO bookmark => $"Bookmark update for {bookmark.PropertyAddress}",
            _ => "Unknown notification type"
        };
    }
}
