package via.sep.restful_server.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import via.sep.restful_server.dto.RegistrationDTO;
import via.sep.restful_server.model.Login;
import via.sep.restful_server.model.UserProfile;
import via.sep.restful_server.repository.LoginRepository;
import via.sep.restful_server.repository.UserProfileRepository;

import java.util.HashMap;
import java.util.Map;

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
}
