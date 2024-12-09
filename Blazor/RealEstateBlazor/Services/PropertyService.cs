using System.Text.Json;
using RealEstateBlazor.Data.Models;
using RealEstateBlazor.Services;

public class PropertyService : IPropertyService
{
    private readonly HttpClient _httpClient;
    private readonly string _baseUrl;
    private readonly JsonSerializerOptions _jsonOptions;

    public PropertyService(HttpClient httpClient, IConfiguration configuration)
    {
        _httpClient = httpClient;
        _baseUrl = configuration["ApiSettings:BaseUrl"];
        _jsonOptions = new JsonSerializerOptions
        {
            PropertyNameCaseInsensitive = true
        };
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
            Console.WriteLine($"Getting property with ID: {id}");
            
            var response = await _httpClient.GetAsync($"{_baseUrl}/api/properties/{id}");
            response.EnsureSuccessStatusCode();

            //var jsonString = await response.Content.ReadAsStreamAsync();
            var jsonString = await response.Content.ReadAsStringAsync();
            
            Console.WriteLine($"Raw JSON response: {jsonString}");

            var jsonDocument = JsonDocument.Parse(jsonString);
            var propertyType = jsonDocument.RootElement.GetProperty("propertyType").GetString();
            
            var property = propertyType switch
            {
                "House" => JsonSerializer.Deserialize<House>(jsonString, _jsonOptions),
                "Apartment" => JsonSerializer.Deserialize<Apartment>(jsonString, _jsonOptions),
                _ => JsonSerializer.Deserialize<Property>(jsonString, _jsonOptions)
            } ?? throw new Exception("Failed to deserialize property");

            Console.WriteLine($"Deserialized property ID: {property.PropertyId}");
            return property;

            /*var data = JsonDocument.Parse(jsonString);
            var propertyType = data.RootElement.GetProperty("propertyType").GetString();

            return propertyType switch
            {
                "House" => JsonSerializer.Deserialize<House>(jsonString),
                "Apartment" => JsonSerializer.Deserialize<Apartment>(jsonString),
                _ => JsonSerializer.Deserialize<Property>(jsonString)
            } ?? throw new Exception("Property not found");*/
        }
        catch (HttpRequestException ex)
        {
            Console.WriteLine($"Error deserializing property: {ex.Message}");
            throw new Exception($"Failed to fetch property with id {id}", ex);
        }
        catch (JsonException ex)
        {
            Console.WriteLine($"JSON Error: {ex.Message}");
            throw new Exception("Invalid response format from server", ex);
        }
    }
}
