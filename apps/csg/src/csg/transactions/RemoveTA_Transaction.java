package csg.transactions;

import jtps.jTPS_Transaction;
import static djf.AppPropertyType.APP_CLIPBOARD_FOOLPROOF_SETTINGS;
import java.util.ArrayList;
import java.util.HashMap;
import csg.CSGApp;
import csg.data.CSGData;
import csg.data.TeachingAssistantPrototype;
import csg.data.TimeSlot;
import csg.data.TimeSlot.DayOfWeek;

public class RemoveTA_Transaction implements jTPS_Transaction {
    CSGData data;
    TeachingAssistantPrototype ta;
    
    public RemoveTA_Transaction(CSGData initData, TeachingAssistantPrototype initTA) {
        data = initData;
        ta = initTA;
    }

    @Override
    public void undoTransaction() {
        data.addTA(ta);        
    }

    @Override
    public void doTransaction() {
        data.removeTA(ta);
    } 
}