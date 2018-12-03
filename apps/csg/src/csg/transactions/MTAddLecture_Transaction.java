/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import csg.CSGApp;
import static csg.CSGPropertyType.MT_LECTURE_TABLE_VIEW;
import csg.data.CSGData;
import csg.data.LecturePrototype;
import csg.data.MeetingTimesData;
import csg.data.SiteData;
import djf.modules.AppGUIModule;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import jtps.jTPS_Transaction;

/**
 *
 * @author Ajay
 */
public class MTAddLecture_Transaction implements jTPS_Transaction{
    CSGApp app;
    CSGData d;
    MeetingTimesData data;
    TextField c;
    String old;
    String n;
    LecturePrototype addedLecture;
    
    public MTAddLecture_Transaction(CSGApp initApp, CSGData d, MeetingTimesData data){
        app = initApp;
        this.d = d;
        this.data = data;
    }
    
    @Override
    public void doTransaction() {
        AppGUIModule gui = app.getGUIModule();
        TableView lecTable = (TableView)gui.getGUINode(MT_LECTURE_TABLE_VIEW);
        addedLecture = data.addLecture();
        lecTable.refresh();
    }

    @Override
    public void undoTransaction() {
        AppGUIModule gui = app.getGUIModule();
        TableView lecTable = (TableView)gui.getGUINode(MT_LECTURE_TABLE_VIEW);
        data.removeLecture(addedLecture);
        lecTable.refresh();

    }
}
