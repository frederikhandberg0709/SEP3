using System.Net;
using System.Text.Json;
using RealEstateBlazor.Data.Models;

namespace RealEstateBlazor.Services;

public class BookmarkService : IBookmarkService
{
    private readonly HttpClient _httpClient;
    private readonly string _baseUrl;
    private readonly JsonSerializerOptions _jsonOptions;

    public BookmarkService(HttpClient httpClient, IConfiguration configuration)
    {
        _httpClient = httpClient;
        _baseUrl = configuration["ApiSettings:BaseUrl"];
        _jsonOptions = new JsonSerializerOptions
        {
            PropertyNameCaseInsensitive = true
        };
    }

    public async Task<Bookmark> CreateBookmarkAsync(long propertyId, long accountId)
    {
        try
        {
            var createBookmarkDto = new { PropertyId = propertyId };
            var response = await _httpClient.PostAsJsonAsync($"{_baseUrl}/api/bookmarks", createBookmarkDto);
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<Bookmark>(_jsonOptions) 
                   ?? throw new Exception("Failed to create bookmark");
        }
        catch (HttpRequestException ex)
        {
            throw new Exception("Failed to create bookmark", ex);
        }
    }

    public async Task<List<Bookmark>> GetBookmarksByAccountIdAsync(long accountId)
    {
        try
        {
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
            var response = await _httpClient.DeleteAsync($"{_baseUrl}/api/bookmarks/{bookmarkId}");
            response.EnsureSuccessStatusCode();
        }
        catch (HttpRequestException ex)
        {
            throw new Exception($"Failed to delete bookmark with id {bookmarkId}", ex);
        }
    }

    public async Task<bool> IsBookmarkOwnerAsync(long bookmarkId, long accountId)
    {
        try
        {
            var response = await _httpClient.GetAsync($"{_baseUrl}/api/bookmarks/{bookmarkId}");
            if (response.StatusCode == HttpStatusCode.NotFound)
                return false;
                
            response.EnsureSuccessStatusCode();
            var bookmark = await response.Content.ReadFromJsonAsync<Bookmark>(_jsonOptions);
            return bookmark?.AccountId == accountId;
        }
        catch (HttpRequestException ex)
        {
            throw new Exception($"Failed to check bookmark ownership", ex);
        }
    }

    public async Task<bool> HasBookmarkedAsync(long accountId, long propertyId)
    {
        try
        {
            var response = await _httpClient.GetAsync($"{_baseUrl}/api/bookmarks/property/{propertyId}");
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<bool>(_jsonOptions);
        }
        catch (HttpRequestException ex)
        {
            throw new Exception("Failed to check bookmark status", ex);
        }
    }
}
