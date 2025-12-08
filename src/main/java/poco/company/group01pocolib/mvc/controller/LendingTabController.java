package poco.company.group01pocolib.mvc.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import poco.company.group01pocolib.mvc.model.*;

import java.time.LocalDate;

public class LendingTabController {
    // Data Sets
    private BookSet bookSet;
    private UserSet userSet;
    private LendingSet lendingSet;

    // ----------- //
    // Lending Tab //
    // ----------- //
    @FXML private Tab lendingTab;

    @FXML private TextField lendingSearchField;

    // Lending Table //
    // ------------------------------------------------ //
    @FXML private TableView<Lending> lendingTable;

    // Lending Specific Columns
    @FXML private TableColumn<Lending, Integer> lendingIdColumn;
    @FXML private TableColumn<Lending, LocalDate> lendingReturnDateColumn;

    // Book Specific Columns
    @FXML private TableColumn<Lending, String> lendingIsbnColumn;
    @FXML private TableColumn<Lending, String> lendingTitleColumn;

    // User Specific Columns
    @FXML private TableColumn<Lending, String> lendingUserIdColumn;
    @FXML private TableColumn<Lending, String> lendingUserColumn;
    // ------------------------------------------------ //

    // Lending Tab Buttons
    @FXML private Button lendingViewEditButton;
    @FXML private Button lendingReturnedButton;

    // Data management
    private ObservableList<Lending> lendingData = FXCollections.observableArrayList();
    private Lending selectedLending;

    private Stage primaryStage;
    private PocoLibController mainController;

    /**
     * @brief   Initializes the controller class. This method is automatically called after fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        initializeLendingColumns();
        lendingTableHandler();
    }

    /**
     * @brief   Sets the primary stage and main controller references.
     * @param   primaryStage The primary stage to set.
     * @param   mainController The main controller reference.
     */
    public void setDependencies(Stage primaryStage, PocoLibController mainController) {
        this.primaryStage = primaryStage;
        this.mainController = mainController;
    }

    /**
     * @brief   Sets the data sets for the controller.
     * @param   bookSet The BookSet to use.
     * @param   userSet The UserSet to use.
     * @param   lendingSet The LendingSet to use.
     */
    public void setDataSets(BookSet bookSet, UserSet userSet, LendingSet lendingSet) {
        this.bookSet = bookSet;
        this.userSet = userSet;
        this.lendingSet = lendingSet;
        loadData();
    }

    /**
     * @brief   Loads data from the model into the controller.
     */
    private void loadData() {
        // TODO: implement data loading logic
    }

    /**
     * @brief   Refreshes the data in the controller by fetching the lists from the model.
     */
    public void refreshData() {
        // TODO: implement data refreshing logic
    }

    /**
     * @brief   Pre-selects a book and user for creating a new lending.
     * @param   book The book to lend.
     * @param   user The user borrowing the book.
     */
    public void initializeNewLending(Book book, User user) {
        // TODO: implement new lending initialization logic
    }

    /**
     * @brief   Initializes the lending table columns.
     * @details This method sets up the cell value factories for each column in the lending table, defining how data from
     *          the Lending objects will be displayed in each column.
     */
    private void initializeLendingColumns() {
        // TODO: implement lending columns initialization logic
    }

    /**
     * @brief   Allows handling of swaps of shown lists based on the search field and sorting
     */
    private void lendingTableHandler() {
        // TODO: implement lending table handling logic
    }

    // --------------- //
    // Button Handlers //
    // --------------- //

    /**
     * @brief   Allows to view or edit the selected lending when the "View & Edit" button is clicked on the Lending tab.
     * @details This method handles both the model and the orchestration of the popup dialog for viewing or editing
     *          a lending.
     */
    @FXML
    private void handleLendingEdit() {
        // TODO: implement lending view/edit logic
    }

    /**
     * @brief   Marks the selected lending as returned when the "Mark as Returned" button is clicked on the Lending tab.
     * @details This method updates the model to mark the selected lending as returned, and updates the book and user
     *          data accordingly.
     */
    @FXML
    private void handleMarkAsReturned() {
        // TODO: implement lending returned logic
    }
}
