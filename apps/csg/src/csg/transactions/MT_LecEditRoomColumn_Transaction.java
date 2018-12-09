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
public class MT_LecEditRoomColumn_Transaction implements jTPS_Transaction{
    CSGApp app;
    CSGData d;
    MeetingTimesData data;
    String old;
    String n;
    LecturePrototype newLecture;
    
    public MT_LecEditRoomColumn_Transaction(CSGApp initApp, CSGData d, MeetingTimesData data, LecturePrototype l, String old, String n){
        app = initApp;
        this.d = d;
        this.data = data;
        this.newLecture = l;
        this.old = old;
        this.n = n;
    }
    
    @Override
    public void doTransaction() {
        AppGUIModule gui = app.getGUIModule();
        TableView lecTable = (TableView)gui.getGUINode(MT_LECTURE_TABLE_VIEW);
        newLecture = data.editLecture(newLecture, "ROOM", n);
        lecTable.refresh();
    }

    @Override
    public void undoTransaction() {
        AppGUIModule gui = app.getGUIModule();
        TableView lecTable = (TableView)gui.getGUINode(MT_LECTURE_TABLE_VIEW);
        data.editLecture(newLecture, "ROOM", old);
        lecTable.refresh();

    }
}
