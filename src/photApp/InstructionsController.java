package photApp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;


public class InstructionsController extends Parent {

    @FXML
    private VBox InstructionsBox;

    @FXML
    private AnchorPane InstructionsPane;
    Stage InstructionsStage = new Stage();

    /**
     * Constructor.
     */
    public InstructionsController() {

        // FXMLLOADER

        FXMLLoader loader = new FXMLLoader();

        loader.setController(this);
        loader.setLocation(getClass().getResource("Instructions.fxml"));
        try {
            loader.load();
        } catch (IOException ex) {
            System.out.println("We've got problems");

        }

        //Window
        InstructionsStage.setScene(new Scene(InstructionsPane));


    }

    /**
     * Shows the window of the SplashScreen
     */
    public void showWindow() {
        InstructionsStage.show();
    }

    @FXML
    private void initialize() {

        InstructionsStage.centerOnScreen();

    }

}
