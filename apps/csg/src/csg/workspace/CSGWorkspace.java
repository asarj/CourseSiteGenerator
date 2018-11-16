package csg.workspace;

import djf.components.AppWorkspaceComponent;
import djf.modules.AppFoolproofModule;
import djf.modules.AppGUIModule;
import static djf.modules.AppGUIModule.ENABLED;
import djf.ui.AppNodesBuilder;
import static djf.AppPropertyType.APP_PATH_IMAGES;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import properties_manager.PropertiesManager;
import csg.CSGApp;
import csg.CSGPropertyType;
import static csg.CSGPropertyType.*;
import csg.data.Lab;
import csg.data.Lecture;
import csg.data.Recitation;
import csg.data.ScheduleItem;
import csg.data.TeachingAssistantPrototype;
import csg.data.TimeSlot;
import csg.workspace.controllers.CSGController;
import csg.workspace.dialogs.TADialog;
import csg.workspace.foolproof.CSGFoolproofDesign;
import static csg.workspace.style.OHStyle.*;
import static djf.AppPropertyType.APP_FILE_PROTOCOL;
import static djf.modules.AppLanguageModule.FILE_PROTOCOL;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javax.swing.JFileChooser;

/**
 *
 * @author McKillaGorilla
 */
public class CSGWorkspace extends AppWorkspaceComponent {
    ImageView fviImgView;
    ImageView navImgView;
    ImageView leftImgView;
    ImageView rightImgView;
    TextArea instructorOHJsonArea;
    
    public CSGWorkspace(CSGApp app) {
        super(app);

        // LAYOUT THE APP
        initLayout();

        // INIT THE EVENT HANDLERS
        initControllers();

        // 
        initFoolproofDesign();

        // INIT DIALOGS
        initDialogs();
    }

    private void initDialogs() {
        TADialog taDialog = new TADialog((CSGApp) app);
        app.getGUIModule().addDialog(OH_TA_EDIT_DIALOG, taDialog);
    }

    // THIS HELPER METHOD INITIALIZES ALL THE CONTROLS IN THE WORKSPACE
    private void initLayout() {
        // FIRST LOAD THE FONT FAMILIES FOR THE COMBO BOX
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // THIS WILL BUILD ALL OF OUR JavaFX COMPONENTS FOR US
        AppNodesBuilder tabBuilder = app.getGUIModule().getNodesBuilder();
        AppNodesBuilder siteBuilder = app.getGUIModule().getNodesBuilder();
        AppNodesBuilder syllabusBuilder = app.getGUIModule().getNodesBuilder();
        AppNodesBuilder mtBuilder = app.getGUIModule().getNodesBuilder();
        AppNodesBuilder ohBuilder = app.getGUIModule().getNodesBuilder();
        AppNodesBuilder schBuilder = app.getGUIModule().getNodesBuilder();
        
        
        //----------------------------------------------SETS UP THE TABS--------------------------------------------------//
        TabPane tabs = new TabPane();
        Tab siteTab = new Tab();
        Tab syllabusTab = new Tab();
        Tab mtTab = new Tab();
        Tab ohTab = new Tab();
        Tab scheduleTab = new Tab();
        
        siteTab.setText(props.getProperty(SITE_TAB_TEXT));
        syllabusTab.setText(props.getProperty(SYLLABUS_TAB_TEXT));
        mtTab.setText(props.getProperty(MT_TAB_TEXT));
        ohTab.setText(props.getProperty(OH_TAB_TEXT));
        scheduleTab.setText(props.getProperty(SCH_TAB_TEXT));
        
        siteTab.setClosable(false);
        syllabusTab.setClosable(false);
        mtTab.setClosable(false);
        ohTab.setClosable(false);
        scheduleTab.setClosable(false);
        
        tabs.getTabs().addAll(siteTab, syllabusTab, mtTab, ohTab, scheduleTab);
        VBox.setVgrow(tabs, Priority.ALWAYS);
        
        //-----------------------------------------------------------------------------------------------------------------//
        
        
        //----------------------------------------------SETS UP THE SITE TAB--------------------------------------------------//
        VBox siteContent= new VBox();
        ScrollPane siteTabScrollPane = new ScrollPane();
        VBox siteTabVBox = siteBuilder.buildVBox(SITE_MAIN_VBOX, null, CLASS_OH_PANE, ENABLED);
        ObservableList subjects = FXCollections.observableArrayList(props.getProperty(SITE_CSE_TEXT));
        ObservableList subjectNums = FXCollections.observableArrayList(props.getProperty(SITE_101_TEXT), 
                                                                        props.getProperty(SITE_114_TEXT), 
                                                                        props.getProperty(SITE_214_TEXT), 
                                                                        props.getProperty(SITE_215_TEXT), 
                                                                        props.getProperty(SITE_216_TEXT), 
                                                                        props.getProperty(SITE_219_TEXT));
        ObservableList semesters = FXCollections.observableArrayList(props.getProperty(SITE_FALL_TEXT), 
                                                                      props.getProperty(SITE_WINTER_TEXT), 
                                                                      props.getProperty(SITE_SPRING_TEXT), 
                                                                      props.getProperty(SITE_SUMMER_TEXT));
        ObservableList years = FXCollections.observableArrayList(Integer.toString(Calendar.getInstance().get(Calendar.YEAR)), Integer.toString(Calendar.getInstance().get(Calendar.YEAR) + 1));
        GridPane bannerBox = siteBuilder.buildGridPane(SITE_BANNERBOX_GRID_PANE, siteTabVBox, CLASS_OH_PANE, ENABLED);
        Label bannerLabel = siteBuilder.buildLabel(SITE_BANNER_LABEL, bannerBox, 0, 0, 1, 1, CLASS_OH_HEADER_LABEL, ENABLED);

        Label subjectLabel = siteBuilder.buildLabel(SITE_SUBJECT_LABEL, bannerBox, 0, 1, 1, 1, CLASS_REG_LABEL, ENABLED);
        ComboBox subjectCBox = siteBuilder.buildComboBox(SITE_SUBJECT_COMBO_BOX, bannerBox, 1, 1, 1, 1, CLASS_OH_BOX, ENABLED, subjects, subjects.get(0));
        

        Label subjectNumberLabel = siteBuilder.buildLabel(SITE_NUMBER_LABEL, bannerBox, 2, 1, 1, 1, CLASS_REG_LABEL, ENABLED);
        ComboBox subjectNumsCBox = siteBuilder.buildComboBox(SITE_SUBJECTNUM_COMBO_BOX, bannerBox, 3, 1, 1, 1, CLASS_OH_BOX, ENABLED, subjectNums, subjectNums.get(0));

        
        Label semesterLabel = siteBuilder.buildLabel(SITE_SEMESTER_LABEL, bannerBox, 0, 2, 1, 1, CLASS_REG_LABEL, ENABLED);
        ComboBox semestersCBox = siteBuilder.buildComboBox(SITE_SEMESTERS_COMBO_BOX, bannerBox, 1, 2, 1, 1, CLASS_OH_BOX, ENABLED, semesters, semesters.get(0));

        
        Label yearLabel = siteBuilder.buildLabel(SITE_YEAR_LABEL, bannerBox, 2, 2, 1, 1, CLASS_REG_LABEL, ENABLED);
        ComboBox yearsCBox = siteBuilder.buildComboBox(SITE_YEARS_COMBO_BOX, bannerBox, 3, 2, 1, 1, CLASS_OH_BOX, ENABLED, years, years.get(0));
        
        
        subjectCBox.setEditable(true);
        subjectNumsCBox.setEditable(true);
        semestersCBox.setEditable(true);
        yearsCBox.setEditable(true);
        subjectCBox.setItems(subjects);
        subjectCBox.getSelectionModel().selectFirst();
        subjectNumsCBox.setItems(subjectNums);
        subjectNumsCBox.getSelectionModel().selectFirst();
        semestersCBox.setItems(semesters);
        semestersCBox.getSelectionModel().selectFirst();
        yearsCBox.setItems(years);
        yearsCBox.getSelectionModel().selectFirst();
        
        
        Label titleLabel = siteBuilder.buildLabel(SITE_TITLE_LABEL, bannerBox, 0, 3, 1, 1, CLASS_REG_LABEL, ENABLED);
        TextField titleTF = siteBuilder.buildTextField(SITE_TITLE_TEXT_FIELD, bannerBox, 1, 3, 1, 1, CLASS_OH_TEXT_FIELD, ENABLED);
        
        GridPane expVBox = siteBuilder.buildGridPane(SITE_EXP_HBOX, siteTabVBox, CLASS_OH_PANE, ENABLED);
        expVBox.setStyle("-fx-background-color: #ebebeb;");
        Label expDirLabel = siteBuilder.buildLabel(SITE_EXPDIR_LABEL, expVBox, 0, 0, 1, 1, CLASS_REG_LABEL, ENABLED);
        Label expDirOutputLabel = siteBuilder.buildLabel(SITE_EXPORT_LABEL, expVBox, 1, 0, 1, 1, CLASS_REG_LABEL, ENABLED);

        subjectCBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override 
            public void changed(ObservableValue ov, String t, String t1) {
                boolean dupFound = false;
                for(Object s: subjects){
                    String c = (String)s;
                    if(c.equals(t1)){
                        dupFound = true;
                        break;
                    }
                }
                if(!dupFound){
                    subjects.add(t1);    
                }
                expDirOutputLabel.setText(".\\export\\" + subjectCBox.getSelectionModel().getSelectedItem().toString() + 
                                                "_" + subjectNumsCBox.getSelectionModel().getSelectedItem().toString() +
                                                "_" + semestersCBox.getSelectionModel().getSelectedItem().toString() +
                                                "_" + yearsCBox.getSelectionModel().getSelectedItem().toString() 
                                                 + "\\public.html");
                subjects.forEach((s) -> {
                    String c = (String)s;
                    if (c.trim().equals("")) {
                        subjects.remove(s);
                    }
                });
            }    
        });
        subjectNumsCBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override 
            public void changed(ObservableValue ov, String t, String t1) {                
                boolean dupFound = false;
                for(Object s: subjectNums){
                    String c = (String)s;
                    if(c.equals(t1)){
                        dupFound = true;
                        break;
                    }
                }
                if(!dupFound){
                    subjectNums.add(t1);    
                }
                expDirOutputLabel.setText(".\\export\\" + subjectCBox.getSelectionModel().getSelectedItem().toString() + 
                                                "_" + subjectNumsCBox.getSelectionModel().getSelectedItem().toString() +
                                                "_" + semestersCBox.getSelectionModel().getSelectedItem().toString() +
                                                "_" + yearsCBox.getSelectionModel().getSelectedItem().toString() 
                                                 + "\\public.html");
                subjectNums.forEach((s) -> {
                    String c = (String)s;
                    if (c.trim().equals("")) {
                        subjectNums.remove(s);
                    }
                });
            }    
        });
        semestersCBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override 
            public void changed(ObservableValue ov, String t, String t1) {                
                boolean dupFound = false;
                for(Object s: semesters){
                    String c = (String)s;
                    if(c.equals(t1)){
                        dupFound = true;
                        break;
                    }
                }
                if(!dupFound){
                    semesters.add(t1);    
                }
                expDirOutputLabel.setText(".\\export\\" + subjectCBox.getSelectionModel().getSelectedItem().toString() + 
                                                "_" + subjectNumsCBox.getSelectionModel().getSelectedItem().toString() +
                                                "_" + semestersCBox.getSelectionModel().getSelectedItem().toString() +
                                                "_" + yearsCBox.getSelectionModel().getSelectedItem().toString() 
                                                 + "\\public.html");
                semesters.forEach((s) -> {
                    String c = (String)s;
                    if (c.trim().equals("")) {
                        semesters.remove(s);
                    }
                });
            }    
        });
        yearsCBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override 
            public void changed(ObservableValue ov, String t, String t1) {                
                boolean dupFound = false;
                for(Object s: years){
                    String c = (String)s;
                    if(c.equals(t1)){
                        dupFound = true;
                        break;
                    }
                }
                if(!dupFound){
                    years.add(t1);    
                }
                expDirOutputLabel.setText(".\\export\\" + subjectCBox.getSelectionModel().getSelectedItem().toString() + 
                                                "_" + subjectNumsCBox.getSelectionModel().getSelectedItem().toString() +
                                                "_" + semestersCBox.getSelectionModel().getSelectedItem().toString() +
                                                "_" + yearsCBox.getSelectionModel().getSelectedItem().toString()
                                                 + "\\public.html");
                years.forEach((s) -> {
                    String c = (String)s;
                    if (c.trim().equals("")) {
                        years.remove(s);
                    }
                });
            }    
        });

