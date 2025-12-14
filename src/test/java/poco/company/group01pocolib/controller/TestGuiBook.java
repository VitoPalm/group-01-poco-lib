package poco.company.group01pocolib.controller;

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
    void testAvvioApplicazione(FxRobot robot) {
        verifyThat("#mainTabPane", isVisible());
    }

    /**
     * @brief Test to verify navigation to the Book Tab.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testNavigazioneBookTab(FxRobot robot) {
        navigateToBookTab(robot);
        verifyThat("#bookAddButton", hasText("Add"));
    }

    /**
     * @brief Test to verify the book table is visible with all columns.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testTabellaLibri(FxRobot robot) {
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
    void testOmnisearchLibri(FxRobot robot) {
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
     * @brief Test to verify that buttons are disabled when no book is selected.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testBottoniDisabilitatiSenzaSelezione(FxRobot robot) {
        navigateToBookTab(robot);
        
        // View & Edit and Lend buttons should be disabled when nothing is selected
        verifyThat("#bookViewEditButton", isDisabled());
        verifyThat("#bookLendButton", isDisabled());
    }

    /**
     * @brief Test to verify the Add button is always enabled.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testBottoneAggiungiAbilitato(FxRobot robot) {
        navigateToBookTab(robot);
        
        verifyThat("#bookAddButton", isEnabled());
        verifyThat("#bookAddButton", hasText("Add"));
    }

    /**
     * @brief Test to verify book add dialog opens.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testAperturaDialogAggiungiLibro(FxRobot robot) {
        navigateToBookTab(robot);
        robot.clickOn("#bookAddButton");
        
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Verify dialog opens with edit fields
        verifyThat("#editBox", isVisible());
        verifyThat("#isbnField", isVisible());
        verifyThat("#titleField", isVisible());
        verifyThat("#authorsField", isVisible());
        verifyThat("#yearField", isVisible());
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
    void testBottoneVisualizzaModifica(FxRobot robot) {
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
    void testBottonePrestito(FxRobot robot) {
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
    void testSelezioneLibro(FxRobot robot) {
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
    void testAggiornamentoTabellaConRicerca(FxRobot robot) {
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
    void testRicercaPerISBN(FxRobot robot) {
        navigateToBookTab(robot);
        
        robot.clickOn("#bookSearchField");
        robot.write("978");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Verify table shows results
        verifyThat("#bookTable", isVisible());
        
        TextField searchField = robot.lookup("#bookSearchField").query();
        robot.interact(() -> searchField.clear());
        sleep(300, TimeUnit.MILLISECONDS);
    }

    /**
     * @brief Test to verify search by author.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testRicercaPerAutore(FxRobot robot) {
        navigateToBookTab(robot);
        
        robot.clickOn("#bookSearchField");
        robot.write("Lowe");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Verify table shows results
        verifyThat("#bookTable", isVisible());
        
        TextField searchField = robot.lookup("#bookSearchField").query();
        robot.interact(() -> searchField.clear());
        sleep(300, TimeUnit.MILLISECONDS);
    }

    /**
     * @brief Test to verify search by year.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testRicercaPerAnno(FxRobot robot) {
        navigateToBookTab(robot);
        
        robot.clickOn("#bookSearchField");
        robot.write("2023");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Verify table shows results
        verifyThat("#bookTable", isVisible());
        
        TextField searchField = robot.lookup("#bookSearchField").query();
        robot.interact(() -> searchField.clear());
        sleep(300, TimeUnit.MILLISECONDS);
    }

    /**
     * @brief Test to verify the search field placeholder text.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testPlaceholderCampoRicerca(FxRobot robot) {
        navigateToBookTab(robot);
        
        TextField searchField = robot.lookup("#bookSearchField").query();
        // The prompt text includes the number of books, format: "OmniSearch X books"
        String promptText = searchField.getPromptText();
        assertEquals(true, promptText != null && promptText.startsWith("OmniSearch"));
    }

    /**
     * @brief Test to verify adding a new book with valid data.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testInserimentoLibroValido(FxRobot robot) {
        navigateToBookTab(robot);
        
        // Click Add button to open dialog
        robot.clickOn("#bookAddButton");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Verify dialog is in edit mode
        verifyThat("#editBox", isVisible());
        
        // Fill in book details
        robot.clickOn("#isbnField");
        robot.write("978-0123456789");
        
        robot.clickOn("#titleField");
        robot.write("Test Book Title");
        
        robot.clickOn("#authorsField");
        robot.write("Test Author");
        
        robot.clickOn("#yearField");
        robot.write("2024");
        
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
    }

    /**
     * @brief Test to verify that adding a book with invalid data shows error.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testInserimentoLibroNonValido(FxRobot robot) {
        navigateToBookTab(robot);
        
        // Click Add button
        robot.clickOn("#bookAddButton");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Leave required fields empty and try to save
        robot.clickOn("#saveButton");
        sleep(300, TimeUnit.MILLISECONDS);
        
        // Should show error alert
        verifyThat(".alert", isVisible());
        verifyThat("OK", isVisible());
        
        // Close alert
        robot.clickOn("OK");
        sleep(200, TimeUnit.MILLISECONDS);
        
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
    void testInserimentoLibroSenzaISBN(FxRobot robot) {
        navigateToBookTab(robot);
        
        robot.clickOn("#bookAddButton");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Fill all fields except ISBN
        robot.clickOn("#titleField");
        robot.write("Book Without ISBN");
        
        robot.clickOn("#authorsField");
        robot.write("Unknown Author");
        
        robot.clickOn("#yearField");
        robot.write("2024");
        
        // Try to save
        robot.clickOn("#saveButton");
        sleep(300, TimeUnit.MILLISECONDS);
        
        // Should show error
        verifyThat(".alert", isVisible());
        robot.clickOn("OK");
        
        robot.clickOn("Cancel");
        sleep(300, TimeUnit.MILLISECONDS);
    }

    /**
     * @brief Test to verify adding a book with invalid year.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testInserimentoLibroAnnoNonValido(FxRobot robot) {
        navigateToBookTab(robot);
        
        robot.clickOn("#bookAddButton");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Fill valid data
        robot.clickOn("#isbnField");
        robot.write("978-1234567890");
        
        robot.clickOn("#titleField");
        robot.write("Test Invalid Year");
        
        robot.clickOn("#authorsField");
        robot.write("Test Author");
        
        // Enter invalid year (3 digits)
        robot.clickOn("#yearField");
        robot.write("999");
        
        // Try to save
        robot.clickOn("#saveButton");
        sleep(300, TimeUnit.MILLISECONDS);
        
        // Should show error about invalid year
        verifyThat(".alert", isVisible());
        robot.clickOn("OK");
        
        robot.clickOn("Cancel");
        sleep(300, TimeUnit.MILLISECONDS);
    }

    /**
     * @brief Test to verify incrementing and decrementing available copies.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testIncrementoDecrementoCopie(FxRobot robot) {
        navigateToBookTab(robot);
        
        robot.clickOn("#bookAddButton");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Default copies should be 1
        TextField copiesAvailableField = robot.lookup("#copiesField").query();
        assertEquals("1", copiesAvailableField.getText());
        
        // Increment copies
        robot.clickOn("#plusButton");
        sleep(100, TimeUnit.MILLISECONDS);
        assertEquals("2", copiesAvailableField.getText());
        
        robot.clickOn("#plusButton");
        sleep(100, TimeUnit.MILLISECONDS);
        assertEquals("3", copiesAvailableField.getText());
        
        // Decrement copies
        robot.clickOn("#minusButton");
        sleep(100, TimeUnit.MILLISECONDS);
        assertEquals("2", copiesAvailableField.getText());
        
        robot.clickOn("#minusButton");
        sleep(100, TimeUnit.MILLISECONDS);
        assertEquals("1", copiesAvailableField.getText());
        
        // Try to decrement below 1 - should show error
        robot.clickOn("#minusButton");
        sleep(100, TimeUnit.MILLISECONDS);
        // Should still be 1 and show error message in errorLabel
        assertEquals("1", copiesAvailableField.getText());
        
        robot.clickOn("Cancel");
        sleep(300, TimeUnit.MILLISECONDS);
    }

    /**
     * @brief Test to verify canceling book addition.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testAnnullaInserimentoLibro(FxRobot robot) {
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
