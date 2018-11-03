package djf.ui.controllers;

import djf.ui.dialogs.AppDialogsFacade;
import djf.AppTemplate;
import djf.modules.AppLanguageModule;
import djf.modules.AppLanguageModule.LanguageException;

public class AppHelpController {
    private AppTemplate app;
    
    public AppHelpController(AppTemplate initApp) {
        app = initApp;
    }  
    
    public void processHelpRequest() {
        AppDialogsFacade.showHelpDialog(app);
    }
    
    public void processLanguageRequest() {
        try {
            AppLanguageModule languageSettings = app.getLanguageModule();
            AppDialogsFacade.showLanguageDialog(languageSettings);
        }
        catch(LanguageException le) {
            System.out.println("Error Loading Language into UI");
        }
    }  
    
    public void processAboutRequest() {
        AppDialogsFacade.showAboutDialog(app);
    }    
}
