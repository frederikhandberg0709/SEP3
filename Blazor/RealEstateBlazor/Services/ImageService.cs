using RealEstateBlazor.Data.Models;

namespace RealEstateBlazor.Services;

public class ImageService : IImageService
{
    private readonly HttpClient _httpClient;
    private readonly string _baseUrl;

    public ImageService(HttpClient httpClient, IConfiguration configuration)
    {
        _httpClient = httpClient;
        _baseUrl = configuration["ApiSettings:BaseUrl"];
    }
    
    public async Task<ImageDTO> GetImageByIdAsync(string id)
    {
        try
        {
            var response = await _httpClient.GetAsync($"{_baseUrl}/api/images/{id}");
            response.EnsureSuccessStatusCode();
            var image = await response.Content.ReadFromJsonAsync<ImageDTO>();
            return image ?? throw new Exception("Image not found");
        }
        catch (HttpRequestException ex)
        {
            throw new Exception($"Failed to fetch image with id {id}", ex);
        }
    }

    public async Task<IEnumerable<ImageDTO>> GetImagesForPropertyAsync(string propertyId)
    {
        try
        {
            var response = await _httpClient.GetAsync($"{_baseUrl}/api/images/property/{propertyId}");
            response.EnsureSuccessStatusCode();
            var images = await response.Content.ReadFromJsonAsync<IEnumerable<ImageDTO>>();
            return images ?? Enumerable.Empty<ImageDTO>();
        }
        catch (HttpRequestException ex)
        {
            throw new Exception($"Failed to fetch images for property with id {propertyId}", ex);
        }
    }
}
