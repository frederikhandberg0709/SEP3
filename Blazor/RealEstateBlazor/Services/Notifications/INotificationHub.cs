using RealEstateBlazor.Data.DTOs;
using RealEstateBlazor.Data.Models;

namespace RealEstateBlazor.Services;

public interface INotificationHub : IAsyncDisposable
{
    Task InitializeAsync(string userToken);
    bool IsConnected { get; }
    event Action<bool> OnConnectionStateChanged;
    event Action<BookmarkNotificationDTO>? OnBookmarkNotificationReceived;
    event Action<PropertyNotificationDTO>? OnPropertyNotificationReceived;
}
