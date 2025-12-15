package poco.company.group01pocolib.controller;

import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        
        sleep(2, TimeUnit.SECONDS);

        verifyThat("#userTab", isVisible());
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
        robot.interact(() -> {
            TextField searchField = robot.lookup("#userSearchField").query();
            searchField.setText("mario");
            assertEquals("mario", searchField.getText());
        });
        sleep(500, TimeUnit.MILLISECONDS);
        
        verifyThat("#userTable", isVisible());
        
        // Clear search field
        robot.interact(() -> {
            TextField searchField = robot.lookup("#userSearchField").query();
            searchField.clear();
        });
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
        
        sleep(1000, TimeUnit.MILLISECONDS);
        
        // Verify dialog opens with edit fields
        verifyThat("#editBox", isVisible());
        verifyThat("#idField", isVisible());
        verifyThat("#nameField", isVisible());
        verifyThat("#surnameField", isVisible());
        verifyThat("#emailField", isVisible());
        
        // Close dialog
        robot.type(javafx.scene.input.KeyCode.ESCAPE);
        sleep(1000, TimeUnit.MILLISECONDS);
    }

    /**
     * @brief Test to verify View & Edit button functionality.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testViewEditButton(FxRobot robot) {
        navigateToUserTab(robot);
        
        // Select first row in table programmatically to ensure selection
        robot.interact(() -> {
            javafx.scene.control.TableView<?> table = robot.lookup("#userTable").queryTableView();
            if (table != null && !table.getItems().isEmpty()) {
                table.getSelectionModel().select(0);
            }
        });
        sleep(1000, TimeUnit.MILLISECONDS);
        
        // Now button should be enabled
        verifyThat("#userViewEditButton", isEnabled());
        
        robot.clickOn("#userViewEditButton");
        sleep(1000, TimeUnit.MILLISECONDS);
        
        // Verify dialog opens in view mode
        verifyThat("#viewBox", isVisible());
        verifyThat("#editButton", isVisible());
        verifyThat("#deleteButton", isVisible());
        
        // Close dialog
        robot.type(javafx.scene.input.KeyCode.ESCAPE);
        sleep(1000, TimeUnit.MILLISECONDS);
    }

    /**
     * @brief Test editing an existing user: open view, edit fields, save and verify change
     */
    @Test
    void testEditUser(FxRobot robot) {
        navigateToUserTab(robot);

        // Select first row to edit
        robot.interact(() -> {
            javafx.scene.control.TableView<?> table = robot.lookup("#userTable").queryTableView();
            if (table == null || table.getItems().isEmpty()) {
                throw new AssertionError("No users available to edit");
            }
            table.getSelectionModel().select(0);
        });
        sleep(300, TimeUnit.MILLISECONDS);

        // Open view dialog
        verifyThat("#userViewEditButton", isEnabled());
        robot.clickOn("#userViewEditButton");
        sleep(300, TimeUnit.MILLISECONDS);

        // Switch to edit mode
        robot.clickOn("#editButton");
        sleep(200, TimeUnit.MILLISECONDS);

        // Modify fields on FX thread
        robot.interact(() -> {
            TextField nameField = robot.lookup("#nameField").query();
            TextField surnameField = robot.lookup("#surnameField").query();
            nameField.setText(nameField.getText() + "_EDIT");
            surnameField.setText(surnameField.getText() + "_EDIT");
        });
        sleep(200, TimeUnit.MILLISECONDS);

        // Save the changes
        robot.interact(() -> {
            javafx.scene.control.Button save = robot.lookup("#saveButton").queryButton();
            if (save.isDisable()) throw new AssertionError("Save button disabled while editing");
            save.fire();
        });
        sleep(600, TimeUnit.MILLISECONDS);

        // Verify the table contains the edited name
        robot.interact(() -> {
            javafx.scene.control.TableView<?> table = robot.lookup("#userTable").queryTableView();
            assertTrue(table.getItems().stream().anyMatch(item -> {
                try {
                    java.lang.reflect.Method m = item.getClass().getMethod("getName");
                    Object name = m.invoke(item);
                    return name != null && name.toString().endsWith("_EDIT");
                } catch (Exception e) {
                    return false;
                }
            }), "Edited user not found in table items");
        });
    }

    /**
     * @brief Test creating then deleting a user using the UI
     */
    @Test
    void testDeleteUser(FxRobot robot) {
        navigateToUserTab(robot);

        // Create a new user to delete
        robot.clickOn("#userAddButton");
        sleep(300, TimeUnit.MILLISECONDS);

        robot.interact(() -> {
            TextField idField = robot.lookup("#idField").query();
            TextField nameField = robot.lookup("#nameField").query();
            TextField surnameField = robot.lookup("#surnameField").query();
            TextField emailField = robot.lookup("#emailField").query();

            idField.setText("DELUSR1");
            nameField.setText("Del");
            surnameField.setText("User");
            emailField.setText("del.user@example.com");
        });

        sleep(300, TimeUnit.MILLISECONDS);

        robot.interact(() -> {
            javafx.scene.control.Button save = robot.lookup("#saveButton").queryButton();
            if (save.isDisable()) throw new AssertionError("Save disabled when creating user for deletion test");
            save.fire();
        });
        sleep(700, TimeUnit.MILLISECONDS);

        // Verify user exists
        robot.interact(() -> {
            javafx.scene.control.TableView<?> table = robot.lookup("#userTable").queryTableView();
            assertTrue(table.getItems().stream().anyMatch(item -> {
                try { return "DELUSR1".equals(item.getClass().getMethod("getId").invoke(item)); }
                catch (Exception e) { return false; }
            }), "Created user DELUSR1 not found");
        });

        // Select the created user
        robot.interact(() -> {
            javafx.scene.control.TableView<?> table = robot.lookup("#userTable").queryTableView();
            table.getItems().stream().filter(item -> {
                try { return "DELUSR1".equals(item.getClass().getMethod("getId").invoke(item)); }
                catch (Exception e) { return false; }
            }).findFirst().ifPresent(item -> {
                int idx = table.getItems().indexOf(item);
                if (idx >= 0) table.getSelectionModel().select(idx);
            });
        });
        sleep(300, TimeUnit.MILLISECONDS);

        // Open view dialog and delete
        verifyThat("#userViewEditButton", isEnabled());
        robot.clickOn("#userViewEditButton");
        sleep(300, TimeUnit.MILLISECONDS);

        robot.clickOn("#deleteButton");
        sleep(200, TimeUnit.MILLISECONDS);

        // Confirm deletion (Alert button "OK")
        robot.clickOn("OK");
        sleep(700, TimeUnit.MILLISECONDS);

        // Verify user removed
        robot.interact(() -> {
            javafx.scene.control.TableView<?> table = robot.lookup("#userTable").queryTableView();
            assertTrue(table.getItems().stream().noneMatch(item -> {
                try { return "DELUSR1".equals(item.getClass().getMethod("getId").invoke(item)); }
                catch (Exception e) { return false; }
            }), "Deleted user still present in table");
        });
    }

    /**
     * @brief Test to verify Lend to button functionality.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testLendToButton(FxRobot robot) {
        navigateToUserTab(robot);
        
        // Select first row in table programmatically to ensure selection
        robot.interact(() -> {
            javafx.scene.control.TableView<?> table = robot.lookup("#userTable").queryTableView();
            if (table != null && !table.getItems().isEmpty()) {
                table.getSelectionModel().select(0);
            }
        });
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
        
        // Select first row in table programmatically to ensure selection
        robot.interact(() -> {
            javafx.scene.control.TableView<?> table = robot.lookup("#userTable").queryTableView();
            if (table != null && !table.getItems().isEmpty()) {
                table.getSelectionModel().select(0);
            }
        });
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
        
        
        robot.interact(() -> {
            TextField searchField = robot.lookup("#userSearchField").query();
            searchField.setText("test");
        });
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Verify table is still visible and shows filtered results
        verifyThat("#userTable", isVisible());
        
        // Clear search
        robot.interact(() -> {
            TextField searchField = robot.lookup("#userSearchField").query();
            searchField.clear();
        });
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
        
        robot.interact(() -> {
            TextField searchField = robot.lookup("#userSearchField").query();
            // The prompt text includes the number of users, format: "OmniSearch X users"
            String promptText = searchField.getPromptText();
            assertEquals(true, promptText != null && promptText.startsWith("OmniSearch"));
        });
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
        
        // Fill in user details via FX thread to ensure bindings update
        robot.interact(() -> {
            TextField idField = robot.lookup("#idField").query();
            TextField nameField = robot.lookup("#nameField").query();
            TextField surnameField = robot.lookup("#surnameField").query();
            TextField emailField = robot.lookup("#emailField").query();

            idField.setText("TEST123");
            nameField.setText("John");
            surnameField.setText("Doe");
            emailField.setText("john.doe@example.com");
        });

        // Allow bindings to react
        sleep(300, TimeUnit.MILLISECONDS);

        // Ensure Save is enabled then click it
        robot.interact(() -> {
            javafx.scene.control.Button save = robot.lookup("#saveButton").queryButton();
            if (save.isDisable()) {
                throw new AssertionError("Save button is disabled even after filling valid data: " + save.getTooltip());
            }
            save.fire();
        });
        // give time for dialog to close and data to refresh
        sleep(700, TimeUnit.MILLISECONDS);
        
        // Verify we're back to the user tab
        verifyThat("#userTable", isVisible());
        
        // Verify the user was added to the table data
        robot.interact(() -> {
            javafx.scene.control.TableView<?> table = robot.lookup("#userTable").queryTableView();
            assertTrue(table.getItems().stream().anyMatch(item -> {
                try {
                    java.lang.reflect.Method m = item.getClass().getMethod("getId");
                    Object id = m.invoke(item);
                    return "TEST123".equals(id);
                } catch (Exception e) {
                    return false;
                }
            }), "New user TEST123 not found in table items");
        });
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
        
        // Fill all fields except ID via FX thread to ensure bindings update
        robot.interact(() -> {
            TextField nameField = robot.lookup("#nameField").query();
            TextField surnameField = robot.lookup("#surnameField").query();
            TextField emailField = robot.lookup("#emailField").query();

            nameField.setText("Jane");
            surnameField.setText("Smith");
            emailField.setText("jane.smith@example.com");
        });
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
        
        // Fill valid data via FX thread and set invalid email
        robot.interact(() -> {
            TextField idField = robot.lookup("#idField").query();
            TextField nameField = robot.lookup("#nameField").query();
            TextField surnameField = robot.lookup("#surnameField").query();
            TextField emailField = robot.lookup("#emailField").query();

            idField.setText("USR999");
            nameField.setText("Bob");
            surnameField.setText("Brown");
            emailField.setText("notanemail");
        });
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
        
        robot.interact(() -> {
            TextField searchField = robot.lookup("#userSearchField").query();
            searchField.setText("USR");
        });
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Verify table shows results
        verifyThat("#userTable", isVisible());
        
        robot.interact(() -> {
            TextField searchField = robot.lookup("#userSearchField").query();
            searchField.clear();
        });
        sleep(300, TimeUnit.MILLISECONDS);
    }

    /**
     * @brief Test to verify search by name.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testSearchByName(FxRobot robot) {
        navigateToUserTab(robot);
        
        robot.interact(() -> {
            TextField searchField = robot.lookup("#userSearchField").query();
            searchField.setText("mario");
        });
        sleep(500, TimeUnit.MILLISECONDS);

        // Verify table shows results
        verifyThat("#userTable", isVisible());

        robot.interact(() -> {
            TextField searchField = robot.lookup("#userSearchField").query();
            searchField.clear();
        });
        sleep(300, TimeUnit.MILLISECONDS);
    }

    /**
     * @brief Test to verify search by email.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testSearchByEmail(FxRobot robot) {
        navigateToUserTab(robot);
        
        robot.interact(() -> {
            TextField searchField = robot.lookup("#userSearchField").query();
            searchField.setText("@email");
        });
        sleep(500, TimeUnit.MILLISECONDS);

        // Verify table shows results
        verifyThat("#userTable", isVisible());

        robot.interact(() -> {
            TextField searchField = robot.lookup("#userSearchField").query();
            searchField.clear();
        });
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
        
        // Fill some data via FX thread
        robot.interact(() -> {
            TextField nameField = robot.lookup("#nameField").query();
            nameField.setText("Cancelled User");
        });

        // Cancel without saving
        robot.clickOn("Cancel");
        sleep(300, TimeUnit.MILLISECONDS);
        
        // Should be back at user tab
        verifyThat("#userTable", isVisible());
        
        // Search for the user - should not exist
        robot.interact(() -> {
            TextField searchField = robot.lookup("#userSearchField").query();
            searchField.setText("Cancelled User");
        });
        sleep(300, TimeUnit.MILLISECONDS);

        // Clear search
        robot.interact(() -> {
            TextField searchField = robot.lookup("#userSearchField").query();
            searchField.clear();
        });
    }
}
