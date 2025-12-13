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
        robot.clickOn("Books");
        verifyThat("#bookTab", isVisible());
        verifyThat("#bookAddButton", hasText("Add"));
    }

    /**
     * @brief Test to verify the book table is visible with all columns.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testTabellaLibri(FxRobot robot) {
        robot.clickOn("Books");
        
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
        robot.clickOn("Books");
        robot.doubleClickOn("Books");

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
        robot.clickOn("Books");
        
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
        robot.clickOn("Books");
        
        verifyThat("#bookAddButton", isEnabled());
        verifyThat("#bookAddButton", hasText("Add"));
    }

    /**
     * @brief Test to verify book add dialog opens.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testAperturaDialogAggiungiLibro(FxRobot robot) {
        robot.clickOn("Books");
        robot.clickOn("#bookAddButton");
        
        sleep(500, TimeUnit.MILLISECONDS);
        
        // TODO: Implement controller feature - handleBookAdd dialog
        // Verify dialog opens with edit fields
        // verifyThat("#editBox", isVisible());
        // verifyThat("#isbnField", isVisible());
        // verifyThat("#titleField", isVisible());
        // verifyThat("#authorsField", isVisible());
        // verifyThat("#yearField", isVisible());
        // verifyThat("#copiesField", isVisible());
    }

    /**
     * @brief Test to verify View & Edit button functionality.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testBottoneVisualizzaModifica(FxRobot robot) {
        robot.clickOn("Books");
        
        // TODO: Implement controller feature - handleBookViewEdit
        // Select a book first, then test view/edit button
        // robot.clickOn book in table
        // robot.clickOn("#bookViewEditButton");
        // verifyThat dialog opens
    }

    /**
     * @brief Test to verify Lend button functionality.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testBottonePrestito(FxRobot robot) {
        robot.clickOn("Books");
        
        // TODO: Implement controller feature - handleBookLend
        // Select a book first, then test lend button
        // robot.clickOn book in table
        // robot.clickOn("#bookLendButton");
        // verifyThat lending dialog opens
    }

    /**
     * @brief Test to verify book selection updates the selected book.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testSelezioneLibro(FxRobot robot) {
        robot.clickOn("Books");
        
        // TODO: Implement test with actual data
        // After selecting a book, buttons should be enabled
        // robot.clickOn book row in table
        // verifyThat("#bookViewEditButton", isEnabled());
        // verifyThat("#bookLendButton", isEnabled());
    }

    /**
     * @brief Test to verify the book table updates when search field changes.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testAggiornamentoTabellaConRicerca(FxRobot robot) {
        robot.clickOn("Books");
        
        TextField searchField = robot.lookup("#bookSearchField").query();
        
        robot.clickOn("#bookSearchField");
        robot.write("test");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // TODO: Verify table content updates based on search
        // Verify filtered results in table
        
        robot.interact(() -> searchField.clear());
        sleep(500, TimeUnit.MILLISECONDS);
    }

    /**
     * @brief Test to verify search by ISBN.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testRicercaPerISBN(FxRobot robot) {
        robot.clickOn("Books");
        
        robot.clickOn("#bookSearchField");
        robot.write("978");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // TODO: Verify table shows books with matching ISBN
        
        TextField searchField = robot.lookup("#bookSearchField").query();
        robot.interact(() -> searchField.clear());
    }

    /**
     * @brief Test to verify search by author.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testRicercaPerAutore(FxRobot robot) {
        robot.clickOn("Books");
        
        robot.clickOn("#bookSearchField");
        robot.write("Tolkien");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // TODO: Verify table shows books by matching author
        
        TextField searchField = robot.lookup("#bookSearchField").query();
        robot.interact(() -> searchField.clear());
    }

    /**
     * @brief Test to verify search by year.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testRicercaPerAnno(FxRobot robot) {
        robot.clickOn("Books");
        
        robot.clickOn("#bookSearchField");
        robot.write("2020");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // TODO: Verify table shows books from matching year
        
        TextField searchField = robot.lookup("#bookSearchField").query();
        robot.interact(() -> searchField.clear());
    }

    /**
     * @brief Test to verify the search field placeholder text.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testPlaceholderCampoRicerca(FxRobot robot) {
        robot.clickOn("Books");
        
        TextField searchField = robot.lookup("#bookSearchField").query();
        assertEquals("OmniSearch books", searchField.getPromptText());
    }
}