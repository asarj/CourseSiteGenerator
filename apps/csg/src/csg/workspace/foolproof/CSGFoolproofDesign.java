package csg.workspace.foolproof;

import djf.modules.AppGUIModule;
import djf.ui.foolproof.FoolproofDesign;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import csg.CSGApp;
import static csg.CSGPropertyType.MT_LAB_REMOVE_BUTTON;
import static csg.CSGPropertyType.MT_LAB_TABLE_VIEW;
import static csg.CSGPropertyType.MT_LECTURE_REMOVE_BUTTON;
import static csg.CSGPropertyType.MT_LECTURE_TABLE_VIEW;
import static csg.CSGPropertyType.MT_RECITATION_REMOVE_BUTTON;
import static csg.CSGPropertyType.MT_RECITATION_TABLE_VIEW;
import static csg.CSGPropertyType.OH_ADD_TA_BUTTON;
import static csg.CSGPropertyType.OH_EMAIL_TEXT_FIELD;
import static csg.CSGPropertyType.OH_ENDTIME_COMBO_BOX;
import static csg.CSGPropertyType.OH_NAME_TEXT_FIELD;
import static csg.CSGPropertyType.OH_OFFICE_HOURS_TABLE_VIEW;
import static csg.CSGPropertyType.OH_REMOVE_TA_BUTTON;
import static csg.CSGPropertyType.OH_STARTTIME_COMBO_BOX;
import static csg.CSGPropertyType.OH_TAS_TABLE_VIEW;
import static csg.CSGPropertyType.SCH_ADD;
import static csg.CSGPropertyType.SCH_ADD_UPDATE_BUTTON;
import static csg.CSGPropertyType.SCH_CLEAR_BUTTON;
import static csg.CSGPropertyType.SCH_ITEMS_TABLE_VIEW;
import static csg.CSGPropertyType.SCH_REMOVE_ITEM_BUTTON;
import static csg.CSGPropertyType.SCH_UPDATE;
import static csg.CSGPropertyType.SITE_CSS_COMBO_BOX;
import static csg.CSGPropertyType.SITE_EMAIL_TEXT_FIELD;
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
import csg.data.TeachingAssistantPrototype;
import csg.data.TimeSlot;
import static csg.workspace.style.OHStyle.CLASS_OH_BOX;
import static csg.workspace.style.OHStyle.CLASS_OH_TEXT_FIELD;
import static csg.workspace.style.OHStyle.CLASS_OH_TEXT_FIELD_ERROR;
import static csg.workspace.style.OHStyle.CLASS_SITE_BOX_ERROR;
import java.awt.Color;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.layout.Background;
import properties_manager.PropertiesManager;

public class CSGFoolproofDesign implements FoolproofDesign {

    CSGApp app;

    public CSGFoolproofDesign(CSGApp initApp) {
        app = initApp;
    }

    @Override
    public void updateControls() {
        updateAddTAFoolproofDesign();
        updateEditTAFoolproofDesign();
        updateRemoveTAFoolproofDesign();
        updateOHTable();
        updateCourseNameCB();
        updateCourseNumCB();
        updateCourseSemCB();
        updateCourseYearCB();
        updateCourseTitleTF();
        updateInstructorNameTF();
        updateInstructorEmailTF();
        updateInstructorRoomTF();
        updateInstructorHPTF();
        updateCourseCSSCB();
        updateRemoveLectureFoolproofDesign();
        updateRemoveRecitationFoolproofDesign();
        updateRemoveLabFoolproofDesign();
        updateClearSelectionFoolproofDesign();
        updateAddEditButtonTextFoolproofDesign();
        updateRemoveScheduleItemFoolproofDesign();
    }

