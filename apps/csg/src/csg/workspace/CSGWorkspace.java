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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

/**
 *
 * @author McKillaGorilla
 */
public class CSGWorkspace extends AppWorkspaceComponent {
    
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
        VBox siteTabVBox= new VBox();
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
        ObservableList years = FXCollections.observableArrayList(2018, 2019, 2020, 2021, 2022);
        GridPane bannerBox = new GridPane();
        ComboBox subjectCBox = new ComboBox(subjects);
        ComboBox subjectNumsCBox = new ComboBox(subjectNums);
        ComboBox semestersCBox = new ComboBox(semesters);
        ComboBox yearsCBox = new ComboBox(years);
        subjectCBox.setEditable(true);
        subjectNumsCBox.setEditable(true);
        semestersCBox.setEditable(true);
        yearsCBox.setEditable(true);
        subjectCBox.getSelectionModel().selectFirst();
        subjectNumsCBox.getSelectionModel().selectFirst();
        semestersCBox.getSelectionModel().selectFirst();
        yearsCBox.getSelectionModel().selectFirst();
        
        Label bannerLabel = new Label(props.getProperty(SITE_BANNER_LABEL));
        bannerLabel.setId("section_header_label");
        Label subjectLabel = new Label(props.getProperty(SITE_SUBJECT_LABEL));
        subjectLabel.setId("reg_label");
        Label semesterLabel = new Label(props.getProperty(SITE_SEMESTER_LABEL));
        semesterLabel.setId("reg_label");
        Label subjectNumberLabel = new Label(props.getProperty(SITE_NUMBER_LABEL));
        subjectNumberLabel.setId("reg_label");
        Label yearLabel = new Label(props.getProperty(SITE_YEAR_LABEL));
        yearLabel.setId("reg_label");
        Label titleLabel = new Label(props.getProperty(SITE_TITLE_LABEL));
        titleLabel.setId("reg_label");
        Label expDirLabel = new Label(props.getProperty(SITE_EXPDIR_LABEL));
        expDirLabel.setId("reg_label");
        Label expDirOutputLabel = new Label(".\\export\\" + subjectCBox.getSelectionModel().getSelectedItem().toString() + 
                                                "_" + subjectNumsCBox.getSelectionModel().getSelectedItem().toString() +
                                                "_" + semestersCBox.getSelectionModel().getSelectedItem().toString() +
                                                "_" + yearsCBox.getSelectionModel().getSelectedItem().toString() 
                                                 + "\\public.html");
        expDirOutputLabel.setId("reg_label");
        subjectCBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override 
            public void changed(ObservableValue ov, String t, String t1) {                
                subjects.add(t1);
                expDirOutputLabel.setText(".\\export\\" + subjectCBox.getSelectionModel().getSelectedItem().toString() + 
                                                "_" + subjectNumsCBox.getSelectionModel().getSelectedItem().toString() +
                                                "_" + semestersCBox.getSelectionModel().getSelectedItem().toString() +
                                                "_" + yearsCBox.getSelectionModel().getSelectedItem().toString() 
                                                 + "\\public.html");
            }    
        });
        subjectNumsCBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override 
            public void changed(ObservableValue ov, String t, String t1) {                
                subjectNums.add(t1);
                expDirOutputLabel.setText(".\\export\\" + subjectCBox.getSelectionModel().getSelectedItem().toString() + 
                                                "_" + subjectNumsCBox.getSelectionModel().getSelectedItem().toString() +
                                                "_" + semestersCBox.getSelectionModel().getSelectedItem().toString() +
                                                "_" + yearsCBox.getSelectionModel().getSelectedItem().toString() 
                                                 + "\\public.html");
            }    
        });
        semestersCBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override 
            public void changed(ObservableValue ov, String t, String t1) {                
                semesters.add(t1);
                expDirOutputLabel.setText(".\\export\\" + subjectCBox.getSelectionModel().getSelectedItem().toString() + 
                                                "_" + subjectNumsCBox.getSelectionModel().getSelectedItem().toString() +
                                                "_" + semestersCBox.getSelectionModel().getSelectedItem().toString() +
                                                "_" + yearsCBox.getSelectionModel().getSelectedItem().toString() 
                                                 + "\\public.html");
            }    
        });
        yearsCBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override 
            public void changed(ObservableValue ov, String t, String t1) {                
                years.add(t1);
                expDirOutputLabel.setText(".\\export\\" + subjectCBox.getSelectionModel().getSelectedItem().toString() + 
                                                "_" + subjectNumsCBox.getSelectionModel().getSelectedItem().toString() +
                                                "_" + semestersCBox.getSelectionModel().getSelectedItem().toString() +
                                                "_" + yearsCBox.getSelectionModel().getSelectedItem().toString() 
                                                 + "\\public.html");
            }    
        });
        TextField titleTF = new TextField();
        titleTF.setPromptText(props.getProperty(SITE_TITLE_TEXT_FIELD_TEXT));
        bannerBox.add(bannerLabel, 0, 0);
        bannerBox.add(subjectLabel, 0, 1);
        bannerBox.add(subjectCBox, 1, 1);
        bannerBox.add(subjectNumberLabel, 2, 1);
        bannerBox.add(subjectNumsCBox, 3, 1);
        bannerBox.add(semesterLabel, 0, 2);
        bannerBox.add(semestersCBox, 1, 2);
        bannerBox.add(yearLabel, 2, 2);
        bannerBox.add(yearsCBox, 3, 2);
        bannerBox.add(titleLabel, 0, 3);
        bannerBox.add(titleTF, 1, 3);
        bannerBox.add(expDirLabel, 0, 4);
        bannerBox.add(expDirOutputLabel, 1, 4);
        bannerBox.setStyle("-fx-background-color: #ebebeb;");
        bannerBox.setPadding(new Insets(10, 10, 10, 10));
        bannerBox.setVgap(5);
        HBox blank1 = new HBox();
        blank1.setStyle("-fx-background-color: #ffc581;");
        blank1.setPadding(new Insets(5, 5, 5, 5));
        siteTabVBox.getChildren().addAll(bannerBox, blank1);
        
        GridPane pagesBox = new GridPane();
        CheckBox homeCB = new CheckBox();
        CheckBox syllabusCB = new CheckBox();
        CheckBox schCB = new CheckBox();
        CheckBox hwCB = new CheckBox();
        homeCB.setIndeterminate(false);
        syllabusCB.setIndeterminate(false);
        schCB.setIndeterminate(false);
        hwCB.setIndeterminate(false);
        Label pagesLabel = new Label(props.getProperty(SITE_PAGES_LABEL));
        pagesLabel.setId("section_header_label");
        Label homeLabel = new Label(props.getProperty(SITE_HOME_LABEL));
        homeLabel.setId("reg_label");
        Label syllabusLabel = new Label(props.getProperty(SITE_SYLLABUS_LABEL));
        syllabusLabel.setId("reg_label");
        Label scheduleLabel = new Label(props.getProperty(SITE_SCHEDULE_LABEL));
        scheduleLabel.setId("reg_label");
        Label hwLabel = new Label(props.getProperty(SITE_HWS_LABEL));
        hwLabel.setId("reg_label");
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
        pagesBox.add(pagesLabel, 0, 0);
        pagesBox.add(checkBoxHBox, 0, 1);
        pagesBox.setStyle("-fx-background-color: #ebebeb;");
        pagesBox.setPadding(new Insets(10, 10, 10, 10));
        pagesBox.setVgap(5);
        HBox blank2 = new HBox();
        blank2.setStyle("-fx-background-color: #ffc581;");
        blank2.setPadding(new Insets(5,5,5,5));
        siteTabVBox.getChildren().addAll(pagesBox, blank2);
        
        GridPane styleBox = new GridPane();
        ObservableList styleSheets = FXCollections.observableArrayList("seawolf.css");
        Label styleLabel = new Label(props.getProperty(SITE_STYLE_LABEL));
        styleLabel.setId("section_header_label");
        Label fontLabel = new Label(props.getProperty(SITE_FONTSTYLE_LABEL));
        fontLabel.setId("reg_label");
        Label fontWarningLabel = new Label(props.getProperty(SITE_FONTSTYLENOTE_LABEL));
        fontWarningLabel.setId("reg_label");
        fontWarningLabel.setStyle("-fx-font-weight: bold");
        Button fviButton = new Button(props.getProperty(SITE_FAVICON_BUTTON));
        fviButton.setId("app_button");
        Button navbarButton = new Button(props.getProperty(SITE_NAVBAR_BUTTON));
        navbarButton.setId("app_button");
        Button lfimg = new Button(props.getProperty(SITE_LFIMG_BUTTON));
        lfimg.setId("app_button");
        Button rfimg = new Button(props.getProperty(SITE_RFIMG_BUTTON));
        rfimg.setId("app_button");
        
        // FIX THIS ///////////////////////////
        ImageView fviImgView = new ImageView(
                props.getProperty(APP_FILE_PROTOCOL) + props.getProperty(APP_PATH_IMAGES) + props.getProperty(DEFAULT_FAVICON_TEXT)
        );
        fviImgView.setFitWidth(25);
        fviImgView.setFitHeight(25);
        fviImgView.setPreserveRatio(true);
        fviImgView.setSmooth(true);
        fviImgView.setCache(true);
        ImageView navImgView = new ImageView(
                props.getProperty(APP_FILE_PROTOCOL) + props.getProperty(APP_PATH_IMAGES) + props.getProperty(DEFAULT_NAVBAR_TEXT)
        );
        navImgView.setFitWidth(300);
        navImgView.setFitHeight(25);
        navImgView.setPreserveRatio(true);
        navImgView.setSmooth(true);
        navImgView.setCache(true);
        ImageView leftImgView = new ImageView(
                props.getProperty(APP_FILE_PROTOCOL) + props.getProperty(APP_PATH_IMAGES) + props.getProperty(DEFAULT_LFIMG_TEXT)
        );
        leftImgView.setFitWidth(300);
        leftImgView.setFitHeight(25);
        leftImgView.setPreserveRatio(true);
        leftImgView.setSmooth(true);
        leftImgView.setCache(true);
        ImageView rightImgView = new ImageView(
               props.getProperty(APP_FILE_PROTOCOL) + props.getProperty(APP_PATH_IMAGES) + props.getProperty(DEFAULT_RFIMG_TEXT)
        );
        rightImgView.setFitWidth(300);
        rightImgView.setFitHeight(25);
        rightImgView.setPreserveRatio(true);
        rightImgView.setSmooth(true);
        rightImgView.setCache(true);
        // FIX THIS ///////////////////////////
        ComboBox css = new ComboBox(styleSheets);
        css.getSelectionModel().selectFirst();
