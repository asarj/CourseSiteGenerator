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
public class Lecture {
    private StringProperty section;
    private StringProperty day;
    private StringProperty room;
    private StringProperty time;
    
    public Lecture(String section, String day, String time, String room) {
        this.section = new SimpleStringProperty(section);
        this.day = new SimpleStringProperty(day);
        this.room = new SimpleStringProperty(room);
        this.time = new SimpleStringProperty(time);
    }

    public StringProperty getSection() {
        return section;
    }

    public void setSection(StringProperty section) {
        this.section = section;
    }

    public StringProperty getDay() {
        return day;
    }

    public void setDay(StringProperty day) {
        this.day = day;
    }

    public StringProperty getRoom() {
        return room;
    }

    public void setRoom(StringProperty room) {
        this.room = room;
    }

    public StringProperty getTime() {
        return time;
    }

    public void setTime(StringProperty time) {
        this.time = time;
    }
    
}
