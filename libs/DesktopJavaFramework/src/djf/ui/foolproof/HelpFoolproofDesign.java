package djf.ui.foolproof;

import static djf.AppPropertyType.LANGUAGE_BUTTON;
import djf.AppTemplate;

/**
 *
 * @author McKillaGorilla
 */
public class HelpFoolproofDesign  implements FoolproofDesign {
    AppTemplate app;
    
    public HelpFoolproofDesign(AppTemplate initApp) {
        app = initApp;
    }    

    @Override
    public void updateControls() {
        boolean appHasMoreThanOneLanguage = app.getLanguageModule().getLanguages().size() > 1;
        app.getGUIModule().getGUINode(LANGUAGE_BUTTON);
    }
}
