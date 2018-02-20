package photApp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class contains the SplashScreen of the Application
 *
 */
public class SplashScreenController extends Parent {

    @FXML
    private ImageView splashImage;
    @FXML
    private StackPane splashPane;
    @FXML
    private ProgressIndicator progress;

    private Logger logger = Logger.getLogger(getClass().getName());
    Stage splashStage = new Stage();

    /**
     * Constructor.
     */
    public SplashScreenController() {

        // FXMLLOADER

          FXMLLoader loader = new FXMLLoader();
          loader.setController(this);
          loader.setLocation(getClass().getResource("Splash.fxml"));

        try {
            loader.load();
        } catch (IOException ex) {
            System.out.println("We've got problems");
            logger.log(Level.SEVERE, "", ex);
        }

        //Window
        splashStage.setScene(new Scene(splashPane));


    }

    /**
     * Shows the window of the SplashScreen
     */
    public void showWindow() {
        splashStage.show();
    }

    /**
     * Hides the window of the SplashScreen
     */
    public void hideWindow() {
        splashStage.hide();
    }

    /**
     * Called as soon as .fxml is initialized
     */
    @FXML
    private void initialize() {

        splashStage.initStyle(StageStyle.UNDECORATED);
        splashStage.centerOnScreen();

    }

}
