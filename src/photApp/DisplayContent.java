package photApp;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.scene.control.*;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import photApp.loaders.SQLdocDataLoader;
import photApp.loaders.SQLvideoDataLoader;

import javax.script.ScriptException;
import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.Base64;


public class DisplayContent {


    /*******************************************************************************************************************
     Method openHomePage
     opens and displays a pdf in docWebView
     ******************************************************************************************************************/
    public void openHomePage(TreeView docTreeView, TextArea docTextArea, WebView docWebView, String link) throws IOException, ClassNotFoundException {

        Boolean isHomePg = true;
        TreeItem<String> descrip = null;
        TreeItem<String> title = null;
        Content content = new Content();
        QueryDB query = new QueryDB();
        content = query.queryDB(content, link, isHomePg);

            title = content.getTitle();
            descrip = content.getDescrip();

            docTreeView.getSelectionModel().select(title);
            TextArea textArea = new TextArea(descrip.getValue());
            docTextArea.setText(textArea.getText());


        String viewer = getClass().getResource("resources/web/viewer.html").toExternalForm();
        WebEngine docEngine = docWebView.getEngine();
        docEngine.setUserStyleSheetLocation(getClass().getResource("resources/web/viewer.css").toExternalForm());
        docEngine.setJavaScriptEnabled(true);
        docEngine.load(viewer);
        docEngine.getLoadWorker()
                .stateProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue == Worker.State.SUCCEEDED) {
                        try {
                            byte[] data = FileUtils.readFileToByteArray(new File(link));
                            //Base64 from java.util
                            String base64 = Base64.getEncoder().encodeToString(data);
                            //This must be ran on FXApplicationThread
                            docEngine.executeScript("openFileFromBase64('" + base64 + "')");

                            data = null;
                            base64 = null;

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
        });

    }
    /*******************************************************************************************************************
     Method Display Home Page when home button is pressed
     opens and displays a pdf in docWebView
     ******************************************************************************************************************/
    public void displayHome(TreeView docTreeView, TreeView videoTreeView, TextArea docTextArea, TextArea videoTextArea,
                             WebView docWebView, WebView videoWebView, TabPane tabPane) throws IOException, ClassNotFoundException, SQLException {

        int paneIndex;
        boolean vidTab = false;
        Boolean isHomePg = true;
        paneIndex = tabPane.getSelectionModel().getSelectedIndex();

        if (paneIndex == 1) {//if videos tab is current
            vidTab = true;
        }

        Configuration config = new Configuration();
        String vidHomePage = config.getVidHomePage();
        String docHomePage = config.getDocHomePage();

        Content content = new Content();
        QueryDB query = new QueryDB();

            //GetProxy proxy = new GetProxy();
            //proxy.findProxyForURL(vidHomePage);

            if(vidTab){
                content = query.queryDB(content, vidHomePage,isHomePg);
                String d = content.getDescrip().getValue();
                WebEngine videoEngine = videoWebView.getEngine();
                videoEngine.load(vidHomePage);
                TextArea textArea = new TextArea(d);
                videoTextArea.setText(textArea.getText());;
            }
            else{

                content = query.queryDB(content,docHomePage,isHomePg);
                String d = content.getDescrip().getValue();

                byte[] data = FileUtils.readFileToByteArray(new File(docHomePage));
                String base64 = Base64.getEncoder().encodeToString(data);
                String finalBase6 = base64;
                docWebView.getEngine().executeScript("openFileFromBase64('" + finalBase6 + "')");
                TextArea textArea = new TextArea(d);
                docTextArea.setText(textArea.getText());;

                data = null;
                base64 = null;
            }
    }
    /*******************************************************************************************************************
     Method Display Video
     opens and displays a video in videoWebView
     ******************************************************************************************************************/
    public void displayVid(String newValue, WebView videoWebView, TextArea videoTextArea, Boolean isHomePg) {

        String selectedItem = newValue;
        String title = null;
        String link = null;
        String descrip = null;
        //String category = null;
        Content content = new Content();

        //GetProxy proxy = new GetProxy();

        //query database and get resultset
        QueryDB query = new QueryDB();
        content = query.queryDB(content,newValue,isHomePg);

        //Assign content info to String variables
        title = content.getTitle().getValue();
        link = content.getLink().getValue();
        descrip = content.getDescrip().getValue();
        //category = content.getCategory().getValue();


            if (link.startsWith("http") || link.endsWith("html") || link.endsWith(".com") || link.endsWith(".htm")&& title.contentEquals(selectedItem)) {

                //proxy.findProxyForURL(link);
                videoWebView.getEngine().load(link);

                TextArea textArea = new TextArea(descrip);
                videoTextArea.setText(textArea.getText());
            }
            //load matching title, description, and youtube link
            else {
                String errPage = getClass().getResource("resources/Error.html").toExternalForm();
                //Load error html page
                videoWebView.getEngine().load(errPage);
                TextArea textArea = new TextArea(descrip);
                videoTextArea.setText(textArea.getText());
            }

    }
    /*******************************************************************************************************************
     Method display Document
     opens and displays a pdf in docWebView
     ******************************************************************************************************************/
    public void displayDoc(String newValue, WebView docWebView, TextArea docTextArea, Boolean isHomepg) throws IOException, ScriptException, NoSuchMethodException {
        String selectedItem = newValue;
        TreeItem<String> title;
        TreeItem<String> link;
        TreeItem<String> descrip;
        TreeItem<String> category = null;
        Content content = new Content();

            QueryDB query = new QueryDB();
            content = query.queryDB(content,selectedItem,isHomepg);

        title = content.getTitle();
        link = content.getLink();
        descrip = content.getDescrip();
        category = content.getCategory();


        try {

                if (!link.getValue().endsWith(".pdf")) {
                    //Get file from resources folder
                    InputStream errPage = getClass().getResourceAsStream("/Error.pdf");
                    byte[] data = IOUtils.toByteArray(errPage);
                    //Base64 from java.util
                    String base64 = Base64.getEncoder().encodeToString(data);
                    //This must be run on FXApplicationThread
                        docWebView.getEngine().executeScript("openFileFromBase64('" + base64 + "')");
                        TextArea textArea = new TextArea(descrip.getValue());
                        docTextArea.setText(textArea.getText());
                        try{
                            errPage.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }

                //load matching title, description, and youtube link
                else if (title.getValue().contentEquals(selectedItem) || link.getValue().contentEquals(selectedItem)) {

                    //proxy.findProxyForURL(link.getValue());
                    byte[] data = FileUtils.readFileToByteArray(new File(link.getValue()));
                    //Base64 from java.util
                    String base64 = Base64.getEncoder().encodeToString(data);
                    //This must be run on FXApplicationThread
                    String finalBase6 = base64;
                    WebEngine webEngine = docWebView.getEngine();
                    webEngine.executeScript("openFileFromBase64('" + finalBase6 + "')");
                    TextArea textArea = new TextArea(descrip.getValue());
                    docTextArea.setText(textArea.getText());
                    data = null;
                    base64 = null;
                    System.gc();
                    }


                if (!selectedItem.contentEquals(category.getValue())) {
                    InputStream errPage = getClass().getResourceAsStream("/Error.pdf");
                    byte[] data = new byte[0];
                    try {
                        data = IOUtils.toByteArray(errPage);
                        errPage.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    //Base64 from java.util
                    String base64 = Base64.getEncoder().encodeToString(data);
                    //This must be run on FXApplicationThread
                    docWebView.getEngine().executeScript("openFileFromBase64('" + base64 + "')");
                    data = null;
                    base64 = null;

                }

        } catch (Exception e) {
            System.out.println(e.getMessage());
       }

    }
    /*******************************************************************************************************************
     Method Open Externally
     opens content using default OS program
     ******************************************************************************************************************/
    public void OpenExternally(TreeView docTreeView, TreeView videoTreeView,TabPane tabPane) throws URISyntaxException {
        int paneIndex;
        boolean isHomepg = false;
        QueryDB query = new QueryDB();
        Content content = new Content();
        paneIndex = tabPane.getSelectionModel().getSelectedIndex();
        TreeItem<String> selectedItem = null;

            if(paneIndex == 0) //if document tab is current
                selectedItem = (TreeItem<String>) docTreeView.getSelectionModel().getSelectedItem();

            if (paneIndex == 1) //if videos tab is current
                selectedItem = (TreeItem<String>) videoTreeView.getSelectionModel().getSelectedItem();

            content = query.queryDB(content,selectedItem.getValue(), isHomepg);
            String link = content.getLink().getValue();

                try {
                    if (!link.startsWith("http"))
                        Desktop.getDesktop().open(new File(link));
                    else
                        Desktop.getDesktop().browse(new URI(link));

                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    /*******************************************************************************************************************
     Refresh Button
     Refresh TreeView
     Builds the treeview
     ******************************************************************************************************************/
    public void RefreshTree(TabPane tabPane, TreeView docTreeView, TreeView videoTreeView,
                             TextArea docTextArea, TextArea videoTextArea) throws IOException {

        Boolean vidTab = false;
        Boolean docTab = false;
        String currentTitle = null;
        boolean isHomepg = false;
        QueryDB query = new QueryDB();
        int paneIndex;
        Content content = new Content();
        paneIndex = tabPane.getSelectionModel().getSelectedIndex();
        TreeItem<String> selectedItem = null;

        if(paneIndex == 0)//if document tab is current
                selectedItem = (TreeItem<String>) docTreeView.getSelectionModel().getSelectedItem();
        if (paneIndex == 1)//if videos tab is current
                selectedItem = (TreeItem<String>) videoTreeView.getSelectionModel().getSelectedItem();

            currentTitle = selectedItem.getValue();
            content = query.queryDB(content,currentTitle, isHomepg);

            if(docTab) {
                TreeItem<String> t = content.getDescrip();
                docTextArea.setText(t.getValue());
            }
            if(vidTab) {
                TreeItem<String> t = content.getDescrip();
                videoTextArea.setText(t.getValue());
            }

        new SQLvideoDataLoader().load(videoTreeView);
        new SQLdocDataLoader().load(docTreeView);
    }
}