//        bannerBox.add(expDirOutputLabel, 1, 4);
        bannerBox.setStyle("-fx-background-color: #ebebeb;");
        bannerBox.setPadding(new Insets(10, 10, 10, 10));
        bannerBox.setVgap(5);
        HBox blank1 = new HBox();
        blank1.setStyle("-fx-background-color: #ffc581;");
        blank1.setPadding(new Insets(5, 5, 5, 5));
        siteTabVBox.getChildren().addAll(blank1);
        
        VBox pagesVBox = siteBuilder.buildVBox(SITE_PAGES_VBOX, null, CLASS_OH_PANE, ENABLED);
        GridPane pagesBox = siteBuilder.buildGridPane(SITE_PAGESBOX_GRID_PANE, pagesVBox, CLASS_OH_PANE, ENABLED);
        
        Label pagesLabel = siteBuilder.buildLabel(SITE_PAGES_LABEL, pagesBox, 0, 0, 1, 1, CLASS_OH_HEADER_LABEL, ENABLED);
        
        Label homeLabel = siteBuilder.buildLabel(SITE_HOME_LABEL, pagesBox, 0, 1, 1, 1, CLASS_REG_LABEL, ENABLED);
        CheckBox homeCB = siteBuilder.buildCheckBox(SITE_HOME_CHECK_BOX, pagesBox, 1, 1, 1, 1, CLASS_OH_CB, ENABLED);
        
        Label syllabusLabel = siteBuilder.buildLabel(SITE_SYLLABUS_LABEL, pagesBox, 2, 1, 1, 1, CLASS_REG_LABEL, ENABLED);
        CheckBox syllabusCB = siteBuilder.buildCheckBox(SITE_SYLLABUS_CHECK_BOX, pagesBox, 3, 1, 1, 1, CLASS_OH_CB, ENABLED);
        
        Label scheduleLabel = siteBuilder.buildLabel(SITE_SCHEDULE_LABEL, pagesBox, 4, 1, 1, 1, CLASS_REG_LABEL, ENABLED);
        CheckBox schCB = siteBuilder.buildCheckBox(SITE_SCHEDULE_CHECK_BOX, pagesBox, 5, 1, 1, 1, CLASS_OH_CB, ENABLED);
        
        Label hwLabel = siteBuilder.buildLabel(SITE_HWS_LABEL, pagesBox, 6, 1, 1, 1, CLASS_REG_LABEL, ENABLED);
        CheckBox hwCB = siteBuilder.buildCheckBox(SITE_HW_CHECK_BOX, pagesBox, 7, 1, 1, 1, CLASS_OH_CB, ENABLED);
        homeCB.setIndeterminate(false);
        syllabusCB.setIndeterminate(false);
        schCB.setIndeterminate(false);
        hwCB.setIndeterminate(false);
        
        HBox homeHB = new HBox();
        HBox sylHB = new HBox();
        HBox schHB = new HBox();
        HBox hwHB = new HBox();
        homeHB.getChildren().addAll(homeLabel, homeCB);
        sylHB.getChildren().addAll(syllabusLabel, syllabusCB);
        schHB.getChildren().addAll(scheduleLabel, schCB);
        hwHB.getChildren().addAll(hwLabel, hwCB);
        HBox checkBoxHBox = new HBox();
        checkBoxHBox.getChildren().addAll(homeHB, sylHB, schHB, hwHB);
