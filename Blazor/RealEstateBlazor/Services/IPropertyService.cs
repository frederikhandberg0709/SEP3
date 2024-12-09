using RealEstateBlazor.Data.Models;

namespace RealEstateBlazor.Services;

public interface IPropertyService
{
    Task<IEnumerable<Property>> GetAllPropertiesAsync();
    Task<Property> GetPropertyByIdAsync(string id);
}