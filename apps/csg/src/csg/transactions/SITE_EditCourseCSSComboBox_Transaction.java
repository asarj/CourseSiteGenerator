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
import java.io.File;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import jtps.jTPS_Transaction;

/**
 *
 * @author Ajay
 */
public class SITE_EditCourseCSSComboBox_Transaction implements jTPS_Transaction {
    CSGApp app;
    CSGData d;
    SiteData data;
    ComboBox c;
    String old;
    String n;
    
    public SITE_EditCourseCSSComboBox_Transaction(CSGApp initApp, CSGData d, SiteData data, ComboBox c){
        app = initApp;
        this.d = d;
        this.data = data;
        this.c = c;
        this.old = data.getCSS().substring(data.getCSS().lastIndexOf("/") + 1);
        this.n = (String)c.getSelectionModel().getSelectedItem();
    }
    
    @Override
    public void doTransaction() {
        AppGUIModule gui = app.getGUIModule();
        if(n != null){
            data.setCSS(n); 
            c.setValue(n);
            c.getSelectionModel().select(n);
//            this.old = data.getSelectedName()

            ObservableList styleSheets = FXCollections.observableArrayList();

            File[] files = new File("./work/css/").listFiles();
            ArrayList<String> fns = new ArrayList<>();
            int x = 0;
            for(File file: files){
                if(file.getPath().contains(".css")){
                    String fileTemp = file.toString();
                    fileTemp = fileTemp.substring(fileTemp.lastIndexOf("/") + 1);
                    fns.add(fileTemp);
                    x++;
                }
            }
            styleSheets.clear();
            styleSheets.addAll(fns);
            c.setItems(styleSheets);
        } 
        else{
            data.setCSS((String)c.getSelectionModel().getSelectedItem());
            c.getSelectionModel().select((String)c.getSelectionModel().getSelectedItem());
        }
    }

    @Override
    public void undoTransaction() {
        AppGUIModule gui = app.getGUIModule();
        if(old != null && !old.equals("")){
            data.setCSS(this.old);
            c.setValue(old);
            c.getSelectionModel().select(old);
        }
        else{           
            c.getSelectionModel().selectFirst();
            this.old = (String)c.getSelectionModel().getSelectedItem();
            c.setValue(old);
            data.setCSS(this.old);
        }

    }
}
