/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;

import csg.CSGApp;
import static csg.CSGPropertyType.MT_LAB_TABLE_VIEW;
import static csg.CSGPropertyType.MT_LECTURE_TABLE_VIEW;
import static csg.CSGPropertyType.MT_RECITATION_TABLE_VIEW;
import djf.modules.AppGUIModule;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

/**
 *
 * @author Ajay
 */
public class MeetingTimesData {
    CSGApp app;
    ObservableList<LecturePrototype> lectures;
    ObservableList<RecitationPrototype> recitations;
    ObservableList<LabPrototype> labs;
    ArrayList<LecturePrototype> lectureItems;
    ArrayList<RecitationPrototype> recitationItems;
    ArrayList<LabPrototype> labItems;
    
    
    public MeetingTimesData(CSGApp initApp){
        app = initApp;
        AppGUIModule gui = app.getGUIModule();
        TableView<LecturePrototype> lectureTable = (TableView)gui.getGUINode(MT_LECTURE_TABLE_VIEW);
        TableView<RecitationPrototype> recTable = (TableView)gui.getGUINode(MT_RECITATION_TABLE_VIEW);
        TableView<LabPrototype> labTable = (TableView)gui.getGUINode(MT_LAB_TABLE_VIEW);
        lectures = lectureTable.getItems();
        recitations = recTable.getItems();
        labs = labTable.getItems();
        lectureItems = new ArrayList<>();
        recitationItems = new ArrayList<>();
        labItems = new ArrayList<>();
    }
    
    public void reset(){
        lectures.clear();
        recitations.clear();
        labs.clear();
        lectureItems.clear();
        recitationItems.clear();
        labItems.clear();
    }
    
    public LecturePrototype addLecture(){
        LecturePrototype l = new LecturePrototype("?", "?", "?", "?");
        lectureItems.add(l);
        selectLectures();
        return l;
    }
    public void addLecture(LecturePrototype l){
        lectureItems.add(l);
        selectLectures();
    }
    
    public LecturePrototype editLecture(LecturePrototype l, String TYPE, String change){
        if(TYPE.equals("SECTION")){
            for(LecturePrototype x: lectureItems){
                if(x.equals(l)){
                    x.setSection(change);
                    return x;
                }
            }
            selectLectures();
        }
        else if(TYPE.equals("DAY")){
            for(LecturePrototype x: lectureItems){
                if(x.equals(l)){
                    x.setDay(change);
                    return x;
                }
            }
            selectLectures();
        }
        else if(TYPE.equals("TIME")){
            for(LecturePrototype x: lectureItems){
                if(x.equals(l)){
                    x.setTime(change);
                    return x;
                }
            }
            selectLectures();
        }
        else if(TYPE.equals("ROOM")){
            for(LecturePrototype x: lectureItems){
                if(x.equals(l)){
                    x.setRoom(change);
                    return x;
                }
            }
            selectLectures();
        }
        return l;
    }
    
    public void removeLecture(LecturePrototype l){
        AppGUIModule gui = app.getGUIModule();
        TableView<LecturePrototype> lecTable = (TableView)gui.getGUINode(MT_LECTURE_TABLE_VIEW);
        lectureItems.remove(l);
        selectLectures();
    }
    
    public void addRecitation(){
        RecitationPrototype r = new RecitationPrototype("?", "?", "?", "?", "?");
        recitationItems.add(r);
        selectRecitations();
    }
    
    public void addRecitation(RecitationPrototype r){
        recitationItems.add(r);
        selectRecitations();
    }
    
    public void removeRecitation(){
        
    }
    
    public void addLab(){
        LabPrototype l = new LabPrototype("?", "?", "?", "?", "?");
        labItems.add(l);
        selectLabs();
    }
    
    public void addLab(LabPrototype l){
        labItems.add(l);
        selectLabs();
    }
    
    public void removeLab(){
        
    }
    
    public void selectLectures() {
        lectures.clear();
        Iterator<LecturePrototype> lecIt = this.lectureIterator();
        while (lecIt.hasNext()) {
            LecturePrototype lec = lecIt.next();
            lectures.add(lec);
        }
        
        // SORT THEM BY NAME
        sortLectures();

        app.getFoolproofModule().updateAll();
    }
    
    public void selectRecitations() {
        recitations.clear();
        Iterator<RecitationPrototype> recIt = this.recitationIterator();
        while (recIt.hasNext()) {
            RecitationPrototype rec = recIt.next();
            recitations.add(rec);
        }
        
        // SORT THEM BY NAME
        sortRecitations();

        app.getFoolproofModule().updateAll();
    }
    
    public void selectLabs() {
        labs.clear();
        Iterator<LabPrototype> labIt = this.labIterator();
        while (labIt.hasNext()) {
            LabPrototype rec = labIt.next();
            labs.add(rec);
        }
        
        // SORT THEM BY NAME
        sortRecitations();

        app.getFoolproofModule().updateAll();
    }
    
    private void sortLectures() {
        Collections.sort(lectures);
    }
    
    private void sortRecitations() {
        Collections.sort(recitations);
    }
    
    private void sortLabs() {
        Collections.sort(labs);
    }
    
    public Iterator<LecturePrototype> lectureIterator() {
        return new LectureIterator();
    }
    
    public Iterator<RecitationPrototype> recitationIterator() {
        return new RecitationIterator();
    }
    
    public Iterator<LabPrototype> labIterator() {
        return new LabIterator();
    }
    
    private class LectureIterator implements Iterator {
        Iterator lectureIt = lectureItems.iterator();

        public LectureIterator() {}
        
        @Override
        public boolean hasNext() {
            if (lectureIt.hasNext())
                return true;
            else
                return false;                
        }

        @Override
        public Object next() {
            if (lectureIt.hasNext())
                return lectureIt.next();
            else
                return null;
        }
    }
    
    private class RecitationIterator implements Iterator {
        Iterator recitationIt = recitationItems.iterator();

        public RecitationIterator() {}
        
        @Override
        public boolean hasNext() {
            if (recitationIt.hasNext())
                return true;
            else
                return false;                
        }

        @Override
        public Object next() {
            if (recitationIt.hasNext())
                return recitationIt.next();
            else
                return null;
        }
    }
    
    private class LabIterator implements Iterator {
        Iterator labIt = labItems.iterator();

        public LabIterator() {}
        
        @Override
        public boolean hasNext() {
            if (labIt.hasNext())
                return true;
            else
                return false;                
        }

        @Override
        public Object next() {
            if (labIt.hasNext())
                return labIt.next();
            else
                return null;
        }
    }
    
}