//        pagesBox.add(pagesLabel, 0, 0);
        pagesBox.add(checkBoxHBox, 0, 1);
        pagesBox.setStyle("-fx-background-color: #ebebeb;");
        pagesBox.setPadding(new Insets(10, 10, 10, 10));
        pagesBox.setVgap(5);
        HBox blank2 = new HBox();
        blank2.setStyle("-fx-background-color: #ffc581;");
        blank2.setPadding(new Insets(5,5,5,5));
        siteTabVBox.getChildren().addAll(pagesBox, blank2);
        
        VBox styleVBox = siteBuilder.buildVBox(SITE_STYLE_VBOX, null, CLASS_OH_PANE, ENABLED);
        GridPane styleBox = siteBuilder.buildGridPane(SITE_STYLEBOX_GRID_PANE, styleVBox, CLASS_OH_PANE, ENABLED);
        ObservableList styleSheets = FXCollections.observableArrayList();

        File[] files = new File("./work/css/").listFiles();
        ArrayList<String> fns = new ArrayList<>();
        int x = 0;
        for(File file: files){
            if(file.getPath().contains(".css")){
                String fileTemp = file.toString();
                fileTemp = fileTemp.substring(fileTemp.lastIndexOf("/") + 1);
                fns.add(fileTemp);
                x++;
            }
        }
        styleSheets.clear();
        styleSheets.addAll(fns);
        
        Label styleLabel = siteBuilder.buildLabel(SITE_STYLE_LABEL, styleBox, 0, 0, 1, 1, CLASS_OH_HEADER_LABEL, ENABLED);
        Button fviButton = siteBuilder.buildTextButton(SITE_FAVICON_BUTTON, styleBox, 0, 1, 1, 1, CLASS_APP_BUTTON, ENABLED);
        Button navbarButton = siteBuilder.buildTextButton(SITE_NAVBAR_BUTTON, styleBox, 0, 2, 1, 1, CLASS_APP_BUTTON, ENABLED);
        Button lfimg = siteBuilder.buildTextButton(SITE_LFIMG_BUTTON, styleBox, 0, 3, 1, 1, CLASS_APP_BUTTON, ENABLED);
        Button rfimg = siteBuilder.buildTextButton(SITE_RFIMG_BUTTON, styleBox, 0, 4, 1, 1, CLASS_APP_BUTTON, ENABLED);

        Label fontLabel = siteBuilder.buildLabel(SITE_FONTSTYLE_LABEL, styleBox, 0, 5, 1, 1, CLASS_REG_LABEL, ENABLED);
        

        fviImgView = new ImageView(
                props.getProperty(APP_FILE_PROTOCOL) + props.getProperty(APP_PATH_IMAGES) + "styleicons/" + props.getProperty(DEFAULT_FAVICON_TEXT)
        );
        fviImgView.setFitWidth(25);
        fviImgView.setFitHeight(25);
        fviImgView.setPreserveRatio(true);
        fviImgView.setSmooth(true);
        fviImgView.setCache(true);
        navImgView = new ImageView(
                props.getProperty(APP_FILE_PROTOCOL) + props.getProperty(APP_PATH_IMAGES) + "styleicons/" + props.getProperty(DEFAULT_NAVBAR_TEXT)
        );
        navImgView.setFitWidth(300);
        navImgView.setFitHeight(25);
        navImgView.setPreserveRatio(true);
        navImgView.setSmooth(true);
        navImgView.setCache(true);
        leftImgView = new ImageView(
                props.getProperty(APP_FILE_PROTOCOL) + props.getProperty(APP_PATH_IMAGES) + "styleicons/" + props.getProperty(DEFAULT_LFIMG_TEXT)
        );
        leftImgView.setFitWidth(300);
        leftImgView.setFitHeight(25);
        leftImgView.setPreserveRatio(true);
        leftImgView.setSmooth(true);
        leftImgView.setCache(true);
        rightImgView = new ImageView(
               props.getProperty(APP_FILE_PROTOCOL) + props.getProperty(APP_PATH_IMAGES) + "styleicons/" + props.getProperty(DEFAULT_RFIMG_TEXT)
        );
        rightImgView.setFitWidth(300);
        rightImgView.setFitHeight(25);
        rightImgView.setPreserveRatio(true);
        rightImgView.setSmooth(true);
        rightImgView.setCache(true);
           
        fviButton.setOnAction(e->{
            FileChooser imgSelect = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpeg");
            imgSelect.getExtensionFilters().add(extFilter);
            imgSelect.setInitialDirectory(new java.io.File("./images/styleicons/"));
            imgSelect.setTitle(props.getProperty(SITE_CHOOSE_IMAGE_TITLE));
            File f = imgSelect.showOpenDialog(styleBox.getScene().getWindow());
            if(f != null){
                fviImgView.setImage(new Image("file:" + f.getAbsolutePath()));
                fviImgView.setFitWidth(25);
                fviImgView.setFitHeight(25);
                fviImgView.setPreserveRatio(true);
                fviImgView.setSmooth(true);
                fviImgView.setCache(true);
                
                CSGController controller = new CSGController((CSGApp) app);
                AppGUIModule gui = app.getGUIModule();
                controller.processFviImgViewFile(f.getPath());

            }
            
            
        });
        
        navbarButton.setOnAction(e->{
            FileChooser imgSelect = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpeg");
            imgSelect.getExtensionFilters().add(extFilter);
            imgSelect.setInitialDirectory(new java.io.File("./images/styleicons/"));
            imgSelect.setTitle(props.getProperty(SITE_CHOOSE_IMAGE_TITLE));
            File f = imgSelect.showOpenDialog(styleBox.getScene().getWindow());
            if(f != null){
                navImgView.setImage(new Image("file:" + f.getAbsolutePath()));
                navImgView.setFitWidth(300);
                navImgView.setFitHeight(25);
                navImgView.setPreserveRatio(true);
                navImgView.setSmooth(true);
                navImgView.setCache(true);
                
                CSGController controller = new CSGController((CSGApp) app);
                AppGUIModule gui = app.getGUIModule();
                controller.processNavImgViewFile(f.getPath());
            }
            
            
        });
        
        lfimg.setOnAction(e->{
            FileChooser imgSelect = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpeg");
            imgSelect.getExtensionFilters().add(extFilter);
            imgSelect.setInitialDirectory(new java.io.File("./images/styleicons/"));
            imgSelect.setTitle(props.getProperty(SITE_CHOOSE_IMAGE_TITLE));
            File f = imgSelect.showOpenDialog(styleBox.getScene().getWindow());
            if(f != null){
                leftImgView.setImage(new Image("file:" + f.getAbsolutePath()));
                leftImgView.setFitWidth(300);
                leftImgView.setFitHeight(25);
                leftImgView.setPreserveRatio(true);
                leftImgView.setSmooth(true);
                leftImgView.setCache(true);
                
                CSGController controller = new CSGController((CSGApp) app);
                AppGUIModule gui = app.getGUIModule();
                controller.processLeftImgViewFile(f.getPath());
            }
            
            
        });
        
        rfimg.setOnAction(e->{
            FileChooser imgSelect = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpeg");
            imgSelect.getExtensionFilters().add(extFilter);
            imgSelect.setInitialDirectory(new java.io.File("./images/styleicons/"));
            imgSelect.setTitle(props.getProperty(SITE_CHOOSE_IMAGE_TITLE));
            File f = imgSelect.showOpenDialog(styleBox.getScene().getWindow());
            if(f != null){
                rightImgView.setImage(new Image("file:" + f.getAbsolutePath()));
                rightImgView.setFitWidth(300);
                rightImgView.setFitHeight(25);
                rightImgView.setPreserveRatio(true);
                rightImgView.setSmooth(true);
                rightImgView.setCache(true);
                
                CSGController controller = new CSGController((CSGApp) app);
                AppGUIModule gui = app.getGUIModule();
                controller.processRightImgViewFile(f.getPath());
            }
            
            
        });

        ComboBox css = siteBuilder.buildComboBox(SITE_CSS_COMBO_BOX, styleBox, 1, 5, 1, 1, CLASS_OH_CB, ENABLED, styleSheets, styleSheets.get(0));
        css.setItems(styleSheets);
        css.getSelectionModel().selectFirst();
        VBox styleContainer = new VBox();
        GridPane warningVBox = siteBuilder.buildGridPane(SITE_EXP_HBOX, styleVBox, CLASS_OH_PANE_EXTRA, ENABLED);
        warningVBox.setStyle("-fx-background-color: #ebebeb;");
        Label warningLabel = siteBuilder.buildLabel(SITE_FONTSTYLENOTE_LABEL, warningVBox, 0, 0, 1, 1, CLASS_REG_LABEL, ENABLED);
        warningLabel.setStyle("-fx-font-weight: bold");
        warningLabel.setPadding(new Insets(5, 5, 5, 5));

        styleBox.add(fviImgView, 1, 1);
        styleBox.add(navImgView, 1, 2);
        styleBox.add(leftImgView, 1, 3);
        styleBox.add(rightImgView, 1, 4);
        styleBox.setStyle("-fx-background-color: #ebebeb;");
        styleBox.setPadding(new Insets(1, 1, 1, 1));
        styleBox.setVgap(5);
        styleContainer.setStyle("-fx-background-color: #ebebeb;");
        styleContainer.getChildren().addAll(styleBox, warningVBox);
        HBox blank3 = new HBox();
        blank3.setStyle("-fx-background-color: #ffc581;");
        blank3.setPadding(new Insets(5,5,5,5));
        siteTabVBox.getChildren().addAll(styleContainer, blank3);
        
        VBox instructorVBox = new VBox();
                //siteBuilder.buildVBox(SITE_INST_VBOX, siteTabVBox, CLASS_OH_PANE, ENABLED);
        GridPane instructorBox = siteBuilder.buildGridPane(SITE_INSTBOX_GRID_PANE, instructorVBox, CLASS_OH_PANE, ENABLED);
        Label instructorLabel = siteBuilder.buildLabel(SITE_INSTRUCTOR_LABEL, instructorBox, 0, 0, 1, 1, CLASS_OH_HEADER_LABEL, ENABLED);
        Label nameLabel = siteBuilder.buildLabel(SITE_NAME_LABEL, instructorBox, 0, 1, 1, 1, CLASS_REG_LABEL, ENABLED);
        TextField siteNameTF = siteBuilder.buildTextField(SITE_NAME_TEXT_FIELD, instructorBox, 1, 1, 1, 1, CLASS_REG_LABEL, ENABLED);
        Label emailLabel = siteBuilder.buildLabel(SITE_EMAIL_LABEL, instructorBox, 0, 2, 1, 1, CLASS_REG_LABEL, ENABLED);
        TextField siteEmailTF = siteBuilder.buildTextField(SITE_EMAIL_TEXT_FIELD, instructorBox, 1, 2, 1, 1, CLASS_REG_LABEL, ENABLED);
        Label roomLabel = siteBuilder.buildLabel(SITE_ROOM_LABEL, instructorBox, 2, 1, 1, 1, CLASS_REG_LABEL, ENABLED);
        TextField siteRoomTF = siteBuilder.buildTextField(SITE_ROOM_TEXT_FIELD, instructorBox, 3, 1, 1, 1, CLASS_REG_LABEL, ENABLED);
        Label hpLabel = siteBuilder.buildLabel(SITE_HP_LABEL, instructorBox, 2, 2, 1, 1, CLASS_REG_LABEL, ENABLED);
        TextField siteHPTF = siteBuilder.buildTextField(SITE_HP_TEXT_FIELD, instructorBox, 3, 2, 1, 1, CLASS_REG_LABEL, ENABLED);

        HBox ohDetail = new HBox();
                //siteBuilder.buildHBox(SITE_INST_OH_DETAILS, siteTabVBox, CLASS_OH_PANE, ENABLED);
        Button siteInstructorOHExpandButton = siteBuilder.buildTextButton(SITE_EXPAND_BUTTON, ohDetail, CLASS_APP_BUTTON, ENABLED);
        Label ohLabel = siteBuilder.buildLabel(SITE_OH_LABEL, ohDetail, CLASS_OH_HEADER_LABEL, ENABLED);
        VBox hiddenTA = new VBox();
                // siteBuilder.buildVBox(SITE_INST_TA_DETAILS, siteTabVBox, CLASS_OH_PANE, ENABLED);
        instructorOHJsonArea = new TextArea();

        instructorOHJsonArea.setText("["
                                    + "\n\t{\"Day\": \"\"\t\"Time\": \"\"}"
                                 + "\n]");
        instructorOHJsonArea.setVisible(false);
        instructorOHJsonArea.setManaged(false);
        
        siteInstructorOHExpandButton.setOnAction(e->{
            siteInstructorOHExpandButton.setText(siteInstructorOHExpandButton.getText().equals("+") ? "-": "+");
            instructorOHJsonArea.setManaged(siteInstructorOHExpandButton.getText().equals("-")? true: false);
            instructorOHJsonArea.setVisible(siteInstructorOHExpandButton.getText().equals("-")? true: false);
        });
        
        instructorOHJsonArea.textProperty().addListener(e->{
            CSGController controller = new CSGController((CSGApp) app);
            AppGUIModule gui = app.getGUIModule();
            controller.processInstructorHoursJSON(instructorOHJsonArea.getText());
        });
        

        Label emptyLbl = new Label();

        ohDetail.setStyle("-fx-background-color: #ebebeb;");
        ohDetail.setPadding(new Insets(10, 10, 10, 10));
        hiddenTA.getChildren().addAll(emptyLbl, instructorOHJsonArea);
        hiddenTA.setStyle("-fx-background-color: #ebebeb;");
        instructorBox.setStyle("-fx-background-color: #ebebeb;");
//        instructorBox.setPadding(new Insets(10, 10, 10, 10));
        
//        instructorBox.getChildren().addAll(ohDetail, hiddenTA);
        siteTabVBox.getChildren().addAll(instructorBox, ohDetail, hiddenTA);

        siteTabVBox.setStyle("-fx-background-color: #ffc581;");
        siteTabVBox.setPadding(new Insets(10, 10, 10, 10));
        siteTabScrollPane.setStyle("-fx-background-color: #ffc581;");
        siteTabScrollPane.setContent(siteTabVBox);
//        siteTabScrollPane.setFitToHeight(true);
        siteTabScrollPane.setFitToWidth(true);
        //--------------------------------------------------------------------------------------------------------------------//
        
        
        //----------------------------------------------SETS UP THE SYLLABUS TAB--------------------------------------------------//
        VBox syllabusTabVBox = syllabusBuilder.buildVBox(SYL_MAIN_VBOX, null, CLASS_OH_PANE, ENABLED);
        GridPane descBox = syllabusBuilder.buildGridPane(SYL_DESCBOX_GRID_PANE, syllabusTabVBox, CLASS_OH_PANE, ENABLED);
        descBox.setStyle("-fx-background-color: #ebebeb;");
        HBox descDetail = syllabusBuilder.buildHBox(SYL_DESC_HBOX, descBox, 0, 0, 1, 2, CLASS_OH_PANE, ENABLED);
        Button sylDescExpandButton = syllabusBuilder.buildTextButton(SYL_DESC_BUTTON, descDetail, CLASS_APP_BUTTON, ENABLED);
        Label sylDesc = syllabusBuilder.buildLabel(SYL_DESC_LABEL, descDetail, CLASS_OH_HEADER_LABEL, ENABLED);
        
        TextArea descTA = new TextArea();
        descTA.setVisible(false);
        descTA.setManaged(false);
        descTA.textProperty().addListener(e->{
            CSGController controller = new CSGController((CSGApp) app);
            AppGUIModule gui = app.getGUIModule();
            controller.processDescriptionJSON(descTA.getText());
        });
        sylDescExpandButton.setOnAction(e->{
            sylDescExpandButton.setText(sylDescExpandButton.getText().equals("+") ? "-": "+");
            descTA.setManaged(sylDescExpandButton.getText().equals("-")? true: false);
            descTA.setVisible(sylDescExpandButton.getText().equals("-")? true: false);
        });
//        descDetail.getChildren().addAll(sylDescExpandButton, sylDesc);
//        descBox.add(descDetail, 0, 1);
        descBox.setPadding(new Insets(10, 10, 10, 10));
        descBox.setVgap(5);
        HBox blank4 = new HBox();
        blank4.setStyle("-fx-background-color: #ffc581;");
        blank4.setPadding(new Insets(5,5,5,5));
        syllabusTabVBox.getChildren().addAll(descTA, blank4);
//        syllabusTabVBox.getChildren().addAll(descBox, descTA, blank4);
        

        GridPane topicBox = syllabusBuilder.buildGridPane(SYL_TOPICBOX_GRID_PANE, syllabusTabVBox, CLASS_OH_PANE, ENABLED);
        topicBox.setStyle("-fx-background-color: #ebebeb;");
        HBox topicDetail = syllabusBuilder.buildHBox(SYL_TOPIC_HBOX, topicBox, 0, 0, 1, 2, CLASS_OH_PANE, ENABLED);
        Button sylTopicExpandButton = syllabusBuilder.buildTextButton(SYL_TOPICS_BUTTON, topicDetail, CLASS_APP_BUTTON, ENABLED);
        Label sylTopic = syllabusBuilder.buildLabel(SYL_TOPICS_LABEL, topicDetail, CLASS_OH_HEADER_LABEL, ENABLED);

        TextArea topicTA = new TextArea();
        topicTA.setText("["
                        + "\n\t\"\""
                    + "\n],");
        topicTA.setVisible(false);
        topicTA.setManaged(false);
        topicTA.textProperty().addListener(e->{
            CSGController controller = new CSGController((CSGApp) app);
            AppGUIModule gui = app.getGUIModule();
            controller.processTopicsJSON(topicTA.getText());
        });
        sylTopicExpandButton.setOnAction(e->{
            sylTopicExpandButton.setText(sylTopicExpandButton.getText().equals("+") ? "-": "+");
            topicTA.setManaged(sylTopicExpandButton.getText().equals("-")? true: false);
            topicTA.setVisible(sylTopicExpandButton.getText().equals("-")? true: false);
        });
