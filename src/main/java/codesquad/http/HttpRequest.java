package codesquad.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequest {
    private final Logger log = LoggerFactory.getLogger(HttpResponse.class);
    private HttpRequestStartLine startLine;
    private HttpHeaders headers;

    public HttpRequest(InputStream inputStream) {
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            startLine = new HttpRequestStartLine(readStartLine(br));
            headers = processHeader(br);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public String getPath() {
        return startLine.getPath();
    }

    public HttpMethod getMethod() {
        return startLine.getMethod();
    }

    public String getQueryString() {return startLine.getQueryString();}

    private String readStartLine(BufferedReader br) throws IOException {
        String line = br.readLine();
        if(line == null){
            throw new IOException("");
        }
        return line;
    }

    private HttpHeaders processHeader(BufferedReader br) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        String line;
        // \r\n 까지 읽는다
        while(!(line = br.readLine()).isEmpty()){
            String[] row = line.split(":");
            headers.add(row[0], row[1].trim());
        }
        return headers;
    }

    @Override
    public String toString() {
        return "HttpRequest{" + startLine +
                "," + headers +
                '}';
    }
}
