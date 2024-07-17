package codesquad.webserver.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class FileWriter {
    private static final Logger log = LoggerFactory.getLogger(FileWriter.class);

    public static void saveFile(byte[] fileData, String fileName){
        String dir = getApplicationDirectory() + "/upload";
        File directory = new File(dir);
        if(!directory.exists()){
            directory.mkdirs();
        }
        File file = new File(directory, fileName);
        try(FileOutputStream fos = new FileOutputStream(file)){
            fos.write(fileData);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getApplicationDirectory() {
        // Get the path of the running JAR file or class
        String path = FileWriter.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        File jarFile = new File(path);
        // Get the directory
        return jarFile.getParentFile().getAbsolutePath();
    }
}
