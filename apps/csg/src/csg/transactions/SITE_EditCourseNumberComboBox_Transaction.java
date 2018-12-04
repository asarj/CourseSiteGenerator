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
import javafx.scene.control.ComboBox;
import jtps.jTPS_Transaction;

/**
 *
 * @author Ajay
 */
public class SITE_EditCourseNumberComboBox_Transaction implements jTPS_Transaction {
    CSGApp app;
    CSGData d;
    SiteData data;
    ComboBox c;
    String old;
    String n;
    
    public SITE_EditCourseNumberComboBox_Transaction(CSGApp initApp, CSGData d, SiteData data, ComboBox c, String old, String n){
        app = initApp;
        this.d = d;
        this.data = data;
        this.c = c;
        this.old = old;
        this.n = n;
    }
    
    @Override
    public void doTransaction() {
        AppGUIModule gui = app.getGUIModule();
        c.getSelectionModel().select(n);
        data.setSelectedNum((String)c.getSelectionModel().getSelectedItem());
        boolean dupFound = false;
        for(Object s: c.getItems()){
            String c = (String)s;
            if(c != null && c.equals(n)){
                dupFound = true;
                break;
            }
        }
        if(!dupFound){
            c.getItems().add(n);    
        }
        c.getItems().forEach((s) -> {
            String h = (String)s;
            if (h.trim().equals("")) {
                c.getItems().remove(s);
            }
        });
    }

    @Override
    public void undoTransaction() {
        AppGUIModule gui = app.getGUIModule();
        c.getSelectionModel().select(old);
        data.setSelectedNum(this.old);

    }
}
