package csg.workspace.controllers;

import djf.modules.AppGUIModule;
import djf.ui.dialogs.AppDialogsFacade;
import javafx.collections.ObservableList;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import csg.CSGApp;
import static csg.CSGPropertyType.OH_EMAIL_TEXT_FIELD;
import static csg.CSGPropertyType.OH_ENDTIME_COMBO_BOX;
import static csg.CSGPropertyType.OH_FOOLPROOF_SETTINGS;
import static csg.CSGPropertyType.OH_NAME_TEXT_FIELD;
import static csg.CSGPropertyType.OH_NO_TA_SELECTED_CONTENT;
import static csg.CSGPropertyType.OH_NO_TA_SELECTED_TITLE;
import static csg.CSGPropertyType.OH_OFFICE_HOURS_TABLE_VIEW;
import static csg.CSGPropertyType.OH_STARTTIME_COMBO_BOX;
import static csg.CSGPropertyType.OH_TAS_TABLE_VIEW;
import static csg.CSGPropertyType.OH_TA_EDIT_DIALOG;
import static csg.CSGPropertyType.SCH_LINK_TEXT_FIELD;
import static csg.CSGPropertyType.SCH_TITLE_TEXT_FIELD;
import static csg.CSGPropertyType.SCH_TOPIC_TEXT_FIELD;
import static csg.CSGPropertyType.SCH_TYPE_COMBO_BOX;
import static csg.CSGPropertyType.SITE_CSS_COMBO_BOX;
import static csg.CSGPropertyType.SITE_EMAIL_TEXT_FIELD;
import static csg.CSGPropertyType.SITE_EXPORT_LABEL;
import static csg.CSGPropertyType.SITE_HOME_CHECK_BOX;
import static csg.CSGPropertyType.SITE_HP_TEXT_FIELD;
import static csg.CSGPropertyType.SITE_HW_CHECK_BOX;
import static csg.CSGPropertyType.SITE_NAME_TEXT_FIELD;
import static csg.CSGPropertyType.SITE_ROOM_TEXT_FIELD;
import static csg.CSGPropertyType.SITE_SCHEDULE_CHECK_BOX;
import static csg.CSGPropertyType.SITE_SEMESTERS_COMBO_BOX;
import static csg.CSGPropertyType.SITE_SUBJECTNUM_COMBO_BOX;
import static csg.CSGPropertyType.SITE_SUBJECT_COMBO_BOX;
import static csg.CSGPropertyType.SITE_SYLLABUS_CHECK_BOX;
import static csg.CSGPropertyType.SITE_TITLE_TEXT_FIELD;
import static csg.CSGPropertyType.SITE_YEARS_COMBO_BOX;
import csg.data.CSGData;
import csg.data.MeetingTimesData;
import csg.data.OHData;
import csg.data.ScheduleData;
import csg.data.ScheduleItemPrototype;
import csg.data.SiteData;
import csg.data.SyllabusData;
import csg.data.TAType;
import csg.data.TeachingAssistantPrototype;
import csg.data.TimeSlot;
import csg.data.TimeSlot.DayOfWeek;
import csg.transactions.AddTA_Transaction;
import csg.transactions.CutTA_Transaction;
import csg.transactions.EditTA_Transaction;
import csg.transactions.RemoveTA_Transaction;
import csg.transactions.ToggleOfficeHours_Transaction;
import csg.transactions.UpdateTable_Transaction;
import csg.workspace.dialogs.TADialog;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

/**
 *
 * @author McKillaGorilla
 */
public class CSGController {

    CSGApp app;

    public CSGController(CSGApp initApp) {
        app = initApp;
    }

