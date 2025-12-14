package poco.company.group01pocolib.db.omnisearch;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * @class   Index
 * @brief   Represents an ngram index for fast searching of items based on a provided `String`.
 * @details This class allows adding, removing, and generating ngrams for searchable content associated with items of
 *          type `T`. The ngrams are stored in a map where each ngram maps to a list of items related to that
 *          ngram.
 *
 * @param   <T> The type of items to be indexed.
 */
public class Index<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Map<String, Set<T>> ngramMappings;

    // Ngram size used for indexing
    public static final int NGRAM_SIZE = 3;
    // Character used to pad strings shorter than 3 characters after processing
    public static final String PADDING_CHAR = "\u0007";

    /**
     * @brief   Default constructor initializes the ngram mappings.
     */
    public Index() {
        this.ngramMappings = new HashMap<>();
    }

    /**
     * @brief   Get the ngram mappings.
     * @return  A map of ngrams to lists of items.
     */
    public Map<String, Set<T>> getNGramMappings() {
        return ngramMappings;
    }

    /**
     * @brief   Generates ngrams from the provided searchable content.
     * @details An ngram is a contiguous sequence of three characters from a given string. This method extracts all
     *          possible ngrams from the input string and returns them as a list.
     *          <br><br>
     *          If the input string is shorter than `NGRAM_SIZE` characters, it will add padding characters (defined by
     *          `PADDING_CHAR`) to the end of the string until it reaches a length of `NGRAM_SIZE`.
     *          <br><br>
     *          The method trims leading and trailing whitespace before generating ngrams.
     *
     * @param   searchableContent The content to generate ngrams from.
     * @return  A `List` of generated ngrams. `null` if the input `String` is empty.
     */
    public List<String> generateNGrams(String searchableContent) {
        String processedContent = searchableContent.trim();

        ArrayList<String> ngrams = new ArrayList<>();

        if (processedContent.isEmpty()) {
            ngrams = null;
        } else if (processedContent.length() < NGRAM_SIZE) {
            StringBuilder output = new StringBuilder(processedContent);

            while (output.length() < NGRAM_SIZE) {
                output.append(PADDING_CHAR);
            }

            ngrams.add(output.toString());
        } else {
            for (int i = 0; i < processedContent.length() - NGRAM_SIZE + 1; i++) {
                ngrams.add(processedContent.substring(i, i+NGRAM_SIZE));
            }
        }

        return ngrams;
    }

    /**
     * @brief   Adds an item to the index based on the provided searchable content.
     *
     * @param   searchableContent The content used to generate ngrams for indexing.
     * @param   item The item to be indexed.
     */
    public void add(String searchableContent, T item) {
        List<String> keys = generateNGrams(searchableContent);
        if (keys == null) return;

        for (String key : keys) {
            // Only does one lookup on map
            ngramMappings.computeIfAbsent(key, k -> new HashSet<>()).add(item);
        }
    }

    /**
     * @brief   Removes an item from the index.
     * @param   item The item to be removed from the index.
     */
    public void remove(T item) {
        // Ngl, refactoring of classic iterator design was suggested by IDE
        ngramMappings.values().removeIf(
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
     *          poco.company.group01pocolib.db.omnisearch.Index#remove remove(T item)} instead.
     */
    public void fastRemove(String searchableContent, T item) {
        List<String> keys = generateNGrams(searchableContent);
        if (keys == null) return;

        for (String key : keys) {
            Set<T> items = ngramMappings.get(key);

            if (items != null) {
                items.remove(item);
                if (items.isEmpty()) {
                    ngramMappings.remove(key);
                }
            }
        }
    }
}
