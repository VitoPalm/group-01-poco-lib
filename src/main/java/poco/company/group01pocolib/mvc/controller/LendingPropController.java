package poco.company.group01pocolib.mvc.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import poco.company.group01pocolib.mvc.model.*;
import java.time.LocalDate;
import javafx.scene.control.Alert.AlertType;

public class LendingPropController {
    // ------------- //
    // Shared fields //
    // ------------- //
    private Stage dialogStage;
    private Lending lending;
    private LendingSet lendingSet;
    private BookSet bookSet;
    private UserSet userSet;
    private PocoLibController mainController;

    private final BooleanProperty validInput = new SimpleBooleanProperty(true);
    private boolean isNewLending = false;

    
    // ----------------- //
    // View declarations //
    // ----------------- //
    @FXML private VBox viewBox;

    @FXML private Label lendingIdLabel;
    @FXML private Label returnDateLabel;

    @FXML private Hyperlink bookLinkView;
    @FXML private Hyperlink userLinkView;

    @FXML private Button editButton;
    @FXML private Button deleteButton;
    @FXML private Button returnButton;

    // ----------------- //
    // Edit declarations //
    // ----------------- //
    @FXML private VBox editBox;

    @FXML private Hyperlink bookLinkEdit;
    @FXML private Hyperlink userLinkEdit;

    @FXML private DatePicker returnDatePicker;

    @FXML private Label infoLabel;
    @FXML private Button saveButton;

    

    // ------------ //
    // Edit methods //
    // ------------ //
    /**
     * @brief   Initializes the controller class allowing real time id and email verification. This method is
     *          automatically called after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        viewBox.managedProperty().bind(viewBox.visibleProperty());
        editBox.managedProperty().bind(editBox.visibleProperty());
        infoLabel.managedProperty().bind(infoLabel.visibleProperty());

        returnDatePicker.valueProperty().addListener(observable -> {
            validInput.set(validateInput());
        });

        saveButton.disableProperty().bind(validInput.not());
    }

    /**
     * @brief   Sets the mode of the dialog (view, edit, view-only)
     * @param   mode The mode to set. Can be any value of the PropMode enum.
     */
    public void setMode(PropMode mode) {
        if (mode == PropMode.VIEW) {
            viewBox.setVisible(true);
            editBox.setVisible(false);
            
            // Update return button based on lending status
            if (lending != null && lending.isReturned()) {
                returnButton.setText("Unmark as returned");
            } else {
                returnButton.setText("Mark as returned");
            }
        } else if (mode == PropMode.EDIT) {
            viewBox.setVisible(false);
            editBox.setVisible(true);
        } else if (mode == PropMode.VIEW_ONLY) {
            // If not editable, stay in view mode and disable all buttons
            viewBox.setVisible(true);
            editBox.setVisible(false);

            editButton.setDisable(true);
            deleteButton.setDisable(true);
            returnButton.setDisable(true);
        }
    }

    /**
     * @brief   Sets the dialog stage
     * @param   dialogStage The stage to set
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * @brief   Sets the dependencies for this controller
     *
     * @param   lendingSet     The LendingSet containing the lendings
     * @param   bookSet        The BookSet containing the books
     * @param   userSet        The UserSet containing the users
     * @param   mainController The main PocoLibController
     */
    public void setDependencies(LendingSet lendingSet, BookSet bookSet, UserSet userSet,
                                PocoLibController mainController) {
        this.lendingSet = lendingSet;
        this.bookSet = bookSet;
        this.userSet = userSet;
        this.mainController = mainController;
    }

    /**
     * @brief   Sets the lending to be edited/viewed in the dialog.
     * @details If the lending is `null`, a new empty lending is created to allow the creation of a new lending. When
     *          setting the lending, both the view labels and edit fields are updated with the lending details.
     *
     * @param   lending The lending to set.
     * @throws  IllegalArgumentException If `lending` is `null` **and** no book or user is selected in the main
     *                                   controller.
     */
    public void setLending(Lending lending) {
        this.lending = lending;

        // If lending is null, create a new empty lending (for new lending creation)
        if (lending == null) {
            if (mainController.getMasterSelectedBook() == null ||
                mainController.getMasterSelectedUser() == null) {
                throw new IllegalArgumentException("Cannot create new lending without selected book and user.");
            }

            isNewLending = true;
            this.lending = new Lending(mainController.getMasterSelectedBook(),
                                       mainController.getMasterSelectedUser(),
                                       LocalDate.now());
        }

        // Set view labels/links
        lendingIdLabel.setText(String.valueOf(this.lending.getLendingId()));
        bookLinkView.setText(this.lending.getBook().getTitle());
        userLinkView.setText(this.lending.getUser().getFullName());
        returnDateLabel.setText(this.lending.getReturnDate().toString());

        // Set edit links/fields
        bookLinkEdit.setText(this.lending.getBook().getTitle());
        userLinkEdit.setText(this.lending.getUser().getFullName());
        returnDatePicker.setValue(this.lending.getReturnDate());
    }

