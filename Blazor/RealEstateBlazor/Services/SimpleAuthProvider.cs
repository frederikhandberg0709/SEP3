using System.Security.Claims;
using System.Text.Json;
using Microsoft.AspNetCore.Components.Authorization;
using Microsoft.JSInterop;
using RealEstateBlazor.Data.DTOs;

namespace RealEstateBlazor.Services;

public class SimpleAuthProvider : AuthenticationStateProvider
{
    private readonly HttpClient httpClient;
    private readonly IJSRuntime jsRuntime;
    private readonly ITokenService tokenService;

    public SimpleAuthProvider(HttpClient httpClient, IJSRuntime jsRuntime, ITokenService tokenService)
    {
        this.httpClient = httpClient;
        this.jsRuntime = jsRuntime;
        this.tokenService = tokenService;
    }

    public override async Task<AuthenticationState> GetAuthenticationStateAsync()
    {
        string tokenAsJson = "";
        try
        {
            tokenAsJson = await jsRuntime.InvokeAsync<string>("sessionStorage.getItem", "authToken");
        }
        catch (InvalidOperationException)
        {
            return new AuthenticationState(new ClaimsPrincipal());
        }

        if (string.IsNullOrEmpty(tokenAsJson))
        {
            return new AuthenticationState(new ClaimsPrincipal());
        }

        var loginResponse = JsonSerializer.Deserialize<LoginResponseDTO>(tokenAsJson);
        if (loginResponse?.Token == null)
        {
            return new AuthenticationState(new ClaimsPrincipal());
        }

        var claims = new List<Claim>
        {
            new Claim(ClaimTypes.Name, loginResponse.Fullname ?? loginResponse.Username), // Use FullName with fallback to Username
            new Claim(ClaimTypes.Role, loginResponse.Role),
            new Claim("AccountId", loginResponse.AccountId.ToString()),
            new Claim("Username", loginResponse.Username),
            new Claim("Token", loginResponse.Token)
        };

        var identity = new ClaimsIdentity(claims, "jwt");
        var user = new ClaimsPrincipal(identity);
        
        return new AuthenticationState(user);
    }

    public async Task LoginAsync(string username, string password)
    {
        var loginRequest = new LoginRequestDTO
        {
            Username = username,
            Password = password
        };

        using var request = new HttpRequestMessage(HttpMethod.Post, "http://localhost:8080/api/users/login");
        request.Content = JsonContent.Create(loginRequest);
        
        var response = await httpClient.SendAsync(request);
        
        if (!response.IsSuccessStatusCode)
        {
            var error = await response.Content.ReadAsStringAsync();
            throw new Exception(error);
        }

        var loginResponse = await response.Content.ReadFromJsonAsync<LoginResponseDTO>();
        if (loginResponse?.Token == null)
        {
            throw new Exception("Invalid response from server");
        }

        string serializedToken = JsonSerializer.Serialize(loginResponse);
        await jsRuntime.InvokeVoidAsync("sessionStorage.setItem", "authToken", serializedToken);
        tokenService.SetToken(loginResponse.Token);

        var claims = new List<Claim>
        {
            new Claim(ClaimTypes.Name, loginResponse.Fullname ?? loginResponse.Username),
            new Claim(ClaimTypes.Role, loginResponse.Role),
            new Claim("AccountId", loginResponse.AccountId.ToString()),
            new Claim("Username", loginResponse.Username),
        };

        var identity = new ClaimsIdentity(claims, "jwt");
        var user = new ClaimsPrincipal(identity);

        NotifyAuthenticationStateChanged(Task.FromResult(new AuthenticationState(user)));
    }

    public async Task LogoutAsync()
    {
        await jsRuntime.InvokeVoidAsync("sessionStorage.removeItem", "authToken");
        tokenService.SetToken(null);
        
        NotifyAuthenticationStateChanged(
            Task.FromResult(new AuthenticationState(new ClaimsPrincipal()))
        );
    }
}
