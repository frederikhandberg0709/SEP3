namespace RealEstateBlazor.Data.Models;

public class House : Property
{
    public decimal? LotSize { get; set; }
    public bool HasGarage { get; set; }
    public int NumFloors { get; set; } = 1;
}
