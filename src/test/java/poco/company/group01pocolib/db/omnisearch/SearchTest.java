/**
 * @file SearchTest.java
 * @brief Unit tests for the Search class.
 * @author Daniele Pepe
 * @author Francesco Marino
 */
package poco.company.group01pocolib.db.omnisearch;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import poco.company.group01pocolib.db.omnisearch.Search.SearchResult;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @class SearchTest
 * @brief Contains unit tests for all methods in the Search class.
 */
class SearchTest {
    
    /**
     * @brief Tests the distance method with identical strings.
     * @details The distance between identical strings should be zero.
     */
    @Test
    public void testDistanceIdenticalStrings() {

        String str1 = "poco";
        String str2 = "poco";

        // Calculate the distance between the two identical strings
        int distance = Search.distance(str1, str2);

        // Verify that the distance is zero for identical strings
        assertEquals(0, distance);
    }

    /**
     * @brief Tests the distance method with completely different strings.
     * @details Checks that the distance is calculated correctly for two different strings.
     */
    @Test
    public void testDistanceDifferentStrings() {
        
        String str1 = "poco";
        String str2 = "library";

        // Calculate the distance between the two different strings
        int distance = Search.distance(str1, str2);

        // Verify that the distance is greater than zero for different strings
        assertTrue(distance != 0);
    }

    /**
     * @brief Tests the distance method for case sensitivity.
     * @details Ensures that the distance calculation is affected by letter casing.
     * @note The actual behavior of the search distance is case sensitive, so "Poco" and "poco" should yield a non-zero distance. In our implementation we ignore case sensitivity by converting strings to lower case before inserting them in the database.
     */
    @Test
    public void testDistanceCaseSensitivity() {
        
        String str1 = "Poco";
        String str2 = "poco";

        // Calculate the distance between the two strings with different cases
        int distance = Search.distance(str1, str2);

        // Verify that the distance is zero, indicating case insensitivity
        assertNotEquals(0, distance);
    }

    /**
     * @brief Tests the orderByDistance method.
     * @details Verifies that a list of strings is correctly ordered by their distance to a target string.
     */
    @Test
    public void testOrderByDistance() {
        String target = "poco";
        List<String> strings = new ArrayList<>();
        strings.add("poco");
        strings.add("poco library");
        strings.add("library");
        strings.add("poco lib");

        // Order the list by distance to the target string
        Search.orderByDistance(target, strings);

        // Verify that the list is ordered correctly
        assertEquals("poco", strings.get(0));
        assertEquals("poco lib", strings.get(1));
        assertEquals("library", strings.get(2));
        assertEquals("poco library", strings.get(3));
    }

    /**
     * @brief Tests the search method.
     * @details Validates the search functionality with a sample input.
     */
    @Test
    public void testSearch() {
        // Create a sample index and add some test data
        Index<String> index = new Index<>();
        String obj1 = "res1";
        String obj2 = "res2";
        String obj3 = "res3";
        String obj4 = "res4";

        index.add("poco", obj1);
        index.add("poco library", obj2);
        index.add("library", obj3);
        index.add("poco lib", obj4);

        // Perform a search
        String query = "poco";
        ArrayList<SearchResult<String>> results = Search.search(query, index);

        // Verify the search results
        assertEquals(obj1, results.get(0).item);
        assertEquals(obj2, results.get(1).item);
        assertEquals(obj4, results.get(2).item);

    }
}