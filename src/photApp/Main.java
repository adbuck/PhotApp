package photApp;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

public class Main extends Application {

    SplashScreenController splashScreen = new SplashScreenController();

    @Override
    public void start(Stage primaryStage) throws Exception {

        splashScreen.showWindow();

        java.net.CookieManager cookieManager = new java.net.CookieManager();
        java.net.CookieHandler.setDefault(cookieManager);

        //calls trustmanager method to trust certificates.
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, new TrustManager[]{trm}, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        Parent root;
        FXMLLoader loader = new FXMLLoader();
        loader.setController(new Controller());
        //setUserAgentStylesheet(STYLESHEET_CASPIAN);
        loader.setLocation(getClass().getResource("Photogrammetry.fxml"));
        root = loader.load();
        Image applicationIcon = new Image(Main.class.getResourceAsStream("/phot.png"));
        primaryStage.getIcons().add(applicationIcon);
        primaryStage.setTitle("PhotApp");
        primaryStage.setScene(new Scene(root));


        //Primary Stage of the application
        //doesn't appear for 2 seconds , so the splash screen is displayed
        PauseTransition splashScreenDelay = new PauseTransition(Duration.seconds(3));
        splashScreenDelay.setOnFinished(f -> {
            primaryStage.show();
            splashScreen.hideWindow();
        });
        splashScreenDelay.playFromStart();


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

    public static void main(String[] args) {

        launch(args);
    }

}