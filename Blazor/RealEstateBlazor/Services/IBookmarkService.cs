using RealEstateBlazor.Data.Models;

namespace RealEstateBlazor.Services;

public interface IBookmarkService
{
    Task<Bookmark> CreateBookmarkAsync(long propertyId, long accountId);
    
    Task<List<Bookmark>> GetBookmarksByAccountIdAsync(long accountId);
    
    Task DeleteBookmarkAsync(long bookmarkId);
    
    Task<bool> IsBookmarkOwnerAsync(long bookmarkId, long accountId);
    
    Task<bool> HasBookmarkedAsync(long accountId, long propertyId);
}
