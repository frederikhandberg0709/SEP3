using System.Net.Http.Headers;
using Microsoft.AspNetCore.Components.Authorization;
using RealEstateBlazor.Data.DTOs;

namespace RealEstateBlazor.Services;

public class BookingService : IBookingService
{
    private readonly HttpClient _httpClient;
    private readonly string _baseUrl;
    private readonly AuthenticationStateProvider _authProvider;

    public BookingService(HttpClient httpClient, IConfiguration configuration, AuthenticationStateProvider authProvider)
    {
        _httpClient = httpClient;
        _baseUrl = configuration["ApiSettings:BaseUrl"] 
                   ?? throw new InvalidOperationException("API Base URL not configured");
        _authProvider = authProvider;
    }
    
    private async Task AddAuthHeader()
    {
        var authState = await _authProvider.GetAuthenticationStateAsync();
        var token = authState.User.FindFirst("Token")?.Value;
        
        if (!string.IsNullOrEmpty(token))
        {
            _httpClient.DefaultRequestHeaders.Authorization = 
                new AuthenticationHeaderValue("Bearer", token);
        }
    }

    public async Task<bool> CreateBookingAsync(BookingDTO booking)
    {
        await AddAuthHeader();
        var response = await _httpClient.PostAsJsonAsync($"{_baseUrl}/api/bookings", booking);
        return response.IsSuccessStatusCode;
    }

    public async Task<List<AgentDTO>> GetAvailableAgentsAsync(DateTime date)
    {
        await AddAuthHeader();
        var response =
            await _httpClient.GetFromJsonAsync<List<AgentDTO>>(
                $"{_baseUrl}/api/agents/available?date={date:yyyy-MM-dd}");
        return response ?? new List<AgentDTO>();
    }
}
