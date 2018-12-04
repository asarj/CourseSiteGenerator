/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import csg.CSGApp;
import static csg.CSGPropertyType.OH_ENDTIME_COMBO_BOX;
import static csg.CSGPropertyType.OH_OFFICE_HOURS_TABLE_VIEW;
import static csg.CSGPropertyType.OH_STARTTIME_COMBO_BOX;
import jtps.jTPS_Transaction;
import csg.data.OHData;
import csg.data.TeachingAssistantPrototype;
import csg.data.TimeSlot;
import djf.modules.AppGUIModule;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;

/**
 *
 * @author McKillaGorilla
 */
public class OH_UpdateOHTable_Transaction implements jTPS_Transaction {
    CSGApp app;
    OHData data;
    int ts;
    int te;
    int oldts;
    int oldte;
    
    public OH_UpdateOHTable_Transaction(CSGApp initApp, OHData initData, int start, int end, int oldStart, int oldEnd) {
        app = initApp;
        data = initData;
        ts = start;
        te = end;
        oldts = oldStart;
        oldte = oldEnd;
        
    }

    @Override
    public void doTransaction() {
        AppGUIModule gui = app.getGUIModule();
        TableView<TimeSlot> officeHoursTableView = (TableView) gui.getGUINode(OH_OFFICE_HOURS_TABLE_VIEW);
        ComboBox timeStart = (ComboBox)gui.getGUINode(OH_STARTTIME_COMBO_BOX);
        ComboBox timeEnd = (ComboBox)gui.getGUINode(OH_ENDTIME_COMBO_BOX);
        timeStart.setValue(timeStart.getItems().get(ts));
        timeEnd.setValue(timeEnd.getItems().get(te));
        data.resetOHTable(ts, te);
        officeHoursTableView.refresh();
    }

    @Override
    public void undoTransaction() {
        AppGUIModule gui = app.getGUIModule();
        TableView<TimeSlot> officeHoursTableView = (TableView) gui.getGUINode(OH_OFFICE_HOURS_TABLE_VIEW);
        ComboBox timeStart = (ComboBox)gui.getGUINode(OH_STARTTIME_COMBO_BOX);
        ComboBox timeEnd = (ComboBox)gui.getGUINode(OH_ENDTIME_COMBO_BOX);
        timeStart.setValue(timeStart.getItems().get(oldts));
        timeEnd.setValue(timeEnd.getItems().get(oldte));
        data.resetOHTable(oldts, oldte);
        officeHoursTableView.refresh();
    }
}
