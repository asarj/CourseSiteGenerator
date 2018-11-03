package csg.transactions;

import jtps.jTPS_Transaction;
import csg.data.CSGData;
import csg.data.TeachingAssistantPrototype;

/**
 *
 * @author McKillaGorilla
 */
public class AddTA_Transaction implements jTPS_Transaction {
    CSGData data;
    TeachingAssistantPrototype ta;
    
    public AddTA_Transaction(CSGData initData, TeachingAssistantPrototype initTA) {
        data = initData;
        ta = initTA;
    }

    @Override
    public void doTransaction() {
        data.addTA(ta);        
    }

    @Override
    public void undoTransaction() {
        data.removeTA(ta);
    }
}
