package codesquad.webserver.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpHeader {
    private final Map<String, String> headers = new HashMap<>();

    public void add(String key, String value) {
        headers.put(key, value);
    }

    public Set<String> getKeys(){
        return headers.keySet();
    }

    public String getValue(String key){
        return headers.get(key);
    }

    @Override
    public String toString() {
        return "headers=" + headers;
    }
}
