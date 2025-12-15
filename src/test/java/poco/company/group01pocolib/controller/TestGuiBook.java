package poco.company.group01pocolib.controller;

import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import poco.company.group01pocolib.Launcher;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.control.LabeledMatchers.hasText;
import static org.testfx.util.WaitForAsyncUtils.sleep;

import java.util.concurrent.TimeUnit;

/**
 * @brief Test class for Book Tab GUI functionality
 */
@ExtendWith(ApplicationExtension.class)
class TestGuiBook {

    /**
     * @brief Start method to launch the JavaFX application for testing.
     */
    @Start
    public void start(Stage stage) throws Exception {
        new Launcher().start(stage);
    }

    /**
     * @brief Helper method to navigate to Books tab reliably.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    private void navigateToBookTab(FxRobot robot) {
        sleep(500, TimeUnit.MILLISECONDS);
        // Multiple clicks to ensure tab is selected
        robot.clickOn("Books");
        sleep(100, TimeUnit.MILLISECONDS);
        robot.doubleClickOn("Books");
        sleep(100, TimeUnit.MILLISECONDS);
        robot.clickOn("Books");
        sleep(300, TimeUnit.MILLISECONDS);
    }

    /**
     * @brief Test to verify that the application starts correctly and the main tab pane is visible.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testApplicationStartup(FxRobot robot) {
        verifyThat("#mainTabPane", isVisible());
    }

    /**
     * @brief Test to verify navigation to the Book Tab.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testNavigationToBookTab(FxRobot robot) {
        navigateToBookTab(robot);
        verifyThat("#bookAddButton", hasText("Add"));
    }

    /**
     * @brief Test to verify the book table is visible with all columns.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testBookTable(FxRobot robot) {
        navigateToBookTab(robot);
        
        verifyThat("#bookTable", isVisible());
        verifyThat("#bookIsbnColumn", isVisible());
        verifyThat("#bookTitleColumn", isVisible());
        verifyThat("#bookAuthorsColumn", isVisible());
        verifyThat("#bookYearColumn", isVisible());
        verifyThat("#bookAvailableColumn", isVisible());
        verifyThat("#bookLentColumn", isVisible());
    }

    /**
     * @brief Test to verify the search functionality in the Book Tab.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testBookOmnisearch(FxRobot robot) {
        navigateToBookTab(robot);

        verifyThat("#bookSearchField", isVisible());
        
        robot.moveTo("#bookSearchField");
        robot.clickOn("#bookSearchField");

        robot.type(javafx.scene.input.KeyCode.J);
        sleep(1, TimeUnit.SECONDS);
        robot.type(javafx.scene.input.KeyCode.A);
        sleep(1, TimeUnit.SECONDS);
        robot.type(javafx.scene.input.KeyCode.V);
        sleep(1, TimeUnit.SECONDS);
        robot.type(javafx.scene.input.KeyCode.A);
        sleep(1, TimeUnit.SECONDS);
        
        // Verifica il contenuto del campo
        TextField searchField = robot.lookup("#bookSearchField").query();
        
        assertEquals("java", searchField.getText());
        
        // Verifica che la tabella sia visibile
        verifyThat("#bookTable", isVisible());
        sleep(1, TimeUnit.SECONDS);
        
        // Cancella il campo di ricerca
        robot.clickOn("#bookSearchField");
        sleep(30, TimeUnit.MILLISECONDS);
        robot.interact(() -> searchField.clear());
        sleep(1, TimeUnit.SECONDS);
    }

    /**
     * @brief Test to verify button states: Add button is always enabled, 
     * while View & Edit and Lend buttons are disabled when no book is selected.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testButtonStates(FxRobot robot) {
        navigateToBookTab(robot);
        
        // Add button should always be enabled
        verifyThat("#bookAddButton", isEnabled());
        verifyThat("#bookAddButton", hasText("Add"));
        
        // View & Edit and Lend buttons should be disabled when nothing is selected
        verifyThat("#bookViewEditButton", isDisabled());
        verifyThat("#bookLendButton", isDisabled());
    }

    /**
     * @brief Test to verify book add dialog opens.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testOpenAddBookDialog(FxRobot robot) {
        navigateToBookTab(robot);
        robot.clickOn("#bookAddButton");
        
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Verify dialog opens with edit fields
        verifyThat("#editBox", isVisible());
        verifyThat("#isbnField", isVisible());
        verifyThat("#titleField", isVisible());
        verifyThat("#authorsField", isVisible());
        verifyThat("#yearSpinner", isVisible());
        verifyThat("#copiesAvailableField", isVisible());
        
        // Close dialog
        robot.clickOn("Cancel");
        sleep(300, TimeUnit.MILLISECONDS);
    }

    /**
     * @brief Test to verify View & Edit button functionality.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testViewEditButton(FxRobot robot) {
        navigateToBookTab(robot);
        
        // Click on first row in table (select a table cell)
        robot.clickOn(".table-row-cell");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Now button should be enabled
        verifyThat("#bookViewEditButton", isEnabled());
        
        robot.clickOn("#bookViewEditButton");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Verify view dialog opens
        verifyThat("#viewBox", isVisible());
        verifyThat("#isbnLabel", isVisible());
        verifyThat("#titleLabel", isVisible());
        verifyThat("#editButton", isVisible());
        verifyThat("#deleteButton", isVisible());
        
        // Close dialog
        robot.type(javafx.scene.input.KeyCode.ESCAPE);
        sleep(300, TimeUnit.MILLISECONDS);
    }

    /**
     * @brief Test to verify Lend button functionality.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testLendButton(FxRobot robot) {
        navigateToBookTab(robot);
        
        // Click on first row in table
        robot.clickOn(".table-row-cell");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Now button should be enabled
        verifyThat("#bookLendButton", isEnabled());
        
        robot.clickOn("#bookLendButton");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Should switch to Users tab to select a user
        verifyThat("#userTab", isVisible());
    }

    /**
     * @brief Test to verify book selection updates the selected book.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testBookSelection(FxRobot robot) {
        navigateToBookTab(robot);
        
        // Initially buttons should be disabled
        verifyThat("#bookViewEditButton", isDisabled());
        verifyThat("#bookLendButton", isDisabled());
        
        // Click on first row in table
        robot.clickOn(".table-row-cell");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // After selecting a book, buttons should be enabled
        verifyThat("#bookViewEditButton", isEnabled());
        verifyThat("#bookLendButton", isEnabled());
    }

    /**
     * @brief Test to verify the book table updates when search field changes.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testTableUpdateWithSearch(FxRobot robot) {
        navigateToBookTab(robot);
        
        TextField searchField = robot.lookup("#bookSearchField").query();
        
        robot.clickOn("#bookSearchField");
        robot.write("java");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Verify table is still visible and shows filtered results
        verifyThat("#bookTable", isVisible());
        
        // Clear search
        robot.interact(() -> searchField.clear());
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Table should still be visible with all books
        verifyThat("#bookTable", isVisible());
    }

    /**
     * @brief Test to verify search by ISBN.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testSearchByISBN(FxRobot robot) {
        navigateToBookTab(robot);
        
        robot.clickOn("#bookSearchField");
        robot.write("9780451524935");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Verify table shows results
        verifyThat("#bookTable", isVisible());
        // Select first row to view the book details
        robot.clickOn(".table-row-cell");
        sleep(500, TimeUnit.MILLISECONDS);

        // Open the view dialog
        robot.clickOn("#bookViewEditButton");
        sleep(500, TimeUnit.MILLISECONDS);

        // Verify the book title is "1984"
        verifyThat("#titleLabel", hasText("1984"));

        // Close the view dialog
        robot.type(javafx.scene.input.KeyCode.ESCAPE);
        sleep(300, TimeUnit.MILLISECONDS);
        
        TextField searchField = robot.lookup("#bookSearchField").query();
        robot.interact(() -> searchField.clear());
        sleep(300, TimeUnit.MILLISECONDS);
    }

    /**
     * @brief Test to verify search by author.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testSearchByAuthor(FxRobot robot) {
        navigateToBookTab(robot);
        
        robot.clickOn("#bookSearchField");
        robot.write("Umberto Eco");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Verify table shows results
        verifyThat("#bookTable", isVisible());
        // Select first row to view the book details
        robot.clickOn(".table-row-cell");
        sleep(500, TimeUnit.MILLISECONDS);
        // Open the view dialog
        robot.clickOn("#bookViewEditButton");
        sleep(500, TimeUnit.MILLISECONDS);
        // Verify the book title is "Il nome della rosa"
        verifyThat("#titleLabel", hasText("Il Nome della Rosa"));

        TextField searchField = robot.lookup("#bookSearchField").query();
        robot.interact(() -> searchField.clear());
        sleep(300, TimeUnit.MILLISECONDS);
    }

    /**
     * @brief Test to verify search by year.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testSearchByYear(FxRobot robot) {
        navigateToBookTab(robot);
        
        robot.clickOn("#bookSearchField");
        robot.write("2008");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Verify table shows results
        verifyThat("#bookTable", isVisible());
        // Select first row to view the book details
        robot.clickOn(".table-row-cell");
        sleep(500, TimeUnit.MILLISECONDS);
        // Open the view dialog
        robot.clickOn("#bookViewEditButton");
        sleep(500, TimeUnit.MILLISECONDS);
        // Verify the book title is "Clean Code"
        verifyThat("#titleLabel", hasText("Clean Code"));

        TextField searchField = robot.lookup("#bookSearchField").query();
        robot.interact(() -> searchField.clear());
        sleep(300, TimeUnit.MILLISECONDS);
    }

    /**
     * @brief Test to verify adding a new book with valid data.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testAddValidBook(FxRobot robot) {
        navigateToBookTab(robot);
        
        // Click Add button to open dialog
        robot.clickOn("#bookAddButton");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Verify dialog is in edit mode
        verifyThat("#editBox", isVisible());
        
        // Fill in book details
        robot.clickOn("#isbnField");
        robot.write("9780123456789");
        
        robot.clickOn("#titleField");
        robot.write("Test Book Title");
        
        robot.clickOn("#authorsField");
        robot.write("Test Author");
        
        // Set year using spinner
        Spinner<Integer> yearSpinner = robot.lookup("#yearSpinner").query();
        robot.interact(() -> yearSpinner.getValueFactory().setValue(2024));
        
        robot.clickOn("#copiesAvailableField");
        // Clear default value first
        robot.press(javafx.scene.input.KeyCode.CONTROL, javafx.scene.input.KeyCode.A);
        robot.release(javafx.scene.input.KeyCode.A, javafx.scene.input.KeyCode.CONTROL);
        robot.write("5");
        
        // Save the book
        robot.clickOn("#saveButton");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Verify we're back to the book tab
        verifyThat("#bookTable", isVisible());
        
        // Search for the newly added book
        robot.clickOn("#bookSearchField");
        robot.write("Test Book Title");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Verify the book appears in search results
        verifyThat("#bookTable", isVisible());
        robot.clickOn(".table-row-cell");
        sleep(500, TimeUnit.MILLISECONDS);
        robot.clickOn("#bookViewEditButton");
        sleep(500, TimeUnit.MILLISECONDS);
        verifyThat("#titleLabel", hasText("Test Book Title"));

    }

    /**
     * @brief Test to verify that save button is disabled with invalid data.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testAddInvalidBook(FxRobot robot) {
        navigateToBookTab(robot);
        
        // Click Add button
        robot.clickOn("#bookAddButton");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Leave required fields empty - save button should be disabled
        verifyThat("#saveButton", isDisabled());
        
        // Dialog should still be open
        verifyThat("#editBox", isVisible());
        
        // Cancel the dialog
        robot.clickOn("Cancel");
        sleep(300, TimeUnit.MILLISECONDS);
    }

    /**
     * @brief Test to verify adding a book with only ISBN missing.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testAddBookWithoutISBN(FxRobot robot) {
        navigateToBookTab(robot);
        
        robot.clickOn("#bookAddButton");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Fill all fields except ISBN
        robot.clickOn("#titleField");
        robot.write("Book Without ISBN");
        
        robot.clickOn("#authorsField");
        robot.write("Unknown Author");
        
        // Set year using spinner
        Spinner<Integer> yearSpinner = robot.lookup("#yearSpinner").query();
        robot.interact(() -> yearSpinner.getValueFactory().setValue(2024));
        
        // Save button should be disabled without ISBN
        verifyThat("#saveButton", isDisabled());
        
        robot.clickOn("Cancel");
        sleep(300, TimeUnit.MILLISECONDS);
    }

    /**
     * @brief Test to verify canceling book addition.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testCancelBookAddition(FxRobot robot) {
        navigateToBookTab(robot);
        
        robot.clickOn("#bookAddButton");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Fill some data
        robot.clickOn("#titleField");
        robot.write("Cancelled Book");
        
        // Cancel without saving
        robot.clickOn("Cancel");
        sleep(300, TimeUnit.MILLISECONDS);
        
        // Should be back at book tab
        verifyThat("#bookTable", isVisible());
        
        // Search for the book - should not exist
        robot.clickOn("#bookSearchField");
        robot.write("Cancelled Book");
        sleep(300, TimeUnit.MILLISECONDS);
        
        // Clear search
        TextField searchField = robot.lookup("#bookSearchField").query();
        robot.interact(() -> searchField.clear());
    }

}
