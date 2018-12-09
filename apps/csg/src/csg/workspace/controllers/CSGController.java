package csg.workspace.controllers;

import csg.transactions.SITE_EditHomeCheckBox_Transaction;
import djf.modules.AppGUIModule;
import djf.ui.dialogs.AppDialogsFacade;
import javafx.collections.ObservableList;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import csg.CSGApp;
import csg.CSGPropertyType;
import static csg.CSGPropertyType.MT_LAB_TABLE_VIEW;
import static csg.CSGPropertyType.MT_LECTURE_TABLE_VIEW;
import static csg.CSGPropertyType.MT_RECITATION_TABLE_VIEW;
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
import static csg.CSGPropertyType.SCH_ADD;
import static csg.CSGPropertyType.SCH_ADD_UPDATE_BUTTON;
import static csg.CSGPropertyType.SCH_ITEMS_TABLE_VIEW;
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
import csg.transactions.OH_AddTA_Transaction;
import csg.transactions.OH_CutTA_Transaction;
import csg.transactions.SYL_EditAcademicDishonestyTA_Transaction;
import csg.transactions.SITE_EditCourseCSSComboBox_Transaction;
import csg.transactions.SITE_EditCourseNumberComboBox_Transaction;
import csg.transactions.SITE_EditCourseSemesterComboBox_Transaction;
import csg.transactions.SITE_EditCourseSubjectComboBox_Transaction;
import csg.transactions.SITE_EditCourseTitleTF_Transaction;
import csg.transactions.SITE_EditCourseYearComboBox_Transaction;
import csg.transactions.SYL_EditDescriptionTA_Transaction;
import csg.transactions.SITE_EditFviImg_Transaction;
import csg.transactions.SYL_EditGradingComponentsTA_Transaction;
import csg.transactions.SYL_EditGradingNoteTA_Transaction;
import csg.transactions.SITE_EditInstructorEmailTF_Transaction;
import csg.transactions.SITE_EditInstructorHPTF_Transaction;
import csg.transactions.SITE_EditInstructorNameTF_Transaction;
import csg.transactions.SITE_EditInstructorOHTA_Transaction;
import csg.transactions.SITE_EditInstructorRoomTF_Transaction;
import csg.transactions.SITE_EditLeftImg_Transaction;
import csg.transactions.SITE_EditNavImg_Transaction;
import csg.transactions.SYL_EditOutcomesTA_Transaction;
import csg.transactions.SYL_EditPrereqTA_Transaction;
import csg.transactions.SITE_EditRightImg_Transaction;
import csg.transactions.SYL_EditSpecialAssistanceTA_Transaction;
import csg.transactions.OH_EditTA_Transaction;
import csg.transactions.SYL_EditTextbooksTA_Transaction;
import csg.transactions.SYL_EditTopicsTA_Transaction;
import csg.transactions.MT_AddLab_Transaction;
import csg.transactions.MT_AddLecture_Transaction;
import csg.transactions.MT_AddRecitation_Transaction;
import csg.transactions.MT_LabEditDayTimeColumn_Transaction;
import csg.transactions.MT_LabEditRoomColumn_Transaction;
import csg.transactions.MT_LabEditSectionColumn_Transaction;
import csg.transactions.MT_LabEditTA1Column_Transaction;
import csg.transactions.MT_LabEditTA2Column_Transaction;
import csg.transactions.MT_LecEditDayColumn_Transaction;
import csg.transactions.MT_LecEditRoomColumn_Transaction;
import csg.transactions.MT_LecEditSectionColumn_Transaction;
import csg.transactions.MT_LecEditTimeColumn_Transaction;
import csg.transactions.MT_RecEditDayTimeColumn_Transaction;
import csg.transactions.MT_RecEditRoomColumn_Transaction;
import csg.transactions.MT_RecEditSectionColumn_Transaction;
import csg.transactions.MT_RecEditTA1Column_Transaction;
import csg.transactions.MT_RecEditTA2Column_Transaction;
import csg.transactions.MT_RemoveLab_Transaction;
import csg.transactions.MT_RemoveLecture_Transaction;
import csg.transactions.MT_RemoveRecitation_Transaction;
import csg.transactions.OH_RemoveTA_Transaction;
import csg.transactions.SCH_AddItem_Transaction;
import csg.transactions.SCH_EditItem_Transaction;
import csg.transactions.SCH_SetEndDate_Transaction;
import csg.transactions.SCH_SetStartDate_Transaction;
import csg.transactions.OH_ToggleOfficeHours_Transaction;
import csg.transactions.OH_UpdateOHTable_Transaction;
import csg.transactions.SCH_RemoveItem_Transaction;
import csg.transactions.SITE_EditHWCheckBox_Transaction;
import csg.transactions.SITE_EditScheduleCheckBox_Transaction;
import csg.transactions.SITE_EditSyllabusCheckBox_Transaction;
import csg.workspace.CSGWorkspace;
import csg.workspace.dialogs.TADialog;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.image.ImageView;
import properties_manager.PropertiesManager;

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
            OH_AddTA_Transaction addTATransaction = new OH_AddTA_Transaction(data, ta);
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
                    OH_ToggleOfficeHours_Transaction transaction = new OH_ToggleOfficeHours_Transaction(data, timeSlot, dow, ta);
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
                OH_EditTA_Transaction transaction = new OH_EditTA_Transaction(app, taToEdit, editTA.getName(), editTA.getEmail(), editTA.getType());
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
            OH_RemoveTA_Transaction transaction = new OH_RemoveTA_Transaction(data, taToRemove);
            app.processTransaction(transaction);
            tasTableView.refresh();
            officeHoursTableView.refresh();
        }
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
        
    }

     public void processUpdateOHTable() {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        OHData data = d.getOfficeHoursData();
        ComboBox timeStart = (ComboBox)gui.getGUINode(OH_STARTTIME_COMBO_BOX);
        ComboBox timeEnd = (ComboBox)gui.getGUINode(OH_ENDTIME_COMBO_BOX);
        OH_UpdateOHTable_Transaction u = new OH_UpdateOHTable_Transaction(app, data, timeStart, timeEnd);
        app.processTransaction(u);
//        officeHoursTableView.refresh();
//            data.resetOHTable(ts, te, oldts, oldte);
       
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }
    
    public void processCourseName(){
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        SiteData data = d.getSiteData();
        ComboBox c = (ComboBox)gui.getGUINode(SITE_SUBJECT_COMBO_BOX);
        if(!data.getSelectedName().equals((String)c.getSelectionModel().getSelectedItem()) && data.isValidComboBoxChoice(c)){
//            data.setSelectedName((String)c.getSelectionModel().getSelectedItem());
            SITE_EditCourseSubjectComboBox_Transaction e = new SITE_EditCourseSubjectComboBox_Transaction(app, d, data, c);
            app.processTransaction(e);
        }
        
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }
    
