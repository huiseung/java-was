package codesquad.webserver.handler;

import codesquad.application.Main;
import codesquad.webserver.annotation.RequestMapping;
import codesquad.webserver.http.HttpMethod;
import codesquad.webserver.util.ClassFinder;
import java.lang.reflect.Method;

public class OperationInitializer {
    public static void init(OperationHandler operationHandler){
        for(Class<?> clazz : ClassFinder.findAllClass(Main.class)){
            try{
                Object handler = clazz.getDeclaredConstructor().newInstance();
                for(Method method : clazz.getDeclaredMethods()){
                    if(method.isAnnotationPresent(RequestMapping.class)){
                        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                        HttpMethod httpMethod = requestMapping.method();
                        String path = requestMapping.path();
                        operationHandler.addRoute(httpMethod, path, new HandlerMethod(handler, method));
                    }
                }
            }catch (Exception e){

            }
        }
    }
}
