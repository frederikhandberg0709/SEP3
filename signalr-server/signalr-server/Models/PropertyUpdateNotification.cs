namespace signalr_server.Models;

public record PropertyUpdateNotification(
    string PropertyId,
    string Address,
    decimal OldPrice,
    decimal NewPrice,
    string PropertyType,
    Dictionary<string, object> UpdatedFields,
    DateTime Timestamp
);
