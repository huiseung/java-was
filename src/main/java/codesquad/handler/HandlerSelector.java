package codesquad.handler;

import codesquad.http.ContentTypeMapping;
import codesquad.http.HttpRequest;

import java.util.regex.Pattern;

public class HandlerSelector {
    public static Handler getHandler(HttpRequest request){
        if(isOperationHandler(request)) {
            return HandlerMapping.getHandler(request);
        }
        if(isResourceHandler(request)){
            return new ResourceHandler();
        }
        return null;
    }

    private static boolean isOperationHandler(HttpRequest request){
        return HandlerMapping.hasHandler(request);
    }


    public static boolean isResourceHandler(HttpRequest request){
        Pattern URL_PATTERN = Pattern.compile(".*" + "("+String.join("|", ContentTypeMapping.getFileExtensions()) + ")$", Pattern.CASE_INSENSITIVE);
        return URL_PATTERN.matcher(request.getPath()).matches();
    }
}
