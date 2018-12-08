/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import csg.CSGApp;
import static csg.CSGPropertyType.DEFAULT_FAVICON_TEXT;
import csg.data.CSGData;
import csg.data.SiteData;
import static djf.AppPropertyType.APP_FILE_PROTOCOL;
import static djf.AppPropertyType.APP_PATH_IMAGES;
import djf.modules.AppGUIModule;
import javafx.scene.control.ComboBox;
import jtps.jTPS_Transaction;
import properties_manager.PropertiesManager;

/**
 *
 * @author Ajay
 */
public class SITE_EditFviImg_Transaction implements jTPS_Transaction {
    CSGApp app;
    CSGData d;
    SiteData data;
    String oldPath;
    String path;
    
    public SITE_EditFviImg_Transaction(CSGApp initApp, CSGData d, SiteData data, String path){
        app = initApp;
        this.d = d;
        this.data = data;
        this.oldPath = data.getFavUrl();
        this.path = path;
    }
    
    @Override
    public void doTransaction() {
        AppGUIModule gui = app.getGUIModule();
        data.setFavUrl(path);
    }

    @Override
    public void undoTransaction() {
        AppGUIModule gui = app.getGUIModule();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        if(this.oldPath.equals("")){
            data.setFavUrl(props.getProperty(APP_PATH_IMAGES) + props.getProperty(DEFAULT_FAVICON_TEXT));
        }
        else{
            data.setFavUrl(this.oldPath);
        }

    }
}
