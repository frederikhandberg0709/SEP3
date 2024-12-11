package via.sep.gui.Model;

import com.google.gson.Gson;
import via.sep.gui.Model.dto.RegisterRequestDTO;
import via.sep.gui.Model.dto.UserResponseDTO;
import via.sep.gui.Server.ServerConnection;

public class RegistrationService {
    private final ServerConnection serverConnection;
    private final Gson gson;
    private static final String REGISTRATION_PATH = "/users/register";

    public RegistrationService(ServerConnection serverConnection, Gson gson) {
        this.serverConnection = serverConnection;
        this.gson = gson;
    }

    public UserResponseDTO register(String username, String password) throws Exception {
        RegisterRequestDTO registerRequest = new RegisterRequestDTO(username, password);
        String json = gson.toJson(registerRequest);
        String response = serverConnection.sendPostRequest(REGISTRATION_PATH, json);
        return gson.fromJson(response, UserResponseDTO.class);
    }
}
