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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;

import poco.company.group01pocolib.db.DB;
import poco.company.group01pocolib.db.omnisearch.Index;

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
        deleteFileIfExists("newTestDB");
        deleteFileIfExists("anotherTestUserSet.ser");
        deleteFileIfExists("anotherTestUsersDB");
        deleteFileIfExists("testLoadHashMatch.ser");
        deleteFileIfExists("testLoadHashMismatch.ser");
        deleteFileIfExists("testHashMismatchDB");
        deleteFileIfExists("testCorrupted.ser");
        deleteFileIfExists("testCycle.ser");
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

    /**
     * @brief Tests the getter and setter methods of UserSet.
     */
    @Test
    public void testSetGet(){
        // Test getUserSet and setUserSet
        Set<User> newSet = new HashSet<>();
        User testUser = new User("99999", "Test", "User", "test@test.com");
        newSet.add(testUser);
        userSet.setUserSet(newSet);

        // Test getUserSet
        assertEquals(newSet, userSet.getUserSet());
        assertTrue(userSet.getUserSet().contains(testUser));

        // Test getDBPath and setDBPath
        String testDBPath = "test/path/to/db";
        userSet.setDBPath(testDBPath);
        assertEquals(testDBPath, userSet.getDBPath());

        // Test getSerializationPath and setSerializationPath
        String testSerPath = "test/path/to/serialization.ser";
        userSet.setSerializationPath(testSerPath);
        assertEquals(testSerPath, userSet.getSerializationPath());

        // Test getUserIndex and setUserIndex
        Index<User> newIndex = new Index<>();
        userSet.setUserIndex(newIndex);
        assertEquals(newIndex, userSet.getUserIndex());

        // Test getUserDB and setUserDB
        // Save the original DB to restore it later
        DB originalDB = userSet.getUserDB();
        DB newDB = new DB("newTestDB");
        userSet.setUserDB(newDB);
        assertEquals(newDB, userSet.getUserDB());
        
        // Restore the original DB for the hash test
        userSet.setUserDB(originalDB);

        // Test getLastKnownDBHash and updateLastKnownDBHash
        // The original userDB from setUp has data written by addOrEditUser
        userSet.updateLastKnownDBHash();
        String hash = userSet.getLastKnownDBHash();
        assertNotNull(hash, "Hash should not be null after updating from an existing DB");
        assertFalse(hash.isEmpty(), "Hash should not be empty after updating from an existing DB");
    }

    /**
     * @brief Tests the getListOfUsers method to ensure it returns the correct list.
     */
    @Test
    public void testGetListOfUsers() {
        User user2 = new User("67890", "Lewis", "Hamilton", "l@w.is");
        User user3 = new User("11111", "Charles", "Leclerc", "c@l.ec");
        
        userSet.addOrEditUser(user2);
        userSet.addOrEditUser(user3);
        
        List<User> userList = userSet.getListOfUsers();
        
        assertNotNull(userList);
        assertEquals(3, userList.size()); // 1 from setUp + 2 added
        assertTrue(userList.contains(user));
        assertTrue(userList.contains(user2));
        assertTrue(userList.contains(user3));
    }

    /**
     * @brief Tests the loadFromSerialized method when serialization file doesn't exist.
     * @details Should rebuild from DB when serialization file is missing or corrupted.
     */
    @Test
    public void testLoadFromSerializedFileNotExists() {
        // Use a non-existent serialization path
        UserSet loadedUserSet = UserSet.loadFromSerialized("nonExistent.ser", "testUsersDB");
        
        // Verify that a new UserSet was created and DB was set
        assertNotNull(loadedUserSet);
        assertNotNull(loadedUserSet.getUserDB());
        assertEquals("testUsersDB", loadedUserSet.getDBPath());
    }

    /**
     * @brief Tests the loadFromSerialized method when hash matches.
     * @details Should load the serialized UserSet as-is when DB hasn't changed.
     */
    @Test
    public void testLoadFromSerializedHashMatches() {
        // Setup: Create and save a UserSet
        UserSet originalUserSet = new UserSet();
        originalUserSet.setDBPath("testUsersDB");
        originalUserSet.setSerializationPath("testLoadHashMatch.ser");
        originalUserSet.setUserDB(new DB("testUsersDB"));
        
        User user1 = new User("11111", "Test", "User", "test@user.com");
        originalUserSet.addOrEditUser(user1);
        
        // Load the UserSet back
        UserSet loadedUserSet = UserSet.loadFromSerialized("testLoadHashMatch.ser", "testUsersDB");
        
        // Verify the UserSet was loaded correctly
        assertNotNull(loadedUserSet);
        assertNotNull(loadedUserSet.getUser("11111"));
        assertEquals(1, loadedUserSet.getUserSet().size());
        assertEquals(originalUserSet.getLastKnownDBHash(), loadedUserSet.getUserDB().getDBFileHash());
    }

    /**
     * @brief Tests the loadFromSerialized method when hash doesn't match.
     * @details Should rebuild from DB when the DB file has been modified externally.
     */
    @Test
    public void testLoadFromSerializedHashMismatch() {
        // Use unique DB file for this test
        String uniqueDBPath = "testHashMismatchDB";
        String uniqueSerPath = "testLoadHashMismatch.ser";
        
        // Setup: Create and save a UserSet with one user
        {
            UserSet originalUserSet = new UserSet();
            originalUserSet.setDBPath(uniqueDBPath);
            originalUserSet.setSerializationPath(uniqueSerPath);
            originalUserSet.setUserDB(new DB(uniqueDBPath));
            
            User user1 = new User("11111", "First", "User", "first@user.com");
            originalUserSet.addOrEditUser(user1);
            // originalUserSet goes out of scope here, releases DB reference
        }
        
        // Now manually add a second user to DB (simulating external modification)
        DB db = new DB(uniqueDBPath);
        User user2 = new User("22222", "Second", "User", "second@user.com");
        db.appendLine(user2.toDBString());
        
        // Load the UserSet - should rebuild from DB due to hash mismatch
        UserSet loadedUserSet = UserSet.loadFromSerialized(uniqueSerPath, uniqueDBPath);
        
        // Verify that UserSet was rebuilt with both users
        assertNotNull(loadedUserSet);
        assertEquals(2, loadedUserSet.getUserSet().size());
        assertNotNull(loadedUserSet.getUser("11111"));
        assertNotNull(loadedUserSet.getUser("22222"));
    }

    /**
     * @brief Tests the loadFromSerialized method when serialization file is corrupted.
     * @details Should rebuild from DB when deserialization fails.
     */
    @Test
    public void testLoadFromSerializedCorruptedFile() throws IOException {
        // Create a corrupted serialization file
        String corruptedPath = "testCorrupted.ser";
        try (FileOutputStream fos = new FileOutputStream(corruptedPath)) {
            fos.write("This is not a valid serialized object".getBytes());
        }
        
        // Attempt to load - should rebuild from DB
        UserSet loadedUserSet = UserSet.loadFromSerialized(corruptedPath, "testUsersDB");
        
        // Verify that a new UserSet was created
        assertNotNull(loadedUserSet);
        assertNotNull(loadedUserSet.getUserDB());
        assertEquals("testUsersDB", loadedUserSet.getDBPath());
    }

    /**
     * @brief Tests the complete serialization cycle.
     * @details Save a UserSet, load it back, verify all data is preserved.
     */
    @Test
    public void testSerializationCycle() {
        // Create and populate a UserSet
        UserSet originalUserSet = new UserSet();
        originalUserSet.setDBPath("testUsersDB");
        originalUserSet.setSerializationPath("testCycle.ser");
        originalUserSet.setUserDB(new DB("testUsersDB"));
        
        User user1 = new User("11111", "John", "Doe", "john@doe.com");
        User user2 = new User("22222", "Jane", "Smith", "jane@smith.com");
        
        originalUserSet.addOrEditUser(user1);
        originalUserSet.addOrEditUser(user2);
        
        // Load it back
        UserSet loadedUserSet = UserSet.loadFromSerialized("testCycle.ser", "testUsersDB");
        
        // Verify all data is preserved
        assertNotNull(loadedUserSet);
        assertEquals(2, loadedUserSet.getUserSet().size());
        
        User retrieved1 = loadedUserSet.getUser("11111");
        assertNotNull(retrieved1);
        assertEquals("John", retrieved1.getName());
        assertEquals("Doe", retrieved1.getSurname());
        assertEquals("john@doe.com", retrieved1.getEmail());
        
        User retrieved2 = loadedUserSet.getUser("22222");
        assertNotNull(retrieved2);
        assertEquals("Jane", retrieved2.getName());
        assertEquals("Smith", retrieved2.getSurname());
        assertEquals("jane@smith.com", retrieved2.getEmail());
    }

    /**
     * @brief Tests the isStored method to verify if a user is stored in the set.
     */
    @Test
    public void testIsStored() {
        assertTrue(userSet.isStored(user.getId())); // User added in setUp
        assertFalse(userSet.isStored("99999")); // Non-existent ID
        
        User newUser = new User("55555", "Carlos", "Sainz", "carlos@sainz.com");
        userSet.addOrEditUser(newUser);
        assertTrue(userSet.isStored("55555"));
    }
}
