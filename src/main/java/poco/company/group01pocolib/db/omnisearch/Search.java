package poco.company.group01pocolib.db.omnisearch;

import java.util.*;

import static java.lang.Math.min;

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
    public static class SearchResult<T> implements Comparable<SearchResult<T>> {
        public T item;
        public int hits;

        public SearchResult(T item, int hits) {
            this.item = item;
            this.hits = hits;
        }

        /**
         * @brief   Overrides the equals method to compare SearchResult objects based on their items.
         * @param   obj   the reference object with which to compare.
         * @return  `true` if this object is the same as the obj argument; `false` otherwise.
         */
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;

            SearchResult<?> other = (SearchResult<?>) obj;
            return item.equals(other.item);
        }

        /**
         * @brief   Custom comparator for SearchResult objects based on hits.
         *
         * @param   other The other SearchResult object to compare with.
         * @return  A negative integer, zero, or a positive integer as this object has less than, equal to, or greater
         *          hits than the specified object.
         */
        @Override
        public int compareTo(SearchResult<T> other) {
            if (this.equals(other)) return 0;

            if (this.hits == other.hits) {
                return this.item.toString().compareTo(other.item.toString());
            }

            return Integer.compare(other.hits, this.hits); // Descending order
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
        String processedQuery = query.toLowerCase().trim();

        if (processedQuery.isEmpty() || index == null) {
            return null;
        }

        // Generate ngrams from the processed query
        List<String> ngrams = index.generateNGrams(processedQuery);

        if (ngrams == null) {
            return null;
        }

        // Map to store items and their hit counts
        Map<T, SearchResult<T>> hitCounts = new HashMap<>();

        for (String ngram : ngrams) {
            // Retrieve set of items for the current ngram
            Set<T> items = index.getNGramMappings().get(ngram);

            // If items are found in the index
            if (items != null) {
                for (T item : items) {
                    // Update hit count for the item, adding it if not already present
                    hitCounts.putIfAbsent(item, new SearchResult<>(item, 0));
                    hitCounts.get(item).hits++;
                }
            }
        }

        // Convert the hit counts map to a list and sort it by hits
        ArrayList<SearchResult<T>> results = new ArrayList<>(hitCounts.values());
        Collections.sort(results);

        return results;
    }

    /**
     * @author  Thibault Debatty
     * @brief   Calculates the unrestricted Damerau-Levenshtein distance between two strings.
     * @details Original implementation at <a href="https://github.com/tdebatty/java-string-similarity/blob/master/src/main/java/info/debatty/java/stringsimilarity/Damerau.java">this</a> link.
     *
     * @param   s1 The first string.
     * @param   s2 The second string.
     * @return  The distance between the two strings as an integer.
     */
    public static int distance(final String s1, final String s2) {
        if (s1 == null || s2 == null) {
            return -1;
        }

        if (s1.equals(s2)) {
            return 0;
        }

        // INFinite distance is the max possible distance
        int inf = s1.length() + s2.length();

        // Create and initialize the character array indices
        HashMap<Character, Integer> da = new HashMap<Character, Integer>();

        for (int d = 0; d < s1.length(); d++) {
            da.put(s1.charAt(d), 0);
        }

        for (int d = 0; d < s2.length(); d++) {
            da.put(s2.charAt(d), 0);
        }

        // Create the distance matrix H[0 .. s1.length+1][0 .. s2.length+1]
        int[][] h = new int[s1.length() + 2][s2.length() + 2];

        // initialize the left and top edges of H
        for (int i = 0; i <= s1.length(); i++) {
            h[i + 1][0] = inf;
            h[i + 1][1] = i;
        }

        for (int j = 0; j <= s2.length(); j++) {
            h[0][j + 1] = inf;
            h[1][j + 1] = j;

        }

        // fill in the distance matrix H
        // look at each character in s1
        for (int i = 1; i <= s1.length(); i++) {
            int db = 0;

            // look at each character in b
            for (int j = 1; j <= s2.length(); j++) {
                int i1 = da.get(s2.charAt(j - 1));
                int j1 = db;

                int cost = 1;
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    cost = 0;
                    db = j;
                }

                h[i + 1][j + 1] = min(
                        h[i][j] + cost, // substitution
                        min(h[i + 1][j] + 1, // insertion
                        min(h[i][j + 1] + 1, // deletion
                        h[i1][j1] + (i - i1 - 1) + 1 + (j - j1 - 1))));
            }

            da.put(s1.charAt(i - 1), i);
        }

        return h[s1.length() + 1][s2.length() + 1];
    }

    /**
     * @brief   Orders a list of strings based on their distance from the query string.
     * @details Uses the Damerau-Levenshtein distance to measure how similar each string in the input list is to the
     *          query string. The original string is used to calculate the distance without any preprocessing.
     *
     * @param   query The reference string to compare against.
     * @param   input The list of strings to be ordered.
     */
    public static void orderByDistance(String query, ArrayList<String> input) {
        if (query == null || input == null) return;

        input.sort(Comparator.comparingInt(s -> distance(query, s)));
    }
}
