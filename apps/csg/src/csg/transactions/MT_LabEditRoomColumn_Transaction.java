/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import csg.CSGApp;
import static csg.CSGPropertyType.MT_LAB_TABLE_VIEW;
import static csg.CSGPropertyType.MT_RECITATION_TABLE_VIEW;
import csg.data.CSGData;
import csg.data.LabPrototype;
import csg.data.MeetingTimesData;
import csg.data.RecitationPrototype;
import csg.data.SiteData;
import djf.modules.AppGUIModule;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import jtps.jTPS_Transaction;

/**
 *
 * @author Ajay
 */
public class MT_LabEditRoomColumn_Transaction implements jTPS_Transaction{
    CSGApp app;
    CSGData d;
    MeetingTimesData data;
    String old;
    String n;
    LabPrototype newLab;
    
    public MT_LabEditRoomColumn_Transaction(CSGApp initApp, CSGData d, MeetingTimesData data, LabPrototype l, String old, String n){
        app = initApp;
        this.d = d;
        this.data = data;
        this.newLab = l;
        this.old = old;
        this.n = n;
    }
    
    @Override
    public void doTransaction() {
        AppGUIModule gui = app.getGUIModule();
        TableView labTable = (TableView)gui.getGUINode(MT_LAB_TABLE_VIEW);
        newLab = data.editLab(newLab, "ROOM", n);
        labTable.refresh();
    }

    @Override
    public void undoTransaction() {
        AppGUIModule gui = app.getGUIModule();
        TableView labTable = (TableView)gui.getGUINode(MT_LAB_TABLE_VIEW);
        data.editLab(newLab, "ROOM", old);
        labTable.refresh();

    }
}
