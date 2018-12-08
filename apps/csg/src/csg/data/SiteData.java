package csg.data;

import djf.components.AppDataComponent;
import djf.modules.AppGUIModule;
import csg.CSGApp;
import static csg.CSGPropertyType.SITE_CSS_COMBO_BOX;
import static csg.CSGPropertyType.SITE_EXPORT_LABEL;
import static csg.CSGPropertyType.SITE_HOME_CHECK_BOX;
import static csg.CSGPropertyType.SITE_HW_CHECK_BOX;
import static csg.CSGPropertyType.SITE_SCHEDULE_CHECK_BOX;
import static csg.CSGPropertyType.SITE_SEMESTERS_COMBO_BOX;
import static csg.CSGPropertyType.SITE_SUBJECTNUM_COMBO_BOX;
import static csg.CSGPropertyType.SITE_SUBJECT_COMBO_BOX;
import static csg.CSGPropertyType.SITE_SYLLABUS_CHECK_BOX;
import static csg.CSGPropertyType.SITE_TITLE_TEXT_FIELD;
import static csg.CSGPropertyType.SITE_YEARS_COMBO_BOX;
import csg.workspace.CSGWorkspace;
import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
    
    public String getSelectedName() {
        return selectedName;
    }

    public void setSelectedName(String selectedName) {
        this.selectedName = selectedName;
        AppGUIModule gui = app.getGUIModule();
        ComboBox n = (ComboBox)gui.getGUINode(SITE_SUBJECT_COMBO_BOX);
        try{
            n.getSelectionModel().select(this.selectedName);
        }
        catch(Exception e){
            n.getItems().add(this.selectedName);
            n.getSelectionModel().select(this.selectedName);
        }
    }

    public String getSelectedNum() {
        return selectedNum;
    }

    public void setSelectedNum(String selectedNum) {
        this.selectedNum = selectedNum;
        AppGUIModule gui = app.getGUIModule();
        ComboBox n = (ComboBox)gui.getGUINode(SITE_SUBJECTNUM_COMBO_BOX);
        try{
            n.getSelectionModel().select(this.selectedNum);
        }
        catch(Exception e){
            n.getItems().add(this.selectedName);
            n.getSelectionModel().select(this.selectedNum);
        }
    }

    public String getSelectedSem() {
        return selectedSem;
    }

    public void setSelectedSem(String selectedSem) {
        this.selectedSem = selectedSem;
        AppGUIModule gui = app.getGUIModule();
        ComboBox n = (ComboBox)gui.getGUINode(SITE_SEMESTERS_COMBO_BOX);
        try{
            n.getSelectionModel().select(this.selectedSem);
        }
        catch(Exception e){
            n.getItems().add(this.selectedName);
            n.getSelectionModel().select(this.selectedSem);
        }
    }

    public String getSelectedYear() {
        return selectedYear;
    }

    public void setSelectedYear(String selectedYear) {
        this.selectedYear = selectedYear;
        AppGUIModule gui = app.getGUIModule();
        ComboBox n = (ComboBox)gui.getGUINode(SITE_YEARS_COMBO_BOX);
        try{
            n.getSelectionModel().select(this.selectedYear);
        }
        catch(Exception e){
            n.getItems().add(this.selectedName);
            n.getSelectionModel().select(this.selectedYear);
        }
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
        AppGUIModule gui = app.getGUIModule();
        Label n = (Label)gui.getGUINode(SITE_EXPORT_LABEL);
        n.setText(this.exp);
    }

    public ArrayList<String> getSelectedPageOptions() {
        return selectedPageOptions;
    }
    
    public String prepareExportUrlForSave(){
        setExp("./export/" + this.selectedName + "_" + this.selectedNum + "_" + this.selectedSem + "_" + this.selectedYear + "/public_html");
        return "./export/" + this.selectedName + "_" + this.selectedNum + "_" + this.selectedSem + "_" + this.selectedYear + "/public_html";
    }

    public void setSelectedPageOptions(ArrayList<String> selectedPageOptions) {
        this.selectedPageOptions = selectedPageOptions;
        AppGUIModule gui = app.getGUIModule();
        CheckBox courseHomeCB = (CheckBox)gui.getGUINode(SITE_HOME_CHECK_BOX);
        CheckBox courseSyllabusCB = (CheckBox)gui.getGUINode(SITE_SYLLABUS_CHECK_BOX);
        CheckBox courseScheduleCB = (CheckBox)gui.getGUINode(SITE_SCHEDULE_CHECK_BOX);
        CheckBox courseHWCB = (CheckBox)gui.getGUINode(SITE_HW_CHECK_BOX);
        for(int i = 0; i < selectedPageOptions.size(); i++){
            if(selectedPageOptions.get(i).equals("home")){
                courseHomeCB.setSelected(true);
            }
            else if(selectedPageOptions.get(i).equals("syllabus")){
                courseSyllabusCB.setSelected(true);
            }
            else if(selectedPageOptions.get(i).equals("schedule")){
                courseScheduleCB.setSelected(true);
            }
            else if(selectedPageOptions.get(i).equals("hw")){
                courseHWCB.setSelected(true);
            }
        }
    }

    public String getFavUrl() {
        return favUrl;
    }

    public void setFavUrl(String favUrl) {
        this.favUrl = favUrl;
        AppGUIModule gui = app.getGUIModule();
        CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
        ImageView imgView = workspace.getFviImgView();
        imgView.setImage(new Image("file:" + favUrl));
        imgView.setFitWidth(25);
        imgView.setFitHeight(25);
        imgView.setPreserveRatio(true);
        imgView.setSmooth(true);
        imgView.setCache(true);
    }

    public String getNavUrl() {
        return navUrl;
    }

    public void setNavUrl(String navUrl) {
        this.navUrl = navUrl;
        AppGUIModule gui = app.getGUIModule();
        CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
        ImageView imgView = workspace.getNavImgView();
        imgView.setImage(new Image("file:" + navUrl));
        imgView.setFitWidth(300);
        imgView.setFitHeight(25);
        imgView.setPreserveRatio(true);
        imgView.setSmooth(true);
        imgView.setCache(true);
    }

    public String getLeftUrl() {
        return leftUrl;
    }

    public void setLeftUrl(String leftUrl) {
        this.leftUrl = leftUrl;
        AppGUIModule gui = app.getGUIModule();
        CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
        ImageView imgView = workspace.getLeftImgView();
        imgView.setImage(new Image("file:" + leftUrl));
        imgView.setFitWidth(300);
        imgView.setFitHeight(25);
        imgView.setPreserveRatio(true);
        imgView.setSmooth(true);
        imgView.setCache(true);
    }

    public String getRightUrl() {
        return rightUrl;
    }

    public void setRightUrl(String rightUrl) {
        this.rightUrl = rightUrl;
        AppGUIModule gui = app.getGUIModule();
        CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
        ImageView imgView = workspace.getRightImgView();
        imgView.setImage(new Image("file:" + rightUrl));
        imgView.setFitWidth(300);
        imgView.setFitHeight(25);
        imgView.setPreserveRatio(true);
        imgView.setSmooth(true);
        imgView.setCache(true);
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
        return this.css;
    }

    public void setCSS(String css) {
        this.css = "./work/css/" + css;
    }
    
    public boolean isValidComboBoxChoice(ComboBox c){
        c.requestFocus();
        if(c.getSelectionModel().getSelectedItem() == null || ((String)c.getSelectionModel().getSelectedItem()).trim().equals("")){
            return false;
        }
        return true;
    }
    
    public boolean isValidTextFieldInput(TextField tf){
        return !tf.getText().trim().equals("");
    }
    
    public boolean isLegalNewEmail(String email) {
        Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile(
                "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        if (matcher.find()) {
            return true;
        }
        else return false;
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