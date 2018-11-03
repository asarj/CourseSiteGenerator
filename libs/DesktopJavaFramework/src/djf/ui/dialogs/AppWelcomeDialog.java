package djf.ui.dialogs;

import static djf.AppPropertyType.APP_BANNER;
import static djf.AppPropertyType.APP_LOGO;
import static djf.AppPropertyType.APP_PATH_IMAGES;
import static djf.AppPropertyType.LOAD_ERROR_CONTENT;
import static djf.AppPropertyType.LOAD_ERROR_TITLE;
import static djf.AppPropertyType.NEW_SUCCESS_CONTENT;
import static djf.AppPropertyType.NEW_SUCCESS_TITLE;
import static djf.AppPropertyType.WELCOME_DIALOG_NEW_BUTTON_TEXT;
import static djf.AppPropertyType.WELCOME_DIALOG_NONE_LABEL;
import static djf.AppPropertyType.WELCOME_DIALOG_RECENT_WORK_LABEL;
import static djf.AppPropertyType.WELCOME_DIALOG_TITLE;
import djf.AppTemplate;
import djf.modules.AppRecentWorkModule;
import static djf.modules.AppLanguageModule.FILE_PROTOCOL;
import static djf.ui.style.DJFStyle.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import properties_manager.PropertiesManager;

/**
 *
 * @author McKillaGorilla
 */
public class AppWelcomeDialog extends Stage {

    // LEFT PANE
    VBox recentlyEditedPane;
    Label recentWorkLabel;
    ArrayList<Button> recentWorkButtons;
    
    // RIGHT PANE
    VBox splashPane;
    ImageView welcomeDialogImageView;
    HBox newPane;
    Button createNewButton;
    
    String selectedPath = null;
    String selectedWorkName = null;

    public AppWelcomeDialog(AppTemplate app) {
        // GET THE RECENT WORK
        AppRecentWorkModule recentWork = app.getRecentWorkModule();
                
        // WE'LL NEED THIS
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // LEFT PANE 
        recentlyEditedPane = new VBox();
        String recentWorkText = props.getProperty(WELCOME_DIALOG_RECENT_WORK_LABEL);
        recentWorkLabel = new Label(recentWorkText);
        recentlyEditedPane.getChildren().add(recentWorkLabel);
        recentWorkButtons = new ArrayList();
        
        // IF THERE IS NO RECENT WORK THEN WE'LL JUST PUT A LABEL THERE SAYING SO
        if (recentWork.size() == 0) {
            String noneText = props.getProperty(WELCOME_DIALOG_NONE_LABEL);
            Label noneLabel = new Label(noneText);
            recentlyEditedPane.getChildren().add(noneLabel);
        }
        else {        
            Iterator<String> it = recentWork.getWorkIterator();
            while (it.hasNext()) {
               String workName = it.next();            
               Button workButton = new Button(workName);
               recentWorkButtons.add(workButton);
                workButton.setUserData(workName);
                workButton.setOnAction(e -> {
                    Button b = (Button) e.getSource();
                    selectedWorkName = (String)b.getUserData();
                    String workPath = recentWork.getPath(workName);
                    File workFile = new File(workPath);
                    recentWork.startWork(workFile);
                    try {
                        app.getFileModule().loadWork(workFile);
                        this.hide();
                        app.getGUIModule().getWindow().show();
                    }
                    catch(IOException ioe) {
                        AppDialogsFacade.showMessageDialog(app.getGUIModule().getWindow(), LOAD_ERROR_TITLE, LOAD_ERROR_CONTENT);
                    }
                });
                recentlyEditedPane.getChildren().add(workButton);
            }
        }

        // RIGHT PANE
        splashPane = new VBox();
        welcomeDialogImageView = new ImageView();
        try {
            String bannerFileName = props.getProperty(APP_BANNER);
            String bannerPath = props.getProperty(APP_PATH_IMAGES) + "/" + bannerFileName;
            File bannerFile = new File(bannerPath);
            BufferedImage bufferedImage = ImageIO.read(bannerFile);
            Image bannerImage = SwingFXUtils.toFXImage(bufferedImage, null);
            welcomeDialogImageView.setImage(bannerImage);
        } catch (IOException ex) {
            ex.printStackTrace();
        }        
        String createNewText = props.getProperty(WELCOME_DIALOG_NEW_BUTTON_TEXT);
        createNewButton = new Button(createNewText);
        createNewButton.setOnAction(e->{
            this.hide();
            app.getGUIModule().getWindow().show();
            app.getGUIModule().getFileController().processNewRequest();
        });
        newPane = new HBox();
        newPane.setAlignment(Pos.CENTER);
        newPane.getChildren().add(createNewButton);
        splashPane.getChildren().add(welcomeDialogImageView);
        splashPane.getChildren().add(newPane);
        
        // WE ORGANIZE EVERYTHING IN HERE
        BorderPane dialogPane = new BorderPane();
        dialogPane.setLeft(recentlyEditedPane);
        dialogPane.setCenter(splashPane);

        // MAKE AND SET THE SCENE
        Scene dialogScene = new Scene(dialogPane);
        this.setScene(dialogScene);
        
        for (Button b : recentWorkButtons) {
            b.setOnMouseEntered(e->{
                dialogScene.setCursor(Cursor.HAND);
            });
            b.setOnMouseExited(e->{
                dialogScene.setCursor(Cursor.DEFAULT);
            });
        }

        // PUT EVERYTHING IN THE DIALOG WINDOW
        String dialogTitle = props.getProperty(WELCOME_DIALOG_TITLE);
        this.setTitle(dialogTitle);
        
        // SET THE APP ICON
        String imagesPath = props.getProperty(APP_PATH_IMAGES);
        String appLogo = FILE_PROTOCOL + imagesPath + props.getProperty(APP_LOGO);
        Image appWindowLogo = new Image(appLogo);
        this.getIcons().add(appWindowLogo);

        // SPECIFY THE STYLE THE BANNER AND NEW BUTTON
        app.getGUIModule().initStylesheet(this);
        welcomeDialogImageView.getStyleClass().add(CLASS_DJF_WELCOME_BANNER);
        recentlyEditedPane.getStyleClass().add(CLASS_DJF_WELCOME_RECENT_PANE);
        recentWorkLabel.getStyleClass().add(CLASS_DJF_WELCOME_HEADER);
        newPane.getStyleClass().add(CLASS_DJF_WELCOME_NEW_PANE);
        createNewButton.getStyleClass().add(CLASS_DJF_WELCOME_NEW_BUTTON);
        for (Button recentWorkButton : recentWorkButtons) {
            recentWorkButton.getStyleClass().add(CLASS_DJF_WELCOME_RECENT_BUTTON);
        }
    }
}