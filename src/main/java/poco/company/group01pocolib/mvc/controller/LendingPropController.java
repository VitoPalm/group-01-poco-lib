package poco.company.group01pocolib.mvc.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import poco.company.group01pocolib.mvc.model.*;

public class LendingPropController {
    // ---------------   //
    // Edit declarations //
    // ---------------   //
    @FXML private DatePicker returnDatePicker;

    @FXML private Label errorLabel;
    @FXML private Button saveButton;

    // ---------------   //
    // View declarations //
    // ---------------   //
    @FXML private Label lendingIdLabel;
    @FXML private Label returnDateLabel;

    @FXML private Hyperlink bookLink;
    @FXML private Hyperlink userLink;

    @FXML private Button deleteButton;
    @FXML private Button returnButton;

    // ---------------   //
    // Shared fields     //
    // ---------------   //
    private Stage dialogStage;
    private Lending lending;
    private boolean saveClicked = false;
    private LendingSet lendingSet;
    private BookSet bookSet;
    private UserSet userSet;
    private PocoLibController mainController;

    // --------------- //
    // Edit methods    //
    // --------------- //

    /**
     * @brief   Initializes the controller class allowing real time id and email verification. This method is
     *          automatically called after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // TODO: implement initialization logic
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
     * @brief   Sets the lending to be edited/viewed in the dialog
     * @param   lending The lending to set
     */
    public void setLending(Lending lending) {
        this.lending = lending;
        // TODO: implement method to set lending details in the dialog
    }

    /**
     * @brief   Returns whether the save button was clicked
     * @return  `true` if the save button was clicked, `false` otherwise
     */
    public boolean isSaveClicked() {
        return saveClicked;
    }

    /**
     * @brief   Updates the view with the current lending details
     * @details This method is needed to refresh the dialog when the lending details are changed by editing it.
     */
    public void updateView() {
        // TODO: implement method to update lending details for the dialog
    }

    // ---------------------- //
    // Edit button handlers   //
    // ---------------------- //

    /**
     * @brief   Handles the save button click event. It validates the input fields and saves the lending details if
     *          valid, then, it closes the dialog.
     */
    @FXML
    private void handleSave() {
        // TODO: implement save logic
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
        // TODO: implement validation logic
        return false;
    }

    // ---------------------- //
    // View button handlers   //
    // ---------------------- //

    /**
     * @brief   Handles the edit button click event. It opens the lending edit dialog and refreshes afterward.
     */
    @FXML
    private void handleEdit() {
        // TODO: implement edit logic
    }

    /**
     * @brief   Handles the mark as returned button click event. It marks the lending as returned.
     */
    @FXML
    private void handleMarkAsReturned() {
        // TODO: implement mark as returned logic
    }

    /**
     * @brief   Handles the delete button click event. It deletes the lending after confirmation.
     */
    @FXML
    private void handleDelete() {
        // TODO: implement delete logic
    }

    // ------------- //
    // Link Handlers //
    // ------------- //

    /**
     * @brief   Handles the view book link click event. It opens the book view dialog.
     */
    @FXML
    private void handleViewBook() {
        // TODO: implement view book logic
    }

    /**
     * @brief   Handles the view user link click event. It opens the user view dialog.
     */
    @FXML
    private void handleViewUser() {
        // TODO: implement view user logic
    }
}
