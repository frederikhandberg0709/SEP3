namespace RealEstateBlazor.Data.Models;

public class Apartment : Property
{
    public int? FloorNumber { get; set; }
    public string BuildingName { get; set; } = string.Empty;
    public bool HasElevator { get; set; }
    public bool HasBalcony { get; set; }
    public int NumFloors { get; set; } = 1;
    public bool HasGarage { get; set; }
    public decimal? LotSize { get; set; }
}