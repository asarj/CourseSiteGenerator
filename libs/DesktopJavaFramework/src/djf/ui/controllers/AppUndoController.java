package djf.ui.controllers;

import djf.AppTemplate;
import jtps.jTPS;

public class AppUndoController {
    private AppTemplate app;
    
    public AppUndoController(AppTemplate initApp) {
        app = initApp;
    }
    
    public void processUndoRequest() {
        jTPS tps = app.getTPS();
        tps.undoTransaction();
        app.getFoolproofModule().updateAll();
    }
    
    public void processRedoRequest() {
        jTPS tps = app.getTPS();
        tps.doTransaction();
        app.getFoolproofModule().updateAll();
    }    
}
