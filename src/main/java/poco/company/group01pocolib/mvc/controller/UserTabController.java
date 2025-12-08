package poco.company.group01pocolib.mvc.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import poco.company.group01pocolib.mvc.model.*;

public class UserTabController {
    // Data Sets
    private BookSet bookSet;
    private UserSet userSet;
    private LendingSet lendingSet;

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

    // Data management
    private ObservableList<User> userData = FXCollections.observableArrayList();
    private User selectedUser;
    private Book selectedBook;

    private Stage primaryStage;
    private PocoLibController mainController;

    /**
     * @brief   Initializes the controller class. This method is automatically called after fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        initializeUserColumns();
        userTableHandler();
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
     * @brief   Sets the selected book from another tab.
     * @param   book The book to set as selected.
     */
    public void setSelectedBook(Book book) {
        this.selectedBook = book;
    }

    /**
     * @brief   Gets the currently selected user.
     * @return  The selected user, or null if none is selected.
     */
    public User getSelectedUser() {
        return selectedUser;
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

    // --------------- //
    // Button Handlers //
    // --------------- //

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
}
