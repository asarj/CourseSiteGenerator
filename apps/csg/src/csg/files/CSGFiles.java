package csg.files;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import djf.components.AppDataComponent;
import djf.components.AppFileComponent;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import csg.CSGApp;
import static csg.CSGPropertyType.DEFAULT_FAVICON_TEXT;
import static csg.CSGPropertyType.DEFAULT_LFIMG_TEXT;
import static csg.CSGPropertyType.DEFAULT_NAVBAR_TEXT;
import static csg.CSGPropertyType.DEFAULT_RFIMG_TEXT;
import static csg.CSGPropertyType.HREF;
import static csg.CSGPropertyType.SITE_CSS_COMBO_BOX;
import static csg.CSGPropertyType.SITE_EMAIL_TEXT_FIELD;
import static csg.CSGPropertyType.SITE_EXPORT_LABEL;
import static csg.CSGPropertyType.SITE_HP_TEXT_FIELD;
import static csg.CSGPropertyType.SITE_NAME_TEXT_FIELD;
import static csg.CSGPropertyType.SITE_ROOM_TEXT_FIELD;
import static csg.CSGPropertyType.SITE_SEMESTERS_COMBO_BOX;
import static csg.CSGPropertyType.SITE_SUBJECTNUM_COMBO_BOX;
import static csg.CSGPropertyType.SITE_SUBJECT_COMBO_BOX;
import static csg.CSGPropertyType.SITE_TITLE_TEXT_FIELD;
import static csg.CSGPropertyType.SITE_YEARS_COMBO_BOX;
import csg.data.CSGData;
import csg.data.LabPrototype;
import csg.data.LecturePrototype;
import csg.data.MeetingTimesData;
import csg.data.OHData;
import csg.data.RecitationPrototype;
import csg.data.ScheduleData;
import csg.data.ScheduleItemPrototype;
import csg.data.SiteData;
import csg.data.SyllabusData;
import csg.data.TAType;
import csg.data.TeachingAssistantPrototype;
import csg.data.TimeSlot;
import csg.data.TimeSlot.DayOfWeek;
import csg.workspace.CSGWorkspace;
import static djf.AppPropertyType.APP_FILE_PROTOCOL;
import static djf.AppPropertyType.APP_PATH_IMAGES;
import djf.modules.AppGUIModule;
import djf.ui.dialogs.AppWebDialog;
import java.io.File;
import java.io.StringReader;
import java.math.BigDecimal;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javax.json.JsonObjectBuilder;
import properties_manager.PropertiesManager;
import org.apache.commons.io.FileUtils;

/**
 * This class serves as the file component for the TA
 * manager app. It provides all saving and loading 
 * services for the application.
 * 
 * @author Richard McKenna
 */
public class CSGFiles implements AppFileComponent {
    // THIS IS THE APP ITSELF
    CSGApp app;
    
    // THESE ARE USED FOR IDENTIFYING JSON TYPES
    
    // SITE DATA FIELDS
    static final String JSON_SITE_SUBJECT = "subject";
    static final String JSON_SITE_OPTION = "option";
    static final String JSON_SITE_NUMBER = "number";
    static final String JSON_SITE_SEMESTER = "semester";
    static final String JSON_SITE_YEAR = "year";
    static final String JSON_SITE_TITLE = "title";
    static final String JSON_SITE_EXPORT_URL = "export_url";
    static final String JSON_SITE_PAGES = "pages";
    static final String JSON_SITE_PAGES_LINK = "link";
    static final String JSON_SITE_PAGES_NAME = "name";
    static final String JSON_SITE_LOGOS = "logos";
    static final String JSON_SITE_FAVICON = "favicon";
    static final String JSON_SITE_NAVBAR = "navbar";
    static final String JSON_SITE_BOTTOM_LEFT = "bottom_left";
    static final String JSON_SITE_BOTTOM_RIGHT = "bottom_right";
    static final String JSON_SITE_SRC = "src";
    static final String JSON_SITE_HREF = "href";
    static final String JSON_SITE_SELECTED_CSS = "css";
    static final String JSON_SITE_INSTRUCTOR = "instructor";
    static final String JSON_SITE_INSTRUCTOR_NAME = "name";
    static final String JSON_SITE_INSTRUCTOR_EMAIL = "email";
    static final String JSON_SITE_INSTRUCTOR_ROOM = "room";
    static final String JSON_SITE_INSTRUCTOR_HP = "link";
    static final String JSON_SITE_INSTRUCTOR_PHOTO = "photo";
    static final String JSON_SITE_INSTRUCTOR_HOURS = "hours";
    
    // SYLLABUS DATA FIELDS
    static final String JSON_SYL_DESCRIPTION = "description";
    static final String JSON_SYL_TOPICS = "topics";
    static final String JSON_SYL_PREREQUISITES = "prerequisites";
    static final String JSON_SYL_OUTCOMES = "outcomes";
    static final String JSON_SYL_TEXTBOOKS = "textbooks";
    static final String JSON_SYL_GC = "gradedComponents";
    static final String JSON_SYL_GN = "gradingNote";
    static final String JSON_SYL_AD = "academicDishonesty";
    static final String JSON_SYL_SA = "specialAssistance";
    
    // MEETING TIMES DATA FIELDS
    static final String JSON_MT_LECTURES = "lectures";
    static final String JSON_MT_LECTURES_SECTION = "section";
    static final String JSON_MT_LECTURES_DAYS = "days";
    static final String JSON_MT_LECTURES_TIME = "time";
    static final String JSON_MT_LECTURES_ROOM = "room";
    static final String JSON_MT_RECITATIONS = "recitations";
    static final String JSON_MT_RECITATIONS_SECTION = "section";
    static final String JSON_MT_RECITATIONS_DAY_TIME = "day_time";
    static final String JSON_MT_RECITATIONS_LOCATION = "location";
    static final String JSON_MT_RECITATIONS_TA1 = "ta_1";
    static final String JSON_MT_RECITATIONS_TA2 = "ta_2";
    static final String JSON_MT_LABS = "labs";
    static final String JSON_MT_LABS_SECTION = "section";
    static final String JSON_MT_LABS_DAY_TIME = "day_time";
    static final String JSON_MT_LABS_LOCATION = "location";
    static final String JSON_MT_LABS_TA1 = "ta_1";
    static final String JSON_MT_LABS_TA2 = "ta_2";
    
    // OH DATA FIELDS
    static final String JSON_OH_GRAD_TAS = "grad_tas";
    static final String JSON_OH_UNDERGRAD_TAS = "undergrad_tas";
    static final String JSON_OH_NAME = "name";
    static final String JSON_OH_EMAIL = "email";
    static final String JSON_OH_TYPE = "type";
    static final String JSON_OH_OFFICE_HOURS = "officeHours";
    static final String JSON_OH_START_HOUR = "startHour";
    static final String JSON_OH_END_HOUR = "endHour";
    static final String JSON_OH_START_TIME = "time";
    static final String JSON_OH_DAY_OF_WEEK = "day";
    static final String JSON_OH_MONDAY = "monday";
    static final String JSON_OH_TUESDAY = "tuesday";
    static final String JSON_OH_WEDNESDAY = "wednesday";
    static final String JSON_OH_THURSDAY = "thursday";
    static final String JSON_OH_FRIDAY = "friday";
    
    // SCHEDULE DATA FIELDS
    static final String JSON_SCH_START_DATE = "startDate";
    static final String JSON_SCH_END_DATE = "endDate";
    static final String JSON_SCH_STARTING_MONDAY_MONTH = "startingMondayMonth";
    static final String JSON_SCH_STARTING_MONDAY_DAY = "startingMondayDay";
    static final String JSON_SCH_STARTING_MONDAY_YEAR = "startingMondayYear";
    static final String JSON_SCH_ENDING_FRIDAY_MONTH = "endingFridayMonth";
    static final String JSON_SCH_ENDING_FRIDAY_DAY = "endingFridayDay";
    static final String JSON_SCH_ENDING_FRIDAY_YEAR = "endingFridayYear";
    static final String JSON_SCH_HOLIDAYS = "holidays";
    static final String JSON_SCH_LECTURES = "lectures";
    static final String JSON_SCH_REFERENCES = "references";
    static final String JSON_SCH_RECITATIONS = "recitations";
    static final String JSON_SCH_HWS = "hws";
    static final String JSON_SCH_LABS = "labs";
    static final String JSON_SCH_EVENT_DATE = "date";
    static final String JSON_SCH_EVENT_MONTH = "month";
    static final String JSON_SCH_EVENT_DAY = "day";
    static final String JSON_SCH_EVENT_YEAR = "year";
    static final String JSON_SCH_EVENT_TITLE = "title";
    static final String JSON_SCH_EVENT_TOPIC = "topic";
    static final String JSON_SCH_EVENT_LINK = "link";

