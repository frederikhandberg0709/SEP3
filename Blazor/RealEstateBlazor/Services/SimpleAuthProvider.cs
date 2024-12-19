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

    public SimpleAuthProvider(HttpClient httpClient, IJSRuntime jsRuntime)
    {
        this.httpClient = httpClient;
        this.jsRuntime = jsRuntime;
    }

    public override async Task<AuthenticationState> GetAuthenticationStateAsync()
    {
        try
        {
            var tokenAsJson = await jsRuntime.InvokeAsync<string>("sessionStorage.getItem", "jwtToken");

            if (string.IsNullOrEmpty(tokenAsJson))
            {
                Console.WriteLine("No token found in storage");
                return new AuthenticationState(new ClaimsPrincipal());
            }

            var loginResponse = JsonSerializer.Deserialize<LoginResponseDTO>(tokenAsJson);
            if (loginResponse?.Token == null)
            {
                Console.WriteLine("Could not deserialize token or token is null");
                return new AuthenticationState(new ClaimsPrincipal());
            }

            var claims = new List<Claim>
            {
                new Claim(ClaimTypes.Name, loginResponse.Fullname ?? loginResponse.Username),
                new Claim(ClaimTypes.Role, loginResponse.Role),
                new Claim("AccountId", loginResponse.AccountId.ToString()),
                new Claim("Username", loginResponse.Username),
                new Claim("Token", loginResponse.Token)
            };

            var identity = new ClaimsIdentity(claims, "jwt");
            var user = new ClaimsPrincipal(identity);
            
            foreach (var claim in claims)
            {
                Console.WriteLine($"- {claim.Type}: {claim.Value}");
            }
            
            return new AuthenticationState(user);
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Error in GetAuthenticationStateAsync: {ex.Message}");
            return new AuthenticationState(new ClaimsPrincipal());
        }
    }

    public async Task LoginAsync(string username, string password)
    {
        try
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
            await jsRuntime.InvokeVoidAsync("sessionStorage.setItem", "jwtToken", serializedToken);

            var claims = new List<Claim>
            {
                new Claim(ClaimTypes.Name, loginResponse.Fullname ?? loginResponse.Username),
                new Claim(ClaimTypes.Role, loginResponse.Role),
                new Claim("AccountId", loginResponse.AccountId.ToString()),
                new Claim("Username", loginResponse.Username),
                new Claim("Token", loginResponse.Token)
            };

            var identity = new ClaimsIdentity(claims, "jwt");
            var user = new ClaimsPrincipal(identity);

            NotifyAuthenticationStateChanged(Task.FromResult(new AuthenticationState(user)));
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Error in LoginAsync: {ex.Message}");
            throw;
        }
    }

    public async Task LogoutAsync()
    {
        await jsRuntime.InvokeVoidAsync("sessionStorage.removeItem", "jwtToken");
        
        NotifyAuthenticationStateChanged(
            Task.FromResult(new AuthenticationState(new ClaimsPrincipal()))
        );
    }
}
