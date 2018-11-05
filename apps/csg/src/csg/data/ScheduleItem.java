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
public class ScheduleItem {
    private StringProperty type;
    private StringProperty date;
    private StringProperty title;
    private StringProperty topic;
    
    public ScheduleItem(String section, String day, String time, String room) {
        this.type = new SimpleStringProperty(section);
        this.date = new SimpleStringProperty(day);
        this.title = new SimpleStringProperty(room);
        this.topic = new SimpleStringProperty(time);
    }

    public StringProperty getType() {
        return type;
    }

    public void setType(StringProperty type) {
        this.type = type;
    }

    public StringProperty getDate() {
        return date;
    }

    public void setDate(StringProperty date) {
        this.date = date;
    }

    public StringProperty getTitle() {
        return title;
    }

    public void setTitle(StringProperty title) {
        this.title = title;
    }

    public StringProperty getTopic() {
        return topic;
    }

    public void setTopic(StringProperty topic) {
        this.topic = topic;
    }
    
}
