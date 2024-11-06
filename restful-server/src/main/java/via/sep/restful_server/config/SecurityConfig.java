package via.sep.restful_server.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import via.sep.restful_server.service.CustomUserDetailsService;

//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())
//                .cors(Customizer.withDefaults())
//                .authorizeHttpRequests(auth -> auth
//                        // Public endpoints
//                        .requestMatchers(
//                                "/api/users/register",
//                                "/api/users",
//                                "/api/users/{id}",
//                                "/api/properties",
//                                "/api/properties/{id}",
//                                "/api/agents",
//                                "/api/agents/{id}",
//                                "/api/bookings",
//                                "/api/bookings/{id}"
//                        ).permitAll()
//                        // Secured endpoints
//                        .anyRequest().authenticated()
//                );
//
//        return http.build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        // Public GET endpoints
                        .requestMatchers(HttpMethod.GET,
                                "/api/properties/**",
                                "/api/agents/**",
                                "/api/bookings/**"
                        ).permitAll()

                        // Public registration
                        .requestMatchers(HttpMethod.POST, "/api/users/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/properties/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/bookings/**").permitAll()

                        // Admin-only operations
                        .requestMatchers(HttpMethod.POST, "/api/properties/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/properties/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/properties/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/agents/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/agents/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/agents/**").hasRole("ADMIN")

                        // Booking operations require authentication
                        .requestMatchers(HttpMethod.POST, "/api/bookings/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/bookings/**").authenticated()

                        // Default
                        .anyRequest().authenticated()
                )
                .userDetailsService(userDetailsService)
                .httpBasic(
                        basic -> basic
                                .authenticationEntryPoint((request, response, authException) -> {
                                    response.setContentType("application/json");
                                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                    response.getWriter().write(
                                            "{\"error\": \"" + authException.getMessage() + "\", " +
                                                    "\"status\": \"401\"}"
                                    );
                                })
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
