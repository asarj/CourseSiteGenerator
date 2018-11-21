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
public class EditLeftImg_Transaction implements jTPS_Transaction {
    CSGApp app;
    CSGData d;
    SiteData data;
    String path;
    
    public EditLeftImg_Transaction(CSGApp initApp, CSGData d, SiteData data, String path){
        app = initApp;
        this.d = d;
        this.data = data;
        this.path = path;
    }
    
    @Override
    public void doTransaction() {
        AppGUIModule gui = app.getGUIModule();
        data.setLeftUrl(path);
    }

    @Override
    public void undoTransaction() {
        AppGUIModule gui = app.getGUIModule();
        

    }
}
