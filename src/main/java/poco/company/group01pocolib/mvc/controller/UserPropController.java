package poco.company.group01pocolib.mvc.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import poco.company.group01pocolib.mvc.model.User;
import poco.company.group01pocolib.mvc.model.UserSet;

public class UserPropController {
    // ----------------- //
    // View declarations //
    // ----------------- //
    @FXML private VBox viewBox;
    @FXML private Label idLabel;
    @FXML private Label nameLabel;
    @FXML private Label surnameLabel;
    @FXML private Label emailLabel;
    @FXML private Hyperlink borrowedLink;

    @FXML private Button editButton;
    @FXML private Button deleteButton;
    @FXML private Button lendToButton;

    // ----------------- //
    // Edit declarations //
    // ----------------- //
    @FXML private VBox editBox;
    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField surnameField;
    @FXML private TextField emailField;
    
    @FXML private Label errorLabel;
    @FXML private Button saveButton;

    // ------------- //
    // Shared fields //
    // ------------- //
    private Stage dialogStage;
    private User user;
    private UserSet userSet;
    private PocoLibController mainController;

    // ------------ //
    // Edit methods //
    // ------------ //
    /**
     * @brief   Initializes the controller class allowing real time id and email verification. This method is
     *          automatically called after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // TODO: implement initialization logic
    }

    /**
     * @brief   Sets the mode of the dialog (view, edit, view-only)
     * @param   mode The mode to set. Can be any value of the PropMode enum.
     */
    public void setMode(PropMode mode) {
        if (mode == PropMode.VIEW) {
            viewBox.setVisible(true);
            editBox.setVisible(false);
        } else if (mode == PropMode.EDIT) {
            viewBox.setVisible(false);
            editBox.setVisible(true);
        } else if (mode == PropMode.VIEW_ONLY) {
            // If not editable, stay in view mode and disable all buttons
            viewBox.setVisible(true);
            editBox.setVisible(false);

            editButton.setDisable(true);
            deleteButton.setDisable(true);
            lendToButton.setDisable(true);
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
     * @param   mainController The main PocoLibController
     * @param   userSet        The UserSet containing the users
     */
    public void setDependencies(PocoLibController mainController, UserSet userSet) {
        this.mainController = mainController;
        this.userSet = userSet;
    }

    /**
     * @brief   Sets the user to be edited/viewed in the dialog
     * @param   user The user to set
     */
    public void setUser(User user) {
        this.user = user;

        String filler = null;

        if (user == null) {
            filler = "ᚁᚁᚁᚁᚁᚁ"; // Random ancient Irish glyphs to pass checks
            String mailFiller = "s.m.t.h@pocolib.com";
            this.user = new User(filler, filler, filler, mailFiller);
        }

        // Set view fields (filler isn't handled cause a null user is only used to create new users)
        idLabel.setText(this.user.getId());
        nameLabel.setText(this.user.getName());
        surnameLabel.setText(this.user.getSurname());
        emailLabel.setText(this.user.getEmail());

        // Set edit fields
        if (filler != null) {
            idField.setText("");
            nameField.setText("");
            surnameField.setText("");
            emailField.setText("");
        } else {
            idField.setText(this.user.getId());
            nameField.setText(this.user.getName());
            surnameField.setText(this.user.getSurname());
            emailField.setText(this.user.getEmail());
        }
    }

    /**
     * @brief   Updates the view with the current user details
     * @details This method is needed to refresh the dialog when the user details are changed by editing it.
     */
    public void updateView() {
        // TODO: implement method to update user details for the dialog
    }

    // -------------------- //
    // View button handlers //
    // -------------------- //
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

    // -------------------- //
    // Edit button handlers //
    // -------------------- //
    /**
     * @brief   Handles the save button click event. It validates the input fields and saves the user details if valid.
     *          Then, it closes the dialog.
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
}
