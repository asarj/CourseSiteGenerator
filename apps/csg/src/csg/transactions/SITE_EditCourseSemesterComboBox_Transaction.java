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
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import jtps.jTPS_Transaction;

/**
 *
 * @author Ajay
 */
public class SITE_EditCourseSemesterComboBox_Transaction implements jTPS_Transaction {
    CSGApp app;
    CSGData d;
    SiteData data;
    ComboBox c;
    String old;
    String n;
    
    public SITE_EditCourseSemesterComboBox_Transaction(CSGApp initApp, CSGData d, SiteData data, ComboBox c){
        app = initApp;
        this.d = d;
        this.data = data;
        this.c = c;
        this.old = data.getSelectedSem();
        this.n = (String)c.getSelectionModel().getSelectedItem();
    }
    
    @Override
    public void doTransaction() {
        AppGUIModule gui = app.getGUIModule();
        if(n != null){
            data.setSelectedSem(n); 
            c.setValue(n);
            c.getSelectionModel().select(n);
//            this.old = data.getSelectedName()
        } 
        else{
            data.setSelectedSem((String)c.getSelectionModel().getSelectedItem());
            c.getSelectionModel().select((String)c.getSelectionModel().getSelectedItem());
        }
        ObservableList semesters = c.getItems();
        boolean dupFound = false;
                for(Object s: semesters){
                    String c = (String)s;
                    if(c != null && c.equals(n)){
                        dupFound = true;
                        break;
                    }
                }
                if(!dupFound){
                    semesters.add(n);  
//                    subjectCBox.getSelectionModel().select(t1);
                }
                semesters.forEach((s) -> {
                    String c = (String)s;
                    if (c != null && c.trim().equals("")) {
                        semesters.remove(s);
                    }
                });
//                for(int i = 0; i < subjects.size(); i++){
//                    String c = (String)subjects.get(i);
//                    if(c != null && c.trim().equals("")){
//                        subjects.remove(i);
//                    }
//                }
                c.setItems(semesters);
                c.getSelectionModel().select(n);
    }

    @Override
    public void undoTransaction() {
        AppGUIModule gui = app.getGUIModule();
        if(old != null && !old.equals("")){
            data.setSelectedSem(this.old);
            c.setValue(old);
            c.getSelectionModel().select(old);
        }
        else{           
            c.getSelectionModel().selectFirst();
            this.old = (String)c.getSelectionModel().getSelectedItem();
            c.setValue(old);
            data.setSelectedSem(this.old);
        }
//        c.getSelectionModel().clearSelection();

    }
}
