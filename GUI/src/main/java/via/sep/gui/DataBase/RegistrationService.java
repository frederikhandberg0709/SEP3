package via.sep.gui.DataBase;



import java.util.HashMap;
import java.util.Map;

public class RegistrationService {
    private final Map<String, String> userDatabase = new HashMap<>();

    public boolean register(String username, String password) {
        if (userDatabase.containsKey(username)) {
            return false; // Username already exists
        }
        userDatabase.put(username, password);
        return true; // Registration successful
    }
}