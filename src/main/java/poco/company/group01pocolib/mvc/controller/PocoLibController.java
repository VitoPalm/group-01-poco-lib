package poco.company.group01pocolib.mvc.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import poco.company.group01pocolib.mvc.model.BookSet;
import poco.company.group01pocolib.mvc.model.LendingSet;
import poco.company.group01pocolib.mvc.model.UserSet;

import java.time.LocalDate;

public class PocoLibController {
    private BookSet bookSet;
    private UserSet userSet;
    private LocalDate currentDate;

    @FXML private TabPane mainTabPane;
    @FXML private Tab bookTab;
    @FXML private Tab userTab;
    @FXML private Tab lendingTab;

    @FXML private BookTabController bookTabController;
    @FXML private UserTabController userTabController;
    @FXML private LendingTabController lendingTabController;

    private Stage primaryStage;
    private Tab selectedTab;

    /**
     * @brief   Initializes the controller class. This method is automatically called after fxml file has been loaded.
     * @details Initializes data sets and passes references to tab controllers.
     */
    @FXML
    private void initialize() {
        // TODO: implement initialization logic
    }

    /**
     * @brief   Sets the primary stage reference.
     * @param   primaryStage The primary stage to set.
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * @brief   Allows switching to a different tab.
     * @param   tab The tab to switch to.
     */
    public void switchToTab(Tab tab) {
        mainTabPane.getSelectionModel().select(tab);
    }

    /**
     * @brief   Loads data into the application.
     *
     * @param   bookSet The set of books.
     * @param   userSet The set of users.
     * @param   lendingSet The set of lendings.
     */
    public void loadData(BookSet bookSet, UserSet userSet, LendingSet lendingSet) {
        // TODO: implement data loading logic
    }

    /**
     * @brief   Refreshes data in all tabs.
     */
    public void refreshTabData() {
        // TODO: implement data refreshing logic
    }
}
