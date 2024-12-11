package via.sep.gui.Server;

import com.google.gson.Gson;
import via.sep.gui.Model.SessionManager;
import via.sep.gui.Model.dto.LoginRequestDTO;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerConnection {
    private String serverUrl;
    private final HttpClient httpClient;
    private final SessionManager sessionManager;

    public ServerConnection() {
        this.serverUrl = "http://localhost:8080/api";
        this.httpClient = HttpClient.newHttpClient();
        this.sessionManager = SessionManager.getInstance();
    }

    private String getAuthToken() {
        return sessionManager.getAuthToken();
    }

    public String sendGetRequest(String endpoint) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + endpoint))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return response.body();
        } else {
            throw new Exception("GET request failed with response code: " + response.statusCode());
        }
    }

    public String loginRequest(String username, String password) throws Exception {
        LoginRequestDTO loginRequest = new LoginRequestDTO(username, password);
        String jsonInputString = new Gson().toJson(loginRequest);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + "/users/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonInputString))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return response.body();
        } else {
            throw new Exception("Login failed with response code: " + response.statusCode());
        }
    }

    public String registerRequest(String jsonInputString) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + "/users/register"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonInputString))
                .build();

        System.out.println("Sending registration request to: " + serverUrl + "/users/register");

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200 || response.statusCode() == 201) {
            return response.body();
        } else {
            throw new Exception("Registration failed with response code: " + response.statusCode()
                    + ", Response body: " + response.body());
        }
    }

    public String sendPostRequest(String endpoint, String jsonInputString) throws Exception {
        if (!sessionManager.isLoggedIn() || !sessionManager.isAdmin()) {
            throw new Exception("Unauthorized: Must be logged in as admin");
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + endpoint))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + getAuthToken())
                .POST(HttpRequest.BodyPublishers.ofString(jsonInputString))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200 || response.statusCode() == 201) {
            return response.body();
        } else {
            throw new Exception("POST request failed with response code: " + response.statusCode());
        }
    }

    public String sendPutRequest(String endpoint, String jsonInputString) throws Exception {
        if (!sessionManager.isLoggedIn() || !sessionManager.isAdmin()) {
            throw new Exception("Unauthorized: Must be logged in as admin");
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + endpoint))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + getAuthToken())
                .PUT(HttpRequest.BodyPublishers.ofString(jsonInputString))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return response.body();
        } else {
            throw new Exception("PUT request failed with response code: " + response.statusCode());
        }
    }

    public void sendDeleteRequest(String endpoint) throws Exception {
        if (!sessionManager.isLoggedIn() || !sessionManager.isAdmin()) {
            throw new Exception("Unauthorized: Must be logged in as admin");
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + endpoint))
                .header("Authorization", "Bearer " + getAuthToken())
                .DELETE()
                .build();

        HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
        if (response.statusCode() != 204 && response.statusCode() != 200) {
            throw new Exception("DELETE request failed with response code: " + response.statusCode());
        }
    }
}
