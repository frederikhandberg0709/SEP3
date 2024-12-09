namespace RealEstateBlazor.Data.Models;

public class Bookmark
{
    public long BookmarkId { get; set; }
    public Property Property { get; set; } = new();
    public long PropertyId { get; set; }
    
    public Login Account { get; set; }
    public long AccountId { get; set; }
    
    public DateTime CreatedAt { get; set; } = DateTime.UtcNow;
    public DateTime UpdatedAt { get; set; } = DateTime.UtcNow;
}
