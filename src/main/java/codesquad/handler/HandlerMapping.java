package codesquad.handler;

import codesquad.app.handler.IndexViewHandler;
import codesquad.app.handler.RegisterApiHandler;
import codesquad.app.handler.RegisterViewHandler;
import codesquad.http.HttpMethod;
import codesquad.http.HttpRequest;

import java.util.HashMap;
import java.util.Map;

public class HandlerMapping {
    private  HandlerMapping(){}
    private static final Map<HttpMethod, Map<String, Handler>> handlers = new HashMap<>();

    static {
        handlers.put(HttpMethod.GET, new HashMap<>());
        handlers.put(HttpMethod.POST, new HashMap<>());

        handlers.get(HttpMethod.GET).put("/index", new IndexViewHandler());
        handlers.get(HttpMethod.GET).put("/registration", new RegisterViewHandler());
        handlers.get(HttpMethod.GET).put("/create", new RegisterApiHandler());
    }

    public static void print(){
        System.out.println(handlers);
    }

    public static Handler getHandler(HttpRequest request){
        return handlers.get(request.getMethod()).get(request.getPath());
    }

    public static boolean hasHandler(HttpRequest request){
        return handlers.get(request.getMethod()).containsKey(request.getPath());
    }

    public static void addHandler(HttpMethod method, String path, Handler handler){
        if(!handlers.containsKey(method)){
            handlers.put(method, new HashMap<>());
        }
        handlers.get(method).put(path, handler);
    }
}
