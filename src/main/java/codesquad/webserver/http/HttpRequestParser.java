package codesquad.webserver.http;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequestParser {
    private static final Logger log = LoggerFactory.getLogger(HttpRequestParser.class);

    public static HttpRequest parse(InputStream inputStream) {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        HttpRequestStartLine startLine = new HttpRequestStartLine(readStartLine(br));
        log.info("[parse] start line "+startLine);
        HttpHeader header = readHeader(br);
        log.info("[parse] header "+header);
        HttpBody body = null;

        if(header.hasKey("Content-Type") && header.hasKey("Content-Length")){
            String contentType = header.getValue("Content-Type");
            String contentLength = header.getValue("Content-Length");
            if(contentType.startsWith("application/x-www-form-urlencoded")){
                body = readUrlEncodedForm(br, Integer.parseInt(contentLength));
            } else if(contentType.startsWith("multipart/form-data")){
                body = readMultipartFormData(br, contentType);
            }
        }
        log.info("[parse] body "+body);
        return new HttpRequest(startLine, header, body);
    }

    private static String readStartLine(BufferedReader br) {
        try {
            String line = br.readLine();
            if (line == null) {
                log.error("");
            }
            return line;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static HttpHeader readHeader(BufferedReader br) {
        try{
            HttpHeader headers = new HttpHeader();
            String line;
            // \r\n 까지 읽는다
            while(!(line = br.readLine()).isEmpty()){
                String[] row = line.split(":\\s*", 2);
                headers.add(row[0], row[1]);
                log.debug("header line: {}", line);
            }
            return headers;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static HttpBody readUrlEncodedForm(BufferedReader br, int contentLength){
        try{
            char[] buffer = new char[contentLength];
            br.read(buffer, 0 , contentLength);
            String mesage = new String(buffer);
            return new HttpBody(HttpRequestBodyParser.parseForm(mesage));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static HttpBody readMultipartFormData(BufferedReader br, String contentType){
        try{
            Map<String, Object> jsonMap = new HashMap<>();
            String boundary = contentType.substring(contentType.indexOf("boundary=") + "boundary=".length());
            log.debug("[readMultipartFormData] boundary: "+ boundary);
            String line;
            while ((line = br.readLine()) != null) {
                log.debug("[readMultipartFormData] line: "+line);
                if (line.startsWith(boundary)) {
                    processFormPart(br, jsonMap, boundary);
                }
                if (line.equals(boundary + "--")) {
                    break;
                }
            }
            return new HttpBody(jsonMap);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void processFormPart(BufferedReader reader, Map<String, Object> formData, String boundary) throws IOException {
        String line = reader.readLine(); // Content-Disposition line
        String[] contentDisposition = line.split(";");
        String name = getFieldName(contentDisposition[1]);

        if (line.contains("filename=")) {
            String filename = getFileName(contentDisposition[2]);
            reader.readLine(); // Content-Type line
            reader.readLine(); // Empty line
            ByteArrayOutputStream fileContent = readBinaryFileData(reader, boundary);
            formData.put(name, new FileData(filename, fileContent.toByteArray()));
        } else {
            reader.readLine(); // Empty line
            StringBuilder value = new StringBuilder();
            while ((line = reader.readLine()) != null && !line.startsWith(boundary)) {
                value.append(line);
            }
            formData.put(name, value.toString());
        }
    }

    private static ByteArrayOutputStream readBinaryFileData(BufferedReader reader, String boundary)
            throws IOException {
        ByteArrayOutputStream fileContent = new ByteArrayOutputStream();
        int ch;
        while ((ch = reader.read()) != -1) {
            if (ch == '\r') {
                reader.mark(1);
                if (reader.read() == '\n') {
                    String nextLine = reader.readLine();
                    if (nextLine.startsWith(boundary)) {
                        break;
                    }
                }
                reader.reset();
            }
            fileContent.write(ch);
        }
        return fileContent;
    }

    private static String getFieldName(String contentDisposition) {
        return contentDisposition.substring(contentDisposition.indexOf("name=") + "name=".length()).replaceAll("\"", "").trim();
    }

    private static String getFileName(String contentDisposition) {
        return contentDisposition.substring(contentDisposition.indexOf("filename=") + "filename=".length()).replaceAll("\"", "").trim();
    }

    record FileData(String filename, byte[] content) {
    }
}
