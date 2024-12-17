package via.sep.restful_server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import via.sep.restful_server.dto.LoginRequestDTO;
import via.sep.restful_server.dto.RegistrationDTO;
import via.sep.restful_server.model.Login;
import via.sep.restful_server.model.UserProfile;
import via.sep.restful_server.repository.LoginRepository;
import via.sep.restful_server.repository.UserProfileRepository;
import via.sep.restful_server.service.CustomUserDetailsService;
import via.sep.restful_server.service.JwtService;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "jwt.secret=testSecretKeyThatIsLongEnoughToBeValid32Bytes",
        "jwt.issuer=test-issuer",
        "jwt.audience=test-audience",
        "jwt.expirationInMs=3600000"
})
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserProfileRepository userProfileRepository;

    @MockBean
    private LoginRepository loginRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private CustomUserDetailsService userDetailsService;

    @MockBean
    private JwtDecoder jwtDecoder;

    private Login adminLogin;
    private Login userLogin;
    private UserProfile adminProfile;
    private UserProfile userProfile;
    private String mockAdminToken;

    @BeforeEach
    void setUp() {
        // ADMIN
        adminLogin = new Login();
        adminLogin.setAccountId(1L);
        adminLogin.setUsername("admin");
        adminLogin.setRole("ADMIN");

        adminProfile = new UserProfile();
        adminProfile.setLogin(adminLogin);
        adminProfile.setFullName("Admin User");
        adminProfile.setEmail("admin@example.com");
        adminProfile.setPhoneNumber("12345678");
        adminProfile.setAddress("123 Admin St");

        // USER
        userLogin = new Login();
        userLogin.setAccountId(2L);
        userLogin.setUsername("user");
        userLogin.setRole("USER");

        userProfile = new UserProfile();
        userProfile.setLogin(userLogin);
        userProfile.setFullName("Regular User");
        userProfile.setEmail("user@example.com");
        userProfile.setPhoneNumber("87654321");
        userProfile.setAddress("456 User St");

        mockAdminToken = "mock.admin.token";
        Jwt jwt = Jwt.withTokenValue(mockAdminToken)
                .header("alg", "HS256")
                .subject(adminLogin.getUsername())
                .claim("role", adminLogin.getRole())
                .claim("accountId", adminLogin.getAccountId())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .issuer("test-issuer")
                .audience(Collections.singletonList("test-audience"))
                .build();

        when(jwtDecoder.decode(anyString())).thenReturn(jwt);

        when(userDetailsService.loadUserByUsername(adminLogin.getUsername()))
                .thenReturn(new User(
                        adminLogin.getUsername(),
                        "password",
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + adminLogin.getRole()))
                ));
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() throws Exception {
        when(userProfileRepository.findAll())
            .thenReturn(Arrays.asList(adminProfile, userProfile));

        mockMvc.perform(get("/api/users")
                        .header("Authorization", "Bearer " + mockAdminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value(adminLogin.getUsername()))
                .andExpect(jsonPath("$[1].username").value(userLogin.getUsername()));
    }

    @Test
    void getUser_WhenUserExists_ShouldReturnUser() throws Exception {
        when(userProfileRepository.findByLogin_AccountId(1L))
                .thenReturn(Optional.of(adminProfile));

        mockMvc.perform(get("/api/users/1")
                    .header("Authorization", "Bearer " + mockAdminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(adminLogin.getUsername()));
    }

    @Test
    void getUser_WhenUserDoesNotExist_ShouldReturn404() throws Exception {
        when(userProfileRepository.findByLogin_AccountId(999L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/999")
                    .header("Authorization", "Bearer " + mockAdminToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void login_WithValidCredentials_ShouldReturnToken() throws Exception {
        adminLogin.setUserProfile(adminProfile);
        adminLogin.setPassword("hashedPassword");

        when(loginRepository.findByUsername("admin"))
                .thenReturn(Optional.of(adminLogin));
        when(passwordEncoder.matches("password", "hashedPassword"))
                .thenReturn(true);
        when(jwtService.generateToken(
                adminLogin.getUsername(),
                adminLogin.getRole(),
                adminLogin.getAccountId()))
                .thenReturn(mockAdminToken);

        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("password");

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(mockAdminToken))
                .andExpect(jsonPath("$.username").value("admin"))
                .andExpect(jsonPath("$.fullName").value(adminProfile.getFullName()))
                .andExpect(jsonPath("$.role").value(adminLogin.getRole()))
                .andExpect(jsonPath("$.accountId").value(adminLogin.getAccountId()));
    }

    @Test
    void login_WithInvalidCredentials_ShouldReturn401() throws Exception {
        when(loginRepository.findByUsername("admin"))
                .thenReturn(Optional.of(adminLogin));
        when(passwordEncoder.matches("wrongPassword", "hashedPassword"))
                .thenReturn(false);

        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("wrongPassword");

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void register_WithNewUsername_ShouldCreateUser() throws Exception {
        when(loginRepository.existsByUsername("newuser")).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(loginRepository.save(any(Login.class))).thenReturn(userLogin);
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(userProfile);

        RegistrationDTO registrationDTO = new RegistrationDTO(
                "newuser", "password", "New User",
                "new@example.com", "12345678", "123 New St", null);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(userLogin.getUsername()));
    }

    @Test
    void register_WithExistingUsername_ShouldReturn400() throws Exception {
        when(loginRepository.existsByUsername("admin")).thenReturn(true);

        RegistrationDTO registrationDTO = new RegistrationDTO(
                "admin", "password", "Test User",
                "test@example.com", "12345678", "123 Test St", null);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Username already taken"));
    }

    @Test
    void deleteUser_WhenUserExists_ShouldReturn204() throws Exception {
        when(loginRepository.findById(1L)).thenReturn(Optional.of(adminLogin));

        mockMvc.perform(delete("/api/users/1")
                        .header("Authorization", "Bearer " + mockAdminToken))
                .andExpect(status().isNoContent());

        verify(loginRepository).delete(adminLogin);
    }

    @Test
    void deleteUser_WhenUserDoesNotExist_ShouldReturn404() throws Exception {
        when(loginRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/users/999")
                        .header("Authorization", "Bearer " + mockAdminToken))
                .andExpect(status().isNotFound());
    }
}
