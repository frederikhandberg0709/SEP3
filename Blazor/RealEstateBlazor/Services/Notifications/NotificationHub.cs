using Microsoft.AspNetCore.SignalR.Client;
using RealEstateBlazor.Data.Models;

namespace RealEstateBlazor.Services;

public class NotificationHub : INotificationHub, IAsyncDisposable
{
    private HubConnection? _hubConnection;
    private readonly string _notificationServerUrl;
    private readonly IConfiguration _configuration;
    private bool _isInitialized;
    
    public NotificationHub(IConfiguration configuration)
    {
        _configuration = configuration;
        _notificationServerUrl = _configuration["ApiSettings:NotificationUrl"];
    }
    
    public async Task InitializeAsync(string userToken)
    {
        if (_isInitialized)
            return;

        try
        {
            _hubConnection = new HubConnectionBuilder()
                .WithUrl($"{_notificationServerUrl}/notificationHub", options =>
                {
                    options.AccessTokenProvider = () => Task.FromResult(userToken);
                })
                .WithAutomaticReconnect()
                .Build();

            _hubConnection.On<BookmarkNotificationDTO>("ReceiveBookmarkNotification", notification =>
            {
                OnBookmarkNotificationReceived?.Invoke(notification);
            });

            _hubConnection.On<PropertyNotificationDTO>("ReceivePropertyNotification", notification =>
            {
                OnPropertyNotificationReceived?.Invoke(notification);
            });

            await _hubConnection.StartAsync();
            _isInitialized = true;
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Error initializing SignalR connection: {ex.Message}");
            _isInitialized = false;
            throw;
        }
    }

    public bool IsConnected => _hubConnection?.State == HubConnectionState.Connected;

    public event Action<BookmarkNotificationDTO>? OnBookmarkNotificationReceived;
    public event Action<PropertyNotificationDTO>? OnPropertyNotificationReceived;

    public async ValueTask DisposeAsync()
    {
        if (_hubConnection is not null)
        {
            await _hubConnection.DisposeAsync();
            _isInitialized = false;
        }
    }
}
