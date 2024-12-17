package via.sep.gui.ViewModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import via.sep.gui.Model.AgentService;
import via.sep.gui.Model.SceneManager;
import via.sep.gui.Model.domain.Agent;

public class AgentListViewModel {
    private final AgentService agentService;
    private final ObservableList<Agent> agents = FXCollections.observableArrayList();

    public AgentListViewModel(AgentService agentService) {
        this.agentService = agentService;
        loadAgents();
    }

    public ObservableList<Agent> getAgents() {
        return agents;
    }

    private void loadAgents() {
        try {
            agents.setAll(agentService.getAllAgents());
        } catch (Exception e) {
            System.err.println("Error loading agents: " + e.getMessage());
        }
    }

    public Agent createAgent(String name, String contactInfo) {
        try {
            Agent agent = agentService.createAgent(name, contactInfo);
            agents.add(agent);
            return agent;
        } catch (Exception e) {
            System.err.println("Error creating agent: " + e.getMessage());
            return null;
        }
    }

    public void deleteAgent(Long agentId) {
        try {
            agentService.deleteAgent(agentId);
            agents.removeIf(agent -> agent.getAgentId().equals(agentId));
        } catch (Exception e) {
            System.err.println("Error deleting agent: " + e.getMessage());
        }
    }

    public void closeAgentList() {
        SceneManager.showDashboard();
    }
}
