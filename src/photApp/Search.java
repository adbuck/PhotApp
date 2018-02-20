package photApp;

import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;
import org.controlsfx.control.textfield.TextFields;
import photApp.loaders.SQLdocDataLoader;

import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Search {


    public void handleSearch (TabPane tabPane, TextField searchBar, WebView docWebView, TextArea docTextArea, TreeView docTreeView,
                              TreeView videoTreeView,WebView videoWebView,TextArea videoTextArea) throws SQLException {
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        int paneIndex;
        paneIndex = tabPane.getSelectionModel().getSelectedIndex();
        String sql = null;
        ArrayList<String> titles = new ArrayList<>();
        Connection con = null;
        Boolean isHomePg = false;
        try {

             con = SQLdocDataLoader.connect();

            if (paneIndex == 0) {//if document tab is current
                sql = "SELECT item_title FROM Phot_Content WHERE item_type = ?";
            }
            else  if (paneIndex == 1) {//if videos tab is current
                sql = "SELECT item_title FROM Phot_Content Where item_type NOT LIKE ?";
            }

            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, "pdf");
            rs = pstmt.executeQuery();



        if (rs != null) {
            while (rs.next()) {
                TreeItem<String> title = new TreeItem<String>(rs.getString("item_title"));
                titles.add(title.getValue());

            }
        }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();

        } finally {
            if (con != null) {
                try {
                    pstmt.close();
                    rs.close();
                    con.close();
                    System.out.println("Database connection closed");

                } catch (SQLException e) {
                    System.out.println("Database connection NOT closed");
                }
            }
        }

        TextFields.bindAutoCompletion(searchBar, titles);
        System.out.println("SEARCHING");
        TreeItem<String> currentSelection = new TreeItem<>(searchBar.getCharacters().toString());


        if (paneIndex == 0) {//if document tab is current

            DisplayContent getSelectedDoc = new DisplayContent();
            try {
                getSelectedDoc.displayDoc(currentSelection.getValue(),docWebView,docTextArea, isHomePg);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (ScriptException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            docTreeView.getSelectionModel().select(currentSelection);

        }
        else {//if videos tab is current

            DisplayContent getSelectedVid = new DisplayContent();
            getSelectedVid.displayVid(currentSelection.getValue(), videoWebView, videoTextArea,isHomePg);
            videoTreeView.getSelectionModel().select(currentSelection);

        }


    }
}
