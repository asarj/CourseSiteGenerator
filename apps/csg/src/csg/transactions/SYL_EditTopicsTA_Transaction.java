/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import csg.CSGApp;
import csg.data.CSGData;
import csg.data.SiteData;
import csg.data.SyllabusData;
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
public class SYL_EditTopicsTA_Transaction implements jTPS_Transaction{
    CSGApp app;
    CSGWorkspace d;
    SyllabusData data;
    TextArea c;
    String old;
    String n;
    
    public SYL_EditTopicsTA_Transaction(CSGApp initApp, CSGWorkspace d, SyllabusData data, TextArea c){
        app = initApp;
        this.d = d;
        this.data = data;
        this.c = c;
        this.old = data.getTopicsJSON();
        this.n = c.getText();
    }
    
    @Override
    public void doTransaction() {
        AppGUIModule gui = app.getGUIModule();
        data.setTopicsJSON(n);
        c.setText(n);
    }

    @Override
    public void undoTransaction() {
        AppGUIModule gui = app.getGUIModule();
        if(this.old.equals("")){
            c.setText("[\n\t\"\"\n]");
        }
        else{
            c.setText(old);
        }
        data.setTopicsJSON(old);
    }
}
