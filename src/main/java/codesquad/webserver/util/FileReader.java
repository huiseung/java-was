package codesquad.webserver.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileReader {
    private FileReader() {
    }

    private static String STATIC_DIRECTORY_PATH = "./src/main/resources/static";

    public static boolean hasFile(String path) {
        File file = new File(STATIC_DIRECTORY_PATH + path);
        return file.exists();
    }

    public static byte[] readFile(String path) throws IOException {
        File file = new File(STATIC_DIRECTORY_PATH + path);

        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] content = new byte[(int) file.length()];
        int bytesRead = fileInputStream.read(content);
        if (bytesRead != file.length()) {
            throw new IOException("Could not read file");
        }
        fileInputStream.close();
        return content;
    }
}
