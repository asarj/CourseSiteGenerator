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
public class LecturePrototype<E extends Comparable<E>> implements Comparable<E> {
    private StringProperty section;
    private StringProperty day;
    private StringProperty time;
    private StringProperty room;
    
    public LecturePrototype(String section, String day, String time, String room) {
        this.section = new SimpleStringProperty(section);
        this.day = new SimpleStringProperty(day);
        this.time = new SimpleStringProperty(time);
        this.room = new SimpleStringProperty(room);
    }

    public String getSection() {
        return section.get();
    }

    public void setSection(String section) {
        this.section.set(section);
    }

    public String getDay() {
        return day.get();
    }

    public void setDay(String day) {
        this.day.set(day);
    }

    public String getRoom() {
        return room.get();
    }

    public void setRoom(String room) {
        this.room.set(room);
    }

    public String getTime() {
        return time.get();
    }

    public void setTime(String time) {
        this.time.set(time);
    }

    @Override
    public int compareTo(E o) {
        return getSection().compareTo(((LecturePrototype)o).getSection());
    }
    
    public boolean equals(LecturePrototype l){
        return l.getSection().equals(this.getSection()) && l.getDay().equals(this.getDay()) && l.getTime().equals(this.getTime()) && l.getRoom().equals(this.getRoom());
    }
    
}
