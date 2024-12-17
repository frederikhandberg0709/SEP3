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

    public UserResponseDTO register(String username, String password, String fullName, String email, String phoneNumber, String address) throws Exception {
        RegisterRequestDTO registerRequest = new RegisterRequestDTO(username, password, fullName, email, phoneNumber, address);
        String json = gson.toJson(registerRequest);
        String response = serverConnection.registerRequest(json);
        return gson.fromJson(response, UserResponseDTO.class);
    }
}
