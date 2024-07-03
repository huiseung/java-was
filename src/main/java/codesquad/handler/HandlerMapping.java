package codesquad.handler;

import java.util.HashMap;
import java.util.Map;

public class HandlerMapping {
    private  HandlerMapping(){}
    private static final Map<String, Handler> handlers = new HashMap<>();

    static {
        handlers.put("/index", new IndexViewHandler());
        handlers.put("/registration", new RegisterViewHandler());
        handlers.put("/create", new RegisterApiHandler());
    }

    public static Handler getHandler(String url){
        return handlers.get(url);
    }
}
