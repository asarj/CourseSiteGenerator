package djf.ui.foolproof;

import static djf.AppPropertyType.REDO_BUTTON;
import static djf.AppPropertyType.UNDO_BUTTON;
import djf.AppTemplate;
import djf.modules.AppGUIModule;
import javafx.scene.control.Button;
import jtps.jTPS;

/**
 *
 * @author McKillaGorilla
 */
public class UndoFoolproofDesign implements FoolproofDesign {
    AppTemplate app;
    
    public UndoFoolproofDesign(AppTemplate initApp) {
        app = initApp;
    }    

    @Override
    public void updateControls() {
        jTPS tps = app.getTPS();
        AppGUIModule gui = app.getGUIModule();
        Button undoButton = (Button)gui.getGUINode(UNDO_BUTTON);
        undoButton.setDisable(!tps.hasTransactionToUndo());
        Button redoButton = (Button)gui.getGUINode(REDO_BUTTON);
        redoButton.setDisable(!tps.hasTransactionToRedo());
    }
}