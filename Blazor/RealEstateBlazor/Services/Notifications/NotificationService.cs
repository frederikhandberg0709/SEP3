using Microsoft.AspNetCore.Components.Authorization;
using RealEstateBlazor.Data.Models;

namespace RealEstateBlazor.Services.Notifications;

public class NotificationService : INotificationService
{
    private readonly HttpClient _httpClient;
    private readonly IConfiguration _configuration;
    private readonly AuthenticationStateProvider _authStateProvider;
    
    public NotificationService(HttpClient httpClient, IConfiguration configuration, AuthenticationStateProvider authStateProvider)
    {
        _httpClient = httpClient;
        _configuration = configuration;
        _authStateProvider = authStateProvider;
        _httpClient.BaseAddress = new Uri(_configuration["ApiSettings:BaseUrl"]);
    }

    public async Task<List<NotificationHistory>> GetNotificationHistoryAsync(long accountId)
    {
        try
        {
            var authState = await _authStateProvider.GetAuthenticationStateAsync();
            var token = authState.User.FindFirst("Token")?.Value;

            if (string.IsNullOrEmpty(token))
            {
                throw new InvalidOperationException("Authentication token not found");
            }

            _httpClient.DefaultRequestHeaders.Authorization = 
                new System.Net.Http.Headers.AuthenticationHeaderValue("Bearer", token);
            
            var response = await _httpClient.GetFromJsonAsync<List<NotificationHistory>>($"/api/notifications?accountId={accountId}");
            return response ?? new List<NotificationHistory>();
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Error fetching notification history: {ex.Message}");
            return new List<NotificationHistory>();
        }
    }
}