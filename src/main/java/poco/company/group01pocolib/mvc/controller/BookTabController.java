package poco.company.group01pocolib.mvc.controller;

import java.net.URL;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
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

    @FXML private VBox containerVBox;

    @FXML private ImageView pocologoImageView;

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
     * @brief   Initializes the controller class, setting up all the listeners. This method is automatically called after fxml file has been loaded.
     * 
     * @details
     * Setup of all the listeners of the BookTab: selected table entry, Omnisearch textfield
     * - When an entry is selected, the "Lend" and "View/Edit" buttons will become clickable
     * - When the Omnisearch textfield is empty, the full table data is shown, whereas a type in the search box
     *   enables the view of the search results
     * - When the window is resized to a tighter height, the pocologo is hidden
     * - When the window is resized to a tighter width, the table resize policy becomes unconstrained to correctly visualize min. column sizes
     *
     * @author  Giovanni Orsini
     */
    @FXML
    private void initialize() {
        // Initialize buttons bindings for disabling when no selection
        bookViewEditButton.disableProperty().bind(
                bookTable.getSelectionModel().selectedItemProperty().isNull()
        );
        bookLendButton.disableProperty().bind(
                bookTable.getSelectionModel().selectedItemProperty().isNull()
        );

        // Binding for selected book
        bookTable.getSelectionModel().selectedItemProperty().addListener(observable -> {
            selectedBook = bookTable.getSelectionModel().getSelectedItem();
        });

        // Initialize search field listener
        bookSearchField.textProperty().addListener(observable -> {
            bookTableHandler();
        });

        Platform.runLater(() -> {
            if (containerVBox.getScene() == null)
                return;

            // height listener to control the pocologo visibility
            containerVBox.getScene().heightProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.doubleValue() < 600) {
                    pocologoImageView.setVisible(false);
                    pocologoImageView.setManaged(false);
                } else {
                    pocologoImageView.setVisible(true);
                    pocologoImageView.setManaged(true);
                }
            });

            // width listener to control the table resize policy
            containerVBox.getScene().widthProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.doubleValue() < 800) {
                    bookTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
                } else {
                    bookTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_SUBSEQUENT_COLUMNS);
                }
            });
        });
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
        bookIsbnColumn.setCellValueFactory(cellData ->
                                           new ReadOnlyObjectWrapper<>(cellData.getValue().getIsbn()));
        bookTitleColumn.setCellValueFactory(cellData ->
                                            new ReadOnlyObjectWrapper<>(cellData.getValue().getTitle()));
        bookAuthorsColumn.setCellValueFactory(cellData ->
                                              new ReadOnlyObjectWrapper<>(cellData.getValue().getAuthorsString()));
        bookYearColumn.setCellValueFactory(cellData ->
                                           new ReadOnlyObjectWrapper<>(cellData.getValue().getYear()));
        bookAvailableColumn.setCellValueFactory(cellData ->
                                                new ReadOnlyObjectWrapper<>(cellData.getValue().getCopies()));
        bookLentColumn.setCellValueFactory(cellData ->
                                           new ReadOnlyObjectWrapper<>(cellData.getValue().getCopiesLent()));
    }

    public void initializeTable() {
        bookTableHandler();
        initializeBookColumns();
    }

    /**
     * @brief   Allows handling of swaps of shown lists based on the search field and sorting
     */
    private void bookTableHandler() {
        // TODO: implement book table handling logic
    }

    /**
     * @brief   Launches the View/Edit Book dialog.
     * @details This method opens a new dialog window for viewing or editing a book's details. It takes into account
     *          whether the book is new or existing, and whether editing is allowed.
     *
     * @param   bookToEdit  The book to view or edit. If `null` and `isNewBook` is `true`, a new book will be created.
     * @param   isNewBook   `true` if creating a new book, `false` if viewing/editing an existing book.
     * @param   mode        The mode of the dialog, either VIEW, VIEW_ONLY, or EDIT.
     */
    public void launchViewEditBookDialog(Book bookToEdit, boolean isNewBook, PropMode mode, Stage ownerStage) {
        // Handling logical inconsistencies
        if (bookToEdit == null && !isNewBook ||
                bookToEdit != null && isNewBook ||
                mode == PropMode.VIEW_ONLY && isNewBook) {
            return;
        }

        try {
            URL url = getClass().getResource("/poco/company/group01pocolib/mvc/view/prop-book.fxml");
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();
            BookPropController controller = loader.getController();
            controller.setDependencies(mainController, bookSet);

            Stage stage = new Stage();
            controller.setDialogStage(stage);
            controller.setBook(bookToEdit);
            stage.setScene(new Scene(root));

            if (isNewBook) {
                stage.setTitle("Add New Book");
            } else {
                stage.setTitle("View Book Details");
            }

            controller.setMode(mode);
            stage.initOwner(ownerStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);

            controller.setDialogStage(stage);

            stage.show();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
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
