package csg.transactions;

import csg.CSGApp;
import static csg.CSGPropertyType.OH_TAS_TABLE_VIEW;
import csg.data.OHData;
import java.time.LocalDate;
import jtps.jTPS_Transaction;
import csg.data.TeachingAssistantPrototype;
import javafx.scene.control.TableView;

/**
 *
 * @author McKillaGorilla
 */
public class OH_EditTA_Transaction implements jTPS_Transaction {
    CSGApp app;
    TeachingAssistantPrototype oldTA;
    TeachingAssistantPrototype taToEdit;
    String oldName, newName;
    String oldEmail, newEmail;
    String oldType, newType;
    
    public OH_EditTA_Transaction(CSGApp initApp, TeachingAssistantPrototype initTAToEdit, 
            String name, String email, String type) {
        app = initApp;
        taToEdit = initTAToEdit;
        oldName = initTAToEdit.getName();
        oldEmail = initTAToEdit.getEmail();
        oldType = initTAToEdit.getType();
        oldTA = initTAToEdit;
        newName = name;
        newEmail = email;
        newType = type;
            
    }


    @Override
    public void doTransaction() {
        OHData data = (OHData)app.getDataComponent();
        taToEdit.setName(newName);
        taToEdit.setEmail(newEmail);
        taToEdit.setType(newType);
//        TableView<TeachingAssistantPrototype> taTableView = (TableView)gui.getGUINode(OH_TAS_TABLE_VIEW);
        if(!newType.equals(oldType)){
            data.updateTAsFromDialog(oldTA, newName, newEmail, newType);    
        }
        ((TableView)app.getGUIModule().getGUINode(OH_TAS_TABLE_VIEW)).refresh();
    }

    @Override
    public void undoTransaction() {
        OHData data = (OHData)app.getDataComponent();
        taToEdit.setName(oldName);
        taToEdit.setEmail(oldEmail);
        taToEdit.setType(oldType);
        ((TableView)app.getGUIModule().getGUINode(OH_TAS_TABLE_VIEW)).refresh();
    }
}