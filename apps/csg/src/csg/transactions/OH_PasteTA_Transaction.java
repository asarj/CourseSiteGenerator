package csg.transactions;

import jtps.jTPS_Transaction;
import csg.CSGApp;
import csg.data.OHData;
import csg.data.TeachingAssistantPrototype;

public class OH_PasteTA_Transaction implements jTPS_Transaction {
    CSGApp app;
    TeachingAssistantPrototype taToPaste;

    public OH_PasteTA_Transaction(  CSGApp initApp, 
                                 TeachingAssistantPrototype initTAToPaste) {
        app = initApp;
        taToPaste = initTAToPaste;
    }

    @Override
    public void doTransaction() {
        OHData data = (OHData)app.getDataComponent();
        data.addTA(taToPaste);
    }

    @Override
    public void undoTransaction() {
        OHData data = (OHData)app.getDataComponent();
        data.removeTA(taToPaste);
    }   
}