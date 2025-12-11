/**
 * @file BookTest.java
 * @brief Unit tests for the Book class.
 * @author Daniele Pepe
 */
package poco.company.group01pocolib.mvc.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

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
        assertEquals(b.getAuthorsString(), "City Council, Pennsylvania State University");
        assertEquals(b.getIsbn(), "316830724");
        assertEquals(b.getYear(), 1985);
        assertEquals(b.getCopies(), 1);
        assertEquals(b.getCopiesLent(), 0);
        assertEquals(b.getTimesLent(), 0);
    }

    /**
     * @brief Tests that the constructor throws IsbnNotValidException when an invalid ISBN is provided.
     */
    @Test
    public void testBookCreationInvalidIsbn() {
        // TODO: implementation
    }
    
    /**
     * @brief Tests the edit method to ensure book details are updated correctly. 
     */
    @Test
    public void testBookEdit(){
        // TODO: implementation
    }

    /**
     * @brief Tests the addCopy method to ensure copy count increases.
     */
    @Test
    public void testAddCopy() {
        // TODO: implementation
    }

    /**
     * @brief Tests the removeCopy method to ensure copy count decreases when copies are available.
     */
    @Test
    public void testRemoveCopyValid() {
        // TODO: implementation
    }

    /**
     * @brief Tests that removeCopy throws an exception (IllegalStateException) when no copies are available.
     */
    @Test
    public void testRemoveCopyInvalid() {
        // TODO: implementation
    }
}