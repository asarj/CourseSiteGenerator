package csg.workspace;

import djf.components.AppWorkspaceComponent;
import djf.modules.AppFoolproofModule;
import djf.modules.AppGUIModule;
import static djf.modules.AppGUIModule.ENABLED;
import djf.ui.AppNodesBuilder;
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
import csg.data.TeachingAssistantPrototype;
import csg.data.TimeSlot;
import csg.workspace.controllers.CSGController;
import csg.workspace.dialogs.TADialog;
import csg.workspace.foolproof.CSGFoolproofDesign;
import static csg.workspace.style.OHStyle.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
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
        AppNodesBuilder ohBuilder = app.getGUIModule().getNodesBuilder();
        
        
        
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
        VBox tabVBox = new VBox();
        
        tabs.getTabs().addAll(siteTab, syllabusTab, mtTab, ohTab, scheduleTab);
        tabVBox.getChildren().addAll(tabs);
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
        subjectCBox.getSelectionModel().selectFirst();
        subjectNumsCBox.getSelectionModel().selectFirst();
        semestersCBox.getSelectionModel().selectFirst();
        yearsCBox.getSelectionModel().selectFirst();
        Label bannerLabel = new Label(props.getProperty(SITE_BANNER_LABEL));
        Label subjectLabel = new Label(props.getProperty(SITE_SUBJECT_LABEL));
        Label semesterLabel = new Label(props.getProperty(SITE_SEMESTER_LABEL));
        Label subjectNumberLabel = new Label(props.getProperty(SITE_NUMBER_LABEL));
        Label yearLabel = new Label(props.getProperty(SITE_YEAR_LABEL));
        Label titleLabel = new Label(props.getProperty(SITE_TITLE_LABEL));
        Label expDirLabel = new Label(props.getProperty(SITE_EXPDIR_LABEL));
        Label expDirOutputLabel = new Label(".\\export\\CSE_219");
        TextField titleTF = new TextField();
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
        siteTabVBox.getChildren().add(bannerBox);
        
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
        Label homeLabel = new Label(props.getProperty(SITE_HOME_LABEL));
        Label syllabusLabel = new Label(props.getProperty(SITE_SYLLABUS_LABEL));
        Label scheduleLabel = new Label(props.getProperty(SITE_SCHEDULE_LABEL));
        Label hwLabel = new Label(props.getProperty(SITE_HWS_LABEL));
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
        siteTabVBox.getChildren().add(pagesBox);
        
        GridPane styleBox = new GridPane();
        ObservableList styleSheets = FXCollections.observableArrayList("seawolf.css");
        Label styleLabel = new Label(props.getProperty(SITE_STYLE_LABEL));
        Label fontLabel = new Label(props.getProperty(SITE_FONTSTYLE_LABEL));
        Label fontWarningLabel = new Label(props.getProperty(SITE_FONTSTYLENOTE_LABEL));
        Button fviButton = new Button(props.getProperty(SITE_FAVICON_BUTTON));
        Button navbarButton = new Button(props.getProperty(SITE_NAVBAR_BUTTON));
        Button lfimg = new Button(props.getProperty(SITE_LFIMG_BUTTON));
        Button rfimg = new Button(props.getProperty(SITE_RFIMG_BUTTON));
        
        // FIX THIS ///////////////////////////
        ImageView fviImgView = new ImageView(/**getClass().getResource(props.getProperty(DEFAULT_IMAGES_PATH_TEXT) + props.getProperty(DEFAULT_FAVICON_TEXT)).toExternalForm()**/);
        ImageView navImgView = new ImageView(/**getClass().getResource(props.getProperty(DEFAULT_IMAGES_PATH_TEXT) + props.getProperty(DEFAULT_NAVBAR_TEXT)).toExternalForm()**/);
        ImageView leftImgView = new ImageView(/**getClass().getResource(props.getProperty(DEFAULT_IMAGES_PATH_TEXT) + props.getProperty(DEFAULT_LFIMG_TEXT)).toExternalForm()**/);
        ImageView rightImgView = new ImageView(/**getClass().getResource(props.getProperty(DEFAULT_IMAGES_PATH_TEXT) + props.getProperty(DEFAULT_RFIMG_TEXT)).toExternalForm()**/);
        // FIX THIS ///////////////////////////
        ComboBox css = new ComboBox(styleSheets);
        css.getSelectionModel().selectFirst();
        HBox favHBox = new HBox();
        HBox navHBox = new HBox();
        HBox leftHBox = new HBox();
        HBox rightHBox = new HBox();
        HBox cssHBox = new HBox();
        rightHBox.getChildren().addAll(rfimg, rightImgView);
        favHBox.getChildren().addAll(fviButton, fviImgView);
        navHBox.getChildren().addAll(navbarButton, navImgView);
        leftHBox.getChildren().addAll(lfimg, leftImgView);
        cssHBox.getChildren().addAll(fontLabel, css);
