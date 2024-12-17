using Microsoft.AspNetCore.SignalR.Client;
using RealEstateBlazor.Data.Models;
using RealEstateBlazor.Data.DTOs;

namespace RealEstateBlazor.Services.Notifications;

public class NotificationHub : INotificationHub
{
    private HubConnection? _hubConnection;
    private readonly string _notificationServerUrl;
    private readonly IConfiguration _configuration;
    private bool _isInitialized;
    public event Action<bool>? OnConnectionStateChanged;
    public event Action<PriceChangeNotificationDTO>? OnPriceChangeNotificationReceived;
    
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
            Console.WriteLine($"Attempting to connect to: {_notificationServerUrl}/hubs/notifications");
            Console.WriteLine($"With token: {userToken.Substring(0, 20)}...");
            
            _hubConnection = new HubConnectionBuilder()
                .WithUrl($"{_notificationServerUrl}/hubs/notifications", options =>
                {
                    options.AccessTokenProvider = () => Task.FromResult(userToken);
                    options.Headers.Add("X-Debug", "true");
                })
                .ConfigureLogging(logging =>
                {
                    logging.SetMinimumLevel(LogLevel.Debug);
                    logging.AddConsole();
                })
                .WithAutomaticReconnect()
                .Build();
            
            _hubConnection.Closed += async (error) =>
            {
                Console.WriteLine($"Connection closed: {error?.Message}");
                OnConnectionStateChanged?.Invoke(false);
                await Task.CompletedTask;
            };

            _hubConnection.Reconnected += async (connectionId) =>
            {
                OnConnectionStateChanged?.Invoke(true);
                await Task.CompletedTask;
            };

            _hubConnection.On<BookmarkNotificationDTO>("ReceiveBookmarkNotification", notification =>
            {
                OnBookmarkNotificationReceived?.Invoke(notification);
            });

            _hubConnection.On<PropertyNotificationDTO>("ReceivePropertyNotification", notification =>
            {
                OnPropertyNotificationReceived?.Invoke(notification);
            });

            _hubConnection.On<PriceChangeNotificationDTO>("OnPriceChanged", notification =>
            {
                Console.WriteLine($"Received price change notification: Property {notification.PropertyId}, New Price: {notification.NewPrice}");
                OnPriceChangeNotificationReceived?.Invoke(notification);
            });

            await _hubConnection.StartAsync();
            _isInitialized = true;
            OnConnectionStateChanged?.Invoke(true);
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
