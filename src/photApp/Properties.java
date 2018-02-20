package photApp;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.apache.commons.io.FilenameUtils;
import photApp.loaders.SQLdocDataLoader;
import photApp.loaders.SQLvideoDataLoader;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Optional;

public class Properties {



    /*******************************************************************************************************************
     Method GetProperties
     Displays a titles properties
     ******************************************************************************************************************/
    public static void GetProperties(TabPane tabPane,TreeView videoTreeView, TreeView docTreeView) throws FileNotFoundException {


        Boolean vidTab = false;
        Boolean docTab = false;
        int paneIndex;
        paneIndex = tabPane.getSelectionModel().getSelectedIndex();

        if (paneIndex == 0) {//if document tab is current
            docTab = true;
        } else if (paneIndex == 1) {//if videos tab is current
            vidTab = true;
        }

        //Populate text Fields with current title and link information
        boolean isHomePg = false;
        String selectedItm = null;
        String title = null;
        String type = null;
        String location = null;
        String size = null;
        String link = null;
        Content content = new Content();


            if (vidTab) {
                TreeItem<String> selectedtItem = (TreeItem<String>) videoTreeView.getSelectionModel().getSelectedItem();
                selectedItm = selectedtItem.getValue();
                QueryDB query = new QueryDB();
                content = query.queryDB(content,selectedItm, isHomePg);
            }
            if (docTab) {
                TreeItem<String> selectedtItem = (TreeItem<String>) docTreeView.getSelectionModel().getSelectedItem();
                selectedItm = selectedtItem.getValue();
                QueryDB query = new QueryDB();
                content = query.queryDB(content,selectedItm, isHomePg);
            }


            title = selectedItm;
            link = content.getLink().getValue();
            type = FilenameUtils.getExtension(location).toUpperCase();
            size = getFileSize(location);

            if (type.equals(""))
                type = "Website";

            TextArea textArea = new TextArea("\n" + "Title: " + "\t\t" + title + "\n" +
                    "Type: " + "\t\t" + type + "\n" +
                    "Size: " + "\t\t" + size + "\n" +
                    "Location: " + "\t" + location);

            textArea.setEditable(false);
            textArea.setWrapText(false);
            textArea.setFont(Font.font("Serif", FontWeight.BOLD, 13));
            GridPane gridPane = new GridPane();
            gridPane.setMaxWidth(Double.MAX_VALUE);
            gridPane.add(textArea, 0, 0);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Properties");
            Image applicationIcon = new Image(Main.class.getResourceAsStream("/phot.png"));
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().

                    add(applicationIcon);
            alert.getDialogPane().

                    setContent(gridPane);
            alert.showAndWait();
    }
    /*******************************************************************************************************************
     Method to get file size
     ******************************************************************************************************************/
    public static String getFileSize(String location) {
        File file = new File(location);

        DecimalFormat df2 = new DecimalFormat("###.##");
        double bytes = 0;
        double kilobytes;
        double megabytes;
        String r;
        if (file.exists()) {

            bytes = file.length();
            kilobytes = (bytes / 1024);
            megabytes = (kilobytes / 1024);
            double gigabytes = (megabytes / 1024);


            if(bytes < 1048576)
                r = df2.format(kilobytes) + " KB";
            else
                r = df2.format(megabytes) + " MB";


        } else {
            return "Size Unavailable";
        }

        return r;

    }
}