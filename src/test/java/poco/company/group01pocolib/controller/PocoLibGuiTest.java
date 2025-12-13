package poco.company.group01pocolib.controller;

import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import poco.company.group01pocolib.Launcher;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import static org.testfx.matcher.control.LabeledMatchers.hasText;
import static org.testfx.util.WaitForAsyncUtils.sleep;

import java.util.concurrent.TimeUnit; 

@ExtendWith(ApplicationExtension.class)
class PocoLibGuiTest {

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
       
        robot.clickOn("Books"); //click on a button with text "Books"
        verifyThat("#bookTab", isVisible());    //click on a button with id "bookTab"
        verifyThat("#bookAddButton", hasText("Add"));
    }

    /**
     * @brief Test per verificare la funzionalità di ricerca omnisearch nella tab dei libri.
     * @param robot The FxRobot instance for simulating user interactions.
     */
    @Test
    void testOmnisearchLibri(FxRobot robot) {
        //TODO: Fix test after implementing data loading in app
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
        
        assertEquals("JAVA", searchField.getText());
        
        // Verifica che la tabella sia visibile
        verifyThat("#bookTable", isVisible());
        sleep(1, TimeUnit.SECONDS);
        
        // Cancella il campo di ricerca
        robot.clickOn("#bookSearchField");
        sleep(30, TimeUnit.MILLISECONDS);
        robot.interact(() -> searchField.clear());
        sleep(1, TimeUnit.SECONDS);
        
    }
}