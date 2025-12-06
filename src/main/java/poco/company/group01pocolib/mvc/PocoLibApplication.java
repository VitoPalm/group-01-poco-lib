/**
 * @file PocoLibApplication.java
 * @brief This file contains the PocoLibApplication class which initializes and starts the JavaFX application.
 * @author Francesco Marino
 * @date 6 December 2025
 */
package poco.company.group01pocolib.mvc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;

import java.io.IOException;

/**
 * @class PocoLibApplication
 * @brief The PocoLibApplication class initializes and starts the JavaFX application.
 */
public class PocoLibApplication extends Application {

    /**
     * @brief Starts the JavaFX application by loading the main FXML layout and setting up the primary stage.
     * @param stage The primary stage for this application.
     * @throws Exception if the FXML resource cannot be loaded.
     */
    @Override
    public void start(Stage stage) throws Exception {
        URL urlRisorsa = getClass().getResource("/poco/company/group01pocolib/Mockup.fxml");
        Parent root = FXMLLoader.load(urlRisorsa);
        stage.setScene(new Scene(root));
        stage.setTitle("Mockup");
        stage.show();
    }
}
