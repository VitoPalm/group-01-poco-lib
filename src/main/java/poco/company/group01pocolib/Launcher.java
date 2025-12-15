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
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import poco.company.group01pocolib.mvc.controller.PocoLibController;
import poco.company.group01pocolib.mvc.model.*;

/**
 * @class   Launcher
 * @brief   The Launcher class is responsible for starting the PocoLib application.
 */
public class Launcher extends Application {
    // DataBase Paths
    public static final String BOOK_SET_DB_PATH = "data/dbs/bookset.db";
    public static final String USER_SET_DB_PATH = "data/dbs/userset.db";
    public static final String LENDING_SET_DB_PATH = "data/dbs/lendingset.db";

    // Serialized Data Paths
    public static final String BOOK_SET_SERIALIZED_PATH = "data/ser/bookset.ser";
    public static final String USER_SET_SERIALIZED_PATH = "data/ser/userset.ser";
    public static final String LENDING_SET_SERIALIZED_PATH = "data/ser/lendingset.ser";

    // Sets
    private static BookSet bookSet;
    private static UserSet userSet;
    private static LendingSet lendingSet;

    /**
     * @brief   Restores the book set of the application.
     * @details Uses the `loadFromSerialized` method to restore the data.
     */
    private static BookSet restoreBookSet() {
        return BookSet.loadFromSerialized(BOOK_SET_SERIALIZED_PATH, BOOK_SET_DB_PATH);
    }

    /**
     * @brief   Restores the user set of the application.
     * @details Uses the `loadFromSerialized` method to restore the data.
     */
    private static UserSet restoreUserSet() {
        return UserSet.loadFromSerialized(USER_SET_SERIALIZED_PATH, USER_SET_DB_PATH);
    }

    /**
     * @brief   Restores the lending set of the application.
     * @details Uses the `loadFromSerialized` method to restore the data.
     */
    private static LendingSet restoreLendingSet() {
        return LendingSet.loadFromSerialized(LENDING_SET_SERIALIZED_PATH, LENDING_SET_DB_PATH, bookSet, userSet);
    }

    /**
     * @brief Starts the JavaFX application by loading the main FXML layout and setting up the primary stage.
     * @param stage The primary stage for this application.
     * @throws Exception if the FXML resource cannot be loaded.
     */
    @Override
    public void start(Stage stage) throws Exception {
        URL url = getClass().getResource("/poco/company/group01pocolib/mvc/view/main-view.fxml");
        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();
        PocoLibController controller = loader.getController();
        controller.setPrimaryStage(stage);

        // Load data sets
        bookSet = restoreBookSet();
        userSet = restoreUserSet();
        lendingSet = restoreLendingSet();

        controller.loadData(bookSet, userSet, lendingSet);
        controller.refreshTabData();

        stage.setScene(new Scene(root));
        stage.setTitle("PocoLib");
        stage.setMinWidth(350);
        stage.setMinHeight(250);

        // Save to serialized on close
        stage.setOnCloseRequest(event -> {
            stage.hide();

            Task<Void> saveTask = new Task<>() {
                @Override
                protected Void call() {
                    bookSet.saveToSerialized();
                    userSet.saveToSerialized();
                    lendingSet.saveToSerialized();
                    return null;
                }
            };

            saveTask.setOnSucceeded(e -> Platform.exit());
            new Thread(saveTask).start();
            event.consume(); // Prevent the default close behavior until saving is done
        });

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
