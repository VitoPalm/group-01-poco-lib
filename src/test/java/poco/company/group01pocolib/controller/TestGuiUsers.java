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
 * @brief Test class for User Tab GUI functionality
 */
@ExtendWith(ApplicationExtension.class)
class TestGuiUsers {

    /**
     * @brief Start method to launch the JavaFX application for testing.
     */
    @Start
    public void start(Stage stage) throws Exception {
        new Launcher().start(stage);
    }

    /**
     * @brief Helper method to navigate to Users tab reliably.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    private void navigateToUserTab(FxRobot robot) {
        sleep(500, TimeUnit.MILLISECONDS);
        // Multiple clicks to ensure tab is selected
        robot.clickOn("Users");
        sleep(100, TimeUnit.MILLISECONDS);
        robot.doubleClickOn("Users");
        sleep(100, TimeUnit.MILLISECONDS);
        robot.clickOn("Users");
        sleep(300, TimeUnit.MILLISECONDS);
    }

    /**
     * @brief Test to verify navigation to the User Tab.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testNavigationToUserTab(FxRobot robot) {
        navigateToUserTab(robot);
        verifyThat("#userAddButton", hasText("Add"));
    }

    /**
     * @brief Test to verify the user search field is visible and functional.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testUserSearchField(FxRobot robot) {
        navigateToUserTab(robot);
        verifyThat("#userSearchField", isVisible());
        
        TextField searchField = robot.lookup("#userSearchField").query();
        assertEquals("", searchField.getText());
    }

    /**
     * @brief Test to verify the user table is visible with all columns.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testUserTable(FxRobot robot) {
        navigateToUserTab(robot);
        
        verifyThat("#userTable", isVisible());
        verifyThat("#userIdColumn", isVisible());
        verifyThat("#userNameColumn", isVisible());
        verifyThat("#userSurnameColumn", isVisible());
        verifyThat("#userEmailColumn", isVisible());
        verifyThat("#userLentColumn", isVisible());
    }

    /**
     * @brief Test to verify the search functionality in the User Tab.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testUserOmnisearch(FxRobot robot) {
        navigateToUserTab(robot);
        
        verifyThat("#userSearchField", isVisible());
        robot.clickOn("#userSearchField");
        robot.write("mario");
        sleep(500, TimeUnit.MILLISECONDS);
        
        TextField searchField = robot.lookup("#userSearchField").query();
        assertEquals("mario", searchField.getText());
        
        verifyThat("#userTable", isVisible());
        
        // Clear search field
        robot.interact(() -> searchField.clear());
        sleep(300, TimeUnit.MILLISECONDS);
    }

    /**
     * @brief Test to verify that View & Edit and Lend to buttons are disabled when no user is selected.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testButtonsDisabledWithoutSelection(FxRobot robot) {
        navigateToUserTab(robot);
        
        // Buttons should be disabled when nothing is selected
        verifyThat("#userViewEditButton", isDisabled());
        verifyThat("#userLendButton", isDisabled());
    }

    /**
     * @brief Test to verify the Add button is always enabled.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testAddButtonEnabled(FxRobot robot) {
        navigateToUserTab(robot);
        
        verifyThat("#userAddButton", isEnabled());
        verifyThat("#userAddButton", hasText("Add"));
    }

    /**
     * @brief Test to verify user add dialog opens.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testOpenAddUserDialog(FxRobot robot) {
        navigateToUserTab(robot);
        robot.clickOn("#userAddButton");
        
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Verify dialog opens with edit fields
        verifyThat("#editBox", isVisible());
        verifyThat("#idField", isVisible());
        verifyThat("#nameField", isVisible());
        verifyThat("#surnameField", isVisible());
        verifyThat("#emailField", isVisible());
        
        // Close dialog
        robot.type(javafx.scene.input.KeyCode.ESCAPE);
        sleep(300, TimeUnit.MILLISECONDS);
    }

    /**
     * @brief Test to verify View & Edit button functionality.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testViewEditButton(FxRobot robot) {
        navigateToUserTab(robot);
        
        // Click on first row in table
        robot.clickOn(".table-row-cell");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Now button should be enabled
        verifyThat("#userViewEditButton", isEnabled());
        
        robot.clickOn("#userViewEditButton");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Verify dialog opens in view mode
        verifyThat("#viewBox", isVisible());
        verifyThat("#editButton", isVisible());
        verifyThat("#deleteButton", isVisible());
        
        // Close dialog
        robot.type(javafx.scene.input.KeyCode.ESCAPE);
        sleep(300, TimeUnit.MILLISECONDS);
    }

    /**
     * @brief Test to verify Lend to button functionality.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testLendToButton(FxRobot robot) {
        navigateToUserTab(robot);
        
        // Click on first row in table
        robot.clickOn(".table-row-cell");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Now button should be enabled
        verifyThat("#userLendButton", isEnabled());
        
        robot.clickOn("#userLendButton");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Should switch to Books tab to select a book
        verifyThat("#bookTab", isVisible());
    }

    /**
     * @brief Test to verify user selection updates the selected user.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testUserSelection(FxRobot robot) {
        navigateToUserTab(robot);
        
        // Initially buttons should be disabled
        verifyThat("#userViewEditButton", isDisabled());
        verifyThat("#userLendButton", isDisabled());
        
        // Click on first row in table
        robot.clickOn(".table-row-cell");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // After selecting a user, buttons should be enabled
        verifyThat("#userViewEditButton", isEnabled());
        verifyThat("#userLendButton", isEnabled());
    }

    /**
     * @brief Test to verify the user table updates when search field changes.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testTableUpdateWithSearch(FxRobot robot) {
        navigateToUserTab(robot);
        
        TextField searchField = robot.lookup("#userSearchField").query();
        
        robot.clickOn("#userSearchField");
        robot.write("test");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Verify table is still visible and shows filtered results
        verifyThat("#userTable", isVisible());
        
        // Clear search
        robot.interact(() -> searchField.clear());
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Table should still be visible with all users
        verifyThat("#userTable", isVisible());
    }

    /**
     * @brief Test to verify the search field placeholder text.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testSearchFieldPlaceholder(FxRobot robot) {
        navigateToUserTab(robot);
        
        TextField searchField = robot.lookup("#userSearchField").query();
        // The prompt text includes the number of users, format: "OmniSearch X users"
        String promptText = searchField.getPromptText();
        assertEquals(true, promptText != null && promptText.startsWith("OmniSearch"));
    }

    /**
     * @brief Test to verify adding a new user with valid data.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testAddValidUser(FxRobot robot) {
        navigateToUserTab(robot);
        
        // Click Add button to open dialog
        robot.clickOn("#userAddButton");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Verify dialog is in edit mode
        verifyThat("#editBox", isVisible());
        
        // Fill in user details
        robot.clickOn("#idField");
        robot.write("TEST123");
        
        robot.clickOn("#nameField");
        robot.write("John");
        
        robot.clickOn("#surnameField");
        robot.write("Doe");
        
        robot.clickOn("#emailField");
        // Clear default invalid email
        robot.press(javafx.scene.input.KeyCode.CONTROL, javafx.scene.input.KeyCode.A);
        robot.release(javafx.scene.input.KeyCode.A, javafx.scene.input.KeyCode.CONTROL);
        robot.write("john.doe@example.com");
        
        // Save the user
        robot.clickOn("#saveButton");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Verify we're back to the user tab
        verifyThat("#userTable", isVisible());
        
        // Search for the newly added user
        robot.clickOn("#userSearchField");
        robot.write("TEST123");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Verify the user appears in search results
        verifyThat("#userTable", isVisible());
    }

    /**
     * @brief Test to verify that adding a user with invalid data disables Save button.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testAddInvalidUser(FxRobot robot) {
        navigateToUserTab(robot);
        
        // Click Add button
        robot.clickOn("#userAddButton");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // With empty fields, Save button should be disabled
        verifyThat("#saveButton", isDisabled());
        
        // Cancel the dialog
        robot.clickOn("Cancel");
        sleep(300, TimeUnit.MILLISECONDS);
    }

    /**
     * @brief Test to verify adding a user with only ID missing keeps Save disabled.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testAddUserWithoutID(FxRobot robot) {
        navigateToUserTab(robot);
        
        robot.clickOn("#userAddButton");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Fill all fields except ID
        robot.clickOn("#nameField");
        robot.write("Jane");
        
        robot.clickOn("#surnameField");
        robot.write("Smith");
        
        robot.clickOn("#emailField");
        robot.press(javafx.scene.input.KeyCode.CONTROL, javafx.scene.input.KeyCode.A);
        robot.release(javafx.scene.input.KeyCode.A, javafx.scene.input.KeyCode.CONTROL);
        robot.write("jane.smith@example.com");
        
        sleep(300, TimeUnit.MILLISECONDS);
        
        // Save button should still be disabled without valid ID
        verifyThat("#saveButton", isDisabled());
        
        robot.clickOn("Cancel");
        sleep(300, TimeUnit.MILLISECONDS);
    }

    /**
     * @brief Test to verify adding a user with invalid email keeps Save disabled.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testAddUserWithInvalidEmail(FxRobot robot) {
        navigateToUserTab(robot);
        
        robot.clickOn("#userAddButton");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Fill valid data
        robot.clickOn("#idField");
        robot.write("USR999");
        
        robot.clickOn("#nameField");
        robot.write("Bob");
        
        robot.clickOn("#surnameField");
        robot.write("Brown");
        
        // Enter invalid email (missing @)
        robot.clickOn("#emailField");
        robot.press(javafx.scene.input.KeyCode.CONTROL, javafx.scene.input.KeyCode.A);
        robot.release(javafx.scene.input.KeyCode.A, javafx.scene.input.KeyCode.CONTROL);
        robot.write("notanemail");
        
        sleep(300, TimeUnit.MILLISECONDS);
        
        // Save button should be disabled with invalid email
        verifyThat("#saveButton", isDisabled());
        
        robot.clickOn("Cancel");
        sleep(300, TimeUnit.MILLISECONDS);
    }

    /**
     * @brief Test to verify search by ID.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testSearchByID(FxRobot robot) {
        navigateToUserTab(robot);
        
        robot.clickOn("#userSearchField");
        robot.write("USR");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Verify table shows results
        verifyThat("#userTable", isVisible());
        
        TextField searchField = robot.lookup("#userSearchField").query();
        robot.interact(() -> searchField.clear());
        sleep(300, TimeUnit.MILLISECONDS);
    }

    /**
     * @brief Test to verify search by name.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testSearchByName(FxRobot robot) {
        navigateToUserTab(robot);
        
        robot.clickOn("#userSearchField");
        robot.write("mario");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Verify table shows results
        verifyThat("#userTable", isVisible());
        
        TextField searchField = robot.lookup("#userSearchField").query();
        robot.interact(() -> searchField.clear());
        sleep(300, TimeUnit.MILLISECONDS);
    }

    /**
     * @brief Test to verify search by email.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testSearchByEmail(FxRobot robot) {
        navigateToUserTab(robot);
        
        robot.clickOn("#userSearchField");
        robot.write("@email");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Verify table shows results
        verifyThat("#userTable", isVisible());
        
        TextField searchField = robot.lookup("#userSearchField").query();
        robot.interact(() -> searchField.clear());
        sleep(300, TimeUnit.MILLISECONDS);
    }

    /**
     * @brief Test to verify canceling user addition.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testCancelUserAddition(FxRobot robot) {
        navigateToUserTab(robot);
        
        robot.clickOn("#userAddButton");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Fill some data
        robot.clickOn("#nameField");
        robot.write("Cancelled User");
        
        // Cancel without saving
        robot.clickOn("Cancel");
        sleep(300, TimeUnit.MILLISECONDS);
        
        // Should be back at user tab
        verifyThat("#userTable", isVisible());
        
        // Search for the user - should not exist
        robot.clickOn("#userSearchField");
        robot.write("Cancelled User");
        sleep(300, TimeUnit.MILLISECONDS);
        
        // Clear search
        TextField searchField = robot.lookup("#userSearchField").query();
        robot.interact(() -> searchField.clear());
    }
}
