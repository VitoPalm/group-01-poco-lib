/**
 * @file BookSetTest.java
 * @brief Unit tests for the BookSet class.
 * @author Daniele Pepe
 * @author Francesco M
 */
package poco.company.group01pocolib.mvc.model;

import poco.company.group01pocolib.db.DB;
import poco.company.group01pocolib.db.omnisearch.Index;
import poco.company.group01pocolib.exceptions.BookDataNotValidException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import poco.company.group01pocolib.db.omnisearch.Search.SearchResult;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * @class BookSetTest
 * @brief Contains unit tests to verify the correctness of the BookSet class methods.
 */
public class BookSetTest {

    private BookSet bookSet;
    private Book book;
    private Book book2;
    private Book book3;
    private DB bookDB;

    @BeforeEach
    public void setUp() {
        // Setup code if needed before each test
        bookSet = new BookSet();

        bookDB = new DB("testBooksDB");
        bookSet.setBookDB(bookDB);
        bookSet.setSerializationPath("testBookSet.ser");

        book = new Book("Il signore degli Anelli", "J.R.R. Tolkien", "978-0261102385", 1954, 20);
        book2 = new Book("Lo Hobbit", "J.R.R. Tolkien", "978-0261102217", 1937, 15);
        book3 = new Book("Il Capitale", "Karl Marx", "978-8807170106", 1867, 5);
    }

