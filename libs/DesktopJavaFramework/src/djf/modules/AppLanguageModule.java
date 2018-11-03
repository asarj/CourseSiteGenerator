package djf.modules;

import djf.AppTemplate;
import static djf.AppPropertyType.LANGUAGE_OPTIONS;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.beans.property.StringProperty;
import properties_manager.InvalidXMLFileFormatException;
import properties_manager.PropertiesManager;

/**
 * This class manages the language settings for the application.
 *
 * @author Richard McKenna
 * @version 1.0
 */
public class AppLanguageModule {
    // WE NEED THESE CONSTANTS JUST TO GET STARTED
    // LOADING SETTINGS FROM OUR XML FILES

    // XML PROPERTIES FILE WHERE ALL LANGUAGE-SPECIFIC TEXT CAN BE FOUND
    public static final String APP_PROPERTIES_FILE_NAME = "app_properties.xml";
    public static final String LANGUAGE_PROPERTIES_PREFIX = "language_properties_";
    public static final String XML_EXT = ".xml";
    public static final String LANGUAGE_ABBREVIATION_SUFFIX = "_ABBREVIATION";
    public static final String DEFAULT_LANGUAGE_PROPERTY = "DEFAULT_LANGUAGE";

    // PROTOCOLS AND PATHS NEEDED FOR LOADING CERTAIN FILES
    public static final String FILE_PROTOCOL = "file:";
    public static final String PATH_EMPTY = ".";

    // ERROR MESSAGE ASSOCIATED WITH PROPERTIES FILE LOADING ERRORS.
    // NOTE THAT THE REASON WE CAN'T LOAD THIS FROM THE XML FILE IS
    // THAT WE DISPLAY IT WHEN THE LOADING OF THAT FILE FAILS
    public static String PROPERTIES_FILE_ERROR_MESSAGE = "Error Loading " + APP_PROPERTIES_FILE_NAME;

    // ERROR DIALOG CONTROL
    public static String CLOSE_BUTTON_LABEL = "Close";

    // LANGUAGE SETTINGS
    private String currentLanguage;
    private HashMap<String, String> languageCodes;
    private ArrayList<String> languages;
    private AppTemplate app;

    // THESE ARE THE CONTROLS THAT HAVE TO BE UPDATED EACH
    // TIME THE APP LANGUAGE CHANGES
    protected HashMap<String, StringProperty> labeledControlProperties;

    /**
     * Constructor, it keeps the app for later.
     */
    public AppLanguageModule(AppTemplate initApp) {
        app = initApp;
        labeledControlProperties = new HashMap();
    }

    public void addLabeledControlProperty(Object key, StringProperty textProperty) {
        labeledControlProperties.put(key.toString(), textProperty);
    }

    public void loadLanguageSettings(String initLanguage) throws LanguageException {
        currentLanguage = initLanguage;
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String languagePropsFilePath = getLanguagePropertiesFilePath();

        try {
            props.loadProperties(languagePropsFilePath);
            resetLanguage();
        } catch (InvalidXMLFileFormatException ixffe) {
            throw new LanguageException("Error loading " + languagePropsFilePath);
        }
    }

    public String getLanguagePropertiesFilePath() {
        String post = languageCodes.get(currentLanguage) + XML_EXT;
        return LANGUAGE_PROPERTIES_PREFIX + post;
    }

    /**
     * This method initializes the languages for use.
     *
     * @throws LanguageException This method reflects a LanguageException
     * whenever there is a error setting up the languages.
     */
    public void init() throws LanguageException {
        try {
            this.languageCodes = new HashMap();
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            languages = props.getPropertyOptionsList(LANGUAGE_OPTIONS);
            
            // ADD EACH LANGUAGE ALONG WITH ITS ABBREVIATION
            for (String languageName : languages) {
                String abbreviationPropName = languageName.toUpperCase() + LANGUAGE_ABBREVIATION_SUFFIX;
                String abbreviation = props.getProperty(abbreviationPropName);
                this.languageCodes.put(languageName, abbreviation);
            }
            
            // NOW LOAD THE LAST 
            loadDefaultLanguage();
        } catch (Exception e) {
            throw new LanguageException("Unable to Load Language Settings");
        }
    }

    private void loadDefaultLanguage() throws LanguageException {
        // AND FINALLY, GET THE DEFAULT LANGUAGE
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String defaultLanguage = props.getProperty(DEFAULT_LANGUAGE_PROPERTY);
        loadLanguageSettings(defaultLanguage);
    }

    public void resetLanguage() throws LanguageException {
        // RELOAD ALL DJF CONTROLS WITH SELECTED LANGUAGE
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        for (String propertyKey : labeledControlProperties.keySet()) {
            StringProperty property = labeledControlProperties.get(propertyKey);
            String text = props.getProperty(propertyKey);
            property.setValue(text);
        }
    }

    public void setCurrentLanguage(String initCurrentLanguage) throws LanguageException {
        loadLanguageSettings(initCurrentLanguage);
        resetLanguage();
    }

    public String getCurrentLanguage() {
        return currentLanguage;
    }

    public ArrayList<String> getLanguages() {
        return languages;
    }

    public class LanguageException extends Exception {

        public LanguageException(String initMessage) {
            super(initMessage);
        }
    }
}
