package photApp;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import photApp.loaders.SQLdocDataLoader;

import javax.swing.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

public class QueryMapDB {

    String selectedItem = null;
    Connection con = null;
    ResultSet rs = null;
    PreparedStatement pstmt = null;
    String sql = null;


    public PhotMapContent queryMapDB(PhotMapContent photMapContent, String selectedItem) throws IOException, ClassNotFoundException {

        try {
            Connection con = SQLdocDataLoader.connect();

            //connect to database and create result set of all entries
            sql = "SELECT compiler_name, project_link, project_comments FROM PhotMap WHERE compiler_name = ?";

            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, selectedItem);
            rs = pstmt.executeQuery();

            photMapContent.setCompiler(new TreeItem<String>(rs.getString("compiler_name")));
            photMapContent.setMapLink(new TreeItem<String>(rs.getString("project_link")));
            photMapContent.setComments(new TreeItem<String>(rs.getString("project_comments")));

        } catch (SQLException e) {
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
        return photMapContent;
    }
    public void updateProject(String link, String user) throws SQLException, IOException, ClassNotFoundException {
        String compilerName = null;

        switch(user) {
            case "abuck" :
                compilerName = "Adam";
                break; // optional

            case "adambuck" :
                compilerName = "Adam";
                break; // optional

            case "estrahley" :
                compilerName = "Eric";
                break; // optional

            case "dgrey" :
                compilerName = "Doug";
                break; // optional

            case "jabecker" :
                compilerName = "John";
                break; // optional

            case "kleclair" :
                compilerName = "Karen";
                break; // optional

            case "kcole" :
                compilerName = "Kristen";
                break; // optional

            case "nrich" :
                compilerName = "Nancy";
                break; // optional

            case "srose" :
                compilerName = "Steve R";
                break; // optional

            case "smcgrail" :
                compilerName = "Steve M";
                break; // optional

            case "ttobias" :
                compilerName = "Toby";
                break; // optional

            // You can have any number of case statements.
            default : // Optional
                JOptionPane.showMessageDialog(null,"Sorry you're not a compiler");

        }
        Statement stmt = null;
        Connection c = SQLdocDataLoader.connect();

        String updateSQL = "UPDATE PhotMap SET project_link = ? WHERE compiler_name = ?";
        PreparedStatement preparedStatement = c.prepareStatement(updateSQL);

        preparedStatement.setString(1, link);
        preparedStatement.setString(2,compilerName);
        preparedStatement.executeUpdate();
        stmt.close();
        c.close();

    }
}