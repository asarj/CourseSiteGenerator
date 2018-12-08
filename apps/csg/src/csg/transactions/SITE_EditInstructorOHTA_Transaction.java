/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import csg.CSGApp;
import csg.data.CSGData;
import csg.data.SiteData;
import csg.workspace.CSGWorkspace;
import djf.modules.AppGUIModule;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import jtps.jTPS_Transaction;

/**
 *
 * @author Ajay
 */
public class SITE_EditInstructorOHTA_Transaction implements jTPS_Transaction{
    CSGApp app;
    CSGWorkspace d;
    SiteData data;
    TextArea c;
    String old;
    String n;
    
    public SITE_EditInstructorOHTA_Transaction(CSGApp initApp, CSGWorkspace d, SiteData data, TextArea c){
        app = initApp;
        this.d = d;
        this.data = data;
        this.c = c;
        this.old = data.getInstructorHoursJSON();
        this.n = c.getText();
    }
    
    @Override
    public void doTransaction() {
        AppGUIModule gui = app.getGUIModule();
        data.setInstructorHoursJSON(n);
        c.setText(n);
    }

    @Override
    public void undoTransaction() {
        AppGUIModule gui = app.getGUIModule();
        if(this.old.equals("")){
            c.setText("["
                                    + "\n\t{\"Day\": \"\", \t\"Time\": \"\"}"
                                 + "\n]");
        }
        else{
            c.setText(old);
        }
        data.setInstructorHoursJSON(old);

    }
}
