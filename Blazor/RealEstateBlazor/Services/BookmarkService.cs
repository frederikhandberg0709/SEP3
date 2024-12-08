using System.Net;
using System.Text.Json;
using Microsoft.AspNetCore.Components.Authorization;
using RealEstateBlazor.Data.Models;
using RealEstateBlazor.Data.DTOs;

namespace RealEstateBlazor.Services;

public class BookmarkService : IBookmarkService
{
    private readonly HttpClient _httpClient;
    private readonly string _baseUrl;
    private readonly JsonSerializerOptions _jsonOptions;
    private readonly AuthenticationStateProvider _authStateProvider;

    public BookmarkService(HttpClient httpClient, IConfiguration configuration, AuthenticationStateProvider authStateProvider)
    {
        _httpClient = httpClient;
        _baseUrl = configuration["ApiSettings:BaseUrl"];
        _jsonOptions = new JsonSerializerOptions
        {
            PropertyNameCaseInsensitive = true
        };
        _authStateProvider = authStateProvider;
    }
    
    private async Task AddAuthorizationHeaderAsync()
    {
        try
        {
            Console.WriteLine("Attempting to add auth header");
            var authState = await _authStateProvider.GetAuthenticationStateAsync();
            Console.WriteLine($"Auth state retrieved, user authenticated: {authState.User.Identity?.IsAuthenticated}");
            var token = authState.User.FindFirst("Token")?.Value;
            Console.WriteLine($"Token present: {!string.IsNullOrEmpty(token)}");
            
            if (!string.IsNullOrEmpty(token))
            {
                if (_httpClient.DefaultRequestHeaders.Contains("Authorization"))
                {
                    _httpClient.DefaultRequestHeaders.Remove("Authorization");
                }
            
                _httpClient.DefaultRequestHeaders.Authorization = 
                    new System.Net.Http.Headers.AuthenticationHeaderValue("Bearer", token);
            
                var authHeader = _httpClient.DefaultRequestHeaders.Authorization;
                Console.WriteLine($"Authorization header set: {authHeader != null}");
                Console.WriteLine($"Authorization header: {authHeader?.Scheme} {authHeader?.Parameter?.Substring(0, Math.Min(20, authHeader.Parameter?.Length ?? 0))}...");
            }
            else
            {
                Console.WriteLine("No token available to add to Authorization header");
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Error in AddAuthorizationHeader: {ex.Message}");
            Console.WriteLine($"Stack trace: {ex.StackTrace}");
            throw;
        }
    }

    public async Task<Bookmark> CreateBookmarkAsync(long propertyId)
    {
        try
        {
            await AddAuthorizationHeaderAsync();
            var dto = new CreateBookmarkDTO { PropertyId = propertyId };
            var response = await _httpClient.PostAsJsonAsync($"{_baseUrl}/api/bookmarks", dto);
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<Bookmark>(_jsonOptions) 
                   ?? throw new Exception("Failed to create bookmark");
        }
        catch (HttpRequestException ex)
        {
            throw new Exception("Failed to create bookmark", ex);
        }
    }

    public async Task<List<Bookmark>> GetBookmarksByAccountIdAsync()
    {
        try
        {
            await AddAuthorizationHeaderAsync();
            var response = await _httpClient.GetAsync($"{_baseUrl}/api/bookmarks");
            response.EnsureSuccessStatusCode();
            var bookmarks = await response.Content.ReadFromJsonAsync<List<Bookmark>>(_jsonOptions);
            return bookmarks ?? new List<Bookmark>();
        }
        catch (HttpRequestException ex)
        {
            throw new Exception("Failed to fetch bookmarks", ex);
        }
    }

    public async Task DeleteBookmarkAsync(long bookmarkId)
    {
        try
        {
            await AddAuthorizationHeaderAsync();
            var response = await _httpClient.DeleteAsync($"{_baseUrl}/api/bookmarks/{bookmarkId}");
            response.EnsureSuccessStatusCode();
        }
        catch (HttpRequestException ex)
        {
            throw new Exception($"Failed to delete bookmark with id {bookmarkId}", ex);
        }
    }

    public async Task<bool> IsBookmarkOwnerAsync(long bookmarkId)
    {
        try
        {
            await AddAuthorizationHeaderAsync();
            var response = await _httpClient.GetAsync($"{_baseUrl}/api/bookmarks/{bookmarkId}");
            if (response.StatusCode == HttpStatusCode.NotFound)
                return false;
                
            response.EnsureSuccessStatusCode();
            var bookmark = await response.Content.ReadFromJsonAsync<Bookmark>(_jsonOptions);
            return bookmark != null;
        }
        catch (HttpRequestException ex)
        {
            throw new Exception($"Failed to check bookmark ownership", ex);
        }
    }

    public async Task<bool> HasBookmarkedAsync(long propertyId)
    {
        try
        {
            Console.WriteLine($"Checking bookmark status for property {propertyId}");
            await AddAuthorizationHeaderAsync();

            var url = $"{_baseUrl}/api/bookmarks/property/{propertyId}";
            Console.WriteLine($"Making request to: {url}");

            var response = await _httpClient.GetAsync(url);
            Console.WriteLine($"Response status code: {response.StatusCode}");

            if (!response.IsSuccessStatusCode)
            {
                var errorContent = await response.Content.ReadAsStringAsync();
                Console.WriteLine($"Error response content: {errorContent}");
                throw new HttpRequestException($"Request failed with status {response.StatusCode}: {errorContent}");
            }

            response.EnsureSuccessStatusCode();
            var result = await response.Content.ReadFromJsonAsync<bool>(_jsonOptions);
            Console.WriteLine($"Bookmark status result: {result}");
            return result;
        }
        catch (HttpRequestException ex)
        {
            Console.WriteLine($"HTTP request error: {ex.Message}");
            Console.WriteLine($"Stack trace: {ex.StackTrace}");
            throw new Exception("Failed to check bookmark status", ex);
        }
        catch (Exception ex)
        {
            Console.WriteLine($"General error: {ex.Message}");
            Console.WriteLine($"Stack trace: {ex.StackTrace}");
            throw new Exception("Failed to check bookmark status", ex);
        }
    }
}
