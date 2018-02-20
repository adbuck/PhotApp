package photApp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class aboutController extends Parent{

    @FXML
    private VBox aboutBox;

    @FXML
    private AnchorPane aboutPane;
    Stage aboutStage = new Stage();

    /**
     * Constructor.
     */
    public aboutController() {

        // FXMLLOADER

        FXMLLoader loader = new FXMLLoader();

        loader.setController(this);
        loader.setLocation(getClass().getResource("about.fxml"));
        try {
            loader.load();
        } catch (IOException ex) {
            System.out.println("We've got problems");

        }

        //Window
        aboutStage.setScene(new Scene(aboutPane));


    }

    /**
     * Shows the window of the SplashScreen
     */
    public void showWindow() {
        aboutStage.show();
    }

    @FXML
    private void initialize() {

        aboutStage.centerOnScreen();

    }



}
