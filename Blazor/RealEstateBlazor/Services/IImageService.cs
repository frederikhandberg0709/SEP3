using RealEstateBlazor.Data.Models;

namespace RealEstateBlazor.Services;

public interface IImageService
{
    Task<Image> GetImageByIdAsync(string id);
    Task<IEnumerable<Image>> GetImagesForPropertyAsync(string propertyId);
}
