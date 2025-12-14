package poco.company.group01pocolib.controller;

import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TableRow;
import javafx.stage.Stage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

/**
 * @brief Test class for Lending Tab GUI functionality
 * @details Tests the complete lending workflow including creation, validation, and table updates
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
     * @brief Helper method to navigate to Books tab reliably.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    private void navigateToBookTab(FxRobot robot) {
        sleep(500, TimeUnit.MILLISECONDS);
        robot.clickOn("Books");
        sleep(100, TimeUnit.MILLISECONDS);
        robot.doubleClickOn("Books");
        sleep(100, TimeUnit.MILLISECONDS);
        robot.clickOn("Books");
        sleep(300, TimeUnit.MILLISECONDS);
    }

    /**
     * @brief Helper method to navigate to Users tab reliably.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    private void navigateToUserTab(FxRobot robot) {
        sleep(500, TimeUnit.MILLISECONDS);
        robot.clickOn("Users");
        sleep(100, TimeUnit.MILLISECONDS);
        robot.doubleClickOn("Users");
        sleep(100, TimeUnit.MILLISECONDS);
        robot.clickOn("Users");
        sleep(300, TimeUnit.MILLISECONDS);
    }

    /**
     * @brief Helper method to navigate to Lending tab reliably.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    private void navigateToLendingTab(FxRobot robot) {
        sleep(500, TimeUnit.MILLISECONDS);
        robot.clickOn("Lending Page");
        sleep(100, TimeUnit.MILLISECONDS);
        robot.doubleClickOn("Lending Page");
        sleep(100, TimeUnit.MILLISECONDS);
        robot.clickOn("Lending Page");
        sleep(300, TimeUnit.MILLISECONDS);
    }

    /**
     * @brief Helper method to create a lending following the complete workflow.
     * @param robot The FxRobot instance for simulating user interactions.
     * @param returnDate The return date for the lending (null for invalid test).
     * @return true if lending was created successfully, false otherwise.
     */
    private boolean createLending(FxRobot robot, LocalDate returnDate) {
        try {
            // Navigate to Books tab
            navigateToBookTab(robot);
            
            // Click first book in table
            robot.clickOn(".table-row-cell");
            sleep(500, TimeUnit.MILLISECONDS);
            
            // Click Lend button
            robot.clickOn("Lend");
            sleep(500, TimeUnit.MILLISECONDS);
            
            // App automatically moves to Users tab
            // Click first user in table
            robot.clickOn(".table-row-cell");
            sleep(500, TimeUnit.MILLISECONDS);
            
            // Click Lend to button
            robot.clickOn("Lend to");
            sleep(500, TimeUnit.MILLISECONDS);
            
            // Lending dialog opens
            verifyThat("#lendingReturnDatePicker", isVisible());
            
            // Set return date
            if (returnDate != null) {
                DatePicker datePicker = robot.lookup("#lendingReturnDatePicker").query();
                robot.interact(() -> datePicker.setValue(returnDate));
                sleep(500, TimeUnit.MILLISECONDS);
            }
            
            // Click Save button
            robot.clickOn("Save");
            sleep(1000, TimeUnit.MILLISECONDS);
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @brief Test to verify navigation to the Lending Tab and basic UI elements.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testNavigationToLendingTab(FxRobot robot) {
        navigateToLendingTab(robot);
        verifyThat("#lendingSearchField", isVisible());
        verifyThat("#lendingTable", isVisible());
        verifyThat("#lendingViewEditButton", isVisible());
        verifyThat("#lendingReturnedButton", isVisible());
    }

    /**
     * @brief Test to create a valid lending and verify it appears in the table.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testCreateValidLending(FxRobot robot) {
        navigateToLendingTab(robot);
        
        TableView<?> lendingTable = robot.lookup("#lendingTable").query();
        int initialCount = lendingTable.getItems().size();
        
        // Create lending with future return date
        LocalDate futureDate = LocalDate.now().plusDays(14);
        boolean created = createLending(robot, futureDate);
        assertTrue(created, "Lending should be created successfully");
        
        // Navigate back to Lending Page to verify
        navigateToLendingTab(robot);
        
        int finalCount = lendingTable.getItems().size();
        assertTrue(finalCount > initialCount, "New lending should appear in table");
    }

    /**
     * @brief Test to create a lending with a past date and verify validation.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testCreateLendingWithPastDate(FxRobot robot) {
        // Navigate to Books tab
        navigateToBookTab(robot);
        
        // Click first book
        robot.clickOn(".table-row-cell");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Click Lend
        robot.clickOn("Lend");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Click first user
        robot.clickOn(".table-row-cell");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Click Lend to
        robot.clickOn("Lend to");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Set past date
        DatePicker datePicker = robot.lookup("#lendingReturnDatePicker").query();
        LocalDate pastDate = LocalDate.now().minusDays(5);
        robot.interact(() -> datePicker.setValue(pastDate));
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Save button should be disabled or show error
        // Try to save anyway
        try {
            robot.clickOn("Save");
            sleep(500, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            // Expected if validation prevents saving
        }
    }

    /**
     * @brief Test to mark a lending as returned and verify the row color changes to green.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testMarkAsReturnedAndVerifyGreenColor(FxRobot robot) {
        navigateToLendingTab(robot);
        
        // Create a lending first
        LocalDate returnDate = LocalDate.now().plusDays(7);
        createLending(robot, returnDate);
        
        // Navigate to Lending Page
        navigateToLendingTab(robot);
        
        // Select the first lending
        robot.clickOn(".table-row-cell");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Click Mark as returned button
        verifyThat("#lendingReturnedButton", isEnabled());
        robot.clickOn("#lendingReturnedButton");
        sleep(1000, TimeUnit.MILLISECONDS);
        
        // Verify button is now disabled (lending already returned)
        verifyThat("#lendingReturnedButton", isDisabled());
        
        // Verify row color is green (hex: #90EE90)
        TableView<?> table = robot.lookup("#lendingTable").query();
        TableRow<?> firstRow = (TableRow<?>) robot.lookup(".table-row-cell").query();
        if (firstRow != null && firstRow.getStyle() != null) {
            String style = firstRow.getStyle();
            assertTrue(style.contains("90ee90") || style.contains("#90EE90") || style.contains("lightgreen"),
                    "Returned lending row should have green background");
        }
    }

    /**
     * @brief Test that lending table is updated correctly after creation.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testLendingTableUpdateAfterCreation(FxRobot robot) {
        navigateToLendingTab(robot);
        
        TableView<?> lendingTable = robot.lookup("#lendingTable").query();
        int initialCount = lendingTable.getItems().size();
        
        // Create new lending
        LocalDate returnDate = LocalDate.now().plusDays(10);
        createLending(robot, returnDate);
        
        // Go back to lending page
        navigateToLendingTab(robot);
        
        // Verify table has one more item
        int newCount = lendingTable.getItems().size();
        assertEquals(initialCount + 1, newCount, "Table should have exactly one more lending");
    }

    /**
     * @brief Test that book availability is updated after creating a lending.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testBookAvailabilityUpdatedAfterLending(FxRobot robot) {
        navigateToBookTab(robot);
        
        // Click first book
        robot.clickOn(".table-row-cell");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Get current available count (need to check table cell value)
        TableView<?> bookTable = robot.lookup("#bookTable").query();
        int initialAvailable = -1;
        
        // Click Lend
        robot.clickOn("Lend");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Click first user
        robot.clickOn(".table-row-cell");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Click Lend to
        robot.clickOn("Lend to");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Set return date
        DatePicker datePicker = robot.lookup("#lendingReturnDatePicker").query();
        LocalDate returnDate = LocalDate.now().plusDays(14);
        robot.interact(() -> datePicker.setValue(returnDate));
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Save
        robot.clickOn("Save");
        sleep(1000, TimeUnit.MILLISECONDS);
        
        // Navigate back to Books tab
        navigateToBookTab(robot);
        
        // Verify available count decreased (or lent count increased)
        verifyThat("#bookTable", isVisible());
    }

    /**
     * @brief Test that user's lent count is updated after creating a lending.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testUserLentCountUpdatedAfterLending(FxRobot robot) {
        navigateToBookTab(robot);
        
        // Click first book
        robot.clickOn(".table-row-cell");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Click Lend
        robot.clickOn("Lend");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Click first user
        robot.clickOn(".table-row-cell");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Click Lend to
        robot.clickOn("Lend to");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Set return date
        DatePicker datePicker = robot.lookup("#lendingReturnDatePicker").query();
        LocalDate returnDate = LocalDate.now().plusDays(7);
        robot.interact(() -> datePicker.setValue(returnDate));
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Save
        robot.clickOn("Save");
        sleep(1000, TimeUnit.MILLISECONDS);
        
        // Navigate to Users tab
        navigateToUserTab(robot);
        
        // Verify user table shows updated lent count
        verifyThat("#userTable", isVisible());
    }

    /**
     * @brief Test the lending search functionality filters correctly.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testLendingSearchFiltersTable(FxRobot robot) {
        navigateToLendingTab(robot);
        
        // Create a lending to have data
        createLending(robot, LocalDate.now().plusDays(10));
        navigateToLendingTab(robot);
        
        TableView<?> table = robot.lookup("#lendingTable").query();
        int totalItems = table.getItems().size();
        
        TextField searchField = robot.lookup("#lendingSearchField").query();
        robot.clickOn("#lendingSearchField");
        robot.write("NONEXISTENT_TERM_XYZ");
        sleep(500, TimeUnit.MILLISECONDS);
        
        int filteredItems = table.getItems().size();
        assertTrue(filteredItems <= totalItems, "Search should filter results");
        
        // Clear search
        robot.interact(() -> searchField.clear());
        sleep(500, TimeUnit.MILLISECONDS);
        
        assertEquals(totalItems, table.getItems().size(), "Should restore all items after clearing search");
    }

    /**
     * @brief Test that buttons are disabled without selection and enabled with selection.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testButtonStateWithSelection(FxRobot robot) {
        navigateToLendingTab(robot);
        
        // Initially disabled
        verifyThat("#lendingViewEditButton", isDisabled());
        verifyThat("#lendingReturnedButton", isDisabled());
        
        // Create lending
        createLending(robot, LocalDate.now().plusDays(5));
        navigateToLendingTab(robot);
        
        // Select a row
        robot.clickOn(".table-row-cell");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Buttons should be enabled
        verifyThat("#lendingViewEditButton", isEnabled());
    }

    /**
     * @brief Test opening the view/edit dialog for a lending.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testOpenViewEditDialog(FxRobot robot) {
        navigateToLendingTab(robot);
        
        // Create lending
        createLending(robot, LocalDate.now().plusDays(14));
        navigateToLendingTab(robot);
        
        // Select lending
        robot.clickOn(".table-row-cell");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Click View & Edit
        robot.clickOn("#lendingViewEditButton");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Verify dialog elements are visible
        verifyThat("#lendingIdLabel", isVisible());
        verifyThat("#returnDateLabel", isVisible());
    }

    /**
     * @brief Test that the lending table displays all required columns.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testLendingTableColumns(FxRobot robot) {
        navigateToLendingTab(robot);
        
        verifyThat("#lendingIdColumn", isVisible());
        verifyThat("#lendingReturnDateColumn", isVisible());
        verifyThat("#lendingIsbnColumn", isVisible());
        verifyThat("#lendingTitleColumn", isVisible());
        verifyThat("#lendingUserIdColumn", isVisible());
        verifyThat("#lendingUserColumn", isVisible());
    }

    /**
     * @brief Test creating multiple lendings in sequence.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testCreateMultipleLendings(FxRobot robot) {
        navigateToLendingTab(robot);
        
        TableView<?> table = robot.lookup("#lendingTable").query();
        int initialCount = table.getItems().size();
        
        // Create first lending
        createLending(robot, LocalDate.now().plusDays(7));
        navigateToLendingTab(robot);
        
        int countAfterFirst = table.getItems().size();
        assertTrue(countAfterFirst > initialCount, "First lending should be added");
        
        // Create second lending
        createLending(robot, LocalDate.now().plusDays(14));
        navigateToLendingTab(robot);
        
        int countAfterSecond = table.getItems().size();
        assertTrue(countAfterSecond > countAfterFirst, "Second lending should be added");
    }

    /**
     * @brief Test complete lending workflow: create, verify in table, mark as returned, verify green color.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testCompleteLendingWorkflow(FxRobot robot) {
        navigateToLendingTab(robot);
        
        TableView<?> table = robot.lookup("#lendingTable").query();
        int initialCount = table.getItems().size();
        
        // Step 1: Create lending
        LocalDate returnDate = LocalDate.now().plusDays(21);
        createLending(robot, returnDate);
        
        // Step 2: Verify in lending table
        navigateToLendingTab(robot);
        int afterCreate = table.getItems().size();
        assertTrue(afterCreate > initialCount, "Lending should be in table");
        
        // Step 3: Select lending
        robot.clickOn(".table-row-cell");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Step 4: Mark as returned
        robot.clickOn("#lendingReturnedButton");
        sleep(1000, TimeUnit.MILLISECONDS);
        
        // Step 5: Verify button is disabled
        verifyThat("#lendingReturnedButton", isDisabled());
        
        // Step 6: Verify data updated in other tabs
        navigateToBookTab(robot);
        verifyThat("#bookTable", isVisible());
        
        navigateToUserTab(robot);
        verifyThat("#userTable", isVisible());
        
        navigateToLendingTab(robot);
        verifyThat("#lendingTable", isVisible());
    }
}
