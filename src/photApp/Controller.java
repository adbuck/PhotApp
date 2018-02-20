package photApp;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTreeView;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.MediaView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import photApp.loaders.SQLdocDataLoader;
import photApp.loaders.SQLvideoDataLoader;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.script.ScriptException;
import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.cert.X509Certificate;
import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;


public class Controller {

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private StackPane vidStack;

    @FXML
    private MenuBar menuBar;

    @FXML
    private Menu menuFile;

    @FXML
    private Menu menuHelp;

    @FXML
    private MenuItem menuItemClose;

    @FXML
    private MenuItem menuItemAbout;

    @FXML
    private MenuItem menuItemExport;

    @FXML
    private MenuItem updatePropertiesFile;

    @FXML
    private JFXTreeView docTreeView;

    @FXML
    private TreeView videoTreeView;

    @FXML
    private TabPane tabPane;

    @FXML
    private WebView videoWebView;

    @FXML
    private WebView docWebView;

    @FXML
    private MediaView mediaView;

    @FXML
    private TextArea videoTextArea;

    @FXML
    private TextArea docTextArea;

    @FXML
    private JFXButton backBtn;

    @FXML
    private JFXButton homeButton;

    @FXML
    private JFXButton forwardBtn;

    @FXML
    private JFXButton searchBtn;

    @FXML
    private JFXButton refreshButton;

    @FXML
    private Button docUpdateButton;

    @FXML
    private Button vidUpdateButton;

    @FXML
    private Slider docTextSlider;

    @FXML
    private Slider vidTextSlider;

    @FXML
    public StackPane splashPane;

    @FXML
    private JFXProgressBar progressBar;

    @FXML
    private ImageView splashImage;

    @FXML
    private TextField searchBar;

    @FXML
    private Pane searchPane;

    @FXML
    private StackPane mapPane;

    @FXML
    private TabPane mapTab;

//-Dprism.order=sw
    @FXML
    public void initialize() throws Exception {


        //Assign images to buttons
        Image refreshImage = new Image(Main.class.getResource("/refresh.png").toExternalForm(),
                35, 17, true, true);
        refreshButton.setGraphic(new ImageView(refreshImage));

        Image homeImage = new Image(Main.class.getResource("/home.png").toExternalForm(),
                35, 17, true, true);
        homeButton.setGraphic(new ImageView(homeImage));

        Image backImage = new Image(Main.class.getResource("/back.png").toExternalForm(),
                30, 17, true, true);
        backBtn.setGraphic(new ImageView(backImage));

        Image forwardImage = new Image(Main.class.getResource("/forward.png").toExternalForm(),
                30, 17, true, true);
        forwardBtn.setGraphic(new ImageView(forwardImage));

        Image playImage = new Image(Main.class.getResource("/play.png").toExternalForm(),
                30, 17, true, true);
        searchBtn.setGraphic(new ImageView(playImage));
        searchBar.setPromptText("Search Titles");

        //call trustmanager method to trust certificates.
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, new TrustManager[]{trm}, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());


        //Set initial variables
        Configuration config = new Configuration();
        String vidHomePage = config.getVidHomePage();
        String docHomePage = config.getDocHomePage();
        String DB = config.getDBName();

        videoTextArea.wrapTextProperty().setValue(true);
        docTextArea.wrapTextProperty().setValue(true);
        //GetProxy proxy = new GetProxy();
        //proxy.findProxyForURL(vidHomePage);

        new SQLvideoDataLoader().load(videoTreeView);
        new SQLdocDataLoader().load(docTreeView);

        //load Video home page
        boolean isHomePg = true;
        DisplayContent vidHome = new DisplayContent();
        vidHome.displayVid(vidHomePage, videoWebView, videoTextArea, isHomePg);

        //load Document home page
        DisplayContent docHome = new DisplayContent();
        docHome.openHomePage(docTreeView,docTextArea,docWebView,docHomePage);

        //set to false after home pages are loaded
        isHomePg = false;

        /*******************************************************************************************************************
         Setup context menu for delete, update, open with Adobe doc method
         ******************************************************************************************************************/