    @AfterEach
    public void tearDown() {
        // Cleanup: delete test serialization files and DB files
        deleteFileIfExists("testBookSet.ser");
        deleteFileIfExists("testBooksDB");
        deleteFileIfExists("anotherTestBookSet.ser");
        deleteFileIfExists("anotherTestBooksDB");
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
     * @brief Tests that a BookSet object is created correctly.
     */
    @Test
    public void testBookSetCreation() {
        BookSet bookSet = new BookSet();

        assertNotNull(bookSet);        
    }

    /**
     * @brief Tests the addBook method to ensure a book is added correctly.
     */
    @Test
    public void testAddBook() {       
        bookSet.addOrEditBook(book);
 
        assertNotNull(bookSet.getBook("978-0261102385"));
    }

    /**
     * @brief Tests that addBook throws NullPointerException when a null book is provided.
     */
    @Test
    public void testAddBookNull() {
  
        Assertions.assertThrows(NullPointerException.class, () -> {
            bookSet.addOrEditBook(null);
        });
    }

    /**
     * @brief Tests the removeBook method to ensure a book is removed correctly.
     */
    @Test
    public void testRemoveBook() {    
        bookSet.addOrEditBook(book);
        bookSet.removeBook("978-0261102385");

        assertNull(bookSet.getBook("978-0261102385"));
    }

    /**
     * @brief Tests the getBook method to ensure it returns the correct book.
     */
    @Test
    public void testGetBook() {
        bookSet.addOrEditBook(book);

        assertNotNull(bookSet.getBook("978-0261102385"));
    }

    /**
     * @brief Tests that getBook returns null when the book is not found.
     */
    @Test
    public void testGetBookNotFound() {
        bookSet.addOrEditBook(book);

        assertNull(bookSet.getBook("123-4567890123"));
    }

    /**
     * @brief Tests the getBookSet method to ensure it returns the correct set of books.
     */
    @Test
    public void testGetBookSet(){
        bookSet.addOrEditBook(book);
        Assertions.assertEquals(1, bookSet.getBookSet().size());
    }

    /**
     * @brief Tests the setBookSet method to ensure it sets the book set correctly.
     */
    @Test
    public void testSetBookSet(){
        // Create a new BookSet with a different book
        BookSet newBookSet = new BookSet();

        // Set up a different DB and serialization path to avoid conflicts
        DB anotherBookDB = new DB("anotherTestBooksDB");
        newBookSet.setBookDB(anotherBookDB);
        newBookSet.setSerializationPath("anotherTestBookSet.ser");

        // Add a different book to the new BookSet
        Book anotherBook = new Book("1984", "George Orwell", "978-0451524935", 1949, 15);
        newBookSet.addOrEditBook(anotherBook);

        // Set the book set of the original BookSet to the new BookSet's book set
        bookSet.setBookSet((HashSet<Book>) newBookSet.getBookSet());

        // Verify that the original BookSet now contains the new book instead of the old one
        Assertions.assertEquals(1, bookSet.getBookSet().size());
        Assertions.assertNotNull(bookSet.getBook("978-0451524935"));
    }
    
    
    @Test
    public void testSetBookIndex(){
        bookSet.addOrEditBook(book);

        Book retrievedBook = bookSet.getBook("978-0261102385");
        Assertions.assertEquals(book, retrievedBook);

        // Create a new index and add a different book
        Index<Book> newBookIndex = new Index<>();
        Book anotherBook = new Book("1984", "George Orwell", "978-0451524935", 1949, 15);
        newBookIndex.add(anotherBook.toSearchableString(), anotherBook);
        
        // Set the new index
        bookSet.setBookIndex(newBookIndex);
        
        // Verify that the new index is set correctly
        Assertions.assertEquals(newBookIndex, bookSet.getBookIndex());
    }


    /**
     * @brief Tests the loadFromSerialized method when serialization file doesn't exist.
     * @details Should rebuild from DB when serialization file is missing or corrupted.
     */
    @Test
    public void testLoadFromSerializedFileNotExists() {
        // Use a non-existent serialization path
        BookSet loadedBookSet = BookSet.loadFromSerialized("nonExistent.ser", "testBooksDB");
        
        // Verify that a new BookSet was created and DB was set
        assertNotNull(loadedBookSet);
        assertNotNull(loadedBookSet.getBookDB());
        assertEquals("testBooksDB", loadedBookSet.getDBPath());
    }

    /**
     * @brief Tests the loadFromSerialized method when hash matches.
     * @details Should load the serialized BookSet as-is when DB hasn't changed.
     */
    @Test
    public void testLoadFromSerializedHashMatches() {
        // Setup: Create and save a BookSet
        BookSet originalBookSet = new BookSet();
        originalBookSet.setDBPath("testBooksDB");
        originalBookSet.setSerializationPath("testLoadHashMatch.ser");
        originalBookSet.setBookDB(new DB("testBooksDB"));
        
        Book book1 = new Book("The Hobbit", "J.R.R. Tolkien", "978-0547928227", 1937, 10);
        originalBookSet.addOrEditBook(book1);
        
        // Load the BookSet back
        BookSet loadedBookSet = BookSet.loadFromSerialized("testLoadHashMatch.ser", "testBooksDB");
        
        // Verify the BookSet was loaded correctly
        assertNotNull(loadedBookSet);
        assertNotNull(loadedBookSet.getBook("978-0547928227"));
        assertEquals(1, loadedBookSet.getBookSet().size());
        assertEquals(originalBookSet.getLastKnownDBHash(), loadedBookSet.getBookDB().getDBFileHash());
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
        
        // Setup: Create and save a BookSet with one book
        {
            BookSet originalBookSet = new BookSet();
            originalBookSet.setDBPath(uniqueDBPath);
            originalBookSet.setSerializationPath(uniqueSerPath);
            originalBookSet.setBookDB(new DB(uniqueDBPath));
            
            Book book1 = new Book("The Hobbit", "J.R.R. Tolkien", "978-0547928227", 1937, 10);
            originalBookSet.addOrEditBook(book1);
            System.out.println("Hash after book1: " + originalBookSet.getLastKnownDBHash());
            // originalBookSet goes out of scope here, releases DB reference
        }
        
        // Now manually add a second book to DB (simulating external modification)
        DB db = new DB(uniqueDBPath);
        System.out.println("Hash before adding book2: " + db.getDBFileHash());
        Book book2 = new Book("1984", "George Orwell", "978-0451524935", 1949, 15);
        db.appendLine(book2.toDBString());
        
        // Force a new DB to recalculate hash
        DB dbAfter = new DB(uniqueDBPath);
        System.out.println("Hash after adding book2: " + dbAfter.getDBFileHash());
        
        // Load the BookSet - should rebuild from DB due to hash mismatch
        BookSet loadedBookSet = BookSet.loadFromSerialized(uniqueSerPath, uniqueDBPath);
        
        System.out.println("Loaded BookSet has " + loadedBookSet.getBookSet().size() + " books");
        
        // Verify that BookSet was rebuilt with both books
        assertNotNull(loadedBookSet);
        assertEquals(2, loadedBookSet.getBookSet().size());
        assertNotNull(loadedBookSet.getBook("978-0547928227"));
        assertNotNull(loadedBookSet.getBook("978-0451524935"));
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
        BookSet loadedBookSet = BookSet.loadFromSerialized(corruptedPath, "testBooksDB");
        
        // Verify that a new BookSet was created
        assertNotNull(loadedBookSet);
        assertNotNull(loadedBookSet.getBookDB());
        assertEquals("testBooksDB", loadedBookSet.getDBPath());
    }

    /**
     * @brief Tests the complete serialization cycle.
     * @details Save a BookSet, load it back, verify all data is preserved.
     */
    @Test
    public void testSerializationCycle() {
        // Create and populate a BookSet
        BookSet originalBookSet = new BookSet();
        originalBookSet.setDBPath("testBooksDB");
        originalBookSet.setSerializationPath("testCycle.ser");
        originalBookSet.setBookDB(new DB("testBooksDB"));
        
        Book book1 = new Book("Harry Potter", "J.K. Rowling", "978-0439708180", 1997, 25);
        Book book2 = new Book("The Great Gatsby", "F. Scott Fitzgerald", "978-0743273565", 1925, 12);
        
        originalBookSet.addOrEditBook(book1);
        originalBookSet.addOrEditBook(book2);
        
        // Load it back
        BookSet loadedBookSet = BookSet.loadFromSerialized("testCycle.ser", "testBooksDB");
        
        // Verify all data is preserved
        assertNotNull(loadedBookSet);
        assertEquals(2, loadedBookSet.getBookSet().size());
        
        Book retrieved1 = loadedBookSet.getBook("978-0439708180");
        assertNotNull(retrieved1);
        assertEquals("Harry Potter", retrieved1.getTitle());
        assertEquals(25, retrieved1.getCopiesAvailable());
        
        Book retrieved2 = loadedBookSet.getBook("978-0743273565");
        assertNotNull(retrieved2);
        assertEquals("The Great Gatsby", retrieved2.getTitle());
        assertEquals(12, retrieved2.getCopiesAvailable());
    }

    @Test
    public void testIsStored(){
        bookSet.addOrEditBook(book);
        Assertions.assertTrue( bookSet.isStored("978-0261102385"));
        Assertions.assertFalse( bookSet.isStored("978-0000000000"));
    }

    /**
     * @brief Tests the editBook functionality by modifying an existing book.
     */
    @Test
    public void testEditBook() {
        // Add initial book
        bookSet.addOrEditBook(book);
        
        // Verify initial state
        Book retrieved = bookSet.getBook("978-0261102385");
        assertNotNull(retrieved);
        assertEquals("Il signore degli Anelli", retrieved.getTitle());
        assertEquals(20, retrieved.getCopiesAvailable());
        
        // Edit the book (same ISBN, different details)
        Book editedBook = new Book("Il Signore degli Anelli - Edizione Speciale", "J.R.R. Tolkien", "978-0261102385", 1954, 25);
        bookSet.addOrEditBook(editedBook);
        
        // Verify the book was updated, not duplicated
        assertEquals(1, bookSet.getBookSet().size());
        
        Book updatedBook = bookSet.getBook("978-0261102385");
        assertNotNull(updatedBook);
        assertEquals("Il Signore degli Anelli - Edizione Speciale", updatedBook.getTitle());
        assertEquals(25, updatedBook.getCopiesAvailable());
    }

    /**
     * @brief Tests that adding a book with duplicate ISBN replaces the old one.
     */
    @Test
    public void testAddBookDuplicateISBN() {
        // Add first book
        Book book1 = new Book("First Title", "Author One", "978-1234567890", 2020, 10);
        bookSet.addOrEditBook(book1);
        assertEquals(1, bookSet.getBookSet().size());
        
        // Add book with same ISBN but different data
        Book book2 = new Book("Second Title", "Author Two", "978-1234567890", 2021, 15);
        bookSet.addOrEditBook(book2);
        
        // Should still have only one book
        assertEquals(1, bookSet.getBookSet().size());
        
        // The new book should have replaced the old one
        Book retrieved = bookSet.getBook("978-1234567890");
        assertEquals("Second Title", retrieved.getTitle());
        assertEquals("Author Two", retrieved.getAuthorsString());
        assertEquals(15, retrieved.getCopiesAvailable());
    }

    /**
     * @brief Tests removing a non-existent book (should not throw exception).
     */
    @Test
    public void testRemoveNonExistentBook() {
        // Add one book
        bookSet.addOrEditBook(book);
        assertEquals(1, bookSet.getBookSet().size());
        
        // Try to remove a book that doesn't exist
        bookSet.removeBook("978-9999999999");
        
        // Should still have the original book
        assertEquals(1, bookSet.getBookSet().size());
        assertNotNull(bookSet.getBook("978-0261102385"));
    }

    /**
     * @brief Tests getBook with null ISBN.
     */
    @Test
    public void testGetBookNullISBN() {
        bookSet.addOrEditBook(book);
        
        // Getting a book with null ISBN should return null
        assertNull(bookSet.getBook(null));
    }

    /**
     * @brief Tests getBook with empty ISBN.
     */
    @Test
    public void testGetBookEmptyISBN() {
        bookSet.addOrEditBook(book);
        
        // Getting a book with empty ISBN should return null
        assertNull(bookSet.getBook(""));
    }

    /**
     * @brief Tests multiple additions and removals.
     */
    @Test
    public void testMultipleAdditionsAndRemovals() {
        // Add multiple books
        Book book1 = new Book("Book 1", "Author 1", "978-1111111111", 2020, 5);
        Book book2 = new Book("Book 2", "Author 2", "978-2222222222", 2021, 10);
        Book book3 = new Book("Book 3", "Author 3", "978-3333333333", 2022, 15);
        
        bookSet.addOrEditBook(book1);
        bookSet.addOrEditBook(book2);
        bookSet.addOrEditBook(book3);
        
        assertEquals(3, bookSet.getBookSet().size());
        
        // Remove one book
        bookSet.removeBook("978-2222222222");
        assertEquals(2, bookSet.getBookSet().size());
        assertNull(bookSet.getBook("978-2222222222"));
        
        // Other books should still be there
        assertNotNull(bookSet.getBook("978-1111111111"));
        assertNotNull(bookSet.getBook("978-3333333333"));
        
        // Remove remaining books
        bookSet.removeBook("978-1111111111");
        bookSet.removeBook("978-3333333333");
        assertEquals(0, bookSet.getBookSet().size());
    }

    /**
     * @brief Tests isStored with Book object.
     */
    @Test
    public void testIsStoredWithBookObject() {
        bookSet.addOrEditBook(book);
        
        // Same ISBN should be considered stored
        Book sameBook = new Book("Different Title", "Different Author", "978-0261102385", 2000, 1);
        Assertions.assertTrue(bookSet.isStored(sameBook));
        
        // Different ISBN should not be stored
        Book differentBook = new Book("Other Book", "Other Author", "978-9999999999", 2000, 1);
        Assertions.assertFalse(bookSet.isStored(differentBook));
    }

    /**
     * @brief Tests that validation exceptions from Book are properly propagated.
     */
    @Test
    public void testAddBookWithInvalidData() {
        // Book with negative available copies should throw exception
        Assertions.assertThrows(BookDataNotValidException.class, () -> {
            Book invalidBook = new Book("Invalid Book", "Author", "978-1234567890", 2020, -5);
            bookSet.addOrEditBook(invalidBook);
        });
    }

    /**
     * @brief Tests getListOfBooks returns correct list.
     */
    @Test
    public void testGetListOfBooks() {
        // Add multiple books
        Book book1 = new Book("Book A", "Author A", "978-1111111111", 2020, 5);
        Book book2 = new Book("Book B", "Author B", "978-2222222222", 2021, 10);
        
        bookSet.addOrEditBook(book1);
        bookSet.addOrEditBook(book2);
        
        List<Book> bookList = bookSet.getListOfBooks();
        
        assertNotNull(bookList);
        assertEquals(2, bookList.size());
        
        // Verify all books are in the list
        boolean hasBook1 = bookList.stream().anyMatch(b -> b.getIsbn().equals("978-1111111111"));
        boolean hasBook2 = bookList.stream().anyMatch(b -> b.getIsbn().equals("978-2222222222"));
        
        Assertions.assertTrue(hasBook1);
        Assertions.assertTrue(hasBook2);
    }

    /**
     * @brief Tests the search functionality of BookSet.
     */
    @Test
    public void testSearch(){
        bookSet.addOrEditBook(book);
        bookSet.addOrEditBook(book2);
        bookSet.addOrEditBook(book3);

        // Search by title
        List<SearchResult<Book>> results = bookSet.search("Il signore degli Anelli");
        Book resultBook = results.get(0).item;
        assertEquals(book, resultBook);

        // Search by author
        results = bookSet.search("Karl Marx");
        resultBook = results.get(0).item;
        assertEquals(book3, resultBook);

        // Search by ISBN
        results = bookSet.search("978-0261102217");
        resultBook = results.get(0).item;
        assertEquals(book2, resultBook);

        // Search by partial title
        results = bookSet.search("Hobbit");
        resultBook = results.get(0).item;
        assertEquals(book2, resultBook);
        
    }

    /**
     * @brief Tests searching for a non-existent book returns empty results.
     */
    @Test
    public void testSearchNonExistentBook(){
        bookSet.addOrEditBook(book);
        bookSet.addOrEditBook(book2);
        bookSet.addOrEditBook(book3);

        // Search for a non-existent book
        List<SearchResult<Book>> results = bookSet.search("Non Existent Book");
        assertEquals(0, results.size());
        
    }


}
