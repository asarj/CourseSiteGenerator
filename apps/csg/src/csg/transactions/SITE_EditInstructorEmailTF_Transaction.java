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
import javafx.scene.control.TextField;
import jtps.jTPS_Transaction;

/**
 *
 * @author Ajay
 */
public class SITE_EditInstructorEmailTF_Transaction implements jTPS_Transaction{
    CSGApp app;
    CSGData d;
    SiteData data;
    TextField c;
    String old;
    String n;
    
    public SITE_EditInstructorEmailTF_Transaction(CSGApp initApp, CSGData d, SiteData data, TextField c, String old, String n){
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
        c.setText(n);
    }

    @Override
    public void undoTransaction() {
        AppGUIModule gui = app.getGUIModule();
        c.setText(old);

    }
}
