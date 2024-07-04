package codesquad.handler;

import codesquad.app.handler.IndexViewHandler;
import codesquad.app.handler.RegisterApiHandler;
import codesquad.app.handler.RegisterViewHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

    public static Set<String> getEndpoints(){
        return handlers.keySet();
    }
}