//        HBox topicDetail = new HBox();
//        topicDetail.getChildren().addAll(sylTopicExpandButton, sylTopic);
//        topicBox.add(topicDetail, 0, 1);
        topicBox.setPadding(new Insets(10, 10, 10, 10));
        topicBox.setVgap(5);
        HBox blank5 = new HBox();
        blank5.setStyle("-fx-background-color: #ffc581;");
        blank5.setPadding(new Insets(5,5,5,5));
        syllabusTabVBox.getChildren().addAll(topicTA, blank5);
        
        
        GridPane prereqBox = syllabusBuilder.buildGridPane(SYL_PRBOX_GRID_PANE, syllabusTabVBox, CLASS_OH_PANE, ENABLED);
        prereqBox.setStyle("-fx-background-color: #ebebeb;");
        HBox prereqDetail = syllabusBuilder.buildHBox(SYL_PR_HBOX, prereqBox, 0, 0, 1, 2, CLASS_OH_PANE, ENABLED);
        Button sylPrereqExpandButton = syllabusBuilder.buildTextButton(SYL_PREREQ_BUTTON, prereqDetail, CLASS_APP_BUTTON, ENABLED);
        Label prereqLabel = syllabusBuilder.buildLabel(SYL_PREREQ_LABEL, prereqDetail, CLASS_OH_HEADER_LABEL, ENABLED);

        TextArea prereqTA = new TextArea();
        prereqTA.setVisible(false);
        prereqTA.setManaged(false);
        prereqTA.textProperty().addListener(e->{
            CSGController controller = new CSGController((CSGApp) app);
            AppGUIModule gui = app.getGUIModule();
            controller.processPrereqsJSON(prereqTA.getText());
        });
        sylPrereqExpandButton.setOnAction(e->{
            sylPrereqExpandButton.setText(sylPrereqExpandButton.getText().equals("+") ? "-": "+");
            prereqTA.setManaged(sylPrereqExpandButton.getText().equals("-")? true: false);
            prereqTA.setVisible(sylPrereqExpandButton.getText().equals("-")? true: false);
        });
//        prereqDetail.getChildren().addAll(sylPrereqExpandButton, prereqLabel);
//        prereqBox.add(prereqDetail, 0, 1);
        prereqBox.setPadding(new Insets(10, 10, 10, 10));
        prereqBox.setVgap(5);
        HBox blank6 = new HBox();
        blank6.setStyle("-fx-background-color: #ffc581;");
        blank6.setPadding(new Insets(5,5,5,5));
        syllabusTabVBox.getChildren().addAll(prereqTA, blank6);
        
        
        GridPane outcomesBox = syllabusBuilder.buildGridPane(SYL_OCBOX_GRID_PANE, syllabusTabVBox, CLASS_OH_PANE, ENABLED);
        outcomesBox.setStyle("-fx-background-color: #ebebeb;");
        HBox outcomesDetail = syllabusBuilder.buildHBox(SYL_OC_HBOX, outcomesBox, 0, 0, 1, 2, CLASS_OH_PANE, ENABLED);
        Button sylOutcomesExpandButton = syllabusBuilder.buildTextButton(SYL_OUTCOMES_BUTTON, outcomesDetail, CLASS_APP_BUTTON, ENABLED);
        Label outcomesLabel = syllabusBuilder.buildLabel(SYL_OUTCOMES_LABEL, outcomesDetail, CLASS_OH_HEADER_LABEL, ENABLED);

        TextArea outcomesTA = new TextArea();
        outcomesTA.setText("["
                            + "\n\t\"\""
                        + "\n],");
        outcomesTA.setVisible(false);
        outcomesTA.setManaged(false);
        outcomesTA.textProperty().addListener(e->{
            CSGController controller = new CSGController((CSGApp) app);
            AppGUIModule gui = app.getGUIModule();
            controller.processOutcomesJSON(outcomesTA.getText());
        });
        sylOutcomesExpandButton.setOnAction(e->{
            sylOutcomesExpandButton.setText(sylOutcomesExpandButton.getText().equals("+") ? "-": "+");
            outcomesTA.setManaged(sylOutcomesExpandButton.getText().equals("-")? true: false);
            outcomesTA.setVisible(sylOutcomesExpandButton.getText().equals("-")? true: false);
        });
//        outcomesDetail.getChildren().addAll(sylOutcomesExpandButton, outcomesLabel);
//        outcomesBox.add(outcomesDetail, 0, 1);
        outcomesBox.setPadding(new Insets(10, 10, 10, 10));
        outcomesBox.setVgap(5);
        HBox blank7 = new HBox();
        blank7.setStyle("-fx-background-color: #ffc581;");
        blank7.setPadding(new Insets(5,5,5,5));
        syllabusTabVBox.getChildren().addAll(outcomesTA, blank7);
        
        
        GridPane textbookBox = syllabusBuilder.buildGridPane(SYL_TBBOX_GRID_PANE, syllabusTabVBox, CLASS_OH_PANE, ENABLED);
        textbookBox.setStyle("-fx-background-color: #ebebeb;");
        HBox textbooksDetail = syllabusBuilder.buildHBox(SYL_TB_HBOX, textbookBox, 0, 0, 1, 2, CLASS_OH_PANE, ENABLED);
        Button sylTextbooksExpandButton = syllabusBuilder.buildTextButton(SYL_TBOOK_BUTTON, textbooksDetail, CLASS_APP_BUTTON, ENABLED);
        Label textbookLabel = syllabusBuilder.buildLabel(SYL_TBOOK_LABEL, textbooksDetail, CLASS_OH_HEADER_LABEL, ENABLED);
        
        TextArea textbooksTA = new TextArea();
        textbooksTA.setText("[\n\t{"
                             + "\n\t\t\"title\":\"\","
                             + "\n\t\t\"link\":\"\","
                             + "\n\t\t\"photo\":\"\","
                             + "\n\t\t\"authors\":["
                                + "\n\t\t\t\"\""
                             + "\n\t\t],"
                             + "\n\t\t\"publisher\":\"\","
                             + "\n\t\t\"year\":\"\","
                        + "\n\t}\n]");
        textbooksTA.setVisible(false);
        textbooksTA.setManaged(false);
        textbooksTA.textProperty().addListener(e->{
            CSGController controller = new CSGController((CSGApp) app);
            AppGUIModule gui = app.getGUIModule();
            controller.processTextbooksJSON(textbooksTA.getText());
        });
        sylTextbooksExpandButton.setOnAction(e->{
            sylTextbooksExpandButton.setText(sylTextbooksExpandButton.getText().equals("+") ? "-": "+");
            textbooksTA.setManaged(sylTextbooksExpandButton.getText().equals("-")? true: false);
            textbooksTA.setVisible(sylTextbooksExpandButton.getText().equals("-")? true: false);
        });
//        textbooksDetail.getChildren().addAll(sylTextbooksExpandButton, textbookLabel);
//        textbookBox.add(textbooksDetail, 0, 1);
        textbookBox.setPadding(new Insets(10, 10, 10, 10));
        textbookBox.setVgap(5);
        HBox blank8 = new HBox();
        blank8.setStyle("-fx-background-color: #ffc581;");
        blank8.setPadding(new Insets(5,5,5,5));
        syllabusTabVBox.getChildren().addAll(textbooksTA, blank8); 
        
        
        GridPane gradedComponentsBox = syllabusBuilder.buildGridPane(SYL_GCBOX_GRID_PANE, syllabusTabVBox, CLASS_OH_PANE, ENABLED);
        gradedComponentsBox.setStyle("-fx-background-color: #ebebeb;");
        HBox gcDetail = syllabusBuilder.buildHBox(SYL_GC_HBOX, gradedComponentsBox, 0, 0, 1, 2, CLASS_OH_PANE, ENABLED);
        Button sylGCExpandButton = syllabusBuilder.buildTextButton(SYL_GRADED_BUTTON, gcDetail, CLASS_APP_BUTTON, ENABLED);
        Label gradedComponentsLabel = syllabusBuilder.buildLabel(SYL_GRADED_LABEL, gcDetail, CLASS_OH_HEADER_LABEL, ENABLED);
//        gradedComponentsLabel.setId("section_header_label");
        TextArea gcTA = new TextArea();
        gcTA.setText("["
                    + "\n\t{"
                            + "\n\t\t\"Name\": \"\""
                            + "\n\t\t\"Description\": \"\""
                            + "\n\t\t\"Weight\": \"\""
                        + "\n\t}"
                 + "\n],");
        gcTA.setVisible(false);
        gcTA.setManaged(false);
        gcTA.textProperty().addListener(e->{
            CSGController controller = new CSGController((CSGApp) app);
            AppGUIModule gui = app.getGUIModule();
            controller.processGCJSON(gcTA.getText());
        });
        sylGCExpandButton.setOnAction(e->{
            sylGCExpandButton.setText(sylGCExpandButton.getText().equals("+") ? "-": "+");
            gcTA.setManaged(sylGCExpandButton.getText().equals("-")? true: false);
            gcTA.setVisible(sylGCExpandButton.getText().equals("-")? true: false);
        });
//        gcDetail.getChildren().addAll(sylGCExpandButton, gradedComponentsLabel);
//        gradedComponentsBox.add(gcDetail, 0, 1);
        gradedComponentsBox.setPadding(new Insets(10, 10, 10, 10));
        gradedComponentsBox.setVgap(5);
        HBox blank9 = new HBox();
        blank9.setStyle("-fx-background-color: #ffc581;");
        blank9.setPadding(new Insets(5,5,5,5));
        syllabusTabVBox.getChildren().addAll(gcTA, blank9);
        
        
        GridPane gradingNoteBox = syllabusBuilder.buildGridPane(SYL_GNBOX_GRID_PANE, syllabusTabVBox, CLASS_OH_PANE, ENABLED);
        gradingNoteBox.setStyle("-fx-background-color: #ebebeb;");
        HBox gradingNoteDetail = syllabusBuilder.buildHBox(SYL_GN_HBOX, gradingNoteBox, 0, 0, 1, 2, CLASS_OH_PANE, ENABLED);
        Button sylGradingNoteExpandButton = syllabusBuilder.buildTextButton(SYL_GRADINGNOTE_BUTTON, gradingNoteDetail, CLASS_APP_BUTTON, ENABLED);
        Label gradingNoteLabel = syllabusBuilder.buildLabel(SYL_GRADINGNOTE_LABEL, gradingNoteDetail, CLASS_OH_HEADER_LABEL, ENABLED);

        TextArea gradingNoteTA = new TextArea();
        gradingNoteTA.setVisible(false);
        gradingNoteTA.setManaged(false);
        gradingNoteTA.textProperty().addListener(e->{
            CSGController controller = new CSGController((CSGApp) app);
            AppGUIModule gui = app.getGUIModule();
            controller.processGNJSON(gradingNoteTA.getText());
        });
        sylGradingNoteExpandButton.setOnAction(e->{
            sylGradingNoteExpandButton.setText(sylGradingNoteExpandButton.getText().equals("+") ? "-": "+");
            gradingNoteTA.setManaged(sylGradingNoteExpandButton.getText().equals("-")? true: false);
            gradingNoteTA.setVisible(sylGradingNoteExpandButton.getText().equals("-")? true: false);
        });
