package poco.company.group01pocolib.db.omnisearch;

import java.util.ArrayList;

/**
 * @class   Search
 * @brief   A class providing static methods that act as utilities for searching and string distance calculations.
 * @details This class includes methods to search an index, calculate the distance between two strings,
 *          and order a list of strings based on their distance from a query string.
 */
public class Search {
    /**
     * @brief   Inner class representing a search result with the associated number of hits on the index.
     */
    public static class SearchResult<T> {
        public T item;
        public int hits;

        public SearchResult(T item, int hits) {
            this.item = item;
            this.hits = hits;
        }
    }

    /**
     * @brief   Searches the given index for items matching the query. It returns a list of SearchResult objects,
     *          each containing an item and the number of hits it had in the index.
     *
     * @param   query The search query string.
     * @param   index The index to search within.
     * @return  A list of SearchResult objects containing the items found and their hit counts.
     */
    public static <T> ArrayList<SearchResult<T>> search(String query, Index<T> index) {
        // TODO: implement
        return null;
    }

    /**
     * @brief   Calculates the distance between two strings.
     *
     * @param   s1 The first string.
     * @param   s2 The second string.
     * @return  The distance between the two strings as an integer.
     */
    public static int distance(String s1, String s2) {
        // TODO: implement
        return 0;
    }

    /**
     * @brief   Orders a list of strings based on their distance from the query string.
     *
     * @param   query The reference string to compare against.
     * @param   input The list of strings to be ordered.
     * @return  A new list of strings ordered by their distance from the query string.
     */
    public static ArrayList<String> orderByDistance(String query, ArrayList<String> input) {
        // TODO: implement
        return null;
    }
}
