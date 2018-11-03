package djf.ui.dialogs;

import djf.AppTemplate;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author McKillaGorilla
 */
public class AppWebDialog extends Stage {

    AppTemplate app;
    WebView webView;

    public AppWebDialog(AppTemplate initApp) {
        // KEEP THIS TO ACCESS THINGS
        app = initApp;

        // THIS WILL BE THE ONLY COMPONENT
        webView = new WebView();

        // NOW PUT THE GRID IN THE SCENE AND THE SCENE IN THE DIALOG
        Scene scene = new Scene(webView);
        this.setScene(scene);

        // MAKE IT MODAL
        this.initOwner(app.getGUIModule().getWindow());
        this.initModality(Modality.APPLICATION_MODAL);
    }

    public void showWebDialog(String htmlFilePath) throws MalformedURLException {
        WebEngine webEngine = webView.getEngine();
        webEngine.documentProperty().addListener(e->{
            // THE PAGE WILL LOAD ASYNCHRONOUSLY, SO MAKE
            // SURE TO GRAB THE TITLE FOR THE WINDOW
            // ONCE IT'S BEEN LOADED
            String title = webEngine.getTitle();
            this.setTitle(title);
        });
        URL pageURL = new File(htmlFilePath).toURI().toURL();
        String pagePath = pageURL.toExternalForm();
        webEngine.load(pagePath);        
        this.showAndWait();
    }

    public void showWebDialog() {
        String html = buildWebContent();
        WebEngine webEngine = webView.getEngine();
        webEngine.documentProperty().addListener(e->{
            // THE PAGE WILL LOAD ASYNCHRONOUSLY, SO MAKE
            // SURE TO GRAB THE TITLE FOR THE WINDOW
            // ONCE IT'S BEEN LOADED
            String title = webEngine.getTitle();
            this.setTitle(title);
        });
        webEngine.loadContent(html);
        this.showAndWait();
    }

    public String buildWebContent() {
        return "<html><body><h3>Override buildWebContent to load dynamic content.</h3></body></html>";
    }
}