//        VBox styleContainer = new VBox();
//        styleContainer.getChildren().addAll(styleLabel, navHBox, leftHBox, rightHBox, cssHBox, fontWarningLabel);
        styleBox.add(styleLabel, 0, 0);
        styleBox.add(fviButton, 0, 1);
        styleBox.add(fviImgView, 1, 1);
        styleBox.add(navbarButton, 0, 2);
        styleBox.add(navImgView, 1, 2);
        styleBox.add(lfimg, 0, 3);
        styleBox.add(leftImgView, 1, 3);
        styleBox.add(rfimg, 0, 4);
        styleBox.add(rightImgView, 1, 4);
        styleBox.add(fontLabel, 0, 5);
        styleBox.add(css, 1, 5);
        styleBox.add(fontWarningLabel, 0, 6);
        styleBox.setStyle("-fx-background-color: #ebebeb;");
        styleBox.setPadding(new Insets(10, 10, 10, 10));
        styleBox.setVgap(5);
        HBox blank3 = new HBox();
        blank3.setStyle("-fx-background-color: #ffc581;");
        blank3.setPadding(new Insets(5,5,5,5));
        siteTabVBox.getChildren().addAll(styleBox, blank3);
        
        
        GridPane instructorBox = new GridPane();
        Label instructorLabel = new Label(props.getProperty(SITE_INSTRUCTOR_LABEL));
        instructorLabel.setId("section_header_label");
        Label nameLabel = new Label(props.getProperty(SITE_NAME_LABEL));
        nameLabel.setId("reg_label");
        Label emailLabel = new Label(props.getProperty(SITE_EMAIL_LABEL));
        emailLabel.setId("reg_label");
        Label roomLabel = new Label(props.getProperty(SITE_ROOM_LABEL));
        roomLabel.setId("reg_label");
        Label hpLabel = new Label(props.getProperty(SITE_HP_LABEL));
        hpLabel.setId("reg_label");
        Label ohLabel = new Label(props.getProperty(SITE_OH_LABEL));
        ohLabel.setId("section_header_label");
        TextField siteNameTF = new TextField();
        siteNameTF.setPromptText(props.getProperty(SITE_NAME_TEXT_FIELD_TEXT));
        TextField siteEmailTF = new TextField();
        siteEmailTF.setPromptText(props.getProperty(SITE_EMAIL_TEXT_FIELD_TEXT));
        TextField siteRoomTF = new TextField();
        siteRoomTF.setPromptText(props.getProperty(SITE_ROOM_TEXT_FIELD_TEXT));
        TextField siteHPTF = new TextField();
        siteHPTF.setPromptText(props.getProperty(SITE_HP_TEXT_FIELD_TEXT));
        
        Button siteInstructorOHExpandButton = new Button(props.getProperty(SITE_EXPAND_BUTTON));
        siteInstructorOHExpandButton.setId("app_button");
        TextArea instructorOHJsonArea = new TextArea();

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
//        HBox instDetail = new HBox();
//        HBox emailDetail = new HBox();
//        HBox roomDetail = new HBox();
//        HBox hpDetail = new HBox();
        HBox ohDetail = new HBox();
        VBox hiddenTA = new VBox();
        Label emptyLbl = new Label();