//    public void processCourseName(String oldOption, String newOption) {
//        AppGUIModule gui = app.getGUIModule();
//        CSGData d = (CSGData)app.getDataComponent();
//        SiteData data = d.getSiteData();
//        ComboBox c = (ComboBox)gui.getGUINode(SITE_SUBJECT_COMBO_BOX);
//        if(data.isValidComboBoxChoice(c)){
////            data.setSelectedName((String)c.getSelectionModel().getSelectedItem());
////            SITE_EditCourseSubjectComboBox_Transaction e = new SITE_EditCourseSubjectComboBox_Transaction(app, d, data, c, oldOption, newOption);
////            app.processTransaction(e);
//        }
//        
//        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
//    }

    public void processCourseNum() {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        SiteData data = d.getSiteData();
        ComboBox c = (ComboBox)gui.getGUINode(SITE_SUBJECTNUM_COMBO_BOX);
        if(!data.getSelectedNum().equals((String)c.getSelectionModel().getSelectedItem()) && data.isValidComboBoxChoice(c)){
//            data.setSelectedNum((String)c.getSelectionModel().getSelectedItem());
            SITE_EditCourseNumberComboBox_Transaction e = new SITE_EditCourseNumberComboBox_Transaction(app, d, data, c);
            app.processTransaction(e);
        }
        
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processCourseSem() {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        SiteData data = d.getSiteData();
        ComboBox c = (ComboBox)gui.getGUINode(SITE_SEMESTERS_COMBO_BOX);
        if(!data.getSelectedSem().equals((String)c.getSelectionModel().getSelectedItem()) && data.isValidComboBoxChoice(c)){
//            data.setSelectedSem((String)c.getSelectionModel().getSelectedItem());
            SITE_EditCourseSemesterComboBox_Transaction e = new SITE_EditCourseSemesterComboBox_Transaction(app, d, data, c);
            app.processTransaction(e);
        }
        
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processCourseYear() {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        SiteData data = d.getSiteData();
        ComboBox c = (ComboBox)gui.getGUINode(SITE_YEARS_COMBO_BOX);
        if(!data.getSelectedYear().equals((String)c.getSelectionModel().getSelectedItem()) && data.isValidComboBoxChoice(c)){
//            data.setSelectedYear((String)c.getSelectionModel().getSelectedItem());
            SITE_EditCourseYearComboBox_Transaction e = new SITE_EditCourseYearComboBox_Transaction(app, d, data, c);
            app.processTransaction(e);
        }
        
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processCourseTitle() {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        SiteData data = d.getSiteData();
        TextField titleTF = (TextField)gui.getGUINode(SITE_TITLE_TEXT_FIELD);
        if(data.isValidTextFieldInput(titleTF)){
            if(!titleTF.isFocused()){
//                data.setTitle(titleTF.getText());
                SITE_EditCourseTitleTF_Transaction e = new SITE_EditCourseTitleTF_Transaction(app, d, data, titleTF);
                app.processTransaction(e);
            }
        }
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processExportURL() {
//        AppGUIModule gui = app.getGUIModule();
//        CSGData d = (CSGData)app.getDataComponent();
//        SiteData data = d.getSiteData();
//        Label l = (Label)gui.getGUINode(SITE_EXPORT_LABEL);
//        data.setExp(l.getText());
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processHomeCheckBox() {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        SiteData data = d.getSiteData();
        CheckBox homeCB = (CheckBox)gui.getGUINode(SITE_HOME_CHECK_BOX);
        CheckBox syllabusCB = (CheckBox)gui.getGUINode(SITE_SYLLABUS_CHECK_BOX);
        CheckBox scheduleCB = (CheckBox)gui.getGUINode(SITE_SCHEDULE_CHECK_BOX);
        CheckBox hwCB = (CheckBox)gui.getGUINode(SITE_HW_CHECK_BOX);
        if(!homeCB.isFocused()){
            SITE_EditHomeCheckBox_Transaction e = new SITE_EditHomeCheckBox_Transaction(app, d, data, homeCB, syllabusCB, scheduleCB, hwCB);
            app.processTransaction(e);
            
        }
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }
    
    public void processSyllabusCheckBox() {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        SiteData data = d.getSiteData();
        CheckBox homeCB = (CheckBox)gui.getGUINode(SITE_HOME_CHECK_BOX);
        CheckBox syllabusCB = (CheckBox)gui.getGUINode(SITE_SYLLABUS_CHECK_BOX);
        CheckBox scheduleCB = (CheckBox)gui.getGUINode(SITE_SCHEDULE_CHECK_BOX);
        CheckBox hwCB = (CheckBox)gui.getGUINode(SITE_HW_CHECK_BOX);
        if(!syllabusCB.isFocused()){
            SITE_EditSyllabusCheckBox_Transaction e = new SITE_EditSyllabusCheckBox_Transaction(app, d, data, homeCB, syllabusCB, scheduleCB, hwCB);
            app.processTransaction(e);
            
        }
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }
    
    public void processScheduleCheckBox() {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        SiteData data = d.getSiteData();
        CheckBox homeCB = (CheckBox)gui.getGUINode(SITE_HOME_CHECK_BOX);
        CheckBox syllabusCB = (CheckBox)gui.getGUINode(SITE_SYLLABUS_CHECK_BOX);
        CheckBox scheduleCB = (CheckBox)gui.getGUINode(SITE_SCHEDULE_CHECK_BOX);
        CheckBox hwCB = (CheckBox)gui.getGUINode(SITE_HW_CHECK_BOX);
        if(!scheduleCB.isFocused()){
            SITE_EditScheduleCheckBox_Transaction e = new SITE_EditScheduleCheckBox_Transaction(app, d, data, homeCB, syllabusCB, scheduleCB, hwCB);
            app.processTransaction(e);
            
        }
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }
    
    public void processHWCheckBox() {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        SiteData data = d.getSiteData();
        CheckBox homeCB = (CheckBox)gui.getGUINode(SITE_HOME_CHECK_BOX);
        CheckBox syllabusCB = (CheckBox)gui.getGUINode(SITE_SYLLABUS_CHECK_BOX);
        CheckBox scheduleCB = (CheckBox)gui.getGUINode(SITE_SCHEDULE_CHECK_BOX);
        CheckBox hwCB = (CheckBox)gui.getGUINode(SITE_HW_CHECK_BOX);
        if(!hwCB.isFocused()){
            SITE_EditHWCheckBox_Transaction e = new SITE_EditHWCheckBox_Transaction(app, d, data, homeCB, syllabusCB, scheduleCB, hwCB);
            app.processTransaction(e);
            
        }
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processFviImgViewFile(String path) {
        CSGData d = (CSGData)app.getDataComponent();
        SiteData data = d.getSiteData();
        SITE_EditFviImg_Transaction e = new SITE_EditFviImg_Transaction(app, d, data, path);
        app.processTransaction(e);
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processNavImgViewFile(String path) {
        CSGData d = (CSGData)app.getDataComponent();
        SiteData data = d.getSiteData();
        SITE_EditNavImg_Transaction e = new SITE_EditNavImg_Transaction(app, d, data, path);
        app.processTransaction(e);
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processLeftImgViewFile(String path) {
        CSGData d = (CSGData)app.getDataComponent();
        SiteData data = d.getSiteData();
        SITE_EditLeftImg_Transaction e = new SITE_EditLeftImg_Transaction(app, d, data, path);
        app.processTransaction(e);
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processRightImgViewFile(String path) {
        CSGData d = (CSGData)app.getDataComponent();
        SiteData data = d.getSiteData();
        SITE_EditRightImg_Transaction e = new SITE_EditRightImg_Transaction(app, d, data, path);
        app.processTransaction(e);
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processSiteCSS() {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        SiteData data = d.getSiteData();
        ComboBox c = (ComboBox)gui.getGUINode(SITE_CSS_COMBO_BOX);
        if(!data.getCSS().substring(data.getCSS().lastIndexOf("/") + 1).equals((String)c.getSelectionModel().getSelectedItem()) && data.isValidComboBoxChoice(c)){
            if(c.isFocused()){
//                data.setCSS((String)c.getSelectionModel().getSelectedItem());
                SITE_EditCourseCSSComboBox_Transaction e = new SITE_EditCourseCSSComboBox_Transaction(app, d, data, c);
                app.processTransaction(e);
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
            if(!titleTF.isFocused()){
    //            data.setInstructorName(titleTF.getText());
                SITE_EditInstructorNameTF_Transaction e = new SITE_EditInstructorNameTF_Transaction(app, d, data, titleTF);
                app.processTransaction(e);               
            }
        }
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processInstructorEmail() {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        SiteData data = d.getSiteData();
        TextField titleTF = (TextField)gui.getGUINode(SITE_EMAIL_TEXT_FIELD);
        if(data.isValidTextFieldInput(titleTF)){
            if(!titleTF.isFocused()){
    //            data.setInstructorEmail(titleTF.getText());
                SITE_EditInstructorEmailTF_Transaction e = new SITE_EditInstructorEmailTF_Transaction(app, d, data, titleTF);
                app.processTransaction(e);            
            }
        }
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processInstructorRoom() {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        SiteData data = d.getSiteData();
        TextField titleTF = (TextField)gui.getGUINode(SITE_ROOM_TEXT_FIELD);
        if(data.isValidTextFieldInput(titleTF)){
            if(!titleTF.isFocused()){
    //            data.setInstructorRoom(titleTF.getText());
                SITE_EditInstructorRoomTF_Transaction e = new SITE_EditInstructorRoomTF_Transaction(app, d, data, titleTF);
                app.processTransaction(e);
                
            }
        }
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processInstructorHP() {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        SiteData data = d.getSiteData();
        TextField titleTF = (TextField)gui.getGUINode(SITE_HP_TEXT_FIELD);
        if(data.isValidTextFieldInput(titleTF)){
            if(!titleTF.isFocused()){
    //            data.setInstructorHP(titleTF.getText());
                SITE_EditInstructorHPTF_Transaction e = new SITE_EditInstructorHPTF_Transaction(app, d, data, titleTF);
                app.processTransaction(e);
                
            }
        }
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processInstructorHoursJSON() {
        CSGData d = (CSGData)app.getDataComponent();
        CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
        SiteData data = d.getSiteData();
        if(!workspace.getInstructorOHJsonArea().isFocused()){
//            data.setInstructorHoursJSON(text);
            SITE_EditInstructorOHTA_Transaction e = new SITE_EditInstructorOHTA_Transaction(app, workspace, data, workspace.getInstructorOHJsonArea());
            app.processTransaction(e);
        }
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processDescriptionJSON() {
        CSGData d = (CSGData)app.getDataComponent();
        CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
        SyllabusData data = d.getSyllabusData();
        if(!workspace.getDescTA().isFocused()){
//            data.setDescriptionJSON(text);
            SYL_EditDescriptionTA_Transaction e = new SYL_EditDescriptionTA_Transaction(app, workspace, data, workspace.getDescTA());
            app.processTransaction(e);
        }
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processTopicsJSON() {
        CSGData d = (CSGData)app.getDataComponent();
        CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
        SyllabusData data = d.getSyllabusData();
        if(!workspace.getTopicTA().isFocused()){
//            data.setTopicsJSON(text);
            SYL_EditTopicsTA_Transaction e = new SYL_EditTopicsTA_Transaction(app, workspace, data, workspace.getTopicTA());
            app.processTransaction(e);
        }
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processPrereqsJSON() {
        CSGData d = (CSGData)app.getDataComponent();
        CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
        SyllabusData data = d.getSyllabusData();
        if(!workspace.getPrereqTA().isFocused()){
//            data.setPrereqJSON(text);
            SYL_EditPrereqTA_Transaction e = new SYL_EditPrereqTA_Transaction(app, workspace, data, workspace.getPrereqTA());
            app.processTransaction(e);
        }
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processOutcomesJSON() {
        CSGData d = (CSGData)app.getDataComponent();
        CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
        SyllabusData data = d.getSyllabusData();
        if(!workspace.getOutcomesTA().isFocused()){
//            data.setOutcomesJSON(text);
            SYL_EditOutcomesTA_Transaction e = new SYL_EditOutcomesTA_Transaction(app, workspace, data, workspace.getOutcomesTA());
            app.processTransaction(e);
        }
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processTextbooksJSON() {
        CSGData d = (CSGData)app.getDataComponent();
        CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
        SyllabusData data = d.getSyllabusData();
        if(!workspace.getTextbooksTA().isFocused()){
//            data.setTextbooksJSON(text);
            SYL_EditTextbooksTA_Transaction e = new SYL_EditTextbooksTA_Transaction(app, workspace, data, workspace.getTextbooksTA());
            app.processTransaction(e);
        }
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processGCJSON() {
        CSGData d = (CSGData)app.getDataComponent();
        CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
        SyllabusData data = d.getSyllabusData();
        if(!workspace.getGcTA().isFocused()){
//            data.setGcJSON(text);
            SYL_EditGradingComponentsTA_Transaction e = new SYL_EditGradingComponentsTA_Transaction(app, workspace, data, workspace.getGcTA());
            app.processTransaction(e);
        }
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processGNJSON() {
        CSGData d = (CSGData)app.getDataComponent();
        CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
        SyllabusData data = d.getSyllabusData();
        if(!workspace.getGradingNoteTA().isFocused()){
//            data.setGnJSON(text);
            SYL_EditGradingNoteTA_Transaction e = new SYL_EditGradingNoteTA_Transaction(app, workspace, data, workspace.getGradingNoteTA());
            app.processTransaction(e);
        }
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processADJSON() {
        CSGData d = (CSGData)app.getDataComponent();
        CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
        SyllabusData data = d.getSyllabusData();
        if(!workspace.getAdTA().isFocused()){
//            data.setAdJSON(text);
            SYL_EditAcademicDishonestyTA_Transaction e = new SYL_EditAcademicDishonestyTA_Transaction(app, workspace, data, workspace.getAdTA());
            app.processTransaction(e);
        }
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processSAJSON() {
        CSGData d = (CSGData)app.getDataComponent();
        CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
        SyllabusData data = d.getSyllabusData();
        if(!workspace.getSaTA().isFocused()){
//            data.setSaJSON(text);
            SYL_EditSpecialAssistanceTA_Transaction e = new SYL_EditSpecialAssistanceTA_Transaction(app, workspace, data, workspace.getSaTA());
            app.processTransaction(e);
        }
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processAddLecture() {
        CSGData d = (CSGData)app.getDataComponent();
        MeetingTimesData data = d.getMeetingTimesData();
        LecturePrototype l = new LecturePrototype("?", "?", "?", "?");
        MT_AddLecture_Transaction e = new MT_AddLecture_Transaction(app, d, data, l);
        app.processTransaction(e);
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }
    
    public void processRemoveLecture() {
        CSGData d = (CSGData)app.getDataComponent();
        MeetingTimesData data = d.getMeetingTimesData();
        if(((TableView)app.getGUIModule().getGUINode(MT_LECTURE_TABLE_VIEW)).getSelectionModel().getSelectedItem() != null){
            MT_RemoveLecture_Transaction e = new MT_RemoveLecture_Transaction(app, d, data, (LecturePrototype)((TableView)app.getGUIModule().getGUINode(MT_LECTURE_TABLE_VIEW)).getSelectionModel().getSelectedItem());
            app.processTransaction(e);
        }
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }
    
    public void processLectureTableSectionEdit(String oldValue, String newValue) {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        MeetingTimesData data = d.getMeetingTimesData();
        TableView lecTable = (TableView)gui.getGUINode(MT_LECTURE_TABLE_VIEW);
        LecturePrototype l = (LecturePrototype)lecTable.getSelectionModel().getSelectedItem();
        MT_LecEditSectionColumn_Transaction e = new MT_LecEditSectionColumn_Transaction(app, d, data, l, oldValue, newValue);
        app.processTransaction(e);
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }
    
    public void processLectureTableDayEdit(String oldValue, String newValue) {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        MeetingTimesData data = d.getMeetingTimesData();
        TableView lecTable = (TableView)gui.getGUINode(MT_LECTURE_TABLE_VIEW);
        LecturePrototype l = (LecturePrototype)lecTable.getSelectionModel().getSelectedItem();
        MT_LecEditDayColumn_Transaction e = new MT_LecEditDayColumn_Transaction(app, d, data, l, oldValue, newValue);
        app.processTransaction(e);
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processLectureTableTimeEdit(String oldValue, String newValue) {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        MeetingTimesData data = d.getMeetingTimesData();
        TableView lecTable = (TableView)gui.getGUINode(MT_LECTURE_TABLE_VIEW);
        LecturePrototype l = (LecturePrototype)lecTable.getSelectionModel().getSelectedItem();
        MT_LecEditTimeColumn_Transaction e = new MT_LecEditTimeColumn_Transaction(app, d, data, l, oldValue, newValue);
        app.processTransaction(e);
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processLectureTableRoomEdit(String oldValue, String newValue) {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        MeetingTimesData data = d.getMeetingTimesData();
        TableView lecTable = (TableView)gui.getGUINode(MT_LECTURE_TABLE_VIEW);
        LecturePrototype l = (LecturePrototype)lecTable.getSelectionModel().getSelectedItem();
        MT_LecEditRoomColumn_Transaction e = new MT_LecEditRoomColumn_Transaction(app, d, data, l, oldValue, newValue);
        app.processTransaction(e);
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }
    
    public void processAddRecitation() {
        CSGData d = (CSGData)app.getDataComponent();
        MeetingTimesData data = d.getMeetingTimesData();
        RecitationPrototype r = new RecitationPrototype("?", "?", "?", "?", "?");
        MT_AddRecitation_Transaction e = new MT_AddRecitation_Transaction(app, d, data, r);
        app.processTransaction(e);
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processRemoveRectiation() {
        CSGData d = (CSGData)app.getDataComponent();
        MeetingTimesData data = d.getMeetingTimesData();
        if(((TableView)app.getGUIModule().getGUINode(MT_RECITATION_TABLE_VIEW)).getSelectionModel().getSelectedItem() != null){
            MT_RemoveRecitation_Transaction e = new MT_RemoveRecitation_Transaction(app, d, data, (RecitationPrototype)((TableView)app.getGUIModule().getGUINode(MT_RECITATION_TABLE_VIEW)).getSelectionModel().getSelectedItem());
            app.processTransaction(e);
        }
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }
    
    public void processRecitationTableSectionEdit(String oldValue, String newValue) {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        MeetingTimesData data = d.getMeetingTimesData();
        TableView recTable = (TableView)gui.getGUINode(MT_RECITATION_TABLE_VIEW);
        RecitationPrototype r = (RecitationPrototype)recTable.getSelectionModel().getSelectedItem();
        MT_RecEditSectionColumn_Transaction e = new MT_RecEditSectionColumn_Transaction(app, d, data, r, oldValue, newValue);
        app.processTransaction(e);
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processRecitationTableDayTimeEdit(String oldValue, String newValue) {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        MeetingTimesData data = d.getMeetingTimesData();
        TableView recTable = (TableView)gui.getGUINode(MT_RECITATION_TABLE_VIEW);
        RecitationPrototype r = (RecitationPrototype)recTable.getSelectionModel().getSelectedItem();
        MT_RecEditDayTimeColumn_Transaction e = new MT_RecEditDayTimeColumn_Transaction(app, d, data, r, oldValue, newValue);
        app.processTransaction(e);
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processRecitationTableRoomEdit(String oldValue, String newValue) {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        MeetingTimesData data = d.getMeetingTimesData();
        TableView recTable = (TableView)gui.getGUINode(MT_RECITATION_TABLE_VIEW);
        RecitationPrototype r = (RecitationPrototype)recTable.getSelectionModel().getSelectedItem();
        MT_RecEditRoomColumn_Transaction e = new MT_RecEditRoomColumn_Transaction(app, d, data, r, oldValue, newValue);
        app.processTransaction(e);
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processRecitationTableTA1Edit(String oldValue, String newValue) {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        MeetingTimesData data = d.getMeetingTimesData();
        TableView recTable = (TableView)gui.getGUINode(MT_RECITATION_TABLE_VIEW);
        RecitationPrototype r = (RecitationPrototype)recTable.getSelectionModel().getSelectedItem();
        MT_RecEditTA1Column_Transaction e = new MT_RecEditTA1Column_Transaction(app, d, data, r, oldValue, newValue);
        app.processTransaction(e);
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processRecitationTableTA2Edit(String oldValue, String newValue) {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        MeetingTimesData data = d.getMeetingTimesData();
        TableView recTable = (TableView)gui.getGUINode(MT_RECITATION_TABLE_VIEW);
        RecitationPrototype r = (RecitationPrototype)recTable.getSelectionModel().getSelectedItem();
        MT_RecEditTA2Column_Transaction e = new MT_RecEditTA2Column_Transaction(app, d, data, r, oldValue, newValue);
        app.processTransaction(e);
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }
    
    public void processAddLab() {
        CSGData d = (CSGData)app.getDataComponent();
        MeetingTimesData data = d.getMeetingTimesData();
        LabPrototype l = new LabPrototype("?", "?", "?", "?", "?");
        MT_AddLab_Transaction e = new MT_AddLab_Transaction(app, d, data, l);
        app.processTransaction(e);
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processRemoveLab() {
        CSGData d = (CSGData)app.getDataComponent();
        MeetingTimesData data = d.getMeetingTimesData();
        if(((TableView)app.getGUIModule().getGUINode(MT_LAB_TABLE_VIEW)).getSelectionModel().getSelectedItem() != null){
            MT_RemoveLab_Transaction e = new MT_RemoveLab_Transaction(app, d, data, (LabPrototype)((TableView)app.getGUIModule().getGUINode(MT_LAB_TABLE_VIEW)).getSelectionModel().getSelectedItem());
            app.processTransaction(e);
        }
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }
    
    public void processLabTableSectionEdit(String oldValue, String newValue) {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        MeetingTimesData data = d.getMeetingTimesData();
        TableView labTable = (TableView)gui.getGUINode(MT_LAB_TABLE_VIEW);
        LabPrototype l = (LabPrototype)labTable.getSelectionModel().getSelectedItem();
        MT_LabEditSectionColumn_Transaction e = new MT_LabEditSectionColumn_Transaction(app, d, data, l, oldValue, newValue);
        app.processTransaction(e);
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processLabTableDayTimeEdit(String oldValue, String newValue) {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        MeetingTimesData data = d.getMeetingTimesData();
        TableView labTable = (TableView)gui.getGUINode(MT_LAB_TABLE_VIEW);
        LabPrototype l = (LabPrototype)labTable.getSelectionModel().getSelectedItem();
        MT_LabEditDayTimeColumn_Transaction e = new MT_LabEditDayTimeColumn_Transaction(app, d, data, l, oldValue, newValue);
        app.processTransaction(e);
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processLabTableRoomEdit(String oldValue, String newValue) {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        MeetingTimesData data = d.getMeetingTimesData();
        TableView labTable = (TableView)gui.getGUINode(MT_LAB_TABLE_VIEW);
        LabPrototype l = (LabPrototype)labTable.getSelectionModel().getSelectedItem();
        MT_LabEditRoomColumn_Transaction e = new MT_LabEditRoomColumn_Transaction(app, d, data, l, oldValue, newValue);
        app.processTransaction(e);
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processLabTableTA1Edit(String oldValue, String newValue) {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        MeetingTimesData data = d.getMeetingTimesData();
        TableView labTable = (TableView)gui.getGUINode(MT_LAB_TABLE_VIEW);
        LabPrototype l = (LabPrototype)labTable.getSelectionModel().getSelectedItem();
        MT_LabEditTA1Column_Transaction e = new MT_LabEditTA1Column_Transaction(app, d, data, l, oldValue, newValue);
        app.processTransaction(e);
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processLabTableTA2Edit(String oldValue, String newValue) {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        MeetingTimesData data = d.getMeetingTimesData();
        TableView labTable = (TableView)gui.getGUINode(MT_LAB_TABLE_VIEW);
        LabPrototype l = (LabPrototype)labTable.getSelectionModel().getSelectedItem();
        MT_LabEditTA2Column_Transaction e = new MT_LabEditTA2Column_Transaction(app, d, data, l, oldValue, newValue);
        app.processTransaction(e);
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processStartDate() {
        CSGData d = (CSGData)app.getDataComponent();
        ScheduleData data = d.getScheduleData();
        CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
        DatePicker start = workspace.getStartDate();
        try{
            if(!start.isFocused()){
                SCH_SetStartDate_Transaction e = new SCH_SetStartDate_Transaction(app, d, data, start);
                app.processTransaction(e);   
            }
                    
        }
        catch(NullPointerException n){
            data.setStartDate(null);
        }
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processEndDate() {
        CSGData d = (CSGData)app.getDataComponent();
        ScheduleData data = d.getScheduleData();
        CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
        DatePicker end = workspace.getEndDate();
        try{
            if(!end.isFocused()){
                SCH_SetEndDate_Transaction e = new SCH_SetEndDate_Transaction(app, d, data, end);
                app.processTransaction(e);
            }
        }
        catch(NullPointerException n){
            data.setEndDate(null);
        }
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }
    
    public void processClearSelection() {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
        ScheduleData data = d.getScheduleData();
        TableView<ScheduleItemPrototype> schTable = (TableView)gui.getGUINode(SCH_ITEMS_TABLE_VIEW);
        ComboBox type = (ComboBox)gui.getGUINode(SCH_TYPE_COMBO_BOX);
        DatePicker editPicker = workspace.getEditDatePicker();
        TextField title = (TextField)gui.getGUINode(SCH_TITLE_TEXT_FIELD);
        TextField topic = (TextField)gui.getGUINode(SCH_TOPIC_TEXT_FIELD);
        TextField link = (TextField)gui.getGUINode(SCH_LINK_TEXT_FIELD);
        
        schTable.getSelectionModel().clearSelection();
        type.getSelectionModel().selectFirst();
        editPicker.setValue(null);
        title.clear();
        topic.clear();
        link.clear();
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processAddEditButton() {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ScheduleData data = d.getScheduleData();
        TableView<ScheduleItemPrototype> schTable = (TableView)gui.getGUINode(SCH_ITEMS_TABLE_VIEW);
        ComboBox type = (ComboBox)gui.getGUINode(SCH_TYPE_COMBO_BOX);
        DatePicker editPicker = workspace.getEditDatePicker();
        TextField title = (TextField)gui.getGUINode(SCH_TITLE_TEXT_FIELD);
        TextField topic = (TextField)gui.getGUINode(SCH_TOPIC_TEXT_FIELD);
        TextField link = (TextField)gui.getGUINode(SCH_LINK_TEXT_FIELD);
        Button addEditButton = (Button)gui.getGUINode(SCH_ADD_UPDATE_BUTTON);
        String typeOption = "";
        String titleOption = "";
        String topicOption = "";
        String linkOption = "";
        if(addEditButton.getText().equals(props.getProperty(SCH_ADD))
//                && (title.getText().trim().equals("") || topic.getText().trim().equals("") || link.getText().trim().equals(""))
                ){
            typeOption = (String)type.getSelectionModel().getSelectedItem();
            titleOption = title.getText();
            topicOption = topic.getText();
            linkOption = link.getText();

            ScheduleItemPrototype s = new ScheduleItemPrototype(typeOption.trim(), editPicker.getValue(), topicOption.trim(), titleOption.trim(), linkOption.trim());
            SCH_AddItem_Transaction e = new SCH_AddItem_Transaction(app, d, data, s);
            app.processTransaction(e);
            
            type.getSelectionModel().selectFirst();
            editPicker.setValue(null);
            title.clear();
            topic.clear();
            link.clear();
            schTable.refresh();
            
        }
        else{
            typeOption = (String)type.getSelectionModel().getSelectedItem();
            titleOption = title.getText();
            topicOption = topic.getText();
            linkOption = link.getText();

            ScheduleItemPrototype s = new ScheduleItemPrototype(typeOption, editPicker.getValue(), topicOption.trim(), titleOption.trim(), linkOption.trim());
            SCH_EditItem_Transaction e = new SCH_EditItem_Transaction(app, d, data, schTable.getSelectionModel().getSelectedItem(), s);
            app.processTransaction(e);
            
            type.getSelectionModel().selectFirst();
            editPicker.setValue(null);
            title.clear();
            topic.clear();
            link.clear();
            schTable.refresh();

        }
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processAddEditSelection() {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
        ScheduleData data = d.getScheduleData();
        TableView<ScheduleItemPrototype> schTable = (TableView)gui.getGUINode(SCH_ITEMS_TABLE_VIEW);
        ComboBox type = (ComboBox)gui.getGUINode(SCH_TYPE_COMBO_BOX);
        DatePicker editPicker = workspace.getEditDatePicker();
        TextField title = (TextField)gui.getGUINode(SCH_TITLE_TEXT_FIELD);
        TextField topic = (TextField)gui.getGUINode(SCH_TOPIC_TEXT_FIELD);
        TextField link = (TextField)gui.getGUINode(SCH_LINK_TEXT_FIELD);
        
        for(int i = 0; i < type.getItems().size(); i++){
            try{
                if(type.getItems().get(i).equals(schTable.getSelectionModel().getSelectedItem().getType())){
                    type.getSelectionModel().select(i);
                    break;
                }
            }
            catch(NullPointerException n){
                
            }
        }
//        type.getSelectionModel().select(schTable.getSelectionModel().getSelectedItem().getType());
        try{
            String oldType = schTable.getSelectionModel().getSelectedItem().getType();
            title.setText(schTable.getSelectionModel().getSelectedItem().getTitle());
            String oldTitle = schTable.getSelectionModel().getSelectedItem().getTitle();
            topic.setText(schTable.getSelectionModel().getSelectedItem().getTopic());
            String oldTopic = schTable.getSelectionModel().getSelectedItem().getTopic();
            link.setText(schTable.getSelectionModel().getSelectedItem().getLink());
            String oldLink = schTable.getSelectionModel().getSelectedItem().getLink();
            editPicker.setValue(schTable.getSelectionModel().getSelectedItem().getLocalDate());
            LocalDate oldDate = schTable.getSelectionModel().getSelectedItem().getLocalDate();
        }
        catch(NullPointerException n){
            
        }
            
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processRemoveItem() {
        AppGUIModule gui = app.getGUIModule();
        CSGData d = (CSGData)app.getDataComponent();
        CSGWorkspace workspace = (CSGWorkspace)app.getWorkspaceComponent();
        ScheduleData data = d.getScheduleData();
        TableView<ScheduleItemPrototype> schTable = (TableView)gui.getGUINode(SCH_ITEMS_TABLE_VIEW);
        ScheduleItemPrototype s = schTable.getSelectionModel().getSelectedItem();
        if(schTable.getSelectionModel().getSelectedItem() != null){
            SCH_RemoveItem_Transaction e = new SCH_RemoveItem_Transaction(app, d, data, s);
            app.processTransaction(e);
            schTable.refresh();
            processClearSelection();
        }
    }

}