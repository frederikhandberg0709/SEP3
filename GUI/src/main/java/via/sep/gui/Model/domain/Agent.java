package via.sep.gui.Model.domain;

import java.util.Set;

public class Agent {
    private Long agentId;
    private String name;
    private String contactInfo;

    public Agent(Long agentId, String name, String contactInfo) {
        this.agentId = agentId;
        this.name = name;
        this.contactInfo = contactInfo;
    }

    public Long getAgentId() {
        return agentId;
    }

    public String getName() {
        return name;
    }

    public String getContactInfo() {
        return contactInfo;
    }
}
