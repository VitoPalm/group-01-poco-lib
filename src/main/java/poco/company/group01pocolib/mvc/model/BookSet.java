/**
 * @file BookSet.java
 * @brief This file contains the definition of BookSet class
 * @author Francesco Marino
 */

package poco.company.group01pocolib.mvc.model;

import poco.company.group01pocolib.db.DB;
import poco.company.group01pocolib.db.omnisearch.Index;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * @class   BookSet
 * @brief   A collection of books that acts as the real data interface for the main controller to interact with.
 * @details This class is the real data model for books for the MVC structure of this project. It allows object
 *          permanence between sessions through serialization, and allows searching through an indexed DB of books.
 *          <br><br>
 *          The fact that the collection of books is essentially memorized in three different data structures (the Set,
 *          the DB file, and the Index) may seem like a waste of resources, but it allows for some serious optimization
 *          of search operations, as well as a mechanism to recover the data in the case of the application being run on
 *          a different machine or after an update that may have corrupted the serialized data. (This is due to
 *          serialization requiring the structure of all classes involved to remain the same between sessions)
 */
public class BookSet implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private HashSet<Book> bookSet;
    private Index<Book> bookIndex;
    private DB bookDB;

    private String lastKnownDBHash;
    private String DBPath;
    private String serializationPath;
    
    /**
     * @brief   Default constructor only initializes an empty collection of `Book` elements
     */
    public BookSet(){
        this.bookSet = new HashSet<>();
        this.bookIndex = new Index<>();
    }

    /**
     * @brief   Get the book set
     * @return  The `Set` of books in the collection
     */
    public Set<Book> getBookSet() {
        return bookSet;
    }

    /**
     * @brief   Set the `bookSet` `Set`
     * @param   bookSet The `Set` of books to set `bookSet` to
     */
    public void setBookSet(HashSet<Book> bookSet) {
        this.bookSet = bookSet;
    }

    /**
     * @brief   Get the book index
     * @return  The Index object for the books
     */
    public Index<Book> getBookIndex() {
        return bookIndex;
    }

    /**
     * @brief   Set the book index
     * @param   bookIndex The Index object to set `bookIndex` to
     */
    public void setBookIndex(Index<Book> bookIndex) {
        this.bookIndex = bookIndex;
    }

    /**
     * @brief   Get the DB object
     * @return  The DB object
     */
    public DB getBookDB() {
        return bookDB;
    }

    /**
     * @brief   Set the DB object
     * @param   bookDB The new DB object to set `bookDB` to
     */
    public void setBookDB(DB bookDB) {
        this.bookDB = bookDB;
    }

    /**
     * @brief   Get the last known DB hash
     * @return  The last known DB hash
     */
    public String getLastKnownDBHash() {
        return lastKnownDBHash;
    }

    /**
     * @brief   Updates the `lastKnownDBHash` field by rehashing the current DB
     */
    public void updateLastKnownDBHash() {
        this.lastKnownDBHash = this.bookDB.updateAndGetDBFileHash();
    }

    /**
     * @brief   Get the path to the DB file
     * @return  The path to the DB file
     */
    public String getDBPath() {
        return DBPath;
    }

    /**
     * @brief   Set the path to the DB file
     * @param   DBPath The path to the DB file
     */
    public void setDBPath(String DBPath) {
        this.DBPath = DBPath;
    }

    /**
     * @brief   Get the path to the serialized version of the BookSet
     * @return  The path to the serialized version of the BookSet
     */
    public String getSerializationPath() {
        return serializationPath;
    }

    /**
     * @brief   Set to the path of the serialized version of the BookSet
     * @param   serializationPath The path to the serialized version of the BookSet
     */
    public void setSerializationPath(String serializationPath) {
        this.serializationPath = serializationPath;
    }

    /**
     * @brief   Get all the books in the collection as a list
     * @return  A `List` of all the books in the collection
     */
    public List<Book> getListOfBooks() {
        return new ArrayList<>(bookSet);
    }

    /**
     * @brief   Loads a `BookSet` from a serialized version on disk
     * @details If the DB file has changed since the last serialization, the `BookSet` will be rebuilt from the DB
     *          file. Otherwise, it will be loaded as is. This includes re-hydrating the Index and the DB object itself.
     *          (This is done to avoid rebuilding the DB's internal Cache from scratch)
     *
     * @param   serializationPath The path to the serialized `BookSet`
     * @param   DBPath The path to the DB file
     * @return  The loaded `BookSet` object
     */
    public static BookSet loadFromSerialized(String serializationPath, String DBPath) {
        // TODO: Implement
        return null;
    }

    /**
     * @brief   Rebuilds the `BookSet` from the DB file
     * @param   DBPath The path to the DB file
     */
    public void rebuildFromDB(String DBPath) {
        // TODO: Implement
    }

    /**
     * @brief   Adds a book to collection. If the book already exists (based on ISBN), it is edited.
     * @param   book The book object to add or edit.
     */
    public void addOrEditBook(Book book){
        // TODO: Implement book add
    }

    /**
     * @brief   Remove a book from the collection
     * @param   isbn The ISBN of the book to remove
     */
    public void removeBook(String isbn){
        // TODO: Implement book remove
    } 

    /**
     * @brief   Get a book from its ISBN
     *
     * @param   isbn The ISBN of the book to get
     * @return  The Book object if found, null otherwise
     */
    public Book getBook(String isbn){
        // TODO: Implement book getter
        return null;
    }

    /**
     * @brief   Perform search on the indexed books with ranking
     *
     * @param   rawQuery The raw search query
     * @return  A list of books matching the search query, ranked by relevance
     */
    public List<Book> search(String rawQuery) {
        // TODO: Implement search
        return null;
    }

    /**
     * @brief   Perform search on the indexed books without ranking
     *
     * @param   rawQuery The raw search query
     * @return  A list of books matching the search query
     */
    private List<Book> rawSearch(String rawQuery) {
        // TODO: Implement search
        return null;
    }

    /**
     * @brief   Calculate the relevance score of a book for a given search query
     *
     * @param   book The book to calculate the score for
     * @param   rawQuery The raw search query
     * @return  The relevance score of the book
     */
    private int calculateScore(Book book, String rawQuery) {
        // TODO: Implement score calculation
        return 0;
    }

    /**
     * @brief   Synchronizes the current state of the BookSet to the DB and serialized file on write operations
     */
    private void syncOnWrite() {
        // TODO: Implement
    }

    /**
     * @brief   Saves the current state of the BookSet to a serialized file on disk
     */
    private void saveToSerialized() {
        // TODO: Implement
    }

    /**
     * @brief   Returns a string representation of the BookSet.
     * @return  A string representation of the BookSet.
     */
    @Override
    public String toString() {
        // TODO: implement
        return "";
    }
}