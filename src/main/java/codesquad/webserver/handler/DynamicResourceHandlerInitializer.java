package codesquad.webserver.handler;

import codesquad.webserver.annotation.ApiHandler;
import codesquad.webserver.annotation.RequestMapping;
import codesquad.webserver.http.HttpMethod;
import java.lang.reflect.Method;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DynamicResourceHandlerInitializer {
    private static final Logger log = LoggerFactory.getLogger(DynamicResourceHandler.class);
    public static void init(DynamicResourceHandler dynamicResourceHandler, List<Class<?>> classes){
        log.info("classes length: {}", classes.size());
        for(Class<?> clazz : classes){
            try{
                if(clazz.isAnnotationPresent(ApiHandler.class)){
                    Object handler = clazz.getDeclaredConstructor().newInstance();
                    for(Method method : clazz.getDeclaredMethods()){
                        if(method.isAnnotationPresent(RequestMapping.class)){
                            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                            HttpMethod httpMethod = requestMapping.method();
                            String path = requestMapping.path();
                            dynamicResourceHandler.addRoute(httpMethod, path, new HandlerMethod(handler, method));
                        }
                    }
                }
            }catch (Exception e){
                log.error(e.getMessage(), e);
            }
        }
        log.info("DynamicResourceHandler {}", dynamicResourceHandler);
    }
}
