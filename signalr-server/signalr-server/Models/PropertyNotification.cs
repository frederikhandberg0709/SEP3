namespace signalr_server.Models;

public record PropertyNotification(
    string PropertyId,
    string Address,
    decimal Price,
    string PropertyType,
    Dictionary<string, object> Details,
    DateTime Timestamp
);
