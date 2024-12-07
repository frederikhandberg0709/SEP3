namespace RealEstateBlazor.Data.Models;

public class UserProfile
{
    public long ProfileId { get; set; }
    
    public Login Login { get; set; }
    
    public long AccountId { get; set; }
    
    public string FullName { get; set; }
    
    public string Email { get; set; }
    
    public string PhoneNumber { get; set; }
    
    public string Address { get; set; }
}
