package via.sep.restful_server.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AgentNotificationData {
    private String agentId;
    private String name;
    private String contactInfo;
}