    public CSGFiles(CSGApp initApp) {
        app = initApp;
    }

    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException {
	// CLEAR THE OLD DATA OUT
        AppGUIModule gui = app.getGUIModule();
	CSGData d = (CSGData)data;
        CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
        SiteData siteDataManager = d.getSiteData();
        SyllabusData syllabusDataManager = d.getSyllabusData();
        MeetingTimesData mtDataManager = d.getMeetingTimesData();
        OHData ohDataManager = d.getOfficeHoursData();
        ScheduleData schDataManager = d.getScheduleData();
        
        mtDataManager.reset();
        ohDataManager.reset();
        schDataManager.reset();
        

	// LOAD THE JSON FILE WITH ALL THE DATA
	JsonObject json = loadJSONFile(filePath);
        JsonObject jsonCB = loadJSONFileForCB("./work/jsondata/cboptions.json");
        /******************LOADS THE SITE DATA*********************/
//        try{
        ObservableList subjectsCB = FXCollections.observableArrayList();
        ObservableList subjectsNumsCB = FXCollections.observableArrayList();
        ObservableList semsCB = FXCollections.observableArrayList();
        ObservableList yearsCB = FXCollections.observableArrayList();
        JsonArray subjects = jsonCB.getJsonArray(JSON_SITE_SUBJECT);
        for(int i = 0; i < subjects.size(); i++){
            JsonObject item = subjects.getJsonObject(i);
            subjectsCB.add(item.getString(JSON_SITE_OPTION));
        }
        JsonArray subjectsNums = jsonCB.getJsonArray(JSON_SITE_NUMBER);
        for(int i = 0; i < subjectsNums.size(); i++){
            JsonObject item = subjectsNums.getJsonObject(i);
            subjectsNumsCB.add(item.getString(JSON_SITE_OPTION));
        }
        JsonArray semesters = jsonCB.getJsonArray(JSON_SITE_SEMESTER);
        for(int i = 0; i < semesters.size(); i++){
            JsonObject item = semesters.getJsonObject(i);
            semsCB.add(item.getString(JSON_SITE_OPTION));
        }
        JsonArray years = jsonCB.getJsonArray(JSON_SITE_YEAR);
        for(int i = 0; i < years.size(); i++){
            JsonObject item = years.getJsonObject(i);
            yearsCB.add(item.getString(JSON_SITE_OPTION));
        }
        String courseSubject = json.getString(JSON_SITE_SUBJECT);
        if(!courseSubject.trim().equals(""))
            siteDataManager.setSelectedName(courseSubject);
        boolean subFound = false;
        for(int i = 0; i < subjectsCB.size(); i++){
            JsonObject item = subjects.getJsonObject(i);
            if(item.getString(JSON_SITE_OPTION).equals(courseSubject)){
                subFound = true;
                break;
            }
        }
        if(!subFound){
            subjectsCB.add(courseSubject);
        }
        
        String courseNum = json.getString(JSON_SITE_NUMBER);
        if(!courseNum.trim().equals(""))
            siteDataManager.setSelectedNum(courseNum);
        boolean subNumFound = false;
        for(int i = 0; i < subjectsNumsCB.size(); i++){
            JsonObject item = subjectsNums.getJsonObject(i);
            if(item.getString(JSON_SITE_OPTION).equals(courseNum)){
                subNumFound = true;
                break;
            }
        }
        if(!subNumFound){
            subjectsNumsCB.add(courseNum);
        }
        
        String courseSem = json.getString(JSON_SITE_SEMESTER);
        if(!courseSem.trim().equals(""))
            siteDataManager.setSelectedSem(courseSem);
        boolean subSemFound = false;
        for(int i = 0; i < semsCB.size(); i++){
            JsonObject item = semesters.getJsonObject(i);
            if(item.getString(JSON_SITE_OPTION).equals(courseSem)){
                subSemFound = true;
                break;
            }
        }
        if(!subSemFound){
            semsCB.add(courseSem);
        }
        
        String courseYr = json.getString(JSON_SITE_YEAR);
        if(!courseYr.trim().equals(""))
            siteDataManager.setSelectedYear(courseYr);
        boolean subYearFound = false;
        for(int i = 0; i < yearsCB.size(); i++){
            JsonObject item = years.getJsonObject(i);
            if(item.getString(JSON_SITE_OPTION).equals(courseYr)){
                subYearFound = true;
                break;
            }
        }
        if(!subYearFound){
            yearsCB.add(courseYr);
        }
        
        String courseTitle = json.getString(JSON_SITE_TITLE);
        if(!courseTitle.trim().equals("")){
            siteDataManager.setTitle(courseTitle);
            TextField nameTF = (TextField)gui.getGUINode(SITE_TITLE_TEXT_FIELD);
            nameTF.setText(courseTitle);
        }
        
        String expUrl = json.getString(JSON_SITE_EXPORT_URL);
        siteDataManager.setExp(expUrl);
        
        workspace.setSubjects(subjectsCB);
        workspace.setSubjectNums(subjectsNumsCB);
        workspace.setSemesters(semsCB);
        workspace.setYears(yearsCB);
//        ((ComboBox)gui.getGUINode(SITE_SUBJECT_COMBO_BOX)).setItems(subjectsCB);
//        ((ComboBox)gui.getGUINode(SITE_SUBJECTNUM_COMBO_BOX)).setItems(subjectsNumsCB);
//        ((ComboBox)gui.getGUINode(SITE_SEMESTERS_COMBO_BOX)).setItems(semsCB);
//        ((ComboBox)gui.getGUINode(SITE_YEARS_COMBO_BOX)).setItems(yearsCB);
        
        JsonArray jsonPagesArray = json.getJsonArray(JSON_SITE_PAGES);
        ArrayList<String> loadedChoices = new ArrayList<>();
        for(int i = 0; i < jsonPagesArray.size(); i++){
            JsonObject item = jsonPagesArray.getJsonObject(i);
            loadedChoices.add(item.getString(JSON_SITE_PAGES_NAME));
        }
        siteDataManager.setSelectedPageOptions(loadedChoices);
        
        JsonArray jsonLogosArray = json.getJsonArray(JSON_SITE_LOGOS);
        if(!jsonLogosArray.getJsonObject(0).getString(JSON_SITE_FAVICON).equals("")){
            siteDataManager.setFavUrl(jsonLogosArray.getJsonObject(0).getString(JSON_SITE_FAVICON));
            try{
                workspace.getFviImgView().setImage(new Image(jsonLogosArray.getJsonObject(0).getString(JSON_SITE_FAVICON)));
            }
            catch(Exception e){
                
            }
        }
        else{
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            siteDataManager.setFavUrl(props.getProperty(APP_PATH_IMAGES) + props.getProperty(DEFAULT_FAVICON_TEXT));
            workspace.getFviImgView().setImage(new Image(props.getProperty(APP_FILE_PROTOCOL) + props.getProperty(APP_PATH_IMAGES) + props.getProperty(DEFAULT_FAVICON_TEXT)));
        }
        if(!jsonLogosArray.getJsonObject(1).getString(JSON_SITE_NAVBAR).equals("")){
            siteDataManager.setNavUrl(jsonLogosArray.getJsonObject(1).getString(JSON_SITE_NAVBAR));
            try{
                workspace.getNavImgView().setImage(new Image(jsonLogosArray.getJsonObject(1).getString(JSON_SITE_NAVBAR)));
            }
            catch(Exception e){
                
            }
        }
        else{
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            siteDataManager.setNavUrl(props.getProperty(APP_PATH_IMAGES) + props.getProperty(DEFAULT_NAVBAR_TEXT));
            workspace.getNavImgView().setImage(new Image(props.getProperty(APP_FILE_PROTOCOL) + props.getProperty(APP_PATH_IMAGES) + props.getProperty(DEFAULT_NAVBAR_TEXT)));
        }
        if(!jsonLogosArray.getJsonObject(2).getString(JSON_SITE_BOTTOM_LEFT).equals("")){
            siteDataManager.setLeftUrl(jsonLogosArray.getJsonObject(2).getString(JSON_SITE_BOTTOM_LEFT));
            try{
                workspace.getLeftImgView().setImage(new Image(jsonLogosArray.getJsonObject(2).getString(JSON_SITE_BOTTOM_LEFT)));
            }
            catch(Exception e){
                
            }
        }
        else{
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            siteDataManager.setLeftUrl(props.getProperty(APP_PATH_IMAGES) + props.getProperty(DEFAULT_LFIMG_TEXT));
            workspace.getLeftImgView().setImage(new Image(props.getProperty(APP_FILE_PROTOCOL) + props.getProperty(APP_PATH_IMAGES) + props.getProperty(DEFAULT_LFIMG_TEXT)));
        }
        if(!jsonLogosArray.getJsonObject(3).getString(JSON_SITE_BOTTOM_RIGHT).equals("")){
            siteDataManager.setRightUrl(jsonLogosArray.getJsonObject(3).getString(JSON_SITE_BOTTOM_RIGHT));
            try{
                workspace.getRightImgView().setImage(new Image(jsonLogosArray.getJsonObject(3).getString(JSON_SITE_BOTTOM_RIGHT)));
            }
            catch(Exception e){
                
            }
        }
        else{
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            siteDataManager.setRightUrl(props.getProperty(APP_PATH_IMAGES) + props.getProperty(DEFAULT_RFIMG_TEXT));
            workspace.getRightImgView().setImage(new Image(props.getProperty(APP_FILE_PROTOCOL) + props.getProperty(APP_PATH_IMAGES) + props.getProperty(DEFAULT_RFIMG_TEXT)));
        }
        
        String courseCSS = json.getString(JSON_SITE_SELECTED_CSS);
        if(!courseCSS.trim().equals("")){
            ((ComboBox)gui.getGUINode(SITE_CSS_COMBO_BOX)).getSelectionModel().select(courseCSS.substring(courseCSS.lastIndexOf("/") + 1));
            siteDataManager.setCSS(courseCSS.substring(courseCSS.lastIndexOf("/") + 1));
        }
        
        JsonArray jsonInstructorArray = json.getJsonArray(JSON_SITE_INSTRUCTOR);
        String instName = jsonInstructorArray.getJsonObject(0).getString(JSON_SITE_INSTRUCTOR_NAME);
        siteDataManager.setInstructorName(instName);
        ((TextField)gui.getGUINode(SITE_NAME_TEXT_FIELD)).setText(instName);
        String instEmail = jsonInstructorArray.getJsonObject(1).getString(JSON_SITE_INSTRUCTOR_EMAIL);
        siteDataManager.setInstructorEmail(instEmail);
        ((TextField)gui.getGUINode(SITE_EMAIL_TEXT_FIELD)).setText(instEmail);
        String instRoom = jsonInstructorArray.getJsonObject(2).getString(JSON_SITE_INSTRUCTOR_ROOM);
        siteDataManager.setInstructorRoom(instRoom);
        ((TextField)gui.getGUINode(SITE_ROOM_TEXT_FIELD)).setText(instRoom);
        String instHP = jsonInstructorArray.getJsonObject(3).getString(JSON_SITE_INSTRUCTOR_HP);
        siteDataManager.setInstructorHP(instHP);
        ((TextField)gui.getGUINode(SITE_HP_TEXT_FIELD)).setText(instHP);
        try{
            siteDataManager.setInstructorHoursJSON(jsonInstructorArray.getJsonObject(4).getString(JSON_SITE_INSTRUCTOR_HOURS));
        }
        catch(Exception e){
            
        }
        
        /**********************************************************/
        
        
        /******************LOADS THE SYLLABUS DATA*****************/
        String desc = json.getString(JSON_SYL_DESCRIPTION);
        if(!desc.trim().equals("")){
            syllabusDataManager.setDescriptionJSON(desc);
            workspace.getDescTA().setText(desc);
        }
        
        String topics = json.getJsonArray(JSON_SYL_TOPICS).getString(0);
        if(!topics.trim().equals("")){
            syllabusDataManager.setTopicsJSON(topics);
            workspace.getTopicTA().setText(topics);
        }
        
        String pr = json.getString(JSON_SYL_PREREQUISITES);
        if(!pr.trim().equals("")){
            syllabusDataManager.setPrereqJSON(pr);
            workspace.getPrereqTA().setText(pr);
        }
        
        String outcomes = json.getJsonArray(JSON_SYL_OUTCOMES).getString(0);
        if(!outcomes.trim().equals("")){
            syllabusDataManager.setOutcomesJSON(outcomes);
            workspace.getOutcomesTA().setText(outcomes);
        }
        
        String tb = json.getJsonArray(JSON_SYL_TEXTBOOKS).getString(0);
        if(!tb.trim().equals("")){
            syllabusDataManager.setTextbooksJSON(tb);
            workspace.getTextbooksTA().setText(tb);
        }
        
        String gc = json.getJsonArray(JSON_SYL_GC).getString(0);
        if(!gc.trim().equals("")){
            syllabusDataManager.setGcJSON(gc);
            workspace.getGcTA().setText(gc);
        }
        
        String gn = json.getString(JSON_SYL_GN);
        if(!gn.trim().equals("")){
            syllabusDataManager.setGnJSON(gn);
            workspace.getGradingNoteTA().setText(gn);
        }
        
        String ad = json.getString(JSON_SYL_AD);
        if(!ad.trim().equals("")){
            syllabusDataManager.setAdJSON(ad);
            workspace.getAdTA().setText(ad);
        }
        
        String sa = json.getString(JSON_SYL_SA);
        if(!sa.trim().equals("")){
            syllabusDataManager.setSaJSON(sa);
            workspace.getSaTA().setText(sa);
        }
        /**********************************************************/
        
        
        /**************LOADS THE MEETING TIMES DATA****************/
        mtDataManager.reset();
        loadLectures(mtDataManager, json, JSON_MT_LECTURES);
        loadRecitations(mtDataManager, json, JSON_MT_RECITATIONS);
        loadLabs(mtDataManager, json, JSON_MT_LABS);
        /**********************************************************/
        
        
        /*******************LOADS THE OH DATA**********************/
	// LOAD THE START AND END HOURS
        ohDataManager.reset();
	String startHour = json.getString(JSON_OH_START_HOUR);
        String endHour = json.getString(JSON_OH_END_HOUR);
        ohDataManager.resetOfficeHours(Integer.parseInt(startHour), Integer.parseInt(endHour));
        
        // LOAD ALL THE GRAD TAs
        loadTAs(ohDataManager, json, JSON_OH_GRAD_TAS, TAType.Graduate);
        loadTAs(ohDataManager, json, JSON_OH_UNDERGRAD_TAS, TAType.Undergraduate);

        // AND THEN ALL THE OFFICE HOURS
        JsonArray jsonOfficeHoursArray = json.getJsonArray(JSON_OH_OFFICE_HOURS);
        for (int i = 0; i < jsonOfficeHoursArray.size(); i++) {
            JsonObject jsonOfficeHours = jsonOfficeHoursArray.getJsonObject(i);
            String startTime = jsonOfficeHours.getString(JSON_OH_START_TIME);
            DayOfWeek dow = DayOfWeek.valueOf(jsonOfficeHours.getString(JSON_OH_DAY_OF_WEEK));
            String name = jsonOfficeHours.getString(JSON_OH_NAME);
            TeachingAssistantPrototype ta = ohDataManager.getTAWithName(name);
            TimeSlot timeSlot = ohDataManager.getTimeSlot(startTime);
            try{
            timeSlot.toggleTA(dow, ta);
            }
            catch(Exception e){
                
            }
        }
        
        /**********************************************************/
        
        
        /****************LOADS THE SCHEDULE DATA*******************/
        schDataManager.reset();
        String startDate = json.getString(JSON_SCH_START_DATE);
        String endDate = json.getString(JSON_SCH_END_DATE);
        if(!startDate.trim().equals("")){
            LocalDate start = LocalDate.parse(startDate);
            schDataManager.setStartDate(start);
            workspace.getStartDate().setValue(start);
        }
        if(!endDate.trim().equals("")){
            LocalDate end = LocalDate.parse(endDate);
            schDataManager.setEndDate(end);
            workspace.getEndDate().setValue(end);
        }
//                Integer.parseInt(json.getString(JSON_SCH_STARTING_MONDAY_MONTH)), 
//                Integer.parseInt(json.getString(JSON_SCH_STARTING_MONDAY_DAY)), 
//                Integer.parseInt(json.getString(JSON_SCH_STARTING_MONDAY_YEAR)));
        loadScheduleHolidays(schDataManager, json, JSON_SCH_HOLIDAYS);
        loadScheduleHWs(schDataManager, json, JSON_SCH_HWS);
        loadScheduleRefs(schDataManager, json, JSON_SCH_REFERENCES);
        loadScheduleLectures(schDataManager, json, JSON_SCH_LECTURES);
        loadScheduleRecitations(schDataManager, json, JSON_SCH_RECITATIONS);
        loadScheduleLabs(schDataManager, json, JSON_SCH_LABS);
        
        
        /**********LOADS THE COMBOBOX DATA IN THE SITE TAB*********/
        
        /**********************************************************/
        
        
        
        /**********************************************************/
//        }
//        catch(NullPointerException e){
//            Alert alert = new Alert(Alert.AlertType.ERROR, "You have selected a invalid/corrupt file, please select another work file", ButtonType.OK);
//            alert.showAndWait();
//
//            if (alert.getResult() == ButtonType.OK) {
//                alert.close();
//            }
//        }
    }
    
    private void loadScheduleHolidays(ScheduleData data, JsonObject json, String h) {
        JsonArray jsonLecArray = json.getJsonArray(h);
        for (int i = 0; i < jsonLecArray.size(); i++) {
            JsonObject jsonTA = jsonLecArray.getJsonObject(i);
            String d = jsonTA.getString(JSON_SCH_EVENT_DATE);
            LocalDate date = LocalDate.parse(d);
            String title = jsonTA.getString(JSON_SCH_EVENT_TITLE);
            String topic = jsonTA.getString(JSON_SCH_EVENT_TOPIC);
            String link = jsonTA.getString(JSON_SCH_EVENT_LINK);
            ScheduleItemPrototype s = new ScheduleItemPrototype("Holiday", date, topic, title, link);
            data.addScheduleItem(s);
        } 
    }

    private void loadScheduleHWs(ScheduleData data, JsonObject json, String h) {
        JsonArray jsonLecArray = json.getJsonArray(h);
        for (int i = 0; i < jsonLecArray.size(); i++) {
            JsonObject jsonTA = jsonLecArray.getJsonObject(i);
            String d = jsonTA.getString(JSON_SCH_EVENT_DATE);
            LocalDate date = LocalDate.parse(d);
            String title = jsonTA.getString(JSON_SCH_EVENT_TITLE);
            String topic = jsonTA.getString(JSON_SCH_EVENT_TOPIC);
            String link = jsonTA.getString(JSON_SCH_EVENT_LINK);
            ScheduleItemPrototype s = new ScheduleItemPrototype("HW", date, topic, title, link);
            data.addScheduleItem(s);
        } 
    }

    private void loadScheduleRefs(ScheduleData data, JsonObject json, String r) {
        JsonArray jsonLecArray = json.getJsonArray(r);
        for (int i = 0; i < jsonLecArray.size(); i++) {
            JsonObject jsonTA = jsonLecArray.getJsonObject(i);
            String d = jsonTA.getString(JSON_SCH_EVENT_DATE);
            LocalDate date = LocalDate.parse(d);
            String title = jsonTA.getString(JSON_SCH_EVENT_TITLE);
            String topic = jsonTA.getString(JSON_SCH_EVENT_TOPIC);
            String link = jsonTA.getString(JSON_SCH_EVENT_LINK);
            ScheduleItemPrototype s = new ScheduleItemPrototype("Reference", date, topic, title, link);
            data.addScheduleItem(s);
        } 
    }

    private void loadScheduleLectures(ScheduleData data, JsonObject json, String l) {
        JsonArray jsonLecArray = json.getJsonArray(l);
        for (int i = 0; i < jsonLecArray.size(); i++) {
            JsonObject jsonTA = jsonLecArray.getJsonObject(i);
            String d = jsonTA.getString(JSON_SCH_EVENT_DATE);
            LocalDate date = LocalDate.parse(d);
            String title = jsonTA.getString(JSON_SCH_EVENT_TITLE);
            String topic = jsonTA.getString(JSON_SCH_EVENT_TOPIC);
            String link = jsonTA.getString(JSON_SCH_EVENT_LINK);
            ScheduleItemPrototype s = new ScheduleItemPrototype("Lecture", date, topic, title, link);
            data.addScheduleItem(s);
        } 
    }