    /**
     * @brief   Updates the view with the current lending details
     * @details This method is needed to refresh the dialog when the lending details are changed by editing it.
     */
    public void updateView() {
        setLending(this.lending);
    }

    public Button getReturnButton() {
        return returnButton;
    }


    // -------------------- //
    // View button handlers //
    // -------------------- //
    /**
     * @brief   Handles the edit button click event. It opens the lending edit dialog and refreshes afterward.
     */
    @FXML
    private void handleEdit() {
        setMode(PropMode.EDIT);
        updateView();
    }

    /**
     * @brief   Handles the mark as returned button click event. It marks the lending as returned for it to be updated on closure by LendingTabController
     */
    @FXML
    private void handleMarkAsReturned() {
        if (returnButton.getText().equals("Mark as returned")) 
            returnButton.setText("Unmarked as returned");

        else    
            returnButton.setText("Mark as returned");
    }
    

    /**
     * @brief   Handles the delete button click event. It deletes the lending after confirmation.
     */
    @FXML
    private void handleDelete() {
        // Create confirmation alert before deleting
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the lending?");
        alert.initOwner(dialogStage);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Delete Lending");

        // Wait for confirmation
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // If lending is not returned, mark it as returned to update counters
                if (!lending.isReturned()) {
                    lending.setReturned();
                    // Save updated book and user with decremented counters
                    bookSet.addOrEditBook(lending.getBook());
                    userSet.addOrEditUser(lending.getUser());
                }
                lendingSet.removeLending(lending);
                mainController.refreshTabData();
                dialogStage.close();
            }
        });
    }

    // -------------------- //
    // Edit button handlers //
    // -------------------- //
    /**
     * @brief   Handles the save button click event. It validates the input fields and saves the lending details if
     *          valid, then, it closes the dialog.
     */
    @FXML
    private void handleSave() {
        // Save lending details from edit fields
        lending.setReturnDate(returnDatePicker.getValue());

        // Add or edit lending in the lending set
        lendingSet.addOrEditLending(lending);

        // Show success info
        infoLabel.setVisible(true);

        // Wait a moment to show the info label
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Clear selected book and user in main controller if it was a new lending
        if (isNewLending) {
            // Increment counters for new lending
            lending.getBook().lendCopy();
            lending.getUser().incrementBorrowedBooksCount();
            // Save updated book and user
            bookSet.addOrEditBook(lending.getBook());
            userSet.addOrEditUser(lending.getUser());
            mainController.setMasterSelectedBook(null);
            mainController.setMasterSelectedUser(null);
        }

        // Refresh the data in all tabs
        mainController.refreshTabData();

        // Close the dialog
        dialogStage.close();
    }

    /**
     * @brief   Handles the cancel button click event. It simply closes the dialog without saving any changes.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }


    /**
     * @brief   Validates the input fields in the dialog.
     * @details This method validates all input fields and displays error messages if any field is empty or not valid.
     *
     * @return  `true` if all fields are valid, `false` otherwise
     */
    private boolean validateInput() {
        boolean output = true;

        if (lending.getBook() == null) {
            output = false;
        } else if (lending.getUser() == null) {
            output = false;
        } else if (returnDatePicker.getValue() == null) {
            output = false;
        }

        return output;
    }

    // ------------- //
    // Link Handlers //
    // ------------- //
    /**
     * @brief   Handles the view book link click event. It opens the book dialog in view-only mode.
     */
    @FXML
    private void handleViewBook() {
        if (lending != null && lending.getBook() != null) {
            Book selectedBook = lending.getBook();
            mainController.getBookTabController().launchViewEditBookDialog(selectedBook,false,
                                                                           PropMode.VIEW_ONLY, dialogStage);
        }
    }

    /**
     * @brief   Handles the view user link click event. It opens the user dialog in view-only mode.
     */
    @FXML
    private void handleViewUser() {
        if (lending != null && lending.getUser() != null) {
            User selectedUser = lending.getUser();
            mainController.getUserTabController().launchViewEditUserDialog(selectedUser,false,
                                                                           PropMode.VIEW_ONLY, dialogStage);
        }
    }
}
