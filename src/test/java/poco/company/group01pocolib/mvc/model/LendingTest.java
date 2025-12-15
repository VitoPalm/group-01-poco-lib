/**
 * @file LendingTest.java
 * @brief Unit tests for the Lending class.
 * @author Daniele Pepe
 */
package poco.company.group01pocolib.mvc.model;

import org.junit.jupiter.api.Test;

import poco.company.group01pocolib.db.DB;

import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Arrays;

/**
 * @class LendingTest
 * @brief Contains unit tests to verify the correctness of the Lending class.
 */
public class LendingTest {
    
    private Book testBook;
    private User testUser;
    private LocalDate testReturnDate;
    private BookSet bookSet;
    private UserSet userSet;
    private DB bookDB;
    private DB userDB;
    
    /**
     * @brief Sets up common test data before each test.
     */
    @BeforeEach
    public void setUp() {
        bookDB = new DB("books.db");
        userDB = new DB("users.db");
        bookSet = new BookSet();
        bookSet.setBookDB(bookDB);
        bookSet.setSerializationPath("test_books.ser");
        userSet = new UserSet();
        userSet.setUserDB(userDB);
        userSet.setSerializationPath("test_users.ser");
        testBook = new Book("Test Book", Arrays.asList("Author One", "Author Two"), "978-0-596-52068-7", 2020, 5);
        testUser = new User("USER12345", "Mario", "Rossi", "mario.rossi@example.com");
        testReturnDate = LocalDate.of(2025, 12, 31);

    }
    
    /**
     * @brief Tests that a Lending object is created correctly when valid arguments are provided.
     */
    @Test
    public void testLendingCreationValid() {
        Lending lending = new Lending(testBook, testUser, testReturnDate);
        
        assertNotNull(lending);
        assertEquals(testBook, lending.getBook());
        assertEquals(testUser, lending.getUser());
        assertEquals(testReturnDate, lending.getReturnDate());
        assertFalse(lending.isReturned());
        assertTrue(lending.getLendingId() > 0);
    }
    
    /**
     * @brief Tests that each Lending object gets a unique auto-incremented ID.
     */
    @Test
    public void testLendingIdAutoIncrement() {
        Lending lending1 = new Lending(testBook, testUser, testReturnDate);
        Lending lending2 = new Lending(testBook, testUser, testReturnDate);
        
        assertTrue(lending2.getLendingId() > lending1.getLendingId());
        assertEquals(lending1.getLendingId() + 1, lending2.getLendingId());
    }
    
    /**
     * @brief Tests the editing functionality of a Lending object(setReturnDate, setReturned, setNotReturned).
     */
    @Test
    public void testLendingEdit() {
        Lending lending = new Lending(testBook, testUser, testReturnDate);
        // Manually increment counters for book and user to simulate lending creation effects
        testBook.lendCopy();
        testUser.incrementBorrowedBooksCount();

        // Test initial state
        assertFalse(lending.isReturned());
        
        // Test setReturned
        lending.setReturned();
        assertTrue(lending.isReturned());
        
        // Test setNotReturned
        lending.setNotReturned();
        assertFalse(lending.isReturned());
        
        // Test setReturnDate
        LocalDate newReturnDate = LocalDate.of(2026, 1, 15);
        lending.setReturnDate(newReturnDate);
        assertEquals(newReturnDate, lending.getReturnDate());
    }
    
    /**
     * @brief Tests the setBook method.
     */
    @Test
    public void testSetBook() {
        Lending lending = new Lending(testBook, testUser, testReturnDate);
        Book newBook = new Book("New Book", Arrays.asList("New Author"), "978-1-56619-909-4", 2021, 3);
        
        lending.setBook(newBook);
        assertEquals(newBook, lending.getBook());
    }
    
    /**
     * @brief Tests the setUser method.
     */
    @Test
    public void testSetUser() {
        Lending lending = new Lending(testBook, testUser, testReturnDate);
        User newUser = new User("USER67890", "Luigi", "Verdi", "luigi.verdi@example.com");
        
        lending.setUser(newUser);
        assertEquals(newUser, lending.getUser());
    }
    
    /**
     * @brief Tests the equals method with same object.
     */
    @Test
    public void testEqualsSameObject() {
        Lending lending = new Lending(testBook, testUser, testReturnDate);
        
        assertTrue(lending.equals(lending));
    }
    
    /**
     * @brief Tests the equals method with null object.
     */
    @Test
    public void testEqualsNull() {
        Lending lending = new Lending(testBook, testUser, testReturnDate);
        
        assertFalse(lending.equals(null));
    }
    
