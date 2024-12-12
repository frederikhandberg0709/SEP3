package via.sep.gui.Model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import via.sep.gui.Model.domain.Agent;
import via.sep.gui.Model.dto.AgentDTO;
import via.sep.gui.Server.ServerConnection;

import java.lang.reflect.Type;
import java.util.List;

public class AgentService {
    private final ServerConnection serverConnection;
    private final Gson gson;
    private static final String AGENT_ENDPOINT = "/agents";

    public AgentService(ServerConnection serverConnection, Gson gson) {
        this.serverConnection = serverConnection;
        this.gson = gson;
    }

    public List<Agent> getAllAgents() throws Exception {
        String response = serverConnection.sendGetRequest(AGENT_ENDPOINT);
        Type listType = new TypeToken<List<Agent>>(){}.getType();
        return gson.fromJson(response, listType);
    }

    public Agent createAgent(String name, String contactInfo) throws Exception {
        AgentDTO agentDTO = new AgentDTO(name, contactInfo);
        String json = gson.toJson(agentDTO);
        String response = serverConnection.sendPostRequest(AGENT_ENDPOINT, json);
        return gson.fromJson(response, Agent.class);
    }

    public void deleteAgent(Long agentId) throws Exception {
        serverConnection.sendDeleteRequest(AGENT_ENDPOINT + "/" + agentId);
    }
}
