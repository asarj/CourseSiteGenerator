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
    private LocalDate localDate;
    private StringProperty title;
    private StringProperty topic;
    private StringProperty link;
    
    public ScheduleItemPrototype(String type, LocalDate day, String topic, String title, String link) {
        this.type = new SimpleStringProperty(type);
        this.localDate = day;
        this.date = new SimpleStringProperty(Integer.toString(day.getMonthValue()) + "/" + Integer.toString(day.getDayOfMonth()) + "/" + Integer.toString(day.getYear()));
        this.title = new SimpleStringProperty(title);
        this.topic = new SimpleStringProperty(topic);
        this.link = new SimpleStringProperty(link);
    }

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getDate() {
        return date.get();
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getTopic() {
        return topic.get();
    }

    public void setTopic(String topic) {
        this.topic.set(topic);
    }

    public String getLink() {
        return link.get();
    }

    public void setLink(String link) {
        this.link.set(link);
    }
    
    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
        setDate(Integer.toString(localDate.getMonthValue()) + "/" + Integer.toString(localDate.getDayOfMonth()) + "/" + Integer.toString(localDate.getYear()));
    }

    @Override
    public int compareTo(E o) {
        return getLocalDate().compareTo(((ScheduleItemPrototype)o).getLocalDate());
    }
    
    public boolean equals(ScheduleItemPrototype s){
        return this.getType().equals(s.getType()) && 
                this.getLocalDate().equals(s.getLocalDate()) && 
                this.getTitle().equals(s.getTitle()) && 
                this.getTopic().equals(s.getTopic()) &&
                this.getLink().equals(s.getLink());
    }
    
    public ScheduleItemPrototype clone(){
        return new ScheduleItemPrototype(this.getType(), this.getLocalDate(), this.getTopic(), this.getTitle(), this.getLink());
    }
    
}
