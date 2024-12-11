package via.sep.gui.ViewModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import via.sep.gui.Model.AgentService;
import via.sep.gui.Model.dto.AgentDTO;

public class CreateAgentViewModel {
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty contactInfo = new SimpleStringProperty();
    private final StringProperty errorMessage = new SimpleStringProperty();

    private final AgentService agentService;

    public CreateAgentViewModel(AgentService agentService) {
        this.agentService = agentService;
    }

    public boolean createAgent() {
        if (!validateInput()) {
            return false;
        }

        try {
            AgentDTO agentDTO = new AgentDTO();
            agentDTO.setName(name.get());
            agentDTO.setContactInfo(contactInfo.get());

            AgentDTO createdAgent = agentService.createAgent(agentDTO);
            clearFields();
            return createdAgent != null;
        } catch (Exception e) {
            errorMessage.set("Failed to create agent: " + e.getMessage());
            return false;
        }
    }

    private boolean validateInput() {
        if (name.get() == null || name.get().trim().isEmpty()) {
            errorMessage.set("Name is required");
            return false;
        }
        if (contactInfo.get() == null || contactInfo.get().trim().isEmpty()) {
            errorMessage.set("Contact info is required");
            return false;
        }
        return true;
    }

    private void clearFields() {
        name.set("");
        contactInfo.set("");
        errorMessage.set("");
    }

    // Property getters
    public StringProperty nameProperty() { return name; }
    public StringProperty contactInfoProperty() { return contactInfo; }
    public StringProperty errorMessageProperty() { return errorMessage; }
}
