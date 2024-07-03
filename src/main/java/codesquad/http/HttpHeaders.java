package codesquad.http;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpHeaders {
    private final Logger log = LoggerFactory.getLogger(HttpHeaders.class);
    private final Map<String, String> headers = new HashMap<>();

    public void add(String key, String value) {
        headers.put(key, value);
    }

    @Override
    public String toString() {
        return "headers=" + headers;
    }
}
