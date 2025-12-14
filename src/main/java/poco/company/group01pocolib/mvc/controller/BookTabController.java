package poco.company.group01pocolib.mvc.controller;

import java.net.URL;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableNumberValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import poco.company.group01pocolib.db.omnisearch.Search.SearchResult;
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

    // Book Tab Button Tooltips
    @FXML private Tooltip bookLendButtonTooltip;
    @FXML private Tooltip bookViewEditButtonTooltip;

    // Data management
    private ObservableList<Book> bookData;
    private List<SearchResult<Book>> currentSearchResults;

    // Table entry selection
    private Book selectedBook;
    private ObjectProperty<Book> selectedBookProperty = new SimpleObjectProperty<>();
    private BooleanBinding selectedBookIsLendable;          /// This binding only updates based on the selected book, NOT the book's attributes


    private Stage primaryStage;
    private PocoLibController mainController;

    /**
     * @brief   Initializes the controller class, setting up all the listeners. This method is automatically called after fxml file has been loaded.
     * @todo    IMPORTANT: ADD `selectedBookProperty.set(book)` EACH TIME THE NUMBER OF COPIES IS MODIFIED
     * 
     * @details
     * Setup of all the listeners of the BookTab: selected table entry, Omnisearch textfield
     * - When an entry is selected, the "View/Edit" button will become clickable
     *   - otherwise, a tooltip will show explaining why the button isn't clickable
     * - When an entry with available copies is selected, the "Lend" button will become clickable
     *   - otherwise, a tooltip will show explaining why the button isn't clickable
     * - When the Omnisearch textfield is empty, the full table data is shown, whereas a type in the search box
     *   enables the view of the search results
     * - When the Omnisearch textfield is empty, the prompt text shows the number of entries in the Set
     * - When the window is resized to a tighter height, the pocologo is hidden
     * - When the window is resized to a tighter width, the table resize policy becomes unconstrained to correctly visualize min. column sizes
     *
     * @author  Giovanni Orsini
     */
    @FXML
    private void initialize() {
        // Binding for selected book and property
        bookTable.getSelectionModel().selectedItemProperty().addListener(observable -> {
            selectedBook = bookTable.getSelectionModel().getSelectedItem();});

        selectedBookProperty.bind(bookTable.getSelectionModel().selectedItemProperty());
        
        // Initialize View/Edit Button bindings for disabling when no selection
        bookViewEditButton.disableProperty().bind(selectedBookProperty.isNull());

        // Bind View/Edit Tooltip
        bookViewEditButtonTooltip.textProperty().bind(
            Bindings.when(selectedBookProperty.isNull())
                .then("No book selected")
                .otherwise(""));

        // Initialize binding telling if a book is lendable !!only updates on selectedBook updates
        selectedBookIsLendable = Bindings.greaterThan(Bindings.createIntegerBinding(() -> 
                selectedBookProperty.get() != null? selectedBookProperty.get().getCopies() : 1, selectedBookProperty), 0);

        // Initialize Lend Button bindings for disabling when no selection or unavailable copies
        bookLendButton.disableProperty().bind(              
                bookTable.getSelectionModel().selectedItemProperty().isNull().or(Bindings.not(selectedBookIsLendable))); 

        // Bind Lend Button tooltip
        bookLendButtonTooltip.textProperty().bind(
            Bindings.when(selectedBookProperty.isNull())
                .then("No book selected")
                .otherwise(Bindings.when(selectedBookIsLendable)
                    .then("")
                    .otherwise("Selected Book has no available copies")));

        // Initialize search field listener
        bookSearchField.textProperty().addListener(observable -> {
            bookTableHandler();
        });

        Platform.runLater(() -> {
            if (bookSet == null)
                return;

            // Note: Actual binding is set in loadData() after bookData initialization
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
    }

    /**
     * @brief   Loads data from the model into the controller.
     * @author  Giovanni Orsini
     */
    void loadData() {
        if (this.bookData == null) {
            this.bookData = FXCollections.observableArrayList(bookSet.getListOfBooks());
            bookTable.setItems(bookData);
            // Set up binding for prompt text to show number of books
            bookSearchField.promptTextProperty().bind(Bindings.format("OmniSearch %d books", Bindings.size(bookData)));
        } else {
            // Update existing observable list to refresh the table
            bookData.setAll(bookSet.getListOfBooks());
        }
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

    /**
     * @brief   Applies the default sorting method to the book table.
     * @details This method clears any existing sort order and applies the default order based on the book's surname.
     */
    private void applyDefaultSortMethod() {
        // Temporarily remove listener to avoid recursion
        bookTable.getSortOrder().removeListener(defaultSortOrderListener);

        try {
            bookTable.getSortOrder().clear();
            bookTitleColumn.setSortType(TableColumn.SortType.ASCENDING);
            bookTable.getSortOrder().add(bookTitleColumn);
            bookTable.sort();
        } finally {
            bookTable.getSortOrder().addListener(defaultSortOrderListener);
        }
    }

    /**
     * @brief   Applies the default search sort method to the book table.
     * @details This method sorts the table when containing search results, based on the hit count
     *          of the SearchResult objects returned by the search.
     */
    private void applyDefaultSearchSortMethod() {
        // Temporarily remove listener to avoid recursion
        bookTable.getSortOrder().removeListener(searchSortOrderListener);

        try {
            bookTable.getSortOrder().clear();

            // Re-order based on hits from search results
            if (currentSearchResults != null && !currentSearchResults.isEmpty()) {
                // Stream the search results
                List<Book> sortedByHits = currentSearchResults.stream()
                        // Sort the stream by hit count
                        .sorted()
                        // Map to underlying `Book` objects
                        .map(sr -> sr.item)
                        // Collect to list
                        .toList();
                // Update the observable list with hit-sorted books
                bookData.setAll(sortedByHits);
            }

        } finally {
            bookTable.getSortOrder().addListener(searchSortOrderListener);
        }
    }

    /**
     * @brief   Listener to re-apply default sorting when sort order becomes empty.
     */
    ListChangeListener<TableColumn<Book, ?>> defaultSortOrderListener = change -> {
        if (bookTable.getSortOrder().isEmpty()) {
            applyDefaultSortMethod();
        }
    };

    /**
     * @brief   Listener to re-apply default search sorting when sort order becomes empty.
     */
    ListChangeListener<TableColumn<Book, ?>> searchSortOrderListener = change -> {
        if (bookTable.getSortOrder().isEmpty()) {
            applyDefaultSearchSortMethod();
        }
    };


    /**
     * @brief   Allows handling of swaps of shown lists based on the search field and sorting
     */
    private void bookTableHandler() {
        if (bookSearchField.textProperty().getValue().isBlank()) {
            loadData();
            applyDefaultSortMethod();

            // Remove eventual listener for search sort order
            bookTable.getSortOrder().removeListener(searchSortOrderListener);

            // Default sort order listener is added
            bookTable.getSortOrder().addListener(defaultSortOrderListener);
        } else {
            // Clear any existing sort order to show results by hits
            bookTable.getSortOrder().clear();

            String query = bookSearchField.textProperty().getValue();

            // Perform search and store results
            currentSearchResults = bookSet.search(query);

            // Initial sort of search results by hits
            applyDefaultSearchSortMethod();

            // Update table items to show only search results
            bookTable.setItems(bookData);

            // Remove default sort listener
            bookTable.getSortOrder().removeListener(defaultSortOrderListener);

            // Add search sort listener
            bookTable.getSortOrder().addListener(searchSortOrderListener);
        }
    }
    
    /**
     * @brief   Initializes the book table by setting up the handler and columns.
     */
    public void initializeTable() {
        bookTableHandler();
        initializeBookColumns();
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
        launchViewEditBookDialog(null, true, PropMode.EDIT, primaryStage);
    }

    /**
     * @brief   Allows to view or edit the selected book when the "View & Edit" button is clicked on the Book tab.
     * @details This method handles both the model and the orchestration of the popup dialog for viewing or editing
     *          a book.
     */
    @FXML
    private void handleBookViewEdit() {
        if (selectedBook != null) {
            launchViewEditBookDialog(selectedBook, false, PropMode.VIEW, primaryStage);
        }
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
        if (selectedBook == null) {
            return; // No user selected(button disabled via Listener in TableHandler), do nothing
        }

        if (selectedBook.getCopies() <= 0) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Lending Not Allowed");
            alert.setHeaderText("Book Cannot Borrow More Books");
            alert.setContentText("The selected user has reached the maximum number of borrowed books and cannot" +
                                 " borrow more at this time.");
            alert.showAndWait();
            return;
        }

        // This is an observed property, so setting it will trigger a check by the PocoLibController to switch tabs
        mainController.setMasterSelectedBook(selectedBook);    }
}
