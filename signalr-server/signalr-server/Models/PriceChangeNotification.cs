namespace signalr_server.Models;

public record PriceChangeNotification(
    string PropertyId,
    string Address,
    decimal OldPrice,
    decimal NewPrice,
    DateTime Timestamp
);
