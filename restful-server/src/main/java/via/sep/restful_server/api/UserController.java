package via.sep.restful_server.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import via.sep.restful_server.dto.RegistrationDTO;
import via.sep.restful_server.dto.UserResponseDTO;
import via.sep.restful_server.model.Login;
import via.sep.restful_server.model.UserProfile;
import via.sep.restful_server.repository.LoginRepository;
import via.sep.restful_server.repository.UserProfileRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final LoginRepository loginRepository;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(LoginRepository loginRepository,
                          UserProfileRepository userProfileRepository,
                          PasswordEncoder passwordEncoder) {
        this.loginRepository = loginRepository;
        this.userProfileRepository = userProfileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userProfileRepository.findAll().stream()
                .map(profile -> new UserResponseDTO(
                        profile.getLogin().getAccountId(),
                        profile.getLogin().getUsername(),
                        profile.getFullName(),
                        profile.getEmail(),
                        profile.getPhoneNumber(),
                        profile.getAddress(),
                        profile.getLogin().getRole()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id) {
        return userProfileRepository.findByLogin_AccountId(id)
                .map(profile -> new UserResponseDTO(
                        profile.getLogin().getAccountId(),
                        profile.getLogin().getUsername(),
                        profile.getFullName(),
                        profile.getEmail(),
                        profile.getPhoneNumber(),
                        profile.getAddress(),
                        profile.getLogin().getRole()
                ))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationDTO registration) {
        if (loginRepository.existsByUsername(registration.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new HashMap<String, String>() {{
                        put("error", "Username already taken");
                    }});
        }

        String encodedPassword = passwordEncoder.encode(registration.getPassword());

        Login login = new Login();
        login.setUsername(registration.getUsername());
        login.setPassword(encodedPassword);
        login.setRole("USER");

        Login savedLogin = loginRepository.save(login);

        UserProfile profile = new UserProfile();
        profile.setLogin(savedLogin);
        profile.setFullName(registration.getFullName());
        profile.setEmail(registration.getEmail());
        profile.setPhoneNumber(registration.getPhoneNumber());
        profile.setAddress(registration.getAddress());

        UserProfile savedProfile = userProfileRepository.save(profile);

        Map<String, Object> response = new HashMap<>();
        response.put("accountId", savedLogin.getAccountId());
        response.put("username", savedLogin.getUsername());
        response.put("fullName", savedProfile.getFullName());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        return loginRepository.findById(id)
                .map(login -> {
                    loginRepository.delete(login);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
