/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;
import csg.CSGApp;
import static csg.CSGPropertyType.SCH_ITEMS_TABLE_VIEW;
import djf.modules.AppGUIModule;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

/**
 *
 * @author Ajay
 */
public class ScheduleData {
    CSGApp app;
    LocalDate startDate;
    LocalDate endDate;
    String start;
    String end;
    ObservableList<ScheduleItemPrototype> schedule;
    ArrayList<ScheduleItemPrototype> scheduleItems;
    
    public ScheduleData(CSGApp initApp){
        app = initApp;
        AppGUIModule gui = app.getGUIModule();
        TableView<ScheduleItemPrototype> scheduleTable = (TableView)gui.getGUINode(SCH_ITEMS_TABLE_VIEW);
        schedule = scheduleTable.getItems();
        scheduleItems = new ArrayList<>();
    }
    
    public void reset(){
        schedule.clear();
        scheduleItems.clear();
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
    
    public void addScheduleItem(ScheduleItemPrototype s){
        scheduleItems.add(s);
        selectSchedule();
    }
    
    public ScheduleItemPrototype editScheduleItem(ScheduleItemPrototype old, ScheduleItemPrototype s){
        for(ScheduleItemPrototype x : scheduleItems){
            if(x.equals(old)){
                x.setType(s.getType());
                x.setTopic(s.getTopic());
                x.setLocalDate(s.getLocalDate());
                x.setDate(Integer.toString(s.getLocalDate().getMonthValue()) 
                        + "/" + Integer.toString(s.getLocalDate().getDayOfMonth()) 
                        + "/" + Integer.toString(s.getLocalDate().getYear()));
                x.setTitle(s.getTitle());
                x.setTopic(s.getTopic());
                x.setLink(s.getLink());
            }
        }
        selectSchedule();
        return s;
    }
    
    public void removeScheduleItem(ScheduleItemPrototype s){
        scheduleItems.remove(s);
        selectSchedule();
    }
    
    public void selectSchedule() {
        schedule.clear();
        Iterator<ScheduleItemPrototype> schIt = this.scheduleIterator();
        while (schIt.hasNext()) {
            ScheduleItemPrototype sip = schIt.next();
            schedule.add(sip);
        }
        
        // SORT THEM BY NAME
        sortSchedule();

        app.getFoolproofModule().updateAll();
    }
    
    private void sortSchedule() {
        Collections.sort(schedule);
    }
    
    public Iterator<ScheduleItemPrototype> scheduleIterator() {
        return new ScheduleIterator();
    }
    
    private class ScheduleIterator implements Iterator {
        Iterator scheduleIt = scheduleItems.iterator();

        public ScheduleIterator() {}
        
        @Override
        public boolean hasNext() {
            if (scheduleIt.hasNext())
                return true;
            else
                return false;                
        }

        @Override
        public Object next() {
            if (scheduleIt.hasNext())
                return scheduleIt.next();
            else
                return null;
        }
    }
}
