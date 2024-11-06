package via.sep.restful_server.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentials(BadCredentialsException e) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Invalid username or password");
        response.put("status", "401");
        return ResponseEntity.status(401).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDenied(AccessDeniedException e) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "You don't have permission to perform this action");
        response.put("status", "403");
        return ResponseEntity.status(403).body(response);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthentication(AuthenticationException e) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Authentication failed: " + e.getMessage());
        response.put("status", "401");
        return ResponseEntity.status(401).body(response);
    }
}
