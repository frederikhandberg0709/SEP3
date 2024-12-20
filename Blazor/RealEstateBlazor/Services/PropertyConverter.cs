using System.Text.Json;
using System.Text.Json.Serialization;
using RealEstateBlazor.Data.Models;

namespace RealEstateBlazor.Services;

public class PropertyConverter : JsonConverter<Property>
{
    public override Property? Read(ref Utf8JsonReader reader, Type typeToConvert, JsonSerializerOptions options)
    {
        if (reader.TokenType != JsonTokenType.StartObject)
        {
            throw new JsonException();
        }
        
        using var jsonDocument = JsonDocument.Parse(JsonSerializer.Serialize(
            JsonSerializer.Deserialize<JsonElement>(ref reader)));

        var root = jsonDocument.RootElement;
        var propertyType = root.TryGetProperty("propertyType", out var typeElement) 
            ? typeElement.GetString() 
            : string.Empty;
        
        var json = root.GetRawText();
        
        return propertyType switch
        {
            "House" => JsonSerializer.Deserialize<House>(json, options),
            "Apartment" => JsonSerializer.Deserialize<Apartment>(json, options),
            _ => JsonSerializer.Deserialize<Property>(json, options)
        };
    }
    
    public override void Write(Utf8JsonWriter writer, Property value, JsonSerializerOptions options)
    {
        JsonSerializer.Serialize(writer, value, value.GetType(), options);
    }
}
