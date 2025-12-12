package poco.company.group01pocolib.mvc.controller;

import java.util.List;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
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
        // if userset is loaded, load users into userData list
        if (userSet != null) {
            userData.setAll(userSet.getUserSet());
            userTable.setItems(userData);   //add all users to the table view
        }
    }

    /**
     * @brief   Refreshes the data in the controller by fetching the lists from the model.
     */
    public void refreshData() {
        loadData(); //reload data from model
        userTable.refresh();  //refresh table view
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
     * @brief   Allows handling of swaps of shown lists based on the search field and sorting
     */
    private void userTableHandler() {
        // Listen to selection changes in the table
        userTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                selectedUser = newValue;
                // Enable/disable buttons based on selection
                userViewEditButton.setDisable(newValue == null);
                userLendButton.setDisable(newValue == null);
            }
        );
        
        // Initially disable buttons until a user is selected
        userViewEditButton.setDisable(true);
        userLendButton.setDisable(true);
        
        // Add listener to search field for real-time filtering
        userSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                // If search field is empty, show all users
                loadData();
            } else {
                // Filter users based on search query
                List<User> searchResults = userSet.search(newValue.toLowerCase().trim());

                // Clear old data and update the observable list with search results
                userData.clear();
                userData.addAll(searchResults);
            }
        });
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
        try{
            // Create a new empty user to be added
            User newUser = new User();

            // Load the fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/poco/company/group01pocolib/mvc/view/prop-user.fxml"));
            VBox page = loader.load();

            // Create the dialog Stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add New User");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the controller
            UserPropController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setDependencies(mainController, userSet);
            controller.setUser(newUser);        //TODO: implement setUser to automatically set edit mode if the user is new

            dialogStage.showAndWait();

            // If the user clicked Save, add the new user to the UserSet and refresh data
            if (controller.isSaveClicked()) {
                userSet.addOrEditUser(newUser);
                refreshData();
                mainController.refreshTabData();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not open the Add User dialog.");
            alert.setContentText("An unexpected error occurred: " + e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * @brief   Allows to view or edit the selected user when the "View & Edit" button is clicked on the User tab.
     * @details This method handles both the model and the orchestration of the popup dialog for viewing or editing
     *          a user.
     */
    @FXML
    private void handleUserViewEdit() {
        
        if (selectedUser == null) {
            return; // No user selected(button disabled via Listner in TableHandler), do nothing
        }

        try{
            // Load the fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/poco/company/group01pocolib/mvc/view/prop-user.fxml"));
            VBox page = loader.load();

            // Create the dialog Stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("View / Edit User");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the controller
            UserPropController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setDependencies(mainController, userSet);
            controller.setUser(selectedUser);  //set the selected user for viewing/editing

            dialogStage.showAndWait();

            // If the user clicked Save, update the user in the UserSet and refresh data
            if (controller.isSaveClicked()) {
                userSet.addOrEditUser(selectedUser); //update existing user
                refreshData();
                mainController.refreshTabData();
            }
        }catch(Exception e){
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not open the View / Edit User dialog.");
            alert.setContentText("An unexpected error occurred: " + e.getMessage());
            alert.showAndWait();
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
            return; // No user selected(button disabled via Listner in TableHandler), do nothing
        }

        if (!selectedUser.canBorrow()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Lending Not Allowed");
            alert.setHeaderText("User Cannot Borrow More Books");
            alert.setContentText("The selected user has reached the maximum number of borrowed books and cannot borrow more at this time.");
            alert.showAndWait();
            return;
        }

        // Check if a book was already selected
        if (selectedBook != null) {
            try {
                // Open the lending dialog with user and book pre-selected
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/poco/company/group01pocolib/mvc/view/prop-lending.fxml"));
                VBox page = loader.load();
                
                Stage dialogStage = new Stage();
                dialogStage.setTitle("New Lending");
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.initOwner(primaryStage);
                dialogStage.setScene(new Scene(page));
                
                LendingPropController controller = loader.getController();
                controller.setDialogStage(dialogStage);
                controller.setDependencies(lendingSet, bookSet, userSet, mainController);
                
                // The LendingPropController will handle creating the lending internally
                // TODO: LendingPropController needs methods to pre-select user and book
                
                dialogStage.showAndWait();
                
                if (controller.isSaveClicked()) {
                    // Refresh data after lending is created
                    refreshData();
                    mainController.refreshTabData();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Could not open the New Lending dialog.");
                alert.setContentText("An unexpected error occurred: " + e.getMessage());
                alert.showAndWait();
            }
        } else {
            // No book selected, go to Book tab to select one
            // TODO: mainController needs getter methods for tabs and controllers
            mainController.switchToTab(mainController.getBookTab());
            mainController.setSelectedUserForLending(selectedUser);
        }
    }
    // TODO: no idea how to handle this
}