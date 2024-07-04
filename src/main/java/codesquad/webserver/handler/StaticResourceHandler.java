package codesquad.webserver.handler;

import codesquad.webserver.http.ContentTypeMapping;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpResponse;
import codesquad.webserver.util.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

public class StaticResourceHandler implements Handler{
    public HttpResponse handle(HttpRequest request){
        return handle(request.getPath());
    }

    public static HttpResponse handle(String path){
        if(!FileReader.hasFile(path)){
            return HttpResponse.createNotFoundResponse();
        }
        try{
            byte[] body = FileReader.readFile(path);
            String fileExtension = path.substring(path.lastIndexOf("."));
            return HttpResponse.createOkResponse(fileExtension, body);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean canHandle(HttpRequest request) {
        Pattern URL_PATTERN = Pattern.compile(".*" + "("+String.join("|", ContentTypeMapping.getFileExtensions()) + ")$", Pattern.CASE_INSENSITIVE);
        return URL_PATTERN.matcher(request.getPath()).matches();
    }
}
