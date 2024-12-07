namespace RealEstateBlazor.Services;

public interface ITokenService
{
    string GetToken();
    void SetToken(string token);
    void RemoveToken();
}