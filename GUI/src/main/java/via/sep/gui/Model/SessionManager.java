package via.sep.gui.Model;

import via.sep.gui.Model.dto.LoginResponseDTO;

public class SessionManager {
    private static SessionManager instance;
    private LoginResponseDTO currentUser;
    private String authToken;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setCurrentUser(LoginResponseDTO userResponse) {
        this.currentUser = userResponse;
        this.authToken = userResponse.getToken();
    }

    public LoginResponseDTO getCurrentUser() {
        return currentUser;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void clearSession() {
        currentUser = null;
        authToken = null;
    }

    public boolean isLoggedIn() {
        return authToken != null;
    }

    public boolean isAdmin() {
        return currentUser != null && "ADMIN".equals(currentUser.getRole());
    }
}