    private void updateAddTAFoolproofDesign() {
        AppGUIModule gui = app.getGUIModule();
        
        // FOOLPROOF DESIGN STUFF FOR ADD TA BUTTON
        TextField nameTextField = ((TextField) gui.getGUINode(OH_NAME_TEXT_FIELD));
        TextField emailTextField = ((TextField) gui.getGUINode(OH_EMAIL_TEXT_FIELD));
        TableView<TimeSlot> officeHoursTableView = (TableView) gui.getGUINode(OH_OFFICE_HOURS_TABLE_VIEW);
        String name = nameTextField.getText();
        String email = emailTextField.getText();
        CSGData d = (CSGData) app.getDataComponent();
        OHData data = d.getOfficeHoursData();
        Button addTAButton = (Button) gui.getGUINode(OH_ADD_TA_BUTTON);

        // FIRST, IF NO TYPE IS SELECTED WE'LL JUST DISABLE
        // THE CONTROLS AND BE DONE WITH IT
        boolean isTypeSelected = data.isTATypeSelected();
        if (!isTypeSelected) {
            nameTextField.setDisable(true);
            emailTextField.setDisable(true);
            addTAButton.setDisable(true);
            return;
        } // A TYPE IS SELECTED SO WE'LL CONTINUE
        else {
            nameTextField.setDisable(false);
            emailTextField.setDisable(false);
            addTAButton.setDisable(false);
        }

        // NOW, IS THE USER-ENTERED DATA GOOD?
        boolean isLegalNewTA = data.isLegalNewTA(name, email);

        // ENABLE/DISABLE THE CONTROLS APPROPRIATELY
        addTAButton.setDisable(!isLegalNewTA);
        if (isLegalNewTA) {
            nameTextField.setOnAction(addTAButton.getOnAction());
            emailTextField.setOnAction(addTAButton.getOnAction());
        } else {
            nameTextField.setOnAction(null);
            emailTextField.setOnAction(null);
        }

        // UPDATE THE CONTROL TEXT DISPLAY APPROPRIATELY
        boolean isLegalNewName = data.isLegalNewName(name);
        boolean isLegalNewEmail = data.isLegalNewEmail(email);
        foolproofTextField(nameTextField, isLegalNewName);
        foolproofTextField(emailTextField, isLegalNewEmail);
        officeHoursTableView.refresh();
    }
    
    private void updateEditTAFoolproofDesign() {
        
    }
    
    public void foolproofTextField(TextField textField, boolean hasLegalData) {
        if (hasLegalData) {
            textField.getStyleClass().remove(CLASS_OH_TEXT_FIELD_ERROR);
            if (!textField.getStyleClass().contains(CLASS_OH_TEXT_FIELD)) {
                textField.getStyleClass().add(CLASS_OH_TEXT_FIELD);
            }
        } else {
            textField.getStyleClass().remove(CLASS_OH_TEXT_FIELD);
            if (!textField.getStyleClass().contains(CLASS_OH_TEXT_FIELD_ERROR)) {
                textField.getStyleClass().add(CLASS_OH_TEXT_FIELD_ERROR);
            }
        }
    }

