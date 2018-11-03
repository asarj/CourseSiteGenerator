package djf.modules;

import djf.ui.controllers.AppUndoController;
import djf.ui.controllers.AppHelpController;
import static djf.ui.style.DJFStyle.*;
import djf.ui.controllers.AppFileController;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;
import djf.AppTemplate;
import static djf.AppPropertyType.*;
import djf.components.AppClipboardComponent;
import static djf.modules.AppLanguageModule.FILE_PROTOCOL;
import djf.modules.AppLanguageModule.LanguageException;
import djf.ui.AppNodesBuilder;
import java.net.URL;
import java.util.HashMap;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

/**
 * This class provides the basic user interface for this application, including
 * all the file controls, but not including the workspace, which would be
 * customly provided for each app.
 *
 * @author Richard McKenna
 * @version 1.0
 */
public class AppGUIModule {

    // WE'LL NEED THIS TO ACCESS COMPONENTS
    protected AppTemplate app;

    // THESE WILL HANDLE DEFAULT RESPONSES
    protected AppFileController fileController;
    protected AppHelpController helpController;
    protected AppUndoController undoController;

    // THIS IS THE APPLICATION WINDOW
    protected Stage primaryStage;

    // THIS IS THE STAGE'S SCENE GRAPH
    protected Scene primaryScene;
    
    // CUSTOM DIALOGS
    protected HashMap<Object, Stage> dialogs;

    // THIS PANE ORGANIZES THE BIG PICTURE CONTAINERS FOR THE
    // APPLICATION AppGUIModule. NOTE THAT THE WORKSPACE WILL GO
    // IN THE CENTER REGION OF THE appPane
    protected BorderPane appPane;

    // THIS IS USED FOR BUILDING AppGUIModule CONTROLS
    protected AppNodesBuilder nodesBuilder;
    protected HashMap<String, Node> guiNodes;

    // THIS IS THE TOP PANE WHERE WE CAN PUT TOOLBAR
    protected HBox topToolbarPane;

    // THIS IS THE FILE TOOLBAR AND ITS CONTROLS
    protected HBox fileToolbar;

    // THIS IS FOR THE CUT/COPY/PASTE BUTTONS IF WE'RE USING THEM
    protected HBox clipboardToolbar;

    // THIS IS FOR THE UNDO/REDO BUTTONS IF WE'RE USING THEM
    protected HBox undoToolbar;

    // THIS IS FOR THE HELP/LANGUAGE/ABOUT BUTTONS IF WE'RE USING THEM
    protected HBox helpToolbar;

    // THIS TITLE WILL GO IN THE TITLE BAR
    protected String appTitle;

    // THESE CONSTANTS MAKE IT EASIER TO SEE WHAT'S WHAT IN METHOD CALLS
    public static final boolean ENABLED = true;
    public static final boolean DISABLED = false;

    /**
     * This constructor initializes the application gui object but is not yet
     * ready for use until all langauge settings have been loaded and then
     * loadGUI is called.
     *
     * @param initApp The app for which this gui is used.
     */
    public AppGUIModule(AppTemplate initApp) {
        // SAVE THESE FOR LATER
        app = initApp;
    }

    /**
     * This constructor initializes the file toolbar for use.
     *
     * @param initPrimaryStage The window for this application.
     */
    public void loadGUI(Stage initPrimaryStage) throws LanguageException {
        primaryStage = initPrimaryStage;

        // GET THE TITLE FROM THE XML FILE
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        appTitle = props.getProperty(APP_TITLE);
        primaryStage.setTitle(appTitle);

        // THIS WILL STORE ALL OF OUR APP NODES
        guiNodes = new HashMap();
        
        // THIS WILL STORE ALL OF OUR APP DIALOGS
        dialogs = new HashMap();

        // THIS WILL BUILD ALL THE NODES
        nodesBuilder = new AppNodesBuilder(this, app.getLanguageModule());

        if (props.isTrue(HAS_TOP_TOOLBAR)) {
            // INIT THE TOOLBAR ONLY IF THERE IS ONE
            initTopToolbar(app);
            initFileToolbarStyle();
        }
        // MAKE SURE WE'RE NOT TRYING TO ADD SUB TOOLBARS
        // WITHOUT THE TOP TOOLBAR
        else {
            String FALSE = "false";
            props.addProperty(HAS_FILE_TOOLBAR, FALSE);
            props.addProperty(HAS_CLIPBOARD_TOOLBAR, FALSE);
            props.addProperty(HAS_UNDO_TOOLBAR, FALSE);
            props.addProperty(HAS_HELP_TOOLBAR, FALSE);
        }

        // AND FINALLY START UP THE WINDOW (WITHOUT THE WORKSPACE)
        initWindow();
    }

