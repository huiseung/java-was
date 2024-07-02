package codesquad.handler;

import java.util.HashMap;
import java.util.Map;

public class HandlerMapping {
    private static final Map<String, Handler> handlers = new HashMap<>();

    static {
        handlers.put("/index", new IndexHandler());
        handlers.put("/registration", new RegisterHandler());
    }

    public static Handler getHandler(String url){
        return handlers.get(url);
    }
}