        docTreeView.setCellFactory(new Callback<TreeView<String>, TreeCell<String>>() {
            @Override

            public TreeCell<String> call(TreeView<String> stringTreeView) {
                TreeCell<String> treeCell = new TreeCell<String>() {
                    @Override

                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty && item != null) {
                            setText(item);
                            setGraphic(getTreeItem().getGraphic());

                            final ContextMenu contextMenu = new ContextMenu();

                            MenuItem menuItemDelete = new MenuItem("Delete");
                            MenuItem menuItemUpdate = new MenuItem("Update");
                            MenuItem menuItemAdobe = new MenuItem("Open Externally");
                            SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
                            MenuItem menuItemProperties = new MenuItem("Properties");

                            menuItemDelete.setOnAction(new EventHandler<ActionEvent>() {
                                public void handle(ActionEvent e) {
                                    try {

                                        handleDeleteDocAction();

                                    } catch (SQLException e1) {
                                        e1.printStackTrace();
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    } catch (ClassNotFoundException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            });

                            menuItemAdobe.setOnAction(new EventHandler<ActionEvent>() {
                                public void handle(ActionEvent adobeEvent) {
                                    try {
                                        handleOpenExternally();

                                    } catch (Exception ex) {
                                    }
                                }
                            });
                            menuItemUpdate.setOnAction(new EventHandler<ActionEvent>() {
                                public void handle(ActionEvent updateEvent) {
                                    try {
                                        handleUpdateAction();

                                    } catch (Exception ex) {
                                    }
                                }
                            });
                            menuItemProperties.setOnAction(new EventHandler<ActionEvent>() {
                                public void handle(ActionEvent propertiesEvent) {
                                    try {
                                        handleGetProperties();

                                    } catch (Exception ex) {
                                    }
                                }
                            });
                            contextMenu.getItems().addAll(menuItemDelete, menuItemUpdate, menuItemAdobe, separatorMenuItem, menuItemProperties);
                            setContextMenu(contextMenu);
                        } else {
                            setText(null);
                            setGraphic(null);
                        }
                    }
                };
                docTreeView.setEditable(true);
                return treeCell;
            }
        });
        /*******************************************************************************************************************
         Setup context menu for delete and update video method
         ******************************************************************************************************************/

