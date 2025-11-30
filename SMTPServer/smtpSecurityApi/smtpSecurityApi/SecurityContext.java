package smtpSecurityApi;


import java.util.HashMap;
import java.util.Map;

public class SecurityContext {

    private final Map<String, Object> attributes = new HashMap<>();

    public void set(String key, Object value) {
        attributes.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) attributes.get(key);
    }
}
