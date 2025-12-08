/**
 * @file Launcher.java
 * @brief This file contains the Launcher class which starts the PocoLib application.
 * @author Francesco Marino
 * @author Giovanni Orsini
 */
package poco.company.group01pocolib;

import java.io.InputStream;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import poco.company.group01pocolib.mvc.controller.PocoLibController;
import poco.company.group01pocolib.mvc.model.BookSet;
import poco.company.group01pocolib.mvc.model.LendingSet;
import poco.company.group01pocolib.mvc.model.UserSet;

/**
 * @class   Launcher
 * @brief   The Launcher class is responsible for starting the PocoLib application.
 */
public class Launcher extends Application {
    /**
     * @brief   Restores the book set of the application.
     * @details Uses the `loadFromSerialized` method to restore the data.
     */
    public static BookSet restoreBookSet() {
        // TODO: Implement restoration of the BookSet
        return null;
    }

    /**
     * @brief   Restores the user set of the application.
     * @details Uses the `loadFromSerialized` method to restore the data.
     */
    public static UserSet restoreUserSet() {
        // TODO: Implement restoration of the UserSet
        return null;
    }

    /**
     * @brief   Restores the lending set of the application.
     * @details Uses the `loadFromSerialized` method to restore the data.
     */
    public static LendingSet restoreLendingSet() {
        // TODO: Implement restoration of the LendingSet
        return null;
    }

    /**
     * @brief Starts the JavaFX application by loading the main FXML layout and setting up the primary stage.
     * @param stage The primary stage for this application.
     * @throws Exception if the FXML resource cannot be loaded.
     */
    @Override
    public void start(Stage stage) throws Exception {
        URL url = getClass().getResource("/poco/company/group01pocolib/mvc/view/MainView.fxml");
        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();
        PocoLibController controller = loader.getController();
        controller.loadData(restoreBookSet(), restoreUserSet(), restoreLendingSet());

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