    public AppFileController getFileController() {
        return fileController;
    }

    public AppHelpController getHelpController() {
        return helpController;
    }

    public AppUndoController getUndoController() {
        return undoController;
    }

    public AppNodesBuilder getNodesBuilder() {
        return nodesBuilder;
    }

    /**
     * Accessor method for getting the application pane, within which all user
     * interface controls are ultimately placed.
     *
     * @return This application GUI's app pane.
     */
    public BorderPane getAppPane() {
        return appPane;
    }

    /**
     * Accessor method for getting all the nodes currently in the UI.
     */
    public void addGUINode(Object nodeId, Node nodeToAdd) {
        guiNodes.put(nodeId.toString(), nodeToAdd);
    }

    /**
     * Accessor method for getting a single node in the UI.
     */
    public Node getGUINode(Object nodeId) {
        return guiNodes.get(nodeId.toString());
    }

    /**
     * Accessor method for getting all the dialogs currently in the UI.
     */
    public void addDialog(Object dialogId, Stage dialogToAdd) {
        dialogs.put(dialogId.toString(), dialogToAdd);
    }

    /**
     * Accessor method for getting a dialog in the UI.
     */
    public Stage getDialog(Object dialogId) {
        return dialogs.get(dialogId.toString());
    }

    /**
     * Accessor method for getting the toolbar pane in the top, within which
     * other toolbars are placed.
     *
     * @return This application GUI's app pane.
     */
    public HBox getTopToolbarPane() {
        return topToolbarPane;
    }

    /**
     * Accessor method for getting the file toolbar, within which all file
     * controls are placed.
     *
     * @return This application GUI's file toolbar.
     */
    public HBox getFileToolbar() {
        return fileToolbar;
    }

    /**
     * Accessor method for getting the cut toolbar, within which all
     * cut/copy/paste controls are placed.
     *
     * @return This application GUI's cut toolbar.
     */
    public HBox getCutToolbar() {
        return clipboardToolbar;
    }

    /**
     * Accessor method for getting the undo toolbar, within which all undo/redo
     * controls are placed.
     *
     * @return This application GUI's undo toolbar.
     */
    public HBox getUndoToolbar() {
        return undoToolbar;
    }

    /**
     * Accesssor method for getting the settings toolbar, within which all
     * settings controls are placed.
     *
     * @return This application GUI's settings toolbar.
     */
    public HBox getSettingsToolbar() {
        return helpToolbar;
    }

    /**
     * Accessor method for getting this application's primary stage's, scene.
     *
     * @return This application's window's scene.
     */
    public Scene getPrimaryScene() {
        return primaryScene;
    }

    /**
     * Accessor method for getting this application's window, which is the
     * primary stage within which the full GUI will be placed.
     *
     * @return This application's primary stage (i.e. window).
     */
    public Stage getWindow() {
        return primaryStage;
    }

