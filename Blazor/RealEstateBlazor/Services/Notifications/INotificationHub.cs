using RealEstateBlazor.Data.DTOs;
using RealEstateBlazor.Data.Models;

namespace RealEstateBlazor.Services;

public interface INotificationHub
{
    Task InitializeAsync(string userToken);
    bool IsConnected { get; }
    event Action<BookmarkNotificationDTO>? OnBookmarkNotificationReceived;
    event Action<PropertyNotificationDTO>? OnPropertyNotificationReceived;
}
