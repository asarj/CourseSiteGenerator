/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.workspace.controllers;

import csg.CSGApp;
import csg.data.CSGData;
import csg.data.SiteData;
import djf.modules.AppGUIModule;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import jtps.jTPS_Transaction;

/**
 *
 * @author Ajay
 */
public class EditPagesCheckboxes_Transaction implements jTPS_Transaction{
    CSGApp app;
    CSGData d;
    SiteData data;
    TextField c;
    CheckBox home;
    CheckBox syllabus;
    CheckBox schedule;
    CheckBox hw;
    boolean wasHomeSelected;
    boolean wasSylSelected;
    boolean wasSchSelected;
    boolean wasHwSelected;
    
    public EditPagesCheckboxes_Transaction(CSGApp initApp, CSGData d, SiteData data, CheckBox a, CheckBox b, CheckBox c, CheckBox da){
        app = initApp;
        this.d = d;
        this.data = data;
        this.home = a;
        this.syllabus = b;
        this.schedule = c;
        this.hw = da;
        wasHomeSelected = a.isSelected();
        wasSylSelected = b.isSelected();
        wasSchSelected = c.isSelected();
        wasHwSelected = da.isSelected();
    }
    
    @Override
    public void doTransaction() {
        AppGUIModule gui = app.getGUIModule();
        data.updatePagesOptions(home, syllabus, schedule, hw);
    }

    @Override
    public void undoTransaction() {
        AppGUIModule gui = app.getGUIModule();
        home.setSelected(wasHomeSelected);
        syllabus.setSelected(wasSylSelected);
        schedule.setSelected(wasSchSelected);
        hw.setSelected(wasHwSelected);

    }
}