    private void loadScheduleRecitations(ScheduleData data, JsonObject json, String r) {
        JsonArray jsonLecArray = json.getJsonArray(r);
        for (int i = 0; i < jsonLecArray.size(); i++) {
            JsonObject jsonTA = jsonLecArray.getJsonObject(i);
            String d = jsonTA.getString(JSON_SCH_EVENT_DATE);
            LocalDate date = LocalDate.parse(d);
            String title = jsonTA.getString(JSON_SCH_EVENT_TITLE);
            String topic = jsonTA.getString(JSON_SCH_EVENT_TOPIC);
            String link = jsonTA.getString(JSON_SCH_EVENT_LINK);
            ScheduleItemPrototype s = new ScheduleItemPrototype("Recitation", date, topic, title, link);
            data.addScheduleItem(s);
        } 
    }

    private void loadScheduleLabs(ScheduleData data, JsonObject json, String l) {
        JsonArray jsonLecArray = json.getJsonArray(l);
        for (int i = 0; i < jsonLecArray.size(); i++) {
            JsonObject jsonTA = jsonLecArray.getJsonObject(i);
            String d = jsonTA.getString(JSON_SCH_EVENT_DATE);
            LocalDate date = LocalDate.parse(d);
            String title = jsonTA.getString(JSON_SCH_EVENT_TITLE);
            String topic = jsonTA.getString(JSON_SCH_EVENT_TOPIC);
            String link = jsonTA.getString(JSON_SCH_EVENT_LINK);
            ScheduleItemPrototype s = new ScheduleItemPrototype("Lab", date, topic, title, link);
            data.addScheduleItem(s);
        } 
    }
    
    private void loadLectures(MeetingTimesData data, JsonObject json, String lecs) {
        JsonArray jsonLecArray = json.getJsonArray(lecs);
        for (int i = 0; i < jsonLecArray.size(); i++) {
            JsonObject jsonTA = jsonLecArray.getJsonObject(i);
            String section = jsonTA.getString(JSON_MT_LECTURES_SECTION);
            String day = jsonTA.getString(JSON_MT_LECTURES_DAYS);
            String time = jsonTA.getString(JSON_MT_LECTURES_TIME);
            String room = jsonTA.getString(JSON_MT_LECTURES_ROOM);
            LecturePrototype l = new LecturePrototype(section, day, time, room);
            data.addLecture(l);
        } 
    }
    
    private void loadRecitations(MeetingTimesData data, JsonObject json, String recs) {
        JsonArray jsonRecArray = json.getJsonArray(recs);
        for (int i = 0; i < jsonRecArray.size(); i++) {
            JsonObject jsonTA = jsonRecArray.getJsonObject(i);
            String section = jsonTA.getString(JSON_MT_RECITATIONS_SECTION);
            String dayTime = jsonTA.getString(JSON_MT_RECITATIONS_DAY_TIME);
            String room = jsonTA.getString(JSON_MT_RECITATIONS_LOCATION);
            String ta1 = jsonTA.getString(JSON_MT_RECITATIONS_TA1);
            String ta2 = jsonTA.getString(JSON_MT_RECITATIONS_TA2);
            RecitationPrototype r = new RecitationPrototype(section, dayTime, room, ta1, ta2);
            data.addRecitation(r);
        } 
    }
    
    private void loadLabs(MeetingTimesData data, JsonObject json, String labs) {
        JsonArray jsonLabsArray = json.getJsonArray(labs);
        for (int i = 0; i < jsonLabsArray.size(); i++) {
            JsonObject jsonTA = jsonLabsArray.getJsonObject(i);
            String section = jsonTA.getString(JSON_MT_LABS_SECTION);
            String dayTime = jsonTA.getString(JSON_MT_LABS_DAY_TIME);
            String room = jsonTA.getString(JSON_MT_LABS_LOCATION);
            String ta1 = jsonTA.getString(JSON_MT_LABS_TA1);
            String ta2 = jsonTA.getString(JSON_MT_LABS_TA2);
            LabPrototype l = new LabPrototype(section, dayTime, room, ta1, ta2);
            data.addLab(l);
        } 
    }
    
    private void loadTAs(OHData data, JsonObject json, String tas, TAType type) {
        JsonArray jsonTAArray = json.getJsonArray(tas);
        for (int i = 0; i < jsonTAArray.size(); i++) {
            JsonObject jsonTA = jsonTAArray.getJsonObject(i);
            String name = jsonTA.getString(JSON_OH_NAME);
            String email = jsonTA.getString(JSON_OH_EMAIL);
            //TAType type = TAType.valueOf(jsonTA.getString(JSON_OH_TYPE));
            TeachingAssistantPrototype ta = new TeachingAssistantPrototype(name, email, type);
            data.addTA(ta);
        }     
    }
      
    // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
	InputStream is = new FileInputStream(jsonFilePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
	return json;
    }
    private JsonObject loadJSONFileForCB(String jsonFilePath) throws IOException {
	InputStream is = new FileInputStream(jsonFilePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
	return json;
    }

    @Override
    public void saveData(AppDataComponent data, String filePath) throws IOException {
	// GET THE DATA
	CSGData d = (CSGData)data;
        CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
        AppGUIModule gui = app.getGUIModule();
        SiteData siteDataManager = d.getSiteData();
        SyllabusData syllabusDataManager = d.getSyllabusData();
        MeetingTimesData mtDataManager = d.getMeetingTimesData();
        OHData ohDataManager = d.getOfficeHoursData();
        ScheduleData schDataManager = d.getScheduleData();
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        /*****************SAVES THE SITE DATA*******************/
        JsonArrayBuilder pagesArrayBuilder = Json.createArrayBuilder();
        for(int i = 0; i < siteDataManager.getSelectedPageOptions().size(); i++){
            if(i == 0){
                if(siteDataManager.getSelectedPageOptions().get(i).equals("home")){
                JsonObject cbOption = Json.createObjectBuilder()
                        .add(JSON_SITE_PAGES_NAME, siteDataManager.getSelectedPageOptions().get(i))
                        .add(JSON_SITE_PAGES_LINK, "index.html").build();
                    pagesArrayBuilder.add(cbOption);
                }
                else if(siteDataManager.getSelectedPageOptions().get(i).equals("syllabus")){
                    JsonObject cbOption = Json.createObjectBuilder()
                            .add(JSON_SITE_PAGES_NAME, siteDataManager.getSelectedPageOptions().get(i))
                            .add(JSON_SITE_PAGES_LINK, "index.html").build();
                    pagesArrayBuilder.add(cbOption);
                }
                else if(siteDataManager.getSelectedPageOptions().get(i).equals("schedule")){
                    JsonObject cbOption = Json.createObjectBuilder()
                            .add(JSON_SITE_PAGES_NAME, siteDataManager.getSelectedPageOptions().get(i))
                            .add(JSON_SITE_PAGES_LINK, "index.html").build();
                    pagesArrayBuilder.add(cbOption);
                }
                else if(siteDataManager.getSelectedPageOptions().get(i).equals("hw")){
                    JsonObject cbOption = Json.createObjectBuilder()
                            .add(JSON_SITE_PAGES_NAME, siteDataManager.getSelectedPageOptions().get(i))
                            .add(JSON_SITE_PAGES_LINK, "index.html").build();
                    pagesArrayBuilder.add(cbOption);
                }
            }
            else{
                if(siteDataManager.getSelectedPageOptions().get(i).equals("home")){
                JsonObject cbOption = Json.createObjectBuilder()
                        .add(JSON_SITE_PAGES_NAME, siteDataManager.getSelectedPageOptions().get(i))
                        .add(JSON_SITE_PAGES_LINK, siteDataManager.getSelectedPageOptions().get(i) + ".html").build();
                    pagesArrayBuilder.add(cbOption);
                }
                else if(siteDataManager.getSelectedPageOptions().get(i).equals("syllabus")){
                    JsonObject cbOption = Json.createObjectBuilder()
                            .add(JSON_SITE_PAGES_NAME, siteDataManager.getSelectedPageOptions().get(i))
                            .add(JSON_SITE_PAGES_LINK, siteDataManager.getSelectedPageOptions().get(i) + ".html").build();
                    pagesArrayBuilder.add(cbOption);
                }
                else if(siteDataManager.getSelectedPageOptions().get(i).equals("schedule")){
                    JsonObject cbOption = Json.createObjectBuilder()
                            .add(JSON_SITE_PAGES_NAME, siteDataManager.getSelectedPageOptions().get(i))
                            .add(JSON_SITE_PAGES_LINK, siteDataManager.getSelectedPageOptions().get(i) + ".html").build();
                    pagesArrayBuilder.add(cbOption);
                }
                else if(siteDataManager.getSelectedPageOptions().get(i).equals("hw")){
                    JsonObject cbOption = Json.createObjectBuilder()
                            .add(JSON_SITE_PAGES_NAME, siteDataManager.getSelectedPageOptions().get(i))
                            .add(JSON_SITE_PAGES_LINK, siteDataManager.getSelectedPageOptions().get(i) + ".html").build();
                    pagesArrayBuilder.add(cbOption);
                }
            }
            
        }
        JsonArray pagesArray = pagesArrayBuilder.build();
        
        JsonArrayBuilder logos = Json.createArrayBuilder();
        if(!siteDataManager.getFavUrl().trim().equals("")){
            JsonObject favUrl = Json.createObjectBuilder().add(JSON_SITE_FAVICON, siteDataManager.getFavUrl()).build();
            logos.add(favUrl);
        }
        else{
            siteDataManager.setFavUrl(props.getProperty(APP_PATH_IMAGES) + props.getProperty(DEFAULT_FAVICON_TEXT));
            JsonObject favUrl = Json.createObjectBuilder().add(JSON_SITE_FAVICON, props.getProperty(APP_PATH_IMAGES) + props.getProperty(DEFAULT_FAVICON_TEXT)).build();
            logos.add(favUrl);
        }
        if(!siteDataManager.getNavUrl().trim().equals("")){
            JsonObject navUrl = Json.createObjectBuilder().add(JSON_SITE_NAVBAR, siteDataManager.getNavUrl()).build();
            logos.add(navUrl);
        }
        else{
            siteDataManager.setNavUrl(props.getProperty(APP_PATH_IMAGES) + props.getProperty(DEFAULT_NAVBAR_TEXT));
            JsonObject navUrl = Json.createObjectBuilder().add(JSON_SITE_NAVBAR, props.getProperty(APP_PATH_IMAGES) + props.getProperty(DEFAULT_NAVBAR_TEXT)).build();
            logos.add(navUrl);
        }
        if(!siteDataManager.getLeftUrl().trim().equals("")){
            JsonObject leftUrl = Json.createObjectBuilder().add(JSON_SITE_BOTTOM_LEFT, siteDataManager.getLeftUrl()).build();
            logos.add(leftUrl);
        }
        else{
            siteDataManager.setLeftUrl(props.getProperty(APP_PATH_IMAGES) + props.getProperty(DEFAULT_LFIMG_TEXT));
            JsonObject leftUrl = Json.createObjectBuilder().add(JSON_SITE_BOTTOM_LEFT, props.getProperty(APP_PATH_IMAGES) + props.getProperty(DEFAULT_LFIMG_TEXT)).build();
            logos.add(leftUrl);
        }
        if(!siteDataManager.getRightUrl().trim().equals("")){
            JsonObject rightUrl = Json.createObjectBuilder().add(JSON_SITE_BOTTOM_RIGHT, siteDataManager.getRightUrl()).build();
            logos.add(rightUrl);
        }
        else{
            siteDataManager.setRightUrl(props.getProperty(APP_PATH_IMAGES) + props.getProperty(DEFAULT_RFIMG_TEXT));
            JsonObject rightUrl = Json.createObjectBuilder().add(JSON_SITE_BOTTOM_RIGHT, props.getProperty(APP_PATH_IMAGES) + props.getProperty(DEFAULT_RFIMG_TEXT)).build();
            logos.add(rightUrl);
        }
        JsonArray logosArray = logos.build();
        
        JsonArrayBuilder inst = Json.createArrayBuilder();
        JsonObject instName = Json.createObjectBuilder().add(JSON_SITE_INSTRUCTOR_NAME, siteDataManager.getInstructorName()).build();
        inst.add(instName);
        JsonObject instEmail = Json.createObjectBuilder().add(JSON_SITE_INSTRUCTOR_EMAIL, siteDataManager.getInstructorEmail()).build();
        inst.add(instEmail);
        JsonObject instRoom = Json.createObjectBuilder().add(JSON_SITE_INSTRUCTOR_ROOM, siteDataManager.getInstructorRoom()).build();
        inst.add(instRoom);
        JsonObject instHP = Json.createObjectBuilder().add(JSON_SITE_INSTRUCTOR_HP, siteDataManager.getInstructorHP()).build();
        inst.add(instHP);
        if(siteDataManager.getInstructorHoursJSON().trim().equals("")){
            String hoursJson = ((TextArea)workspace.getInstructorOHJsonArea()).getText();
            JsonArrayBuilder instHours = Json.createArrayBuilder().add(hoursJson);
            inst.add(instHours);
        }
        else{
            JsonArrayBuilder instHours = Json.createArrayBuilder().add(siteDataManager.getInstructorHoursJSON());
            inst.add(instHours);    
        }
        JsonArray instructorArray = inst.build();
        /*******************************************************/
        
        
        
        /*****************SAVES THE SYLLABUS DATA*******************/
        JsonArrayBuilder topicsArrayBuilder = Json.createArrayBuilder();
        if(syllabusDataManager.getTopicsJSON().trim().equals("")){
            topicsArrayBuilder.add(((String)workspace.getTopicTA().getText()));
            syllabusDataManager.setTopicsJSON(((String)workspace.getTopicTA().getText()));
        }
        else{
            topicsArrayBuilder.add(syllabusDataManager.getTopicsJSON());
        }
        JsonArray topicsArray = topicsArrayBuilder.build();
        
        JsonArrayBuilder outcomesArrayBuilder = Json.createArrayBuilder();
        if(syllabusDataManager.getOutcomesJSON().trim().equals("")){
            outcomesArrayBuilder.add(((String)workspace.getOutcomesTA().getText()));
            syllabusDataManager.setOutcomesJSON(((String)workspace.getOutcomesTA().getText()));
        }
        else{
            outcomesArrayBuilder.add(syllabusDataManager.getOutcomesJSON());
        }
        JsonArray outcomesArray = outcomesArrayBuilder.build();
        
        JsonArrayBuilder textbooksArrayBuilder = Json.createArrayBuilder();
        if(syllabusDataManager.getTextbooksJSON().trim().equals("")){
            textbooksArrayBuilder.add(((String)workspace.getTextbooksTA().getText()));
            syllabusDataManager.setTextbooksJSON(((String)workspace.getTextbooksTA().getText()));
        }
        else{
            textbooksArrayBuilder.add(syllabusDataManager.getTextbooksJSON());
        }
        JsonArray textbooksArray = textbooksArrayBuilder.build();
        
        JsonArrayBuilder gcArrayBuilder = Json.createArrayBuilder();
        if(syllabusDataManager.getGcJSON().trim().equals("")){
            gcArrayBuilder.add(((String)workspace.getGcTA().getText()));
            syllabusDataManager.setGcJSON(((String)workspace.getGcTA().getText()));
        }
        else{
            gcArrayBuilder.add(syllabusDataManager.getGcJSON());
        }
        JsonArray gcArray = gcArrayBuilder.build();
        /***********************************************************/
        
        
        
        /**************SAVES THE MEETING TIMES DATA*************/
        JsonArrayBuilder lecArrayBuilder = Json.createArrayBuilder();
        Iterator<LecturePrototype> lecIterator = mtDataManager.lectureIterator();
        while(lecIterator.hasNext()){
            LecturePrototype l = lecIterator.next();
            JsonObject lecJson = Json.createObjectBuilder()
                    .add(JSON_MT_LECTURES_SECTION, l.getSection())
                    .add(JSON_MT_LECTURES_DAYS, l.getDay())
                    .add(JSON_MT_LECTURES_TIME, l.getRoom())
                    .add(JSON_MT_LECTURES_ROOM, l.getRoom()).build();
            lecArrayBuilder.add(lecJson);
        }
        JsonArray lectureArray = lecArrayBuilder.build();
        
        JsonArrayBuilder recArrayBuilder = Json.createArrayBuilder();
        Iterator<RecitationPrototype> recIterator = mtDataManager.recitationIterator();
        while(recIterator.hasNext()){
            RecitationPrototype l = recIterator.next();
            JsonObject lecJson = Json.createObjectBuilder()
                    .add(JSON_MT_RECITATIONS_SECTION, l.getSection())
                    .add(JSON_MT_RECITATIONS_DAY_TIME, l.getRoom())
                    .add(JSON_MT_RECITATIONS_LOCATION, l.getRoom())
                    .add(JSON_MT_RECITATIONS_TA1, l.getTA1())
                    .add(JSON_MT_RECITATIONS_TA2, l.getTA2()).build();
            recArrayBuilder.add(lecJson);
        }
        JsonArray recitationArray = recArrayBuilder.build();
        
        JsonArrayBuilder labsArrayBuilder = Json.createArrayBuilder();
        Iterator<LabPrototype> labsIterator = mtDataManager.labIterator();
        while(labsIterator.hasNext()){
            LabPrototype l = labsIterator.next();
            JsonObject lecJson = Json.createObjectBuilder()
                    .add(JSON_MT_LABS_SECTION, l.getSection())
                    .add(JSON_MT_LABS_DAY_TIME, l.getRoom())
                    .add(JSON_MT_LABS_LOCATION, l.getRoom())
                    .add(JSON_MT_LABS_TA1, l.getTA1())
                    .add(JSON_MT_LABS_TA2, l.getTA2()).build();
            labsArrayBuilder.add(lecJson);
        }
        JsonArray labsArray = labsArrayBuilder.build();
        /*******************************************************/
        
        

        /******************SAVES THE OH DATA********************/
	// NOW BUILD THE TA JSON OBJCTS TO SAVE
	JsonArrayBuilder gradTAsArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder undergradTAsArrayBuilder = Json.createArrayBuilder();
	Iterator<TeachingAssistantPrototype> tasIterator = ohDataManager.teachingAssistantsIterator();
        while (tasIterator.hasNext()) {
            TeachingAssistantPrototype ta = tasIterator.next();
	    JsonObject taJson = Json.createObjectBuilder()
		    .add(JSON_OH_NAME, ta.getName())
		    .add(JSON_OH_EMAIL, ta.getEmail())
                    .add(JSON_OH_TYPE, ta.getType().toString()).build();
            if (ta.getType().equals(TAType.Graduate.toString()))
                gradTAsArrayBuilder.add(taJson);
            else
                undergradTAsArrayBuilder.add(taJson);
	}
        JsonArray gradTAsArray = gradTAsArrayBuilder.build();
	JsonArray undergradTAsArray = undergradTAsArrayBuilder.build();

	// NOW BUILD THE OFFICE HOURS JSON OBJCTS TO SAVE
	JsonArrayBuilder officeHoursArrayBuilder = Json.createArrayBuilder();
        Iterator<TimeSlot> timeSlotsIterator = ohDataManager.officeHoursIterator();
        while (timeSlotsIterator.hasNext()) {
            TimeSlot timeSlot = timeSlotsIterator.next();
            for (int i = 0; i < DayOfWeek.values().length; i++) {
                DayOfWeek dow = DayOfWeek.values()[i];
                tasIterator = timeSlot.getTAsIterator(dow);
                while (tasIterator.hasNext()) {
                    TeachingAssistantPrototype ta = tasIterator.next();
                    JsonObject tsJson = Json.createObjectBuilder()
                        .add(JSON_OH_START_TIME, timeSlot.getStartTime().replace(":", "_"))
                        .add(JSON_OH_DAY_OF_WEEK, dow.toString())
                        .add(JSON_OH_NAME, ta.getName()).build();
                    officeHoursArrayBuilder.add(tsJson);
                }
            }    
	}
        for (TimeSlot ts : ohDataManager.getHeldTs()) {
            for (int i = 0; i < DayOfWeek.values().length; i++) {
                DayOfWeek dow = DayOfWeek.values()[i];
                tasIterator = ts.getTAsIterator(dow);
                while (tasIterator.hasNext()) {
                    TeachingAssistantPrototype ta = tasIterator.next();
                    JsonObject tsJson = Json.createObjectBuilder()
                        .add(JSON_OH_START_TIME, ts.getStartTime().replace(":", "_"))
                        .add(JSON_OH_DAY_OF_WEEK, dow.toString())
                        .add(JSON_OH_NAME, ta.getName()).build();
                    officeHoursArrayBuilder.add(tsJson);
                }
            }    
	}
	JsonArray officeHoursArrayDRAFT = officeHoursArrayBuilder.build();
        
        String[] strips = officeHoursArrayDRAFT.toString().replace("[", "").replace("]", "").split(",");
        for(int i = 0; i < strips.length; i++){
//            System.out.println(strips[i]);
        }
        String rstrip = "";
        for(int i = 0; i < strips.length; i++){
            rstrip += strips[i];
        }
        String[] rstrips = rstrip.split("\\{");
        for(int i = 0; i < rstrips.length; i++){
//            System.out.println(rstrips[i]);
        }
        String rs = "";
        for(int i = 0; i < rstrips.length; i++){
            rs += rstrips[i];
        }
        String[] srs = rs.replace("\"\"", "\",\"").replace("\"time\":","").replace("\"day\":", "").replace("\"name\":", "").split("\\}");
        ArrayList<String> tss = new ArrayList<>(Arrays.asList(srs));
        Set<String> set = new LinkedHashSet<>(tss);
        JsonArrayBuilder updateOHBuilder = Json.createArrayBuilder();
        for(String s: set){
//            System.out.println(s);
//            if(!(s.length() == 0)){
            try{
                JsonObject js = Json.createObjectBuilder()
                        .add(JSON_OH_START_TIME, s.substring(0, s.indexOf(",")).replace("\"", ""))
                        .add(JSON_OH_DAY_OF_WEEK, s.substring(s.indexOf(",") + 1, s.lastIndexOf(",")).replace("\"", ""))
                        .add(JSON_OH_NAME, s.substring(s.lastIndexOf(",") + 1).replace("\"", "")).build();
                updateOHBuilder.add(js);
            }
            catch(Exception e){
                
            }

//            }
        }
        JsonArray officeHoursArray = updateOHBuilder.build();

        /*******************************************************/
        
        
        
        /******************SAVES THE SCHEDULE DATA********************/
        String startDate = "";
        String startingMondayMonth = "";
        String startingMondayDay = "";
        String startingMondayYear = "";
        String endDate = "";
        String endingFridayMonth = "";
        String endingFridayDay = "";
        String endingFridayYear = "";
        if(!(schDataManager.getStartDate() == null)){
            startDate = schDataManager.getStartDate().toString();
            startingMondayMonth = Integer.toString(schDataManager.getStartDate().getMonthValue());
            startingMondayDay = Integer.toString(schDataManager.getStartDate().getDayOfMonth());
            startingMondayYear = Integer.toString(schDataManager.getStartDate().getYear());
        }
        else{
            startDate = "";
            startingMondayMonth= "";
            startingMondayDay = "";
            startingMondayYear = "";
        }
        if(!(schDataManager.getEndDate() == null)){
            endDate = schDataManager.getEndDate().toString();
            endingFridayMonth = Integer.toString(schDataManager.getEndDate().getMonthValue());
            endingFridayDay = Integer.toString(schDataManager.getEndDate().getDayOfMonth());
            endingFridayYear = Integer.toString(schDataManager.getEndDate().getYear());
        }
        else{
            endDate = "";
            endingFridayMonth = "";
            endingFridayDay = "";
            endingFridayYear = "";
        }

        JsonArrayBuilder schHolidayArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder schLectureArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder schRefArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder schRecArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder schLabArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder schHwArrayBuilder = Json.createArrayBuilder();
        
        Iterator<ScheduleItemPrototype> schIterator = schDataManager.scheduleIterator();
        while(schIterator.hasNext()){
            ScheduleItemPrototype s = schIterator.next();
            if(s.getType().equals("Holiday")){
                JsonObject schJson = Json.createObjectBuilder()
                        .add(JSON_SCH_EVENT_DATE, s.getLocalDate().toString())
                        .add(JSON_SCH_EVENT_MONTH, Integer.toString(s.getLocalDate().getMonthValue()))
                        .add(JSON_SCH_EVENT_DAY, Integer.toString(s.getLocalDate().getDayOfMonth()))
                        .add(JSON_SCH_EVENT_YEAR, Integer.toString(s.getLocalDate().getYear()))
                        .add(JSON_SCH_EVENT_TITLE, s.getTitle())
                        .add(JSON_SCH_EVENT_TOPIC, s.getTopic())
                        .add(JSON_SCH_EVENT_LINK, s.getLink()).build();
                schHolidayArrayBuilder.add(schJson);               
            }
            else if(s.getType().equals("Lecture")){
                JsonObject schJson = Json.createObjectBuilder()
                        .add(JSON_SCH_EVENT_DATE, s.getLocalDate().toString())
                        .add(JSON_SCH_EVENT_MONTH, Integer.toString(s.getLocalDate().getMonthValue()))
                        .add(JSON_SCH_EVENT_DAY, Integer.toString(s.getLocalDate().getDayOfMonth()))
                        .add(JSON_SCH_EVENT_YEAR, Integer.toString(s.getLocalDate().getYear()))
                        .add(JSON_SCH_EVENT_TITLE, s.getTitle())
                        .add(JSON_SCH_EVENT_TOPIC, s.getTopic())
                        .add(JSON_SCH_EVENT_LINK, s.getLink()).build();
                schLectureArrayBuilder.add(schJson);              
            }
            else if(s.getType().equals("Recitation")){
                JsonObject schJson = Json.createObjectBuilder()
                        .add(JSON_SCH_EVENT_DATE, s.getLocalDate().toString())
                        .add(JSON_SCH_EVENT_MONTH, Integer.toString(s.getLocalDate().getMonthValue()))
                        .add(JSON_SCH_EVENT_DAY, Integer.toString(s.getLocalDate().getDayOfMonth()))
                        .add(JSON_SCH_EVENT_YEAR, Integer.toString(s.getLocalDate().getYear()))
                        .add(JSON_SCH_EVENT_TITLE, s.getTitle())
                        .add(JSON_SCH_EVENT_TOPIC, s.getTopic())
                        .add(JSON_SCH_EVENT_LINK, s.getLink()).build();
                schRecArrayBuilder.add(schJson);                
            }
            else if(s.getType().equals("Lab")){
                JsonObject schJson = Json.createObjectBuilder()
                        .add(JSON_SCH_EVENT_DATE, s.getLocalDate().toString())
                        .add(JSON_SCH_EVENT_MONTH, Integer.toString(s.getLocalDate().getMonthValue()))
                        .add(JSON_SCH_EVENT_DAY, Integer.toString(s.getLocalDate().getDayOfMonth()))
                        .add(JSON_SCH_EVENT_YEAR, Integer.toString(s.getLocalDate().getYear()))
                        .add(JSON_SCH_EVENT_TITLE, s.getTitle())
                        .add(JSON_SCH_EVENT_TOPIC, s.getTopic())
                        .add(JSON_SCH_EVENT_LINK, s.getLink()).build();
                schLabArrayBuilder.add(schJson);                
            }
            else if(s.getType().equals("Reference")){
                JsonObject schJson = Json.createObjectBuilder()
                        .add(JSON_SCH_EVENT_DATE, s.getLocalDate().toString())
                        .add(JSON_SCH_EVENT_MONTH, Integer.toString(s.getLocalDate().getMonthValue()))
                        .add(JSON_SCH_EVENT_DAY, Integer.toString(s.getLocalDate().getDayOfMonth()))
                        .add(JSON_SCH_EVENT_YEAR, Integer.toString(s.getLocalDate().getYear()))
                        .add(JSON_SCH_EVENT_TITLE, s.getTitle())
                        .add(JSON_SCH_EVENT_TOPIC, s.getTopic())
                        .add(JSON_SCH_EVENT_LINK, s.getLink()).build();
                schRefArrayBuilder.add(schJson);                
            }
            else if(s.getType().equals("HW")){
                JsonObject schJson = Json.createObjectBuilder()
                        .add(JSON_SCH_EVENT_DATE, s.getLocalDate().toString())
                        .add(JSON_SCH_EVENT_MONTH, Integer.toString(s.getLocalDate().getMonthValue()))
                        .add(JSON_SCH_EVENT_DAY, Integer.toString(s.getLocalDate().getDayOfMonth()))
                        .add(JSON_SCH_EVENT_YEAR, Integer.toString(s.getLocalDate().getYear()))
                        .add(JSON_SCH_EVENT_TITLE, s.getTitle())
                        .add(JSON_SCH_EVENT_TOPIC, s.getTopic())
                        .add(JSON_SCH_EVENT_LINK, s.getLink()).build();
                schHwArrayBuilder.add(schJson);                
            }
        }
        JsonArray schHolidayArray = schHolidayArrayBuilder.build();
        JsonArray schLectureArray = schLectureArrayBuilder.build();
        JsonArray schRefArray = schRefArrayBuilder.build();
        JsonArray schRecArray = schRecArrayBuilder.build();
        JsonArray schLabArray = schLabArrayBuilder.build();
        JsonArray schHwArray = schHwArrayBuilder.build();
        /*************************************************************/
        
        
        /*****************TO FIX UNCHANGED VALUES IN APP**************/
        
        String siteSubject = "";
        if(siteDataManager.getSelectedName().trim().equals("")){
            siteSubject = (String)((ComboBox)gui.getGUINode(SITE_SUBJECT_COMBO_BOX)).getSelectionModel().getSelectedItem();
            siteDataManager.setSelectedName(siteSubject);
            ((Label)gui.getGUINode(SITE_EXPORT_LABEL)).setText(siteDataManager.prepareExportUrlForSave());
            siteDataManager.prepareExportUrlForSave();
        }
        else{
            siteSubject = siteDataManager.getSelectedName();
            ((Label)gui.getGUINode(SITE_EXPORT_LABEL)).setText(siteDataManager.prepareExportUrlForSave());
            siteDataManager.prepareExportUrlForSave();
        }
        String siteNumber = "";
        if(siteDataManager.getSelectedNum().trim().equals("")){
            siteNumber = (String)((ComboBox)gui.getGUINode(SITE_SUBJECTNUM_COMBO_BOX)).getSelectionModel().getSelectedItem();
            siteDataManager.setSelectedNum(siteNumber);
            ((Label)gui.getGUINode(SITE_EXPORT_LABEL)).setText(siteDataManager.prepareExportUrlForSave());
            siteDataManager.prepareExportUrlForSave();
        }
        else{
            siteNumber = siteDataManager.getSelectedNum();
            ((Label)gui.getGUINode(SITE_EXPORT_LABEL)).setText(siteDataManager.prepareExportUrlForSave());
            siteDataManager.prepareExportUrlForSave();
        }
        String siteSem = "";
        if(siteDataManager.getSelectedSem().trim().equals("")){
            siteSem = (String)((ComboBox)gui.getGUINode(SITE_SEMESTERS_COMBO_BOX)).getSelectionModel().getSelectedItem();
            siteDataManager.setSelectedSem(siteSem);
            ((Label)gui.getGUINode(SITE_EXPORT_LABEL)).setText(siteDataManager.prepareExportUrlForSave());
            siteDataManager.prepareExportUrlForSave();
        }
        else{
            siteSem = siteDataManager.getSelectedSem();
            ((Label)gui.getGUINode(SITE_EXPORT_LABEL)).setText(siteDataManager.prepareExportUrlForSave());
            siteDataManager.prepareExportUrlForSave();
        }
        String siteYr = "";
        if(siteDataManager.getSelectedYear().trim().equals("")){
            siteYr = (String)((ComboBox)gui.getGUINode(SITE_YEARS_COMBO_BOX)).getSelectionModel().getSelectedItem();
            siteDataManager.setSelectedYear(siteYr);
            ((Label)gui.getGUINode(SITE_EXPORT_LABEL)).setText(siteDataManager.prepareExportUrlForSave());
            siteDataManager.prepareExportUrlForSave();
        }
        else{
            siteYr = siteDataManager.getSelectedYear();
            ((Label)gui.getGUINode(SITE_EXPORT_LABEL)).setText(siteDataManager.prepareExportUrlForSave());
            siteDataManager.prepareExportUrlForSave();
        }
        String siteTitle = "";
        if(siteDataManager.getTitle().trim().equals("")){
            siteTitle = (String)((TextField)gui.getGUINode(SITE_TITLE_TEXT_FIELD)).getText();
            siteDataManager.setTitle(siteTitle);
        }
        else{
            siteTitle = siteDataManager.getTitle();
        }
        String siteCSS = "";
        if(siteDataManager.getCSS().trim().equals("")){
            siteCSS = (String)((ComboBox)gui.getGUINode(SITE_CSS_COMBO_BOX)).getSelectionModel().getSelectedItem();
            siteDataManager.setCSS(siteCSS);
        }
        else{
            siteCSS = siteDataManager.getCSS();
        }
        String sylDescription = "";
        if(syllabusDataManager.getDescriptionJSON().trim().equals("")){
            sylDescription = workspace.getDescTA().getText();
            syllabusDataManager.setDescriptionJSON(sylDescription);
        }
        else{
            sylDescription = syllabusDataManager.getDescriptionJSON();
        }
        String sylPR = "";
        if(syllabusDataManager.getPrereqJSON().trim().equals("")){
            sylPR = workspace.getPrereqTA().getText();
            syllabusDataManager.setPrereqJSON(sylPR);
        }
        else{
            sylPR = syllabusDataManager.getPrereqJSON();
        }
        String sylGN = "";
        if(syllabusDataManager.getGnJSON().trim().equals("")){
            sylGN = workspace.getGradingNoteTA().getText();
            syllabusDataManager.setGnJSON(sylGN);
        }
        else{
            sylGN = syllabusDataManager.getGnJSON();
        }
        String sylAD = "";
        if(syllabusDataManager.getAdJSON().trim().equals("")){
            sylAD = workspace.getAdTA().getText();
            syllabusDataManager.setAdJSON(sylAD);
        }
        else{
            sylAD = syllabusDataManager.getAdJSON();
        }
        String sylSA = "";
        if(syllabusDataManager.getSaJSON().trim().equals("")){
            sylSA = workspace.getSaTA().getText();
            syllabusDataManager.setSaJSON(sylSA);
        }
        else{
            sylSA = syllabusDataManager.getSaJSON();
        }
        /*************************************************************/
        
        
        
	// THEN PUT IT ALL TOGETHER IN A JsonObject
	JsonObject dataManagerJSO = Json.createObjectBuilder()
                // Adds the Site info
                .add(JSON_SITE_SUBJECT, siteSubject)
                .add(JSON_SITE_NUMBER, siteNumber)
                .add(JSON_SITE_SEMESTER, siteSem)
                .add(JSON_SITE_YEAR, siteYr)
                .add(JSON_SITE_TITLE, siteTitle)
                .add(JSON_SITE_EXPORT_URL, siteDataManager.getExp())
                .add(JSON_SITE_PAGES, pagesArray)
                .add(JSON_SITE_LOGOS, logosArray)
                .add(JSON_SITE_SELECTED_CSS, siteCSS)
                .add(JSON_SITE_INSTRUCTOR, instructorArray)
                // Adds the Syllabus info
                .add(JSON_SYL_DESCRIPTION, sylDescription)
                .add(JSON_SYL_TOPICS, /*syllabusDataManager.getTopicsJSON()*/topicsArray)
                .add(JSON_SYL_PREREQUISITES, sylPR)
                .add(JSON_SYL_OUTCOMES, /*syllabusDataManager.getOutcomesJSON()*/outcomesArray)
                .add(JSON_SYL_TEXTBOOKS, /*syllabusDataManager.getTextbooksJSON()*/textbooksArray)
                .add(JSON_SYL_GC, /*syllabusDataManager.getGcJSON()*/gcArray)
                .add(JSON_SYL_GN, sylGN)
                .add(JSON_SYL_AD, sylAD)
                .add(JSON_SYL_SA, sylSA)
                // Adds the Meeting Times info
                .add(JSON_MT_LECTURES, lectureArray)
                .add(JSON_MT_RECITATIONS, recitationArray)
                .add(JSON_MT_LABS, labsArray)
                // Adds the OH Info
		.add(JSON_OH_START_HOUR, "" + ohDataManager.getStartHour())
		.add(JSON_OH_END_HOUR, "" + ohDataManager.getEndHour())
                .add(JSON_OH_GRAD_TAS, gradTAsArray)
                .add(JSON_OH_UNDERGRAD_TAS, undergradTAsArray)
                .add(JSON_OH_OFFICE_HOURS, officeHoursArray)
                // Adds the Schedule Info
                .add(JSON_SCH_START_DATE, startDate)
                .add(JSON_SCH_END_DATE, endDate)
                .add(JSON_SCH_STARTING_MONDAY_MONTH, startingMondayMonth)
                .add(JSON_SCH_STARTING_MONDAY_DAY, startingMondayDay)
                .add(JSON_SCH_STARTING_MONDAY_YEAR, startingMondayYear)
                .add(JSON_SCH_ENDING_FRIDAY_MONTH, endingFridayMonth)
                .add(JSON_SCH_ENDING_FRIDAY_DAY, endingFridayDay)
                .add(JSON_SCH_ENDING_FRIDAY_YEAR, endingFridayYear)
                .add(JSON_SCH_HOLIDAYS, schHolidayArray)
                .add(JSON_SCH_LECTURES, schLectureArray)
                .add(JSON_SCH_RECITATIONS, schRecArray)
                .add(JSON_SCH_LABS, schLabArray)
                .add(JSON_SCH_REFERENCES, schRefArray)
                .add(JSON_SCH_HWS, schHwArray)
		.build();
	
	// AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
        
        // NOW SAVE ALL THE COMBOBOXES FOR THE SITE TAB
        ObservableList subjects = ((ComboBox)gui.getGUINode(SITE_SUBJECT_COMBO_BOX)).getItems();
        ObservableList subjectsNums = ((ComboBox)gui.getGUINode(SITE_SUBJECTNUM_COMBO_BOX)).getItems();
        ObservableList years = ((ComboBox)gui.getGUINode(SITE_YEARS_COMBO_BOX)).getItems();
        ObservableList semesters = ((ComboBox)gui.getGUINode(SITE_SEMESTERS_COMBO_BOX)).getItems();
        JsonArrayBuilder siteSubjectsArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder siteSubjectNumsArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder siteSubjectSemArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder siteSubjectYrArrayBuilder = Json.createArrayBuilder();
        for(Object s: subjects){
            if(!((String)s == null)){
                JsonObject op = Json.createObjectBuilder()
                        .add(JSON_SITE_OPTION, (String)s).build();
                siteSubjectsArrayBuilder.add(op);
                
            }
        }
        for(Object s: subjectsNums){
            if(!((String)s == null)){
                JsonObject op = Json.createObjectBuilder()
                        .add(JSON_SITE_OPTION, (String)s).build();
                siteSubjectNumsArrayBuilder.add(op);
                
            }
        }
        for(Object s: semesters){
            if(!((String)s == null)){
                JsonObject op = Json.createObjectBuilder()
                        .add(JSON_SITE_OPTION, (String)s).build();
                siteSubjectSemArrayBuilder.add(op);
                
            }
        }
        for(Object s: years){
            if(!((String)s == null)){
                JsonObject op = Json.createObjectBuilder()
                        .add(JSON_SITE_OPTION, (String)s).build();
                siteSubjectYrArrayBuilder.add(op);
                
            }
        }
        JsonArray subjectsArray = siteSubjectsArrayBuilder.build();
        JsonArray subjectNumsArray = siteSubjectNumsArrayBuilder.build();
        JsonArray semestersArray = siteSubjectSemArrayBuilder.build();
        JsonArray yearsArray = siteSubjectYrArrayBuilder.build();
        JsonObject cbJSO = Json.createObjectBuilder()
                .add(JSON_SITE_SUBJECT, subjectsArray)
                .add(JSON_SITE_NUMBER, subjectNumsArray)
                .add(JSON_SITE_SEMESTER, semestersArray)
                .add(JSON_SITE_YEAR, yearsArray).build();
        Map<String, Object> propertiesCB = new HashMap<>(1);
	propertiesCB.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactoryCB = Json.createWriterFactory(propertiesCB);
	StringWriter swCB = new StringWriter();
	JsonWriter jsonWriterCB = writerFactoryCB.createWriter(swCB);
	jsonWriterCB.writeObject(cbJSO);
	jsonWriterCB.close();

	// INIT THE WRITER
	OutputStream osCB = new FileOutputStream("./work/jsondata/cboptions.json");
	JsonWriter jsonFileWriterCB = Json.createWriter(osCB);
	jsonFileWriterCB.writeObject(cbJSO);
	String prettyPrintedCB = swCB.toString();
	PrintWriter pwCB = new PrintWriter("./work/jsondata/cboptions.json");
	pwCB.write(prettyPrintedCB);
	pwCB.close();
    }
    
    // IMPORTING/EXPORTING DATA IS USED WHEN WE READ/WRITE DATA IN AN
    // ADDITIONAL FORMAT USEFUL FOR ANOTHER PURPOSE, LIKE ANOTHER APPLICATION
    
    /**
     * THIS MUST BE RENAMED TO EXPORT
     * @param data
     * @param filePath
     * @throws IOException 
     */
    @Override
    public void exportData(AppDataComponent data, String filePath) throws IOException {
        CSGData d = (CSGData)data;
        CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
        AppGUIModule gui = app.getGUIModule();
        SiteData siteDataManager = d.getSiteData();
        SyllabusData syllabusDataManager = d.getSyllabusData();
        MeetingTimesData mtDataManager = d.getMeetingTimesData();
        OHData ohDataManager = d.getOfficeHoursData();
        ScheduleData schDataManager = d.getScheduleData();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        String pageDataPath = "./public_html/js/PageData.json";
        String ohDataPath = "./public_html/js/OfficeHoursData.json";
        String mtDataPath = "./public_html/js/SectionsData.json";
        String schDataPath = "./public_html/js/ScheduleData.json";
        String sylDataPath = "./public_html/js/SyllabusData.json";
//        String bannerImageDirectory = filePath + "images/SBUDarkRedShieldLogo.png";
//        String leftFooterDirectory = filePath + "images/CSLogo.png";
//        String rightFooterDirectory = filePath + "images/SBUWhiteShieldLogo.jpg";
        
        /***********************************FORMATS SITE DATA***********************************/
        String siteSubject = "";
        if(siteDataManager.getSelectedName().trim().equals("")){
            siteSubject = (String)((ComboBox)gui.getGUINode(SITE_SUBJECT_COMBO_BOX)).getSelectionModel().getSelectedItem();
            siteDataManager.setSelectedName(siteSubject);
//            ((Label)gui.getGUINode(SITE_EXPORT_LABEL)).setText(siteDataManager.prepareExportUrlForSave());
            siteDataManager.prepareExportUrlForSave();
        }
        else{
            siteSubject = siteDataManager.getSelectedName();
//            ((Label)gui.getGUINode(SITE_EXPORT_LABEL)).setText(siteDataManager.prepareExportUrlForSave());
            siteDataManager.prepareExportUrlForSave();
        }
        String siteNumber = "";
        if(siteDataManager.getSelectedNum().trim().equals("")){
            siteNumber = (String)((ComboBox)gui.getGUINode(SITE_SUBJECTNUM_COMBO_BOX)).getSelectionModel().getSelectedItem();
            siteDataManager.setSelectedNum(siteNumber);
//            ((Label)gui.getGUINode(SITE_EXPORT_LABEL)).setText(siteDataManager.prepareExportUrlForSave());
            siteDataManager.prepareExportUrlForSave();
        }
        else{
            siteNumber = siteDataManager.getSelectedNum();
//            ((Label)gui.getGUINode(SITE_EXPORT_LABEL)).setText(siteDataManager.prepareExportUrlForSave());
            siteDataManager.prepareExportUrlForSave();
        }
        String siteSem = "";
        if(siteDataManager.getSelectedSem().trim().equals("")){
            siteSem = (String)((ComboBox)gui.getGUINode(SITE_SEMESTERS_COMBO_BOX)).getSelectionModel().getSelectedItem();
            siteDataManager.setSelectedSem(siteSem);
//            ((Label)gui.getGUINode(SITE_EXPORT_LABEL)).setText(siteDataManager.prepareExportUrlForSave());
            siteDataManager.prepareExportUrlForSave();
        }
        else{
            siteSem = siteDataManager.getSelectedSem();
//            ((Label)gui.getGUINode(SITE_EXPORT_LABEL)).setText(siteDataManager.prepareExportUrlForSave());
            siteDataManager.prepareExportUrlForSave();
        }
        String siteYr = "";
        if(siteDataManager.getSelectedYear().trim().equals("")){
            siteYr = (String)((ComboBox)gui.getGUINode(SITE_YEARS_COMBO_BOX)).getSelectionModel().getSelectedItem();
            siteDataManager.setSelectedYear(siteYr);
//            ((Label)gui.getGUINode(SITE_EXPORT_LABEL)).setText(siteDataManager.prepareExportUrlForSave());
            siteDataManager.prepareExportUrlForSave();
        }
        else{
            siteYr = siteDataManager.getSelectedYear();
//            ((Label)gui.getGUINode(SITE_EXPORT_LABEL)).setText(siteDataManager.prepareExportUrlForSave());
        }
        siteDataManager.prepareExportUrlForSave();
        
        pageDataPath = siteDataManager.getExp() + pageDataPath.substring(pageDataPath.lastIndexOf("l/") + 1, pageDataPath.lastIndexOf("PageData.json"));
        ohDataPath = siteDataManager.getExp() + ohDataPath.substring(ohDataPath.lastIndexOf("l/") + 1, ohDataPath.lastIndexOf("OfficeHoursData.json"));
        mtDataPath = siteDataManager.getExp() + mtDataPath.substring(mtDataPath.lastIndexOf("l/") + 1, mtDataPath.lastIndexOf("SectionsData.json"));
        schDataPath = siteDataManager.getExp() + schDataPath.substring(schDataPath.lastIndexOf("l/") + 1, schDataPath.lastIndexOf("ScheduleData.json"));
        sylDataPath = siteDataManager.getExp() + sylDataPath.substring(sylDataPath.lastIndexOf("l/") + 1, sylDataPath.lastIndexOf("SyllabusData.json"));
        new File(pageDataPath).mkdirs();
        new File(ohDataPath).mkdirs();
        new File(mtDataPath).mkdirs();
        new File(schDataPath).mkdirs();
        new File(sylDataPath).mkdirs();
        String cssPath = siteDataManager.getExp() + "/css/";
        new File(cssPath).mkdirs();
        String imgPath = siteDataManager.getExp() + "/images/";
        new File(imgPath).mkdirs();
        pageDataPath = siteDataManager.getExp() + "/js/PageData.json";
        ohDataPath = siteDataManager.getExp() + "/js/OfficeHoursData.json";
        mtDataPath = siteDataManager.getExp() + "/js/SectionsData.json";
        schDataPath = siteDataManager.getExp() + "/js/ScheduleData.json";
        sylDataPath = siteDataManager.getExp() + "/js/SyllabusData.json";
        String siteTitle = "";
        if(siteDataManager.getTitle().trim().equals("")){
            siteTitle = (String)((TextField)gui.getGUINode(SITE_TITLE_TEXT_FIELD)).getText();
            siteDataManager.setTitle(siteTitle);
        }
        else{
            siteTitle = siteDataManager.getTitle();
        }
        String siteCSS = "";
        if(siteDataManager.getCSS().trim().equals("")){
            siteCSS = (String)((ComboBox)gui.getGUINode(SITE_CSS_COMBO_BOX)).getSelectionModel().getSelectedItem();
            siteDataManager.setCSS(siteCSS);
        }
        else{
            siteCSS = siteDataManager.getCSS();
        }
        JsonArrayBuilder pagesArrayBuilder = Json.createArrayBuilder();
        for(int i = 0; i < siteDataManager.getSelectedPageOptions().size(); i++){
            if(i == 0){
                if(siteDataManager.getSelectedPageOptions().get(i).equals("home")){
                JsonObject cbOption = Json.createObjectBuilder()
                        .add(JSON_SITE_PAGES_NAME, "H" + siteDataManager.getSelectedPageOptions().get(i).substring(1))
                        .add(JSON_SITE_PAGES_LINK, "index.html").build();
                    pagesArrayBuilder.add(cbOption);
                }
                else if(siteDataManager.getSelectedPageOptions().get(i).equals("syllabus")){
                    JsonObject cbOption = Json.createObjectBuilder()
                            .add(JSON_SITE_PAGES_NAME, "S" + siteDataManager.getSelectedPageOptions().get(i).substring(1))
                            .add(JSON_SITE_PAGES_LINK, "index.html").build();
                    pagesArrayBuilder.add(cbOption);
                }
                else if(siteDataManager.getSelectedPageOptions().get(i).equals("schedule")){
                    JsonObject cbOption = Json.createObjectBuilder()
                            .add(JSON_SITE_PAGES_NAME, "S" + siteDataManager.getSelectedPageOptions().get(i).substring(1))
                            .add(JSON_SITE_PAGES_LINK, "index.html").build();
                    pagesArrayBuilder.add(cbOption);
                }
                else if(siteDataManager.getSelectedPageOptions().get(i).equals("hw")){
                    JsonObject cbOption = Json.createObjectBuilder()
                            .add(JSON_SITE_PAGES_NAME, "HWs")
                            .add(JSON_SITE_PAGES_LINK, "index.html").build();
                    pagesArrayBuilder.add(cbOption);
                }
            }
            else{
                if(siteDataManager.getSelectedPageOptions().get(i).equals("home")){
                JsonObject cbOption = Json.createObjectBuilder()
                        .add(JSON_SITE_PAGES_NAME, "H" + siteDataManager.getSelectedPageOptions().get(i).substring(1))
                        .add(JSON_SITE_PAGES_LINK, siteDataManager.getSelectedPageOptions().get(i) + ".html").build();
                    pagesArrayBuilder.add(cbOption);
                }
                else if(siteDataManager.getSelectedPageOptions().get(i).equals("syllabus")){
                    JsonObject cbOption = Json.createObjectBuilder()
                            .add(JSON_SITE_PAGES_NAME, "S" + siteDataManager.getSelectedPageOptions().get(i).substring(1))
                            .add(JSON_SITE_PAGES_LINK, siteDataManager.getSelectedPageOptions().get(i) + ".html").build();
                    pagesArrayBuilder.add(cbOption);
                }
                else if(siteDataManager.getSelectedPageOptions().get(i).equals("schedule")){
                    JsonObject cbOption = Json.createObjectBuilder()
                            .add(JSON_SITE_PAGES_NAME, "S" + siteDataManager.getSelectedPageOptions().get(i).substring(1))
                            .add(JSON_SITE_PAGES_LINK, siteDataManager.getSelectedPageOptions().get(i) + ".html").build();
                    pagesArrayBuilder.add(cbOption);
                }
                else if(siteDataManager.getSelectedPageOptions().get(i).equals("hw")){
                    JsonObject cbOption = Json.createObjectBuilder()
                            .add(JSON_SITE_PAGES_NAME, "HWs")
                            .add(JSON_SITE_PAGES_LINK, siteDataManager.getSelectedPageOptions().get(i) + "s.html").build();
                    pagesArrayBuilder.add(cbOption);
                }
            }
            
        }
        JsonArray pagesArray = pagesArrayBuilder.build();
        
        JsonObjectBuilder logos = Json.createObjectBuilder();
        if(!siteDataManager.getFavUrl().trim().equals("")){
            JsonObject favUrl = Json.createObjectBuilder()
                    .add(JSON_SITE_HREF, siteDataManager.getFavUrl()).build();
            logos.add(JSON_SITE_FAVICON, favUrl);
//            siteDataManager.prepareExportUrlForSave();
        }
        else{
            siteDataManager.setFavUrl(props.getProperty(APP_PATH_IMAGES) + props.getProperty(DEFAULT_FAVICON_TEXT));
            JsonObject favUrl = Json.createObjectBuilder()
                    .add(JSON_SITE_HREF, props.getProperty(APP_PATH_IMAGES) + props.getProperty(DEFAULT_FAVICON_TEXT)).build();
            logos.add(JSON_SITE_FAVICON, favUrl);
//            siteDataManager.prepareExportUrlForSave();
        }
        if(!siteDataManager.getNavUrl().trim().equals("")){
            JsonObject navUrl = Json.createObjectBuilder()
                    .add(JSON_SITE_HREF, props.getProperty(HREF))
                    .add(JSON_SITE_SRC, siteDataManager.getNavUrl()).build();
            logos.add(JSON_SITE_NAVBAR, navUrl);
//            siteDataManager.prepareExportUrlForSave();
        }
        else{
            siteDataManager.setNavUrl(props.getProperty(APP_PATH_IMAGES) + props.getProperty(DEFAULT_NAVBAR_TEXT));
            JsonObject navUrl = Json.createObjectBuilder()
                    .add(JSON_SITE_HREF, props.getProperty(HREF))
                    .add(JSON_SITE_SRC, props.getProperty(APP_PATH_IMAGES) + props.getProperty(DEFAULT_NAVBAR_TEXT)).build();
            logos.add(JSON_SITE_NAVBAR, navUrl);
//            siteDataManager.prepareExportUrlForSave();
        }
        if(!siteDataManager.getLeftUrl().trim().equals("")){
            JsonObject leftUrl = Json.createObjectBuilder()
                    .add(JSON_SITE_HREF, props.getProperty(HREF))
                    .add(JSON_SITE_SRC, siteDataManager.getLeftUrl()).build();
            logos.add(JSON_SITE_BOTTOM_LEFT, leftUrl);
//            siteDataManager.prepareExportUrlForSave();
        }
        else{
            siteDataManager.setLeftUrl(props.getProperty(APP_PATH_IMAGES) + props.getProperty(DEFAULT_LFIMG_TEXT));
            JsonObject leftUrl = Json.createObjectBuilder()
                    .add(JSON_SITE_HREF, props.getProperty(HREF))
                    .add(JSON_SITE_SRC, props.getProperty(APP_PATH_IMAGES) + props.getProperty(DEFAULT_LFIMG_TEXT)).build();
            logos.add(JSON_SITE_BOTTOM_LEFT, leftUrl);
//            siteDataManager.prepareExportUrlForSave();
        }
        if(!siteDataManager.getRightUrl().trim().equals("")){
            JsonObject rightUrl = Json.createObjectBuilder()
                    .add(JSON_SITE_HREF, props.getProperty(HREF))
                    .add(JSON_SITE_SRC, siteDataManager.getRightUrl()).build();
            logos.add(JSON_SITE_BOTTOM_RIGHT, rightUrl);
//            siteDataManager.prepareExportUrlForSave();
        }
        else{
            siteDataManager.setRightUrl(props.getProperty(APP_PATH_IMAGES) + props.getProperty(DEFAULT_RFIMG_TEXT));
            JsonObject rightUrl = Json.createObjectBuilder()
                    .add(JSON_SITE_HREF, props.getProperty(HREF))
                    .add(JSON_SITE_SRC, props.getProperty(APP_PATH_IMAGES) + props.getProperty(DEFAULT_RFIMG_TEXT)).build();
            logos.add(JSON_SITE_BOTTOM_RIGHT, rightUrl);
//            siteDataManager.prepareExportUrlForSave();
        }
        JsonObject logosArray = logos.build();
        
        JsonObjectBuilder inst = Json.createObjectBuilder();
//        JsonObject instName = Json.createObjectBuilder().add(JSON_SITE_INSTRUCTOR_NAME, siteDataManager.getInstructorName()).build();
        inst.add(JSON_SITE_INSTRUCTOR_NAME, siteDataManager.getInstructorName());
//        JsonObject instHP = Json.createObjectBuilder().add(JSON_SITE_INSTRUCTOR_HP, siteDataManager.getInstructorHP()).build();
        inst.add(JSON_SITE_INSTRUCTOR_HP, siteDataManager.getInstructorHP());
//        JsonObject instEmail = Json.createObjectBuilder().add(JSON_SITE_INSTRUCTOR_EMAIL, siteDataManager.getInstructorEmail()).build();
        inst.add(JSON_SITE_INSTRUCTOR_EMAIL, siteDataManager.getInstructorEmail());
//        JsonObject instRoom = Json.createObjectBuilder().add(JSON_SITE_INSTRUCTOR_ROOM, siteDataManager.getInstructorRoom()).build();
        inst.add(JSON_SITE_INSTRUCTOR_ROOM, siteDataManager.getInstructorRoom());
//        JsonObject instPhoto = Json.createObjectBuilder().add(JSON_SITE_INSTRUCTOR_PHOTO, "").build();
        inst.add(JSON_SITE_INSTRUCTOR_PHOTO, "insert_photo_here");
        if(siteDataManager.getInstructorHoursJSON().trim().equals("")){
            JsonArray ohArray;
            try (JsonReader jsonReader = Json.createReader(new StringReader(((TextArea)workspace.getInstructorOHJsonArea()).getText()))) {
                ohArray = jsonReader.readArray();
            }
//            String hoursJson = ((TextArea)workspace.getInstructorOHJsonArea()).getText();
//            JsonArrayBuilder instHours = Json.createArrayBuilder().add(hoursJson);
            inst.add(JSON_SITE_INSTRUCTOR_HOURS, ohArray);
        }
        else{
            JsonArray ohArray;
            try (JsonReader jsonReader = Json.createReader(new StringReader(siteDataManager.getInstructorHoursJSON()))) {
                ohArray = jsonReader.readArray();
            }
            inst.add(JSON_SITE_INSTRUCTOR_HOURS, ohArray);    
        }
        JsonObject instructorArray = inst.build();
        /*****************************************************************************************************/
        
        
        /***********************************FORMATS SYLLABUS DATA***********************************/
        String sylDescription = "";
        if(syllabusDataManager.getDescriptionJSON().trim().equals("")){
            sylDescription = workspace.getDescTA().getText();
            syllabusDataManager.setDescriptionJSON(sylDescription);
        }
        else{
            sylDescription = syllabusDataManager.getDescriptionJSON();
        }
        String sylPR = "";
        if(syllabusDataManager.getPrereqJSON().trim().equals("")){
            sylPR = workspace.getPrereqTA().getText();
            syllabusDataManager.setPrereqJSON(sylPR);
        }
        else{
            sylPR = syllabusDataManager.getPrereqJSON();
        }
        String sylGN = "";
        if(syllabusDataManager.getGnJSON().trim().equals("")){
            sylGN = workspace.getGradingNoteTA().getText();
            syllabusDataManager.setGnJSON(sylGN);
        }
        else{
            sylGN = syllabusDataManager.getGnJSON();
        }
        String sylAD = "";
        if(syllabusDataManager.getAdJSON().trim().equals("")){
            sylAD = workspace.getAdTA().getText();
            syllabusDataManager.setAdJSON(sylAD);
        }
        else{
            sylAD = syllabusDataManager.getAdJSON();
        }
        String sylSA = "";
        if(syllabusDataManager.getSaJSON().trim().equals("")){
            sylSA = workspace.getSaTA().getText();
            syllabusDataManager.setSaJSON(sylSA);
        }
        else{
            sylSA = syllabusDataManager.getSaJSON();
        }
//        JsonObjectBuilder topicsArrayBuilder = Json.createObjectBuilder();
        JsonArray topicArray;
        if(syllabusDataManager.getTopicsJSON().trim().equals("")){
//            topicsArrayBuilder.add(((String)workspace.getTopicTA().getText()));
            try (JsonReader jsonReader = Json.createReader(new StringReader(((String)workspace.getTopicTA().getText())))) {
                topicArray = jsonReader.readArray();
            }
            syllabusDataManager.setTopicsJSON(((String)workspace.getTopicTA().getText()));
        }
        else{

            try (JsonReader jsonReader = Json.createReader(new StringReader(syllabusDataManager.getTopicsJSON()))) {
                topicArray = jsonReader.readArray();
            }
//            topicsArrayBuilder.add(JSON_SYL_TOPICS, topicArray);
        }
//        JsonObject topicsArray = topicArray.build();
        
//        JsonArrayBuilder outcomesArrayBuilder = Json.createArrayBuilder();
        JsonArray outcomeArray;
        if(syllabusDataManager.getOutcomesJSON().trim().equals("")){
//            outcomesArrayBuilder.add(((String)workspace.getOutcomesTA().getText()));
            try (JsonReader jsonReader = Json.createReader(new StringReader(((String)workspace.getOutcomesTA().getText())))) {
                outcomeArray = jsonReader.readArray();
            }
            syllabusDataManager.setOutcomesJSON(((String)workspace.getOutcomesTA().getText()));
        }
        else{
            try (JsonReader jsonReader = Json.createReader(new StringReader((syllabusDataManager.getOutcomesJSON())))) {
                outcomeArray = jsonReader.readArray();
            }
//            outcomesArrayBuilder.add(syllabusDataManager.getOutcomesJSON());
        }
//        JsonArray outcomesArray = outcomesArrayBuilder.build();
        
//        JsonArrayBuilder textbooksArrayBuilder = Json.createArrayBuilder();
        JsonArray textbookArray;
        if(syllabusDataManager.getTextbooksJSON().trim().equals("")){
//            textbooksArrayBuilder.add(((String)workspace.getTextbooksTA().getText()));
            try (JsonReader jsonReader = Json.createReader(new StringReader(((String)workspace.getTextbooksTA().getText())))) {
                textbookArray = jsonReader.readArray();
            }
            syllabusDataManager.setTextbooksJSON(((String)workspace.getTextbooksTA().getText()));
        }
        else{
//            textbooksArrayBuilder.add(syllabusDataManager.getTextbooksJSON());
            try (JsonReader jsonReader = Json.createReader(new StringReader((syllabusDataManager.getTextbooksJSON())))) {
                textbookArray = jsonReader.readArray();
            }
        }
//        JsonArray textbooksArray = textbooksArrayBuilder.build();
        
//        JsonArrayBuilder gcArrayBuilder = Json.createArrayBuilder();
        JsonArray gcArray;
        if(syllabusDataManager.getGcJSON().trim().equals("")){
//            gcArrayBuilder.add(((String)workspace.getGcTA().getText()));
            try (JsonReader jsonReader = Json.createReader(new StringReader(((String)workspace.getGcTA().getText())))) {
                gcArray = jsonReader.readArray();
            }
            syllabusDataManager.setGcJSON(((String)workspace.getGcTA().getText()));
        }
        else{
//            gcArrayBuilder.add(syllabusDataManager.getGcJSON());
            try (JsonReader jsonReader = Json.createReader(new StringReader((syllabusDataManager.getGcJSON())))) {
                gcArray = jsonReader.readArray();
            }
        }
//        JsonArray gcArray = gcArrayBuilder.build();
        /*****************************************************************************************************/
        
        
        /***********************************FORMATS MEETING TIMES DATA***********************************/
        JsonArrayBuilder lecArrayBuilder = Json.createArrayBuilder();
        Iterator<LecturePrototype> lecIterator = mtDataManager.lectureIterator();
        while(lecIterator.hasNext()){
            LecturePrototype l = lecIterator.next();
            JsonObject lecJson = Json.createObjectBuilder()
                    .add(JSON_MT_LECTURES_SECTION, l.getSection())
                    .add(JSON_MT_LECTURES_DAYS, l.getDay())
                    .add(JSON_MT_LECTURES_TIME, l.getRoom())
                    .add(JSON_MT_LECTURES_ROOM, l.getRoom()).build();
            lecArrayBuilder.add(lecJson);
        }
        JsonArray lectureArray = lecArrayBuilder.build();
        
        JsonArrayBuilder recArrayBuilder = Json.createArrayBuilder();
        Iterator<RecitationPrototype> recIterator = mtDataManager.recitationIterator();
        while(recIterator.hasNext()){
            RecitationPrototype l = recIterator.next();
            JsonObject lecJson = Json.createObjectBuilder()
                    .add(JSON_MT_RECITATIONS_SECTION, l.getSection())
                    .add(JSON_MT_RECITATIONS_DAY_TIME, l.getRoom())
                    .add(JSON_MT_RECITATIONS_LOCATION, l.getRoom())
                    .add(JSON_MT_RECITATIONS_TA1, l.getTA1())
                    .add(JSON_MT_RECITATIONS_TA2, l.getTA2()).build();
            recArrayBuilder.add(lecJson);
        }
        JsonArray recitationArray = recArrayBuilder.build();
        
        JsonArrayBuilder labsArrayBuilder = Json.createArrayBuilder();
        Iterator<LabPrototype> labsIterator = mtDataManager.labIterator();
        while(labsIterator.hasNext()){
            LabPrototype l = labsIterator.next();
            JsonObject lecJson = Json.createObjectBuilder()
                    .add(JSON_MT_LABS_SECTION, l.getSection())
                    .add(JSON_MT_LABS_DAY_TIME, l.getRoom())
                    .add(JSON_MT_LABS_LOCATION, l.getRoom())
                    .add(JSON_MT_LABS_TA1, l.getTA1())
                    .add(JSON_MT_LABS_TA2, l.getTA2()).build();
            labsArrayBuilder.add(lecJson);
        }
        JsonArray labsArray = labsArrayBuilder.build();
        /*****************************************************************************************************/
        
        /***********************************FORMATS OH DATA***********************************/
        JsonArrayBuilder gradTAsArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder undergradTAsArrayBuilder = Json.createArrayBuilder();
	Iterator<TeachingAssistantPrototype> tasIterator = ohDataManager.teachingAssistantsIterator();
        while (tasIterator.hasNext()) {
            TeachingAssistantPrototype ta = tasIterator.next();
	    JsonObject taJson = Json.createObjectBuilder()
		    .add(JSON_OH_NAME, ta.getName())
		    .add(JSON_OH_EMAIL, ta.getEmail()).build();
                    //.add(JSON_OH_TYPE, ta.getType().toString())
            if (ta.getType().equals(TAType.Graduate.toString()))
                gradTAsArrayBuilder.add(taJson);
            else
                undergradTAsArrayBuilder.add(taJson);
	}
        JsonArray gradTAsArray = gradTAsArrayBuilder.build();
	JsonArray undergradTAsArray = undergradTAsArrayBuilder.build();

	// NOW BUILD THE OFFICE HOURS JSON OBJCTS TO SAVE
	JsonArrayBuilder officeHoursArrayBuilder = Json.createArrayBuilder();
        Iterator<TimeSlot> timeSlotsIterator = ohDataManager.officeHoursIterator();
        while (timeSlotsIterator.hasNext()) {
            TimeSlot timeSlot = timeSlotsIterator.next();
            for (int i = 0; i < DayOfWeek.values().length; i++) {
                DayOfWeek dow = DayOfWeek.values()[i];
                tasIterator = timeSlot.getTAsIterator(dow);
                while (tasIterator.hasNext()) {
                    TeachingAssistantPrototype ta = tasIterator.next();
                    JsonObject tsJson = Json.createObjectBuilder()
                        .add(JSON_OH_START_TIME, timeSlot.getStartTime().replace(":", "_"))
                        .add(JSON_OH_DAY_OF_WEEK, dow.toString())
                        .add(JSON_OH_NAME, ta.getName()).build();
                    officeHoursArrayBuilder.add(tsJson);
                }
            }    
	}
        for (TimeSlot ts : ohDataManager.getHeldTs()) {
            for (int i = 0; i < DayOfWeek.values().length; i++) {
                DayOfWeek dow = DayOfWeek.values()[i];
                tasIterator = ts.getTAsIterator(dow);
                while (tasIterator.hasNext()) {
                    TeachingAssistantPrototype ta = tasIterator.next();
                    JsonObject tsJson = Json.createObjectBuilder()
                        .add(JSON_OH_START_TIME, ts.getStartTime().replace(":", "_"))
                        .add(JSON_OH_DAY_OF_WEEK, dow.toString())
                        .add(JSON_OH_NAME, ta.getName()).build();
                    officeHoursArrayBuilder.add(tsJson);
                }
            }    
	}
	JsonArray officeHoursArrayDRAFT = officeHoursArrayBuilder.build();
        
        String[] strips = officeHoursArrayDRAFT.toString().replace("[", "").replace("]", "").split(",");
        for(int i = 0; i < strips.length; i++){
//            System.out.println(strips[i]);
        }
        String rstrip = "";
        for(int i = 0; i < strips.length; i++){
            rstrip += strips[i];
        }
        String[] rstrips = rstrip.split("\\{");
        for(int i = 0; i < rstrips.length; i++){
//            System.out.println(rstrips[i]);
        }
        String rs = "";
        for(int i = 0; i < rstrips.length; i++){
            rs += rstrips[i];
        }
        String[] srs = rs.replace("\"\"", "\",\"").replace("\"time\":","").replace("\"day\":", "").replace("\"name\":", "").split("\\}");
        ArrayList<String> tss = new ArrayList<>(Arrays.asList(srs));
        Set<String> set = new LinkedHashSet<>(tss);
        JsonArrayBuilder updateOHBuilder = Json.createArrayBuilder();
        for(String s: set){
//            System.out.println(s);
//            if(!(s.length() == 0)){
            try{
                JsonObject js = Json.createObjectBuilder()
                        .add(JSON_OH_START_TIME, s.substring(0, s.indexOf(",")).replace("\"", ""))
                        .add(JSON_OH_DAY_OF_WEEK, s.substring(s.indexOf(",") + 1, s.lastIndexOf(",")).replace("\"", ""))
                        .add(JSON_OH_NAME, s.substring(s.lastIndexOf(",") + 1).replace("\"", "")).build();
                updateOHBuilder.add(js);
            }
            catch(Exception e){
                
            }

//            }
        }
        JsonArray officeHoursArray = updateOHBuilder.build();
        /*****************************************************************************************************/
        
        
        /******************FORMATS THE SCHEDULE DATA********************/
        String startDate = "";
        String startingMondayMonth = "";
        String startingMondayDay = "";
        String startingMondayYear = "";
        String endDate = "";
        String endingFridayMonth = "";
        String endingFridayDay = "";
        String endingFridayYear = "";
        if(!(schDataManager.getStartDate() == null)){
            startDate = schDataManager.getStartDate().toString();
            startingMondayMonth = Integer.toString(schDataManager.getStartDate().getMonthValue());
            startingMondayDay = Integer.toString(schDataManager.getStartDate().getDayOfMonth());
            startingMondayYear = Integer.toString(schDataManager.getStartDate().getYear());
        }
        else{
            startDate = "";
            startingMondayMonth= "";
            startingMondayDay = "";
            startingMondayYear = "";
        }
        if(!(schDataManager.getEndDate() == null)){
            endDate = schDataManager.getEndDate().toString();
            endingFridayMonth = Integer.toString(schDataManager.getEndDate().getMonthValue());
            endingFridayDay = Integer.toString(schDataManager.getEndDate().getDayOfMonth());
            endingFridayYear = Integer.toString(schDataManager.getEndDate().getYear());
        }
        else{
            endDate = "";
            endingFridayMonth = "";
            endingFridayDay = "";
            endingFridayYear = "";
        }

        JsonArrayBuilder schHolidayArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder schLectureArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder schRefArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder schRecArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder schLabArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder schHwArrayBuilder = Json.createArrayBuilder();
        
        Iterator<ScheduleItemPrototype> schIterator = schDataManager.scheduleIterator();
        while(schIterator.hasNext()){
            ScheduleItemPrototype s = schIterator.next();
            if(s.getType().equals("Holiday")){
                JsonObject schJson = Json.createObjectBuilder()
                        .add(JSON_SCH_EVENT_DATE, s.getLocalDate().toString())
                        .add(JSON_SCH_EVENT_MONTH, Integer.toString(s.getLocalDate().getMonthValue()))
                        .add(JSON_SCH_EVENT_DAY, Integer.toString(s.getLocalDate().getDayOfMonth()))
                        .add(JSON_SCH_EVENT_YEAR, Integer.toString(s.getLocalDate().getYear()))
                        .add(JSON_SCH_EVENT_TITLE, s.getTitle())
                        .add(JSON_SCH_EVENT_TOPIC, s.getTopic())
                        .add(JSON_SCH_EVENT_LINK, s.getLink()).build();
                schHolidayArrayBuilder.add(schJson);               
            }
            else if(s.getType().equals("Lecture")){
                JsonObject schJson = Json.createObjectBuilder()
                        .add(JSON_SCH_EVENT_DATE, s.getLocalDate().toString())
                        .add(JSON_SCH_EVENT_MONTH, Integer.toString(s.getLocalDate().getMonthValue()))
                        .add(JSON_SCH_EVENT_DAY, Integer.toString(s.getLocalDate().getDayOfMonth()))
                        .add(JSON_SCH_EVENT_YEAR, Integer.toString(s.getLocalDate().getYear()))
                        .add(JSON_SCH_EVENT_TITLE, s.getTitle())
                        .add(JSON_SCH_EVENT_TOPIC, s.getTopic())
                        .add(JSON_SCH_EVENT_LINK, s.getLink()).build();
                schLectureArrayBuilder.add(schJson);              
            }
            else if(s.getType().equals("Recitation")){
                JsonObject schJson = Json.createObjectBuilder()
                        .add(JSON_SCH_EVENT_DATE, s.getLocalDate().toString())
                        .add(JSON_SCH_EVENT_MONTH, Integer.toString(s.getLocalDate().getMonthValue()))
                        .add(JSON_SCH_EVENT_DAY, Integer.toString(s.getLocalDate().getDayOfMonth()))
                        .add(JSON_SCH_EVENT_YEAR, Integer.toString(s.getLocalDate().getYear()))
                        .add(JSON_SCH_EVENT_TITLE, s.getTitle())
                        .add(JSON_SCH_EVENT_TOPIC, s.getTopic())
                        .add(JSON_SCH_EVENT_LINK, s.getLink()).build();
                schRecArrayBuilder.add(schJson);                
            }
            else if(s.getType().equals("Lab")){
                JsonObject schJson = Json.createObjectBuilder()
                        .add(JSON_SCH_EVENT_DATE, s.getLocalDate().toString())
                        .add(JSON_SCH_EVENT_MONTH, Integer.toString(s.getLocalDate().getMonthValue()))
                        .add(JSON_SCH_EVENT_DAY, Integer.toString(s.getLocalDate().getDayOfMonth()))
                        .add(JSON_SCH_EVENT_YEAR, Integer.toString(s.getLocalDate().getYear()))
                        .add(JSON_SCH_EVENT_TITLE, s.getTitle())
                        .add(JSON_SCH_EVENT_TOPIC, s.getTopic())
                        .add(JSON_SCH_EVENT_LINK, s.getLink()).build();
                schLabArrayBuilder.add(schJson);                
            }
            else if(s.getType().equals("Reference")){
                JsonObject schJson = Json.createObjectBuilder()
                        .add(JSON_SCH_EVENT_DATE, s.getLocalDate().toString())
                        .add(JSON_SCH_EVENT_MONTH, Integer.toString(s.getLocalDate().getMonthValue()))
                        .add(JSON_SCH_EVENT_DAY, Integer.toString(s.getLocalDate().getDayOfMonth()))
                        .add(JSON_SCH_EVENT_YEAR, Integer.toString(s.getLocalDate().getYear()))
                        .add(JSON_SCH_EVENT_TITLE, s.getTitle())
                        .add(JSON_SCH_EVENT_TOPIC, s.getTopic())
                        .add(JSON_SCH_EVENT_LINK, s.getLink()).build();
                schRefArrayBuilder.add(schJson);                
            }
            else if(s.getType().equals("HW")){
                JsonObject schJson = Json.createObjectBuilder()
                        .add(JSON_SCH_EVENT_DATE, s.getLocalDate().toString())
                        .add(JSON_SCH_EVENT_MONTH, Integer.toString(s.getLocalDate().getMonthValue()))
                        .add(JSON_SCH_EVENT_DAY, Integer.toString(s.getLocalDate().getDayOfMonth()))
                        .add(JSON_SCH_EVENT_YEAR, Integer.toString(s.getLocalDate().getYear()))
                        .add(JSON_SCH_EVENT_TITLE, s.getTitle())
                        .add(JSON_SCH_EVENT_TOPIC, s.getTopic())
                        .add(JSON_SCH_EVENT_LINK, s.getLink()).build();
                schHwArrayBuilder.add(schJson);                
            }
        }
        JsonArray schHolidayArray = schHolidayArrayBuilder.build();
        JsonArray schLectureArray = schLectureArrayBuilder.build();
        JsonArray schRefArray = schRefArrayBuilder.build();
        JsonArray schRecArray = schRecArrayBuilder.build();
        JsonArray schLabArray = schLabArrayBuilder.build();
        JsonArray schHwArray = schHwArrayBuilder.build();
        /*************************************************************/
        /** PUTS IT ALL TOGETHER **/
        
        JsonObject pageDataJSON = Json.createObjectBuilder()
                //Add the site data
                .add(JSON_SITE_SUBJECT, siteSubject)
                .add(JSON_SITE_NUMBER, siteNumber)
                .add(JSON_SITE_SEMESTER, siteSem)
                .add(JSON_SITE_YEAR, siteYr)
                .add(JSON_SITE_TITLE, siteTitle)
                .add(JSON_SITE_LOGOS, logosArray)
                .add(JSON_SITE_INSTRUCTOR, instructorArray)
                .add(JSON_SITE_PAGES, pagesArray).build();
//                .add(JSON_SITE_EXPORT_URL, siteDataManager.getExp())
        Map<String, Object> pageProperties = new HashMap<>(1);
	pageProperties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory pageWriterFactory = Json.createWriterFactory(pageProperties);
	StringWriter pageSW = new StringWriter();
	JsonWriter pageJsonWriter = pageWriterFactory.createWriter(pageSW);
	pageJsonWriter.writeObject(pageDataJSON);
	pageJsonWriter.close();
	// INIT THE WRITER
	OutputStream pageOS = new FileOutputStream(pageDataPath);
	JsonWriter pageJsonFileWriter = Json.createWriter(pageOS);
	pageJsonFileWriter.writeObject(pageDataJSON);
	String pagePrettyPrinted = pageSW.toString();
	PrintWriter pagePW = new PrintWriter(pageDataPath);
	pagePW.write(pagePrettyPrinted);
	pagePW.close();
        
                 //Adds the Syllabus info
        JsonObject syllabusDataJSON = Json.createObjectBuilder()
                .add(JSON_SYL_DESCRIPTION, sylDescription)
                .add(JSON_SYL_TOPICS, /*syllabusDataManager.getTopicsJSON()*/topicArray)
                .add(JSON_SYL_PREREQUISITES, sylPR)
                .add(JSON_SYL_OUTCOMES, /*syllabusDataManager.getOutcomesJSON()*/outcomeArray)
                .add(JSON_SYL_TEXTBOOKS, /*syllabusDataManager.getTextbooksJSON()*/textbookArray)
                .add(JSON_SYL_GC, /*syllabusDataManager.getGcJSON()*/gcArray)
                .add(JSON_SYL_GN, sylGN)
                .add(JSON_SYL_AD, sylAD)
                .add(JSON_SYL_SA, sylSA).build();
        Map<String, Object> sylProperties = new HashMap<>(1);
	sylProperties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory sylWriterFactory = Json.createWriterFactory(sylProperties);
	StringWriter sylSW = new StringWriter();
	JsonWriter sylJsonWriter = sylWriterFactory.createWriter(sylSW);
	sylJsonWriter.writeObject(syllabusDataJSON);
	sylJsonWriter.close();
	// INIT THE WRITER
	OutputStream sylOS = new FileOutputStream(sylDataPath);
	JsonWriter sylJsonFileWriter = Json.createWriter(sylOS);
	sylJsonFileWriter.writeObject(pageDataJSON);
	String sylPrettyPrinted = sylSW.toString();
	PrintWriter sylPW = new PrintWriter(sylDataPath);
	sylPW.write(sylPrettyPrinted);
	sylPW.close();
        
                 //Adds the Meeting Times info
        JsonObject mtDataJSON = Json.createObjectBuilder()
                .add(JSON_MT_LECTURES, lectureArray)
                .add(JSON_MT_RECITATIONS, recitationArray)
                .add(JSON_MT_LABS, labsArray).build();
        Map<String, Object> mtProperties = new HashMap<>(1);
	mtProperties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory mtWriterFactory = Json.createWriterFactory(mtProperties);
	StringWriter mtSW = new StringWriter();
	JsonWriter mtJsonWriter = mtWriterFactory.createWriter(mtSW);
	mtJsonWriter.writeObject(mtDataJSON);
	mtJsonWriter.close();
	// INIT THE WRITER
	OutputStream mtOS = new FileOutputStream(mtDataPath);
	JsonWriter mtJsonFileWriter = Json.createWriter(mtOS);
	mtJsonFileWriter.writeObject(mtDataJSON);
	String mtPrettyPrinted = mtSW.toString();
	PrintWriter mtPW = new PrintWriter(mtDataPath);
	mtPW.write(mtPrettyPrinted);
	mtPW.close();
        
        JsonObject ohDataJSON = Json.createObjectBuilder()
                // Adds the OH Info
		.add(JSON_OH_START_HOUR, "" + ohDataManager.getStartHour())
		.add(JSON_OH_END_HOUR, "" + ohDataManager.getEndHour())
                .add(JSON_SITE_INSTRUCTOR, instructorArray)
                .add(JSON_OH_GRAD_TAS, gradTAsArray)
                .add(JSON_OH_UNDERGRAD_TAS, undergradTAsArray)
                .add(JSON_OH_OFFICE_HOURS, officeHoursArray).build();
        Map<String, Object> ohProperties = new HashMap<>(1);
	ohProperties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory ohWriterFactory = Json.createWriterFactory(ohProperties);
	StringWriter ohSW = new StringWriter();
	JsonWriter ohJsonWriter = ohWriterFactory.createWriter(ohSW);
	ohJsonWriter.writeObject(ohDataJSON);
	ohJsonWriter.close();
	// INIT THE WRITER
	OutputStream ohOS = new FileOutputStream(ohDataPath);
	JsonWriter ohJsonFileWriter = Json.createWriter(ohOS);
	ohJsonFileWriter.writeObject(ohDataJSON);
	String ohPrettyPrinted = ohSW.toString();
	PrintWriter ohPW = new PrintWriter(ohDataPath);
	ohPW.write(ohPrettyPrinted);
	ohPW.close();
        
        JsonObject schDataJSON = Json.createObjectBuilder()
                // Adds the Schedule Info
                .add(JSON_SCH_START_DATE, startDate)
                .add(JSON_SCH_END_DATE, endDate)
                .add(JSON_SCH_STARTING_MONDAY_MONTH, startingMondayMonth)
                .add(JSON_SCH_STARTING_MONDAY_DAY, startingMondayDay)
                .add(JSON_SCH_STARTING_MONDAY_YEAR, startingMondayYear)
                .add(JSON_SCH_ENDING_FRIDAY_MONTH, endingFridayMonth)
                .add(JSON_SCH_ENDING_FRIDAY_DAY, endingFridayDay)
                .add(JSON_SCH_ENDING_FRIDAY_YEAR, endingFridayYear)
                .add(JSON_SCH_HOLIDAYS, schHolidayArray)
                .add(JSON_SCH_LECTURES, schLectureArray)
                .add(JSON_SCH_RECITATIONS, schRecArray)
                .add(JSON_SCH_LABS, schLabArray)
                .add(JSON_SCH_REFERENCES, schRefArray)
                .add(JSON_SCH_HWS, schHwArray)
		.build();
        Map<String, Object> schProperties = new HashMap<>(1);
	schProperties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory schWriterFactory = Json.createWriterFactory(schProperties);
	StringWriter schSW = new StringWriter();
	JsonWriter schJsonWriter = schWriterFactory.createWriter(schSW);
	schJsonWriter.writeObject(schDataJSON);
	schJsonWriter.close();
	// INIT THE WRITER
	OutputStream schOS = new FileOutputStream(schDataPath);
	JsonWriter schJsonFileWriter = Json.createWriter(schOS);
	schJsonFileWriter.writeObject(schDataJSON);
	String schPrettyPrinted = schSW.toString();
	PrintWriter schPW = new PrintWriter(schDataPath);
	schPW.write(schPrettyPrinted);
	schPW.close();
        
        
        // Copying the CSS over
        Path cssImport = Paths.get(siteDataManager.getCSS());
        Path cssExport = Paths.get(siteDataManager.getExp() + "/css/");
        try{
            Files.copy(cssImport, cssExport.resolve(cssImport.getFileName()));
            
        }
        catch(FileAlreadyExistsException e){
            
        }
        File rename = new File(cssExport + "/" + siteDataManager.getCSS().substring(siteDataManager.getCSS().lastIndexOf("/") + 1));
        boolean renameResult = rename.renameTo(new File(cssExport + "/sea_wolf.css"));
//        Files.move(Paths.get(cssExport + siteDataManager.getCSS().substring(siteDataManager.getCSS().lastIndexOf("/") + 1)), Paths.get(cssExport + siteDataManager.getCSS().substring(siteDataManager.getCSS().lastIndexOf("/") + 1)).resolveSibling("sea_wolf.css"));
        Path cssDefImport = Paths.get("./work/selectedcss/course_homepage_layout.css");
        Path cssDefExport = Paths.get(siteDataManager.getExp() + "/css/");
        try{
            Files.copy(cssDefImport, cssDefExport.resolve(cssDefImport.getFileName()));
        }
        catch(FileAlreadyExistsException e){
            
        }
        
        // Copying the images over
        Path favImport = Paths.get(siteDataManager.getFavUrl());
        Path navImport = Paths.get(siteDataManager.getNavUrl());
        Path leftImport = Paths.get(siteDataManager.getLeftUrl());
        Path rightImport = Paths.get(siteDataManager.getRightUrl());
        Path imgExport = Paths.get(siteDataManager.getExp() + "/images/");
        try{
            Files.copy(favImport, imgExport.resolve(favImport.getFileName())); 
        }
        catch(FileAlreadyExistsException e){
            
        }
        try{
            Files.copy(navImport, imgExport.resolve(navImport.getFileName()));         
        }
        catch(FileAlreadyExistsException e){
            
        }
        try{
            Files.copy(leftImport, imgExport.resolve(leftImport.getFileName())); 
        }
        catch(FileAlreadyExistsException e){
            
        }
        try{
            Files.copy(rightImport, imgExport.resolve(rightImport.getFileName()));  
        }
        catch(FileAlreadyExistsException e){
            
        }
        
        // Copying everything over
        File exportTo = new File("./app_data/");
        File importTo = new File(siteDataManager.getExp());
        File exportToCSS = new File("./app_data/css/");
        File exportToImg = new File("./app_data/images/");
        File exportToJS = new File("./app_data/js/");
        File htmlImport = new File("./work/html/");
        File htmlExport= new File(siteDataManager.getExp());
        File jsImport = new File("./work/js/");
        File jsExport = new File(siteDataManager.getExp() + "/js/");
        FileUtils.copyDirectory(htmlImport, htmlExport);
        FileUtils.copyDirectory(jsImport, jsExport);
        FileUtils.copyDirectory(importTo, exportTo);
        AppWebDialog a = new AppWebDialog(app);
        a.showWebDialog(siteDataManager.getExp() + "/index.html");
    }

    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}