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
    ComboBox start;
    ComboBox end;
    
    public OH_UpdateOHTable_Transaction(CSGApp initApp, OHData initData, ComboBox start, ComboBox end) {
        app = initApp;
        data = initData;
        this.start = start;
        this.end = end;
        this.ts = start.getSelectionModel().getSelectedIndex();
        this.te = end.getSelectionModel().getSelectedIndex();
        this.oldts = data.getStartHour();
        this.oldte = data.getEndHour();
//        ts = start;
//        te = end;
//        oldts = oldStart;
//        oldte = oldEnd;
        
    }

    @Override
    public void doTransaction() {
        AppGUIModule gui = app.getGUIModule();
        TableView<TimeSlot> officeHoursTableView = (TableView) gui.getGUINode(OH_OFFICE_HOURS_TABLE_VIEW);
        start.setValue(start.getItems().get(ts));
        end.setValue(end.getItems().get(te));
        data.setStartHour(ts);
        data.setEndHour(te);
        data.resetOHTable(ts, te);
        officeHoursTableView.refresh();
    }

    @Override
    public void undoTransaction() {
        AppGUIModule gui = app.getGUIModule();
        TableView<TimeSlot> officeHoursTableView = (TableView) gui.getGUINode(OH_OFFICE_HOURS_TABLE_VIEW);
        start.setValue(start.getItems().get(oldts));
        end.setValue(end.getItems().get(oldte));
        data.setStartHour(oldts);
        data.setEndHour(oldte);
        data.resetOHTable(oldts, oldte);
        officeHoursTableView.refresh();
    }
}
