package via.sep.restful_server.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import via.sep.restful_server.dto.*;
import via.sep.restful_server.model.Login;
import via.sep.restful_server.model.UserProfile;
import via.sep.restful_server.repository.LoginRepository;
import via.sep.restful_server.repository.UserProfileRepository;
import via.sep.restful_server.service.JwtService;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final LoginRepository loginRepository;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Autowired
    public UserController(LoginRepository loginRepository,
                          UserProfileRepository userProfileRepository,
                          PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.loginRepository = loginRepository;
        this.userProfileRepository = userProfileRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
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

    @PatchMapping
    public ResponseEntity<UserResponseDTO> partialUpdateUser(@RequestBody UserResponseDTO userDTO) {
        return loginRepository.findById(userDTO.getAccountId())
                .map(login -> {
                    if (userDTO.getUsername() != null) {
                        login.setUsername(userDTO.getUsername());
                    }
                    if (userDTO.getRole() != null) {
                        login.setRole(userDTO.getRole());
                    }
                    Login updatedLogin = loginRepository.save(login);

                    UserProfile profile = userProfileRepository.findByLogin_AccountId(updatedLogin.getAccountId()).orElseThrow();

                    if (userDTO.getFullName() != null) {
                        profile.setFullName(userDTO.getFullName());
                    }
                    if (userDTO.getEmail() != null) {
                        profile.setEmail(userDTO.getEmail());
                    }
                    if (userDTO.getPhoneNumber() != null) {
                        profile.setPhoneNumber(userDTO.getPhoneNumber());
                    }
                    if (userDTO.getAddress() != null) {
                        profile.setAddress(userDTO.getAddress());
                    }

                    UserProfile updatedProfile = userProfileRepository.save(profile);

                    return ResponseEntity.ok(new UserResponseDTO(
                            updatedLogin.getAccountId(),
                            updatedLogin.getUsername(),
                            updatedProfile.getFullName(),
                            updatedProfile.getEmail(),
                            updatedProfile.getPhoneNumber(),
                            updatedProfile.getAddress(),
                            updatedLogin.getRole()
                    ));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/verify-password")
    public ResponseEntity<?> verifyPassword(@RequestBody PasswordVerificationDTO request) {
        Optional<Login> loginOptional = loginRepository.findByUsername(request.getUsername());

        if (loginOptional.isPresent()) {
            Login login = loginOptional.get();

            if (passwordEncoder.matches(request.getPassword(), login.getPassword())) {
                return ResponseEntity.ok().build();
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Collections.singletonMap("error", "Incorrect password"));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        return loginRepository.findByUsername(loginRequest.getUsername())
                .filter(login -> passwordEncoder.matches(loginRequest.getPassword(), login.getPassword()))
                .map(login -> {
                    String token = jwtService.generateToken(
                            login.getUsername(),
                            login.getRole(),
                            login.getAccountId()
                    );
                    return ResponseEntity.ok(new LoginResponseDTO(
                            token,
                            login.getUsername(),
                            login.getUserProfile().getFullName(),
                            login.getRole(),
                            login.getAccountId()
                    ));
                })
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Invalid username or password"
                ));
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

        String role = registration.getRole() != null ?
                registration.getRole().toUpperCase() : "USER";
        if (!role.equals("ADMIN") && !role.equals("USER")) {
            role = "USER";
        }
        login.setRole(role);

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
        response.put("role", savedLogin.getRole());
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
