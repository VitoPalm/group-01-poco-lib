package poco.company.group01pocolib.db.omnisearch;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * @class   Index
 * @brief   Represents a trigram index for fast searching of items based on a provided `String`.
 * @details This class allows adding, removing, and generating trigrams for searchable content associated with items of
 *          type `T`. The trigrams are stored in a map where each trigram maps to a list of items related to that
 *          trigram.
 *
 * @param   <T> The type of items to be indexed.
 */
public class Index<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Map<String, Set<T>> triMappings;

    // Character used to pad strings shorter than 3 characters after processing
    private static final String PADDING_CHAR = "\u0007";

    /**
     * @brief   Default constructor initializes the trigram mappings.
     */
    public Index() {
        this.triMappings = new HashMap<>();
    }

    /**
     * @brief   Get the trigram mappings.
     * @return  A map of trigrams to lists of items.
     */
    public Map<String, Set<T>> getTriMappings() {
        return triMappings;
    }

    /**
     * @brief   Generates trigrams from the provided searchable content.
     * @details A trigram is a contiguous sequence of three characters from a given string. This method extracts all
     *          possible trigrams from the input string and returns them as a list.
     *          <br><br>
     *          If the input string is shorter than three characters, it will generate all possible combinations of the
     *          Ngram available, that result into a trigram when adding bell characters as padding.
     *          <br><br>
     *          The method does some basic preprocessing, such as converting the string to lowercase, and trimming
     *          whitespace.
     *
     * @param   searchableContent The content to generate trigrams from.
     * @return  A `List` of generated trigrams. `null` if the input `String` is empty.
     */
    public List<String> generateTrigrams(String searchableContent) {
        String processedContent = searchableContent.toLowerCase().trim();

        ArrayList<String> trigrams = new ArrayList<>();

        if (processedContent.isEmpty()) {
            trigrams = null;
        } else if (processedContent.length() < 3) {
            StringBuilder output = new StringBuilder(processedContent);

            while (output.length() < 3) {
                output.append('\u0007');
            }

            trigrams.add(output.toString());
        } else {
            for (int i = 0; i < processedContent.length() - 2; i++) {
                trigrams.add(processedContent.substring(i, i+3));
            }
        }

        return trigrams;
    }

    /**
     * @brief   Adds an item to the index based on the provided searchable content.
     *
     * @param   searchableContent The content used to generate trigrams for indexing.
     * @param   item The item to be indexed.
     */
    public void add(String searchableContent, T item) {
        List<String> keys = generateTrigrams(searchableContent);
        if (keys == null) return;

        for (String key : keys) {
            // Only does one lookup on map
            triMappings.computeIfAbsent(key, k -> new HashSet<>()).add(item);
        }
    }

    /**
     * @brief   Removes an item from the index.
     * @param   item The item to be removed from the index.
     */
    public void remove(T item) {
        // Ngl, refactoring of classic iterator design was suggested by IDE
        triMappings.values().removeIf(
                set -> set.remove(item) && set.isEmpty());
    }

    /**
     * @brief   Removes an item from the index using searchableContent related to it.
     *
     * @param   searchableContent   The `String` associated with `item`.
     * @param   item                The item to be removed from the index.
     *
     * @warning If the `searchableContent` used to remove `item` isn't the same as the original one used to add `item`,
     *          `item` may not be completely be removed from the index. To make sure `item` is removed, use {@link
     *          poco.company.group01pocolib.db.omnisearch.Index.remove remove(T item)} instead.
     */
    public void fastRemove(String searchableContent, T item) {
        List<String> keys = generateTrigrams(searchableContent);

        for (String key : keys) {
            Set<T> items = triMappings.get(key);

            if (items != null) {
                items.remove(item);
                if (items.isEmpty()) {
                    triMappings.remove(key);
                }
            }
        }
    }
}