        videoTreeView.setCellFactory(new Callback<TreeView<String>, TreeCell<String>>() {
            @Override

            public TreeCell<String> call(TreeView<String> stringTreeView) {
                TreeCell<String> treeCell = new TreeCell<String>() {
                    @Override

                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty && item != null) {
                            setText(item);
                            setGraphic(getTreeItem().getGraphic());

                            final ContextMenu contextMenu = new ContextMenu();

                            MenuItem menuItemUpdate = new MenuItem("Update");
                            MenuItem menuItemDelete = new MenuItem("Delete");
                            MenuItem menuItemExternal = new MenuItem("Open Externally");
                            SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
                            MenuItem menuItemProperties = new MenuItem("Properties");

                            menuItemDelete.setOnAction(new EventHandler<ActionEvent>() {
                                public void handle(ActionEvent deleteEvent) {

                                    try {

                                        handleDeleteVidAction();

                                    } catch (SQLException e1) {
                                        e1.printStackTrace();
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    } catch (ClassNotFoundException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            });
                            menuItemUpdate.setOnAction(new EventHandler<ActionEvent>() {
                                public void handle(ActionEvent updateEvent) {

                                    try {

                                        handleUpdateAction();

                                    } catch (SQLException e1) {
                                        e1.printStackTrace();
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    } catch (ClassNotFoundException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            });
                            menuItemExternal.setOnAction(new EventHandler<ActionEvent>() {
                                public void handle(ActionEvent updateEvent) {

                                    try {

                                        handleOpenExternally();

                                    } catch (URISyntaxException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            menuItemProperties.setOnAction(new EventHandler<ActionEvent>() {
                                public void handle(ActionEvent propertiesEvent) {
                                    try {
                                        handleGetProperties();

                                    } catch (Exception ex) {
                                    }
                                }
                            });
                            contextMenu.getItems().addAll(menuItemUpdate, menuItemDelete, menuItemExternal, separatorMenuItem, menuItemProperties);
                            setContextMenu(contextMenu);

                        } else {
                            setText(null);
                            setGraphic(null);

                        }
                    }
                };

                videoTreeView.setEditable(true);
                return treeCell;
            }

        });
        /****************************** VIDEO TAB - SELECT AND DISPLAY VIDEO *******************************************
         when video titles in videoTreeView are selected a youtube link associated with that
         title will load into webView
         **************************************************************************************************************/
        //boolean finalIsHomePg = isHomePg;
        videoTreeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            TreeItem<String> selectedItem = (TreeItem<String>) newValue;

            DisplayContent getSelectedVid = new DisplayContent();
            Boolean IsHomePg = false;
                getSelectedVid.displayVid(((TreeItem<String>) newValue).getValue(), videoWebView, videoTextArea, IsHomePg);

        });
        /****************************** DOCUMENT TAB - SELECT AND DISPLAY DOCUMENTS ************************************
         when document/pdf titles in docTreeView are selected a PDF associated with that
         title is loaded into docView
         **************************************************************************************************************/

        docTreeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            TreeItem<String> selectedItem = (TreeItem<String>) newValue;
            DisplayContent getSelectedDoc = new DisplayContent();
                try {

                    getSelectedDoc.displayDoc(((TreeItem<String>) newValue).getValue(), docWebView, docTextArea, false);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (ScriptException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
    }
    /*******************************************************************************************************************
     Method doTheSearch
     Uses TextFields.bindAutoCompletion to search ArrayList of content Titles to populate textfield
     ******************************************************************************************************************/
    public void doTheSearch () throws SQLException {
        Search search = new Search();
        search.handleSearch(tabPane, searchBar,docWebView,docTextArea,
                docTreeView,videoTreeView,videoWebView,videoTextArea);
        searchBar.clear();
    }
    /*******************************************************************************************************************
     Method handleMap
     displays the map
     ******************************************************************************************************************/
    public void handleMap()  {

        try {
            MapIt myMap = new MapIt();
            myMap.buildMap();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*******************************************************************************************************************
     Method handleInstructionsAction
     opens and displays a pdf in docWebView
     ******************************************************************************************************************/
    public void handleInstructionsAction(){

        InstructionsController instruct = new InstructionsController();
        Image applicationIcon = new Image(Main.class.getResourceAsStream("/phot.png"));
        Stage stage = (Stage) instruct.InstructionsStage.getScene().getWindow();
        stage.getIcons().add(applicationIcon);
        instruct.showWindow();

    }
    /*******************************************************************************************************************
     Method handleGetProperties
     Displays a titles properties
     ******************************************************************************************************************/
    public void handleGetProperties() throws IOException {

        Properties contentProperties = new Properties();
        contentProperties.GetProperties(tabPane,videoTreeView,docTreeView);

    }

    /*******************************************************************************************************************
     Method to add a new document to the tree
     Creates and displays a dialog box for user to enter new content details
     It calls the addToDB method to add content to database and then refreshes treeView
     ******************************************************************************************************************/
    @FXML
    public void handleAddDocAction() throws IOException, ClassNotFoundException, SQLException {

        Dialog<ArrayList> dialog = new Dialog<>();
        dialog.setTitle("Add Document");
        dialog.setHeaderText("Enter Document Information. \n" +
                "press ADD (or click cancel to exit).");
        dialog.setResizable(true);
        Image applicationIcon = new Image(Main.class.getResourceAsStream("/phot.png"));
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(applicationIcon);
        Label catLabel = new Label("Category: ");
        Label titleLabel = new Label("Title: ");
        Label descripLabel = new Label("Description: ");
        Label linkLabel = new Label("Link: ");
        Button browseBtn = new Button("...");
        TextField catTxt = new TextField();
        TextField titleTxt = new TextField();
        TextField descripTxt = new TextField();
        TextField linkTxt = new TextField();

        GridPane grid = new GridPane();

        grid.add(catLabel, 1, 1);
        grid.add(catTxt, 2, 1);
        grid.add(titleLabel, 1, 2);
        grid.add(titleTxt, 2, 2);
        grid.add(descripLabel, 1, 3);
        grid.add(descripTxt, 2, 3);
        grid.add(linkLabel, 1, 4);
        grid.add(linkTxt, 2, 4);
        grid.add(browseBtn,3,4);
        dialog.getDialogPane().setContent(grid);

        // Set the button types.
        ButtonType buttonTypeOk = new ButtonType("ADD", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeOk, ButtonType.CANCEL);
        browseBtn.setOnAction(event ->  linkTxt.setText(handleBrowseAction()));
        dialog.setResultConverter(new Callback<ButtonType, ArrayList>() {
            @Override
            public ArrayList call(ButtonType b) {

                if (b == buttonTypeOk) {
                    ArrayList<String> array = new ArrayList<>();
                    array.add(catTxt.getText());
                    array.add(titleTxt.getText());
                    array.add(descripTxt.getText());
                    array.add(linkTxt.getText());

                    return array;
                }

                return null;
            }
        });

        Optional<ArrayList> result = dialog.showAndWait();

        if (result.isPresent()){

            SQLdocDataLoader.addDocToDB(result);
            new SQLdocDataLoader().load(docTreeView);

        }
    }
    /*******************************************************************************************************************
     Method to update the existing Item in the tree
     a dialog box is opened that allows the user to update the link and title of the
     current item.
     ******************************************************************************************************************/
    @FXML
    public void handleUpdateAction() throws IOException, ClassNotFoundException, SQLException {


        Boolean vidTab = false;
        Boolean docTab = false;
        Dialog<ArrayList> dialog = new Dialog<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        int paneIndex;
        paneIndex = tabPane.getSelectionModel().getSelectedIndex();

        if(paneIndex == 0) {//if document tab is current
            docTab = true;
            dialog.setTitle("Update Document");
            dialog.setHeaderText("Enter Document Information. \n" +
                    "press Update (or click cancel to exit).");
        }
        if (paneIndex == 1) {//if videos tab is current
            vidTab = true;
            dialog.setTitle("Update Video");
            dialog.setHeaderText("Enter Video Information. \n" +
                    "press Update (or click cancel to exit).");
        }

        dialog.setResizable(true);

        Label titleLabel = new Label("Title: ");
        Label linkLabel = new Label("Link: ");
        Button browseBtn = new Button("...");
        TextField titleTxt = new TextField();
        TextField linkTxt = new TextField();
        GridPane grid = new GridPane();
        Image applicationIcon = new Image(Main.class.getResourceAsStream("/phot.png"));
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(applicationIcon);
        String s = null;
        grid.add(titleLabel, 1, 1);
        grid.add(titleTxt, 2, 1);
        grid.add(linkLabel, 1, 2);
        grid.add(linkTxt, 2, 2);
        grid.add(browseBtn,3,2);
        dialog.getDialogPane().setContent(grid);

        //Populate text Fields with current title and link information
        Connection con = null;
        String selectedItm = null;
        try {

            if(vidTab) {
                TreeItem<String> selectedtItem = (TreeItem<String>) videoTreeView.getSelectionModel().getSelectedItem();
                selectedItm = selectedtItem.getValue();
                con = SQLvideoDataLoader.connect();
            }
            if(docTab) {
                TreeItem<String> selectedtItem = (TreeItem<String>) docTreeView.getSelectionModel().getSelectedItem();
                selectedItm = selectedtItem.getValue();
                con = SQLdocDataLoader.connect();
            }


            String sql = "SELECT item_link FROM Phot_Content WHERE item_title = ?";

            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, selectedItm);
            rs = pstmt.executeQuery();

            TreeItem<String> link = new TreeItem<String>(rs.getString("item_link"));

            titleTxt.setText(selectedItm);
            linkTxt.setText(link.getValue());
            s = selectedItm;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
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


        // Set the button types.
        ButtonType buttonTypeOk = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeOk, ButtonType.CANCEL);
        browseBtn.setOnAction(event ->  linkTxt.setText(handleBrowseAction()));
        dialog.setResultConverter(new Callback<ButtonType, ArrayList>() {
            @Override
            public ArrayList call(ButtonType b) {

                if (b == buttonTypeOk) {
                    ArrayList<String> array = new ArrayList<>();
                    array.add(titleTxt.getText());
                    array.add(linkTxt.getText());
                    return array;
                }

                return null;
            }
        });

        Optional<ArrayList> result = dialog.showAndWait();

        if (result.isPresent() && docTab == true){

            SQLdocDataLoader.updateDoc(result, docTreeView);
            new SQLdocDataLoader().load(docTreeView);
        }
        else
            SQLvideoDataLoader.updateVid(result, videoTreeView);
            new SQLvideoDataLoader().load(videoTreeView);

    }
    /*******************************************************************************************************************
     Method to determine which add method to call.  called from File menu ADD.  Based on Which pane is active doc or vid.
     ******************************************************************************************************************/
    @FXML
    public void handleAdd(){

        int paneIndex;
        boolean docTab = false;
        boolean vidTab = false;
        boolean isFile = false;
        paneIndex = tabPane.getSelectionModel().getSelectedIndex();

        try {
        if(paneIndex == 0) {//if document tab is current

                handleAddDocAction();
        }
        if (paneIndex == 1) {//if videos tab is current

               handleAddVidAction();
        }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /*******************************************************************************************************************
     Method to add a Video to tree
     Creates and displays a dialog box for user to enter new content details
     It calls the addToDB method to add content to database and then refreshes treeView
     ******************************************************************************************************************/
    @FXML
    public void handleAddVidAction() throws IOException, ClassNotFoundException, SQLException {

        Dialog<ArrayList> dialog = new Dialog<>();
        dialog.setTitle("Add Video");
        dialog.setHeaderText("Enter Video Information. \n" +
                "press ADD (or click cancel to exit).");
        dialog.setResizable(true);
        Image applicationIcon = new Image(Main.class.getResourceAsStream("/phot.png"));
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(applicationIcon);
        Label catLabel = new Label("Category: ");
        Label titleLabel = new Label("Title: ");
        Label descripLabel = new Label("Description: ");
        Label linkLabel = new Label("Link: ");
        Button browseBtn = new Button("...");

        TextField catTxt = new TextField();
        TextField titleTxt = new TextField();
        TextField descripTxt = new TextField();
        TextField linkTxt = new TextField();

        GridPane grid = new GridPane();

        grid.add(catLabel, 1, 1);
        grid.add(catTxt, 2, 1);
        grid.add(titleLabel, 1, 2);
        grid.add(titleTxt, 2, 2);
        grid.add(descripLabel, 1, 3);
        grid.add(descripTxt, 2, 3);
        grid.add(linkLabel, 1, 4);
        grid.add(linkTxt, 2, 4);
        grid.add(browseBtn,3,4);
        dialog.getDialogPane().setContent(grid);

        // Set the button types.

        ButtonType buttonTypeOk = new ButtonType("ADD", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeOk, ButtonType.CANCEL);
        browseBtn.setOnAction(event ->  linkTxt.setText(handleBrowseAction()));
        dialog.setResultConverter(new Callback<ButtonType, ArrayList>() {
            @Override
            public ArrayList call(ButtonType b) {

                if (b == buttonTypeOk) {
                    ArrayList<String> array = new ArrayList<>();
                    array.add(catTxt.getText());
                    array.add(titleTxt.getText());
                    array.add(descripTxt.getText());
                    array.add(linkTxt.getText());

                    return array;
                }

                return null;
            }
        });

        Optional<ArrayList> result = dialog.showAndWait();

        if (result.isPresent()) {

            SQLvideoDataLoader.addVidToDB(result);
            new SQLvideoDataLoader().load(videoTreeView);
        }
    }
    /*******************************************************************************************************************
     Method to DELETE documents from treeView.  Calls the deleteDocFromDB method to remove content from database.
     ******************************************************************************************************************/
    @FXML
    private void handleDeleteDocAction() throws SQLException, IOException, ClassNotFoundException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Confirm Delete");
        alert.setContentText("Are you sure you want to delete this title?");
        Image applicationIcon = new Image(Main.class.getResourceAsStream("/phot.png"));
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(applicationIcon);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            // ... user chose OK
        TreeItem<String> currentItem = (TreeItem<String>) docTreeView.getSelectionModel().getSelectedItem();
        String title = currentItem.getValue();
        try {

            SQLdocDataLoader.deleteDocFromDB(title);
            new SQLdocDataLoader().load(docTreeView);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        } else {
            // ... user chose CANCEL or closed the dialog
        }
    }
    /*******************************************************************************************************************
     Method to DELETE videos from treeView.  Calls the deleteVidFromDB method to remove content from database.
     ******************************************************************************************************************/
    @FXML
    private void handleDeleteVidAction() throws SQLException, IOException, ClassNotFoundException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Confirm Delete");
        alert.setContentText("Are you sure you want to delete this title?");
        Image applicationIcon = new Image(Main.class.getResourceAsStream("/phot.png"));
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(applicationIcon);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            // ... user chose OK

        TreeItem<String> currentItem = (TreeItem<String>) videoTreeView.getSelectionModel().getSelectedItem();
        String title = currentItem.getValue();
        System.out.println(title);
        System.out.println("Deleting " + title);
        try {

            SQLvideoDataLoader.deleteVidFromDB(title);
            new SQLvideoDataLoader().load(videoTreeView);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        } else {
            // ... user chose CANCEL or closed the dialog
        }
    }
    /*******************************************************************************************************************
     Slider - used to increase and decrease Font size in Text Area
     ******************************************************************************************************************/

    @FXML
    private void handleSliderAction() {

        docTextSlider.setMin(5);
        docTextSlider.setMax(50);
        docTextSlider.setBlockIncrement(1);
        vidTextSlider.setMin(5);
        vidTextSlider.setMax(50);
        vidTextSlider.setBlockIncrement(1);

        docTextSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                docTextArea.setFont(Font.font("Helvetica", FontWeight.SEMI_BOLD, new_val.doubleValue()));
            }
        });
        vidTextSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                videoTextArea.setFont(Font.font("Helvetica", FontWeight.SEMI_BOLD, new_val.doubleValue()));
            }
        });



    }
    /*******************************************************************************************************************
     Exit Program - Close menuItem
     ******************************************************************************************************************/