//        
//        instDetail.getChildren().addAll(nameLabel, siteNameTF);
//        emailDetail.getChildren().addAll(emailLabel, siteEmailTF);
//        roomDetail.getChildren().addAll(roomLabel, siteRoomTF);
//        hpDetail.getChildren().addAll(hpLabel, siteHPTF);
        ohDetail.getChildren().addAll(siteInstructorOHExpandButton, ohLabel);
        ohDetail.setStyle("-fx-background-color: #ebebeb;");
        ohDetail.setPadding(new Insets(10, 10, 10, 10));
        hiddenTA.getChildren().addAll(emptyLbl, instructorOHJsonArea);
        hiddenTA.setStyle("-fx-background-color: #ebebeb;");
//        instructorBox.add(instructorLabel, 0, 0);
//        instructorBox.add(instDetail, 0, 1);
//        instructorBox.add(emailDetail, 0, 2);
//        instructorBox.add(roomDetail, 1, 1);
//        instructorBox.add(hpDetail, 1, 2);
//        instructorBox.add(ohDetail, 0, 3);
        instructorBox.add(instructorLabel, 0, 0);
        instructorBox.add(nameLabel, 0, 1);
        instructorBox.add(siteNameTF, 1, 1);
        instructorBox.add(roomLabel, 2, 1);
        instructorBox.add(siteRoomTF, 3, 1);
        instructorBox.add(emailLabel, 0, 2);
        instructorBox.add(siteEmailTF, 1, 2);
        instructorBox.add(hpLabel, 2, 2);
        instructorBox.add(siteHPTF, 3, 2);
        instructorBox.setStyle("-fx-background-color: #ebebeb;");
        instructorBox.setPadding(new Insets(10, 10, 10, 10));
        siteTabVBox.getChildren().addAll(instructorBox, ohDetail, hiddenTA);
        


        siteTabVBox.setStyle("-fx-background-color: #ffc581;");
        siteTabVBox.setPadding(new Insets(10, 10, 10, 10));
        ScrollPane siteTabScrollPane = new ScrollPane();
        siteTabScrollPane.setContent(siteTabVBox);
