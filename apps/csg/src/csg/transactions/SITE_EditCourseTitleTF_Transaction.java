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
public class SITE_EditCourseTitleTF_Transaction implements jTPS_Transaction{
    CSGApp app;
    CSGData d;
    SiteData data;
    TextField c;
    String old;
    String n;
    
    public SITE_EditCourseTitleTF_Transaction(CSGApp initApp, CSGData d, SiteData data, TextField c){
        app = initApp;
        this.d = d;
        this.data = data;
        this.c = c;
        this.old = data.getTitle();
        this.n = c.getText();
    }
    
    @Override
    public void doTransaction() {
        AppGUIModule gui = app.getGUIModule();
        data.setTitle(n);
        c.setText(n);
    }

    @Override
    public void undoTransaction() {
        AppGUIModule gui = app.getGUIModule();
        data.setTitle(old);
        c.setText(old);

    }
}
