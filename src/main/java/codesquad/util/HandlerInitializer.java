package codesquad.util;

import codesquad.Main;
import codesquad.annotation.RequestHandler;
import codesquad.annotation.RequestMapping;
import codesquad.handler.Handler;
import codesquad.handler.HandlerMapping;
import codesquad.http.HttpMethod;
import java.lang.reflect.Method;
import java.util.List;

public class HandlerInitializer {
    public static void initialize() {
        List<Class<?>> classes = ClassFinder.findAllClass(Main.class);
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(RequestHandler.class)) {
                for (Method method : clazz.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(RequestMapping.class)) {
                        RequestMapping mapping = method.getAnnotation(RequestMapping.class);
                        HttpMethod httpMethod = mapping.method();
                        String path = mapping.path();
                        try {
                            Handler handlerInstance = (Handler) clazz.getDeclaredConstructor().newInstance();
                            HandlerMapping.addHandler(httpMethod, path, handlerInstance);
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
        HandlerMapping.print();
    }
}
