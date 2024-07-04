package codesquad.http;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import codesquad.util.FileReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponse {
    private final Logger log = LoggerFactory.getLogger(HttpResponse.class);
    private final DataOutputStream outputStream;
    private final Map<String, String> headers = new HashMap<>();
    public HttpResponse(OutputStream outputStream) {
        this.outputStream = new DataOutputStream(outputStream);
    }

    public void setBodyFile(String path){
        try{
            byte[] body = FileReader.readFile(path);
            String fileExtension = path.substring(path.lastIndexOf("."));

            writeSuccessStatusLine();
            headers.put("Content-Length", body.length+"");
            headers.put("Content-Type", ContentTypeMapping.getContentType(fileExtension));
            writeHeader();
            writeResponseBody(body);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void setBodyMessage(String body) {
        byte[] contents = body.getBytes();
        writeSuccessStatusLine();
        headers.put("Content-Type", ContentTypeMapping.getContentType(".html"));
        headers.put("Content-Length", contents.length +"");
        writeHeader();
        writeResponseBody(contents);
    }

    public void setNotFound(){
        writeNotFoundStatusLine();
        setBodyMessage("404 Not Found");
    }

    public void sendAndKeepConnection(){
        try{
            outputStream.flush();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void writeSuccessStatusLine(){
        try{
            outputStream.writeBytes("HTTP/1.1 200 OK\r\n");
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void writeNotFoundStatusLine(){
        try{
            outputStream.writeBytes("HTTP/1.1 404 NOT_FOUND\r\n");
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void writeEmptyLine(){
        try{
            outputStream.writeBytes("\r\n");
        }catch (IOException e){
            log.error(e.getMessage());
        }
    }

    private void writeResponseBody(byte[] body){
        try{
            outputStream.write(body, 0, body.length);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void writeHeader(){
        try{
            for(String key : headers.keySet()){
                outputStream.writeBytes(key+": "+headers.get(key)+"\r\n");
            }
            writeEmptyLine();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                ", headers=" + headers +
                '}';
    }
}
