using System.Text.Json.Serialization;

namespace RealEstateBlazor.Data.DTOs;

public class CreateBookmarkDTO
{
    //[JsonPropertyName("propertyId")]
    public long PropertyId { get; set; }
}
