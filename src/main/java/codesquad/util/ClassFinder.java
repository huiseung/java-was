package codesquad.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public final class ClassFinder {
    private static Logger log = LoggerFactory.getLogger(ClassFinder.class);
    private ClassFinder(){}

    public static List<Class<?>> findAllClass(Class<?> startClass){
        String packageName = startClass.getPackageName();
        String packagePath = packageName.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL packageURL = classLoader.getResource(packagePath);
        if(packageURL == null){
            log.error("패키지를 찾을 수 없습니다: "+packageURL);
        }
        File packageDirectory = new File(packageURL.getFile());
        return findClasses(packageDirectory, packageName);
    }

    private static List<Class<?>> findClasses(File directory, String packageName){
        List<Class<?>> classes = new ArrayList<>();
        if(!directory.exists()){
            return classes;
        }

        File[] files = directory.listFiles();
        for(File file : files){
            if(file.isDirectory()){
                classes.addAll(findClasses(file, packageName+"."+file.getName()));
            }
            else if(file.getName().endsWith(".class")){
                String className = packageName + '.' + file.getName().substring(0, file.getName().length() - ".class".length());
                try{
                    classes.add(Class.forName(className));
                } catch (ClassNotFoundException e) {
                    log.error("클래스를 로드 할 수 없습니다: "+className);
                }
            }
        }
        return classes;
    }

}