    /**
     * @brief Tests the equals method with different class.
     */
    @Test
    public void testEqualsDifferentClass() {
        Lending lending = new Lending(testBook, testUser, testReturnDate);
        String notALending = "Not a Lending";
        
        assertFalse(lending.equals(notALending));
    }
    
    /**
     * @brief Tests the equals method with different lending objects having different IDs.
     */
    @Test
    public void testEqualsDifferentLendingIds() {
        Lending lending1 = new Lending(testBook, testUser, testReturnDate);
        Lending lending2 = new Lending(testBook, testUser, testReturnDate);
        
        assertFalse(lending1.equals(lending2));
    }
    
    /**
     * @brief Tests the hashCode method consistency.
     */
    @Test
    public void testHashCodeConsistency() {
        Lending lending = new Lending(testBook, testUser, testReturnDate);
        
        int hashCode1 = lending.hashCode();
        int hashCode2 = lending.hashCode();
        
        assertEquals(hashCode1, hashCode2);
    }
    
    /**
     * @brief Tests that different lending objects have different hash codes.
     */
    @Test
    public void testHashCodeDifferentObjects() {
        Lending lending1 = new Lending(testBook, testUser, testReturnDate);
        Lending lending2 = new Lending(testBook, testUser, testReturnDate);
        
        assertNotEquals(lending1.hashCode(), lending2.hashCode());
    }
    
    /**
     * @brief Tests the initial returned status is false.
     */
    @Test
    public void testInitialReturnedStatus() {
        Lending lending = new Lending(testBook, testUser, testReturnDate);
        
        assertFalse(lending.isReturned());
    }
    
    /**
     * @brief Tests multiple setReturned and setNotReturned calls.
     */
    @Test
    public void testMultipleReturnedStatusChanges() {
        Lending lending = new Lending(testBook, testUser, testReturnDate);
        // Manually increment counters for book and user to simulate lending creation effects
        testBook.lendCopy();
        testUser.incrementBorrowedBooksCount();
        
        lending.setReturned();
        assertTrue(lending.isReturned());
        
        lending.setReturned();
        assertTrue(lending.isReturned());
        
        lending.setNotReturned();
        assertFalse(lending.isReturned());
        
        lending.setNotReturned();
        assertFalse(lending.isReturned());
    }
    
    /**
     * @brief Tests toDBString method returns non-empty string.
     */
    @Test
    public void testToDBStringContainsSeparator() {
        Lending lending = new Lending(testBook, testUser, testReturnDate);
        String dbString = lending.toDBString();
        
        assertFalse(dbString.isEmpty());
        assertTrue(dbString.contains("\u001C"));
    }
    
    /**
     * @brief Tests toSearchableString contains lending information.
     */
    @Test
    public void testToSearchableStringContainsInfo() {
        Lending lending = new Lending(testBook, testUser, testReturnDate);
        String searchableString = lending.toSearchableString();
        System.out.println(searchableString);
        assertNotNull(searchableString);
        assertTrue(searchableString.contains(String.valueOf(lending.getLendingId()).toLowerCase()) &&
                   searchableString.contains(lending.getReturnDate().toString().toLowerCase()) &&
                   searchableString.contains(lending.getBook().toSearchableString()) &&
                   searchableString.contains(lending.getUser().toSearchableString()));
    }
    
    /**
     * @brief Tests fromDBString with null input.
     */
    @Test
    public void testFromDBStringNull() {
        Lending lending = Lending.fromDBString(null, bookSet, userSet);
        
        assertNull(lending);
    }

    /**
     * @brief Tests fromDBString with empty string.
     */
    @Test
    public void testFromDBStringEmpty() {
        Lending lending = Lending.fromDBString("", bookSet, userSet);
        
        assertNull(lending);
    }

    /**
     * @brief Tests fromDBString with valid formatted string.
     */
    @Test
    public void testFromDBStringValid() {
        
        Lending original = new Lending(testBook, testUser, testReturnDate);
        String dbString = original.toDBString();
        
        
        bookSet.addOrEditBook(testBook);
       
        userSet.addOrEditUser(testUser);
        
        Lending reconstructed = Lending.fromDBString(dbString, bookSet, userSet);
        
        assertNotNull(reconstructed);
        assertEquals(original.getReturnDate(), reconstructed.getReturnDate());
        assertEquals(original.isReturned(), reconstructed.isReturned());
        assertEquals(original.getBook(), reconstructed.getBook());
        assertEquals(original.getUser(), reconstructed.getUser());
    }
    
}