    /**
     * *************************************************************************
     */
    /* BELOW ARE ALL THE PRIVATE HELPER METHODS WE USE FOR INITIALIZING OUR AppGUIModule */
    /**
     * *************************************************************************
     */
    /**
     * This function initializes all the buttons in the toolbar at the top of
     * the application window. These are related to file management.
     */
    private void initTopToolbar(AppTemplate app) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // THESE OPTIONAL TOOLBARS WILL GO HERE
        topToolbarPane = new HBox();
        if (props.isTrue(HAS_FILE_TOOLBAR)) {
            initFileToolbar();
        }
        if (props.isTrue(HAS_CLIPBOARD_TOOLBAR)) {
            initClipboardToolbar();
        }
        if (props.isTrue(HAS_UNDO_TOOLBAR)) {
            initUndoToolbar();
        }
        if (props.isTrue(HAS_HELP_TOOLBAR)) {
            initHelpToolbar();
        }

    }

    private void initFileToolbar() {
        fileToolbar = new HBox();
        topToolbarPane.getChildren().add(fileToolbar);
        fileController = new AppFileController(app);
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        if (props.isTrue(HAS_NEW)) {
            Button newButton = nodesBuilder.buildIconButton(NEW_BUTTON, fileToolbar, CLASS_DJF_ICON_BUTTON, ENABLED);
            newButton.setOnAction(e -> {
                fileController.processNewRequest();
            });
        }
        if (props.isTrue(HAS_LOAD)) {
            Button loadButton = nodesBuilder.buildIconButton(LOAD_BUTTON, fileToolbar, CLASS_DJF_ICON_BUTTON, ENABLED);
            loadButton.setOnAction(e -> {
                fileController.processLoadRequest();
            });
        }
        if (props.isTrue(HAS_CLOSE)) {
            Button closeButton = nodesBuilder.buildIconButton(CLOSE_BUTTON, fileToolbar, CLASS_DJF_ICON_BUTTON, DISABLED);
            closeButton.setOnAction(e -> {
                fileController.processCloseRequest();
            });
        }
        if (props.isTrue(HAS_SAVE)) {
            Button saveButton = nodesBuilder.buildIconButton(SAVE_BUTTON, fileToolbar, CLASS_DJF_ICON_BUTTON, DISABLED);
            saveButton.setOnAction(e -> {
                fileController.processSaveRequest();
            });
        }
        if (props.isTrue(HAS_EXPORT)) {
            Button exportButton = nodesBuilder.buildIconButton(EXPORT_BUTTON, fileToolbar, CLASS_DJF_ICON_BUTTON, DISABLED);
            exportButton.setOnAction(e -> {
                fileController.processExportRequest();
            });
        }
        if (props.isTrue(HAS_EXIT)) {
            Button exitButton = nodesBuilder.buildIconButton(EXIT_BUTTON, fileToolbar, CLASS_DJF_ICON_BUTTON, ENABLED);
            exitButton.setOnAction(e -> {
                fileController.processExitRequest();
            });
        }
    }

    // HELPER METHOD FOR INITIALIZING THE CLIPBOARD TOOLBAR
    private void initClipboardToolbar() {
        // NOTE THAT IN ORDER FOR THIS INITIALIZATION TO WORK 
        // PROPERLY THE AppClipboardComponent MUST ALREADY EXIST
        clipboardToolbar = new HBox();
        topToolbarPane.getChildren().add(clipboardToolbar);

        // THIS IS AN ALL OR NOTHING TOOLBAR
        Button cutButton = nodesBuilder.buildIconButton(CUT_BUTTON, clipboardToolbar, CLASS_DJF_ICON_BUTTON, DISABLED);
        Button copyButton = nodesBuilder.buildIconButton(COPY_BUTTON, clipboardToolbar, CLASS_DJF_ICON_BUTTON, DISABLED);
        Button pasteButton = nodesBuilder.buildIconButton(PASTE_BUTTON, clipboardToolbar, CLASS_DJF_ICON_BUTTON, DISABLED);
    }

    public void registerClipboardComponent() {
        AppClipboardComponent clipboard = app.getClipboardComponent();
        if (clipboard != null) {
            ((Button) this.getGUINode(CUT_BUTTON)).setOnAction(e -> {
                clipboard.cut();
            });
            ((Button) this.getGUINode(COPY_BUTTON)).setOnAction(e -> {
                clipboard.copy();
            });
            ((Button) this.getGUINode(PASTE_BUTTON)).setOnAction(e -> {
                clipboard.paste();
            });
        }
    }

    // HELPER METHOD FOR INITIALIZING THE UNDO TOOLBAR    
    private void initUndoToolbar() {
        undoToolbar = new HBox();
        topToolbarPane.getChildren().add(undoToolbar);
        undoController = new AppUndoController(app);

        // THIS IS AN ALL OR NOTHING TOOLBAR
        Button undoButton = nodesBuilder.buildIconButton(UNDO_BUTTON, undoToolbar, CLASS_DJF_ICON_BUTTON, DISABLED);
        undoButton.setOnAction(e -> {
            undoController.processUndoRequest();
        });
        Button redoButton = nodesBuilder.buildIconButton(REDO_BUTTON, undoToolbar, CLASS_DJF_ICON_BUTTON, DISABLED);
        redoButton.setOnAction(e -> {
            undoController.processRedoRequest();
        });
    }

    // HELPER METHOD FOR INITIALIZING THE HELP TOOLBAR        
    private void initHelpToolbar() {
        helpToolbar = new HBox();
        topToolbarPane.getChildren().add(helpToolbar);
        helpController = new AppHelpController(app);
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        if (props.isTrue(HAS_HELP)) {
            Button helpButton = nodesBuilder.buildIconButton(HELP_BUTTON, helpToolbar, CLASS_DJF_ICON_BUTTON, ENABLED);
            helpButton.setOnAction(e -> {
                helpController.processHelpRequest();
            });
        }
        if (props.isTrue(HAS_LANGUAGE)) {
            Button languageButton = nodesBuilder.buildIconButton(LANGUAGE_BUTTON, helpToolbar, CLASS_DJF_ICON_BUTTON, ENABLED);
            languageButton.setOnAction(e -> {
                helpController.processLanguageRequest();
            });
        }
        if (props.isTrue(HAS_ABOUT)) {
            Button aboutButton = nodesBuilder.buildIconButton(ABOUT_BUTTON, helpToolbar, CLASS_DJF_ICON_BUTTON, ENABLED);
            aboutButton.setOnAction(e -> {
                helpController.processAboutRequest();
            });
        }
    }

    // INITIALIZE THE WINDOW (i.e. STAGE) PUTTING ALL THE CONTROLS
    // THERE EXCEPT THE WORKSPACE, WHICH WILL BE ADDED THE FIRST
    // TIME A NEW Page IS CREATED OR LOADED
    private void initWindow() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // SET THE WINDOW TITLE
        primaryStage.setTitle(appTitle);

        // START FULL-SCREEN OR NOT, ACCORDING TO PREFERENCES
        primaryStage.setMaximized("true".equals(props.getProperty(START_MAXIMIZED)));

        // ADD THE TOOLBAR ONLY, NOTE THAT THE WORKSPACE
        // HAS BEEN CONSTRUCTED, BUT WON'T BE ADDED UNTIL
        // THE USER STARTS EDITING A COURSE
        appPane = new BorderPane();
        if (topToolbarPane != null) {
            appPane.setTop(topToolbarPane);
        }
        primaryScene = new Scene(appPane);

        // SET THE APP PANE PREFERRED SIZE ACCORDING TO THE PREFERENCES
        double prefWidth = Double.parseDouble(props.getProperty(PREF_WIDTH));
        double prefHeight = Double.parseDouble(props.getProperty(PREF_HEIGHT));
        appPane.setPrefWidth(prefWidth);
        appPane.setPrefHeight(prefHeight);

        // SET THE APP ICON
        String imagesPath = props.getProperty(APP_PATH_IMAGES);
        String appLogo = FILE_PROTOCOL + imagesPath + props.getProperty(APP_LOGO);
        Image appWindowLogo = new Image(appLogo);
        primaryStage.getIcons().add(appWindowLogo);
        primaryStage.setIconified(true);

        // NOW TIE THE SCENE TO THE WINDOW
        primaryStage.setScene(primaryScene);

        // INIT THE STYLESHEET FOR THE SCENE
        initStylesheet(primaryStage);
    }

    /**
     * This function sets up the stylesheet to be used for specifying all style
     * for this application. Note that it does not attach CSS style classes to
     * controls, that must be done separately.
     */
    public void initStylesheet(Stage stage) {
        // SELECT THE STYLESHEET
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String stylesheet = props.getProperty(APP_PATH_CSS);
        stylesheet += props.getProperty(APP_CSS);
        Class appClass = app.getClass();
        URL stylesheetURL = appClass.getResource(stylesheet);
        String stylesheetPath = stylesheetURL.toExternalForm();
        Scene scene = stage.getScene();
        scene.getStylesheets().add(stylesheetPath);
    }

    /**
     * This function specifies the CSS style classes for the controls managed by
     * this framework.
     */
    private void initFileToolbarStyle() {
        // SET THE STYLE FOR THE TOP TOOLBAR
        topToolbarPane.getStyleClass().add(CLASS_DJF_TOP_TOOLBAR);

        // AND THEN ALL THE TOOLBARS IT CONTAINS
        for (Node toolbar : topToolbarPane.getChildren()) {
            ObservableList<String> styleClasses = toolbar.getStyleClass();
            styleClasses.add(CLASS_DJF_TOOLBAR_PANE);
        }
    }
}
