using System.ComponentModel.DataAnnotations;

namespace RealEstateBlazor.Data.Models;

public class Image
{
    public long Id { get; set; }
    [Required]
    public string Base64ImageData { get; set; } = string.Empty;
        
    public long PropertyId { get; set; }
        
    public Property? Property { get; set; }
}
