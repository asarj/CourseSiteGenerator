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
public class EditTopicsTA_Transaction implements jTPS_Transaction{
    CSGApp app;
    CSGWorkspace d;
    SyllabusData data;
    TextArea c;
    String old;
    String n;
    
    public EditTopicsTA_Transaction(CSGApp initApp, CSGWorkspace d, SyllabusData data, TextArea c, String old, String n){
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
