package poco.company.group01pocolib.mvc.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import poco.company.group01pocolib.mvc.model.Book;

public class BookEditController {
    @FXML private TextField isbnField;
    @FXML private TextField titleField;
    @FXML private TextField authorsField;
    @FXML private TextField yearField;

    @FXML private Button minusButton;
    @FXML private TextField copiesField;
    @FXML private Button plusButton;

    @FXML private Label errorLabel;
    @FXML private Button saveButton;

    private Stage dialogStage;
    private Book book;
    private boolean saveClicked = false;

    /**
     * @brief   Initializes the controller class allowing real time ISBN verification This method is automatically
     *          called after the fxml file has been loaded.
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
     * @brief   Sets the book to be edited in the dialog
     * @param   book The book to set
     */
    public void setBook(Book book) {
        // TODO: implement method to set book details in the dialog
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
     * @brief   Handles the decrement button click event. It simply decreases the number in the 'copiesField' by 1.
     */
    @FXML
    private void handleDecrement() {
        // TODO: implement decrement logic
    }

    /**
     * @brief   Handles the increment button click event. It simply increases the number in the 'copiesField' by 1.
     */
    @FXML
    private void handleIncrement() {
        // TODO: implement increment logic
    }

    /**
     * @brief   Handles the save button click event. It validates the input fields and saves the book details if valid.
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
     * @brief   Converts the copies to an Integer. If the conversion fails, it returns -1.
     * @return  The number of copies as an Integer, or -1 if conversion fails
     */
    private int getCopiesAsInteger() {
        // TODO: implement conversion logic
        return -1;
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
