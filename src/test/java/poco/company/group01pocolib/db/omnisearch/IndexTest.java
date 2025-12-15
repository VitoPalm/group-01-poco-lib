/**
 * @file IndexTest.java
 * @brief Unit tests for the Index class.
 * @author Daniele Pepe
 * @author Francesco Marino
 */
package poco.company.group01pocolib.db.omnisearch;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

/**
 * @class IndexTest
 * @brief Contains unit tests for methods in Index class.
 */
public class IndexTest {

    /**
     * @brief Tests the generateNGrams method of the Index class.
     * @details It checks if the generated n-grams match the expected output for a given input string.
     */
    @Test
    public void testGenerateNGrams() {
        
        String input = "ciao mondo"; 
        Index<Object> index = new Index<>();
        List<String> ngrams = index.generateNGrams(input);
        
        // Expected n-grams for the input string
        List<String> expectedNGrams = Arrays.asList(
            "cia", "iao", "ao ", "o m", " mo", "mon", "ond", "ndo"
        );

        assertEquals(expectedNGrams.size(), ngrams.size());
        assertTrue(ngrams.containsAll(expectedNGrams));
    }

    /**
     * @brief Tests the generateNGrams method with an empty string.
     * @details It should return null for an empty input string.
     */
    @Test
    public void testGenerateNGrams_EmptyString() {
        String input = "";
         
        Index<Object> index = new Index<>();
        List<String> ngrams = index.generateNGrams(input);
        
        assertNull(ngrams);
    }

    /**
     * @brief Tests the generateNGrams method with a short string.
     * @details It should return at least one n-gram containing the input string.
     */
    @Test
    public void testGenerateNGrams_ShortString() {
        String input = "hi"; 
         
        Index<Object> index = new Index<>();
        List<String> ngrams = index.generateNGrams(input);
        
        assertTrue(ngrams.get(0).contains(input));
    }

    /**
     * @brief Tests the add method with basic searchable content.
     * @details Verifies that an item is correctly indexed and can be retrieved.
     */
    @Test
    public void testAddBasic() {
        // Create an Index for String items
        Index<String> index = new Index<>();
        
        // Add an item with searchable content
        index.add("tolkien", "libro1");
        
        // Verify the item was added to the index
        Map<String, Set<String>> mappings = index.getNGramMappings();
        
        // "tolkien" generates: "tol", "olk", "lki", "kie", "ien"
        assertTrue(mappings.containsKey("tol"));
        assertTrue(mappings.containsKey("olk"));
        assertTrue(mappings.containsKey("lki"));
        assertTrue(mappings.containsKey("kie"));
        assertTrue(mappings.containsKey("ien"));
        
        // Verify that the item is associated with the correct n-grams
        assertTrue(mappings.get("tol").contains("libro1"));
        assertTrue(mappings.get("olk").contains("libro1"));
        assertTrue(mappings.get("lki").contains("libro1"));
        assertTrue(mappings.get("kie").contains("libro1"));
        assertTrue(mappings.get("ien").contains("libro1"));

    }
    
    /**
     * @brief Tests add method with empty searchable content.
     * @details Should not add any mappings when content is empty.
     */
    @Test
    public void testAddEmptyContent() {
        Index<String> index = new Index<>();
        
        index.add("", "item1");
        
        Map<String, Set<String>> mappings = index.getNGramMappings();
        assertTrue(mappings.isEmpty());
    }

    /**
     * @brief Tests the remove method removes an item from all ngrams.
     * @details Verifies that an item is completely removed from the index.
     */
    @Test
    public void testRemoveBasic() {
        Index<String> index = new Index<>();
        
        index.add("tolkien", "libro1");
        index.remove("libro1");
        
        Map<String, Set<String>> mappings = index.getNGramMappings();
        
        // All sets should be empty or removed
        assertFalse(mappings.containsKey("tol") && mappings.get("tol").contains("libro1"));
        assertFalse(mappings.containsKey("olk") && mappings.get("olk").contains("libro1"));
        assertFalse(mappings.containsKey("lki") && mappings.get("lki").contains("libro1"));
        assertFalse(mappings.containsKey("kie") && mappings.get("kie").contains("libro1"));
        assertFalse(mappings.containsKey("ien") && mappings.get("ien").contains("libro1"));
    }   

    /**
     * @brief Tests remove with multiple items sharing ngrams.
     * @details Only the specified item should be removed, others remain.
     */
    @Test
    public void testRemoveWithSharedNGrams() {
        Index<String> index = new Index<>();
        
        index.add("tokien", "libro1");
        index.add("token", "libro2");  // Shares "tok" ngram
        
        index.remove("libro1");
        
        Map<String, Set<String>> mappings = index.getNGramMappings();
        
        // libro2 should still be in "tok"
        assertTrue(mappings.containsKey("tok"));
        assertTrue(mappings.get("tok").contains("libro2"));
        assertFalse(mappings.get("tok").contains("libro1"));
    }

    /**
     * @brief Tests the fastRemove method removes an item based on searchable content.
     * @details Verifies that an item is removed from the index using its searchable content.
     */
    @Test
    public void testFastRemove(){
        Index<String> index = new Index<>();
        
        index.add("tolkien", "libro1");
        index.add("token", "libro2");
        
        index.fastRemove("tolkien", "libro1");
        
        Map<String, Set<String>> mappings = index.getNGramMappings();
        
        // libro1 should be removed from its ngrams
        assertFalse(mappings.containsKey("tol") && mappings.get("tol").contains("libro1"));
        assertFalse(mappings.containsKey("olk") && mappings.get("olk").contains("libro1"));
        assertFalse(mappings.containsKey("lki") && mappings.get("lki").contains("libro1"));
        assertFalse(mappings.containsKey("kie") && mappings.get("kie").contains("libro1"));
        assertFalse(mappings.containsKey("ien") && mappings.get("ien").contains("libro1"));
        
        // libro2 should still exist
        assertTrue(mappings.containsKey("tok"));
        assertTrue(mappings.get("tok").contains("libro2"));
    }

}