//        gradingNoteDetail.getChildren().addAll(sylGradingNoteExpandButton, gradingNoteLabel);
//        gradingNoteBox.add(gradingNoteDetail, 0, 1);
        gradingNoteBox.setPadding(new Insets(10, 10, 10, 10));
        gradingNoteBox.setVgap(5);
        HBox blank10 = new HBox();
        blank10.setStyle("-fx-background-color: #ffc581;");
        blank10.setPadding(new Insets(5,5,5,5));
        syllabusTabVBox.getChildren().addAll(gradingNoteTA, blank10);
        
        
        GridPane adBox = syllabusBuilder.buildGridPane(SYL_AD_GRID_PANE, syllabusTabVBox, CLASS_OH_PANE, ENABLED);
        adBox.setStyle("-fx-background-color: #ebebeb;");
        HBox adDetail = syllabusBuilder.buildHBox(SYL_AD_HBOX, adBox, 0, 0, 1, 2, CLASS_OH_PANE, ENABLED);
        Button sylADExpandButton = syllabusBuilder.buildTextButton(SYL_AD_BUTTON, adDetail, CLASS_APP_BUTTON, ENABLED);
        Label adLabel = syllabusBuilder.buildLabel(SYL_AD_LABEL, adDetail, CLASS_OH_HEADER_LABEL, ENABLED);

        TextArea adTA = new TextArea();
        adTA.setVisible(false);
        adTA.setManaged(false);
        adTA.textProperty().addListener(e->{
            CSGController controller = new CSGController((CSGApp) app);
            AppGUIModule gui = app.getGUIModule();
            controller.processADJSON(adTA.getText());
        });
        sylADExpandButton.setOnAction(e->{
            sylADExpandButton.setText(sylADExpandButton.getText().equals("+") ? "-": "+");
            adTA.setManaged(sylADExpandButton.getText().equals("-")? true: false);
            adTA.setVisible(sylADExpandButton.getText().equals("-")? true: false);
        });
//        adDetail.getChildren().addAll(sylADExpandButton, adLabel);
//        adBox.add(adDetail, 0, 1);
        adBox.setPadding(new Insets(10, 10, 10, 10));
        adBox.setVgap(5);
        HBox blank11 = new HBox();
        blank11.setStyle("-fx-background-color: #ffc581;");
        blank11.setPadding(new Insets(5,5,5,5));
        syllabusTabVBox.getChildren().addAll(adTA, blank11);
        
        
        GridPane saBox = syllabusBuilder.buildGridPane(SYL_SA_GRID_PANE, syllabusTabVBox, CLASS_OH_PANE, ENABLED);
        saBox.setStyle("-fx-background-color: #ebebeb;");
        HBox saDetail = syllabusBuilder.buildHBox(SYL_SA_HBOX, saBox, 0, 0, 1, 2, CLASS_OH_PANE, ENABLED);
        Button sylSAExpandButton = syllabusBuilder.buildTextButton(SYL_SA_BUTTON, saDetail, CLASS_APP_BUTTON, ENABLED);
        Label saLabel = syllabusBuilder.buildLabel(SYL_SA_LABEL, saDetail, CLASS_OH_HEADER_LABEL, ENABLED);

        TextArea saTA = new TextArea();
        saTA.setVisible(false);
        saTA.setManaged(false);
        saTA.textProperty().addListener(e->{
            CSGController controller = new CSGController((CSGApp) app);
            AppGUIModule gui = app.getGUIModule();
            controller.processSAJSON(saTA.getText());
        });
        sylSAExpandButton.setOnAction(e->{
            sylSAExpandButton.setText(sylSAExpandButton.getText().equals("+") ? "-": "+");
            saTA.setManaged(sylSAExpandButton.getText().equals("-")? true: false);
            saTA.setVisible(sylSAExpandButton.getText().equals("-")? true: false);
        });
//        saDetail.getChildren().addAll(sylSAExpandButton, saLabel);
//        saBox.add(saDetail, 0, 1);
        saBox.setPadding(new Insets(10, 10, 10, 10));
        saBox.setVgap(5);
        syllabusTabVBox.getChildren().addAll(saTA);  
       
        syllabusTabVBox.setStyle("-fx-background-color: #ffc581;");
        syllabusTabVBox.setPadding(new Insets(10, 10, 10, 10));
        ScrollPane syllabusTabScrollPane = new ScrollPane();
        syllabusTabScrollPane.setContent(syllabusTabVBox);
