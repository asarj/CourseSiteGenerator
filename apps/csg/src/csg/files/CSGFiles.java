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
import csg.data.CSGData;
import csg.data.MeetingTimesData;
import csg.data.OHData;
import csg.data.ScheduleData;
import csg.data.SiteData;
import csg.data.SyllabusData;
import csg.data.TAType;
import csg.data.TeachingAssistantPrototype;
import csg.data.TimeSlot;
import csg.data.TimeSlot.DayOfWeek;
import java.math.BigDecimal;
import java.util.ArrayList;
import javax.json.JsonObjectBuilder;

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
    static final String JSON_SCH_EVENT_MONTH = "month";
    static final String JSON_SCH_EVENT_DAY = "event_day";
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
	CSGData d = (CSGData)data;
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
        siteDataManager.setSelectedName(courseSubject);
        
        String courseNum = json.getString(JSON_SITE_NUMBER);
        siteDataManager.setSelectedNum(courseNum);
        
        String courseSem = json.getString(JSON_SITE_SEMESTER);
        siteDataManager.setSelectedSem(courseSem);
        
        String courseYr = json.getString(JSON_SITE_YEAR);
        siteDataManager.setSelectedYear(courseYr);
        
        String courseTitle = json.getString(JSON_SITE_TITLE);
        siteDataManager.setTitle(courseTitle);
        
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
        }
        if(!jsonLogosArray.getJsonObject(1).getString(JSON_SITE_NAVBAR).equals("")){
            siteDataManager.setNavUrl(jsonLogosArray.getJsonObject(1).getString(JSON_SITE_NAVBAR));
        }
        if(!jsonLogosArray.getJsonObject(2).getString(JSON_SITE_BOTTOM_LEFT).equals("")){
            siteDataManager.setLeftUrl(jsonLogosArray.getJsonObject(2).getString(JSON_SITE_BOTTOM_LEFT));
        }
        if(!jsonLogosArray.getJsonObject(3).getString(JSON_SITE_BOTTOM_RIGHT).equals("")){
            siteDataManager.setRightUrl(jsonLogosArray.getJsonObject(3).getString(JSON_SITE_BOTTOM_RIGHT));
        }
        
        String courseCSS = json.getString(JSON_SITE_SELECTED_CSS);
        siteDataManager.setCSS(courseCSS.substring(courseCSS.lastIndexOf("/") + 1));
        
        JsonArray jsonInstructorArray = json.getJsonArray(JSON_SITE_INSTRUCTOR);
        siteDataManager.setInstructorName(jsonInstructorArray.getJsonObject(0).getString(JSON_SITE_INSTRUCTOR_NAME));
        siteDataManager.setInstructorEmail(jsonInstructorArray.getJsonObject(1).getString(JSON_SITE_INSTRUCTOR_EMAIL));
        siteDataManager.setInstructorRoom(jsonInstructorArray.getJsonObject(2).getString(JSON_SITE_INSTRUCTOR_ROOM));
        siteDataManager.setInstructorHP(jsonInstructorArray.getJsonObject(3).getString(JSON_SITE_INSTRUCTOR_HP));
        try{
            siteDataManager.setInstructorHoursJSON(jsonInstructorArray.getJsonObject(4).getString(JSON_SITE_INSTRUCTOR_HOURS));
        }
        catch(Exception e){
            
        }
        
        /**********************************************************/
        
        
        /*******************LOADS THE OH DATA**********************/
	// LOAD THE START AND END HOURS
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
            timeSlot.toggleTA(dow, ta);
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
        SiteData siteDataManager = d.getSiteData();
        SyllabusData syllabusDataManager = d.getSyllabusData();
        MeetingTimesData mtDataManager = d.getMeetingTimesData();
        OHData ohDataManager = d.getOfficeHoursData();
        ScheduleData schDataManager = d.getScheduleData();

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
        JsonObject favUrl = Json.createObjectBuilder().add(JSON_SITE_FAVICON, siteDataManager.getFavUrl()).build();
        logos.add(favUrl);
        JsonObject navUrl = Json.createObjectBuilder().add(JSON_SITE_NAVBAR, siteDataManager.getNavUrl()).build();
        logos.add(navUrl);
        JsonObject leftUrl = Json.createObjectBuilder().add(JSON_SITE_BOTTOM_LEFT, siteDataManager.getLeftUrl()).build();
        logos.add(leftUrl);
        JsonObject rightUrl = Json.createObjectBuilder().add(JSON_SITE_BOTTOM_RIGHT, siteDataManager.getRightUrl()).build();
        logos.add(rightUrl);
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
        JsonArrayBuilder instHours = Json.createArrayBuilder().add(siteDataManager.getInstructorHoursJSON());
        inst.add(instHours);
        JsonArray instructorArray = inst.build();
        /*******************************************************/
        
        
        
        /*****************SAVES THE SYLLABUS DATA*******************/
        
        /***********************************************************/
        
        /****************SAVES THE OH DATA**********************/
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
        
	// THEN PUT IT ALL TOGETHER IN A JsonObject
	JsonObject dataManagerJSO = Json.createObjectBuilder()
                // Adds the Site info
                .add(JSON_SITE_SUBJECT, siteDataManager.getSelectedName())
                .add(JSON_SITE_NUMBER, siteDataManager.getSelectedNum())
                .add(JSON_SITE_SEMESTER, siteDataManager.getSelectedSem())
                .add(JSON_SITE_YEAR, siteDataManager.getSelectedYear())
                .add(JSON_SITE_TITLE, siteDataManager.getTitle())
                .add(JSON_SITE_EXPORT_URL, siteDataManager.getExp())
                .add(JSON_SITE_PAGES, pagesArray)
                .add(JSON_SITE_LOGOS, logosArray)
                .add(JSON_SITE_SELECTED_CSS, siteDataManager.getCSS())
                .add(JSON_SITE_INSTRUCTOR, instructorArray)
                // Adds the Syllabus info
                
                // Adds the OH Info
		.add(JSON_OH_START_HOUR, "" + ohDataManager.getStartHour())
		.add(JSON_OH_END_HOUR, "" + ohDataManager.getEndHour())
                .add(JSON_OH_GRAD_TAS, gradTAsArray)
                .add(JSON_OH_UNDERGRAD_TAS, undergradTAsArray)
                .add(JSON_OH_OFFICE_HOURS, officeHoursArray)
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