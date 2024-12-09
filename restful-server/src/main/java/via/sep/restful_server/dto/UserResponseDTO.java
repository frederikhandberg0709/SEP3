package via.sep.restful_server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseDTO {
    private Long accountId;
    private String username;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
    private String role;
}
