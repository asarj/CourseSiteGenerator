/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import csg.CSGApp;
import static csg.CSGPropertyType.MT_LAB_TABLE_VIEW;
import csg.data.CSGData;
import csg.data.LabPrototype;
import csg.data.MeetingTimesData;
import csg.data.RecitationPrototype;
import djf.modules.AppGUIModule;
import javafx.scene.control.TableView;
import jtps.jTPS_Transaction;

/**
 *
 * @author Ajay
 */
public class MT_LabEditTA2Column_Transaction implements jTPS_Transaction{
    CSGApp app;
    CSGData d;
    MeetingTimesData data;
    String old;
    String n;
    LabPrototype newLab;
    
    public MT_LabEditTA2Column_Transaction(CSGApp initApp, CSGData d, MeetingTimesData data, String old, String n){
        app = initApp;
        this.d = d;
        this.data = data;
        this.old = old;
        this.n = n;
    }
    
    @Override
    public void doTransaction() {
        AppGUIModule gui = app.getGUIModule();
        TableView labTable = (TableView)gui.getGUINode(MT_LAB_TABLE_VIEW);
        LabPrototype l = (LabPrototype)labTable.getSelectionModel().getSelectedItem();
        newLab = data.editLab(l, "TA2", n);
        labTable.refresh();
    }

    @Override
    public void undoTransaction() {
        AppGUIModule gui = app.getGUIModule();
        TableView labTable = (TableView)gui.getGUINode(MT_LAB_TABLE_VIEW);
        LabPrototype r = (LabPrototype)labTable.getSelectionModel().getSelectedItem(); 
        data.editLab(newLab, "TA2", old);
        labTable.refresh();

    }
}
