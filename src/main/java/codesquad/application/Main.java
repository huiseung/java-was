package codesquad.application;


import codesquad.webserver.annotation.ClassScan;
import codesquad.webserver.server.WebServer;

@ClassScan
public class Main {
    public static void main(String[] args) {
        WebServer webServer = new WebServer();
        webServer.run();
    }
}
