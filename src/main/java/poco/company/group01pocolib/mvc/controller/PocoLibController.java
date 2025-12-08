package poco.company.group01pocolib.mvc.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import poco.company.group01pocolib.mvc.model.*;

import java.time.LocalDate;

public class PocoLibController {
    // Data Sets
    private BookSet bookSet;
    private UserSet userSet;
    private LendingSet lendingSet;

    // UI Elements
    @FXML private TabPane mainTabPane;

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

    // -------- //
    // Book Tab //
    // -------- //
    @FXML private Tab bookTab;

    @FXML private TextField bookSearchField;

    // Book Table //
    // ------------------------------------------------ //
    @FXML private TableView<Book> bookTable;

    @FXML private TableColumn<Book, String> bookIsbnColumn;
    @FXML private TableColumn<Book, String> bookTitleColumn;
    @FXML private TableColumn<Book, String> bookAuthorsColumn;
    @FXML private TableColumn<Book, Integer> bookYearColumn;
    @FXML private TableColumn<Book, Integer> bookAvailableColumn;
    @FXML private TableColumn<Book, Integer> bookLentColumn;
    // ------------------------------------------------ //

    // Book Tab Buttons
    @FXML private Button bookAddButton;
    @FXML private Button bookViewEditButton;
    @FXML private Button bookLendButton;

    // -------- //
    // User Tab //
    // -------- //
    @FXML private Tab userTab;

    @FXML private TextField userSearchField;

    // User Table //
    // ------------------------------------------------ //
    @FXML private TableView<User> userTable;

    @FXML private TableColumn<User, String> userIdColumn;
    @FXML private TableColumn<User, String> userNameColumn;
    @FXML private TableColumn<User, String> userSurnameColumn;
    @FXML private TableColumn<User, String> userEmailColumn;
    @FXML private TableColumn<User, Integer> userLentColumn;
    // ------------------------------------------------ //

    // User Tab Buttons
    @FXML private Button userAddButton;
    @FXML private Button userViewEditButton;
    @FXML private Button userLendButton;


    // Some base objects to better manage the tables and data
    private ObservableList<Book> bookData = FXCollections.observableArrayList();
    private ObservableList<User> userData = FXCollections.observableArrayList();
    private ObservableList<Lending> lendingData = FXCollections.observableArrayList();

    private Book selectedBook;
    private User selectedUser;

    private Stage primaryStage;

    /**
     * @brief   Sets the primary stage for the application.
     * @param   primaryStage The primary stage to set.
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * @brief   Initializes the controller class. This method is automatically called after fxml file has been loaded.
     * @details This method acts as a sort of wrapper to dispatch a series of initialization methods for the various
     *          tables and UI elements, as well as loading and initializing the data from the model.
     */
    @FXML
    private void initialize() {
        // TODO: implement initialization logic
    }

    // ----------------------------- //
    // Initialization Helper Methods //
    // ----------------------------- //

    /**
     * @brief   Loads data from the model into the controller.
     * @details As this method is normally called during initialization, it also needs to start the process of
     *          de-serializing the data from the model files, before it can actually load them into the controller's
     *          own data structures.
     */
    private void loadData() {
        // TODO: implement data loading logic
    }

    /**
     * @brief   Refreshes the data in the controller by fetching the lists from the model.
     */
    private void dataRefresher() {
        // TODO: implement data refreshing logic
    }

    /**
     * @brief   Clears selections for tab changes
     * @param   newTab The new tab that has been selected
     */
    private void clearSelections(Tab newTab) {
        // TODO: implement selection clearing logic
    }

    /**
     * @brief   Initializes the book table columns.
     * @details This method sets up the cell value factories for each column in the book table, defining how data from
     *          the Book objects will be displayed in each column.
     */
    private void initializeBookColumns() {
        // TODO: implement book columns initialization logic
    }

    /**
     * @brief   Allows handling of swaps of shown lists based on the search field and sorting
     */
    private void bookTableHandler() {
        // TODO: implement book table handling logic
    }

    /**
     * @brief   Initializes the user table columns.
     * @details This method sets up the cell value factories for each column in the user table, defining how data from
     *          the User objects will be displayed in each column.
     */
    private void initializeUserColumns() {
        // TODO: implement user columns initialization logic
    }

