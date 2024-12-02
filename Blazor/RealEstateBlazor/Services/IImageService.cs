using RealEstateBlazor.Data.Models;

namespace RealEstateBlazor.Services;

public interface IImageService
{
    Task<ImageDTO> GetImageByIdAsync(string id);
    Task<IEnumerable<ImageDTO>> GetImagesForPropertyAsync(string propertyId);
}
