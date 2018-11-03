package djf.modules;

import djf.AppTemplate;
import static djf.AppTemplate.PATH_RECENT;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author McKillaGorilla
 */
public class AppRecentWorkModule {
    // HERE'S THE APP
    AppTemplate app;
    
    HashMap<String, String> recentWorkPaths;
    LinkedList<String> recentWork;
    
    public AppRecentWorkModule(AppTemplate initApp) {
        app = initApp;
        recentWorkPaths = new HashMap();
        recentWork = new LinkedList();
    }

    final String RECENT_WORK_FILE = PATH_RECENT + "/RecentWork.txt";
    
    public String getPath(String workName) {
        return recentWorkPaths.get(workName);
    }
    
    public void loadRecentWorkList() {
        try {
            FileReader fr = new FileReader(RECENT_WORK_FILE);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            // IF THE FILE IS EMPTY JUST RETURN
            if (line != null) {
                if (line.trim().length() == 0)
                    return;
            }
            while (line != null) {
                String[] lineSplit = line.split("-");
                String work = lineSplit[0].trim();
                String path = lineSplit[1].trim();
                recentWork.add(work);
                recentWorkPaths.put(work, path);
                line = br.readLine();
            }
        }
        catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    public void saveRecentWorkList() {
        try {
            PrintWriter pw = new PrintWriter(RECENT_WORK_FILE);
            Iterator<String> it = recentWork.iterator();
            while(it.hasNext()) {
                String work = it.next();
                String path = recentWorkPaths.get(work);
                String textToSave = work + "-" + path;
                pw.print(textToSave);
                if (it.hasNext())
                    pw.print("\n");
            }
            pw.flush();
            pw.close();
        }
        catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    private void startWork(String workName, String workPath) {
        if (recentWorkPaths.containsKey(workName)) {
            recentWork.remove(workName);
        }
        else {
            recentWorkPaths.put(workName, workPath);
        }
        // AND PUT THIS WORK AT THE FRONT OF THE LIST
        recentWork.addFirst(workName);
        
        // AND MAKE SURE IT'S REGISTERED IN THE FILE
        saveRecentWorkList();
    }
    
    public Iterator<String> getWorkIterator() {
        return recentWork.iterator();
    }

    public void startWork(File workFile) {
        String workPath = new File(".").toURI().relativize(workFile.toURI()).getPath();
        String workName = workFile.getName();
        if (workName.contains(".")) {
            workName = workName.substring(0, workName.indexOf("."));
        }
        this.startWork(workName, workPath);
    }
    
    public int size() {
        return this.recentWork.size();
    }
}
