package via.sep.gui.Model.dto;

public class RegisterRequestDTO {
    private String username;
    private String password;
    private final String role = "ADMIN";

    public RegisterRequestDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}