    @FXML
    private void handleCloseAction() throws NoSuchMethodException {
       System.exit(0);
    }

    /*******************************************************************************************************************
      Program info box.  Still need to add some info.  Maybe
       directions on use.  - About menuItem
     ******************************************************************************************************************/

    @FXML
    private void handleAboutAction() throws MalformedURLException {

        aboutController about = new aboutController();
        Image applicationIcon = new Image(Main.class.getResourceAsStream("/phot.png"));
        Stage stage = (Stage) about.aboutStage.getScene().getWindow();
        stage.getIcons().add(applicationIcon);
        about.showWindow();

    }

    /*******************************************************************************************************************
     This method handles the Edit Configuration menu item.  Its purpose is to create a text file called
     "Tutorials_File_Path.txt" which contains the location of the database.  FileChooser is
     used to select location of database
     ******************************************************************************************************************/
    @FXML
    private void handleUpdatePropertiesFile() throws IOException {

        InputStream input;
        String dbTemp;
        String DBName;
        String vidHomePage;
        String docHomePage;

        Dialog<ArrayList> dialog = new Dialog<>();
        dialog.setTitle("Update Configuration File");
        dialog.setHeaderText("Enter New Settings. \n" +
                "press Enter (or click cancel to exit).");
        dialog.setResizable(true);
        Image applicationIcon = new Image(Main.class.getResourceAsStream("/phot.png"));
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(applicationIcon);
        Label DBLabel = new Label("Database Location: ");
        Label vidLabel = new Label("Video Home Page: ");
        Label docLabel = new Label("Document Home Page: ");
        Button DBBtn = new Button("...");
        Button vidBtn = new Button("...");
        Button docBtn = new Button("...");

        TextField dbTxt = new TextField();
        TextField vidTxt = new TextField();
        TextField docTxt = new TextField();

        GridPane grid = new GridPane();

        grid.add(DBLabel, 1, 1);
        grid.add(dbTxt, 2, 1);
        grid.add(vidLabel, 1, 2);
        grid.add(vidTxt, 2, 2);
        grid.add(docLabel, 1, 3);
        grid.add(docTxt, 2, 3);
        grid.add(DBBtn, 3, 1);
        grid.add(vidBtn, 3, 2);
        grid.add(docBtn, 3, 3);
        dialog.getDialogPane().setContent(grid);

        java.util.Properties prop = new java.util.Properties();

        try {

            String path = System.getProperty("user.home") + File.separator + "Documents";
            path += File.separator + "PhotAppConfig";
            File customDir = new File(path);

            if(new File(path +  "\\config.properties").isFile()) {
                input = new FileInputStream(path + "\\config.properties");
            }
            else{
                input = Properties.class.getResourceAsStream("/config.properties");
            }

            //load a properties file from class path, inside static method
            prop.load(input);
            dbTemp = prop.get("database").toString();
            DBName = dbTemp.substring(12);//remove jdbc:sqlite:
            vidHomePage = prop.get("vidHome").toString();
            docHomePage = prop.get("docHome").toString();

            dbTxt.setText(DBName);
            vidTxt.setText(vidHomePage);
            docTxt.setText(docHomePage);

        } catch (IOException e) {
            e.printStackTrace();
        }
            // Set the button types.
            ButtonType buttonTypeOk = new ButtonType("Enter", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(buttonTypeOk, ButtonType.CANCEL);

            DBBtn.addEventHandler(ActionEvent.ACTION, (e)->{
                        dbTxt.clear();
                        dbTxt.setText(handleBrowseAction());
            });
            vidBtn.addEventHandler(ActionEvent.ACTION, (e)->{
                        vidTxt.clear();
                        vidTxt.setText(handleBrowseAction());
            });
            docBtn.addEventHandler(ActionEvent.ACTION, (e)->{
                        docTxt.clear();
                        docTxt.setText(handleBrowseAction());
            });


            dialog.setResultConverter(new Callback<ButtonType, ArrayList>() {
                @Override
                public ArrayList call(ButtonType b) {

                    if (b == buttonTypeOk) {
                        ArrayList<String> array = new ArrayList<>();
                        array.add(dbTxt.getText());
                        array.add(vidTxt.getText());
                        array.add(docTxt.getText());

                        return array;
                    }

                    return null;
                }
            });

            Optional<ArrayList> result = dialog.showAndWait();

            if (result.isPresent()) {

                Configuration.updateConfigFile(result);
            }
        }
    /*******************************************************************************************************************
     This method handles the browse button in the add document or video functions.  It returns the absolute path
     and file name to be added as a link to the database.
     ******************************************************************************************************************/
    @FXML
    public static String handleBrowseAction() {

        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);

        //null is cancel or X
        if (selectedFile != null) {

            String contentLink = selectedFile.getAbsolutePath();
            return contentLink;
        } else {
            System.out.println("Nothing Chosen");

            return null;
        }

    }
    /*******************************************************************************************************************
     Use this method to enable users to right click and
     open pdf or video externally - in OS default viewer for that file type
     ******************************************************************************************************************/
    @FXML
    private void handleOpenExternally() throws URISyntaxException {
       DisplayContent content = new DisplayContent();
       content.OpenExternally(docTreeView,videoTreeView,tabPane);
    }
    /*******************************************************************************************************************
     Home Button
     ******************************************************************************************************************/
    @FXML
    private void handleHome() throws IOException, ClassNotFoundException, SQLException {

        DisplayContent showHome = new DisplayContent();
        showHome.displayHome(docTreeView,videoTreeView,docTextArea,videoTextArea,docWebView,videoWebView,tabPane);

    }

