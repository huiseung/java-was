package codesquad.webserver.handler;

import codesquad.webserver.http.ContentTypeMapping;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpResponse;
import codesquad.webserver.util.FileReader;

import java.io.IOException;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StaticResourceHandler implements Handler {
    private static final Logger log = LoggerFactory.getLogger(DynamicResourceHandler.class);

    public HttpResponse handle(HttpRequest request) {
        return handle(request.getPath());
    }

    public static HttpResponse handle(String path) {
        log.info("StaticResourceHandler handle");
        if (!FileReader.hasFile(path)) {
            return HttpResponse.createNotFoundResponse();
        }
        String content = FileReader.readFileToString(path);
        byte[] body = FileReader.stringToByteArray(content);
        String fileExtension = path.substring(path.lastIndexOf("."));
        return HttpResponse.createOkResponse(fileExtension, body);

    }

    public boolean canHandle(HttpRequest request) {
        Pattern URL_PATTERN = Pattern.compile(".*" + "(" + String.join("|", ContentTypeMapping.getFileExtensions()) + ")$", Pattern.CASE_INSENSITIVE);
        return URL_PATTERN.matcher(request.getPath()).matches();
    }
}
