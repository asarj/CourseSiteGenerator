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
import jtps.jTPS_Transaction;

/**
 *
 * @author Ajay
 */
public class SITE_EditSyllabusCheckBox_Transaction implements jTPS_Transaction{
    CSGApp app;
    CSGData d;
    SiteData data;
    CheckBox home;
    CheckBox syllabus;
    CheckBox schedule;
    CheckBox hw;
//    boolean isHomeSelected;
    boolean isSylSelected;
//    boolean isSchSelected;
//    boolean isHwSelected;
    
    public SITE_EditSyllabusCheckBox_Transaction(CSGApp initApp, CSGData d, SiteData data, CheckBox a, CheckBox s, CheckBox sc, CheckBox h){
        app = initApp;
        this.d = d;
        this.data = data;
        this.home = a;
        this.syllabus = s;
        this.schedule = sc;
        this.hw = h;
        isSylSelected = syllabus.isSelected();
    }
    
    @Override
    public void doTransaction() {
        this.syllabus.setSelected(isSylSelected);
        data.updatePagesOptions(home, syllabus, schedule, hw);
    }

    @Override
    public void undoTransaction() {
         this.syllabus.setSelected(!isSylSelected);
         data.updatePagesOptions(home, syllabus, schedule, hw);
    }
}
