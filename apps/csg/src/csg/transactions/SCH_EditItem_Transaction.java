/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import csg.CSGApp;
import static csg.CSGPropertyType.SCH_ITEMS_TABLE_VIEW;
import csg.data.CSGData;
import csg.data.ScheduleData;
import csg.data.ScheduleItemPrototype;
import csg.workspace.CSGWorkspace;
import djf.modules.AppGUIModule;
import java.time.LocalDate;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import jtps.jTPS_Transaction;

/**
 *
 * @author Ajay
 */
public class SCH_EditItem_Transaction implements jTPS_Transaction{
    CSGApp app;
    CSGData d;
    ScheduleData data;
    ScheduleItemPrototype newItem; ScheduleItemPrototype oldItem;
    String newOption; String oldOption;
    LocalDate oldDate; LocalDate newDate;
    String oldTopic; String newTopic;
    String oldTitle; String newTitle;
    String oldLink; String newLink;
    
    public SCH_EditItem_Transaction(CSGApp app, CSGData d, ScheduleData data, ScheduleItemPrototype old, String typeOption, LocalDate value, String topicOption, String titleOption, String linkOption) {
        this.app = app;
        this.d = d;
        this.data = data;
        this.oldItem = old;
        this.oldDate = oldItem.getLocalDate();
        this.oldOption = oldItem.getType();
        this.oldTopic = oldItem.getTopic();
        this.oldTitle = oldItem.getTitle();
        this.oldLink = oldItem.getLink();
        this.newItem = old;
        this.newOption = typeOption;
        this.newDate = value;
        this.newTopic = topicOption;
        this.newTitle = titleOption;
        this.newLink = linkOption;
    }
    
    @Override
    public void doTransaction() {
        AppGUIModule gui = app.getGUIModule();
        TableView<ScheduleItemPrototype> schTable = (TableView)gui.getGUINode(SCH_ITEMS_TABLE_VIEW);
        newItem.setType(newOption);
        newItem.setLocalDate(newDate);
        newItem.setTopic(newTopic);
        newItem.setTitle(newTitle);
        newItem.setLink(newLink);
        if(!newItem.equals(oldItem)){
            data.editScheduleItem(oldItem, newItem);
        }
//        ScheduleItemPrototype temp = old.clone();
//        this.old = data.editScheduleItem(newItem, old);
//        this.newItem = temp;
        schTable.refresh();
        /**
         * temp = old
         * old = new
         * new = temp
         */
    }

    @Override
    public void undoTransaction() {
        AppGUIModule gui = app.getGUIModule();
        TableView<ScheduleItemPrototype> schTable = (TableView)gui.getGUINode(SCH_ITEMS_TABLE_VIEW);
        newItem.setType(oldOption);
        newItem.setLocalDate(oldDate);
        newItem.setTopic(oldTopic);
        newItem.setTitle(oldTitle);
        newItem.setLink(oldLink);
        data.editScheduleItem(oldItem, newItem);
//        ScheduleItemPrototype temp = newItem.clone();
//        this.newItem = data.editScheduleItem(old, newItem);
//        this.old = temp;
        schTable.refresh();
        /**
         * temp = new;
         * new = old;
         * old = temp;
         */
    }
}