//        syllabusTabScrollPane.setFitToHeight(true);
        syllabusTabScrollPane.setFitToWidth(true);
        syllabusTabScrollPane.setStyle("-fx-background-color: #ffc581;");
        //------------------------------------------------------------------------------------------------------------------------//        
        
        
        //----------------------------------------------SETS UP THE MEETING TIMES TAB--------------------------------------------------//
        ScrollPane mtTabScrollPane = new ScrollPane();
        VBox mtContent = new VBox();
        VBox lecturePane = mtBuilder.buildVBox(MT_LECTURE_PANE, null, CLASS_OH_PANE, ENABLED);
        HBox lectureHeaderBox = mtBuilder.buildHBox(MT_LECTURE_HEADER_BOX, lecturePane, CLASS_OH_BOX, ENABLED);
        mtBuilder.buildTextButton(MT_LECTURE_ADD_BUTTON, lectureHeaderBox, CLASS_APP_BUTTON, ENABLED);
        mtBuilder.buildTextButton(MT_LECTURE_REMOVE_BUTTON, lectureHeaderBox, CLASS_APP_BUTTON, ENABLED);
        mtBuilder.buildLabel(CSGPropertyType.MT_LECTURE_HEADER_LABEL, lectureHeaderBox, CLASS_OH_HEADER_LABEL, ENABLED); 
        
        TableView<Lecture> lectureTable = mtBuilder.buildTableView(MT_LECTURE_TABLE_VIEW, lecturePane, CLASS_OH_TABLE_VIEW, ENABLED);
        lectureTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        TableColumn lectureSectionColumn = mtBuilder.buildTableColumn(MT_LECTURE_SECTION_TABLE_COLUMN, lectureTable, CLASS_OH_COLUMN);
        TableColumn lectureDayColumn = mtBuilder.buildTableColumn(MT_LECTURE_DAY_TABLE_COLUMN, lectureTable, CLASS_OH_COLUMN);
        TableColumn lectureTimeColumn = mtBuilder.buildTableColumn(MT_LECTURE_TIME_TABLE_COLUMN, lectureTable, CLASS_OH_COLUMN);
        TableColumn lectureRoomColumn = mtBuilder.buildTableColumn(MT_LECTURE_ROOM_TABLE_COLUMN, lectureTable, CLASS_OH_COLUMN);
        lectureSectionColumn.setCellValueFactory(new PropertyValueFactory<String, String>("section"));
        lectureDayColumn.setCellValueFactory(new PropertyValueFactory<String, String>("day"));
        lectureTimeColumn.setCellValueFactory(new PropertyValueFactory<String, String>("time"));
        lectureRoomColumn.setCellValueFactory(new PropertyValueFactory<String, String>("room"));
        lectureSectionColumn.prefWidthProperty().bind(lectureTable.widthProperty().multiply(.2));
        lectureDayColumn.prefWidthProperty().bind(lectureTable.widthProperty().multiply(.4));
        lectureTimeColumn.prefWidthProperty().bind(lectureTable.widthProperty().multiply(.2));
        lectureRoomColumn.prefWidthProperty().bind(lectureTable.widthProperty().multiply(.2));
        lecturePane.setSpacing(5);
        lecturePane.setStyle("-fx-background-color: #ebebeb;");
        
        
        lecturePane.setPadding(new Insets(10, 10, 10, 10));
        HBox blank12 = new HBox();
        blank12.setStyle("-fx-background-color: #ffc581;");
        blank12.setPadding(new Insets(5,5,5,5));
        mtContent.getChildren().addAll(lecturePane, blank12);
        
        VBox recitationPane = mtBuilder.buildVBox(MT_RECITATION_PANE, null, CLASS_OH_PANE, ENABLED);
        HBox recitationHeaderBox = mtBuilder.buildHBox(MT_RECITATION_HEADER_BOX, recitationPane, CLASS_OH_BOX, ENABLED);
        mtBuilder.buildTextButton(MT_RECITATION_ADD_BUTTON, recitationHeaderBox, CLASS_APP_BUTTON, ENABLED);
        mtBuilder.buildTextButton(MT_RECITATION_REMOVE_BUTTON, recitationHeaderBox, CLASS_APP_BUTTON, ENABLED);
        mtBuilder.buildLabel(CSGPropertyType.MT_RECITATIONS_HEADER_LABEL, recitationHeaderBox, CLASS_OH_HEADER_LABEL, ENABLED); 
        
        TableView<Recitation> recitationTable = mtBuilder.buildTableView(MT_RECITATION_TABLE_VIEW, recitationPane, CLASS_OH_TABLE_VIEW, ENABLED);
        recitationTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        TableColumn recitationSectionColumn = mtBuilder.buildTableColumn(MT_RECITATION_SECTION_TABLE_COLUMN, recitationTable, CLASS_OH_COLUMN);
        TableColumn recitationDayTimeColumn = mtBuilder.buildTableColumn(MT_RECITATION_DAYANDTIME_TABLE_COLUMN, recitationTable, CLASS_OH_COLUMN);
        TableColumn recitationRoomColumn = mtBuilder.buildTableColumn(MT_RECITATION_ROOM_TABLE_COLUMN, recitationTable, CLASS_OH_COLUMN);
        TableColumn recitationTA1Column = mtBuilder.buildTableColumn(MT_RECITATION_TA1_TABLE_COLUMN, recitationTable, CLASS_OH_COLUMN);
        TableColumn recitationTA2Column = mtBuilder.buildTableColumn(MT_RECITATION_TA2_TABLE_COLUMN, recitationTable, CLASS_OH_COLUMN);
        recitationSectionColumn.setCellValueFactory(new PropertyValueFactory<String, String>("section"));
        recitationDayTimeColumn.setCellValueFactory(new PropertyValueFactory<String, String>("day"));
        recitationRoomColumn.setCellValueFactory(new PropertyValueFactory<String, String>("room"));
        recitationTA1Column.setCellValueFactory(new PropertyValueFactory<String, String>("TA1"));
        recitationTA2Column.setCellValueFactory(new PropertyValueFactory<String, String>("TA2"));
        recitationSectionColumn.prefWidthProperty().bind(recitationTable.widthProperty().multiply(.15));
        recitationDayTimeColumn.prefWidthProperty().bind(recitationTable.widthProperty().multiply(.4));
        lectureRoomColumn.prefWidthProperty().bind(recitationTable.widthProperty().multiply(.15));
        recitationTA1Column.prefWidthProperty().bind(recitationTable.widthProperty().multiply(.15));
        recitationTA2Column.prefWidthProperty().bind(recitationTable.widthProperty().multiply(.15));
        recitationPane.setStyle("-fx-background-color: #ebebeb;");
        recitationPane.setPadding(new Insets(10, 10, 10, 10));
        HBox blank13 = new HBox();
        blank13.setStyle("-fx-background-color: #ffc581;");
        blank13.setPadding(new Insets(5,5,5,5));
        recitationPane.setSpacing(5);
        mtContent.getChildren().addAll(recitationPane, blank13);
        
        VBox labPane = mtBuilder.buildVBox(MT_LAB_PANE, null, CLASS_OH_PANE, ENABLED);
        HBox labHeaderBox = mtBuilder.buildHBox(MT_LAB_HEADER_BOX, labPane, CLASS_OH_BOX, ENABLED);
        mtBuilder.buildTextButton(MT_LAB_ADD_BUTTON, labHeaderBox, CLASS_APP_BUTTON, ENABLED);
        mtBuilder.buildTextButton(MT_LAB_REMOVE_BUTTON, labHeaderBox, CLASS_APP_BUTTON, ENABLED);
        mtBuilder.buildLabel(CSGPropertyType.MT_LABS_HEADER_LABEL, labHeaderBox, CLASS_OH_HEADER_LABEL, ENABLED); 
        
        TableView<Lab> labTable = mtBuilder.buildTableView(MT_LAB_TABLE_VIEW, labPane, CLASS_OH_TABLE_VIEW, ENABLED);
        labTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        TableColumn labSectionColumn = mtBuilder.buildTableColumn(MT_LAB_SECTION_TABLE_COLUMN, labTable, CLASS_OH_COLUMN);
        TableColumn labDayTimeColumn = mtBuilder.buildTableColumn(MT_LAB_DAYANDTIME_TABLE_COLUMN, labTable, CLASS_OH_COLUMN);
        TableColumn labRoomColumn = mtBuilder.buildTableColumn(MT_LAB_ROOM_TABLE_COLUMN, labTable, CLASS_OH_COLUMN);
        TableColumn labTA1Column = mtBuilder.buildTableColumn(MT_LAB_TA1_TABLE_COLUMN, labTable, CLASS_OH_COLUMN);
        TableColumn labTA2Column = mtBuilder.buildTableColumn(MT_LAB_TA2_TABLE_COLUMN, labTable, CLASS_OH_COLUMN);
        labSectionColumn.setCellValueFactory(new PropertyValueFactory<String, String>("section"));
        labDayTimeColumn.setCellValueFactory(new PropertyValueFactory<String, String>("day"));
        labRoomColumn.setCellValueFactory(new PropertyValueFactory<String, String>("room"));
        labTA1Column.setCellValueFactory(new PropertyValueFactory<String, String>("TA1"));
        labTA2Column.setCellValueFactory(new PropertyValueFactory<String, String>("TA2"));
        labSectionColumn.prefWidthProperty().bind(labTable.widthProperty().multiply(.15));
        labDayTimeColumn.prefWidthProperty().bind(labTable.widthProperty().multiply(.4));
        lectureRoomColumn.prefWidthProperty().bind(labTable.widthProperty().multiply(.15));
        labTA1Column.prefWidthProperty().bind(labTable.widthProperty().multiply(.15));
        labTA2Column.prefWidthProperty().bind(labTable.widthProperty().multiply(.15));
        labPane.setStyle("-fx-background-color: #ebebeb;");
        labPane.setPadding(new Insets(10, 10, 10, 10));
        labPane.setSpacing(5);
        mtContent.getChildren().add(labPane);
        
        mtContent.setStyle("-fx-background-color: #ffc581;");
        mtContent.setPadding(new Insets(10, 10, 10, 10));
        mtTabScrollPane.setFitToWidth(true);
        mtTabScrollPane.setContent(mtContent);

        
        //-----------------------------------------------------------------------------------------------------------------------------//
        
        
        //----------------------------------------------SETS UP THE OH TAB--------------------------------------------------//
        // INIT THE HEADER ON THE LEFT
        VBox leftPane = ohBuilder.buildVBox(OH_LEFT_PANE, null, CLASS_OH_PANE, ENABLED);
        HBox tasHeaderBox = ohBuilder.buildHBox(OH_TAS_HEADER_PANE, leftPane, CLASS_OH_BOX, ENABLED);
        ohBuilder.buildTextButton(OH_REMOVE_TA_BUTTON, tasHeaderBox, CLASS_APP_BUTTON, !ENABLED);
        ohBuilder.buildLabel(CSGPropertyType.OH_TAS_HEADER_LABEL, tasHeaderBox, CLASS_OH_HEADER_LABEL, ENABLED);
        HBox typeHeaderBox = ohBuilder.buildHBox(OH_GRAD_UNDERGRAD_TAS_PANE, tasHeaderBox, CLASS_OH_RADIO_BOX, ENABLED);
        ToggleGroup tg = new ToggleGroup();
        ohBuilder.buildRadioButton(OH_ALL_RADIO_BUTTON, typeHeaderBox, CLASS_OH_RADIO_BUTTON, ENABLED, tg, true);
        ohBuilder.buildRadioButton(OH_GRAD_RADIO_BUTTON, typeHeaderBox, CLASS_OH_RADIO_BUTTON, ENABLED, tg, false);
        ohBuilder.buildRadioButton(OH_UNDERGRAD_RADIO_BUTTON, typeHeaderBox, CLASS_OH_RADIO_BUTTON, ENABLED, tg, false);

        // MAKE THE TABLE AND SETUP THE DATA MODEL
        TableView<TeachingAssistantPrototype> taTable = ohBuilder.buildTableView(OH_TAS_TABLE_VIEW, leftPane, CLASS_OH_TABLE_VIEW, ENABLED);
        taTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        TableColumn nameColumn = ohBuilder.buildTableColumn(OH_NAME_TABLE_COLUMN, taTable, CLASS_OH_COLUMN);
        TableColumn emailColumn = ohBuilder.buildTableColumn(OH_EMAIL_TABLE_COLUMN, taTable, CLASS_OH_COLUMN);
        TableColumn slotsColumn = ohBuilder.buildTableColumn(OH_SLOTS_TABLE_COLUMN, taTable, CLASS_OH_CENTERED_COLUMN);
        TableColumn typeColumn = ohBuilder.buildTableColumn(OH_TYPE_TABLE_COLUMN, taTable, CLASS_OH_COLUMN);
        nameColumn.setCellValueFactory(new PropertyValueFactory<String, String>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<String, String>("email"));
        slotsColumn.setCellValueFactory(new PropertyValueFactory<String, String>("slots"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<String, String>("type"));
        nameColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(1.0 / 4.0));
        emailColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(1.0 / 4.0));
        slotsColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(1.0 / 4.0));
        typeColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(1.0 / 4.0));

        // ADD BOX FOR ADDING A TA
        HBox taBox = ohBuilder.buildHBox(OH_ADD_TA_PANE, leftPane, CLASS_OH_PANE, ENABLED);
        ohBuilder.buildTextField(OH_NAME_TEXT_FIELD, taBox, CLASS_OH_TEXT_FIELD, ENABLED);
        ohBuilder.buildTextField(OH_EMAIL_TEXT_FIELD, taBox, CLASS_OH_TEXT_FIELD, ENABLED);
        ohBuilder.buildTextButton(OH_ADD_TA_BUTTON, taBox, CLASS_OH_BUTTON, !ENABLED);

        // MAKE SURE IT'S THE TABLE THAT ALWAYS GROWS IN THE LEFT PANE
        VBox.setVgrow(taTable, Priority.ALWAYS);
        leftPane.setPadding(new Insets(10, 10, 10, 10));
        leftPane.setSpacing(5);
        // INIT THE HEADER ON THE RIGHT
        ObservableList<String> startTimes = FXCollections.observableArrayList(
                                                                        props.getProperty(OH_START_12AM),
                                                                        props.getProperty(OH_START_1AM),
                                                                        props.getProperty(OH_START_2AM),
                                                                        props.getProperty(OH_START_3AM),
                                                                        props.getProperty(OH_START_4AM),
                                                                        props.getProperty(OH_START_5AM),
                                                                        props.getProperty(OH_START_6AM),
                                                                        props.getProperty(OH_START_7AM),
                                                                        props.getProperty(OH_START_8AM),
                                                                        props.getProperty(OH_START_9AM),
                                                                        props.getProperty(OH_START_10AM),
                                                                        props.getProperty(OH_START_11AM),
                                                                        props.getProperty(OH_START_12PM),
                                                                        props.getProperty(OH_START_1PM),
                                                                        props.getProperty(OH_START_2PM),
                                                                        props.getProperty(OH_START_3PM),
                                                                        props.getProperty(OH_START_4PM),
                                                                        props.getProperty(OH_START_5PM),
                                                                        props.getProperty(OH_START_6PM),
                                                                        props.getProperty(OH_START_7PM),
                                                                        props.getProperty(OH_START_8PM),
                                                                        props.getProperty(OH_START_9PM),
                                                                        props.getProperty(OH_START_10PM),
                                                                        props.getProperty(OH_START_11PM)
        );
        
        ObservableList<String> endTimes = FXCollections.observableArrayList(
                                                                        props.getProperty(OH_END_12AM),
                                                                        props.getProperty(OH_END_1AM),
                                                                        props.getProperty(OH_END_2AM),
                                                                        props.getProperty(OH_END_3AM),
                                                                        props.getProperty(OH_END_4AM),
                                                                        props.getProperty(OH_END_5AM),
                                                                        props.getProperty(OH_END_6AM),
                                                                        props.getProperty(OH_END_7AM),
                                                                        props.getProperty(OH_END_8AM),
                                                                        props.getProperty(OH_END_9AM),
                                                                        props.getProperty(OH_END_10AM),
                                                                        props.getProperty(OH_END_11AM),
                                                                        props.getProperty(OH_END_12PM),
                                                                        props.getProperty(OH_END_1PM),
                                                                        props.getProperty(OH_END_2PM),
                                                                        props.getProperty(OH_END_3PM),
                                                                        props.getProperty(OH_END_4PM),
                                                                        props.getProperty(OH_END_5PM),
                                                                        props.getProperty(OH_END_6PM),
                                                                        props.getProperty(OH_END_7PM),
                                                                        props.getProperty(OH_END_8PM),
                                                                        props.getProperty(OH_END_9PM),
                                                                        props.getProperty(OH_END_10PM),
                                                                        props.getProperty(OH_END_11PM)
        );
        
        leftPane.setStyle("-fx-background-color: #ebebeb;");
        
        
        VBox rightPane = ohBuilder.buildVBox(OH_RIGHT_PANE, null, CLASS_OH_PANE, ENABLED);
        HBox officeHoursHeaderBox = ohBuilder.buildHBox(OH_OFFICE_HOURS_HEADER_PANE, rightPane, CLASS_OH_PANE, ENABLED);
        ohBuilder.buildLabel(OH_OFFICE_HOURS_HEADER_LABEL, officeHoursHeaderBox, CLASS_OH_HEADER_LABEL, ENABLED);
        
        // FIX THIS PART ///////////////////////
        ohBuilder.buildLabel(OH_STARTTIME_LABEL, officeHoursHeaderBox, CLASS_OH_HEADER_LABEL, ENABLED);
        ComboBox start = ohBuilder.buildComboBox(OH_STARTTIME_COMBO_BOX, startTimes, startTimes.get(0), officeHoursHeaderBox, CLASS_OH_COMBO_BOX, ENABLED);
        
        start.setItems(startTimes);
        start.getSelectionModel().selectFirst();
        ohBuilder.buildLabel(OH_ENDTIME_LABEL, officeHoursHeaderBox, CLASS_OH_HEADER_LABEL, ENABLED);
        ComboBox end = ohBuilder.buildComboBox(OH_ENDTIME_COMBO_BOX, endTimes, endTimes.get(endTimes.size() - 1), officeHoursHeaderBox, CLASS_OH_COMBO_BOX, ENABLED);
        end.setItems(endTimes);
        end.getSelectionModel().selectLast();
        // FIX THIS PART ///////////////////////

        // SETUP THE OFFICE HOURS TABLE
        TableView<TimeSlot> officeHoursTable = ohBuilder.buildTableView(OH_OFFICE_HOURS_TABLE_VIEW, rightPane, CLASS_OH_OFFICE_HOURS_TABLE_VIEW, ENABLED);
        setupOfficeHoursColumn(OH_START_TIME_TABLE_COLUMN, officeHoursTable, CLASS_OH_TIME_COLUMN, "startTime");
        setupOfficeHoursColumn(OH_END_TIME_TABLE_COLUMN, officeHoursTable, CLASS_OH_TIME_COLUMN, "endTime");
        setupOfficeHoursColumn(OH_MONDAY_TABLE_COLUMN, officeHoursTable, CLASS_OH_DAY_OF_WEEK_COLUMN, "monday");
        setupOfficeHoursColumn(OH_TUESDAY_TABLE_COLUMN, officeHoursTable, CLASS_OH_DAY_OF_WEEK_COLUMN, "tuesday");
        setupOfficeHoursColumn(OH_WEDNESDAY_TABLE_COLUMN, officeHoursTable, CLASS_OH_DAY_OF_WEEK_COLUMN, "wednesday");
        setupOfficeHoursColumn(OH_THURSDAY_TABLE_COLUMN, officeHoursTable, CLASS_OH_DAY_OF_WEEK_COLUMN, "thursday");
        setupOfficeHoursColumn(OH_FRIDAY_TABLE_COLUMN, officeHoursTable, CLASS_OH_DAY_OF_WEEK_COLUMN, "friday");

        // MAKE SURE IT'S THE TABLE THAT ALWAYS GROWS IN THE LEFT PANE
        VBox.setVgrow(officeHoursTable, Priority.ALWAYS);
        rightPane.setPadding(new Insets(10, 10, 10, 10));
        rightPane.setSpacing(5);
        rightPane.setStyle("-fx-background-color: #ebebeb;");
        // BOTH PANES WILL NOW GO IN A SPLIT PANE
        SplitPane sPane = new SplitPane(leftPane, rightPane);
        sPane.setStyle("-fx-background-color: #ffc581;");
        sPane.setPadding(new Insets(10, 10, 10, 10));
        sPane.setDividerPositions(.4);
        //----------------------------------------------------------------------------------------------------------------------------------//
        
        
        //----------------------------------------------SETS UP THE SCHEDULE TAB--------------------------------------------------//
        ScrollPane schTabScrollPane = new ScrollPane();
        VBox schContent = new VBox();
        VBox schPane = schBuilder.buildVBox(SCH_PANE, null, CLASS_OH_PANE, ENABLED);
       
        VBox schBoundariesPane = schBuilder.buildVBox(SCH_BOUNDARIES_PANE, schPane, CLASS_OH_PANE, ENABLED);
        schBuilder.buildLabel(SCH_CALENDAR_BOUNDARIES_LABEL, schBoundariesPane, CLASS_OH_HEADER_LABEL, ENABLED);
        HBox schBoundariesBox = schBuilder.buildHBox(SCH_BOUNDARIES_OPTIONS_HEADER_BOX, schBoundariesPane, CLASS_OH_BOX, ENABLED);
        schBuilder.buildLabel(SCH_STARTING_MONDAY_LABEL, schBoundariesBox, CLASS_OH_LABEL, ENABLED);
        DatePicker startDate = new DatePicker();
        schBoundariesBox.getChildren().add(startDate);
        schBuilder.buildLabel(SCH_ENDING_FRIDAY_LABEL, schBoundariesBox, CLASS_OH_LABEL, ENABLED);
        DatePicker endDate = new DatePicker();
        schBoundariesPane.setStyle("-fx-background-color: #ebebeb;");
        schBoundariesPane.setSpacing(5);
        HBox blank14 = schBuilder.buildHBox(SCH_BLANK14_HBOX, schPane, CLASS_OH_BOX, ENABLED);
        blank14.setStyle("-fx-background-color: #ffc581;");
        blank14.setPadding(new Insets(5,5,5,5));
        schBoundariesBox.getChildren().addAll(endDate);
        
        VBox schItemsPane = schBuilder.buildVBox(SCH_ITEMS_PANE, schPane, CLASS_OH_PANE, ENABLED);
        HBox schItemsPaneHeaderBox = schBuilder.buildHBox(SCH_ITEMS_PANE_HEADER_BOX, schItemsPane, CLASS_OH_PANE, ENABLED);
        schBuilder.buildTextButton(SCH_REMOVE_ITEM_BUTTON, schItemsPaneHeaderBox, CLASS_APP_BUTTON, ENABLED);
        schBuilder.buildLabel(SCH_SCHEDULE_ITEMS_LABEL, schItemsPaneHeaderBox, CLASS_OH_HEADER_LABEL, ENABLED);
        
        TableView<ScheduleItem> itemTable = schBuilder.buildTableView(SCH_ITEMS_TABLE_VIEW, schItemsPane, CLASS_OH_TABLE_VIEW, ENABLED);
        itemTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        TableColumn schTypeColumn = schBuilder.buildTableColumn(SCH_TYPE_TABLE_COLUMN, itemTable, CLASS_OH_COLUMN);
        TableColumn schDateColumn = schBuilder.buildTableColumn(SCH_DATE_TABLE_COLUMN, itemTable, CLASS_OH_COLUMN);
        TableColumn schTitleColumn = schBuilder.buildTableColumn(SCH_TITLE_TABLE_COLUMN, itemTable, CLASS_OH_COLUMN);
        TableColumn schTopicColumn = schBuilder.buildTableColumn(SCH_TOPIC_TABLE_COLUMN, itemTable, CLASS_OH_COLUMN);
        schTypeColumn.setCellValueFactory(new PropertyValueFactory<String, String>("type"));
        schDateColumn.setCellValueFactory(new PropertyValueFactory<String, String>("date"));
        schTitleColumn.setCellValueFactory(new PropertyValueFactory<String, String>("title"));
        schTopicColumn.setCellValueFactory(new PropertyValueFactory<String, String>("topic"));
        schTypeColumn.prefWidthProperty().bind(lectureTable.widthProperty().multiply(.2));
        schDateColumn.prefWidthProperty().bind(lectureTable.widthProperty().multiply(.4));
        schTitleColumn.prefWidthProperty().bind(lectureTable.widthProperty().multiply(.2));
        schTopicColumn.prefWidthProperty().bind(lectureTable.widthProperty().multiply(.2));
        schItemsPane.setStyle("-fx-background-color: #ebebeb;");
        schItemsPane.setSpacing(5);
        HBox blank15 = schBuilder.buildHBox(SCH_BLANK14_HBOX, schPane, CLASS_OH_BOX, ENABLED);
        blank15.setStyle("-fx-background-color: #ffc581;");
        blank15.setPadding(new Insets(5,5,5,5));
        
        ObservableList typesOfEvents = FXCollections.observableArrayList(props.getProperty(SCH_HOLIDAY), 
                                                                        props.getProperty(SCH_LECTURE), 
                                                                        props.getProperty(SCH_LAB), 
                                                                        props.getProperty(SCH_RECITATION), 
                                                                        props.getProperty(SCH_HW), 
                                                                        props.getProperty(SCH_REFERENCE));
        
        GridPane schAddEditPane = schBuilder.buildGridPane(SCH_ADD_EDIT_PANE, schPane, CLASS_OH_PANE, ENABLED);
        schBuilder.buildLabel(SCH_ADD_EDIT_LABEL, schAddEditPane, 0, 1, 1, 1, CLASS_OH_HEADER_LABEL, ENABLED);
