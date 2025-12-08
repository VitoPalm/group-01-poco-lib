package poco.company.group01pocolib.mvc.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import poco.company.group01pocolib.mvc.model.*;

public class LendingViewController {
    @FXML public Label lendingIdLabel;
    @FXML public Label returnDateLabel;

    @FXML public Hyperlink bookLink;
    @FXML public Hyperlink userLink;

    @FXML public Button deleteButton;
    @FXML public Button returnButton;

    private Stage dialogStage;
    private Lending lending;

    // Dependencies needed for complex updates
    private LendingSet lendingSet;
    private BookSet bookSet;
    private UserSet userSet;
    private PocoLibController mainController;

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
     * @brief   Sets the lending to be viewed in the dialog
     * @param   lending The lending to set
     */
    public void setLending(Lending lending) {
        this.lending = lending;
    }

    /**
     * @brief   Updates the view with the current lending details
     * @details This method is needed to refresh the dialog when the lending details are changed by editing it.
     */
    private void updateView() {
        // TODO: implement method to update lending details for the dialog
    }

    // --------------- //
    // Button Handlers //
    // --------------- //

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

