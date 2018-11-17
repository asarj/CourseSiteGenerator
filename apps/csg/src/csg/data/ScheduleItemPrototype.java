/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;

import java.time.LocalDate;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Ajay
 */
public class ScheduleItemPrototype<E extends Comparable<E>> implements Comparable<E> {
    private StringProperty type;
    private StringProperty date;
    private StringProperty title;
    private StringProperty topic;
    private StringProperty link;
    
    public ScheduleItemPrototype(String type, LocalDate day, String time, String room, String link) {
        this.type = new SimpleStringProperty(type);
        this.date = new SimpleStringProperty(Integer.toString(day.getMonthValue()) + "/" + Integer.toString(day.getDayOfMonth()) + "/" + Integer.toString(day.getYear()));
        this.title = new SimpleStringProperty(room);
        this.topic = new SimpleStringProperty(time);
        this.link = new SimpleStringProperty(link);
    }

    public String getType() {
        return type.get();
    }

    public void setType(StringProperty type) {
        this.type = type;
    }

    public String getDate() {
        return date.get();
    }

    public void setDate(StringProperty date) {
        this.date = date;
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(StringProperty title) {
        this.title = title;
    }

    public String getTopic() {
        return topic.get();
    }

    public void setTopic(StringProperty topic) {
        this.topic = topic;
    }

    public String getLink() {
        return link.get();
    }

    public void setLink(StringProperty link) {
        this.link = link;
    }

    @Override
    public int compareTo(E o) {
        return getDate().compareTo(((ScheduleItemPrototype)o).getDate());
    }
    
    
    
}