//        schBuilder.buildLabel(SCH_ADD_EDIT_LABEL, schAddEditPane, CLASS_OH_HEADER_LABEL, ENABLED);
//        HBox typeBox = schBuilder.buildHBox(SCH_TYPE_HBOX, schAddEditPane, 0, 2, 2, 1, CLASS_OH_BOX, ENABLED);
        schBuilder.buildLabel(SCH_TYPE_LABEL, schAddEditPane, 0, 2, 2, 1, CLASS_OH_LABEL, ENABLED);
        ComboBox eventBox = schBuilder.buildComboBox(SCH_TYPE_COMBO_BOX, schAddEditPane, 1, 2, 2, 1, CLASS_OH_COMBO_BOX, ENABLED, typesOfEvents, typesOfEvents.get(0));
        eventBox.setItems(typesOfEvents);
        eventBox.getSelectionModel().selectFirst();
//        schBuilder.buildComboBox(end, saBox, BUTTON_TAG_WIDTH, BUTTON_TAG_WIDTH, BUTTON_TAG_WIDTH, BUTTON_TAG_WIDTH, EMPTY_TEXT, ENABLED, mtContent, DEFAULT_NAVBAR_TEXT)
//        HBox dateBox = schBuilder.buildHBox(SCH_DATE_HBOX, schAddEditPane, 0, 3, 2, 1, CLASS_OH_BOX, ENABLED);
        schBuilder.buildLabel(SCH_DATE_LABEL, schAddEditPane, 0, 3, 2, 1, CLASS_OH_LABEL, ENABLED);
        DatePicker editDatePicker = new DatePicker();
        schAddEditPane.add(editDatePicker, 1, 3, 2, 1);
//        HBox titleBox = schBuilder.buildHBox(SCH_TITLE_HBOX, schAddEditPane, 0, 4, 2, 1, CLASS_OH_BOX, ENABLED);
        schBuilder.buildLabel(SCH_TITLE_LABEL, schAddEditPane, 0 ,4, 2, 1, CLASS_OH_LABEL, ENABLED);
        schBuilder.buildTextField(SCH_TITLE_TEXT_FIELD, schAddEditPane, 1, 4, 2, 1, CLASS_OH_TEXT_FIELD, ENABLED);
//        HBox schTopicBox = schBuilder.buildHBox(SCH_TOPIC_HBOX, schAddEditPane, 0, 5, 2, 1, CLASS_OH_BOX, ENABLED);
        schBuilder.buildLabel(SCH_TOPIC_LABEL, schAddEditPane, 0, 5, 2, 1, CLASS_OH_LABEL, ENABLED);
        schBuilder.buildTextField(SCH_TOPIC_TEXT_FIELD, schAddEditPane, 1, 5, 2, 1, CLASS_OH_TEXT_FIELD, ENABLED);
