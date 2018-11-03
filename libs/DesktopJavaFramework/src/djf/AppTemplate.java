package djf;

import djf.modules.AppGUIModule;
import djf.ui.dialogs.AppDialogsFacade;
import djf.components.*;
import javafx.application.Application;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;
import static djf.AppPropertyType.*;
import djf.ui.foolproof.ClipboardFoolproofDesign;
import djf.ui.foolproof.FileFoolproofDesign;
import djf.ui.foolproof.UndoFoolproofDesign;
import djf.modules.AppFileModule;
import djf.modules.AppFoolproofModule;
import djf.modules.AppLanguageModule;
import djf.modules.AppRecentWorkModule;
import static djf.modules.AppLanguageModule.*;
import jtps.jTPS;
import jtps.jTPS_Transaction;
import properties_manager.InvalidXMLFileFormatException;

/**
 * This is the framework's JavaFX application. It provides the start method
 * that begins the program initialization, which delegates component
 * initialization to the application-specific child class' hook function.
 * 
 * @author Richard McKenna
 * @version 1.0
 */
public abstract class AppTemplate extends Application {
    
    /**
     * Base directory for data files loaded for setting up
     * the application. This directory should have three
     * sub-directores: properties, recent, and web.
     */
    public static final String PATH_DATA = "./app_data/";
    
    /**
     * Directory for all properties files, including app_properties.xml,
     * which has all the non-language settings for loading the 
     * application, and all the language-specific properties files
     * that are used to load text into UI controls whenever the 
     * application language changes.
     */
    public static final String PATH_PROPERTIES = PATH_DATA + "properties/";
    
    /**
     * Directory for storing the recent work file, which keeps track
     * of which files were edited most recently.
     */
    public static final String PATH_RECENT = PATH_DATA + "recent/";
    
    /**
     * Directory for storing Web pages for the Help and About screens
     * as well as any Web pages or Web templates needed by the
     * particular application.
     */
    public static final String PATH_WEB = PATH_DATA + "web/";
    
    /**
     * Directory where user work is saved by default.
     */
    public static final String PATH_WORK = "./work/";
    public static final String PATH_ICONS = "./images/icons/";
    public static final String PATH_TEMP = "./temp/";
    
    // APP MODULES PROVIDE IMPORTANT APP SERVICES AND
    // USE FILES WITH APPLICATION-DEPENDENT DATA THAT
    // CAN CHANGE DEPENDING ON THE APPLICDATION NEED
    protected AppFileModule         fileModule = new AppFileModule(this);
    protected AppFoolproofModule    foolproofModule = new AppFoolproofModule(this);
    protected AppGUIModule          guiModule = new AppGUIModule(this);
    protected AppLanguageModule     languageModule = new AppLanguageModule(this);
    protected AppRecentWorkModule   recentWorkModule = new AppRecentWorkModule(this);

    // COMPONENTS ARE CLASSES WHOSE IMPLEMENTATIONS ARE
    // APPLICATION-DEPENDENT, AND SO THEY ARE PROVIDED
    // BY THE APPLICATION DEVELOPER. AppTemplate HAS
    // FOUR COMPONENT OBJECTS
    protected AppClipboardComponent clipboardComponent;
    protected AppDataComponent      dataComponent;
    protected AppFileComponent      fileComponent;
    protected AppWorkspaceComponent workspaceComponent;

    // jTPS IS FOR MANAGING UNDO/REDO, NOTE THAT IT
    // IS AN EXTERNAL DEPENDENCY, MEANING IT IS NOT
    // DEFINED INSIDE OF THIS PROJECT
    protected jTPS tps = new jTPS();
        
    // APP ACCESSOR METHODS ARE PROVIDED FOR
    // ALL MODULES AND COMPONENTS AS WELL AS
    // FOR THE SHARED TRANSACTION PROCESSING SYSTEM
 
    /**
     * Accessor method for getting the app's file module, which
     * provides appropriate responses for interations with the
     * file toolbar.
     * 
     * @return The AppFileModule object for this app.
     */
    public AppFileModule getFileModule() {
        return fileModule;
    }

    /**
     * Accessor method for getting the app's foolproof module, which
     * enables/disables controls as needed.
     * 
     * @return The AppFoolproofModule object for this app.
     */
    public AppFoolproofModule getFoolproofModule() {
        return foolproofModule;
    }

    /**
     * Accessor method for getting the app's gui module, which
     * provides access to all the user interface controls.
     * 
     * @return the AppGUIModule for this app.
     */
    public AppGUIModule getGUIModule() { 
        return guiModule; 
    }

    /**
     * Accessor method for getting the app's language module, which
     * manages changing UI text according to the loaded language.
     * 
     * @return the AppLanguageModeul for this app.
     */
    public AppLanguageModule getLanguageModule() {
        return languageModule;
    }

