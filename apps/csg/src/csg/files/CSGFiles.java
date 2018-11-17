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
import java.util.ArrayList;

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
        
        /* LOADS THE OH DATA */
        ohDataManager.reset();

	// LOAD THE JSON FILE WITH ALL THE DATA
	JsonObject json = loadJSONFile(filePath);

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