package csg.clipboard;

import djf.components.AppClipboardComponent;
import java.util.ArrayList;
import java.util.HashMap;
import csg.CSGApp;
import csg.data.CSGData;
import csg.data.TeachingAssistantPrototype;
import csg.data.TimeSlot;
import csg.data.TimeSlot.DayOfWeek;
import csg.transactions.CutTA_Transaction;
import csg.transactions.PasteTA_Transaction;


public class CSGClipboard implements AppClipboardComponent {
    CSGApp app;
    TeachingAssistantPrototype clipboardCutTA;
    TeachingAssistantPrototype clipboardCopiedTA;

    public CSGClipboard(CSGApp initApp) {
        app = initApp;
        clipboardCutTA = null;
        clipboardCopiedTA = null;
    }

    @Override
    public void cut() {
        CSGData data = (CSGData)app.getDataComponent();
        if (data.isTASelected()) {
            clipboardCutTA = data.getSelectedTA();
            clipboardCopiedTA = null;
            HashMap<TimeSlot, ArrayList<DayOfWeek>> officeHours = data.getTATimeSlots(clipboardCutTA);
            CutTA_Transaction transaction = new CutTA_Transaction((CSGApp)app, clipboardCutTA, officeHours);
            app.processTransaction(transaction);
        }
    }

    @Override
    public void copy() {
        CSGData data = (CSGData)app.getDataComponent();
        if (data.isTASelected()) {
            TeachingAssistantPrototype tempTA = data.getSelectedTA();
            copyToCopiedClipboard(tempTA);
        }
    }
    
    private void copyToCopiedClipboard(TeachingAssistantPrototype taToCopy) {
        clipboardCutTA = null;
        clipboardCopiedTA = copyTA(taToCopy);
        app.getFoolproofModule().updateAll();        
    }
    
    private TeachingAssistantPrototype copyTA(TeachingAssistantPrototype taToCopy) {
        TeachingAssistantPrototype tempCopy = (TeachingAssistantPrototype)taToCopy.clone(); 
        tempCopy.setName(tempCopy.getName() + "_1");
        tempCopy.setEmail(tempCopy.getEmail() + "_1");
        return tempCopy;
    }

    @Override
    public void paste() {
        CSGData data = (CSGData)app.getDataComponent();
        if (clipboardCutTA != null) {
            PasteTA_Transaction transaction = new PasteTA_Transaction((CSGApp)app, clipboardCutTA);
            app.processTransaction(transaction);
            
            this.clipboardCutTA = null;
            app.getFoolproofModule().updateAll();
        }
        else if (clipboardCopiedTA != null) {
            // FIGURE OUT THE PROPER NAME AND
            // EMAIL ADDRESS SO THAT THERE ISN'T A DUPLICATE             
            PasteTA_Transaction transaction = new PasteTA_Transaction((CSGApp)app, clipboardCopiedTA);
            app.processTransaction(transaction);
            
            this.clipboardCopiedTA = null;
            app.getFoolproofModule().updateAll();
        }
    }    

    @Override
    public boolean hasSomethingToCut() {
        return ((CSGData)app.getDataComponent()).isTASelected();
    }

    @Override
    public boolean hasSomethingToCopy() {
        return ((CSGData)app.getDataComponent()).isTASelected();
    }

    @Override
    public boolean hasSomethingToPaste() {
        if ((clipboardCutTA != null) || (clipboardCopiedTA != null))
            return true;
        else
            return false;
    }
}