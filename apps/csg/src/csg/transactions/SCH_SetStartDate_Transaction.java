/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import csg.CSGApp;
import csg.data.CSGData;
import csg.data.ScheduleData;
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
public class SCH_SetStartDate_Transaction implements jTPS_Transaction{
    CSGApp app;
    CSGData d;
    ScheduleData data;
    LocalDate old;
    LocalDate date;
    
    public SCH_SetStartDate_Transaction(CSGApp initApp, CSGData d, ScheduleData data, LocalDate date){
        app = initApp;
        this.d = d;
        this.data = data;
        this.date = date;
        this.old = data.getStartDate() != null ? data.getStartDate() : null;
    }
    
    @Override
    public void doTransaction() {
        AppGUIModule gui = app.getGUIModule();
        CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
        DatePicker d = workspace.getStartDate();
        d.setValue(date);
        data.setStartDate(date); 
    }

    @Override
    public void undoTransaction() {
        AppGUIModule gui = app.getGUIModule();
        CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
        DatePicker d = workspace.getStartDate();
        d.setValue(old);
        data.setStartDate(old);
    }
}
