package via.sep.restful_server.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.audience}")
    private String audience;

    @Value("${jwt.expirationInMs:3600000}")
    private long expirationInMs;

    public String generateToken(String username, String role, Long accountId) {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .claim("accountId", accountId)
                .setIssuer(issuer)
                .setAudience(audience)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationInMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
