using System.Text.Json.Serialization;

namespace RealEstateBlazor.Data.DTOs;

public class UpdateProfileDTO
{
    [JsonPropertyName("accountId")]
    public long UserId { get; set; }
    public string Username { get; set; }
    public string FullName { get; set; }
    public string Email { get; set; }
    public string PhoneNumber { get; set; }
    public string Address { get; set; }
    public string Role { get; set; } = "USER";
}
