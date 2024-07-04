package codesquad.webserver.handler;

import codesquad.webserver.http.HttpMethod;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class DynamicResourceHandler implements Handler{
    private Map<HttpMethod, Map<String, HandlerMethod>> routes = new HashMap<>();

    {
        for(HttpMethod httpMethod : HttpMethod.values()){
            routes.put(httpMethod, new HashMap<>());
        }
    }
    public HttpResponse handle(HttpRequest request){
        HandlerMethod handlerMethod = routes.get(request.getHttpMethod()).get(request.getPath());
        return handlerMethod.invoke(request);
    }

    public boolean canHandle(HttpRequest request){
        return routes.get(request.getHttpMethod()).containsKey(request.getPath());
    }

    public void addRoute(HttpMethod httpMethod, String path, HandlerMethod handlerMethod){
        routes.get(httpMethod).put(path, handlerMethod);
    }

    @Override
    public String toString() {
        return "Router{" +
                "routes=" + routes +
                '}';
    }
}
