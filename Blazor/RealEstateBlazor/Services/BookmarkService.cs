using System.Net;
using System.Text.Json;
using RealEstateBlazor.Data.Models;
using RealEstateBlazor.Data.DTOs;

namespace RealEstateBlazor.Services;

public class BookmarkService : IBookmarkService
{
    private readonly HttpClient _httpClient;
    private readonly string _baseUrl;
    private readonly JsonSerializerOptions _jsonOptions;
    private readonly ITokenService _tokenService;

    public BookmarkService(HttpClient httpClient, IConfiguration configuration, ITokenService tokenService)
    {
        _httpClient = httpClient;
        _baseUrl = configuration["ApiSettings:BaseUrl"];
        _jsonOptions = new JsonSerializerOptions
        {
            PropertyNameCaseInsensitive = true
        };
        _tokenService = tokenService;
    }
    
    private void AddAuthorizationHeader()
    {
        var token = _tokenService.GetToken();
        Console.WriteLine($"Token present: {!string.IsNullOrEmpty(token)}");
        if (!string.IsNullOrEmpty(token))
        {
            _httpClient.DefaultRequestHeaders.Authorization = 
                new System.Net.Http.Headers.AuthenticationHeaderValue("Bearer", token);
        }
        else
        {
            Console.WriteLine("No token found in TokenService");
        }
    }

    public async Task<Bookmark> CreateBookmarkAsync(long propertyId)
    {
        try
        {
            AddAuthorizationHeader();
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
            AddAuthorizationHeader();
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
            AddAuthorizationHeader();
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
            AddAuthorizationHeader();
            var response = await _httpClient.GetAsync($"{_baseUrl}/api/bookmarks/{bookmarkId}");
            if (response.StatusCode == HttpStatusCode.NotFound)
                return false;
                
            response.EnsureSuccessStatusCode();
            // var bookmark = await response.Content.ReadFromJsonAsync<Bookmark>(_jsonOptions);
            // return bookmark?.AccountId == accountId;
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
            AddAuthorizationHeader();
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
