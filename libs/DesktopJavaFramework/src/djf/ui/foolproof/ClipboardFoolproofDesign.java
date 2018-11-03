package djf.ui.foolproof;

import static djf.AppPropertyType.*;
import djf.AppTemplate;
import djf.components.AppClipboardComponent;
import djf.modules.AppGUIModule;
import javafx.scene.control.Button;

/**
 *
 * @author McKillaGorilla
 */
public class ClipboardFoolproofDesign implements FoolproofDesign {
    AppTemplate app;
    
    public ClipboardFoolproofDesign(AppTemplate initApp) {
        app = initApp;
    }    

    @Override
    public void updateControls() {
        AppClipboardComponent clipboard = app.getClipboardComponent();
        AppGUIModule gui = app.getGUIModule();
        Button cutButton = (Button)gui.getGUINode(CUT_BUTTON);
        cutButton.setDisable(!clipboard.hasSomethingToCut());
        Button copyButton = (Button)gui.getGUINode(COPY_BUTTON);
        copyButton.setDisable(!clipboard.hasSomethingToCopy());
        Button pasteButton = (Button)gui.getGUINode(PASTE_BUTTON);
        pasteButton.setDisable(!clipboard.hasSomethingToPaste());
    }
}