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

    public void setSection(StringProperty section) {
        this.section = section;
    }

    public String getDay() {
        return day.get();
    }

    public void setDay(StringProperty day) {
        this.day = day;
    }

    public String getRoom() {
        return room.get();
    }

    public void setRoom(StringProperty room) {
        this.room = room;
    }

    public String getTime() {
        return time.get();
    }

    public void setTime(StringProperty time) {
        this.time = time;
    }

    @Override
    public int compareTo(E o) {
        return getSection().compareTo(((LecturePrototype)o).getSection());
    }
    
}
