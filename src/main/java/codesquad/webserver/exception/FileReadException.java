package codesquad.webserver.exception;

import java.io.IOException;

public class FileReadException extends IOException {
    public FileReadException(String message){
        super(message);
    }
    public FileReadException(Exception e){
        super(e);
    }
}
