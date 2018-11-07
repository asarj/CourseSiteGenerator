package csg.transactions;

import csg.CSGApp;
import csg.data.CSGData;
import java.time.LocalDate;
import jtps.jTPS_Transaction;
import csg.data.TeachingAssistantPrototype;

/**
 *
 * @author McKillaGorilla
 */
public class EditTA_Transaction implements jTPS_Transaction {
    CSGApp app;
    TeachingAssistantPrototype taToEdit;
    String oldName, newName;
    String oldEmail, newEmail;
    String oldType, newType;
    
    public EditTA_Transaction(CSGApp initApp, TeachingAssistantPrototype initTAToEdit, 
            String name, String email, String type) {
        app = initApp;
        taToEdit = initTAToEdit;
        oldName = initTAToEdit.getName();
        oldEmail = initTAToEdit.getEmail();
        oldType = initTAToEdit.getType();
        newName = name;
        newEmail = email;
        newType = type;
    }


    @Override
    public void doTransaction() {
        CSGData data = (CSGData)app.getDataComponent();
        taToEdit.setName(newName);
        taToEdit.setEmail(newEmail);
        taToEdit.setType(newType);
        data.updateTAs();
    }

    @Override
    public void undoTransaction() {
        CSGData data = (CSGData)app.getDataComponent();
        taToEdit.setName(oldName);
        taToEdit.setEmail(oldEmail);
        taToEdit.setType(oldType);
        data.updateTAs();
    }
}