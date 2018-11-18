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
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javax.json.JsonObjectBuilder;
import properties_manager.PropertiesManager;

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
    static final String JSON_SITE_NUMBER = "number";
    static final String JSON_SITE_SEMESTER = "semester";
    static final String JSON_SITE_YEAR = "year";
    static final String JSON_SITE_TITLE = "site_title";
    static final String JSON_SITE_EXPORT_URL = "export_url";
    static final String JSON_SITE_PAGES = "pages";
    static final String JSON_SITE_PAGES_NAME = "name";
    static final String JSON_SITE_LOGOS = "logos";
    static final String JSON_SITE_FAVICON = "favicon";
    static final String JSON_SITE_NAVBAR = "navbar";
    static final String JSON_SITE_BOTTOM_LEFT = "bottom_left";
    static final String JSON_SITE_BOTTOM_RIGHT = "bottom_right";
    static final String JSON_SITE_SELECTED_CSS = "css";
    static final String JSON_SITE_INSTRUCTOR = "instructor";
    static final String JSON_SITE_INSTRUCTOR_NAME = "instructor_name";
    static final String JSON_SITE_INSTRUCTOR_EMAIL = "instructor_email";
    static final String JSON_SITE_INSTRUCTOR_ROOM = "instructor_room";
    static final String JSON_SITE_INSTRUCTOR_HP = "instructor_hp";
    static final String JSON_SITE_INSTRUCTOR_HOURS = "instructor_hours";
    
    // SYLLABUS DATA FIELDS
    static final String JSON_SYL_DESCRIPTION = "description";
    static final String JSON_SYL_TOPICS = "topics";
    static final String JSON_SYL_PREREQUISITES = "prerequisites";
    static final String JSON_SYL_OUTCOMES = "outcomes";
    static final String JSON_SYL_TEXTBOOKS = "textbooks";
    static final String JSON_SYL_GC = "graded_components";
    static final String JSON_SYL_GN = "grading_note";
    static final String JSON_SYL_AD = "academic_dishonesty";
    static final String JSON_SYL_SA = "special_assistance";
    
    // MEETING TIMES DATA FIELDS
    static final String JSON_MT_LECTURES = "lectures";
    static final String JSON_MT_LECTURES_SECTION = "lec_section";
    static final String JSON_MT_LECTURES_DAYS = "lec_days";
    static final String JSON_MT_LECTURES_TIME = "lec_time";
    static final String JSON_MT_LECTURES_ROOM = "lec_room";
    static final String JSON_MT_RECITATIONS = "recitations";
    static final String JSON_MT_RECITATIONS_SECTION = "r_section";
    static final String JSON_MT_RECITATIONS_DAY_TIME = "r_day_time";
    static final String JSON_MT_RECITATIONS_LOCATION = "r_location";
    static final String JSON_MT_RECITATIONS_TA1 = "r_ta_1";
    static final String JSON_MT_RECITATIONS_TA2 = "r_ta_2";
    static final String JSON_MT_LABS = "labs";
    static final String JSON_MT_LABS_SECTION = "lab_section";
    static final String JSON_MT_LABS_DAY_TIME = "lab_day_time";
    static final String JSON_MT_LABS_LOCATION = "lab_location";
    static final String JSON_MT_LABS_TA1 = "lab_ta_1";
    static final String JSON_MT_LABS_TA2 = "lab_ta_2";
    
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
    static final String JSON_SCH_HOLIDAYS = "sch_holidays";
    static final String JSON_SCH_LECTURES = "sch_lectures";
    static final String JSON_SCH_REFERENCES = "sch_references";
    static final String JSON_SCH_RECITATIONS = "sch_recitations";
    static final String JSON_SCH_HWS = "sch_hws";
    static final String JSON_SCH_LABS = "sch_labs";
    static final String JSON_SCH_EVENT_DATE = "sch_date";
    static final String JSON_SCH_EVENT_MONTH = "sch_month";
    static final String JSON_SCH_EVENT_DAY = "sch_event_day";
    static final String JSON_SCH_EVENT_YEAR = "sch_year";
    static final String JSON_SCH_EVENT_TITLE = "sch_title";
    static final String JSON_SCH_EVENT_TOPIC = "sch_topic";
    static final String JSON_SCH_EVENT_LINK = "sch_link";

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
        schDataManager.reset();
        ohDataManager.reset();
        

	// LOAD THE JSON FILE WITH ALL THE DATA
	JsonObject json = loadJSONFile(filePath);

        /******************LOADS THE SITE DATA*********************/
        String courseSubject = json.getString(JSON_SITE_SUBJECT);
        if(!courseSubject.trim().equals(""))
            siteDataManager.setSelectedName(courseSubject);
        
        String courseNum = json.getString(JSON_SITE_NUMBER);
        if(!courseNum.trim().equals(""))
            siteDataManager.setSelectedNum(courseNum);
        
        String courseSem = json.getString(JSON_SITE_SEMESTER);
        if(!courseSem.trim().equals(""))
            siteDataManager.setSelectedSem(courseSem);
        
        String courseYr = json.getString(JSON_SITE_YEAR);
        if(!courseYr.trim().equals(""))
            siteDataManager.setSelectedYear(courseYr);
        
        String courseTitle = json.getString(JSON_SITE_TITLE);
        if(!courseTitle.trim().equals("")){
            siteDataManager.setTitle(courseTitle);
            TextField nameTF = (TextField)gui.getGUINode(SITE_TITLE_TEXT_FIELD);
            nameTF.setText(courseTitle);
        }
        
        String expUrl = json.getString(JSON_SITE_EXPORT_URL);
        siteDataManager.setExp(expUrl);
        
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
            siteDataManager.setFavUrl(props.getProperty(APP_PATH_IMAGES) + "styleicons/" + props.getProperty(DEFAULT_FAVICON_TEXT));
            workspace.getFviImgView().setImage(new Image(props.getProperty(APP_FILE_PROTOCOL) + props.getProperty(APP_PATH_IMAGES) + "styleicons/" + props.getProperty(DEFAULT_FAVICON_TEXT)));
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
            siteDataManager.setNavUrl(props.getProperty(APP_PATH_IMAGES) + "styleicons/" + props.getProperty(DEFAULT_NAVBAR_TEXT));
            workspace.getNavImgView().setImage(new Image(props.getProperty(APP_FILE_PROTOCOL) + props.getProperty(APP_PATH_IMAGES) + "styleicons/" + props.getProperty(DEFAULT_NAVBAR_TEXT)));
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
            siteDataManager.setLeftUrl(props.getProperty(APP_PATH_IMAGES) + "styleicons/" + props.getProperty(DEFAULT_LFIMG_TEXT));
            workspace.getLeftImgView().setImage(new Image(props.getProperty(APP_FILE_PROTOCOL) + props.getProperty(APP_PATH_IMAGES) + "styleicons/" + props.getProperty(DEFAULT_LFIMG_TEXT)));
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
            siteDataManager.setRightUrl(props.getProperty(APP_PATH_IMAGES) + "styleicons/" + props.getProperty(DEFAULT_RFIMG_TEXT));
            workspace.getRightImgView().setImage(new Image(props.getProperty(APP_FILE_PROTOCOL) + props.getProperty(APP_PATH_IMAGES) + "styleicons/" + props.getProperty(DEFAULT_RFIMG_TEXT)));
        }
        
        String courseCSS = json.getString(JSON_SITE_SELECTED_CSS);
        if(!courseCSS.trim().equals("")){
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
        ohDataManager.initHours(startHour, endHour);
        
        // LOAD ALL THE GRAD TAs
        loadTAs(ohDataManager, json, JSON_OH_GRAD_TAS);
        loadTAs(ohDataManager, json, JSON_OH_UNDERGRAD_TAS);

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
        
        /**********************************************************/
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
    
    private void loadTAs(OHData data, JsonObject json, String tas) {
        JsonArray jsonTAArray = json.getJsonArray(tas);
        for (int i = 0; i < jsonTAArray.size(); i++) {
            JsonObject jsonTA = jsonTAArray.getJsonObject(i);
            String name = jsonTA.getString(JSON_OH_NAME);
            String email = jsonTA.getString(JSON_OH_EMAIL);
            TAType type = TAType.valueOf(jsonTA.getString(JSON_OH_TYPE));
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
        for(String s: siteDataManager.getSelectedPageOptions()){
            if(s.equals("home")){
                JsonObject cbOption = Json.createObjectBuilder()
                        .add(JSON_SITE_PAGES_NAME, s).build();
                pagesArrayBuilder.add(cbOption);
            }
            else if(s.equals("syllabus")){
                JsonObject cbOption = Json.createObjectBuilder()
                        .add(JSON_SITE_PAGES_NAME, s).build();
                pagesArrayBuilder.add(cbOption);
            }
            else if(s.equals("schedule")){
                JsonObject cbOption = Json.createObjectBuilder()
                        .add(JSON_SITE_PAGES_NAME, s).build();
                pagesArrayBuilder.add(cbOption);
            }
            else if(s.equals("hw")){
                JsonObject cbOption = Json.createObjectBuilder()
                        .add(JSON_SITE_PAGES_NAME, s).build();
                pagesArrayBuilder.add(cbOption);
            }
        }
        JsonArray pagesArray = pagesArrayBuilder.build();
        
        JsonArrayBuilder logos = Json.createArrayBuilder();
        if(!siteDataManager.getFavUrl().trim().equals("")){
            JsonObject favUrl = Json.createObjectBuilder().add(JSON_SITE_FAVICON, siteDataManager.getFavUrl()).build();
            logos.add(favUrl);
        }
        else{
            siteDataManager.setFavUrl(props.getProperty(APP_PATH_IMAGES) + "styleicons/" + props.getProperty(DEFAULT_FAVICON_TEXT));
            JsonObject favUrl = Json.createObjectBuilder().add(JSON_SITE_FAVICON, props.getProperty(APP_PATH_IMAGES) + "styleicons/" + props.getProperty(DEFAULT_FAVICON_TEXT)).build();
            logos.add(favUrl);
        }
        if(!siteDataManager.getNavUrl().trim().equals("")){
            JsonObject navUrl = Json.createObjectBuilder().add(JSON_SITE_NAVBAR, siteDataManager.getNavUrl()).build();
            logos.add(navUrl);
        }
        else{
            siteDataManager.setNavUrl(props.getProperty(APP_PATH_IMAGES) + "styleicons/" + props.getProperty(DEFAULT_NAVBAR_TEXT));
            JsonObject navUrl = Json.createObjectBuilder().add(JSON_SITE_NAVBAR, props.getProperty(APP_PATH_IMAGES) + "styleicons/" + props.getProperty(DEFAULT_NAVBAR_TEXT)).build();
            logos.add(navUrl);
        }
        if(!siteDataManager.getLeftUrl().trim().equals("")){
            JsonObject leftUrl = Json.createObjectBuilder().add(JSON_SITE_BOTTOM_LEFT, siteDataManager.getLeftUrl()).build();
            logos.add(leftUrl);
        }
        else{
            siteDataManager.setLeftUrl(props.getProperty(APP_PATH_IMAGES) + "styleicons/" + props.getProperty(DEFAULT_LFIMG_TEXT));
            JsonObject leftUrl = Json.createObjectBuilder().add(JSON_SITE_BOTTOM_LEFT, props.getProperty(APP_PATH_IMAGES) + "styleicons/" + props.getProperty(DEFAULT_LFIMG_TEXT)).build();
            logos.add(leftUrl);
        }
        if(!siteDataManager.getRightUrl().trim().equals("")){
            JsonObject rightUrl = Json.createObjectBuilder().add(JSON_SITE_BOTTOM_RIGHT, siteDataManager.getRightUrl()).build();
            logos.add(rightUrl);
        }
        else{
            siteDataManager.setRightUrl(props.getProperty(APP_PATH_IMAGES) + "styleicons/" + props.getProperty(DEFAULT_RFIMG_TEXT));
            JsonObject rightUrl = Json.createObjectBuilder().add(JSON_SITE_BOTTOM_RIGHT, props.getProperty(APP_PATH_IMAGES) + "styleicons/" + props.getProperty(DEFAULT_RFIMG_TEXT)).build();
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
	JsonArray officeHoursArray = officeHoursArrayBuilder.build();
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
    }
    
    // IMPORTING/EXPORTING DATA IS USED WHEN WE READ/WRITE DATA IN AN
    // ADDITIONAL FORMAT USEFUL FOR ANOTHER PURPOSE, LIKE ANOTHER APPLICATION

    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void exportData(AppDataComponent data, String filePath) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}