package djf.components;

import djf.AppTemplate;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

/**
 * This abstract class provides the structure for workspace components in
 * our applications. Note that by doing so we make it possible
 * for customly provided descendent classes to have their methods
 * called from this framework.
 * 
 * @author Richard McKenna
 * @version 1.0
 */
public abstract class AppWorkspaceComponent {
    // HERE'S THE APP
    protected AppTemplate app;
    
    // THIS IS THE WORKSPACE WHICH WILL BE DIFFERENT
    // DEPENDING ON THE CUSTOM APP USING THIS FRAMEWORK

    // THIS IS THE MAIN WORKSPACE PANE, ALL OTHER CONTROLS 
    // WOULD GO INSIDE. NOTE THAT WHEN IT IS CONSTRUCTED,
    // IT MAY ACTUALLY BE ANY Pane DESCENDENT CLASS
    protected Pane workspace;
    protected boolean activated;
 
    public AppWorkspaceComponent(AppTemplate initApp) {
        app = initApp;
    }
    
    public boolean isActivated() {
        return activated;
    }

    /**
     * When called this function puts the workspace into the window,
     * revealing the controls for editing work.
     * 
     * @param appPane The pane that contains all the controls in the
     * entire application, including the file toolbar controls, which
     * this framework manages, as well as the customly provided workspace,
     * which would be different for each app.
     */
    public void activate() {
        if (!activated) {
            // PUT THE WORKSPACE IN THE GUI
            app.getGUIModule().getAppPane().setCenter(workspace);            
            activated = true;
        }
    }
    
    public void deactivate() {
        if (activated) {
            // REMOVE THE WORKSPACE FROM THE GUI
            app.getGUIModule().getAppPane().setCenter(null);
            activated = false;
        }
    }
    
    /**
     * Mutator method for setting the custom workspace.
     * 
     * @param initWorkspace The workspace to set as the user
     * interface's workspace.
     */
    public void setWorkspace(Pane initWorkspace) { 
	workspace = initWorkspace; 
    }
    
    /**
     * Accessor method for getting the workspace.
     * 
     * @return The workspace pane for this app.
     */
    public Pane getWorkspace() { return workspace; }
    
    // THE DEFINITION OF THIS CLASS SHOULD BE PROVIDED
    // BY THE CONCRETE WORKSPACE
   
    public abstract void showNewDialog();
    public abstract void processWorkspaceKeyEvent(KeyEvent ke);
}
