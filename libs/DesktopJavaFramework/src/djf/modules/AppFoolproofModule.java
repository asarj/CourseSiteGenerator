package djf.modules;

import djf.AppTemplate;
import java.util.HashMap;
import djf.ui.foolproof.FoolproofDesign;

/**
 *
 * @author McKillaGorilla
 */
public class AppFoolproofModule { 
    // HERE'S THE APP
    AppTemplate app;
    
    // THIS WILL STORE ALL SETTINGS NEEDED FOR ENABLING
    // AND DISABLING CONTROLS
    protected HashMap<String, FoolproofDesign> foolproofSettings;
    
    public AppFoolproofModule(AppTemplate initApp) {
        app = initApp;       
        foolproofSettings = new HashMap();
    }
    
    public void registerModeSettings(Object foolproofMode, FoolproofDesign settings) {
        String key = foolproofMode.toString();
        foolproofSettings.put(key, settings);
    }
    
    public void updateControls(Object foolproofMode) {
        String key = foolproofMode.toString();
        FoolproofDesign settings = foolproofSettings.get(key);
        settings.updateControls();
    }
    
    public void updateAll() {
        for (FoolproofDesign foolproofDesign : foolproofSettings.values()) {
            foolproofDesign.updateControls();
        }
    }
}