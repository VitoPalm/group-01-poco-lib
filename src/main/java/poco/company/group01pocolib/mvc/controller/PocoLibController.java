package poco.company.group01pocolib.mvc.controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import poco.company.group01pocolib.mvc.model.*;

/**
 * @class   PocoLibController
 * @brief   Main controller for the PocoLib application.
 * @details This controller manages the main tab pane and coordinates between the book, user, and lending tabs.
 */
public class PocoLibController {
    @FXML private TabPane mainTabPane;
    @FXML private Tab bookTab;
    @FXML private Tab userTab;
    @FXML private Tab lendingTab;

    @FXML private BookTabController bookTabController;
    @FXML private UserTabController userTabController;
    @FXML private LendingTabController lendingTabController;

    private BookSet bookSet;
    private UserSet userSet;
    private LendingSet lendingSet;

    private ObjectProperty<Book> masterSelectedBook;
    private ObjectProperty<User> masterSelectedUser;
    private ObjectProperty<Book> researchSelectedBookInLendings;
    private ObjectProperty<User> researchSelectedUserInLendings;
    private ObjectProperty<Lending> researchSelectedLendinginBooks;
    private ObjectProperty<Lending> researchSelectedLendinginUsers;

    private Stage primaryStage;
    private Tab selectedTab;

    /**
     * @brief   Gets the lending tab.
     * @return  The lending tab.
     */
    public Tab getLendingTab() {
        return lendingTab;
    }

    /**
     * @brief   Gets the lending tab controller.
     * @return  The lending tab controller.
     */
    public LendingTabController getLendingTabController() {
        return lendingTabController;
    }

    /**
     * @brief   Gets the book tab.
     * @return  The book tab.
     */
    public Tab getBookTab() {
        return bookTab;
    }

    /**
     * @brief   Gets the book tab controller.
     * @return  The book tab controller.
     */
    public BookTabController getBookTabController() {
        return bookTabController;
    }

    /**
     * @brief   Gets the user tab.
     * @return  The user tab.
     */
    public Tab getUserTab() {
        return userTab;
    }

    /**
     * @brief   Gets the user tab controller.
     * @return  The user tab controller.
     */
    public UserTabController getUserTabController() {
        return userTabController;
    }

    /**
     * @brief   Gets the app-wide selected book.
     * @return  The selected book.
     */
    public Book getMasterSelectedBook() {
        return masterSelectedBook.get();
    }

    /**
     * @brief   Sets the app-wide selected book.
     * @param   book The book to set as selected.
     */
    public void setMasterSelectedBook(Book book) {
        this.masterSelectedBook.set(book);
    }

    public Book getResearchSelectedBookInLendings() {
        return researchSelectedBookInLendings.get();
    }

    public Lending getResearchSelectedLendinginBooks() {
        return researchSelectedLendinginBooks.get();
    }

    public Lending getResearchSelectedLendinginUsers() {
        return researchSelectedLendinginUsers.get();
    }

    public User getResearchSelectedUserInLendings() {
        return researchSelectedUserInLendings.get();
    }

    public void setResearchSelectedBookInLendings(Book book) {
        this.researchSelectedBookInLendings.set(book);
    }

    public void setResearchSelectedLendinginBooks(Lending lending) {
        this.researchSelectedLendinginBooks.set(lending);
    }

    public void setResearchSelectedLendinginUsers(Lending lending) {
        this.researchSelectedLendinginUsers.set(lending);
    }

    public void setResearchSelectedUserInLendings(User user) {
        this.researchSelectedUserInLendings.set(user);
    }


    /**
     * @brief   Gets the app-wide selected user.
     * @return  The selected user.
     */
    public User getMasterSelectedUser() {
        return masterSelectedUser.get();
    }

    /**
     * @brief   Sets the app-wide selected user.
     * @param   user The user to set as selected.
     */
    public void setMasterSelectedUser(User user) {
        this.masterSelectedUser.set(user);
    }

    /**
     * @brief   Initializes the controller class. This method is automatically called after fxml file has been loaded.
     * @details Configures the initial tab selection and sets up listeners for the master properties 
     * to handle automatic navigation between tabs (e.g., switching to Lending tab when a book and user are selected).
     */
    @FXML
    private void initialize() {
        selectedTab = lendingTab;
        switchToTab(selectedTab);

        // Initialize master selected user/book properties
        masterSelectedBook = new SimpleObjectProperty<>();
        masterSelectedUser = new SimpleObjectProperty<>();
        researchSelectedBookInLendings = new SimpleObjectProperty<>();
        researchSelectedUserInLendings = new SimpleObjectProperty<>();
        researchSelectedLendinginBooks = new SimpleObjectProperty<>();
        researchSelectedLendinginUsers = new SimpleObjectProperty<>();

        masterSelectedBook.addListener(observable -> {
            if (lendingTabController != null) {
                handleMasterPropertiesChange();
            }
        });

        masterSelectedUser.addListener(observable -> {
            if (lendingTabController != null) {
                handleMasterPropertiesChange();
            }
        });

        researchSelectedBookInLendings.addListener(observable -> {
            switchToTab(lendingTab);
            lendingTabController.setSearchText(researchSelectedBookInLendings.get().getIsbn());
        });

        researchSelectedUserInLendings.addListener(observable -> {
            // handleResearchUserInLendingsChange();
        });

        researchSelectedLendinginBooks.addListener(observable -> {
            // handleResearchLendinginBooksChange();
        });

        researchSelectedLendinginUsers.addListener(observable -> {
            // handleResearchLendinginUsersChange();
        });
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
        selectedTab = tab;
    }

    /**
     * @brief   Loads data into the application.
     *
     * @details This method assigns the provided data sets to the controller's fields and propagates them 
     * to the specific tab controllers (BookTab, UserTab, LendingTab). It also initializes 
     * the TableViews within those tabs with the loaded data.
     * 
     * @param   bookSet     The set of books.
     * @param   userSet     The set of users.
     * @param   lendingSet  The set of lendings.
     */
    public void loadData(BookSet bookSet, UserSet userSet, LendingSet lendingSet) {
        this.bookSet = bookSet;
        this.userSet = userSet;
        this.lendingSet = lendingSet;

        this.bookTabController.setDataSets(bookSet, userSet, lendingSet);
        this.userTabController.setDataSets(bookSet, userSet, lendingSet);
        this.lendingTabController.setDataSets(bookSet, userSet, lendingSet);

        this.bookTabController.setDependencies(primaryStage, this);
        this.userTabController.setDependencies(primaryStage, this);
        this.lendingTabController.setDependencies(primaryStage, this);

        this.bookTabController.initializeTable();
        this.userTabController.initializeTable();
        this.lendingTabController.initializeTable();
    }

    /**
     * @brief   Refreshes data in all tabs.
     */
    public void refreshTabData() {
        this.lendingTabController.loadData();
        this.bookTabController.loadData();
        this.userTabController.loadData();

    }

    private void handleMasterPropertiesChange() {
        if (masterSelectedBook.get() != null && masterSelectedUser.get() == null) {
            switchToTab(userTab);
        } else if (masterSelectedBook.get() == null && masterSelectedUser.get() != null) {
            switchToTab(bookTab);
        } else if (masterSelectedBook.get() != null && masterSelectedUser.get() != null) {
            switchToTab(lendingTab);
            lendingTabController.initializeNewLending();
        }
    }


}