    /**
     * @brief   Allows handling of swaps of shown lists based on the search field and sorting
     */
    private void userTableHandler() {
        // TODO: implement user table handling logic
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

    // Book Tab //
    // ------------------------------------------------ //
    /**
     * @brief   Allows to add a new book when the "Add" button is clicked on the Book tab.
     * @details This method handles both the model and the orchestration of the popup dialog for adding a new book.
     */
    @FXML
    private void handleBookAdd() {
        // TODO: implement book add logic
    }

    /**
     * @brief   Allows to view or edit the selected book when the "View & Edit" button is clicked on the Book tab.
     * @details This method handles both the model and the orchestration of the popup dialog for viewing or editing
     *          a book.
     */
    @FXML
    private void handleBookViewEdit() {
        // TODO: implement book view/edit logic
    }

    /**
     * @brief   Allows to lend the selected book when the "Lend" button is clicked on the Book tab.
     * @details This method checks if a user was selected before selecting the book and pressing the button. If that is
     *          case, it moves to the Lending tab with the selected user and book pre-selected, only requiring the
     *          return date to be set.
     *          <br><br>
     *          If no user was selected, it moves to the User tab to allow selection of a user to lend the book to.
     */
    @FXML
    private void handleBookLend() {
        // TODO: implement book lend logic
    }
    // ------------------------------------------------ //

    // User Tab //
    // ------------------------------------------------ //
    /**
     * @brief   Allows to add a new user when the "Add" button is clicked on the User tab.
     * @details This method handles both the model and the orchestration of the popup dialog for adding a new user.
     */
    @FXML
    private void handleUserAdd() {
        // TODO: implement user add logic
    }

    /**
     * @brief   Allows to view or edit the selected user when the "View & Edit" button is clicked on the User tab.
     * @details This method handles both the model and the orchestration of the popup dialog for viewing or editing
     *          a user.
     */
    @FXML
    private void handleUserViewEdit() {
        // TODO: implement user view/edit logic
    }

    /**
     * @brief   Allows to lend a book to the selected user when the "Lend To" button is clicked on the User tab.
     * @details This method checks if a book was selected before selecting the user and pressing the button. If that is
     *          case, it moves to the Lending tab with the selected user and book pre-selected, only requiring the
     *          return date to be set.
     *          <br><br>
     *          If no book was selected, it moves to the Book tab to allow selection of a book to lend to the user.
     */
    @FXML
    private void handleUserLend() {
        // TODO: implement user lend logic
    }
    // ------------------------------------------------ //

    // Lending Tab //
    // ------------------------------------------------ //
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
    // ------------------------------------------------ //

    // ---------------------- //
    // Pop Up Dialog Handlers //
    // ---------------------- //

    /**
     * @brief   Opens a dialog to add or edit a book. It is handled by its own dedicated controller.
     * @param   book The book to edit, or null to add a new book.
     *
     * @return  true if the user clicked OK, false otherwise.
     */
    public boolean showBookEditDialog(Book book) {
        // TODO: implement book edit dialog logic
        return true;
    }

    /**
     * @brief   Opens a dialog to get a detailed view of a book. It is handled by its own dedicated controller.
     * @param   book The book to view.
     */
    public void showBookDetailDialog(Book book) {
        // TODO: implement book detail dialog logic
    }

    /**
     * @brief   Opens a dialog to add or edit a user. It is handled by its own dedicated controller.
     * @param   user The user to edit, or null to add a new user.
     *
     * @return  true if the user clicked OK, false otherwise.
     */
    public boolean showUserEditDialog(User user) {
        // TODO: implement user edit dialog logic
        return true;
    }

    /**
     * @brief   Opens a dialog to get a detailed view of a user. It is handled by its own dedicated controller.
     * @param   user The user to view.
     */
    public void showUserDetailDialog(User user) {
        // TODO: implement user detail dialog logic
    }

    /**
     * @brief   Opens a dialog to add or edit a lending. It is handled by its own dedicated controller.
     * @param   lending The lending to edit, or null to add a new lending.
     *
     * @return  true if the user clicked OK, false otherwise.
     */
    public boolean showLendingEditDialog(Lending lending) {
        // TODO: implement lending edit dialog logic
        return true;
    }

    /**
     * @brief   Opens a dialog to get a detailed view of a lending. It is handled by its own dedicated controller.
     * @param   lending The lending to view.
     */
    public void showLendingDetailDialog(Lending lending) {
        // TODO: implement lending detail dialog logic
    }

    /**
     * @brief   Finishes lending process when both user and book are selected.
     * @details This method is called when both a user and a book have been selected for lending. It moves to the
     *          Lending tab with the selected user and book pre-selected, only requiring the return date to be set. (in
     *          a dedicated dialog)
     */
    public void finalizeLendingProcess() {
        // TODO: implement finalize lending process logic
    }
}
