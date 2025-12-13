package poco.company.group01pocolib.mvc.controller;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.beans.property.ReadOnlyObjectWrapper;
import poco.company.group01pocolib.db.omnisearch.Search.SearchResult;
import poco.company.group01pocolib.mvc.model.*;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;

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
    private ObservableList<Lending> lendingData;
    private Lending selectedLending;
    private List<SearchResult<Lending>> currentSearchResults;

    private Stage primaryStage;
    private PocoLibController mainController;

    /**
     * @brief   Initializes the controller class. This method is automatically called after fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize buttons bindings for disabling when no selection
        lendingViewEditButton.disableProperty().bind(
            lendingTable.getSelectionModel().selectedItemProperty().isNull()
        );
        lendingReturnedButton.disableProperty().bind(
            lendingTable.getSelectionModel().selectedItemProperty().isNull()
        );

        // Binding for selected lending
        lendingTable.getSelectionModel().selectedItemProperty().addListener(observable -> {
            selectedLending = lendingTable.getSelectionModel().getSelectedItem();
        });

        // Initialize search field listener
        lendingSearchField.textProperty().addListener(observable -> {
            lendingTableHandler();
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
     * @param   bookSet     The BookSet to use.
     * @param   userSet     The UserSet to use.
     * @param   lendingSet  The LendingSet to use.
     */
    public void setDataSets(BookSet bookSet, UserSet userSet, LendingSet lendingSet) {
        this.bookSet = bookSet;
        this.userSet = userSet;
        this.lendingSet = lendingSet;
    }

    /**
     * @brief   Loads data from the model into the controller.
     */
    public void loadData() {
        this.lendingData = FXCollections.observableArrayList(lendingSet.getAllLendingsAsList());
        lendingTable.setItems(lendingData);
    }

    /**
     * @pre     User has selected a `Book` and `User` in the other tabs
     * @brief   Creates a lending based on selected `Book` and `User` objects.
     */
    public void initializeNewLending() {
        if (mainController.getMasterSelectedBook() == null ||
                mainController.getMasterSelectedUser() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(primaryStage);
            alert.setTitle("Cannot Create Lending");
            alert.setHeaderText("Book or User Not Selected");
            alert.setContentText("Please select both a book and a user before creating a new lending.");
            alert.showAndWait();
            return;
        }

        launchViewEditLendingDialog(null, true, PropMode.EDIT);
    }

    /**
     * @brief   Initializes the lending table columns.
     * @details This method sets up the cell value factories for each column in the lending table, defining how data
     *          from the Lending objects will be displayed in each column.
     */
    private void initializeLendingColumns() {
        // Lending specific columns
        lendingIdColumn.setCellValueFactory(cellData ->
            new ReadOnlyObjectWrapper<>(cellData.getValue().getLendingId()));

        lendingReturnDateColumn.setCellValueFactory(cellData ->
            new ReadOnlyObjectWrapper<>(cellData.getValue().getReturnDate()));

        // Book specific columns - accessing nested Book object
        lendingIsbnColumn.setCellValueFactory(cellData -> {
            Book book = cellData.getValue().getBook();
            return new ReadOnlyStringWrapper(book != null ? book.getIsbn() : "");
        });

        lendingTitleColumn.setCellValueFactory(cellData -> {
            Book book = cellData.getValue().getBook();
            return new ReadOnlyStringWrapper(book != null ? book.getTitle() : "");
        });

        // User specific columns - accessing nested User object
        lendingUserIdColumn.setCellValueFactory(cellData -> {
            User user = cellData.getValue().getUser();
            return new ReadOnlyStringWrapper(user != null ? user.getId() : "");
        });

        lendingUserColumn.setCellValueFactory(cellData -> {
            User user = cellData.getValue().getUser();
            return new ReadOnlyStringWrapper(user != null ? user.getName() : "");
        });
    }

    /**
     * @brief   Applies the default sorting method to the lending table.
     * @details This method clears any existing sort order and applies the default sorting by return date in ascending
     *          order.
     */
    private void applyDefaultSortMethod() {
        // Temporarily remove listener to avoid recursion
        lendingTable.getSortOrder().removeListener(defaultSortOrderListener);

        try {
            lendingTable.getSortOrder().clear();
            lendingReturnDateColumn.setSortType(TableColumn.SortType.ASCENDING);
            lendingTable.getSortOrder().add(lendingReturnDateColumn);
            lendingTable.sort();
        } finally {
            lendingTable.getSortOrder().addListener(defaultSortOrderListener);
        }
    }

    /**
     * @brief   Applies the default search sort method to the lending table.
     * @details This method sorts the table when containing search results, based on the hit count
     *          of the SearchResult objects returned by the search.
     */
    private void applyDefaultSearchSortMethod() {
        // Temporarily remove listener to avoid recursion
        lendingTable.getSortOrder().removeListener(searchSortOrderListener);

        try {
            lendingTable.getSortOrder().clear();

            // Re-order based on hits from search results
            if (currentSearchResults != null && !currentSearchResults.isEmpty()) {
                // Stream the search results
                List<Lending> sortedByHits = currentSearchResults.stream()
                        // Sort the stream by hit count
                        .sorted()
                        // Map to underlying Lending objects
                        .map(sr -> sr.item)
                        // Collect to list
                        .toList();
                // Update the observable list with hit-sorted lendings
                lendingData.setAll(sortedByHits);
            }
        } finally {
            lendingTable.getSortOrder().addListener(searchSortOrderListener);
        }
    }

    /**
     * @brief   Listener to re-apply default sorting when sort order becomes empty.
     */
    ListChangeListener<TableColumn<Lending, ?>> defaultSortOrderListener = change -> {
        if (lendingTable.getSortOrder().isEmpty()) {
            applyDefaultSortMethod();
        }
    };

    /**
     * @brief   Listener to re-apply default search sorting when sort order becomes empty.
     */
    ListChangeListener<TableColumn<Lending, ?>> searchSortOrderListener = change -> {
        if (lendingTable.getSortOrder().isEmpty()) {
            applyDefaultSearchSortMethod();
        }
    };

    /**
     * @brief   Allows handling of swaps of shown lists based on the search field and sorting
     */
    private void lendingTableHandler() {
        if (lendingSearchField.textProperty().getValue().isBlank()) {
            loadData();
            applyDefaultSortMethod();

            // Remove eventual listener for search sort order
            lendingTable.getSortOrder().removeListener(searchSortOrderListener);

            // Default sort order listener is added
            lendingTable.getSortOrder().addListener(defaultSortOrderListener);
        } else {
            // Clear any existing sort order to show results by hits
            lendingTable.getSortOrder().clear();

            String query = lendingSearchField.textProperty().getValue();

            // Perform search and store results
            currentSearchResults = lendingSet.search(query);

            // Initial sort of search results by hits
            applyDefaultSearchSortMethod();

            // Update table items to show only search results
            lendingTable.setItems(lendingData);

            // Remove default sort listener
            lendingTable.getSortOrder().removeListener(defaultSortOrderListener);

            // Add search sort listener
            lendingTable.getSortOrder().addListener(searchSortOrderListener);
        }
    }

    /**
     * @brief   Initializes the lending table by setting up the columns.
     */
    public void initializeTable() {
        initializeLendingColumns();
    }

    /**
     * @brief   Launches the View/Edit Lending dialog.
     * @details This method opens a new dialog window for viewing or editing a lending's details. It takes into account
     *          whether the lending is new or existing, and whether editing is allowed.
     *
     * @param   lendingToViewOrEdit  The lending to view or edit. If `null` and `isNewLending` is `true`, a new lending
     *                               will be created.
     * @param   isNewLending         `true` if creating a new lending, `false` if viewing/editing an existing lending.
     * @param   mode                 The mode of the dialog, either VIEW, VIEW_ONLY, or EDIT.
     */
    public void launchViewEditLendingDialog(Lending lendingToViewOrEdit, boolean isNewLending, PropMode mode) {
        // Handling logical inconsistencies
        if (lendingToViewOrEdit == null && !isNewLending ||
                lendingToViewOrEdit != null && isNewLending ||
                mode == PropMode.VIEW_ONLY && isNewLending)
            return;

        try {
            URL url = getClass().getResource("/poco/company/group01pocolib/mvc/view/prop-lending.fxml");
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();
            LendingPropController controller = loader.getController();
            controller.setDependencies(lendingSet, bookSet, userSet, mainController);

            Stage stage = new Stage();
            controller.setDialogStage(stage);
            controller.setLending(lendingToViewOrEdit);
            stage.setScene(new Scene(root));

            if (isNewLending) {
                stage.setTitle("Add New Lending");
            } else {
                stage.setTitle("View Lending Details");
            }

            controller.setMode(mode);
            stage.initOwner(primaryStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
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
        if (selectedLending != null) {
            launchViewEditLendingDialog(selectedLending, false, PropMode.EDIT);
        }
    }

    /**
     * @brief   Marks the selected lending as returned when the "Mark as Returned" button is clicked on the Lending tab.
     * @details This method updates the model to mark the selected lending as returned, and updates the book and user
     *          data accordingly.
     */
    @FXML
    private void handleMarkAsReturned() {
        if (selectedLending != null) {
            selectedLending.setReturned();
        }
    }
}
