namespace signalr_server.Models;

public record StatusChangeNotification(
    string PropertyId,
    string Address,
    string OldStatus,
    string NewStatus,
    DateTime Timestamp
);
