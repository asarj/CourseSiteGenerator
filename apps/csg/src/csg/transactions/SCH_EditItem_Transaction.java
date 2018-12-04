/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import csg.CSGApp;
import static csg.CSGPropertyType.SCH_ITEMS_TABLE_VIEW;
import csg.data.CSGData;
import csg.data.ScheduleData;
import csg.data.ScheduleItemPrototype;
import csg.workspace.CSGWorkspace;
import djf.modules.AppGUIModule;
import java.time.LocalDate;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import jtps.jTPS_Transaction;

/**
 *
 * @author Ajay
 */
public class SCH_EditItem_Transaction implements jTPS_Transaction{
    CSGApp app;
    CSGData d;
    ScheduleData data;
    ScheduleItemPrototype s;
    ScheduleItemPrototype old;
    
    public SCH_EditItem_Transaction(CSGApp initApp, CSGData d, ScheduleData data, ScheduleItemPrototype old, ScheduleItemPrototype s){
        app = initApp;
        this.d = d;
        this.data = data;
        this.old = old;
        this.s = s;
    }
    
    @Override
    public void doTransaction() {
        AppGUIModule gui = app.getGUIModule();
        TableView<ScheduleItemPrototype> schTable = (TableView)gui.getGUINode(SCH_ITEMS_TABLE_VIEW);
        ScheduleItemPrototype temp = old.clone();
        this.old = data.editScheduleItem(old, s);
        this.s = temp;
        schTable.refresh();
    }

    @Override
    public void undoTransaction() {
        AppGUIModule gui = app.getGUIModule();
        TableView<ScheduleItemPrototype> schTable = (TableView)gui.getGUINode(SCH_ITEMS_TABLE_VIEW);
        ScheduleItemPrototype temp = old.clone();
        this.s = data.editScheduleItem(old, s);
        this.old = temp;
        schTable.refresh();
    }
}
