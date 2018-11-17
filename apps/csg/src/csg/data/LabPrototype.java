/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Ajay
 */
public class LabPrototype<E extends Comparable<E>> implements Comparable<E> {
    private StringProperty section;
    private StringProperty dayAndTime;
    private StringProperty room;
    private StringProperty TA1;
    private StringProperty TA2;
    
    public LabPrototype(String section, String day, String room, String ta1, String ta2) {
        this.section = new SimpleStringProperty(section);
        this.dayAndTime = new SimpleStringProperty(day);
        this.room = new SimpleStringProperty(room);
        this.TA1 = new SimpleStringProperty(ta1);
        this.TA2 = new SimpleStringProperty(ta2);
    }

    public String getSection() {
        return section.get();
    }

    public void setSection(StringProperty section) {
        this.section = section;
    }

    public String getDayAndTime() {
        return dayAndTime.get();
    }

    public void setDayAndTime(StringProperty dayAndTime) {
        this.dayAndTime = dayAndTime;
    }

    public String getRoom() {
        return room.get();
    }

    public void setRoom(StringProperty room) {
        this.room = room;
    }

    public String getTA1() {
        return TA1.get();
    }

    public void setTA1(StringProperty TA1) {
        this.TA1 = TA1;
    }

    public String getTA2() {
        return TA2.get();
    }

    public void setTA2(StringProperty TA2) {
        this.TA2 = TA2;
    }

    @Override
    public int compareTo(E o) {
        return getSection().compareTo(((LabPrototype)o).getSection());
    }
}
