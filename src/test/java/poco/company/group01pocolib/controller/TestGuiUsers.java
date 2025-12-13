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
     * @brief Test to verify navigation to the User Tab.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testNavigazioneUserTab(FxRobot robot) {
        robot.clickOn("Users");
        verifyThat("#userTab", isVisible());
        verifyThat("#userAddButton", hasText("Add"));
        verifyThat("#userSearchField", isVisible());
    }

    /**
     * @brief Test to verify the user search field is visible and functional.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testCampoRicercaUtente(FxRobot robot) {
        robot.clickOn("Users");
        verifyThat("#userSearchField", isVisible());
        
        TextField searchField = robot.lookup("#userSearchField").query();
        assertEquals("", searchField.getText());
    }

    /**
     * @brief Test to verify the user table is visible with all columns.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testTabellaUtenti(FxRobot robot) {
        robot.clickOn("Users");
        
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
    void testOmnisearchUtenti(FxRobot robot) {
        robot.clickOn("Users");
        
        verifyThat("#userSearchField", isVisible());
        robot.clickOn("#userSearchField");
        
        robot.type(javafx.scene.input.KeyCode.M);
        sleep(100, TimeUnit.MILLISECONDS);
        robot.type(javafx.scene.input.KeyCode.A);
        sleep(100, TimeUnit.MILLISECONDS);
        robot.type(javafx.scene.input.KeyCode.R);
        sleep(100, TimeUnit.MILLISECONDS);
        robot.type(javafx.scene.input.KeyCode.I);
        sleep(100, TimeUnit.MILLISECONDS);
        robot.type(javafx.scene.input.KeyCode.O);
        sleep(100, TimeUnit.MILLISECONDS);
        
        TextField searchField = robot.lookup("#userSearchField").query();
        assertEquals("MARIO", searchField.getText());
        
        verifyThat("#userTable", isVisible());
        sleep(500, TimeUnit.MILLISECONDS);
        
        // Clear search field
        robot.clickOn("#userSearchField");
        robot.interact(() -> searchField.clear());
        sleep(500, TimeUnit.MILLISECONDS);
    }

    /**
     * @brief Test to verify that View & Edit and Lend to buttons are disabled when no user is selected.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testBottoniDisabilitatiSenzaSelezione(FxRobot robot) {
        robot.clickOn("Users");
        
        // Buttons should be disabled when nothing is selected
        verifyThat("#userViewEditButton", isDisabled());
        verifyThat("#userLendButton", isDisabled());
    }

    /**
     * @brief Test to verify the Add button is always enabled.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testBottoneAggiungiAbilitato(FxRobot robot) {
        robot.clickOn("Users");
        
        verifyThat("#userAddButton", isEnabled());
        verifyThat("#userAddButton", hasText("Add"));
    }

    /**
     * @brief Test to verify user add dialog opens.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testAperturaDialogAggiungiUtente(FxRobot robot) {
        robot.clickOn("Users");
        robot.clickOn("#userAddButton");
        
        sleep(500, TimeUnit.MILLISECONDS);
        
        // TODO: Implement controller feature - handleUserAdd dialog
        // Verify dialog opens with edit fields
        // verifyThat("#editBox", isVisible());
        // verifyThat("#idField", isVisible());
        // verifyThat("#nameField", isVisible());
        // verifyThat("#surnameField", isVisible());
        // verifyThat("#emailField", isVisible());
    }

    /**
     * @brief Test to verify View & Edit button functionality.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testBottoneVisualizzaModifica(FxRobot robot) {
        robot.clickOn("Users");
        
        // TODO: Implement controller feature - handleUserViewEdit
        // Select a user first, then test view/edit button
        // robot.clickOn user in table
        // robot.clickOn("#userViewEditButton");
        // verifyThat dialog opens
    }

    /**
     * @brief Test to verify Lend to button functionality.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testBottonePrestito(FxRobot robot) {
        robot.clickOn("Users");
        
        // TODO: Implement controller feature - handleUserLend
        // Select a user first, then test lend button
        // robot.clickOn user in table
        // robot.clickOn("#userLendButton");
        // verifyThat lending dialog or book tab opens
    }

    /**
     * @brief Test to verify user selection updates the selected user.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testSelezioneUtente(FxRobot robot) {
        robot.clickOn("Users");
        
        // TODO: Implement test with actual data
        // After selecting a user, buttons should be enabled
        // robot.clickOn user row in table
        // verifyThat("#userViewEditButton", isEnabled());
        // verifyThat("#userLendButton", isEnabled());
    }

    /**
     * @brief Test to verify the user table updates when search field changes.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testAggiornamentoTabellaConRicerca(FxRobot robot) {
        robot.clickOn("Users");
        
        TextField searchField = robot.lookup("#userSearchField").query();
        
        robot.clickOn("#userSearchField");
        robot.write("test");
        sleep(500, TimeUnit.MILLISECONDS);
        
        // TODO: Verify table content updates based on search
        // Verify filtered results in table
        
        robot.interact(() -> searchField.clear());
        sleep(500, TimeUnit.MILLISECONDS);
    }
}
