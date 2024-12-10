using Microsoft.AspNetCore.Components.Authorization;
using Microsoft.JSInterop;
using RealEstateBlazor.Components;
using RealEstateBlazor.Services;
using RealEstateBlazor.Services.Notifications;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
builder.Services.AddRazorComponents()
    .AddInteractiveServerComponents();

builder.Services.AddAuthorizationCore();
builder.Services.AddAuthentication();
builder.Services.AddAuthorization();

builder.Services.AddScoped<AuthenticationStateProvider, SimpleAuthProvider>();

builder.Services.AddHttpClient<SimpleAuthProvider>(client =>
{
    var baseUrl = builder.Configuration["ApiSettings:BaseUrl"];
    
    if (string.IsNullOrEmpty(baseUrl))
    {
        throw new InvalidOperationException("API Base URL is not configured");
    }
    
    client.BaseAddress = new Uri(baseUrl);
});

builder.Services.AddHttpClient<IPropertyService, PropertyService>(client =>
{
    var baseUrl = builder.Configuration["ApiSettings:BaseUrl"];
    
    if (string.IsNullOrEmpty(baseUrl))
    {
        throw new InvalidOperationException("API Base URL is not configured");
    }
    
    client.BaseAddress = new Uri(baseUrl);
});

/*builder.Services.AddHttpClient<PropertyService>(client =>
{
    var baseUrl = builder.Configuration["ApiSettings:BaseUrl"]
                  ?? throw new InvalidOperationException("API Base URL is not configured");
    client.BaseAddress = new Uri(baseUrl);
});*/

builder.Services.AddHttpClient<IImageService, ImageService>(client =>
{
    client.BaseAddress = new Uri(builder.Configuration["ApiSettings:BaseUrl"] 
                                 ?? throw new InvalidOperationException("API Base URL not configured"));
});

builder.Services.AddScoped<IPropertyService, PropertyService>();
//builder.Services.AddScoped<IImageService>(sp => sp.GetRequiredService<ImageService>());

builder.Services.AddScoped<INotificationHub, NotificationHub>();
builder.Services.AddScoped<IBookmarkService, BookmarkService>();

builder.Services.AddHttpClient<BookmarkService>(client =>
{
    var baseUrl = builder.Configuration["ApiSettings:BaseUrl"]
                  ?? throw new InvalidOperationException("API Base URL not configured");
    client.BaseAddress = new Uri(baseUrl);
});

var app = builder.Build();

// Configure the HTTP request pipeline.
if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Error", createScopeForErrors: true);
    app.UseHsts();
}

app.UseHttpsRedirection();

app.UseAntiforgery();

app.MapStaticAssets();
app.MapRazorComponents<App>()
    .AddInteractiveServerRenderMode();

app.Run();