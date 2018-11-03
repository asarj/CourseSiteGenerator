package djf.modules;

import static djf.AppPropertyType.APP_FILE_FOOLPROOF_SETTINGS;
import static djf.AppPropertyType.APP_TITLE;
import djf.AppTemplate;
import java.io.File;
import java.io.IOException;
import static djf.AppPropertyType.APP_UNDO_FOOLPROOF_SETTINGS;
import javafx.application.Platform;
import properties_manager.PropertiesManager;

/**
 *
 * @author McKillaGorilla
 */
public class AppFileModule {
    // HERE'S THE APP
    AppTemplate app;
    
    // WE WANT TO KEEP TRACK OF WHEN SOMETHING HAS NOT BEEN SAVED
    boolean saved;
    
    // THIS IS THE FILE FOR THE WORK CURRENTLY BEING WORKED ON
    File workFile;

    public AppFileModule(AppTemplate initApp) {
        app = initApp;
        saved = true;
        workFile = null;
    }    
    
    public File getWorkFile() {
        return workFile;
    }
    
    /**
     * @todo
     * 
     * This method marks the appropriate variable such that we know
     * that the current Work has been edited since it's been saved.
     * The UI is then updated to reflect this.
     * 
     * @param gui The user interface editing the Work.
     */
    public void markAsEdited(boolean edited) {
        // THE WORK IS NOW DIRTY
        saved = !edited;
        
        // LET THE UI KNOW
        app.getFoolproofModule().updateAll();
    }

    /**
     * Accessor method for checking to see if the current work has been saved
     * since it was last edited.
     *
     * @return true if the current work is saved to the file, false otherwise.
     */
    public boolean isSaved() {
        return saved;
    }
    
    /**
     * Accessor method for checking to see if the the current work has ever
     * been saved since its creation.
     * 
     * @return true if it has been saved at least once.
     */
    public boolean wasSaved() {
        return workFile != null;
    }
    
    public void newWork() {
        // RESET THE DATA
        app.getDataComponent().reset();
        
        // CLEAR OUT ANY OLD TRANSACTIONS
        app.getTPS().clearAllTransactions();
        
        // MAKE SURE THE WORKSPACE IS ACTIVATED
        app.getWorkspaceComponent().activate();
        
        // WORK IS NOT SAVED
        saved = false;
        workFile = null;

        // SET THE WINDOW TITLE
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        app.getGUIModule().getWindow().setTitle(props.getProperty(APP_TITLE));
        
        // RESET THE UI CONTROLS
        app.getFoolproofModule().updateAll();
    }
    
    public void closeWork() {
        // DEACTIVATE THE WORKSPACE
        app.getWorkspaceComponent().deactivate();
        
        // WORK IS NOT SAVED
        saved = true;
        workFile = null;

        // SET THE WINDOW TITLE
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        app.getGUIModule().getWindow().setTitle(props.getProperty(APP_TITLE));

        // RESET THE UI CONTROLS
        app.getFoolproofModule().updateControls(APP_FILE_FOOLPROOF_SETTINGS);
        app.getFoolproofModule().updateControls(APP_UNDO_FOOLPROOF_SETTINGS);
    }
    
    public void saveWork() throws IOException {
	// SAVE IT TO A FILE
	app.getFileComponent().saveData(app.getDataComponent(), workFile.getPath());
	
	// MARK IT AS SAVED
	saved = true;
        
        // PUT IT AT THE TOP OF THE RECENT WORK LIST
        app.getRecentWorkModule().startWork(workFile);

        // SET THE WINDOW TITLE        
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String title = props.getProperty(APP_TITLE) + " - " + workFile.getName();
        app.getGUIModule().getWindow().setTitle(title);

	// AND REFRESH THE GUI, WHICH WILL ENABLE AND DISABLE
	// THE APPROPRIATE CONTROLS
        app.getFoolproofModule().updateControls(APP_FILE_FOOLPROOF_SETTINGS);
    }    
    
    public void saveWork(File selectedFile) throws IOException {
        // MAKE THE PROVIDED FILE THE WORK FILE
        workFile = selectedFile;
        
        // AND NOW SAVE
        saveWork();
    }
    
    public void loadWork(File selectedFile) throws IOException {
        // RESET THE DATA
        app.getDataComponent().reset();
        
        // CLEAR OUT ANY OLD TRANSACTIONS
        app.getTPS().clearAllTransactions();

        // MAKE SURE THE WORKSPACE IS ACTIVATED
        app.getWorkspaceComponent().activate();

        // REMEMBEr THE FILE THAT'S BEEN LOADED
        saved = true; 
        workFile = selectedFile;

        // LOAD THE FILE INTO THE DATA, BUT MAKE SURE THE
        // WORKSPACE IS SIZED FIRST
        Platform.runLater(new Runnable(){
            public void run() {
                try {
                    if (app.getWorkspaceComponent().getWorkspace().getWidth() > 0.0) {
                        app.getFileComponent().loadData(app.getDataComponent(), selectedFile.getAbsolutePath());
                    }
                    else {
                        Thread.sleep(100);
                    }
                }
                catch(Exception exc) {
                    exc.printStackTrace();
                }
            }
        });
        
        // SET THE WINDOW TITLE        
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String title = props.getProperty(APP_TITLE) + " - " + workFile.getName();
        app.getGUIModule().getWindow().setTitle(title);
        
        // RESET THE UI CONTROLS
        app.getFoolproofModule().updateAll();
    }
}