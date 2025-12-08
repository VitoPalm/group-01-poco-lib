package poco.company.group01pocolib.mvc.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import poco.company.group01pocolib.mvc.model.Lending;

public class LendingEditController {
    @FXML private DatePicker returnDatePicker;

    @FXML private Label errorLabel;
    @FXML private Button saveButton;

    private Stage dialogStage;
    private Lending lending;
    private boolean saveClicked = false;

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
     * @brief   Sets the lending to be edited in the dialog
     * @param   lending The lending to set
     */
    public void setLending(Lending lending) {
        // TODO: implement method to set lending details in the dialog
    }

    /**
     * @brief   Returns whether the save button was clicked
     * @return  `true` if the save button was clicked, `false` otherwise
     */
    public boolean isSaveClicked() {
        return saveClicked;
    }

    // --------------- //
    // Button handlers //
    // --------------- //

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
}
