/**
 * @file LendingSetTest.java
 * @brief Unit tests for the LendingSet class.
 * @author Daniele Pepe
 */
package poco.company.group01pocolib.mvc.model;

import poco.company.group01pocolib.db.DB;
import poco.company.group01pocolib.db.omnisearch.Search.*;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @class LendingSetTest
 * @brief Contains unit tests to verify the correctness of the LendingSet class methods.
 */
public class LendingSetTest {

    private LendingSet lendingSet;
    private BookSet bookSet;
    private UserSet userSet;
    private Book book;
    private User user;
    private Lending lending;
    private Lending lending2;
    private Lending lending3;
    private Lending lending4;
    private DB lendingDB;

    @BeforeEach
    public void setUp() {
        // Initialize LendingSet with DB and serialization
        lendingSet = new LendingSet();
        lendingDB = new DB("testLendingsDB");
        lendingSet.setLendingDB(lendingDB);
        lendingSet.setDBPath("testLendingsDB");
        lendingSet.setSerializationPath("testLendingSet.ser");

        // Initialize BookSet for linking
        bookSet = new BookSet();
        DB bookDB = new DB("testBooksDB");
        bookSet.setBookDB(bookDB);
        bookSet.setSerializationPath("testBookSet.ser");

        // Initialize UserSet for linking
        userSet = new UserSet();
        DB userDB = new DB("testUsersDB");
        userSet.setUserDB(userDB);
        userSet.setSerializationPath("testUserSet.ser");

        // Create test data
        book = new Book("Il signore degli Anelli", "J.R.R. Tolkien", "978-0261102385", 1954, 20);
        bookSet.addOrEditBook(book);
        
        Book book2 = new Book("Lo Hobbit", "J.R.R. Tolkien", "978-0261102217", 1937, 10);
        bookSet.addOrEditBook(book2);

        Book book3 = new Book("Il Silmarillion", "J.R.R. Tolkien", "978-0261102736", 1977, 5);
        bookSet.addOrEditBook(book3);

        user = new User("67890","Frodo", "Baggins", "frodo.baggins@shire.com");
        User user2 = new User("12345","Samwise", "Gamgee", "samwise.gamgee@shire.com");
        User user3 = new User("54321","Gandalf", "The Grey", "gandalf.grey@middleearth.com");

        userSet.addOrEditUser(user);
        userSet.addOrEditUser(user2);
        userSet.addOrEditUser(user3);

        lending = new Lending(book, user, LocalDate.now().plusDays(14));
        lending2 = new Lending(book2, user2, LocalDate.now().plusDays(21));
        lending3 = new Lending(book3, user3, LocalDate.now().plusDays(30));
        lending4 = new Lending(book, user2, LocalDate.now().plusDays(14));
    }

    @AfterEach
    public void tearDown() {
        // Cleanup: delete test serialization files and DB files
        deleteFileIfExists("testLendingSet.ser");
        deleteFileIfExists("testLendingsDB");
        deleteFileIfExists("testBookSet.ser");
        deleteFileIfExists("testBooksDB");
        deleteFileIfExists("testUserSet.ser");
        deleteFileIfExists("testUsersDB");
        deleteFileIfExists("anotherTestLendingSet.ser");
        deleteFileIfExists("anotherTestLendingsDB");
        deleteFileIfExists("testLoadHashMatch.ser");
        deleteFileIfExists("testLoadHashMismatch.ser");
        deleteFileIfExists("testHashMismatchDB");
        deleteFileIfExists("testCorrupted.ser");
        deleteFileIfExists("nonExistent.ser");
    }

