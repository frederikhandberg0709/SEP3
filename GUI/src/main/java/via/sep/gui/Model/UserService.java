package via.sep.gui.Model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import via.sep.gui.Model.dto.LoginRequestDTO;
import via.sep.gui.Model.dto.LoginResponseDTO;
import via.sep.gui.Model.dto.UserResponseDTO;
import via.sep.gui.Server.ServerConnection;

import java.lang.reflect.Type;
import java.util.List;

public class UserService {
    private final ServerConnection serverConnection;
    private final Gson gson;
    private static final String BASE_PATH = "/users";

    public UserService(ServerConnection serverConnection, Gson gson) {
        this.serverConnection = serverConnection;
        this.gson = gson;
    }

    public LoginResponseDTO login(String username, String password) throws Exception {
        LoginRequestDTO loginRequest = new LoginRequestDTO(username, password);
        String json = gson.toJson(loginRequest);
        String response = serverConnection.sendPostRequest(BASE_PATH + "/login", json);
        return gson.fromJson(response, LoginResponseDTO.class);
    }

    public List<UserResponseDTO> getAllUsers() throws Exception {
        String response = serverConnection.sendGetRequest(BASE_PATH);
        Type listType = new TypeToken<List<UserResponseDTO>>(){}.getType();
        return gson.fromJson(response, listType);
    }

    public UserResponseDTO getUserById(Long id) throws Exception {
        String response = serverConnection.sendGetRequest(BASE_PATH + "/" + id);
        return gson.fromJson(response, UserResponseDTO.class);
    }

    public void deleteUser(Long id) throws Exception {
        serverConnection.sendDeleteRequest(BASE_PATH + "/" + id);
    }
}
