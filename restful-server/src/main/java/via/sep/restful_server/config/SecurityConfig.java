package via.sep.restful_server.config;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import via.sep.restful_server.service.CustomUserDetailsService;
import java.nio.charset.StandardCharsets;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final CustomUserDetailsService userDetailsService;

    @Value("${jwt.secret}")
    private String jwtSecret;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/.well-known/openid-configuration").permitAll()
                        // Public GET endpoints
                        .requestMatchers(HttpMethod.GET,
                                "/api/properties/**",
                                "/api/images/**",
                                "/api/agents/**",
                                "/api/bookings/**",
                                "/api/images/**"
                        ).permitAll()

                        // Public registration
                        .requestMatchers(HttpMethod.POST, "/api/users/register", "/api/users/login").permitAll()

                        // Admin-only operations
                        .requestMatchers(HttpMethod.POST, "/api/properties/**", "/api/images/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/properties/**", "/api/images/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/properties/**", "/api/images/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/properties/**", "/api/images/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/agents/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/agents/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/agents/**").hasRole("ADMIN")

                        // Bookmark operations require authentication
                        .requestMatchers(HttpMethod.POST, "/api/bookmarks/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/bookmarks/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/bookmarks").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/bookmarks/property/{id}").authenticated()

                        // Booking operations require authentication
                        .requestMatchers(HttpMethod.POST, "/api/bookings/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/bookings/**").authenticated()

                        // Default
                        .anyRequest().authenticated()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .decoder(jwtDecoder())
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                );
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withSecretKey(
                Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8))
        ).build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("role");
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
