/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;

import csg.CSGApp;

/**
 *
 * @author Ajay
 */
public class SyllabusData {
    CSGApp app;
    private String descriptionJSON;
    private String topicsJSON;
    private String prereqJSON;
    private String outcomesJSON;
    private String textbooksJSON;
    private String gcJSON;
    private String gnJSON;
    private String adJSON;
    private String saJSON;
    
    public SyllabusData(CSGApp initApp) {
        app = initApp;
        this.descriptionJSON = "";
        this.topicsJSON = "";
        this.prereqJSON = "";
        this.outcomesJSON = "";
        this.textbooksJSON = "";
        this.gcJSON = "";
        this.gnJSON = "";
        this.adJSON = "";
        this.saJSON = "";
    }
    
    public String getDescriptionJSON() {
        return descriptionJSON;
    }

    public void setDescriptionJSON(String descriptionJSON) {
        this.descriptionJSON = descriptionJSON;
    }

    public String getTopicsJSON() {
        return topicsJSON;
    }

    public void setTopicsJSON(String topicsJSON) {
        this.topicsJSON = topicsJSON;
    }

    public String getPrereqJSON() {
        return prereqJSON;
    }

    public void setPrereqJSON(String prereqJSON) {
        this.prereqJSON = prereqJSON;
    }

    public String getOutcomesJSON() {
        return outcomesJSON;
    }

    public void setOutcomesJSON(String outcomesJSON) {
        this.outcomesJSON = outcomesJSON;
    }

    public String getTextbooksJSON() {
        return textbooksJSON;
    }

    public void setTextbooksJSON(String textbooksJSON) {
        this.textbooksJSON = textbooksJSON;
    }

    public String getGcJSON() {
        return gcJSON;
    }

    public void setGcJSON(String gcJSON) {
        this.gcJSON = gcJSON;
    }

    public String getGnJSON() {
        return gnJSON;
    }

    public void setGnJSON(String gnJSON) {
        this.gnJSON = gnJSON;
    }

    public String getAdJSON() {
        return adJSON;
    }

    public void setAdJSON(String adJSON) {
        this.adJSON = adJSON;
    }

    public String getSaJSON() {
        return saJSON;
    }

    public void setSaJSON(String saJSON) {
        this.saJSON = saJSON;
    }
    
}
