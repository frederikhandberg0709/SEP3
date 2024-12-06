using System.Text.Json.Serialization;

namespace RealEstateBlazor.Data.DTOs;

public class LoginReponseDTO
{
    [JsonPropertyName("token")]
    public string Token { get; set; }
    
    [JsonPropertyName("username")]
    public string Username { get; set; }
    
    [JsonPropertyName("role")]
    public string Role { get; set; }
    
    [JsonPropertyName("accountId")]
    public long AccountId { get; set; }
}