    private void deleteFileIfExists(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * @brief Tests that a LendingSet object is created correctly with default constructor.
     */
    @Test
    public void testLendingSetCreation() {
        LendingSet lendingSet = new LendingSet();
        
        assertNotNull(lendingSet);
        assertNotNull(lendingSet.getLendingSet());
        assertNotNull(lendingSet.getLendingIndex());
        assertTrue(lendingSet.getLendingSet().isEmpty());
    }

    /**
     * @brief Tests that updateLastKnownDBHash actually updates the hash.
     */
    @Test
    public void testUpdateLastKnownDBHash() {
        // Add a lending first to ensure DB has content
        lendingSet.addOrEditLending(lending);
        String initialHash = lendingSet.getLastKnownDBHash();
        
        // Modify DB to change hash by adding another lending
        Book book2 = new Book("1984", "George Orwell", "978-0451524935", 1949, 15);
        bookSet.addOrEditBook(book2);
        Lending lending2 = new Lending(book2, user, LocalDate.now().plusDays(21));
        lendingSet.addOrEditLending(lending2);
        
        String updatedHash = lendingSet.getLastKnownDBHash();
        
        assertNotNull(initialHash);
        assertNotNull(updatedHash);
        assertNotEquals(initialHash, updatedHash);
    }

    /**
     * @brief Tests that updateLastKnownDBHash produces consistent hashes for unchanged DB.
     */
    @Test
    public void testUpdateLastKnownDBHashConsistency() {
        // Add a lending to ensure DB has content
        lendingSet.addOrEditLending(lending);
        
        lendingSet.updateLastKnownDBHash();
        String hash1 = lendingSet.getLastKnownDBHash();
        
        lendingSet.updateLastKnownDBHash();
        String hash2 = lendingSet.getLastKnownDBHash();
        
        assertNotNull(hash1);
        assertNotNull(hash2);
        assertEquals(hash1, hash2);
    }

    /**
     * @brief Tests the getAllLendingsAsList method returns a list of the correct size.
     */
    @Test
    public void testGetAllLendingsAsList() {

        List<Lending> lendingList = lendingSet.getAllLendingsAsList();
        
        assertNotNull(lendingList);
        assertTrue(lendingList instanceof List);

        assertTrue(lendingList.isEmpty());
        
        lendingSet.addOrEditLending(lending);
        lendingList = lendingSet.getAllLendingsAsList();

        assertEquals(1, lendingList.size());
    }


    /**
     * @brief Tests that addOrEditLending updates an existing lending based on ID.
     */
    @Test
    public void testEditLending() {
        lendingSet.addOrEditLending(lending);
        int originalId = lending.getLendingId();
        
        LocalDate newReturnDate = lending.getReturnDate().plusDays(30);
        
        // Modify lending and re-add
        Lending retrieved = lendingSet.getLending(originalId);
        retrieved.setReturnDate(newReturnDate);
        lendingSet.addOrEditLending(retrieved);
        
        assertEquals(1, lendingSet.getLendingSet().size());
        assertEquals(newReturnDate, lendingSet.getLending(originalId).getReturnDate());
    }
    
    /**
     * @brief Tests the removeLending method removes a lending correctly.
    */
   @Test
   public void testRemoveLending() {
       lendingSet.addOrEditLending(lending);
       
       lending.getUser().incrementBorrowedBooksCount();
       lending.getBook().lendCopy();

       int lendingId = lending.getLendingId();
       
       lendingSet.removeLending(lending);
       
       assertNull(lendingSet.getLending(lendingId));
       assertEquals(0, lendingSet.getLendingSet().size());
    }
    
    /**
     * @brief Tests the getLending method returns the correct lending by ID.
    */
   @Test
   public void testGetLending() {
       lendingSet.addOrEditLending(lending);
       int lendingId = lending.getLendingId();
       
       Lending retrieved = lendingSet.getLending(lendingId);
       
       assertNotNull(retrieved);
       assertEquals(lendingId, retrieved.getLendingId());
       assertEquals(lending, retrieved);
    }
    
    /**
     * @brief Tests that getLending returns null when lending is not found.
    */
   @Test
   public void testGetLendingNotFound() {
       lendingSet.addOrEditLending(lending);
       
       Lending retrieved = lendingSet.getLending(99999);
       
       assertNull(retrieved);
    }
    
    /**
     * @brief Tests that getLending returns null for negative IDs.
    */
   @Test
   public void testGetLendingNegativeId() {
       lendingSet.addOrEditLending(lending);
       
       Lending retrieved = lendingSet.getLending(-1);
       
       assertNull(retrieved);
    }
    
    /**
     * @brief Tests that getLending works correctly with multiple lendings.
     */
    @Test
    public void testGetLendingMultiple() {
        Book book2 = new Book("1984", "George Orwell", "978-0451524935", 1949, 15);
        bookSet.addOrEditBook(book2);
        
        Lending lending1 = new Lending(book, user, LocalDate.now().plusDays(14));
        Lending lending2 = new Lending(book2, user, LocalDate.now().plusDays(21));
        
        lendingSet.addOrEditLending(lending1);
        lendingSet.addOrEditLending(lending2);
        
        Lending retrieved1 = lendingSet.getLending(lending1.getLendingId());
        Lending retrieved2 = lendingSet.getLending(lending2.getLendingId());
        
        assertNotNull(retrieved1);
        assertNotNull(retrieved2);
        assertEquals(lending1, retrieved1);
        assertEquals(lending2, retrieved2);
        assertNotEquals(retrieved1, retrieved2);
    }

    /**
     * @brief Tests the rebuildFromDB method correctly rebuilds the lending set.
    */
   @Test
   public void testRebuildFromDB() {
        
        lendingSet.addOrEditLending(lending);
        String dbPath = lendingSet.getDBPath();
        
        // Create new LendingSet and rebuild
        LendingSet newLendingSet = new LendingSet();
        newLendingSet.setDBPath(dbPath);
        newLendingSet.rebuildFromDB(dbPath, bookSet, userSet);
        
        assertNotNull(newLendingSet.getLendingDB());
        assertNotNull(newLendingSet.getLastKnownDBHash());
    }
 
    /**
     * @brief Tests that rebuildFromDB clears existing data before rebuilding.
    */
    @Test
    public void testRebuildFromDBClearsExisting() {
        // Add some initial data
        Lending lending1 = new Lending(book, user, LocalDate.now().plusDays(14));
        lendingSet.addOrEditLending(lending1);
        
        // Rebuild from empty DB
        DB emptyDB = new DB("anotherTestLendingsDB");
        lendingSet.setLendingDB(emptyDB);
        lendingSet.rebuildFromDB("anotherTestLendingsDB", bookSet, userSet);
        
        assertEquals(0, lendingSet.getLendingSet().size());
    }
    
    /**
     * @brief Tests the loadFromSerialized method when serialization file doesn't exist.
     */
    @Test
    public void testLoadFromSerializedFileNotExists() {
        LendingSet loadedLendingSet = LendingSet.loadFromSerialized("nonExistent.ser", "testLendingsDB", bookSet, userSet);
        
        assertNotNull(loadedLendingSet);
        assertNotNull(loadedLendingSet.getLendingDB());
        assertEquals("testLendingsDB", loadedLendingSet.getDBPath());
    }

    /**
     * @brief Tests the loadFromSerialized method when hash matches.
     */
    @Test
    public void testLoadFromSerializedHashMatches() {
        // Setup: Create and save a LendingSet
        LendingSet originalLendingSet = new LendingSet();
        originalLendingSet.setDBPath("testLendingsDB");
        originalLendingSet.setSerializationPath("testLoadHashMatch.ser");
        originalLendingSet.setLendingDB(new DB("testLendingsDB"));
        
        Lending lending1 = new Lending(book, user, LocalDate.now().plusDays(14));
        originalLendingSet.addOrEditLending(lending1);
        
        // Load the LendingSet back
        LendingSet loadedLendingSet = LendingSet.loadFromSerialized("testLoadHashMatch.ser", "testLendingsDB", bookSet, userSet);
        
        assertNotNull(loadedLendingSet);
        assertNotNull(loadedLendingSet.getLending(lending1.getLendingId()));
        assertEquals(1, loadedLendingSet.getLendingSet().size());
        assertEquals(originalLendingSet.getLastKnownDBHash(), loadedLendingSet.getLendingDB().getDBFileHash());
    }
    
    /**
     * @brief Tests the loadFromSerialized method when hash doesn't match.
    */
   @Test
   public void testLoadFromSerializedHashMismatch() {
       String uniqueDBPath = "testHashMismatchDB";
       String uniqueSerPath = "testLoadHashMismatch.ser";
       
       // Create and save a LendingSet
        LendingSet originalLendingSet = new LendingSet();
        originalLendingSet.setDBPath(uniqueDBPath);
        originalLendingSet.setSerializationPath(uniqueSerPath);
        originalLendingSet.setLendingDB(new DB(uniqueDBPath));
           
        Lending lending1 = new Lending(book, user, LocalDate.now().plusDays(14));
        originalLendingSet.addOrEditLending(lending1);
        
        
        // Modify DB file externally
        try (FileWriter writer = new FileWriter(uniqueDBPath, true)) {
            writer.write("\nmodified");
        } catch (IOException e) {
            fail("Failed to modify DB file for test");
        }
        
        // Load - should rebuild from DB
        LendingSet loadedLendingSet = LendingSet.loadFromSerialized(uniqueSerPath, uniqueDBPath, bookSet, userSet);
        
        assertNotNull(loadedLendingSet);
        assertEquals(uniqueDBPath, loadedLendingSet.getDBPath());
    }

    /**
     * @brief Tests that loadFromSerialized handles corrupted serialization file.
     */
    @Test
    public void testLoadFromSerializedCorruptedFile() {
        // Create a corrupted serialization file
        try (FileWriter writer = new FileWriter("testCorrupted.ser")) {
            writer.write("corrupted data");
        } catch (IOException e) {
            fail("Failed to create corrupted file for test");
        }
        
        LendingSet loadedLendingSet = LendingSet.loadFromSerialized("testCorrupted.ser", "testLendingsDB", bookSet, userSet);
        
        assertNotNull(loadedLendingSet);
        assertNotNull(loadedLendingSet.getLendingDB());
        assertEquals("testLendingsDB", loadedLendingSet.getDBPath());
    }

    /**
     * @brief Tests serialization and deserialization preserves data.
    */
   @Test
   public void testSerializationPreservesData() {
        lendingSet.addOrEditLending(lending);
        int originalId = lending.getLendingId();
       
        LendingSet loadedSet = LendingSet.loadFromSerialized(lendingSet.getSerializationPath(), lendingSet.getDBPath(), bookSet, userSet);
        
        assertNotNull(loadedSet);
        Lending loadedLending = loadedSet.getLending(originalId);
        
        assertEquals(lending, loadedLending);
    }
    
    /**
     * @brief Tests that addOrEditLending properly synchronizes with DB.
     */
    @Test
    public void testAddLendingSyncOnWrite() {
        lendingSet.addOrEditLending(lending);
        
        // Verify DB has been updated
        assertNotNull(lendingSet.getLendingDB());
        assertNotNull(lendingSet.getLastKnownDBHash());
        
        // Verify lending is in the set
        assertEquals(1, lendingSet.getLendingSet().size());
        
        // Explicitly save to serialization
        lendingSet.saveToSerialized();
        
        // Now verify serialization file exists
        File serFile = new File(lendingSet.getSerializationPath());
        assertTrue(serFile.exists());
    }
 
    /**
     * @brief Tests that removeLending properly synchronizes with DB.
     */
    @Test
    public void testRemoveLendingSyncOnWrite() {
        lendingSet.addOrEditLending(lending);
        String intialHash = lendingSet.getLastKnownDBHash();
        
        // Explicitly save to get initial serialization file
        lendingSet.saveToSerialized();
        File serFile = new File(lendingSet.getSerializationPath());
        long initialSize = serFile.length();

        lending.getUser().incrementBorrowedBooksCount();
        lending.getBook().lendCopy();

        lendingSet.removeLending(lending);
        
        // Verify DB has been updated
        String newHash = lendingSet.getLastKnownDBHash();
        assertNotNull(newHash);
        assertNotEquals(intialHash, newHash);
        
        // Explicitly save after removal
        lendingSet.saveToSerialized();
        
        // Verify serialization file has been updated
        long newSize = serFile.length();
        assertNotEquals(initialSize, newSize);      
        
    }
    
    /**
     * @brief Tests the search method returns a list of matching lendings.
    */
   @Test
   public void testSearch() {
        lendingSet.addOrEditLending(lending);
        lendingSet.addOrEditLending(lending2);
        lendingSet.addOrEditLending(lending3);
        lendingSet.addOrEditLending(lending4);

        // Search by book title
        List<SearchResult<Lending>> results = lendingSet.search("Hobbit");
        Lending foundLending = results.get(0).item;
        
        assertEquals(lending2, foundLending);

        // Search by user name
        results = lendingSet.search("Baggins");
        foundLending = results.get(0).item;

        assertEquals(lending, foundLending);

        // Search by user email
        results = lendingSet.search("samwise.gamgee");
        foundLending = results.get(0).item;

        assertEquals(lending2, foundLending);


        // Search by Lending ID
        results = lendingSet.search(String.valueOf(lending3.getLendingId()));
        foundLending = results.get(0).item;

        assertEquals(lending3, foundLending);
        
    }
    
    /**
     * @brief Tests the search method returns an empty list when no matches found.
    */
    @Test
    public void testSearchNoMatches() {
        lendingSet.addOrEditLending(lending);
        lendingSet.addOrEditLending(lending2);
        // Search for a term that doesn't match any lending
        List<SearchResult<Lending>> results = lendingSet.search("lorem");

        // Verify if the first result does not contain the term "lorem"
        assertFalse(results.get(0).item.toString().contains("lorem"));

    }

}    
