/**
 * @file UserSetTest.java
 * @brief Unit tests for the UserSet class.
 * @author Daniele Pepe
 */
package poco.company.group01pocolib.mvc.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;

import poco.company.group01pocolib.db.DB;

/**
 * @class UserSetTest
 * @brief Contains unit tests to verify the correctness of the UserSet class methods.
 */
public class UserSetTest {


    private UserSet userSet;
    private User user;
    private DB userDB;

    @BeforeEach
    public void setUp() {
        // Setup code if needed before each test
        userSet = new UserSet();

        userDB = new DB("testUsersDB");
        userSet.setUserDB(userDB);
        userSet.setSerializationPath("testUserSet.ser");
        user = new User("12345","Max", "Verstappen", "v@l.id");
        userSet.addOrEditUser(user);
    }

    @AfterEach
    public void tearDown() {
        // Cleanup: delete test serialization files and DB files
        deleteFileIfExists("testUserSet.ser");
        deleteFileIfExists("testUsersDB");

    }

    private void deleteFileIfExists(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }


    /**
     * @brief Tests that a UserSet object is created correctly.
     */
    @Test
    public void testUserSetCreation() {
        assertNotNull(userSet);
    }

    /**
     * @brief Tests the addUser method to ensure a user is added correctly.
     */
    @Test
    public void testAddUser() {
        User newUser = new User("67890", "Lewis", "Hamilton", "l@w.is");
        userSet.addOrEditUser(newUser);
        assertTrue(userSet.getUserSet().contains(newUser));
    }

    /**
     * @brief Tests the removeUser method to ensure a user is removed correctly.
     */
    @Test
    public void testRemoveUser() {
        userSet.removeUser(user.getId());
        assertFalse(userSet.getUserSet().contains(user));
    }


    //TODO: add more tests for other methods
}
