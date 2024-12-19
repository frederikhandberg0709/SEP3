package via.sep.restful_server.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import via.sep.restful_server.model.Login;
import via.sep.restful_server.repository.LoginRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {
    @Mock
    private LoginRepository loginRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private Login testLogin;

    @BeforeEach
    void setUp() {
        testLogin = new Login();
        testLogin.setUsername("testuser");
        testLogin.setPassword("password123");
        testLogin.setRole("USER");
    }

    @Test
    void loadUserByUsername_Success() {
        when(loginRepository.findByUsername("testuser")).thenReturn(Optional.of(testLogin));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testuser");

        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("password123", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void loadUserByUsername_UserNotFound() {
        String nonExistentUsername = "nonexistent";
        when(loginRepository.findByUsername(nonExistentUsername)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername(nonExistentUsername));

        assertEquals("User not found: " + nonExistentUsername, exception.getMessage());
    }

    @Test
    void loadUserByUsername_AdminRole() {
        Login adminLogin = new Login();
        adminLogin.setUsername("admin");
        adminLogin.setPassword("adminpass");
        adminLogin.setRole("ADMIN");

        when(loginRepository.findByUsername("admin")).thenReturn(Optional.of(adminLogin));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("admin");

        assertNotNull(userDetails);
        assertEquals("admin", userDetails.getUsername());
        assertEquals("adminpass", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }
}
