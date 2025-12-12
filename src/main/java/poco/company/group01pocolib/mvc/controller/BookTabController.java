package poco.company.group01pocolib.mvc.controller;

import java.util.List;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import poco.company.group01pocolib.mvc.model.*;

public class BookTabController {
    // Data Sets
    private BookSet bookSet;
    private UserSet userSet;
    private LendingSet lendingSet;

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

    // Data management
    private ObservableList<Book> bookData = FXCollections.observableArrayList();
    private Book selectedBook;
    private User selectedUser;

    private Stage primaryStage;
    private PocoLibController mainController;

    /**
     * @brief   Initializes the controller class. This method is automatically called after fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        initializeBookColumns();
        listenersSetup();
        bookTableHandler();
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
     * @todo    Understand why the not null check is necessary
     * @author  Giovanni Orsini
     */
    void loadData() {
        if (bookSet != null) {
            bookData.setAll(bookSet.getBookSet());      ///< creates the observable list from the BookSet
            bookTable.setItems(bookData);

        }
    }

    /**
     * @brief   Sets the selected user from another tab.
     * @param   user The user to set as selected.
     */
    public void setSelectedUser(User user) {
        this.selectedUser = user;
    }

    /**
     * @brief   Gets the currently selected book.
     * @return  The selected book, or null if none is selected.
     */
    public Book getSelectedBook() {
        return selectedBook;
    }

    /**
     * @brief   Initializes the book table columns.
     * @details This method sets up the cell value factories for each column in the book table, defining how data from
     *          the Book objects will be displayed in each column.
     * @author  Giovanni Orsini
     */
    private void initializeBookColumns() {
        bookIsbnColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getIsbn()));
        bookTitleColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getTitle()));
        bookAuthorsColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getAuthorsString()));
        bookYearColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getYear()));
        bookAvailableColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getCopies()));
        bookLentColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getCopiesLent()));
    }

    /**
     * @brief   Setup of all the listeners of the BookTab: selected table entry, Omnisearch textfield
     * @details 
     * - When an entry is selected, the "Lend" and "View/Edit" buttons will become clickable
     * - When the Omnisearch textfield is empty, the full table data is shown, whereas a type in the search box 
     *   enables the view of the search results
     * 
     * @author  Giovanni Orsini
     */
    private void listenersSetup() {
        bookViewEditButton.setDisable(true);                                ///< Buttons are initialized as disabled
        bookLendButton.setDisable(true);

        bookTable.getSelectionModel().selectedItemProperty().addListener(   ///< On selection:
            (observable, oldValue, newValue) -> {
                this.selectedBook = newValue;                               ///< Updates the attribute

                this.bookViewEditButton.setDisable(newValue == null);       ///< Disables buttons
                this.bookLendButton.setDisable(newValue == null);
            }
        );

        bookSearchField.textProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue == null || newValue.isBlank()) {
                    loadData();
                } else {
                    List<Book> searchResults = bookSet.search(newValue.toLowerCase().trim());

                    bookData.clear();
                    bookData.addAll(searchResults);
                }

            }
        );
    }

    /**
     * @brief   Allows handling of swaps of shown lists based on the search field and sorting
     */
    private void bookTableHandler() {
        // TODO: implement book table handling logic
    }

    // --------------- //
    // Button Handlers //
    // --------------- //

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
}
