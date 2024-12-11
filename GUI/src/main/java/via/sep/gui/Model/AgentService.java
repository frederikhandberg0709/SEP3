package via.sep.gui.Model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import via.sep.gui.Model.dto.AgentDTO;
import via.sep.gui.Server.ServerConnection;

import java.lang.reflect.Type;
import java.util.List;

public class AgentService {
    private final ServerConnection serverConnection;
    private final Gson gson;
    private static final String BASE_PATH = "/api/agents";

    public AgentService(ServerConnection serverConnection, Gson gson) {
        this.serverConnection = serverConnection;
        this.gson = gson;
    }

    public List<AgentDTO> getAllAgents() throws Exception {
        String response = serverConnection.sendGetRequest(BASE_PATH);
        Type listType = new TypeToken<List<AgentDTO>>(){}.getType();
        return gson.fromJson(response, listType);
    }

    public AgentDTO getAgentById(Long id) throws Exception {
        String response = serverConnection.sendGetRequest(BASE_PATH + "/" + id);
        return gson.fromJson(response, AgentDTO.class);
    }

    public AgentDTO createAgent(AgentDTO agentDTO) throws Exception {
        String json = gson.toJson(agentDTO);
        String response = serverConnection.sendPostRequest(BASE_PATH, json);
        return gson.fromJson(response, AgentDTO.class);
    }
}
