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
 * @brief Test class for Lending Tab GUI functionality
 */
@ExtendWith(ApplicationExtension.class)
class TestGuiLending {

    /**
     * @brief Start method to launch the JavaFX application for testing.
     */
    @Start
    public void start(Stage stage) throws Exception {
        new Launcher().start(stage);
    }

    /**
     * @brief Test to verify navigation to the Lending Tab.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testNavigationToLendingTab(FxRobot robot) {
        robot.clickOn("Lending Page");
        verifyThat("#lendingTab", isVisible());
        verifyThat("#lendingSearchField", isVisible());
        verifyThat("#lendingTable", isVisible());
    }

    /**
     * @brief Test to verify the lending search field is visible and functional.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testLendingSearchField(FxRobot robot) {
        robot.clickOn("Lending Page");
        
        verifyThat("#lendingSearchField", isVisible());
        TextField searchField = robot.lookup("#lendingSearchField").query();
        assertEquals("", searchField.getText());
    }

    /**
     * @brief Test to verify the lending table is visible with all columns.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testLendingTable(FxRobot robot) {
        robot.clickOn("Lending Page");
        
        verifyThat("#lendingTable", isVisible());
        verifyThat("#lendingIdColumn", isVisible());
        verifyThat("#lendingReturnDateColumn", isVisible());
        verifyThat("#lendingIsbnColumn", isVisible());
        verifyThat("#lendingTitleColumn", isVisible());
        verifyThat("#lendingUserIdColumn", isVisible());
        verifyThat("#lendingUserColumn", isVisible());
    }

    /**
     * @brief Test to verify the search functionality in the Lending Tab.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testLendingOmnisearch(FxRobot robot) {
        robot.clickOn("Lending Page");
        
        verifyThat("#lendingSearchField", isVisible());
        robot.clickOn("#lendingSearchField");
        
        robot.type(javafx.scene.input.KeyCode.DIGIT2);
        sleep(100, TimeUnit.MILLISECONDS);
        robot.type(javafx.scene.input.KeyCode.DIGIT0);
        sleep(100, TimeUnit.MILLISECONDS);
        robot.type(javafx.scene.input.KeyCode.DIGIT2);
        sleep(100, TimeUnit.MILLISECONDS);
        robot.type(javafx.scene.input.KeyCode.DIGIT5);
        sleep(100, TimeUnit.MILLISECONDS);
        
        TextField searchField = robot.lookup("#lendingSearchField").query();
        assertEquals("2025", searchField.getText());
        
        verifyThat("#lendingTable", isVisible());
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Clear search field
        robot.clickOn("#lendingSearchField");
        robot.interact(() -> searchField.clear());
        sleep(500, TimeUnit.MILLISECONDS);
    }

    /**
     * @brief Test to verify that buttons are disabled when no lending is selected.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testButtonsDisabledWithoutSelection(FxRobot robot) {
        robot.clickOn("Lending Page");
        
        // Buttons should be disabled when nothing is selected
        verifyThat("#lendingViewEditButton", isDisabled());
        verifyThat("#lendingReturnedButton", isDisabled());
    }

    /**
     * @brief Test to verify the View & Edit button text.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testViewEditButton(FxRobot robot) {
        robot.clickOn("Lending Page");
        
        verifyThat("#lendingViewEditButton", hasText("View & Edit"));
    }

    /**
     * @brief Test to verify the Mark as returned button text.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testMarkAsReturnedButton(FxRobot robot) {
        robot.clickOn("Lending Page");
        
        verifyThat("#lendingReturnedButton", hasText("Mark as returned"));
    }

    /**
     * @brief Test to verify lending view/edit dialog opens.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testOpenLendingViewDialog(FxRobot robot) {
        robot.clickOn("Lending Page");
        
        // TODO: Implement controller feature - handleLendingViewEdit
        // Select a lending first, then test view/edit button
        // robot.clickOn lending in table
        // robot.clickOn("#lendingViewEditButton");
        // verifyThat dialog opens with lending details
    }

    /**
     * @brief Test to verify mark as returned functionality.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testMarkAsReturned(FxRobot robot) {
        robot.clickOn("Lending Page");
        
        // TODO: Implement controller feature - handleMarkAsReturned
        // Select a lending first, then test mark as returned button
        // robot.clickOn lending in table
        // robot.clickOn("#lendingReturnedButton");
        // verifyThat confirmation dialog or success message
    }

    /**
     * @brief Test to verify lending selection enables buttons.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testLendingSelection(FxRobot robot) {
        robot.clickOn("Lending Page");
        
        // TODO: Implement test with actual data
        // After selecting a lending, buttons should be enabled
        // robot.clickOn lending row in table
        // verifyThat("#lendingViewEditButton", isEnabled());
        // verifyThat("#lendingReturnedButton", isEnabled());
    }

    /**
     * @brief Test to verify the lending table updates when search field changes.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testTableUpdateWithSearch(FxRobot robot) {
        robot.clickOn("Lending Page");
        
        TextField searchField = robot.lookup("#lendingSearchField").query();
        
        robot.clickOn("#lendingSearchField");
        robot.write("test");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // TODO: Verify table content updates based on search
        // Verify filtered results in table
        
        robot.interact(() -> searchField.clear());
        sleep(500, TimeUnit.MILLISECONDS);
    }

    /**
     * @brief Test to verify search by book ISBN.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testSearchByISBN(FxRobot robot) {
        robot.clickOn("Lending Page");
        
        robot.clickOn("#lendingSearchField");
        robot.write("978");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // TODO: Verify table shows lendings with matching ISBN
        
        TextField searchField = robot.lookup("#lendingSearchField").query();
        robot.interact(() -> searchField.clear());
    }

    /**
     * @brief Test to verify search by user ID.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testSearchByUserID(FxRobot robot) {
        robot.clickOn("Lending Page");
        
        robot.clickOn("#lendingSearchField");
        robot.write("USER");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // TODO: Verify table shows lendings for matching user
        
        TextField searchField = robot.lookup("#lendingSearchField").query();
        robot.interact(() -> searchField.clear());
    }

    /**
     * @brief Test to verify search by book title.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testSearchByTitle(FxRobot robot) {
        robot.clickOn("Lending Page");
        
        robot.clickOn("#lendingSearchField");
        robot.write("Java");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // TODO: Verify table shows lendings with matching book title
        
        TextField searchField = robot.lookup("#lendingSearchField").query();
        robot.interact(() -> searchField.clear());
    }
}
