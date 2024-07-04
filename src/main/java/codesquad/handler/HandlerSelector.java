package codesquad.handler;

import codesquad.http.ContentTypeMapping;

import java.util.regex.Pattern;

public class HandlerSelector {
    public static Handler getHandler(String path){
        if(isOperationHandler(path)) {
            return HandlerMapping.getHandler(path);
        }
        if(isResourceHandler(path)){
            return new ResourceHandler();
        }
        return null;
    }

    private static boolean isOperationHandler(String path){
        return HandlerMapping.getEndpoints().contains(path);
    }


    public static boolean isResourceHandler(String path){
        Pattern URL_PATTERN = Pattern.compile(".*" + "("+String.join("|", ContentTypeMapping.getFileExtensions()) + ")$", Pattern.CASE_INSENSITIVE);
        return URL_PATTERN.matcher(path).matches();
    }
}
