/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import csg.CSGApp;
import static csg.CSGPropertyType.DEFAULT_LFIMG_TEXT;
import static csg.CSGPropertyType.DEFAULT_NAVBAR_TEXT;
import csg.data.CSGData;
import csg.data.SiteData;
import static djf.AppPropertyType.APP_PATH_IMAGES;
import djf.modules.AppGUIModule;
import javafx.scene.control.ComboBox;
import jtps.jTPS_Transaction;
import properties_manager.PropertiesManager;

/**
 *
 * @author Ajay
 */
public class SITE_EditLeftImg_Transaction implements jTPS_Transaction {
    CSGApp app;
    CSGData d;
    SiteData data;
    String oldPath;
    String path;
    
    public SITE_EditLeftImg_Transaction(CSGApp initApp, CSGData d, SiteData data, String path){
        app = initApp;
        this.d = d;
        this.data = data;
        this.oldPath = data.getLeftUrl();
        this.path = path;
    }
    
    @Override
    public void doTransaction() {
        AppGUIModule gui = app.getGUIModule();
        data.setLeftUrl(path);
    }

    @Override
    public void undoTransaction() {
        AppGUIModule gui = app.getGUIModule();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        if(this.oldPath.equals("")){
            data.setLeftUrl(props.getProperty(APP_PATH_IMAGES) + props.getProperty(DEFAULT_LFIMG_TEXT));
        }
        else{
            data.setLeftUrl(this.oldPath);
        }

    }
}
