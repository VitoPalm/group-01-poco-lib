package poco.company.group01pocolib.mvc.controller;

import java.net.URL;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import poco.company.group01pocolib.db.omnisearch.Search.*;
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

    @FXML private VBox containerVBox;

    @FXML private ImageView pocologoImageView;

    @FXML private GridPane userSearchGridPane;
    private ColumnConstraints column0Constraints;
    private ColumnConstraints column2Constraints;
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

    // User Tab Button Tooltips
    @FXML private Tooltip userLendButtonTooltip;
    @FXML private Tooltip userViewEditButtonTooltip;

    // Data management
    private ObservableList<User> userData;
    private List<SearchResult<User>> currentSearchResults;
    
    // Table entry selection
    private User selectedUser;
    private ObjectProperty<User> selectedUserProperty = new SimpleObjectProperty<>();
    private BooleanBinding selectedUserCanBorrow;


    private Stage primaryStage;
    private PocoLibController mainController;

    /**
     * @brief   Initializes the controller class, setting up all the listeners. This method is automatically called after fxml file has been loaded.
     * 
     * @details
     * Setup of all the listeners of the UserTab: selected table entry, Omnisearch textfield
     * - When an entry is selected, the "View/Edit" button will become clickable
     *   - otherwise, a tooltip will show explaining why the button isn't clickable
     * - When an entry with available copies is selected, the "Lend to" button will become clickable
     *   - otherwise, a tooltip will show explaining why the button isn't clickable
     * - When the Omnisearch textfield is empty, the prompt text shows the number of entries in the Set
     * - When the window is resized to a tighter height, the pocologo is hidden
     * - When the window is resized to a tighter width, the table resize policy becomes unconstrained to correctly visualize min. column sizes
     *
     * @author  Vito Palmieri
     * @author  Giovanni Orsini
     */
    @FXML
    private void initialize() {
        // Binding for selected user and property
        userTable.getSelectionModel().selectedItemProperty().addListener(observable -> {
            selectedUser = userTable.getSelectionModel().getSelectedItem();});
        
        selectedUserProperty.bind(userTable.getSelectionModel().selectedItemProperty());

        // Initialize View/Edit Button bindings for disabling when no selection
        userViewEditButton.disableProperty().bind(selectedUserProperty.isNull());

        // Bind View/Edit Tooltip
        userViewEditButtonTooltip.textProperty().bind(
            Bindings.when(selectedUserProperty.isNull())
                .then("No user selected")
                .otherwise(""));

        // Intialize binding telling if a user can borrow !!only updates on selectedUser updates
        selectedUserCanBorrow = Bindings.lessThan(Bindings.createIntegerBinding(() -> 
                selectedUserProperty.get() != null? selectedUserProperty.get().getBorrowedBooksCount() : 1, selectedUserProperty), 3);

        // Initialize Lend Button bindings for disabling when no selection or unavailable copies
        userLendButton.disableProperty().bind(
                userTable.getSelectionModel().selectedItemProperty().isNull().or(Bindings.not(selectedUserCanBorrow)));

        // Bind Lend to button tooltip
        userLendButtonTooltip.textProperty().bind(
            Bindings.when(selectedUserProperty.isNull())
            .then("No user selected")
            .otherwise(Bindings.when(selectedUserCanBorrow)
                .then("")
                .otherwise("Selected User has too many books currently borrowed")));

        // Initialize search field listener
        userSearchField.textProperty().addListener(observable -> {
            userTableHandler();
        });

        Platform.runLater(() -> {
            if (userSet == null)
                return;

            // Note: Actual binding is set in loadData() after userData initialization
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
                    userTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
                } else {
                    userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_SUBSEQUENT_COLUMNS);
                }
            });
        });

                column0Constraints = userSearchGridPane.getColumnConstraints().get(0);
        column2Constraints = userSearchGridPane.getColumnConstraints().get(2);
        Platform.runLater(() -> {
            if (column0Constraints == null || column2Constraints == null || containerVBox.getScene() == null)
                return;
            
            // width listener to control the search gridpane size and placement
            containerVBox.getScene().widthProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.doubleValue() < 700) {
                    column0Constraints.setPrefWidth(0); // (it hides the first and third column to take all the space)
                    column0Constraints.setMinWidth(0);
                    column0Constraints.setHgrow(javafx.scene.layout.Priority.NEVER);
                    column2Constraints.setPrefWidth(0); 
                    column2Constraints.setMinWidth(0);
                    column2Constraints.setHgrow(javafx.scene.layout.Priority.NEVER);

                } else {
                    column0Constraints.setPrefWidth(300); // restores them
                    column0Constraints.setMinWidth(10);
                    column0Constraints.setHgrow(javafx.scene.layout.Priority.ALWAYS);
                    column2Constraints.setPrefWidth(300); 
                    column2Constraints.setMinWidth(10);
                    column2Constraints.setHgrow(javafx.scene.layout.Priority.ALWAYS);

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
     *
     * @param   bookSet The BookSet to use.
     * @param   userSet The UserSet to use.
     * @param   lendingSet The LendingSet to use.
     */
    public void setDataSets(BookSet bookSet, UserSet userSet, LendingSet lendingSet) {
        this.bookSet = bookSet;
        this.userSet = userSet;
        this.lendingSet = lendingSet;
    }

    // ------------------------------------------- //
    // Something I may eventually decide to delete //
    // ------------------------------------------- //
    public TableView<User> getUserTable() {
        return userTable;
    }

    /**
     * @brief   Loads data from the model into the controller.
     */
    public void loadData() {
        if (this.userData == null) {
            this.userData = FXCollections.observableArrayList(userSet.getListOfUsers());
            userTable.setItems(userData);
            // Set up binding for prompt text to show number of users
            userSearchField.promptTextProperty().bind(Bindings.format("OmniSearch %d users", Bindings.size(userData)));
        } else {
            // Use setAll to update existing ObservableList instead of creating new one
            // This ensures table updates correctly when data changes
            userData.setAll(userSet.getListOfUsers());
        }
    }

    /**
     * @brief   Initializes the user table columns.
     * @details This method sets up the cell value factories for each column in the user table, defining how data from
     *          the User objects will be displayed in each column.
     */
    private void initializeUserColumns() {
        userIdColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getId()));
            
        userNameColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getName()));

        userSurnameColumn.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getSurname()));
        
        userEmailColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getEmail()));
        
        userLentColumn.setCellValueFactory(cellData -> 
            new SimpleIntegerProperty(cellData.getValue().getBorrowedBooksCount()).asObject());
    }

    /**
     * @brief   Applies the default sorting method to the user table.
     * @details This method clears any existing sort order and applies the default order based on the user's surname.
     */
    private void applyDefaultSortMethod() {
        // Temporarily remove listener to avoid recursion
        userTable.getSortOrder().removeListener(defaultSortOrderListener);

        try {
            userTable.getSortOrder().clear();
            userSurnameColumn.setSortType(TableColumn.SortType.ASCENDING);
            userTable.getSortOrder().add(userSurnameColumn);
            userTable.sort();
        } finally {
            userTable.getSortOrder().addListener(defaultSortOrderListener);
        }
    }

    /**
     * @brief   Applies the default search sort method to the user table.
     * @details This method sorts the table when containing search results, based on the hit count
     *          of the SearchResult objects returned by the search.
     */
    private void applyDefaultSearchSortMethod() {
        // Temporarily remove listener to avoid recursion
        userTable.getSortOrder().removeListener(searchSortOrderListener);

        try {
            userTable.getSortOrder().clear();

            // Re-order based on hits from search results
            if (currentSearchResults != null && !currentSearchResults.isEmpty()) {
                // Stream the search results
                List<User> sortedByHits = currentSearchResults.stream()
                        // Sort the stream by hit count
                        .sorted()
                        // Map to underlying `User` objects
                        .map(sr -> sr.item)
                        // Collect to list
                        .toList();
                // Update the observable list with hit-sorted users
                userData.setAll(sortedByHits);
            }

        } finally {
            userTable.getSortOrder().addListener(searchSortOrderListener);
        }
    }

    /**
     * @brief   Listener to re-apply default sorting when sort order becomes empty.
     */
    ListChangeListener<TableColumn<User, ?>> defaultSortOrderListener = change -> {
        if (userTable.getSortOrder().isEmpty()) {
            applyDefaultSortMethod();
        }
    };

    /**
     * @brief   Listener to re-apply default search sorting when sort order becomes empty.
     */
    ListChangeListener<TableColumn<User, ?>> searchSortOrderListener = change -> {
        if (userTable.getSortOrder().isEmpty()) {
            applyDefaultSearchSortMethod();
        }
    };

    /**
     * @brief   Allows handling of swaps of shown lists based on the search field and sorting
     */
    private void userTableHandler() {
        if (userSearchField.textProperty().getValue().isBlank()) {
            loadData();
            applyDefaultSortMethod();

            // Remove eventual listener for search sort order
            userTable.getSortOrder().removeListener(searchSortOrderListener);

            // Default sort order listener is added
            userTable.getSortOrder().addListener(defaultSortOrderListener);
        } else {
            // Clear any existing sort order to show results by hits
            userTable.getSortOrder().clear();

            String query = userSearchField.textProperty().getValue();

            // Perform search and store results
            currentSearchResults = userSet.search(query);

            // Initial sort of search results by hits
            applyDefaultSearchSortMethod();

            // Update table items to show only search results
            userTable.setItems(userData);

            // Remove default sort listener
            userTable.getSortOrder().removeListener(defaultSortOrderListener);

            // Add search sort listener
            userTable.getSortOrder().addListener(searchSortOrderListener);
        }
    }

    /**
     * @brief   Initializes the user table by setting up the handler and columns.
     */
    public void initializeTable() {
        userTableHandler();
        initializeUserColumns();
    }

    /**
     * @brief   Launches the View/Edit User dialog.
     * @details This method opens a new dialog window for viewing or editing a user's details. It takes into account
     *          whether the user is new or existing, and whether editing is allowed.
     *
     * @param   userToEdit  The user to view or edit. If `null` and `isNewUser` is `true`, a new user will be created.
     * @param   isNewUser   `true` if creating a new user, `false` if viewing/editing an existing user.
     * @param   mode        The mode of the dialog, either VIEW, VIEW_ONLY, or EDIT.
     */
    public void launchViewEditUserDialog(User userToEdit, boolean isNewUser,
                                         PropMode mode, Stage ownerStage) {
        // Handling logical inconsistencies
        if (userToEdit == null && !isNewUser ||
            userToEdit != null && isNewUser ||
            mode == PropMode.VIEW_ONLY && isNewUser)
            return;

        try {
            URL url = getClass().getResource("/poco/company/group01pocolib/mvc/view/prop-user.fxml");
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();
            UserPropController controller = loader.getController();
            controller.setDependencies(mainController, userSet);

            Stage stage = new Stage();
            controller.setDialogStage(stage);
            controller.setUser(userToEdit);
            stage.setScene(new Scene(root));

            if (isNewUser) {
                stage.setTitle("Add New User");
            } else {
                stage.setTitle("View User Details");
            }

            controller.setMode(mode);
            stage.initOwner(ownerStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);

            stage.show();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
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
        launchViewEditUserDialog(null, true, PropMode.EDIT, primaryStage);
    }

    /**
     * @brief   Allows to view or edit the selected user when the "View & Edit" button is clicked on the User tab.
     * @details This method handles both the model and the orchestration of the popup dialog for viewing or editing
     *          a user.
     */
    @FXML
    private void handleUserViewEdit() {
        if (selectedUser != null) {
            launchViewEditUserDialog(selectedUser, false, PropMode.VIEW, primaryStage);
        }
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
        if (selectedUser == null) {
            return; // No user selected(button disabled via Listener in TableHandler), do nothing
        }

        if (!selectedUser.canBorrow()) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Lending Not Allowed");
            alert.setHeaderText("User Cannot Borrow More Books");
            alert.setContentText("The selected user has reached the maximum number of borrowed books (" +
                                 User.MAX_BORROWED_BOOKS + ") and cannot borrow more at this time.");
            alert.showAndWait();
            return;
        }

        // This is an observed property, so setting it will trigger a check by the PocoLibController to switch tabs
        mainController.setMasterSelectedUser(selectedUser);
    }
}