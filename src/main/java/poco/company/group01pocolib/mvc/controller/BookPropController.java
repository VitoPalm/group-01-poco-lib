package poco.company.group01pocolib.mvc.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import poco.company.group01pocolib.mvc.model.Book;
import poco.company.group01pocolib.mvc.model.BookSet;

import java.util.ArrayList;

public class BookPropController {

    // ----------------- //
    // View declarations //
    // ----------------- //
    @FXML private VBox viewBox;

    @FXML private Label isbnLabel;
    @FXML private Label titleLabel;
    @FXML private Label authorsLabel;
    @FXML private Label yearLabel;
    @FXML private Label copiesLabel;
    @FXML private Hyperlink lentToLink;

    @FXML private Button editButton;
    @FXML private Button deleteButton;
    @FXML private Button lendButton;

    // ----------------- //
    // Edit declarations //
    // ----------------- //
    @FXML private VBox editBox;

    @FXML private TextField isbnField;
    @FXML private TextField titleField;
    @FXML private TextField authorsField;
    @FXML private TextField yearField;

    @FXML private Button minusButton;
    @FXML private TextField copiesField;
    @FXML private Button plusButton;

    @FXML private Label errorLabel;
    @FXML private Button saveButton;


    // ------------- //
    // Shared fields //
    // ------------- //
    private Stage dialogStage;
    private Book book;
    private BookSet bookSet;
    private PocoLibController mainController;

    /**
     * @brief   Initializes the controller class allowing real time ISBN verification. This method is automatically
     *          called after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        
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
            lendButton.setDisable(true);
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
     * @param   bookSet        The BookSet containing the books
     */
    public void setDependencies(PocoLibController mainController, BookSet bookSet) {
        this.mainController = mainController;
        this.bookSet = bookSet;
    }

    /**
     * @brief   Sets the book to be edited/viewed in the dialog.
     * @details If the book is `null`, a new empty book is created to allow the creation of a new book. When setting the
     *          book, both the view labels and edit fields are updated with the book details.
     *
     * @param   book The book to set
     */
    public void setBook(Book book) {
        this.book = book;

        // If book is null, create a new empty book (for new book creation)
        if (book == null) {
            book = new Book("", new ArrayList<String>(), "", 0, 1);
            book.setAuthors("");
        }

        // Set view labels
        isbnLabel.setText(book.getIsbn());
        titleLabel.setText(book.getTitle());
        authorsLabel.setText(book.getAuthorsString());
        yearLabel.setText(String.valueOf(book.getYear()));
        copiesLabel.setText(String.valueOf(book.getCopies()));

        // Set edit fields
        isbnField.setText(book.getIsbn());
        titleField.setText(book.getTitle());
        authorsField.setText(book.getAuthorsString());
        yearField.setText(String.valueOf(book.getYear()));
        copiesField.setText(String.valueOf(book.getCopies()));
    }

    /**
     * @brief   Updates the view with the current book details
     * @details This method is needed to refresh the dialog when the book details are changed by editing it.
     */
    public void updateView() {
        setBook(this.book);
    }

    // -------------------- //
    // View button handlers //
    // -------------------- //
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

    // -------------------- //
    // Edit Button handlers //
    // -------------------- //

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