//        siteTabScrollPane.setFitToHeight(true);
        siteTabScrollPane.setFitToWidth(true);
        //--------------------------------------------------------------------------------------------------------------------//
        
        
        //----------------------------------------------SETS UP THE SYLLABUS TAB--------------------------------------------------//
        VBox syllabusTabVBox = new VBox();
        GridPane descBox = new GridPane();
        descBox.setStyle("-fx-background-color: #ebebeb;");
        Label sylDesc = new Label(props.getProperty(SYL_DESC_LABEL));
        sylDesc.setId("section_header_label");
        Button sylDescExpandButton = new Button("+");
        TextArea descTA = new TextArea();
        descTA.setVisible(false);
        descTA.setManaged(false);
        sylDescExpandButton.setOnAction(e->{
            sylDescExpandButton.setText(sylDescExpandButton.getText().equals("+") ? "-": "+");
            descTA.setManaged(sylDescExpandButton.getText().equals("-")? true: false);
            descTA.setVisible(sylDescExpandButton.getText().equals("-")? true: false);
        });
        HBox descDetail = new HBox();
        descDetail.getChildren().addAll(sylDescExpandButton, sylDesc);
        descBox.add(descDetail, 0, 1);
        descBox.setPadding(new Insets(10, 10, 10, 10));
        descBox.setVgap(5);
        HBox blank4 = new HBox();
        blank4.setStyle("-fx-background-color: #ffc581;");
        blank4.setPadding(new Insets(5,5,5,5));
        syllabusTabVBox.getChildren().addAll(descBox, descTA, blank4);
        
        GridPane topicBox = new GridPane();
        topicBox.setStyle("-fx-background-color: #ebebeb;");
        Label sylTopic = new Label(props.getProperty(SYL_TOPICS_LABEL));
        sylTopic.setId("section_header_label");
        Button sylTopicExpandButton = new Button("+");
        TextArea topicTA = new TextArea();
        topicTA.setText("["
                        + "\n\t\"\""
                    + "\n],");
        topicTA.setVisible(false);
        topicTA.setManaged(false);
        sylTopicExpandButton.setOnAction(e->{
            sylTopicExpandButton.setText(sylTopicExpandButton.getText().equals("+") ? "-": "+");
            topicTA.setManaged(sylTopicExpandButton.getText().equals("-")? true: false);
            topicTA.setVisible(sylTopicExpandButton.getText().equals("-")? true: false);
        });
        HBox topicDetail = new HBox();
        topicDetail.getChildren().addAll(sylTopicExpandButton, sylTopic);
        topicBox.add(topicDetail, 0, 1);
        topicBox.setPadding(new Insets(10, 10, 10, 10));
        topicBox.setVgap(5);
        HBox blank5 = new HBox();
        blank5.setStyle("-fx-background-color: #ffc581;");
        blank5.setPadding(new Insets(5,5,5,5));
        
        syllabusTabVBox.getChildren().addAll(topicBox, topicTA, blank5);
        
        GridPane prereqBox = new GridPane();
        prereqBox.setStyle("-fx-background-color: #ebebeb;");
        Label prereqLabel = new Label(props.getProperty(SYL_PREREQ_LABEL));
        prereqLabel.setId("section_header_label");
        Button sylPrereqExpandButton = new Button("+");
        TextArea prereqTA = new TextArea();
        prereqTA.setVisible(false);
        prereqTA.setManaged(false);
        sylPrereqExpandButton.setOnAction(e->{
            sylPrereqExpandButton.setText(sylPrereqExpandButton.getText().equals("+") ? "-": "+");
            prereqTA.setManaged(sylPrereqExpandButton.getText().equals("-")? true: false);
            prereqTA.setVisible(sylPrereqExpandButton.getText().equals("-")? true: false);
        });
        HBox prereqDetail = new HBox();
        prereqDetail.getChildren().addAll(sylPrereqExpandButton, prereqLabel);
        prereqBox.add(prereqDetail, 0, 1);
        prereqBox.setPadding(new Insets(10, 10, 10, 10));
        prereqBox.setVgap(5);
        HBox blank6 = new HBox();
        blank6.setStyle("-fx-background-color: #ffc581;");
        blank6.setPadding(new Insets(5,5,5,5));
        syllabusTabVBox.getChildren().addAll(prereqBox, prereqTA, blank6);
        
        GridPane outcomesBox = new GridPane();
        outcomesBox.setStyle("-fx-background-color: #ebebeb;");
        Label outcomesLabel = new Label(props.getProperty(SYL_OUTCOMES_LABEL));
        outcomesLabel.setId("section_header_label");
        Button sylOutcomesExpandButton = new Button("+");
        TextArea outcomesTA = new TextArea();
        outcomesTA.setText("["
                            + "\n\t\"\""
                        + "\n],");
        outcomesTA.setVisible(false);
        outcomesTA.setManaged(false);
        sylOutcomesExpandButton.setOnAction(e->{
            sylOutcomesExpandButton.setText(sylOutcomesExpandButton.getText().equals("+") ? "-": "+");
            outcomesTA.setManaged(sylOutcomesExpandButton.getText().equals("-")? true: false);
            outcomesTA.setVisible(sylOutcomesExpandButton.getText().equals("-")? true: false);
        });
        HBox outcomesDetail = new HBox();
        outcomesDetail.getChildren().addAll(sylOutcomesExpandButton, outcomesLabel);
        outcomesBox.add(outcomesDetail, 0, 1);
        outcomesBox.setPadding(new Insets(10, 10, 10, 10));
        outcomesBox.setVgap(5);
        HBox blank7 = new HBox();
        blank7.setStyle("-fx-background-color: #ffc581;");
        blank7.setPadding(new Insets(5,5,5,5));
        syllabusTabVBox.getChildren().addAll(outcomesBox, outcomesTA, blank7);
        
        GridPane textbookBox = new GridPane();
        textbookBox.setStyle("-fx-background-color: #ebebeb;");
        Label textbookLabel = new Label(props.getProperty(SYL_TBOOK_LABEL));
        textbookLabel.setId("section_header_label");
        Button sylTextbooksExpandButton = new Button("+");
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
        sylTextbooksExpandButton.setOnAction(e->{
            sylTextbooksExpandButton.setText(sylTextbooksExpandButton.getText().equals("+") ? "-": "+");
            textbooksTA.setManaged(sylTextbooksExpandButton.getText().equals("-")? true: false);
            textbooksTA.setVisible(sylTextbooksExpandButton.getText().equals("-")? true: false);
        });
        HBox textbooksDetail = new HBox();
        textbooksDetail.getChildren().addAll(sylTextbooksExpandButton, textbookLabel);
        textbookBox.add(textbooksDetail, 0, 1);
        textbookBox.setPadding(new Insets(10, 10, 10, 10));
        textbookBox.setVgap(5);
        HBox blank8 = new HBox();
        blank8.setStyle("-fx-background-color: #ffc581;");
        blank8.setPadding(new Insets(5,5,5,5));
        syllabusTabVBox.getChildren().addAll(textbookBox, textbooksTA, blank8); 
        
        GridPane gradedComponentsBox = new GridPane();
        gradedComponentsBox.setStyle("-fx-background-color: #ebebeb;");
        Label gradedComponentsLabel = new Label(props.getProperty(SYL_GRADED_LABEL));
        gradedComponentsLabel.setId("section_header_label");
        Button sylGCExpandButton = new Button("+");
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
        sylGCExpandButton.setOnAction(e->{
            sylGCExpandButton.setText(sylGCExpandButton.getText().equals("+") ? "-": "+");
            gcTA.setManaged(sylGCExpandButton.getText().equals("-")? true: false);
            gcTA.setVisible(sylGCExpandButton.getText().equals("-")? true: false);
        });
        HBox gcDetail = new HBox();
        gcDetail.getChildren().addAll(sylGCExpandButton, gradedComponentsLabel);
        gradedComponentsBox.add(gcDetail, 0, 1);
        gradedComponentsBox.setPadding(new Insets(10, 10, 10, 10));
        gradedComponentsBox.setVgap(5);
        HBox blank9 = new HBox();
        blank9.setStyle("-fx-background-color: #ffc581;");
        blank9.setPadding(new Insets(5,5,5,5));
        syllabusTabVBox.getChildren().addAll(gradedComponentsBox, gcTA, blank9);
        
        GridPane gradingNoteBox = new GridPane();
        gradingNoteBox.setStyle("-fx-background-color: #ebebeb;");
        Label gradingNoteLabel = new Label(props.getProperty(SYL_GRADINGNOTE_LABEL));
        gradingNoteLabel.setId("section_header_label");
        Button sylGradingNoteExpandButton = new Button("+");
        TextArea gradingNoteTA = new TextArea();
        gradingNoteTA.setVisible(false);
        gradingNoteTA.setManaged(false);
        sylGradingNoteExpandButton.setOnAction(e->{
            sylGradingNoteExpandButton.setText(sylGradingNoteExpandButton.getText().equals("+") ? "-": "+");
            gradingNoteTA.setManaged(sylGradingNoteExpandButton.getText().equals("-")? true: false);
            gradingNoteTA.setVisible(sylGradingNoteExpandButton.getText().equals("-")? true: false);
        });
        HBox gradingNoteDetail = new HBox();
        gradingNoteDetail.getChildren().addAll(sylGradingNoteExpandButton, gradingNoteLabel);
        gradingNoteBox.add(gradingNoteDetail, 0, 1);
        gradingNoteBox.setPadding(new Insets(10, 10, 10, 10));
        gradingNoteBox.setVgap(5);
        HBox blank10 = new HBox();
        blank10.setStyle("-fx-background-color: #ffc581;");
        blank10.setPadding(new Insets(5,5,5,5));
        syllabusTabVBox.getChildren().addAll(gradingNoteBox, gradingNoteTA, blank10);
        
        GridPane adBox = new GridPane();
        adBox.setStyle("-fx-background-color: #ebebeb;");
        Label adLabel = new Label(props.getProperty(SYL_AD_LABEL));
        adLabel.setId("section_header_label");
        Button sylADExpandButton = new Button("+");
        TextArea adTA = new TextArea();
        adTA.setVisible(false);
        adTA.setManaged(false);
        sylADExpandButton.setOnAction(e->{
            sylADExpandButton.setText(sylADExpandButton.getText().equals("+") ? "-": "+");
            adTA.setManaged(sylADExpandButton.getText().equals("-")? true: false);
            adTA.setVisible(sylADExpandButton.getText().equals("-")? true: false);
        });
        HBox adDetail = new HBox();
        adDetail.getChildren().addAll(sylADExpandButton, adLabel);
        adBox.add(adDetail, 0, 1);
        adBox.setPadding(new Insets(10, 10, 10, 10));
        adBox.setVgap(5);
        HBox blank11 = new HBox();
        blank11.setStyle("-fx-background-color: #ffc581;");
        blank11.setPadding(new Insets(5,5,5,5));
        syllabusTabVBox.getChildren().addAll(adBox, adTA, blank11);
        
        GridPane saBox = new GridPane();
        saBox.setStyle("-fx-background-color: #ebebeb;");
        Label saLabel = new Label(props.getProperty(SYL_SA_LABEL));
        saLabel.setId("section_header_label");
        Button sylSAExpandButton = new Button("+");
        TextArea saTA = new TextArea();
        saTA.setVisible(false);
        saTA.setManaged(false);
        sylSAExpandButton.setOnAction(e->{
            sylSAExpandButton.setText(sylSAExpandButton.getText().equals("+") ? "-": "+");
            saTA.setManaged(sylSAExpandButton.getText().equals("-")? true: false);
            saTA.setVisible(sylSAExpandButton.getText().equals("-")? true: false);
        });
        HBox saDetail = new HBox();
        saDetail.getChildren().addAll(sylSAExpandButton, saLabel);
        saBox.add(saDetail, 0, 1);
        saBox.setPadding(new Insets(10, 10, 10, 10));
        saBox.setVgap(5);
        syllabusTabVBox.getChildren().addAll(saBox, saTA);  
       
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
            controller.processUpdateOHTable();
        });
        timeEnd.setOnAction(ev->{
            controller.processUpdateOHTable();
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
}
