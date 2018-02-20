package photApp.loaders;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import photApp.Configuration;
import photApp.Main;
import photApp.Properties;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;


public class SQLdocDataLoader implements DataLoader {

    public void load(TreeView treeView) throws IOException {
        try {

            loadTreeView(treeView);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();

        }
    }
    /*******************************************************************************************************************
     *      Connect to Database
     ******************************************************************************************************************/
     public static Connection connect() throws ClassNotFoundException, IOException {

         Configuration config = new Configuration();
         String DB = config.getDBName();
         Connection c = null;

        try {

            c = DriverManager.getConnection(DB);

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }

        System.out.println("Opened database successfully");
        return c;
    }
    /*******************************************************************************************************************
     * Method loadTreeView - Builds the treeView from the Result Set of the Phot_Content
     * database.
     * This method is called by the SQLDataLoader class which returns the treeView back
     * to the Controller.
     ******************************************************************************************************************/
    public static void loadTreeView(TreeView tree) throws IOException, ClassNotFoundException {
        String sql = "SELECT * FROM Phot_Content ORDER BY category";
        String contentType = null;
        Statement stmt = null;
        ResultSet rs = null;
        Connection c = null;


        Image files = new Image(Main.class.getResource("/files.png").toExternalForm());
        Node rootIcon = new ImageView(new Image(files.getUrl()));

        try {
            c = connect();
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            TreeItem<String> rootNode = new TreeItem<String>("Documents",rootIcon);
            tree.setRoot(rootNode);
            rootNode.setExpanded(true);
            while (rs.next()) {

                Image folder = new Image(Main.class.getResource("/folder.png").toExternalForm());
                Node catIcon = new ImageView(new Image(folder.getUrl()));

                contentType = rs.getString("item_type");
                if (contentType.equals("video")) {
                    continue;
                }
                TreeItem<String> contentLeaf = new TreeItem<String>(rs.getString("item_title"));
                boolean found = false;
                for (TreeItem<String> cat : rootNode.getChildren()) {
                    if (cat.getValue().contentEquals(rs.getString("category"))) {
                        cat.getChildren().add(contentLeaf);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    TreeItem<String> catNode = new TreeItem<String>(rs.getString("category"));
                    catNode.setGraphic(catIcon);
                    rootNode.getChildren().add((catNode));
                    catNode.getChildren().add(contentLeaf);
                }
            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            if (c != null) {
                try {
                    stmt.close();
                    rs.close();
                    c.close();
                    System.out.println("Database connection closed");
                } catch (SQLException e) {
                    System.out.println("Database connection NOT closed");
                }
            }

        }
    }

        /*******************************************************************************************************************
         * Method addDocToDB
         * This method adds a new item to either the video or document tree view and adds the neccesary information
         * to the database.
         ******************************************************************************************************************/

    public static void addDocToDB(Optional<ArrayList> arr) throws SQLException, IOException, ClassNotFoundException {

        String cat = (String) arr.get().get(0);
        String title = (String) arr.get().get(1);
        String descrip = (String) arr.get().get(2);
        String link = (String) arr.get().get(3);
        String type = null;

        int l = link.length();
        String ext = link.substring(l - 3);
        System.out.println(ext);

        if(ext.equals("pdf"))
             type = "pdf";
        else
            type = "html";

        Statement stmt = null;
        Connection c = connect();


        String insertSQL = "INSERT INTO Phot_Content"
                + "(category, item_title, item_description, item_link, item_type) VALUES"
                + "(?,?,?,?,?)";

        PreparedStatement preparedStatement = c.prepareStatement(insertSQL);

        preparedStatement.setString(1, cat);
        preparedStatement.setString(2, title);
        preparedStatement.setString(3, descrip);
        preparedStatement.setString(4, link);
        preparedStatement.setString(5, type);
        preparedStatement.executeUpdate();
        stmt.close();
        c.close();

    }
    /*******************************************************************************************************************
     * Method updateDoc
     * This method adds a new item to either the video or document tree view and adds the neccesary information
     * to the database.
     ******************************************************************************************************************/

    public static void updateDoc(Optional<ArrayList> arr, TreeView docTreeView) throws SQLException, IOException, ClassNotFoundException {

        Statement stmt = null;
        Connection c = connect();

        TreeItem<String> selectedtItem = (TreeItem<String>) docTreeView.getSelectionModel().getSelectedItem();
        String selectedItm = selectedtItem.getValue();

        String newTitle = (String) arr.get().get(0);
        String link = (String) arr.get().get(1);


        String updateSQL = "UPDATE Phot_Content SET item_title = ?, item_link = ? WHERE item_title = ?";
        PreparedStatement preparedStatement = c.prepareStatement(updateSQL);

        preparedStatement.setString(1, newTitle);
        preparedStatement.setString(2, link);
        preparedStatement.setString(3, selectedItm);
        preparedStatement.executeUpdate();
        stmt.close();
        c.close();

    }
    /*******************************************************************************************************************
     * Method deleteDocFromDB
     * This method deletes a document from the docTreeView along with the corresponding
     * information from the database.
     ******************************************************************************************************************/
    public static void deleteDocFromDB(String title) throws SQLException, IOException, ClassNotFoundException {

            Connection c = connect();
            String sql = "DELETE FROM Phot_Content WHERE item_title = ?";
            PreparedStatement pstmt = c.prepareStatement(sql);
            pstmt.setString(1, title);
            pstmt.executeUpdate();
            pstmt.close();
            c.close();
        }
}
