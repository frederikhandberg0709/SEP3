using RealEstateBlazor.Components;
using RealEstateBlazor.Services;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
builder.Services.AddRazorComponents()
    .AddInteractiveServerComponents();

builder.Services.AddHttpClient<IPropertyService, PropertyService>(client =>
{
    var baseUrl = builder.Configuration["ApiSettings:BaseUrl"];
    
    if (string.IsNullOrEmpty(baseUrl))
    {
        throw new InvalidOperationException("API Base URL is not configured");
    }
    
    client.BaseAddress = new Uri(baseUrl);
});

builder.Services.AddHttpClient<IImageService, ImageService>(client =>
{
    client.BaseAddress = new Uri(builder.Configuration["ApiSettings:BaseUrl"] 
                                 ?? throw new InvalidOperationException("API Base URL not configured"));
});

builder.Services.AddScoped<IPropertyService, PropertyService>();

var app = builder.Build();

// Configure the HTTP request pipeline.
if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Error", createScopeForErrors: true);
    // The default HSTS value is 30 days. You may want to change this for production scenarios, see https://aka.ms/aspnetcore-hsts.
    app.UseHsts();
}

app.UseHttpsRedirection();


app.UseAntiforgery();

app.MapStaticAssets();
app.MapRazorComponents<App>()
    .AddInteractiveServerRenderMode();

app.Run();