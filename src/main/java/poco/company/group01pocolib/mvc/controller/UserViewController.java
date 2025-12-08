package poco.company.group01pocolib.mvc.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import poco.company.group01pocolib.mvc.model.User;
import poco.company.group01pocolib.mvc.model.UserSet;

public class UserViewController {
    @FXML private Label idLabel;
    @FXML private Label nameLabel;
    @FXML private Label surnameLabel;
    @FXML private Label emailLabel;
    @FXML private Hyperlink borrowedLink;

    @FXML private Button deleteButton;
    @FXML private Button lendToButton;

    private Stage dialogStage;
    private User user;
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
     * @param   mainController The main PocoLibController
     * @param   userSet        The UserSet containing the users
     */
    public void setDependencies(PocoLibController mainController, UserSet userSet) {
        this.mainController = mainController;
        this.userSet = userSet;
    }

    /**
     * @brief   Sets the user to be viewed in the dialog
     * @param   user The user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @brief   Updates the view with the current user details
     * @details This method is needed to refresh the dialog when the user details are changed by editing it.
     */
    public void updateView() {
        // TODO: implement method to update user details for the dialog
    }

    // --------------- //
    // Button handlers //
    // --------------- //

    /**
     * @brief   Handles the edit button click event. It opens the user edit dialog and refreshes afterward.
     */
    @FXML
    private void handleEdit() {
        // TODO: implement edit logic
    }

    /**
     * @brief   Handles the delete button click event. It deletes the user after confirmation.
     */
    @FXML
    private void handleDelete() {
        // TODO: implement delete logic
    }

    /**
     * @brief   Handles the lend button click event. It switches to the Book tab if no book was selected, if book was
     *          selected, it opens the dialog for a new lending (allowing the user to set the return date).
     */
    @FXML
    private void handleLendTo() {
        // TODO: implement lend logic
    }

    /**
     * @brief   Handles the lent hyperlink click event. It switches to the Book tab and selects the books currently
     *          borrowed by the user.
     */
    @FXML
    private void handleViewHistory() {
        // TODO: implement view history logic
    }
}
