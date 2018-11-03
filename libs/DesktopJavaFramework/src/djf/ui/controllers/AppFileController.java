package djf.ui.controllers;

import static djf.AppPropertyType.*;
import djf.AppTemplate;
import djf.modules.AppFileModule;
import djf.ui.dialogs.AppDialogsFacade;
import java.io.File;
import java.io.IOException;
import javafx.scene.control.ButtonType;
import properties_manager.PropertiesManager;

public class AppFileController {

    // HERE'S THE APP
    AppTemplate app;

    /**
     * This constructor just keeps the app for later.
     *
     * @param initApp The application within which this controller will provide
     * file toolbar responses.
     */
    public AppFileController(AppTemplate initApp) {
        // NOTHING YET
        app = initApp;
    }

    /**
     * This method starts the process of editing new Work. If work is already
     * being edited, it will prompt the user to save it first.
     *
     */
    public void processNewRequest() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        try {
            // WE MAY HAVE TO SAVE CURRENT WORK
            AppFileModule fileSettings = app.getFileModule();
            boolean continueToMakeNew = true;
            if (!fileSettings.isSaved()) {
                // THE USER CAN OPT OUT HERE WITH A CANCEL
                continueToMakeNew = promptToSave();
            }

            // IF THE USER REALLY WANTS TO MAKE A NEW COURSE
            if (continueToMakeNew) {

                if (props.isTrue(HAS_NEW_DIALOG)) {
                    app.getWorkspaceComponent().showNewDialog();
                } else {
                    app.getFileModule().newWork();
                }
            }
        } catch (IOException ioe) {
            // SOMETHING WENT WRONG, PROVIDE FEEDBACK
            AppDialogsFacade.showMessageDialog(app.getGUIModule().getWindow(), NEW_ERROR_TITLE, NEW_ERROR_CONTENT);
        }
    }

    /**
     * This method lets the user open a Course saved to a file. It will also
     * make sure data for the current Course is not lost.
     */
    public void processLoadRequest() {
        try {
            // WE MAY HAVE TO SAVE CURRENT WORK
            AppFileModule fileSettings = app.getFileModule();
            boolean continueToOpen = true;
            if (!fileSettings.isSaved()) {
                // THE USER CAN OPT OUT HERE WITH A CANCEL
                continueToOpen = promptToSave();
            }

            // IF THE USER REALLY WANTS TO OPEN A Course
            if (continueToOpen) {
                // GO AHEAD AND PROCEED LOADING A Course
                promptToOpen();
            }
        } catch (IOException ioe) {
            // SOMETHING WENT WRONG
            AppDialogsFacade.showMessageDialog(app.getGUIModule().getWindow(), LOAD_ERROR_TITLE, LOAD_ERROR_CONTENT);
        }
    }

    public void processCloseRequest() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        try {
            // WE MAY HAVE TO SAVE CURRENT WORK
            AppFileModule fileSettings = app.getFileModule();
            boolean continueToClose = true;
            if (!fileSettings.isSaved()) {
                // THE USER CAN OPT OUT HERE WITH A CANCEL
                continueToClose = promptToSave();
            }

            // IF THE USER REALLY WANTS TO CLOSE
            if (continueToClose) {
                fileSettings.closeWork();
            }
        } catch (IOException ioe) {
            // SOMETHING WENT WRONG, PROVIDE FEEDBACK
            AppDialogsFacade.showMessageDialog(app.getGUIModule().getWindow(), SAVE_ERROR_TITLE, SAVE_ERROR_CONTENT);
        }
    }

    /**
     * This method will save the current course to a file. Note that we already
     * know the name of the file, so we won't need to prompt the user.
     *
     * @param courseToSave The course being edited that is to be saved to a
     * file.
     */
    public void processSaveRequest() {
        // WE'LL NEED THIS TO GET CUSTOM STUFF
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        try {
            // MAYBE WE ALREADY KNOW THE FILE
            AppFileModule fileSettings = app.getFileModule();
            if (fileSettings.wasSaved()) {
                fileSettings.saveWork();
            } // OTHERWISE WE NEED TO PROMPT THE USER
            else {
                File saveFile = AppDialogsFacade.showSaveDialog(app.getGUIModule().getWindow(), SAVE_WORK_TITLE);
                if (saveFile != null) {
                    fileSettings.saveWork(saveFile);
                }
            }
        } catch (IOException ioe) {
            AppDialogsFacade.showMessageDialog(app.getGUIModule().getWindow(), SAVE_ERROR_TITLE, SAVE_ERROR_CONTENT);
        }
    }

    public void processExportRequest() {
        AppFileModule fileSettings = app.getFileModule();
        String workFileName = fileSettings.getWorkFile().getName();
        try {
            app.getFileComponent().exportData(app.getDataComponent(), workFileName);
            AppDialogsFacade.showExportDialog(app);
        } catch (IOException ioe) {
            AppDialogsFacade.showMessageDialog(app.getGUIModule().getWindow(), EXPORT_ERROR_TITLE, EXPORT_ERROR_CONTENT);
        }
    }

    /**
     * This method will exit the application, making sure the user doesn't lose
     * any data first.
     *
     */
    public void processExitRequest() {
        try {
            // WE MAY HAVE TO SAVE CURRENT WORK
            boolean continueToExit = true;
            AppFileModule fileSettings = app.getFileModule();
            if (!fileSettings.isSaved()) {
                // THE USER CAN OPT OUT HERE
                continueToExit = promptToSave();
            }

            // IF THE USER REALLY WANTS TO EXIT THE APP
            if (continueToExit) {
                // EXIT THE APPLICATION
                System.exit(0);
            }
        } catch (IOException ioe) {
            AppDialogsFacade.showMessageDialog(app.getGUIModule().getWindow(), SAVE_ERROR_TITLE, SAVE_ERROR_CONTENT);
        }
    }

    /**
     * This helper method verifies that the user really wants to save their
     * unsaved work, which they might not want to do. Note that it could be used
     * in multiple contexts before doing other actions, like creating new work,
     * or opening another file. Note that the user will be presented with 3
     * options: YES, NO, and CANCEL. YES means the user wants to save their work
     * and continue the other action (we return true to denote this), NO means
     * don't save the work but continue with the other action (true is
     * returned), CANCEL means don't save the work and don't continue with the
     * other action (false is returned).
     *
     * @return true if the user presses the YES option to save, true if the user
     * presses the NO option to not save, false if the user presses the CANCEL
     * option to not continue.
     */
    private boolean promptToSave() throws IOException {

        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // CHECK TO SEE IF THE CURRENT WORK HAS
        // BEEN SAVED AT LEAST ONCE
        // PROMPT THE USER TO SAVE UNSAVED WORK
        ButtonType selection = AppDialogsFacade.showYesNoCancelDialog(app.getGUIModule().getWindow(), SAVE_VERIFY_TITLE, SAVE_VERIFY_CONTENT);

        // IF THE USER SAID YES, THEN SAVE BEFORE MOVING ON
        if (selection == ButtonType.YES) {
            // WE MAY NEED TO ASK THE USER WHERE TO SAVE
            AppFileModule fileSettings = app.getFileModule();
            if (!fileSettings.wasSaved()) {
                File selectedFile = AppDialogsFacade.showSaveDialog(app.getGUIModule().getWindow(), SAVE_WORK_TITLE);
                if (selectedFile != null) {
                    fileSettings.saveWork(selectedFile);
                }
            } else {
                fileSettings.saveWork();
            }
        } // IF THE USER SAID CANCEL, THEN WE'LL TELL WHOEVER
        // CALLED THIS THAT THE USER IS NOT INTERESTED ANYMORE
        else if (selection == ButtonType.CANCEL) {
            return false;
        }

        // IF THE USER SAID NO, WE JUST GO ON WITHOUT SAVING
        // BUT FOR BOTH YES AND NO WE DO WHATEVER THE USER
        // HAD IN MIND IN THE FIRST PLACE
        return true;
    }

    /**
     * This helper method asks the user for a file to open. The user-selected
     * file is then loaded and the GUI updated. Note that if the user cancels
     * the open process, nothing is done. If an error occurs loading the file, a
     * message is displayed, but nothing changes.
     */
    private void promptToOpen() {
        // WE'LL NEED TO GET CUSTOMIZED STUFF WITH THIS
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // AND NOW ASK THE USER FOR THE FILE TO OPEN
        File selectedFile = AppDialogsFacade.showOpenDialog(app.getGUIModule().getWindow(), LOAD_WORK_TITLE);

        // ONLY OPEN A NEW FILE IF THE USER SAYS OK
        if (selectedFile != null) {
            try {
                app.getFileModule().loadWork(selectedFile);
                app.getTPS().clearAllTransactions();
                app.getFoolproofModule().updateAll();
            } catch (Exception e) {
                AppDialogsFacade.showMessageDialog(app.getGUIModule().getWindow(), LOAD_ERROR_TITLE, LOAD_ERROR_CONTENT);
            }
        }
    }
}
