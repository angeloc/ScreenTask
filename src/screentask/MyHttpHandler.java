/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screentask;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 *
 * @author Ahmad
 */
public class MyHttpHandler implements HttpHandler {

    private HttpExchange _httpExchange;
    private OutputStream _outputStream;
    private ScreenShotManager smanager = ScreenShotManager.getInstance();

    public void handle(HttpExchange httpExchange) throws IOException, MalformedURLException {
        _httpExchange = httpExchange;

        Resources resource = new Resources();

        String webServerPath = "";
        try {
            webServerPath = resource.appStartUpPath() + "/WebServer/";

        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        }

        String path = "";

        boolean isImage = false;
        URI uri = httpExchange.getRequestURI();

        if (uri.getPath().equals("/")) // Route The Root Dir to the Index Page
        {
            path += "index.html";
        } else if (uri.getPath().equals("/bootstrap.min.css")) {
            path += "bootstrap.min.css";
        } else if (uri.getPath().contains("/ScreenTask.jpg")) {
            isImage = true;
        } else {
            path += "";//uri.getPath().toString();
        }

        String filePath = webServerPath + path;
        boolean fileExist;
        File file = new File(filePath);

        if (!isImage) {
            fileExist = file.exists();
        } else {
            fileExist = true;
        }

        _outputStream = httpExchange.getResponseBody();

        if (fileExist) {
            if (isImage) {
                byte[] img = smanager.getImage();
                _httpExchange.sendResponseHeaders(200, img.length);
                _outputStream.write(img);
            } else {
                String content = Resources.readFileAsString(file);
                httpExchange.sendResponseHeaders(200, content.length());
                _outputStream.write(content.getBytes());
            }

            _outputStream.close();

        } else {
            String errorPage = WebServer.errorPageContent();
            httpExchange.sendResponseHeaders(200, errorPage.length());
            _outputStream.write(errorPage.getBytes());
            _outputStream.close();
        }
    }
}