    /**
     * Accessor method for getting the app's recent work module, which
     * keeps track of what files the user has been editing most recently.
     * 
     * @return the AppRecentWorkModule for this app.
     */
    public AppRecentWorkModule getRecentWorkModule() {
        return recentWorkModule;
    }

    /**
     * Accessor method for getting the app's clipboard component, which
     * is an app-dependent object that manages how selected items are
     * cut, copied, and pasted in the application.
     * 
     * @return the AppClipboardComponent for this app.
     */
    public AppClipboardComponent getClipboardComponent() { 
        return clipboardComponent; 
    }

    /**
     * Accessor method for getting the app's data component, which
     * is an app-dependent object that manages access to the app's data
     * and how it pulls from and pushes to user interface components.
     * 
     * @return the AppDataComponent for this app.
     */
    public AppDataComponent getDataComponent() { 
        return dataComponent;
    }

    /**
     * Accessor method for getting the app's file component, which
     * is an app-dependent object that manages saving, loading, exporting,
     * and importing to and from files in specific file formats as 
     * determined by the needs of the given application.
     * 
     * @return the AppFileComponent for this app.
     */
    public AppFileComponent getFileComponent() { 
        return fileComponent; 
    }

    /**
     * Accessor method for getting the app's workspace component, which
     * loads the controls needed for the app's workspace.
     * 
     * @return the AppWorkspaceComponent for this app.
     */
    public AppWorkspaceComponent getWorkspaceComponent() { 
        return workspaceComponent; 
    }

    /**
     * Accessor method for getting the app's transaction processing system object.
     * 
     * @return the application's jTPS, which manages all app transactions
     * for its Undo/Redo system.
     */
    public jTPS getTPS() { 
        return tps; 
    }
    
    /**
     * This is where our Application begins its initialization, it will load
     * the custom app properties, build the modules and components, and open
     * the window.
     *
     * @param primaryStage This application's window.
     */
    @Override
    public void start(Stage primaryStage) {
        // FIRST SETUP THE PropertiesManager WITH
        // IT'S MINIMAL LANGUAGE PROPERTIES IN CASE OF ERROR
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        props.addProperty(APP_ERROR_TITLE, "Application Error");
        props.addProperty(APP_ERROR_CONTENT, "An Error Occured in the Application");
        props.addProperty(PROPERTIES_ERROR_TITLE, "Properties File Error");
        props.addProperty(PROPERTIES_ERROR_CONTENT, "There was an Error Loading a Properties File");        
        
	try {
            // NOW LOAD THE APP PROPERTIES, WHICH CONTAINS THE UI SETTINGS
            // THAT ARE NOT LANGUAGE-SPECIFIC.
	    boolean success = loadProperties(APP_PROPERTIES_FILE_NAME);
	    
            // IF THE APPLICATION PROPERTIES LOADS
            // SUCCESSFULLY THEN WE WILL PROCEED
	    if (success) {         
                // MAKE SURE ALL OUR NODES WITH TEXT WILL
                // BE PROPERLY MANAGED SUCH THAT WE CAN LOAD
                // LANGUAGE-DEPENDENT TEXT
                languageModule.init();
                
                // LOAD THE GUI INTO THE WINDOW (i.e. Stage)
		guiModule.loadGUI(primaryStage);

                // THIS BUILDS ALL OF THE APP-DEPENDENT COMPONENTS IN
                // A PARTICULAR ORDER WHERE THE CONSTRUCTION OF EACH
                // COMPONENT IS UNIQUE TO THE APP
		buildAppComponents();

                // SETUP THE CLIPBOARD, IF IT'S BEING USED, NOTE THAT
                // THIS WOULD HAVE BEEN SPECIFIED IN THE PROPERTIES FILE
                // THAT WE LOADED
                if (props.isTrue(HAS_CLIPBOARD_TOOLBAR)) {
                    guiModule.registerClipboardComponent();
                }

                // LOAD THE FOOLPROOF SETTINGS THAT WE'LL BE USING. THESE
                // MANAGE THE ENABLING AND DISABLING OF SPECIFIC APP
                // CONTROLS DEPENDING ON CERTAIN CONDITIONS
                if (props.isTrue(HAS_FILE_TOOLBAR)) {
                    foolproofModule.registerModeSettings(APP_FILE_FOOLPROOF_SETTINGS, 
                                                        new FileFoolproofDesign(this));
                }
                if (props.isTrue(HAS_CLIPBOARD_TOOLBAR)) {
                    foolproofModule.registerModeSettings(APP_CLIPBOARD_FOOLPROOF_SETTINGS, 
                                                        new ClipboardFoolproofDesign(this));
                }
                if (props.isTrue(HAS_UNDO_TOOLBAR)) {
                    foolproofModule.registerModeSettings(APP_UNDO_FOOLPROOF_SETTINGS, 
                                                         new UndoFoolproofDesign(this));
                }

                // LOAD ALL THE PROPER TEXT INTO OUR CONTROLS
                // USING THE DEFAULT LANGUAGE SETTING
                languageModule.resetLanguage();
                
                // OPEN THE WELCOME DIALOG, IF IT'S BEING USED
                if (props.isTrue(HAS_WELCOME_DIALOG)) {
                    // NOTE, THIS WINDOW WILL HAVE TO BE CLOSED
                    // BEFORE CONTINUING ON TO THE APPLICATION WINDOW
                    AppDialogsFacade.showWelcomeDialog(this);                
                }
                
                // NOW OPEN UP THE APPLICATION WINDOW
                primaryStage.show();
	    } 
	}catch (Exception e) {
            // THIS TYPE OF ERROR IS LIKELY DUE TO PROGRAMMER ERROR IN
            // THE APP ITSELF SO WE'LL PROVIDE A STACK TRACE DIALOG AND EXIT
            AppDialogsFacade.showStackTraceDialog(guiModule.getWindow(), e, APP_ERROR_TITLE, APP_ERROR_CONTENT);
            System.exit(0);
	}
    }
    
