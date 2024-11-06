namespace signalr_server.Models;

public record PropertyDeletedNotification(
    string PropertyId,
    string PropertyType,
    DateTime Timestamp
);
