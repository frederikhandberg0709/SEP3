namespace RealEstateBlazor.Data.DTOs;
    
public class ImageDTO
{
    public long Id { get; set; }
    public long PropertyId { get; set; }
    public string Base64ImageData { get; set; } = string.Empty;
}