    /*******************************************************************************************************************
     Back Button
     ******************************************************************************************************************/
    @FXML
    private void goBack() {

        final WebHistory history = videoWebView.getEngine().getHistory();
        ObservableList<WebHistory.Entry> entryList = history.getEntries();
        int currentIndex = history.getCurrentIndex();

        Platform.runLater(() ->
        {
            history.go(entryList.size() > 1
                    && currentIndex > 0
                    ? -1
                    : 0);
        });
    }

    /*******************************************************************************************************************
         Update Text - Database Button
     ******************************************************************************************************************/
    @FXML
    private void handleTextUpdateAction(){
    System.out.println("updating text area...");

      Boolean vidTab = false;
      Boolean docTab = false;
      String updatedVidText = "";
      String updatedDocText = "";
        PreparedStatement pstmt = null;
      int paneIndex;
      paneIndex = tabPane.getSelectionModel().getSelectedIndex();

      if(paneIndex == 0) {//if document tab is current
          docTab = true;
          updatedDocText = docTextArea.getText();
      }
      if (paneIndex == 1) {//if videos tab is current
          vidTab = true;
          updatedVidText = videoTextArea.getText();
      }


        TreeItem<String> selectedItem = null;
        Connection con = null;
        try {

            if(docTab) {
                selectedItem = (TreeItem<String>) docTreeView.getSelectionModel().getSelectedItem();
                con = SQLdocDataLoader.connect();
            }
            if (vidTab) {
                selectedItem = (TreeItem<String>) videoTreeView.getSelectionModel().getSelectedItem();
                con = SQLvideoDataLoader.connect();
            }

              String sql = "UPDATE Phot_Content SET item_description = ? WHERE item_title = ?";

              pstmt = con.prepareStatement(sql);
              if(docTab) {
                  pstmt.setString(1, updatedDocText);
                  pstmt.setString(2, selectedItem.getValue());
              }
              if(vidTab) {
                  pstmt.setString(1, updatedVidText);
                  pstmt.setString(2, selectedItem.getValue());
              }
                  pstmt.executeUpdate();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    pstmt.close();
                    con.close();
                    System.out.println("Database connection closed");
                } catch (SQLException e) {
                    System.out.println("Database connection NOT closed");
                }
            }
        }
        if(docTab) {
            docUpdateButton.setText("UPDATED!");

            KeyValue keyValue = new KeyValue(docUpdateButton.opacityProperty(), 0);
            Duration duration = Duration.millis(2000);
            Timeline timeline = new Timeline();
            timeline.setCycleCount(1);

            EventHandler onFinished = new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    docUpdateButton.setText("Save Text");
                    docUpdateButton.opacityProperty().setValue(1);
                }
            };

            KeyFrame keyFrame = new KeyFrame(duration, onFinished, keyValue);

            timeline.getKeyFrames().addAll(keyFrame);
            timeline.play();
        }
        if(vidTab){
            vidUpdateButton.setText("UPDATED!");

            KeyValue keyValue = new KeyValue(vidUpdateButton.opacityProperty(), 0);
            Duration duration = Duration.millis(2000);
            Timeline timeline = new Timeline();
            timeline.setCycleCount(1);

            EventHandler onFinished = new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    vidUpdateButton.setText("Save Text");
                    vidUpdateButton.opacityProperty().setValue(1);
                }
            };

            KeyFrame keyFrame = new KeyFrame(duration, onFinished, keyValue);

            timeline.getKeyFrames().addAll(keyFrame);
            timeline.play();

        }
    }


    /*******************************************************************************************************************
     Forward Button
     ******************************************************************************************************************/
    @FXML
    private void goForward() {
        final WebHistory history = videoWebView.getEngine().getHistory();
        ObservableList<WebHistory.Entry> entryList = history.getEntries();
        int currentIndex = history.getCurrentIndex();

        Platform.runLater(() ->
        {
            history.go(entryList.size() > 1
                    && currentIndex < entryList.size() - 1
                    ? 1
                    : 0);
        });
    }
    /*******************************************************************************************************************
     Refresh Button
     ******************************************************************************************************************/
    @FXML
    private void handleRefresh() throws IOException {

        DisplayContent refresh = new DisplayContent();
        refresh.RefreshTree(tabPane,docTreeView,videoTreeView,docTextArea,videoTextArea);

    }
    /*******************************************************************************************************************
     Export Text Menu Item
     ******************************************************************************************************************/
    @FXML
    private void handleExportAction() {

        System.out.println("Exporting!!!");
        FileChooser fileChooser = new FileChooser();
        Boolean vidTab = false;
        Boolean docTab = false;
        String vidText = "";
        String docText = "";

        int paneIndex;
        paneIndex = tabPane.getSelectionModel().getSelectedIndex();

        if(paneIndex == 0) {//if document tab is current
            docTab = true;
            docText = docTextArea.getText();
        }
        if (paneIndex == 1) {//if videos tab is current
            vidTab = true;
            vidText = videoTextArea.getText();
        }


        //Set extension filter
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(null);

        if(file != null){
            try {
                FileWriter fileWriter;

                fileWriter = new FileWriter(file);
                if(docTab)
                    fileWriter.write(docText);
                    fileWriter.close();
                if(vidTab)
                    fileWriter.write(vidText);
                    fileWriter.close();
            } catch (IOException ex) {
                //Logger.getLogger(JavaFXSaveText.class
                        //.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    /*******************************************************************************************************************
     This method trusts all certificates so unsigned pages will load.  This was neccesary to load
     https://www.dot.ny.gov/ - this may not be neccessary anymore.

     ******************************************************************************************************************/
    static TrustManager trm = new X509TrustManager() {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) {

        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }
    };
}
//        docTreeView.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> //don't select with right click
//                {
//                    if(event.isSecondaryButtonDown()){
//                        event.consume();
//                    }
//                    });

//webView.getEngine().getLoadWorker().stateProperty().addListener(new HyperlinkRedirectListener(webView));