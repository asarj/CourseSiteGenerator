package csg.data;

import djf.components.AppDataComponent;
import djf.modules.AppGUIModule;
import csg.CSGApp;
import java.util.ArrayList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * This is the data component for TAManagerApp. It has all the data needed
 * to be set by the user via the User Interface and file I/O can set and get
 * all the data from this object
 * 
 * @author Ajay
 */
public class SiteData{

    // WE'LL NEED ACCESS TO THE APP TO NOTIFY THE GUI WHEN DATA CHANGES
    CSGApp app;
    
    private String selectedName;
    private String selectedNum;
    private String selectedSem;
    private String selectedYear;
    private String title;
    private String exp;
    
    private ArrayList<String> selectedPageOptions;
    
    private String favUrl;
    private String navUrl;
    private String leftUrl;
    private String rightUrl;
    private String css;
    
    private String instructorName;
    private String instructorEmail;
    private String instructorRoom;
    private String instructorHP;
    private String instructorHoursJSON;
    
    public SiteData(CSGApp initApp) {
        // KEEP THIS FOR LATER
        app = initApp;
        AppGUIModule gui = app.getGUIModule();
        this.selectedName = "";
        this.selectedNum = "";
        this.selectedSem = "";
        this.selectedYear = "";
        this.title = "";
        this.exp = "";
        
        this.selectedPageOptions = new ArrayList<>();
        
        this.favUrl = "";
        this.navUrl = "";
        this.leftUrl = "";
        this.rightUrl = "";
        this.css = "";
        
        this.instructorName = "";
        this.instructorEmail = "";
        this.instructorRoom = "";
        this.instructorHP = "";
        this.instructorHoursJSON = "";

    }
    
    public void reset() {
        this.selectedName = "";
        this.selectedNum = "";
        this.selectedSem = "";
        this.selectedYear = "";
        this.title = "";
        this.exp = "";

        this.selectedPageOptions.clear();

        this.favUrl = "";
        this.navUrl = "";
        this.leftUrl = "";
        this.rightUrl = "";

        this.instructorName = "";
        this.instructorEmail = "";
        this.instructorRoom = "";
        this.instructorHP = "";
        this.instructorHoursJSON = "";
       
    }
    
    public String getSelectedName() {
        return selectedName;
    }

    public void setSelectedName(String selectedName) {
        this.selectedName = selectedName;
    }

    public String getSelectedNum() {
        return selectedNum;
    }

    public void setSelectedNum(String selectedNum) {
        this.selectedNum = selectedNum;
    }

    public String getSelectedSem() {
        return selectedSem;
    }

    public void setSelectedSem(String selectedSem) {
        this.selectedSem = selectedSem;
    }

    public String getSelectedYear() {
        return selectedYear;
    }

    public void setSelectedYear(String selectedYear) {
        this.selectedYear = selectedYear;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public ArrayList<String> getSelectedPageOptions() {
        return selectedPageOptions;
    }

    public void setSelectedPageOptions(ArrayList<String> selectedPageOptions) {
        this.selectedPageOptions = selectedPageOptions;
    }

    public String getFavUrl() {
        return favUrl;
    }

    public void setFavUrl(String favUrl) {
        this.favUrl = favUrl;
    }

    public String getNavUrl() {
        return navUrl;
    }

    public void setNavUrl(String navUrl) {
        this.navUrl = navUrl;
    }

    public String getLeftUrl() {
        return leftUrl;
    }

    public void setLeftUrl(String leftUrl) {
        this.leftUrl = leftUrl;
    }

    public String getRightUrl() {
        return rightUrl;
    }

    public void setRightUrl(String rightUrl) {
        this.rightUrl = rightUrl;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String getInstructorEmail() {
        return instructorEmail;
    }

    public void setInstructorEmail(String instructorEmail) {
        this.instructorEmail = instructorEmail;
    }

    public String getInstructorRoom() {
        return instructorRoom;
    }

    public void setInstructorRoom(String instructorRoom) {
        this.instructorRoom = instructorRoom;
    }

    public String getInstructorHP() {
        return instructorHP;
    }

    public void setInstructorHP(String instructorHP) {
        this.instructorHP = instructorHP;
    }

    public String getInstructorHoursJSON() {
        return instructorHoursJSON;
    }

    public void setInstructorHoursJSON(String instructorHoursJSON) {
        this.instructorHoursJSON = instructorHoursJSON;
    }   
    
    public String getCSS() {
        return css;
    }

    public void setCSS(String css) {
        this.css = "/work/css/" + css;
    }
    
    public boolean isValidComboBoxChoice(ComboBox c){
        c.requestFocus();
        if(c.getSelectionModel().getSelectedItem() == null || ((String)c.getSelectionModel().getSelectedItem()).trim().equals("")){
            return false;
        }
        return true;
    }
    
    public boolean isValidTextFieldInput(TextField tf){
        return tf.getText().trim().equals("");
    }
    
    public void clearOptions(){
        this.selectedPageOptions.clear();
    }
    
    public void updatePagesOptions(CheckBox home, CheckBox sy, CheckBox sc, CheckBox h){
        clearOptions();
        if(home.isSelected()){
            this.selectedPageOptions.add("home");
        }
        if(sy.isSelected()){
            this.selectedPageOptions.add("syllabus");
        }
        if(sc.isSelected()){
            this.selectedPageOptions.add("schedule");
        }
        if(h.isSelected()){
            this.selectedPageOptions.add("hw");
        }
    }
    
}