    /**
     * This function adds the transaction argument to the transaction processing
     * system, which will also do it. It then forces a call to update all the
     * controls according to the rules registered with the foolproof module such
     * that unusable controls are disabled and usable controls are enabled.
     * 
     * @param transaction This is the transaction that will be added to the
     * applicaiton's transaction processing system and then executed.
     */
    public void processTransaction(jTPS_Transaction transaction) {
        tps.addTransaction(transaction);
        fileModule.markAsEdited(true);
    }
    
    // ABSTRACT FUNCTIONS TO BE IMPLEMENTED IN CONCRETE CHILD CLASSES
    
    /**
     * This function must construct and return the clipboard component to
     * be used for this application.
     * 
     * @param app The application object, which provides access to all modules
     * and components.
     * 
     * @return The application-dependent clipboard component.
     */
    public abstract AppClipboardComponent buildClipboardComponent(AppTemplate app);

    /**
     * This function must construct and return the data component to
     * be used for this application.
     * 
     * @param app The application object, which provides access to all modules
     * and components.
     * 
     * @return The application-dependent data component.
     */    
    public abstract AppDataComponent buildDataComponent(AppTemplate app);

    /**
     * This function must construct and return the file component to
     * be used for this application.
     * 
     * @param app The application object, which provides access to all modules
     * and components.
     * 
     * @return The application-dependent file component.
     */
    public abstract AppFileComponent buildFileComponent();

    /**
     * This function must construct and return the workspace component to
     * be used for this application.
     * 
     * @param app The application object, which provides access to all modules
     * and components.
     * 
     * @return The application-dependent workspace component.
     */    
    public abstract AppWorkspaceComponent buildWorkspaceComponent(AppTemplate app);
    
    // private HELPER FUNCTIONS
    
    // loadProperties LOADS THIS APPLICATION'S PROPERTIES FILE, WHICH HAS
    // A NUMBER OF SETTINGS FOR INITIALIZING THE USER INTERFACE EXCLUDING
    // THE TEXT TO BE DISPLAYED INSIDE CONTROLS, WHICH IS TO BE LOADED
    // ON A LANGUAGE-TO-LANGUAGE BASIS
    private boolean loadProperties(String propertiesFileName) {
	    PropertiesManager props = PropertiesManager.getPropertiesManager();
	try {
	    // LOAD THE SETTINGS FOR STARTING THE APP
	    props.setPropertiesDataPath(PATH_PROPERTIES);
	    props.loadProperties(propertiesFileName);
	    return true;
	} catch (InvalidXMLFileFormatException ixmlffe) {
	    // SOMETHING WENT WRONG INITIALIZING THE XML FILE
	    AppDialogsFacade.showMessageDialog(guiModule.getWindow(), PROPERTIES_ERROR_TITLE, PROPERTIES_ERROR_CONTENT);
	    return false;
	}
    }

    // THIS FUNCTION BUILDS THE COMPONENTS BY CALLING THE abstract
    // FUNCTIONS OF THIS OBJECT THAT ARE TO BE IMPLEMENTED BY 
    // THE APPLICATION.
    private void buildAppComponents() {
        // MAKE ALL FOUR COMPONENTS FIRST
        this.fileComponent = buildFileComponent();
        this.workspaceComponent = buildWorkspaceComponent(this);
        this.dataComponent = buildDataComponent(this);
        this.clipboardComponent = buildClipboardComponent(this);        
    }
}