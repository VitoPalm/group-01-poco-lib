package poco.company.group01pocolib.mvc.controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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

    @FXML private Label infoLabel;
    @FXML private Label errorLabel;
    @FXML private Button saveButton;

    // ------------- //
    // Shared fields //
    // ------------- //
    private Stage dialogStage;
    private User user;
    private String originalId; // Store original ID to handle ID changes
    private UserSet userSet;
    private PocoLibController mainController;

    private final BooleanProperty validInput = new SimpleBooleanProperty(true);
    private boolean isNewUser = false;

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
        errorLabel.managedProperty().bind(errorLabel.visibleProperty());

        // Real-time input validation
        idField.textProperty().addListener(observable -> validInput.set(validateInput()));
        nameField.textProperty().addListener(observable -> validInput.set(validateInput()));
        surnameField.textProperty().addListener(observable -> validInput.set(validateInput()));
        emailField.textProperty().addListener(observable -> validInput.set(validateInput()));

        errorLabel.textProperty().addListener(observable -> {
            if (dialogStage != null) {
                dialogStage.sizeToScene();
            }
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
            isNewUser = true;
            this.originalId = null; // New user has no original ID

            filler = "ᚁᚁᚁᚁᚁᚁ"; // Random ancient Irish glyphs to pass checks
            String mailFiller = "s.m.t.h@pocolib.com";
            this.user = new User(filler, filler, filler, mailFiller);
        } else {
            this.originalId = user.getId(); // Save original ID
        }

        // Set view fields (filler isn't handled cause a null user is only used to create new users)
        idLabel.setText(this.user.getId());
        nameLabel.setText(this.user.getName());
        surnameLabel.setText(this.user.getSurname());
        emailLabel.setText(this.user.getEmail());
        borrowedLink.setText(this.user.getBorrowedBooksCount() + " books (" + this.user.getBorrowedBooksEverCount() + " total)");

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
        setUser(this.user);
    }

    // -------------------- //
    // View button handlers //
    // -------------------- //
    /**
     * @brief   Handles the edit button click event. It opens the user edit dialog and refreshes afterward.
     */
    @FXML
    private void handleEdit() {
        setMode(PropMode.EDIT);
        updateView();
    }

    /**
     * @brief   Handles the delete button click event. It deletes the user after confirmation.
     */
    @FXML
    private void handleDelete() {
        // Create confirmation alert before deleting
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the user?");
        alert.initOwner(dialogStage);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Delete User");

        // Wait for confirmation
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                userSet.removeUser(user.getId());
                mainController.refreshTabData();
                dialogStage.close();
            }
        });
    }

    /**
     * @brief   Handles the lend to button click event by setting the master selected user to this user.
     */
    @FXML
    private void handleLendTo() {
        if (user == null) return;
        mainController.setMasterSelectedUser(user);
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
        if (!validateInput()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid input fields.");
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Input");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorLabel.getText());
            alert.showAndWait();
        } else {
            // If ID changed, remove the user with the old ID first
            String newId = idField.getText().trim();
            if (originalId != null && !originalId.equals(newId)) {
                userSet.removeUser(originalId);
            }

            // Save user details from edit fields
            user.setId(newId);
            user.setName(nameField.getText());
            user.setSurname(surnameField.getText());
            user.setEmail(emailField.getText());

            // Add or edit user in the user set
            userSet.addOrEditUser(user);

            // Show success info
            infoLabel.setVisible(true);
            errorLabel.setVisible(false);

            // Wait a moment to show the info label
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Refresh the data in all tabs
            mainController.refreshTabData();

            if (isNewUser) {
                // Select the newly created user in the User table
                mainController.getUserTabController().getUserTable().getSelectionModel().select(user);
                mainController.getUserTabController().getUserTable().scrollTo(user);
            }

            // Close the dialog
            dialogStage.close();
        }
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
     * @bug     The error label messes up the layout when shown. Needs investigation.
     *
     * @return  `true` if all fields are valid, `false` otherwise
     */
    private boolean validateInput() {
        StringBuilder errors = new StringBuilder();

        // Validate ID
        if (!User.isValidID(idField.getText())) {
            errors.append("Invalid ID. ID must be alphanumeric and 5-16 characters long.\n");
        }

        // Validate Name
        if (nameField.getText() == null || nameField.getText().isBlank()) {
            errors.append("Name cannot be empty.\n");
        }

        // Validate Surname
        if (surnameField.getText() == null || surnameField.getText().isBlank()) {
            errors.append("Surname cannot be empty.\n");
        }

        // Validate Email
        if (!User.isValidEmail(emailField.getText())) {
            errors.append("Invalid email format.\n");
        }

        // Set error label and return result
        if (errors.isEmpty()) {
            errorLabel.setVisible(false);
            return true;
        } else {
            errorLabel.setText(errors.toString());
            errorLabel.setVisible(true);
            return false;
        }
    }
}