//        VBox styleContainer = new VBox();
//        styleContainer.getChildren().addAll(styleLabel, navHBox, leftHBox, rightHBox, cssHBox, fontWarningLabel);
        styleBox.add(styleLabel, 0, 0);
        styleBox.add(favHBox, 0, 1);
//        styleBox.add(fviImgView, 1, 1);
        styleBox.add(navHBox, 0, 2);
//        styleBox.add(navImgView, 1, 2);
        styleBox.add(leftHBox, 0, 3);
//        styleBox.add(leftImgView, 1, 3);
        styleBox.add(rightHBox, 0, 4);
//        styleBox.add(rightImgView, 1, 4);
        styleBox.add(cssHBox, 0, 5);
//        styleBox.add(css, 1, 5);
        styleBox.add(fontWarningLabel, 0, 6);
        siteTabVBox.getChildren().add(styleBox);
        
        
        GridPane instructorBox = new GridPane();
        Label instructorLabel = new Label(props.getProperty(SITE_INSTRUCTOR_LABEL));
        Label nameLabel = new Label(props.getProperty(SITE_NAME_LABEL));
        Label emailLabel = new Label(props.getProperty(SITE_EMAIL_LABEL));
        Label roomLabel = new Label(props.getProperty(SITE_ROOM_LABEL));
        Label hpLabel = new Label(props.getProperty(SITE_HP_LABEL));
        Label ohLabel = new Label(props.getProperty(SITE_OH_LABEL));
        TextField siteNameTF = new TextField();
        TextField siteEmailTF = new TextField();
        TextField siteRoomTF = new TextField();
        TextField siteHPTF = new TextField();
        Button siteInstructorOHExpandButton = new Button(props.getProperty(SITE_EXPAND_BUTTON));
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
        HBox instDetail = new HBox();
        HBox emailDetail = new HBox();
        HBox roomDetail = new HBox();
        HBox hpDetail = new HBox();
        HBox ohDetail = new HBox();
        VBox hiddenTA = new VBox();
        Label emptyLbl = new Label();
        
        instDetail.getChildren().addAll(nameLabel, siteNameTF);
        emailDetail.getChildren().addAll(emailLabel, siteEmailTF);
        roomDetail.getChildren().addAll(roomLabel, siteRoomTF);
        hpDetail.getChildren().addAll(hpLabel, siteHPTF);
        ohDetail.getChildren().addAll(siteInstructorOHExpandButton, ohLabel);
        hiddenTA.getChildren().addAll(emptyLbl, instructorOHJsonArea);
        instructorBox.add(instructorLabel, 0, 0);
        instructorBox.add(instDetail, 0, 1);
        instructorBox.add(emailDetail, 0, 2);
        instructorBox.add(roomDetail, 1, 1);
        instructorBox.add(hpDetail, 1, 2);
        instructorBox.add(ohDetail, 0, 3);
        instructorBox.add(hiddenTA, 0, 4);
        siteTabVBox.getChildren().add(instructorBox);
        
        
        ScrollPane siteTabScrollPane = new ScrollPane();
        siteTabScrollPane.setContent(siteTabVBox);
        siteTabScrollPane.setFitToHeight(true);
        siteTabScrollPane.setFitToWidth(true);
        //--------------------------------------------------------------------------------------------------------------------//
        
        
        //----------------------------------------------SETS UP THE SYLLABUS TAB--------------------------------------------------//
        VBox syllabusTabVBox = new VBox();
        GridPane descBox = new GridPane();
        Label sylDesc = new Label(props.getProperty(SYL_DESC_LABEL));
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
        descBox.add(descTA, 0, 2);
        syllabusTabVBox.getChildren().add(descBox);
        
        GridPane topicBox = new GridPane();
        Label sylTopic = new Label(props.getProperty(SYL_TOPICS_LABEL));
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
        topicBox.add(topicTA, 0, 2);
        syllabusTabVBox.getChildren().add(topicBox);
        
        GridPane prereqBox = new GridPane();
        Label prereqLabel = new Label(props.getProperty(SYL_PREREQ_LABEL));
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
        prereqBox.add(prereqTA, 0, 2);
        syllabusTabVBox.getChildren().add(prereqBox);
        
        GridPane outcomesBox = new GridPane();
        Label outcomesLabel = new Label(props.getProperty(SYL_OUTCOMES_LABEL));
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
        outcomesBox.add(outcomesTA, 0, 2);
        syllabusTabVBox.getChildren().add(outcomesBox);
        
        GridPane textbookBox = new GridPane();
        Label textbookLabel = new Label(props.getProperty(SYL_TBOOK_LABEL));
        Button sylTextbooksExpandButton = new Button("+");
        TextArea textbooksTA = new TextArea();
        textbooksTA.setText("[\n\t{"
                             + "\n\t\t\"title\":\"\","
                             + "\n\t\t\"link\":\"\","
                             + "\n\t\t\"photo\":\"\","
                             + "\n\t\t\"authors\":["
                                + "\n\t\t\t\"\""
                             + "],"
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
        textbookBox.add(textbooksTA, 0, 2);
        syllabusTabVBox.getChildren().add(textbookBox); 
        
        GridPane gradedComponentsBox = new GridPane();
        Label gradedComponentsLabel = new Label(props.getProperty(SYL_GRADED_LABEL));
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
        gradedComponentsBox.add(gcTA, 0, 2);
        syllabusTabVBox.getChildren().add(gradedComponentsBox);
        
        GridPane gradingNoteBox = new GridPane();
        Label gradingNoteLabel = new Label(props.getProperty(SYL_GRADINGNOTE_LABEL));
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
        gradingNoteBox.add(gradingNoteTA, 0, 2);
        syllabusTabVBox.getChildren().add(gradingNoteBox);
        
        GridPane adBox = new GridPane();
        Label adLabel = new Label(props.getProperty(SYL_AD_LABEL));
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
        adBox.add(adTA, 0, 2);
        syllabusTabVBox.getChildren().add(adBox);
        
        GridPane saBox = new GridPane();
        Label saLabel = new Label(props.getProperty(SYL_SA_LABEL));
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
        saBox.add(saTA, 0, 2);
        syllabusTabVBox.getChildren().add(saBox);  
       
        ScrollPane syllabusTabScrollPane = new ScrollPane();
        syllabusTabScrollPane.setContent(syllabusTabVBox);
        syllabusTabScrollPane.setFitToHeight(true);
        syllabusTabScrollPane.setFitToWidth(true);
        //------------------------------------------------------------------------------------------------------------------------//        
        
        
        //----------------------------------------------SETS UP THE MEETING TIMES TAB--------------------------------------------------//
        
        //-----------------------------------------------------------------------------------------------------------------------------//
        
        
        //----------------------------------------------SETS UP THE OH TAB--------------------------------------------------//
        // INIT THE HEADER ON THE LEFT
        VBox leftPane = ohBuilder.buildVBox(OH_LEFT_PANE, null, CLASS_OH_PANE, ENABLED);
        HBox tasHeaderBox = ohBuilder.buildHBox(OH_TAS_HEADER_PANE, leftPane, CLASS_OH_BOX, ENABLED);
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
        nameColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(1.0 / 5.0));
        emailColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(2.0 / 5.0));
        slotsColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(1.0 / 5.0));
        typeColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(1.0 / 5.0));

        // ADD BOX FOR ADDING A TA
        HBox taBox = ohBuilder.buildHBox(OH_ADD_TA_PANE, leftPane, CLASS_OH_PANE, ENABLED);
        ohBuilder.buildTextField(OH_NAME_TEXT_FIELD, taBox, CLASS_OH_TEXT_FIELD, ENABLED);
        ohBuilder.buildTextField(OH_EMAIL_TEXT_FIELD, taBox, CLASS_OH_TEXT_FIELD, ENABLED);
        ohBuilder.buildTextButton(OH_ADD_TA_BUTTON, taBox, CLASS_OH_BUTTON, !ENABLED);

        // MAKE SURE IT'S THE TABLE THAT ALWAYS GROWS IN THE LEFT PANE
        VBox.setVgrow(taTable, Priority.ALWAYS);

        // INIT THE HEADER ON THE RIGHT
        VBox rightPane = ohBuilder.buildVBox(OH_RIGHT_PANE, null, CLASS_OH_PANE, ENABLED);
        HBox officeHoursHeaderBox = ohBuilder.buildHBox(OH_OFFICE_HOURS_HEADER_PANE, rightPane, CLASS_OH_PANE, ENABLED);
        ohBuilder.buildLabel(OH_OFFICE_HOURS_HEADER_LABEL, officeHoursHeaderBox, CLASS_OH_HEADER_LABEL, ENABLED);

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

        // BOTH PANES WILL NOW GO IN A SPLIT PANE
        SplitPane sPane = new SplitPane(leftPane, rightPane);
        sPane.setDividerPositions(.4);
        //----------------------------------------------------------------------------------------------------------------------------------//
        
        
        //----------------------------------------------SETS UP THE SCHEDULE TAB--------------------------------------------------//
        
        //------------------------------------------------------------------------------------------------------------------------//
        
        
        //----------------------------------------------SETS UP THE CONTENT IN EACH TAB--------------------------------------------------//
        siteTab.setContent(siteTabScrollPane);
        syllabusTab.setContent(syllabusTabScrollPane);
        ohTab.setContent(sPane);
        
        //-------------------------------------------------------------------------------------------------------------------------------//
        
        
        //----------------------------------------------PUTS EVERYTHING TOGETHER--------------------------------------------------//
        workspace = new BorderPane();

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
