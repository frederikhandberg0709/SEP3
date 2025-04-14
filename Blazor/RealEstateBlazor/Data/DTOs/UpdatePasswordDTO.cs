namespace RealEstateBlazor.Data.DTOs;

public class UpdatePasswordDTO
{
    public int UserId { get; set; }
    public string CurrentPassword { get; set; }
    public string NewPassword { get; set; }
    public string ConfirmPassword { get; set; }
}
