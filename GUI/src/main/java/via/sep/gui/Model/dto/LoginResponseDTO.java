package via.sep.gui.Model.dto;

public class LoginResponseDTO {
    private String token;
    private String username;
    private String fullName;
    private String role;
    private Long accountId;

    public LoginResponseDTO(String token, String username, String fullName, String role, Long accountId) {
        this.token = token;
        this.username = username;
        this.fullName = fullName;
        this.role = role;
        this.accountId = accountId;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public String getRole() {
        return role;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
}
