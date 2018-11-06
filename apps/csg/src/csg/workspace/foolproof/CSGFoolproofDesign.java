package csg.workspace.foolproof;

import djf.modules.AppGUIModule;
import djf.ui.foolproof.FoolproofDesign;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import csg.CSGApp;
import static csg.CSGPropertyType.OH_ADD_TA_BUTTON;
import static csg.CSGPropertyType.OH_EMAIL_TEXT_FIELD;
import static csg.CSGPropertyType.OH_ENDTIME_COMBO_BOX;
import static csg.CSGPropertyType.OH_NAME_TEXT_FIELD;
import static csg.CSGPropertyType.OH_OFFICE_HOURS_TABLE_VIEW;
import static csg.CSGPropertyType.OH_REMOVE_TA_BUTTON;
import static csg.CSGPropertyType.OH_STARTTIME_COMBO_BOX;
import csg.data.CSGData;
import csg.data.TimeSlot;
import static csg.workspace.style.OHStyle.CLASS_OH_TEXT_FIELD;
import static csg.workspace.style.OHStyle.CLASS_OH_TEXT_FIELD_ERROR;
import java.awt.Color;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.layout.Background;

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
    }

    private void updateAddTAFoolproofDesign() {
        AppGUIModule gui = app.getGUIModule();
        
        // FOOLPROOF DESIGN STUFF FOR ADD TA BUTTON
        TextField nameTextField = ((TextField) gui.getGUINode(OH_NAME_TEXT_FIELD));
        TextField emailTextField = ((TextField) gui.getGUINode(OH_EMAIL_TEXT_FIELD));
        TableView<TimeSlot> officeHoursTableView = (TableView) gui.getGUINode(OH_OFFICE_HOURS_TABLE_VIEW);
        String name = nameTextField.getText();
        String email = emailTextField.getText();
        CSGData data = (CSGData) app.getDataComponent();
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
        CSGData data = (CSGData) app.getDataComponent();
        TableView<TimeSlot> officeHoursTableView = (TableView) gui.getGUINode(OH_OFFICE_HOURS_TABLE_VIEW);
        Button removeTAButton = (Button) gui.getGUINode(OH_REMOVE_TA_BUTTON);
        boolean isTypeSelected = data.isTATypeSelected();
        if (!isTypeSelected) {
            removeTAButton.setDisable(true);
            officeHoursTableView.refresh();
            return;
        } // A TYPE IS SELECTED SO WE'LL CONTINUE
        else {
            removeTAButton.setDisable(false);
            officeHoursTableView.refresh();
            return;
        }
    }

    private void updateOHTable() {
        AppGUIModule gui = app.getGUIModule();
        CSGData data = (CSGData)app.getDataComponent();
        ComboBox timeStart = (ComboBox)gui.getGUINode(OH_STARTTIME_COMBO_BOX);
        ComboBox timeEnd = (ComboBox)gui.getGUINode(OH_ENDTIME_COMBO_BOX);
        int ts = timeStart.getSelectionModel().getSelectedIndex();
        int te = timeEnd.getSelectionModel().getSelectedIndex();
        if(ts <= te){
            data.resetOfficeHours(ts, te);
        }
        else{
            Alert alert = new Alert(AlertType.ERROR, "Start Date cannot be greater than the end date!", ButtonType.OK);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
                alert.close();
            }
        }
    }
}
