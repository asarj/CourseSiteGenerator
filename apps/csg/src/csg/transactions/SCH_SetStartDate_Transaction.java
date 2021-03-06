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
    DatePicker dp;
    
    public SCH_SetStartDate_Transaction(CSGApp initApp, CSGData d, ScheduleData data, DatePicker dp){
        app = initApp;
        this.d = d;
        this.data = data;
        this.dp = dp;
        this.old = data.getStartDate();
        this.date = dp.getValue();
    }
    
    @Override
    public void doTransaction() {
        dp.setValue(date);
        data.setStartDate(date); 
    }

    @Override
    public void undoTransaction() {
        dp.setValue(old);
        data.setStartDate(old);
    }
}
