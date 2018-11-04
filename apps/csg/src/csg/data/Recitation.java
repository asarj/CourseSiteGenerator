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
public class Recitation {
    private StringProperty section;
    private StringProperty dayAndTime;
    private StringProperty room;
    private TeachingAssistantPrototype TA1;
    private TeachingAssistantPrototype TA2;
    
    public Recitation(String section, String day, String room, TeachingAssistantPrototype ta1, TeachingAssistantPrototype ta2) {
        this.section = new SimpleStringProperty(section);
        this.dayAndTime = new SimpleStringProperty(day);
        this.room = new SimpleStringProperty(room);
        this.TA1 = ta1;
        this.TA2 = ta2;
    }

    public StringProperty getSection() {
        return section;
    }

    public void setSection(StringProperty section) {
        this.section = section;
    }

    public StringProperty getDayAndTime() {
        return dayAndTime;
    }

    public void setDayAndTime(StringProperty dayAndTime) {
        this.dayAndTime = dayAndTime;
    }

    public StringProperty getRoom() {
        return room;
    }

    public void setRoom(StringProperty room) {
        this.room = room;
    }

    public TeachingAssistantPrototype getTA1() {
        return TA1;
    }

    public void setTA1(TeachingAssistantPrototype TA1) {
        this.TA1 = TA1;
    }

    public TeachingAssistantPrototype getTA2() {
        return TA2;
    }

    public void setTA2(TeachingAssistantPrototype TA2) {
        this.TA2 = TA2;
    }
}