    public void processAddTA() {
        AppGUIModule gui = app.getGUIModule();
        TextField nameTF = (TextField) gui.getGUINode(OH_NAME_TEXT_FIELD);
        String name = nameTF.getText();
        TextField emailTF = (TextField) gui.getGUINode(OH_EMAIL_TEXT_FIELD);
        String email = emailTF.getText();
        CSGData d = (CSGData) app.getDataComponent();
        OHData data = d.getOfficeHoursData();
        TAType type = data.getSelectedType();
        if (data.isLegalNewTA(name, email)) {
            TeachingAssistantPrototype ta = new TeachingAssistantPrototype(name.trim(), email.trim(), type);
            AddTA_Transaction addTATransaction = new AddTA_Transaction(data, ta);
            app.processTransaction(addTATransaction);

            // NOW CLEAR THE TEXT FIELDS
            nameTF.setText("");
            emailTF.setText("");
            nameTF.requestFocus();
        }
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processVerifyTA() {

    }

    public void processToggleOfficeHours() {
        AppGUIModule gui = app.getGUIModule();
        TableView<TimeSlot> officeHoursTableView = (TableView) gui.getGUINode(OH_OFFICE_HOURS_TABLE_VIEW);
        ObservableList<TablePosition> selectedCells = officeHoursTableView.getSelectionModel().getSelectedCells();
        if (selectedCells.size() > 0) {
            TablePosition cell = selectedCells.get(0);
            int cellColumnNumber = cell.getColumn();
            CSGData d = (CSGData)app.getDataComponent();
            OHData data = d.getOfficeHoursData();
            if (data.isDayOfWeekColumn(cellColumnNumber)) {
                DayOfWeek dow = data.getColumnDayOfWeek(cellColumnNumber);
                TableView<TeachingAssistantPrototype> taTableView = (TableView)gui.getGUINode(OH_TAS_TABLE_VIEW);
                TeachingAssistantPrototype ta = taTableView.getSelectionModel().getSelectedItem();
                if (ta != null) {
                    TimeSlot timeSlot = officeHoursTableView.getSelectionModel().getSelectedItem();
                    ToggleOfficeHours_Transaction transaction = new ToggleOfficeHours_Transaction(data, timeSlot, dow, ta);
                    app.processTransaction(transaction);
                }
                else {
                    Stage window = app.getGUIModule().getWindow();
                    AppDialogsFacade.showMessageDialog(window, OH_NO_TA_SELECTED_TITLE, OH_NO_TA_SELECTED_CONTENT);
                }
            }
            int row = cell.getRow();
            cell.getTableView().refresh();
        }
    }

    public void processTypeTA() {
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processEditTA() {
        CSGData d = (CSGData)app.getDataComponent();
        OHData data = d.getOfficeHoursData();
        if (data.isTASelected()) {
            TeachingAssistantPrototype taToEdit = data.getSelectedTA();
            TADialog taDialog = (TADialog)app.getGUIModule().getDialog(OH_TA_EDIT_DIALOG);
            taDialog.showEditDialog(taToEdit);
            TeachingAssistantPrototype editTA = taDialog.getEditTA();
            if (editTA != null) {
                EditTA_Transaction transaction = new EditTA_Transaction(app, taToEdit, editTA.getName(), editTA.getEmail(), editTA.getType());
                app.processTransaction(transaction);
            }
        }
    }

    public void processSelectAllTAs() {
        CSGData d = (CSGData)app.getDataComponent();
        OHData data = d.getOfficeHoursData();
        data.selectTAs(TAType.All);
    }

    public void processSelectGradTAs() {
        CSGData d = (CSGData)app.getDataComponent();
        OHData data = d.getOfficeHoursData();
        data.selectTAs(TAType.Graduate);
    }

    public void processSelectUndergradTAs() {
        CSGData d = (CSGData)app.getDataComponent();
        OHData data = d.getOfficeHoursData();
        data.selectTAs(TAType.Undergraduate);
    }

    public void processSelectTA() {
        AppGUIModule gui = app.getGUIModule();
        TableView<TimeSlot> officeHoursTableView = (TableView) gui.getGUINode(OH_OFFICE_HOURS_TABLE_VIEW);
        officeHoursTableView.refresh();
    }

    public void processRemoveTA() {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        OHData data = d.getOfficeHoursData();
        TableView<TimeSlot> officeHoursTableView = (TableView) gui.getGUINode(OH_OFFICE_HOURS_TABLE_VIEW);
        TableView<TeachingAssistantPrototype> tasTableView = (TableView) gui.getGUINode(OH_TAS_TABLE_VIEW);
        if (data.isTASelected()) {
            TeachingAssistantPrototype taToRemove = data.getSelectedTA();
            HashMap<TimeSlot, ArrayList<DayOfWeek>> officeHours = data.getTATimeSlots(taToRemove);
            RemoveTA_Transaction transaction = new RemoveTA_Transaction(data, taToRemove);
            app.processTransaction(transaction);
            tasTableView.refresh();
            officeHoursTableView.refresh();
        }
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
        
    }

    public void processUpdateOHTable(int old, boolean isStartCB) {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        OHData data = d.getOfficeHoursData();
        TableView<TimeSlot> officeHoursTableView = (TableView) gui.getGUINode(OH_OFFICE_HOURS_TABLE_VIEW);
        ComboBox timeStart = (ComboBox)gui.getGUINode(OH_STARTTIME_COMBO_BOX);
        ComboBox timeEnd = (ComboBox)gui.getGUINode(OH_ENDTIME_COMBO_BOX);
        int ts = timeStart.getSelectionModel().getSelectedIndex();
        int te = timeEnd.getSelectionModel().getSelectedIndex();
        int oldts = 0;
        int oldte = 0;
        if(ts <= te){
            timeStart.setDisable(false);
            timeEnd.setDisable(false);
            if(isStartCB){
                oldts = old;
                oldte = te;
            }
            else{
                oldts = ts;
                oldte = old;
            }
            System.out.println(ts);
            System.out.println(te);
            System.out.println(oldts);
            System.out.println(oldte);
            UpdateTable_Transaction u = new UpdateTable_Transaction(app, data, ts, te, oldts, oldte);
            app.processTransaction(u);
            officeHoursTableView.refresh();
//            data.resetOHTable(ts, te, oldts, oldte);
        }
        else{
//            if(te < ts){
////                timeStart.setDisable(true);
//            }
//            else if(ts > te){
////                timeEnd.setDisable(true);
//            }
        }
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }
    
    public void processCourseName(){
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        SiteData data = d.getSiteData();
        ComboBox c = (ComboBox)gui.getGUINode(SITE_SUBJECT_COMBO_BOX);
        if(data.isValidComboBoxChoice(c)){
            data.setSelectedName((String)c.getSelectionModel().getSelectedItem());
        }
        
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processCourseNum() {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        SiteData data = d.getSiteData();
        ComboBox c = (ComboBox)gui.getGUINode(SITE_SUBJECTNUM_COMBO_BOX);
        if(data.isValidComboBoxChoice(c)){
            data.setSelectedNum((String)c.getSelectionModel().getSelectedItem());
        }
        
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processCourseSem() {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        SiteData data = d.getSiteData();
        ComboBox c = (ComboBox)gui.getGUINode(SITE_SEMESTERS_COMBO_BOX);
        if(data.isValidComboBoxChoice(c)){
            data.setSelectedSem((String)c.getSelectionModel().getSelectedItem());
        }
        
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processCourseYear() {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        SiteData data = d.getSiteData();
        ComboBox c = (ComboBox)gui.getGUINode(SITE_YEARS_COMBO_BOX);
        if(data.isValidComboBoxChoice(c)){
            data.setSelectedYear((String)c.getSelectionModel().getSelectedItem());
        }
        
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processCourseTitle() {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        SiteData data = d.getSiteData();
        TextField titleTF = (TextField)gui.getGUINode(SITE_TITLE_TEXT_FIELD);
        if(data.isValidTextFieldInput(titleTF)){
            if(titleTF.isFocused()){
                data.setTitle(titleTF.getText());
            }
        }
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processExportURL() {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        SiteData data = d.getSiteData();
        Label l = (Label)gui.getGUINode(SITE_EXPORT_LABEL);
        data.setExp(l.getText());
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processCheckedOptions() {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        SiteData data = d.getSiteData();
        CheckBox homeCB = (CheckBox)gui.getGUINode(SITE_HOME_CHECK_BOX);
        CheckBox syllabusCB = (CheckBox)gui.getGUINode(SITE_SYLLABUS_CHECK_BOX);
        CheckBox scheduleCB = (CheckBox)gui.getGUINode(SITE_SCHEDULE_CHECK_BOX);
        CheckBox hwCB = (CheckBox)gui.getGUINode(SITE_HW_CHECK_BOX);
        data.updatePagesOptions(homeCB, syllabusCB, scheduleCB, hwCB);
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processFviImgViewFile(String path) {
        CSGData d = (CSGData)app.getDataComponent();
        SiteData data = d.getSiteData();
        data.setFavUrl(path);
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processNavImgViewFile(String path) {
        CSGData d = (CSGData)app.getDataComponent();
        SiteData data = d.getSiteData();
        data.setNavUrl(path);
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processLeftImgViewFile(String path) {
        CSGData d = (CSGData)app.getDataComponent();
        SiteData data = d.getSiteData();
        data.setLeftUrl(path);
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processRightImgViewFile(String path) {
        CSGData d = (CSGData)app.getDataComponent();
        SiteData data = d.getSiteData();
        data.setRightUrl(path);
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processSiteCSS() {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        SiteData data = d.getSiteData();
        ComboBox c = (ComboBox)gui.getGUINode(SITE_CSS_COMBO_BOX);
        if(data.isValidComboBoxChoice(c)){
            if(c.isFocused()){
                data.setCSS((String)c.getSelectionModel().getSelectedItem());
            }
        }
        
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processInstructorName() {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        SiteData data = d.getSiteData();
        TextField titleTF = (TextField)gui.getGUINode(SITE_NAME_TEXT_FIELD);
        if(data.isValidTextFieldInput(titleTF)){
            data.setInstructorName(titleTF.getText());
        }
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processInstructorEmail() {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        SiteData data = d.getSiteData();
        TextField titleTF = (TextField)gui.getGUINode(SITE_EMAIL_TEXT_FIELD);
        if(data.isValidTextFieldInput(titleTF)){
            data.setInstructorEmail(titleTF.getText());
        }
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processInstructorRoom() {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        SiteData data = d.getSiteData();
        TextField titleTF = (TextField)gui.getGUINode(SITE_ROOM_TEXT_FIELD);
        if(data.isValidTextFieldInput(titleTF)){
            data.setInstructorRoom(titleTF.getText());
        }
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processInstructorHP() {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        SiteData data = d.getSiteData();
        TextField titleTF = (TextField)gui.getGUINode(SITE_HP_TEXT_FIELD);
        if(data.isValidTextFieldInput(titleTF)){
            data.setInstructorHP(titleTF.getText());
        }
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processInstructorHoursJSON(String text) {
        CSGData d = (CSGData)app.getDataComponent();
        SiteData data = d.getSiteData();
        data.setInstructorHoursJSON(text);
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processDescriptionJSON(String text) {
        CSGData d = (CSGData)app.getDataComponent();
        SyllabusData data = d.getSyllabusData();
        data.setDescriptionJSON(text);
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processTopicsJSON(String text) {
        CSGData d = (CSGData)app.getDataComponent();
        SyllabusData data = d.getSyllabusData();
        data.setTopicsJSON(text);
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processPrereqsJSON(String text) {
        CSGData d = (CSGData)app.getDataComponent();
        SyllabusData data = d.getSyllabusData();
        data.setPrereqJSON(text);
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processOutcomesJSON(String text) {
        CSGData d = (CSGData)app.getDataComponent();
        SyllabusData data = d.getSyllabusData();
        data.setOutcomesJSON(text);
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processTextbooksJSON(String text) {
        CSGData d = (CSGData)app.getDataComponent();
        SyllabusData data = d.getSyllabusData();
        data.setTextbooksJSON(text);
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processGCJSON(String text) {
        CSGData d = (CSGData)app.getDataComponent();
        SyllabusData data = d.getSyllabusData();
        data.setGcJSON(text);
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processGNJSON(String text) {
        CSGData d = (CSGData)app.getDataComponent();
        SyllabusData data = d.getSyllabusData();
        data.setGnJSON(text);
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processADJSON(String text) {
        CSGData d = (CSGData)app.getDataComponent();
        SyllabusData data = d.getSyllabusData();
        data.setAdJSON(text);
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processSAJSON(String text) {
        CSGData d = (CSGData)app.getDataComponent();
        SyllabusData data = d.getSyllabusData();
        data.setSaJSON(text);
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processAddLecture() {
        CSGData d = (CSGData)app.getDataComponent();
        MeetingTimesData data = d.getMeetingTimesData();
        data.addLecture();
    }

    public void processRemoveLecture() {
        
    }

    public void processAddRecitation() {
        CSGData d = (CSGData)app.getDataComponent();
        MeetingTimesData data = d.getMeetingTimesData();
        data.addRecitation();
    }

    public void processRemoveRectiation() {
        
    }

    public void processAddLab() {
        CSGData d = (CSGData)app.getDataComponent();
        MeetingTimesData data = d.getMeetingTimesData();
        data.addLab();
    }

    public void processRemoveLab() {
        
    }

    public void processStartDate(LocalDate date) {
        CSGData d = (CSGData)app.getDataComponent();
        ScheduleData data = d.getScheduleData();
        if(date.getDayOfWeek().getValue() == 1){
            data.setStartDate(date);      
        }
        else{
            Alert alert = new Alert(AlertType.ERROR, "Start date must happen on a Monday!", ButtonType.OK);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
                alert.close();
            }
        }
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processEndDate(LocalDate date) {
        CSGData d = (CSGData)app.getDataComponent();
        ScheduleData data = d.getScheduleData();
        if(date.getDayOfWeek().getValue() == 5){
            data.setEndDate(date);      
        }
        else{
            Alert alert = new Alert(AlertType.ERROR, "End date must happen on a Friday!", ButtonType.OK);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
                alert.close();
            }
        }
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processClearSelection() {
        
    }

    public void processAddEditSelection(LocalDate date) {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        ScheduleData data = d.getScheduleData();
        ComboBox type = (ComboBox)gui.getGUINode(SCH_TYPE_COMBO_BOX);
        TextField title = (TextField)gui.getGUINode(SCH_TITLE_TEXT_FIELD);
        TextField topic = (TextField)gui.getGUINode(SCH_TOPIC_TEXT_FIELD);
        TextField link = (TextField)gui.getGUINode(SCH_LINK_TEXT_FIELD);
        String typeOption = (String)type.getSelectionModel().getSelectedItem();
        String titleOption = title.getText();
        String topicOption = topic.getText();
        String linkOption = link.getText();
        if(date.getDayOfWeek().getValue() > 0 && date.getDayOfWeek().getValue() < 6){
            ScheduleItemPrototype s = new ScheduleItemPrototype(typeOption.trim(), date, titleOption.trim(), topicOption.trim(), linkOption.trim());
            data.addScheduleItem(s);
        }
        else{
            Alert alert = new Alert(AlertType.ERROR, "The event must happen on a Monday or Friday!", ButtonType.OK);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
                alert.close();
            }
        }
    }
    
}