using RealEstateBlazor.Data.Models;

namespace RealEstateBlazor.Services;

public interface IBookmarkService
{
    Task<Bookmark> CreateBookmarkAsync(long propertyId);
    
    Task<List<Bookmark>> GetBookmarksByAccountIdAsync();
    
    Task DeleteBookmarkAsync(long bookmarkId);
    
    Task<bool> IsBookmarkOwnerAsync(long bookmarkId);
    
    Task<bool> HasBookmarkedAsync(long propertyId);
}
