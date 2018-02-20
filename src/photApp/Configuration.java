package photApp;

import java.io.*;
import java.util.ArrayList;
import java.util.Optional;

public class Configuration {

    private String DBName;
    private String vidHomePage;
    private String docHomePage;

    public Configuration() throws FileNotFoundException {

        java.util.Properties prop = new java.util.Properties();
        Boolean configDir = false;
        Boolean configFileExists = false;
        Boolean makeNewConfigFile = false;
        InputStream input = null;

        File tmpDir = new File(System.getProperty("user.home") + File.separator + "Documents/PhotAppConfig");
        //set boolean variable
        if (tmpDir.exists() && tmpDir.isDirectory()) {
            configDir = tmpDir.exists();
        }
        //Check if PhotAppConfig Folder in User Docs exists
        if (configDir) {
            System.out.println("PhotAppConfig Folder Exists");
        }
        //Make the Directory
        else{
            tmpDir.mkdirs();
            System.out.println("PhotAppConfig Folder was created");
            makeNewConfigFile = true;
        }

        // test to see if a file exists
        File configFile = new File(tmpDir + File.separator + "config.Properties");
        configFileExists = configFile.exists();
        if (configFile.exists() && configFile.isFile())
        {
            input = new FileInputStream(configFile);
            System.out.println("config.Properties exists, and it is a file");
        }
        else{
            input = Properties.class.getResourceAsStream("/config.properties");
            System.out.println("Reading config File from Resource Folder");
        }


        try {

            prop.load(input);
            DBName =      prop.get("database").toString();
            vidHomePage = prop.get("vidHome").toString();
            docHomePage = prop.get("docHome").toString();

if(makeNewConfigFile){
    //create a new Properties file from prop
                OutputStream output = null;
            try {
                output = new FileOutputStream(new File(tmpDir + File.separator + "config.Properties"));
                prop.store(output, "null");

            } catch (IOException io) {
                io.printStackTrace();
            } finally {
                if (output != null) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    /*******************************************************************************************************************
     Method update Configuration File
     Displays a titles properties
     ******************************************************************************************************************/
    public static void updateConfigFile(Optional<ArrayList> vars) throws FileNotFoundException {

        File configFile = new File(System.getProperty("user.home") + File.separator + "Documents/PhotAppConfig/config.Properties");
        String database = "jdbc:sqlite:" + (String) vars.get().get(0);
        String vidHome = (String) vars.get().get(1);
        String docHome = (String) vars.get().get(2);

        java.util.Properties prop = new java.util.Properties();
        OutputStream output = null;

        try {

            output = new FileOutputStream(configFile);
            //set the properties value
            prop.setProperty("database", database);
            prop.setProperty("vidHome", vidHome);
            prop.setProperty("docHome", docHome);

            // save properties to project root folder
            prop.store(output, "null");

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /*******************************************************************************************************************
     GETTERS AND SETTERS
     ******************************************************************************************************************/
    public String getDBName() {
        return DBName;
    }

    public void setDBName(String DBName) {
        this.DBName = DBName;
    }

    public String getVidHomePage() {
        return vidHomePage;
    }

    public void setVidHomePage(String vidHomePage) {
        this.vidHomePage = vidHomePage;
    }

    public String getDocHomePage() {
        return docHomePage;
    }

    public void setDocHomePage(String docHomePage) {
        this.docHomePage = docHomePage;
    }

}