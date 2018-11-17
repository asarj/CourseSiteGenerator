/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;

import csg.CSGApp;
import djf.components.AppDataComponent;

/**
 *
 * @author Ajay
 */
public class CSGData implements AppDataComponent{
    CSGApp app;
    SiteData siteData;
    SyllabusData syllabusData;
    MeetingTimesData meetingTimesData;
    OHData officeHoursData;
    ScheduleData scheduleData;

    
    
    public CSGData(CSGApp initApp){
        app = initApp;
        siteData = new SiteData(app);
        syllabusData = new SyllabusData(app);
        meetingTimesData = new MeetingTimesData(app);
        officeHoursData = new OHData(app);
        scheduleData = new ScheduleData(app);
    }
    
    public void reset(){
        
    }
    
    public SiteData getSiteData() {
        return siteData;
    }

    public void setSiteData(SiteData siteData) {
        this.siteData = siteData;
    }
    
    public SyllabusData getSyllabusData() {
        return syllabusData;
    }

    public void setSyllabusData(SyllabusData syllabusData) {
        this.syllabusData = syllabusData;
    }

    public OHData getOfficeHoursData() {
        return officeHoursData;
    }

    public void setOfficeHoursData(OHData officeHoursData) {
        this.officeHoursData = officeHoursData;
    }

    public MeetingTimesData getMeetingTimesData() {
        return meetingTimesData;
    }

    public void setMeetingTimesData(MeetingTimesData meetingTimesData) {
        this.meetingTimesData = meetingTimesData;
    }

    public ScheduleData getScheduleData() {
        return scheduleData;
    }

    public void setScheduleData(ScheduleData scheduleData) {
        this.scheduleData = scheduleData;
    }
    
}
