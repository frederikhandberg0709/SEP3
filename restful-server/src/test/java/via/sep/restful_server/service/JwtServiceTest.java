package via.sep.restful_server.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {
    private JwtService jwtService;
    private final String SECRET = "a_test_secret_key_that_is_long_enough_for_hs256";
    private final String ISSUER = "test_issuer";
    private final String AUDIENCE = "test_audience";
    private final long EXPIRATION = 3600000; // 1 hour in milliseconds

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret", SECRET);
        ReflectionTestUtils.setField(jwtService, "issuer", ISSUER);
        ReflectionTestUtils.setField(jwtService, "audience", AUDIENCE);
        ReflectionTestUtils.setField(jwtService, "expirationInMs", EXPIRATION);
    }

    @Test
    void generateToken_ValidInput_Success() {
        String username = "testuser";
        String role = "USER";
        Long accountId = 123L;

        String token = jwtService.generateToken(username, role, accountId);

        assertNotNull(token);

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals(username, claims.getSubject());
        assertEquals(role, claims.get("role"));
        assertEquals(accountId, claims.get("accountId", Long.class));
        assertEquals(ISSUER, claims.getIssuer());
        assertEquals(AUDIENCE, claims.getAudience());

        Date expiration = claims.getExpiration();
        Date issuedAt = claims.getIssuedAt();

        assertNotNull(expiration);
        assertNotNull(issuedAt);

        long diff = expiration.getTime() - issuedAt.getTime();
        assertEquals(EXPIRATION, diff, 1000);
    }

    @Test
    void generateToken_DifferentRoles_Success() {
        String username = "admin";
        String role = "ADMIN";
        Long accountId = 456L;

        String token = jwtService.generateToken(username, role, accountId);

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals(username, claims.getSubject());
        assertEquals(role, claims.get("role"));
        assertEquals(accountId, claims.get("accountId", Long.class));
    }

    @Test
    void generateToken_ValidatesSignature() {
        String username = "testuser";
        String role = "USER";
        Long accountId = 123L;

        String token = jwtService.generateToken(username, role, accountId);

        assertThrows(Exception.class, () -> {
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor("wrong_secret_key_that_is_long_enough_for_hs256"
                            .getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token);
        });
    }

    @Test
    void generateToken_NullValues_Success() {
        String username = "testuser";
        String role = null;
        Long accountId = null;

        String token = jwtService.generateToken(username, role, accountId);

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals(username, claims.getSubject());
        assertNull(claims.get("role"));
        assertNull(claims.get("accountId"));
    }
}