    private void updateRemoveTAFoolproofDesign() {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData) app.getDataComponent();
        OHData data = d.getOfficeHoursData();
        TableView<TimeSlot> officeHoursTableView = (TableView) gui.getGUINode(OH_OFFICE_HOURS_TABLE_VIEW);
        TableView<TeachingAssistantPrototype> tasTableView = (TableView) gui.getGUINode(OH_TAS_TABLE_VIEW);
        Button removeTAButton = (Button) gui.getGUINode(OH_REMOVE_TA_BUTTON);
        boolean isTypeSelected = data.isTASelected();
        if (!isTypeSelected) {
            removeTAButton.setDisable(true);
            tasTableView.refresh();
            officeHoursTableView.refresh();
            
        } // A TYPE IS SELECTED SO WE'LL CONTINUE
        else {
            removeTAButton.setDisable(false);
            tasTableView.refresh();
            officeHoursTableView.refresh();
            
        }
    }

    private void updateOHTable() {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        OHData data = d.getOfficeHoursData();
        ComboBox timeStart = (ComboBox)gui.getGUINode(OH_STARTTIME_COMBO_BOX);
        ComboBox timeEnd = (ComboBox)gui.getGUINode(OH_ENDTIME_COMBO_BOX);
        int ts = timeStart.getSelectionModel().getSelectedIndex();
        int te = timeEnd.getSelectionModel().getSelectedIndex();
        if(ts <= te){
            
        }
        else{
            if(te < ts){
                timeStart.setDisable(true);
            }
        }
    }
    private void updateCourseNameCB() {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        SiteData data = d.getSiteData();
        ComboBox c = (ComboBox)gui.getGUINode(SITE_SUBJECT_COMBO_BOX);
//        c.requestFocus();
        if(c.getSelectionModel().getSelectedItem() == null || ((String)c.getSelectionModel().getSelectedItem()).trim().equals("")){
            if (c.getStyleClass().contains(CLASS_OH_BOX)) {
                c.getStyleClass().remove(CLASS_OH_BOX);
                c.getStyleClass().add(CLASS_SITE_BOX_ERROR);
            }
        }
        else{
            if(c.getStyleClass().contains(CLASS_SITE_BOX_ERROR)){
                c.getStyleClass().remove(CLASS_SITE_BOX_ERROR);
                c.getStyleClass().add(CLASS_OH_BOX); 
            }
        }
    }
    
    private void updateCourseNumCB() {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        SiteData data = d.getSiteData();
        ComboBox c = (ComboBox)gui.getGUINode(SITE_SUBJECTNUM_COMBO_BOX);
//        c.requestFocus();
        if(c.getSelectionModel().getSelectedItem() == null || ((String)c.getSelectionModel().getSelectedItem()).trim().equals("")){
            if (c.getStyleClass().contains(CLASS_OH_BOX)) {
                c.getStyleClass().remove(CLASS_OH_BOX);
                c.getStyleClass().add(CLASS_SITE_BOX_ERROR);
            }
        }
        else{
            if(c.getStyleClass().contains(CLASS_SITE_BOX_ERROR)){
                c.getStyleClass().remove(CLASS_SITE_BOX_ERROR);
                c.getStyleClass().add(CLASS_OH_BOX); 
            }
        }
    }
    
    private void updateCourseSemCB() {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        SiteData data = d.getSiteData();
        ComboBox c = (ComboBox)gui.getGUINode(SITE_SEMESTERS_COMBO_BOX);
//        c.requestFocus();
        if(c.getSelectionModel().getSelectedItem() == null || ((String)c.getSelectionModel().getSelectedItem()).trim().equals("")){
            if (c.getStyleClass().contains(CLASS_OH_BOX)) {
                c.getStyleClass().remove(CLASS_OH_BOX);
                c.getStyleClass().add(CLASS_SITE_BOX_ERROR);
            }
        }
        else{
            if(c.getStyleClass().contains(CLASS_SITE_BOX_ERROR)){
                c.getStyleClass().remove(CLASS_SITE_BOX_ERROR);
                c.getStyleClass().add(CLASS_OH_BOX); 
            }
        }
    }
    
    private void updateCourseYearCB() {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        SiteData data = d.getSiteData();
        ComboBox c = (ComboBox)gui.getGUINode(SITE_YEARS_COMBO_BOX);
//        c.requestFocus();
        if(c.getSelectionModel().getSelectedItem() == null || ((String)c.getSelectionModel().getSelectedItem()).trim().equals("")){
            if (c.getStyleClass().contains(CLASS_OH_BOX)) {
                c.getStyleClass().remove(CLASS_OH_BOX);
                c.getStyleClass().add(CLASS_SITE_BOX_ERROR);
            }
        }
        else{
            if(c.getStyleClass().contains(CLASS_SITE_BOX_ERROR)){
                c.getStyleClass().remove(CLASS_SITE_BOX_ERROR);
                c.getStyleClass().add(CLASS_OH_BOX); 
            }
        }
    }
    
    private void updateCourseTitleTF() {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        SiteData data = d.getSiteData();
        TextField c = (TextField)gui.getGUINode(SITE_TITLE_TEXT_FIELD);
        boolean valid = data.isValidTextFieldInput(c);
//        c.requestFocus();
        foolproofTextField(c, valid);
    }
    
    private void updateInstructorNameTF() {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        SiteData data = d.getSiteData();
        TextField c = (TextField)gui.getGUINode(SITE_NAME_TEXT_FIELD);
        boolean valid = data.isValidTextFieldInput(c);
//        c.requestFocus();
        foolproofTextField(c, valid);
    }
    
    private void updateInstructorEmailTF() {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        SiteData data = d.getSiteData();
        TextField c = (TextField)gui.getGUINode(SITE_EMAIL_TEXT_FIELD);
        boolean valid = data.isValidTextFieldInput(c);
//        c.requestFocus();
        foolproofTextField(c, valid);
    }
    
    private void updateInstructorRoomTF() {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        SiteData data = d.getSiteData();
        TextField c = (TextField)gui.getGUINode(SITE_ROOM_TEXT_FIELD);
        boolean valid = data.isValidTextFieldInput(c);
//        c.requestFocus();
        foolproofTextField(c, valid);
    }
    
    private void updateInstructorHPTF() {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        SiteData data = d.getSiteData();
        TextField c = (TextField)gui.getGUINode(SITE_HP_TEXT_FIELD);
        boolean valid = data.isValidTextFieldInput(c);
//        c.requestFocus();
        foolproofTextField(c, valid);
    }
    
    private void updateCourseCSSCB() {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        SiteData data = d.getSiteData();
        ComboBox c = (ComboBox)gui.getGUINode(SITE_CSS_COMBO_BOX);
//        c.requestFocus();
        if(c.getSelectionModel().getSelectedItem() == null || ((String)c.getSelectionModel().getSelectedItem()).trim().equals("")){
            if (c.getStyleClass().contains(CLASS_OH_BOX)) {
                c.getStyleClass().remove(CLASS_OH_BOX);
                c.getStyleClass().add(CLASS_SITE_BOX_ERROR);
            }
        }
        else{
            if(c.getStyleClass().contains(CLASS_SITE_BOX_ERROR)){
                c.getStyleClass().remove(CLASS_SITE_BOX_ERROR);
                c.getStyleClass().add(CLASS_OH_BOX); 
            }
        }
    }
    
    private void updateRemoveLectureFoolproofDesign(){
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        MeetingTimesData data = d.getMeetingTimesData();
        TableView<LecturePrototype> lecTableView = (TableView) gui.getGUINode(MT_LECTURE_TABLE_VIEW);
        Button remove = (Button)gui.getGUINode(MT_LECTURE_REMOVE_BUTTON);
        boolean isLecSelected = lecTableView.getSelectionModel().getSelectedItem() != null;
        if(!isLecSelected){
            remove.setDisable(true);
        }
        else{
            remove.setDisable(false);
        }
    }
    
    private void updateRemoveRecitationFoolproofDesign(){
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        MeetingTimesData data = d.getMeetingTimesData();
        TableView<RecitationPrototype> recTableView = (TableView) gui.getGUINode(MT_RECITATION_TABLE_VIEW);
        Button remove = (Button)gui.getGUINode(MT_RECITATION_REMOVE_BUTTON);
        boolean isRecSelected = recTableView.getSelectionModel().getSelectedItem() != null;
        if(!isRecSelected){
            remove.setDisable(true);
        }
        else{
            remove.setDisable(false);
        }
    }
    
    private void updateRemoveLabFoolproofDesign(){
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        MeetingTimesData data = d.getMeetingTimesData();
        TableView<LabPrototype> labTableView = (TableView) gui.getGUINode(MT_LAB_TABLE_VIEW);
        Button remove = (Button)gui.getGUINode(MT_LAB_REMOVE_BUTTON);
        boolean isLabSelected = labTableView.getSelectionModel().getSelectedItem() != null;
        if(!isLabSelected){
            remove.setDisable(true);
        }
        else{
            remove.setDisable(false);
        }
    }
    
    private void updateRemoveScheduleItemFoolproofDesign(){
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        ScheduleData data = d.getScheduleData();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        TableView<ScheduleItemPrototype> schTableView = (TableView) gui.getGUINode(SCH_ITEMS_TABLE_VIEW);
        Button removeButton = (Button)gui.getGUINode(SCH_REMOVE_ITEM_BUTTON);
        boolean isItemSelected = schTableView.getSelectionModel().getSelectedItem() != null;
        if(!isItemSelected){
            removeButton.setDisable(true);
        }
        else{
            removeButton.setDisable(false);
        }
    }
    
    private void updateAddEditButtonTextFoolproofDesign(){
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        ScheduleData data = d.getScheduleData();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        TableView<ScheduleItemPrototype> schTableView = (TableView) gui.getGUINode(SCH_ITEMS_TABLE_VIEW);
        Button addEditButton = (Button)gui.getGUINode(SCH_ADD_UPDATE_BUTTON);
        boolean isItemSelected = schTableView.getSelectionModel().getSelectedItem() != null;
        if(!isItemSelected){
            addEditButton.setText(props.getProperty(SCH_ADD));
        }
        else{
            addEditButton.setText(props.getProperty(SCH_UPDATE));
        }
    }
    
    private void updateClearSelectionFoolproofDesign(){
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        ScheduleData data = d.getScheduleData();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        TableView<ScheduleItemPrototype> schTableView = (TableView) gui.getGUINode(SCH_ITEMS_TABLE_VIEW);
        Button clearButton = (Button)gui.getGUINode(SCH_CLEAR_BUTTON);
        boolean isItemSelected = schTableView.getSelectionModel().getSelectedItem() != null;
        if(!isItemSelected){
            clearButton.setDisable(true);
        }
        else{
            clearButton.setDisable(false);
        }
    }
    
}
