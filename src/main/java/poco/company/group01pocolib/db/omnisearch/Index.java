package poco.company.group01pocolib.db.omnisearch;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private Map<String, List<T>> triMappings;

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
    public Map<String, List<T>> getTriMappings() {
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
     * @return  A list of generated trigrams.
     */
    public ArrayList<String> generateTrigrams(String searchableContent) {
        // TODO: implement
        return null;
    }

    /**
     * @brief   Adds an item to the index based on the provided searchable content.
     *
     * @param   searchableContent The content used to generate trigrams for indexing.
     * @param   item The item to be indexed.
     */
    public void add(String searchableContent, T item) {
        // TODO: implement
    }

    /**
     * @brief   Removes an item from the index.
     * @param   item The item to be removed from the index.
     */
    public void remove (T item) {
        // TODO: implement
    }
}
