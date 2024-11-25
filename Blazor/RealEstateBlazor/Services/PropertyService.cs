using RealEstateBlazor.Data.Models;

namespace RealEstateBlazor.Services;

public class PropertyService : IPropertyService
{
    private readonly HttpClient _httpClient;
    private readonly string _baseUrl;

    public PropertyService(HttpClient httpClient, IConfiguration configuration)
    {
        _httpClient = httpClient;
        _baseUrl = configuration["ApiSettings:BaseUrl"];
    }

    public async Task<IEnumerable<Property>> GetAllPropertiesAsync()
    {
        try
        {
            var response = await _httpClient.GetAsync($"{_baseUrl}/api/properties");
            response.EnsureSuccessStatusCode();
            var properties = await response.Content.ReadFromJsonAsync<IEnumerable<Property>>();
            return properties ?? Enumerable.Empty<Property>();
        }
        catch (HttpRequestException ex)
        {
            throw new Exception("Failed to fetch properties", ex);
        }
    }

    public async Task<Property> GetPropertyByIdAsync(string id)
    {
        try
        {
            var response = await _httpClient.GetAsync($"{_baseUrl}/api/properties/{id}");
            response.EnsureSuccessStatusCode();
            var property = await response.Content.ReadFromJsonAsync<Property>();
            return property ?? throw new Exception("Property not found");
        }
        catch (HttpRequestException ex)
        {
            throw new Exception($"Failed to fetch property with id {id}", ex);
        }
    }
}