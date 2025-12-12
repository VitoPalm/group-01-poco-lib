/**
 * @file BookTest.java
 * @brief Unit tests for the Book class.
 * @author Daniele Pepe
 */
package poco.company.group01pocolib.mvc.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import poco.company.group01pocolib.exceptions.BookDataNotValidException;

/**
 * @class BookTest
 * @brief Contains unit tests to verify the correctness of the Book class methods and validation logic.
 */
public class BookTest {

    /**
     * @brief Tests that a Book object is created correctly when valid arguments are provided.
     */
    @Test
    public void testBookCreationValid() {
        List<String> authors = new ArrayList<>();
        authors.add("City Council");
        authors.add("Pennsylvania State University");
        Book b = new Book("General Plan for Phoenix, 1985-2000", authors, "316830724", 1985, 1);

        assertEquals(b.getTitle(), "General Plan for Phoenix, 1985-2000");
        assertEquals(b.getAuthorsString(), "City Council; Pennsylvania State University");
        assertEquals(b.getIsbn(), "316830724");
        assertEquals(b.getYear(), 1985);
        assertEquals(b.getCopies(), 1);
        assertEquals(b.getCopiesLent(), 0);
        assertEquals(b.getTimesLent(), 0);
    }

    /**
     * @brief Tests that the constructor throws {@link poco.company.group01pocolib.exceptions.BookDataNotValidException BookDataNotValidException} when an invalid number of copies is provided.
     */
    @Test
    public void testBookCreationInvalid() {
        List<String> authors = new ArrayList<>();
        authors.add("Some author");
        assertThrows(BookDataNotValidException.class, () -> {
            Book b = new Book("The Book", authors, "9788888888", 2009, -4);
        });
        }
    
    /**
     * @brief Tests the edit method to ensure book details are updated correctly. 
     */
    @Test
    public void testBookEditValid(){
        List<String> authors = new ArrayList<>();
        authors.add("City Council");
        authors.add("Pennsylvania State University");
        Book b = new Book("General Plan for Phoenix, 1985-2000", authors, "316830724", 1985, 1);
        
        b.setTitle("Unknown 1");
        b.setAuthors("Unknown 2; Unknown 3; Unknown 4");
        b.setIsbn("Unknown");
        b.setYear(0);
        b.setCopies(3);
        b.setCopiesLent(4);
        b.setTimesLent(12);
        
        assertEquals("Unknown 1", b.getTitle());
        assertEquals("Unknown 2; Unknown 3; Unknown 4", b.getAuthorsString());
        assertEquals("Unknown", b.getIsbn());
        assertEquals(0, b.getYear());
        assertEquals(3, b.getCopies());
        assertEquals(4, b.getCopiesLent());
        assertEquals(12, b.getTimesLent());        
    }


    /** 
     * @brief Tests that the edit method throws {@link poco.company.group01pocolib.exceptions.BookDataNotValidException BookDataNotValidException} when an invalid number of copies is provided.
     */
    @Test
    public void testBookEditInvalid(){
        List<String> authors = new ArrayList<>();
        authors.add("City Council");
        authors.add("Pennsylvania State University");
        Book b = new Book("General Plan for Phoenix, 1985-2000", authors, "316830724", 1985, 1);
        assertThrows(BookDataNotValidException.class, () -> {
            b.setCopies(-1);
        });
    }

    /**
     * @brief Tests the addCopy method to ensure copy count increases.
     */
    @Test
    public void testAddCopy() {
        Book b = new Book("General Plan for Phoenix, 1985-2000", "City Council", "316830724", 1985, 1);

        b.addCopy();
        assertEquals(2, b.getCopies());
    }

    /**
     * @brief Tests the removeCopy method to ensure copy count decreases when copies are available.
     */
    @Test
    public void testRemoveCopyValid() {
        Book b = new Book("General Plan for Phoenix, 1985-2000", "City Council", "316830724", 1985, 1);
                
        b.removeCopy();
        assertEquals(0, b.getCopies());
    }

    /**
     * @brief Tests that removeCopy throws an exception (IllegalStateException) when no copies are available.
     */
    @Test
    public void testRemoveCopyInvalid() {
        Book b = new Book("General Plan for Phoenix, 1985-2000", "City Council", "316830724", 1985, 0);
                
        assertThrows(IllegalStateException.class, () -> b.removeCopy());
    }
}