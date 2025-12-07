/**
 * @file Launcher.java
 * @brief This file contains the Launcher class which starts the PocoLib application.
 * @author Francesco Marino
 * @author Giovanni Orsini
 * @date 6 December 2025
 */
package poco.company.group01pocolib;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 * @class Launcher
 * @brief The Launcher class is responsible for starting the PocoLib application.
 */
public class Launcher extends Application {
    /**
     * @brief Starts the JavaFX application by loading the main FXML layout and setting up the primary stage.
     * @param stage The primary stage for this application.
     * @throws Exception if the FXML resource cannot be loaded.
     */
    @Override
    public void start(Stage stage) throws Exception {
        URL urlRisorsa = getClass().getResource("/poco/company/group01pocolib/mvc/view/Mockup.fxml");
        Parent root = FXMLLoader.load(urlRisorsa);
        stage.setScene(new Scene(root));
        stage.setTitle("Mockup");
        stage.show();
    }


    /**
     * @brief Main method to launch the PocoLib application.
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        launch();
    }
}
