namespace RealEstateBlazor.Data.Models;

public class Property
{
    public long PropertyId { get; set; }
    public string PropertyType { get; set; }
    public string Address { get; set; }
    public decimal FloorArea { get; set; }
    public decimal Price { get; set; }
    public int? NumBedrooms { get; set; }
    public int? NumBathrooms { get; set; }
    public int? YearBuilt { get; set; }
    public string Description { get; set; }
}
