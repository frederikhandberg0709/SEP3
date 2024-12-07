using Microsoft.JSInterop;

namespace RealEstateBlazor.Services;

public class TokenService : ITokenService
{
    private const string TokenKey = "jwtToken";
    private readonly IJSRuntime _jsRuntime;

    public TokenService(IJSRuntime jsRuntime)
    {
        _jsRuntime = jsRuntime;
    }

    public string GetToken()
    {
        try
        {
            return _jsRuntime.InvokeAsync<string>("localStorage.getItem", TokenKey).Result;
        }
        catch
        {
            return null;
        }
    }

    public void SetToken(string token)
    {
        _jsRuntime.InvokeVoidAsync("localStorage.setItem", TokenKey, token);
    }

    public void RemoveToken()
    {
        _jsRuntime.InvokeVoidAsync("localStorage.removeItem", TokenKey);
    }
}
