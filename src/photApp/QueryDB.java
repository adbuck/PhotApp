package photApp;

import javafx.scene.control.TreeItem;
import photApp.loaders.SQLdocDataLoader;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryDB {

    String selectedItem = null;
    Connection con = null;
    ResultSet rs = null;
    PreparedStatement pstmt = null;
    String sql = null;

    public Content queryDB(Content content, String selectedItem, Boolean isHomePg) {

        try {

            //connect to database and create result set of all entries
            con = SQLdocDataLoader.connect();

            if (isHomePg) {
                sql = "SELECT item_title, item_link, item_description, item_type, category FROM Phot_Content WHERE item_link = ?";
            } else {
                sql = "SELECT item_title, item_link, item_description, item_type, category FROM Phot_Content WHERE item_title = ?";
            }

            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, selectedItem);
            rs = pstmt.executeQuery();

            content.setTitle(new TreeItem<String>(rs.getString("item_title")));
            content.setCategory(new TreeItem<String>(rs.getString("category")));
            content.setLink(new TreeItem<String>(rs.getString("item_link")));
            content.setDescrip(new TreeItem<String>(rs.getString("item_description")));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    rs.close();
                    pstmt.close();
                    con.close();
                    System.out.println("Database connection closed");

                } catch (SQLException e) {
                    System.out.println("Database connection NOT closed");
                }
            }
        }
        return content;
    }
}