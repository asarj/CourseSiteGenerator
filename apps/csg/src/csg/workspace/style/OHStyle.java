package csg.workspace.style;

/**
 * This class lists all CSS style types for this application. These
 * are used by JavaFX to apply style properties to controls like
 * buttons, labels, and panes.

 * @author Richard McKenna
 * @author ?
 * @version 1.0
 */
public class OHStyle {
    public static final String EMPTY_TEXT = "";
    public static final int BUTTON_TAG_WIDTH = 75;

    // THESE CONSTANTS ARE FOR TYING THE PRESENTATION STYLE OF
    // THIS M3Workspace'S COMPONENTS TO A STYLE SHEET THAT IT USES
    // NOTE THAT FOUR CLASS STYLES ALREADY EXIST:
    // top_toolbar, toolbar, toolbar_text_button, toolbar_icon_button
    
    public static final String CLASS_OH_PANE          = "oh_pane";
    public static final String CLASS_OH_BOX           = "oh_box";            
    public static final String CLASS_OH_HEADER_LABEL  = "oh_header_label";
    public static final String CLASS_OH_PROMPT        = "oh_prompt";
    public static final String CLASS_OH_TEXT_FIELD    = "oh_text_field";
    public static final String CLASS_OH_TEXT_FIELD_ERROR = "oh_text_field_error";
    public static final String CLASS_OH_BUTTON        = "oh_button";
    public static final String CLASS_OH_RADIO_BOX     = "oh_radio_box";
    public static final String CLASS_OH_RADIO_BUTTON  = "oh_radio_button";
    public static final String CLASS_OH_TAB_PANE      = "oh_tab_pane";
    public static final String CLASS_OH_TABLE_VIEW    = "oh_table_view";
    public static final String CLASS_OH_COLUMN        = "oh_column";
    public static final String CLASS_OH_CENTERED_COLUMN = "oh_centered_column";
    public static final String CLASS_OH_OFFICE_HOURS_TABLE_VIEW = "oh_office_hours_table_view";
    public static final String CLASS_OH_TIME_COLUMN = "oh_time_column";
    public static final String CLASS_OH_DAY_OF_WEEK_COLUMN = "oh_day_of_week_column";
    
    // FOR THE DIALOG
    public static final String CLASS_OH_DIALOG_GRID_PANE = "oh_dialog_grid_pane";
    public static final String CLASS_OH_DIALOG_HEADER = "oh_dialog_header"; 
    public static final String CLASS_OH_DIALOG_PROMPT = "oh_dialog_prompt"; 
    public static final String CLASS_OH_DIALOG_TEXT_FIELD = "oh_dialog_text_field";
    public static final String CLASS_OH_DIALOG_RADIO_BUTTON = "oh_dialog_radio_button";
    public static final String CLASS_OH_DIALOG_BOX = "oh_dialog_box";
    public static final String CLASS_OH_DIALOG_BUTTON = "oh_dialog_button";
    
}