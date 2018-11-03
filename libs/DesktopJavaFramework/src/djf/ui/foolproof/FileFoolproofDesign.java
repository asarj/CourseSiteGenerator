package djf.ui.foolproof;

import static djf.AppPropertyType.CLOSE_BUTTON;
import static djf.AppPropertyType.EXIT_BUTTON;
import static djf.AppPropertyType.EXPORT_BUTTON;
import static djf.AppPropertyType.HAS_CLOSE;
import static djf.AppPropertyType.HAS_EXIT;
import static djf.AppPropertyType.HAS_EXPORT;
import static djf.AppPropertyType.HAS_LOAD;
import static djf.AppPropertyType.HAS_NEW;
import static djf.AppPropertyType.HAS_SAVE;
import static djf.AppPropertyType.LOAD_BUTTON;
import static djf.AppPropertyType.NEW_BUTTON;
import static djf.AppPropertyType.SAVE_BUTTON;
import djf.AppTemplate;
import djf.modules.AppGUIModule;
import javafx.scene.control.Button;
import properties_manager.PropertiesManager;

/**
 *
 * @author McKillaGorilla
 */
public class FileFoolproofDesign implements FoolproofDesign {
    AppTemplate app;
    
    public FileFoolproofDesign(AppTemplate initApp) {
        app = initApp;
    }    

    @Override
    public void updateControls() {
        AppGUIModule gui = app.getGUIModule();
        enableIfInUse(HAS_NEW,      NEW_BUTTON,     true);
        enableIfInUse(HAS_LOAD,     LOAD_BUTTON,    true);
        enableIfInUse(HAS_CLOSE,    CLOSE_BUTTON,   app.getWorkspaceComponent().isActivated());
        enableIfInUse(HAS_SAVE,     SAVE_BUTTON,    !app.getFileModule().isSaved());
        enableIfInUse(HAS_EXPORT,   EXPORT_BUTTON,  app.getWorkspaceComponent().isActivated());
        enableIfInUse(HAS_EXIT,     EXIT_BUTTON,    true);
    }
    
    private void enableIfInUse(Object controlInUseProperty, Object controlProperty, boolean enabled) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        if (props.isTrue(controlInUseProperty)) {
            Button button = (Button)app.getGUIModule().getGUINode(controlProperty);
            button.setDisable(!enabled);
        }
    }
}