/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

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
public class SITE_EditPagesCheckboxes_Transaction implements jTPS_Transaction{
    CSGApp app;
    CSGData d;
    SiteData data;
    TextField c;
    CheckBox home;
    CheckBox syllabus;
    CheckBox schedule;
    CheckBox hw;
    boolean isHomeSelected;
    boolean isSylSelected;
    boolean isSchSelected;
    boolean isHwSelected;
    boolean wasHomeSelected;
    boolean wasSylSelected;
    boolean wasSchSelected;
    boolean wasHwSelected;
    
    public SITE_EditPagesCheckboxes_Transaction(CSGApp initApp, CSGData d, SiteData data, CheckBox a, CheckBox b, CheckBox c, CheckBox da){
        app = initApp;
        this.d = d;
        this.data = data;
        this.home = a;
        this.syllabus = b;
        this.schedule = c;
        this.hw = da;
        isHomeSelected = a.isSelected();
        isSylSelected = b.isSelected();
        isSchSelected = c.isSelected();
        isHwSelected = da.isSelected();
        for(int i = 0; i < data.getSelectedPageOptions().size(); i++){
            if(data.getSelectedPageOptions().get(i).equals("home")){
                wasHomeSelected = true;
            }
            else{
                wasHomeSelected = false;
            }
            if(data.getSelectedPageOptions().get(i).equals("syllabus")){
                wasSylSelected = true;
            }
            else{
                wasSylSelected = false;
            }
            if(data.getSelectedPageOptions().get(i).equals("schedule")){
                wasSchSelected = true;
            }
            else{
                wasSchSelected = false;
            }
            if(data.getSelectedPageOptions().get(i).equals("hw")){
                wasHwSelected = true;
            }
            else{
                wasHwSelected = false;
            }
        }
    }
    
    @Override
    public void doTransaction() {
        AppGUIModule gui = app.getGUIModule();
        data.updatePagesOptions(home, syllabus, schedule, hw);
        home.setSelected(isHomeSelected);
        syllabus.setSelected(isSylSelected);
        schedule.setSelected(isSchSelected);
        hw.setSelected(isHwSelected);
    }

    @Override
    public void undoTransaction() {
        AppGUIModule gui = app.getGUIModule();
        if(isHomeSelected == wasHomeSelected)
            home.setSelected(wasHomeSelected);
        if(isSylSelected == wasSylSelected)
            syllabus.setSelected(wasSylSelected);
        if(isSchSelected == wasSchSelected)
            schedule.setSelected(wasSchSelected);
        if(isHwSelected = wasHwSelected)
            hw.setSelected(wasHwSelected);
         

    }
}
