/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import csg.CSGApp;
import static csg.CSGPropertyType.MT_LAB_TABLE_VIEW;
import static csg.CSGPropertyType.MT_LECTURE_TABLE_VIEW;
import csg.data.CSGData;
import csg.data.LabPrototype;
import csg.data.LecturePrototype;
import csg.data.MeetingTimesData;
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
public class MT_RemoveLab_Transaction implements jTPS_Transaction{
    CSGApp app;
    CSGData d;
    MeetingTimesData data;
    TextField c;
    String old;
    String n;
    LabPrototype removedLab;
    
    public MT_RemoveLab_Transaction(CSGApp initApp, CSGData d, MeetingTimesData data, LabPrototype removed){
        app = initApp;
        this.d = d;
        this.data = data;
        this.removedLab = removed;
    }
    
    @Override
    public void undoTransaction() {
        AppGUIModule gui = app.getGUIModule();
        TableView labTable = (TableView)gui.getGUINode(MT_LAB_TABLE_VIEW);
        data.addLab(removedLab);
        labTable.refresh();
    }

    @Override
    public void doTransaction() {
        AppGUIModule gui = app.getGUIModule();
        TableView labTable = (TableView)gui.getGUINode(MT_LAB_TABLE_VIEW);
        data.removeLab(removedLab);
        labTable.refresh();

    }
}
