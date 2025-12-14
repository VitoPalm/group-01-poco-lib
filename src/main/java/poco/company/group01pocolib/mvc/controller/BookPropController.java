package poco.company.group01pocolib.mvc.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableNumberValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import poco.company.group01pocolib.mvc.model.Book;
import poco.company.group01pocolib.mvc.model.BookSet;

import java.time.LocalDate;
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
    @FXML private Label copiesAvailableLabel;
    @FXML private Hyperlink lentToLink;

    @FXML private Button editButton;
    @FXML private Button deleteButton;
    @FXML private Tooltip deleteButtonTooltip;
    @FXML private Button lendButton;

    // ----------------- //
    // Edit declarations //
    // ----------------- //
    @FXML private VBox editBox;

    @FXML private TextField isbnField;
    @FXML private TextField titleField;
    @FXML private TextField authorsField;
    @FXML private Spinner<Integer> yearSpinner;
    private IntegerProperty yearProperty = new SimpleIntegerProperty();

    @FXML private Button minusButton;
    @FXML private TextField copiesAvailableField;                /// Represents the number of available copies
    private IntegerProperty copiesAvailableProperty = new SimpleIntegerProperty();
    @FXML private Button plusButton;
    @FXML private Label copiesEditLabel;

    @FXML private Label errorLabel;
    @FXML private Button saveButton;
    @FXML private Tooltip saveButtonTooltip;
    private BooleanBinding emptyTextfieldsBinding;

    private BooleanProperty isbnIsValid = new SimpleBooleanProperty(false);


    // ------------- //
    // Shared fields //
    // ------------- //
    private Stage dialogStage;
    private Book book;
    private String originalIsbn; // Store original ISBN to handle ISBN changes
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
        // If book is null, create a new empty book (for new book creation)
        if (book == null) {
            this.book = new Book("", new ArrayList<String>(), "", 0, 1);
            this.book.setAuthors("");
            this.originalIsbn = null; // New book has no original ISBN
        } else {
            this.book = book;
            this.originalIsbn = book.getIsbn(); // Save original ISBN
        }

        // ------------------ //
        //  View static sets  //
        // ------------------ //

        // Properties
        isbnLabel.setText(this.book.getIsbn());
        titleLabel.setText(this.book.getTitle());
        authorsLabel.setText(this.book.getAuthorsString());
        yearLabel.setText(String.valueOf(this.book.getYear()));
        copiesAvailableLabel.setText(String.format("%d now (%d total)", this.book.getCopiesAvailable(), this.book.getCopiesAvailable()+this.book.getCopiesLent()));
        lentToLink.setText(String.format("%d users now (%d users total)", this.book.getCopiesLent(), this.book.getTimesLent()));

        // Buttons and Tooltips
        deleteButton.setDisable(this.book.getCopiesLent() > 0);
        deleteButtonTooltip.setText(this.book.getCopiesLent() > 0? "Book has active lendings" : "");
        
        
        // ------------------ //
        //  Edit static sets  //
        // ------------------ //

        // Set edit properties
        isbnField.setText(this.book.getIsbn());
        titleField.setText(this.book.getTitle());
        authorsField.setText(this.book.getAuthorsString());
        yearSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, LocalDate.now().getYear()+10, this.book.getYear() > 0? this.book.getYear() : LocalDate.now().getYear()));
        copiesAvailableField.setText(String.valueOf(this.book.getCopiesAvailable()));

        initializeBindings();
    }

    private void initializeBindings() {
        // ----------------- //
        //   View bindings   //
        // ----------------- //


        // ----------------- //
        //   Edit bindings   //
        // ----------------- //

        // Binding to get the value of numerical fields as an IntegerProperties (the numberstringconverter messes up with Locales)
        copiesAvailableField.textProperty().bindBidirectional(copiesAvailableProperty, new NumberStringConverter());
        yearProperty.bind(yearSpinner.valueProperty());

        // Binding for any of the textfields empty
        emptyTextfieldsBinding = isbnField.textProperty().isEmpty().or(      
            titleField.textProperty().isEmpty().or(
            authorsField.textProperty().isEmpty().or(
            yearSpinner.valueProperty().isNull().or(
            copiesAvailableField.textProperty().isEmpty()))));

        // Binding to disable the minus button on 0 in the available copiesField
        minusButton.disableProperty().bind(Bindings.when(Bindings.lessThanOrEqual(copiesAvailableProperty, 0)).then(true).otherwise(false));

        // ISBN univocity check
        isbnIsValid.bind(Bindings.createBooleanBinding(() -> {
            String isbn = isbnField.getText();

            if (isbn == null || isbn.isBlank())     // if blank it cannot be saved
                return false;

            if (isbn.equals(originalIsbn))               // In Edit mode you can make changes keeping the same isbn
                return true;

            return !bookSet.isStored(isbn);         // finally checks if other books have the same isbn inserted
        }, isbnField.textProperty()));
        
        // copiesEditLabel binding
        final int lentCopies = book.getCopiesLent();
        copiesEditLabel.textProperty().bind(Bindings.createStringBinding(() -> {
            return String.format("(Lent: %d; Total: %d)", lentCopies, copiesAvailableProperty.get()+lentCopies);}, copiesAvailableProperty));

        // Save button disabling logic
        saveButton.disableProperty().bind(emptyTextfieldsBinding.or(
            Bindings.not(isbnIsValid).or(               // invalid isbn
            
            yearProperty.lessThanOrEqualTo(0).or(       // invalid numbers 
            copiesAvailableProperty.lessThan(0)))));

        // Save button tooltip message logic
        saveButtonTooltip.textProperty().bind(
            Bindings.when(emptyTextfieldsBinding)
                .then("Some fields are incomplete")
            .otherwise("Some data is invalid"));
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
        setMode(PropMode.EDIT);
        updateView();
    }

    /**
     * @brief   Handles the delete button click event. It deletes the book after confirmation.
     */
    @FXML
    private void handleDelete() {
        // Create confirmation alert before deleting
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the Book?");
        alert.initOwner(dialogStage);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Delete Book");

        // Wait for confirmation
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                bookSet.removeBook(book.getIsbn());
                mainController.refreshTabData();
                dialogStage.close();
            }
        });
    }

    /**
     * @brief   Handles the lend button click event. It switches to the User tab if no user was selected, if user was
     *          selected, it opens the dialog for a new lending (allowing the user to set the return date).
     */
    @FXML
    private void handleLend() {
        if (book.getCopiesAvailable() == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(dialogStage);
            alert.setTitle("Lending Not Allowed");
            alert.setHeaderText("No Copies Available");
            alert.setContentText("This book has no available copies and cannot be lent at this time.");
            alert.showAndWait();
            return;
        }

        // This is an observed property, so setting it will trigger a check by the PocoLibController to switch tabs
        mainController.setMasterSelectedBook(book);
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
        int currentCopiesAvailable = getCopiesAvailableAsInteger();
        copiesAvailableField.setText(String.valueOf(currentCopiesAvailable - 1));
    }

    /**
     * @brief   Handles the increment button click event. It simply increases the number in the 'copiesField' by 1.
     */
    @FXML
    private void handleIncrement() {
        int currentCopiesAvailable = getCopiesAvailableAsInteger();
        if (currentCopiesAvailable >= 1) {
            copiesAvailableField.setText(String.valueOf(currentCopiesAvailable + 1));
            errorLabel.setText("");
        } else {
            copiesAvailableField.setText("1");
            errorLabel.setText("");
        }
    }

    /**
     * @brief   Handles the save button click event. It validates the input fields and saves the book details if valid.
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
        }else {
            // If ISBN changed, remove the book with the old ISBN first
            String newIsbn = isbnField.getText().trim();
            if (originalIsbn != null && !originalIsbn.equals(newIsbn)) {
                bookSet.removeBook(originalIsbn);
            }
            
            book.setIsbn(newIsbn);
            book.setTitle(titleField.getText());
            book.setAuthors(authorsField.getText());
            book.setYear(yearSpinner.getValue());
            book.setCopiesAvailable(getCopiesAvailableAsInteger());

            bookSet.addOrEditBook(book);

            //TODO: missing info label stuff and stuff if book is new (idk if we need that here)

            mainController.refreshTabData();
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
     * @brief   Converts the copies to an Integer. If the conversion fails, it returns -1.
     * @return  The number of copies as an Integer, or -1 if conversion fails
     */
    private int getCopiesAvailableAsInteger() {
        try {
            return Integer.parseInt(copiesAvailableField.getText().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * @brief   Validates the input fields in the dialog.
     * @details This method validates all input fields and displays error messages if any field is empty or not valid.
     *
     * @return  `true` if all fields are valid, `false` otherwise
     */
    private boolean validateInput() {
        StringBuilder errorMessage = new StringBuilder();

        // Validate ISBN
        if (isbnField.getText() == null || isbnField.getText().trim().isEmpty()) {
            errorMessage.append("ISBN is required.\n");
        }

        // Validate Title
        if (titleField.getText() == null || titleField.getText().trim().isEmpty()) {
            errorMessage.append("Title is required.\n");
        }

        // Validate Authors
        if (authorsField.getText() == null || authorsField.getText().trim().isEmpty()) {
            errorMessage.append("At least one author is required.\n");
        }


        // Validate Available Copies
        int copiesAvailable = getCopiesAvailableAsInteger();
        if (copiesAvailable < 0) {
            errorMessage.append("Number of available copies must be at least 0.\n");
        }

        // Set error label and return result
        if (errorMessage.length() == 0) {
            errorLabel.setText("");
            return true;
        } else {
            errorLabel.setText(errorMessage.toString());
            return false;
        }
    }
}
