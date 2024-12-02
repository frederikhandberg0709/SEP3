using System.Text.Json.Serialization;
using RealEstateBlazor.Services;

namespace RealEstateBlazor.Data.Models;

[JsonConverter(typeof(PropertyConverter))]
public class Property
{
    public long PropertyId { get; set; }
    public string PropertyType { get; set; } = string.Empty;
    public string Address { get; set; } = string.Empty;
    public decimal FloorArea { get; set; }
    public decimal Price { get; set; }
    public int? NumBedrooms { get; set; }
    public int? NumBathrooms { get; set; }
    public int? YearBuilt { get; set; }
    public string Description { get; set; } = String.Empty;
    public List<Image> Images { get; set; } = new();
}
