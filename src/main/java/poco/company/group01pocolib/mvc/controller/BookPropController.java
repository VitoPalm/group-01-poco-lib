package poco.company.group01pocolib.mvc.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import poco.company.group01pocolib.mvc.model.Book;
import poco.company.group01pocolib.mvc.model.BookSet;

public class BookPropController {
    // ---------------   //
    // Edit declarations //
    // ---------------   //
    @FXML private TextField isbnField;
    @FXML private TextField titleField;
    @FXML private TextField authorsField;
    @FXML private TextField yearField;

    @FXML private Button minusButton;
    @FXML private TextField copiesField;
    @FXML private Button plusButton;

    @FXML private Label errorLabel;
    @FXML private Button saveButton;

    // ---------------   //
    // View declarations //
    // ---------------   //
    @FXML private Label isbnLabel;
    @FXML private Label titleLabel;
    @FXML private Label authorsLabel;
    @FXML private Label yearLabel;
    @FXML private Label copiesLabel;
    @FXML private Hyperlink lentToLink;

    @FXML private Button deleteButton;
    @FXML private Button lendButton;

    // ---------------   //
    // Shared fields     //
    // ---------------   //
    private Stage dialogStage;
    private Book book;
    private boolean saveClicked = false;
    private BookSet bookSet;
    private PocoLibController mainController;

    // --------------- //
    // Edit methods    //
    // --------------- //

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
     * @brief   Sets the dependencies for this controller
     * @param   mainController The main PocoLibController
     * @param   bookSet        The BookSet containing the books
     */
    public void setDependencies(PocoLibController mainController, BookSet bookSet) {
        this.mainController = mainController;
        this.bookSet = bookSet;
    }

    /**
     * @brief   Sets the book to be edited/viewed in the dialog
     * @param   book The book to set
     */
    public void setBook(Book book) {
        this.book = book;
        // TODO: implement method to set book details in the dialog
    }

    /**
     * @brief   Returns whether the save button was clicked
     * @return  `true` if the save button was clicked, `false` otherwise
     */
    public boolean isSaveClicked() {
        return saveClicked;
    }

    /**
     * @brief   Updates the view with the current book details
     * @details This method is needed to refresh the dialog when the book details are changed by editing it.
     */
    public void updateView() {
        // TODO: implement method to update book details for the dialog
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


    // ---------------------- //
    // View button handlers   //
    // ---------------------- //

    /**
     * @brief   Handles the edit button click event. It opens the book edit dialog and refreshes afterward.
     */
    @FXML
    private void handleEdit() {
        // TODO: implement edit logic
    }

    /**
     * @brief   Handles the delete button click event. It deletes the book after confirmation.
     */
    @FXML
    private void handleDelete() {
        // TODO: implement delete logic
    }

    /**
     * @brief   Handles the lend button click event. It switches to the User tab if no user was selected, if user was
     *          selected, it opens the dialog for a new lending (allowing the user to set the return date).
     */
    @FXML
    private void handleLend() {
        // TODO: implement lend logic
    }

    /**
     * @brief   Handles the lent to hyperlink click event. It switches to the User tab and selects the user who has
     *          currently borrowed the book.
     */
    @FXML
    private void handleViewHistory() {
        // TODO: implement view history logic
    }
}