//        HBox schLinkBox = schBuilder.buildHBox(SCH_LINK_HBOX, schAddEditPane, 0, 6, 2, 1, CLASS_OH_BOX, ENABLED);
        schBuilder.buildLabel(SCH_LINK_LABEL, schAddEditPane, 0, 6, 2, 1, CLASS_OH_LABEL, ENABLED);
        schBuilder.buildTextField(SCH_LINK_TEXT_FIELD, schAddEditPane, 1, 6, 2, 1, CLASS_OH_TEXT_FIELD, ENABLED);
        schBuilder.buildTextButton(SCH_ADD_UPDATE_BUTTON, schAddEditPane, 0, 7, 1, 1, CLASS_OH_BUTTON, ENABLED);
        schBuilder.buildTextButton(SCH_CLEAR_BUTTON, schAddEditPane, 2, 7, 2, 1, CLASS_OH_BUTTON, ENABLED);
        schAddEditPane.setStyle("-fx-background-color: #ebebeb;");
        schAddEditPane.setVgap(5);
        
        
        schContent.getChildren().add(schPane);
        schContent.setPadding(new Insets(1, 1, 1, 1));
        schContent.setStyle("-fx-background-color: #ffc581;");
        schTabScrollPane.setContent(schContent);
        //------------------------------------------------------------------------------------------------------------------------//
        
        
        //----------------------------------------------SETS UP THE CONTENT IN EACH TAB--------------------------------------------------//
        siteTab.setContent(siteTabScrollPane);
        syllabusTab.setContent(syllabusTabScrollPane);
        mtTab.setContent(mtTabScrollPane);
        ohTab.setContent(sPane);
        scheduleTab.setContent(schTabScrollPane);
        
        //-------------------------------------------------------------------------------------------------------------------------------//
        
        
        //----------------------------------------------PUTS EVERYTHING TOGETHER--------------------------------------------------//
        workspace = new BorderPane();
        tabs.prefWidthProperty().bind(workspace.widthProperty());
        tabs.tabMinWidthProperty().bind(workspace.widthProperty().divide(tabs.getTabs().size()).subtract(20));
//        tabs.minWidthProperty().bind(workspace.widthProperty().divide(5));
        // AND PUT EVERYTHING IN THE WORKSPACE
        ((BorderPane) workspace).setCenter(tabs);
        //------------------------------------------------------------------------------------------------------------------------------//
    }

    private void setupOfficeHoursColumn(Object columnId, TableView tableView, String styleClass, String columnDataProperty) {
        AppNodesBuilder builder = app.getGUIModule().getNodesBuilder();
        TableColumn<TeachingAssistantPrototype, String> column = builder.buildTableColumn(columnId, tableView, styleClass);
        column.setCellValueFactory(new PropertyValueFactory<TeachingAssistantPrototype, String>(columnDataProperty));
        column.prefWidthProperty().bind(tableView.widthProperty().multiply(1.0 / 7.0));
        column.setCellFactory(col -> {
            return new TableCell<TeachingAssistantPrototype, String>() {
                @Override
                protected void updateItem(String text, boolean empty) {
                    super.updateItem(text, empty);
                    if (text == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        // CHECK TO SEE IF text CONTAINS THE NAME OF
                        // THE CURRENTLY SELECTED TA
                        setText(text);
                        TableView<TeachingAssistantPrototype> tasTableView = (TableView) app.getGUIModule().getGUINode(OH_TAS_TABLE_VIEW);
                        TeachingAssistantPrototype selectedTA = tasTableView.getSelectionModel().getSelectedItem();
                        if (selectedTA == null) {
                            setStyle("");
                        } else if (text.contains(selectedTA.getName())) {
                            setStyle("-fx-background-color: yellow");
                        } else {
                            setStyle("");
                        }
                    }
                }
            };
        });
    }

    public void initControllers() {
        CSGController controller = new CSGController((CSGApp) app);
        AppGUIModule gui = app.getGUIModule();

        // FOOLPROOF DESIGN STUFF
        TextField nameTextField = ((TextField) gui.getGUINode(OH_NAME_TEXT_FIELD));
        TextField emailTextField = ((TextField) gui.getGUINode(OH_EMAIL_TEXT_FIELD));

        nameTextField.textProperty().addListener(e -> {
            controller.processTypeTA();
        });
        emailTextField.textProperty().addListener(e -> {
            controller.processTypeTA();
        });

        // FIRE THE ADD EVENT ACTION
        nameTextField.setOnAction(e -> {
            controller.processAddTA();
        });
        emailTextField.setOnAction(e -> {
            controller.processAddTA();
        });
        ((Button) gui.getGUINode(OH_ADD_TA_BUTTON)).setOnAction(e -> {
            controller.processAddTA();
        });
        ((Button) gui.getGUINode(OH_REMOVE_TA_BUTTON)).setOnAction(e->{
            controller.processRemoveTA();
        });

        TableView officeHoursTableView = (TableView) gui.getGUINode(OH_OFFICE_HOURS_TABLE_VIEW);
        officeHoursTableView.getSelectionModel().setCellSelectionEnabled(true);
        officeHoursTableView.setOnMouseClicked(e -> {
            controller.processToggleOfficeHours();
        });

        // DON'T LET ANYONE SORT THE TABLES
        TableView tasTableView = (TableView) gui.getGUINode(OH_TAS_TABLE_VIEW);
        for (int i = 0; i < officeHoursTableView.getColumns().size(); i++) {
            ((TableColumn) officeHoursTableView.getColumns().get(i)).setSortable(false);
        }
        for (int i = 0; i < tasTableView.getColumns().size(); i++) {
            ((TableColumn) tasTableView.getColumns().get(i)).setSortable(false);
        }

        tasTableView.setOnMouseClicked(e -> {
            app.getFoolproofModule().updateAll();
            if (e.getClickCount() == 2) {
                controller.processEditTA();
            }
            controller.processSelectTA();
        });

        RadioButton allRadio = (RadioButton) gui.getGUINode(OH_ALL_RADIO_BUTTON);
        allRadio.setOnAction(e -> {
            controller.processSelectAllTAs();
        });
        RadioButton gradRadio = (RadioButton) gui.getGUINode(OH_GRAD_RADIO_BUTTON);
        gradRadio.setOnAction(e -> {
            controller.processSelectGradTAs();
        });
        RadioButton undergradRadio = (RadioButton) gui.getGUINode(OH_UNDERGRAD_RADIO_BUTTON);
        undergradRadio.setOnAction(e -> {
            controller.processSelectUndergradTAs();
        });
        
        ComboBox timeStart = (ComboBox)gui.getGUINode(OH_STARTTIME_COMBO_BOX);
        ComboBox timeEnd = (ComboBox)gui.getGUINode(OH_ENDTIME_COMBO_BOX);
        timeStart.setOnAction(e->{
//            controller.processUpdateEndOptions();
            controller.processUpdateOHTable();
        });
        timeEnd.setOnAction(ev->{
//            controller.processUpdateStartOptions();
            controller.processUpdateOHTable();
        });
        
        ComboBox courseName = (ComboBox)gui.getGUINode(SITE_SUBJECT_COMBO_BOX);
        courseName.setOnAction(e->{
            controller.processCourseName();
            controller.processExportURL();
        });
        ComboBox courseNum = (ComboBox)gui.getGUINode(SITE_SUBJECTNUM_COMBO_BOX);
        courseNum.setOnAction(e->{
            controller.processCourseNum();
            controller.processExportURL();
        });
        ComboBox courseSem = (ComboBox)gui.getGUINode(SITE_SEMESTERS_COMBO_BOX);
        courseSem.setOnAction(e->{
            controller.processCourseSem();
            controller.processExportURL();
        });
        ComboBox courseYear = (ComboBox)gui.getGUINode(SITE_YEARS_COMBO_BOX);
        courseYear.setOnAction(e->{
            controller.processCourseYear();
            controller.processExportURL();
        });
        TextField courseTitle =(TextField)gui.getGUINode(SITE_TITLE_TEXT_FIELD);
        courseTitle.setOnAction(e->{
            controller.processCourseTitle();
        });
        CheckBox courseHomeCB = (CheckBox)gui.getGUINode(SITE_HOME_CHECK_BOX);
        courseHomeCB.setOnAction(e->{
            controller.processCheckedOptions();
        });
        CheckBox courseSyllabusCB = (CheckBox)gui.getGUINode(SITE_SYLLABUS_CHECK_BOX);
        courseSyllabusCB.setOnAction(e->{
            controller.processCheckedOptions();
        });
        CheckBox courseScheduleCB = (CheckBox)gui.getGUINode(SITE_SCHEDULE_CHECK_BOX);
        courseScheduleCB.setOnAction(e->{
            controller.processCheckedOptions();
        });
        CheckBox courseHWCB = (CheckBox)gui.getGUINode(SITE_HW_CHECK_BOX);
        courseHWCB.setOnAction(e->{
            controller.processCheckedOptions();
        });
        ComboBox siteCSS = (ComboBox)gui.getGUINode(SITE_CSS_COMBO_BOX);
        siteCSS.setOnAction(e->{
            controller.processSiteCSS();
        });
        TextField instructorName = (TextField)gui.getGUINode(SITE_NAME_TEXT_FIELD);
        instructorName.setOnAction(e->{
            controller.processInstructorName();
        });
        TextField instructorEmail = (TextField)gui.getGUINode(SITE_EMAIL_TEXT_FIELD);
        instructorEmail.setOnAction(e->{
            controller.processInstructorEmail();
        });
        TextField instructorRoom = (TextField)gui.getGUINode(SITE_ROOM_TEXT_FIELD);
        instructorRoom.setOnAction(e->{
            controller.processInstructorRoom();
        });
        TextField instructorHP = (TextField)gui.getGUINode(SITE_HP_TEXT_FIELD);
        instructorHP.setOnAction(e->{
            controller.processInstructorHP();
        });
        
    }

    public void initFoolproofDesign() {
        AppGUIModule gui = app.getGUIModule();
        AppFoolproofModule foolproofSettings = app.getFoolproofModule();
        foolproofSettings.registerModeSettings(OH_FOOLPROOF_SETTINGS,
                new CSGFoolproofDesign((CSGApp) app));
    }

    @Override
    public void processWorkspaceKeyEvent(KeyEvent ke) {
        // WE AREN'T USING THIS FOR THIS APPLICATION
    }

    @Override
    public void showNewDialog() {
        // WE AREN'T USING THIS FOR THIS APPLICATION
    }
    
    public ImageView getFviImgView() {
        return fviImgView;
    }

    public void setFviImgView(ImageView fviImgView) {
        this.fviImgView = fviImgView;
    }

    public ImageView getNavImgView() {
        return navImgView;
    }

    public void setNavImgView(ImageView navImgView) {
        this.navImgView = navImgView;
    }

    public ImageView getLeftImgView() {
        return leftImgView;
    }

    public void setLeftImgView(ImageView leftImgView) {
        this.leftImgView = leftImgView;
    }

    public ImageView getRightImgView() {
        return rightImgView;
    }

    public void setRightImgView(ImageView rightImgView) {
        this.rightImgView = rightImgView;
    }
    
    public TextArea getInstructorOHJsonArea() {
        return instructorOHJsonArea;
    }

    public void setInstructorOHJsonArea(TextArea instructorOHJsonArea) {
        this.instructorOHJsonArea = instructorOHJsonArea;
    }
}
