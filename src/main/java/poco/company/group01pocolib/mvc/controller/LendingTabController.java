package poco.company.group01pocolib.mvc.controller;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
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

    @FXML private VBox containerVBox;

    @FXML private ImageView pocologoImageView;

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

    // Lending Tab Button Tooltips
    @FXML private Tooltip lendingViewEditButtonTooltip;
    @FXML private Tooltip lendingReturnedButtonTooltip;

    // Data management
    private ObservableList<Lending> lendingData;
    private List<SearchResult<Lending>> currentSearchResults;
    
    // Table entry selection
    private Lending selectedLending;
    private ObjectProperty<Lending> selectedLendingProperty = new SimpleObjectProperty<>();

    private Stage primaryStage;
    private PocoLibController mainController;

    /**
     * @brief   Initializes the controller class, setting up all the listeners. This method is automatically called after fxml file has been loaded.
     * 
     * @details
     * Setup of all the listeners of the LendingTab: selected table entry, Omnisearch textfield
     * - When an entry is selected, the "Mark as returned" and "View/Edit" buttons will become clickable 
     *   - otherwise, a tooltip will show explaining why the buttons aren't clickable
     * - When the Omnisearch textfield is empty, the full table data is shown, whereas a type in the search box
     *   enables the view of the search results
     * - When the Omnisearch textfield is empty, the prompt text shows the number of entries in the Set
     * - When the window is resized to a tighter height, the pocologo is hidden
     * - When the window is resized to a tighter width, the table resize policy becomes unconstrained to correctly visualize min. column sizes
     *
     * @author  Vito Palmieri
     * @author  Giovanni Orsini
     */
    @FXML
    private void initialize() {
        // Binding for selected lending
        lendingTable.getSelectionModel().selectedItemProperty().addListener(observable -> {
            selectedLending = lendingTable.getSelectionModel().getSelectedItem();});

        selectedLendingProperty.bind(lendingTable.getSelectionModel().selectedItemProperty());


        // Initialize buttons bindings for disabling when no selection
        lendingViewEditButton.disableProperty().bind(selectedLendingProperty.isNull());
        lendingReturnedButton.disableProperty().bind(
            selectedLendingProperty.isNull()
            .or(Bindings.createBooleanBinding(
                () -> selectedLendingProperty.get() != null && selectedLendingProperty.get().isReturned(),
                selectedLendingProperty
            ))
        );

        // Tooltips binding
        lendingViewEditButtonTooltip.textProperty().bind(
            Bindings.when(selectedLendingProperty.isNull())
                .then("No lending selected")
                .otherwise(""));

        lendingReturnedButtonTooltip.textProperty().bind(
            Bindings.when(selectedLendingProperty.isNull())
                .then("No lending selected")
                .otherwise(
                    Bindings.when(Bindings.createBooleanBinding(
                        () -> selectedLendingProperty.get() != null && selectedLendingProperty.get().isReturned(),
                        selectedLendingProperty
                    ))
                    .then("Already returned")
                    .otherwise("")
                )
        );

        // Initialize search field listener
        lendingSearchField.textProperty().addListener(observable -> {
            lendingTableHandler();
        });

        // Set row factory for color coding
        lendingTable.setRowFactory(tv -> new TableRow<Lending>() {
            @Override
            protected void updateItem(Lending lending, boolean empty) {
                super.updateItem(lending, empty);
                
                if (empty || lending == null) {
                    setStyle("");
                } else if (lending.isReturned()) {
                    // Green background for returned lendings
                    setStyle("-fx-background-color: #90EE90;");
                } else if (lending.getReturnDate().isBefore(LocalDate.now())) {
                    // Red background for overdue lendings
                    setStyle("-fx-background-color: #FFB6C1;");
                } else {
                    setStyle("");
                }
            }
        });
        
        Platform.runLater(() -> {
            if (lendingSet == null)
                return;

            // Binding for number of entries in the set
            lendingSearchField.promptTextProperty().bind(Bindings.format("OmniSearch %d lendings", lendingSet.size()));
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
                    lendingTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
                } else {
                    lendingTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_SUBSEQUENT_COLUMNS);
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
        lendingTable.refresh(); // Force refresh to update cell values
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

        launchViewEditLendingDialog(null, true, PropMode.EDIT, primaryStage);
    }

    /**
     * @brief   Initializes the lending table columns.
     * @details This method sets up the cell value factories for each column in the lending table, defining how data
     *          from the Lending objects will be displayed in each column.
     */
    private void initializeLendingColumns() {
        // Lending specific columns
        lendingIdColumn.setCellValueFactory(cellData -> {
            Lending lending = cellData.getValue();
            return new ReadOnlyObjectWrapper<>(lending != null ? lending.getLendingId() : null);
        });

        lendingReturnDateColumn.setCellValueFactory(cellData -> {
            Lending lending = cellData.getValue();
            return new ReadOnlyObjectWrapper<>(lending != null ? lending.getReturnDate() : null);
        });

        // Book specific columns - accessing nested Book object
        lendingIsbnColumn.setCellValueFactory(cellData -> {
            Lending lending = cellData.getValue();
            Book book = lending != null ? lending.getBook() : null;
            return new ReadOnlyStringWrapper(book != null ? book.getIsbn() : "");
        });

        lendingTitleColumn.setCellValueFactory(cellData -> {
            Lending lending = cellData.getValue();
            Book book = lending != null ? lending.getBook() : null;
            return new ReadOnlyStringWrapper(book != null ? book.getTitle() : "");
        });

        // User specific columns - accessing nested User object
        lendingUserIdColumn.setCellValueFactory(cellData -> {
            Lending lending = cellData.getValue();
            User user = lending != null ? lending.getUser() : null;
            return new ReadOnlyStringWrapper(user != null ? user.getId() : "");
        });

        lendingUserColumn.setCellValueFactory(cellData -> {
            Lending lending = cellData.getValue();
            User user = lending != null ? lending.getUser() : null;
            return new ReadOnlyStringWrapper(user != null ? user.getFullName() : "");
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
    public void launchViewEditLendingDialog(Lending lendingToViewOrEdit, boolean isNewLending,
                                            PropMode mode, Stage ownerStage) {
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
            stage.initOwner(ownerStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);

            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
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
    private void handleLendingViewEdit() {
        if (selectedLending != null) {
            launchViewEditLendingDialog(selectedLending, false, PropMode.VIEW, primaryStage);
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
            lendingSet.addOrEditLending(selectedLending);
            // Save updated book and user with decremented counters
            bookSet.addOrEditBook(selectedLending.getBook());
            userSet.addOrEditUser(selectedLending.getUser());
            mainController.refreshTabData();
        }
    }
}
