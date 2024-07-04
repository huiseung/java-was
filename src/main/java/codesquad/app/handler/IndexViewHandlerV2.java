package codesquad.app.handler;

import codesquad.annotation.RequestHandler;
import codesquad.annotation.RequestMapping;
import codesquad.http.HttpMethod;

@RequestHandler
public class IndexViewHandlerV2 {
    @RequestMapping(method = HttpMethod.GET, path = "/index")
    public String index(){
        return "/index.html";
    }
}
