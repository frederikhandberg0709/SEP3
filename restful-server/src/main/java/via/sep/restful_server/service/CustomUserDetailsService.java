package via.sep.restful_server.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import via.sep.restful_server.repository.LoginRepository;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final LoginRepository loginRepository;

    public CustomUserDetailsService(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return loginRepository.findByUsername(username)
                .map(login -> new User(
                        login.getUsername(),
                        login.getPassword(),
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + login.getRole()))
                ))
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
