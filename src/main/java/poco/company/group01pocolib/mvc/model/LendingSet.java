/**
 * @file LendingSet.java
 * @brief This file contains the definition of the LendingSet class, which represents a collection of Lendings.
 * @author Daniele Pepe
 */
package poco.company.group01pocolib.mvc.model;

import poco.company.group01pocolib.db.DB;
import poco.company.group01pocolib.db.omnisearch.Index;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @class   LendingSet
 * @brief   A collection of lendings that acts as the real data interface for the main controller to interact with.
 * @details This class is the real data model for lendings for the MVC structure of this project. It allows object
 *          permanence between sessions through serialization, and allows searching through an indexed DB of lendings.
 *          <br><br>
 *          The fact that the collection of lendings is essentially memorized in three different data structures (the
 *          Set, the DB file, and the Index) may seem like a waste of resources, but it allows for some serious
 *          optimization of search operations, as well as a mechanism to recover the data in the case of the application
 *          being run on a different machine or after an update that may have corrupted the serialized data. (This is
 *          due to serialization requiring the structure of all classes involved to remain the same between sessions)
 */
public class LendingSet implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Set<Lending> lendingSet;
    private Index<Lending> lendingIndex;
    private DB lendingDB;

    private String lastKnownDBHash;
    private String DBPath;
    private String serializationPath;

    /**
     * @brief Default constructor only initializes an empty collection of `Lending` elements
     */
    public LendingSet(){
        this.lendingSet = new HashSet<>();
        this.lendingIndex = new Index<>();
    }

    /**
     * @brief   Get the lending set
     * @return  The `Set` of lendings in the collection
     */
    public Set<Lending> getLendingSet() {
        return lendingSet;
    }

    /**
     * @brief   Set the `lendingSet` `Set`
     * @param   lendingSet The `Set` of lendings to set `lendingSet` to
     */
    public void setLendingSet(HashSet<Lending> lendingSet) {
        this.lendingSet = lendingSet;
    }

    /**
     * @brief   Get the lending index
     * @return  The Index object for the lendings
     */
    public Index<Lending> getLendingIndex() {
        return lendingIndex;
    }

    /**
     * @brief   Set the lending index
     * @param   lendingIndex The Index object to set `lendingIndex` to
     */
    public void setLendingIndex(Index<Lending> lendingIndex) {
        this.lendingIndex = lendingIndex;
    }

    /**
     * @brief   Get the DB object
     * @return  The DB object
     */
    public DB getLendingDB() {
        return lendingDB;
    }

    /**
     * @brief   Set the DB object
     * @param   lendingDB The DB object to set `lendingDB` to
     */
    public void setLendingDB(DB lendingDB) {
        this.lendingDB = lendingDB;
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
        this.lastKnownDBHash = this.lendingDB.updateAndGetDBFileHash();
    }

    /**
     * @brief   Get the DB path
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
     * @brief   Get the path to the serialized version of LendingSet
     * @return  The path to the serialized LendingSet file
     */
    public String getSerializationPath() {
        return serializationPath;
    }

    /**
     * @brief   Set to the path of the serialized version of the LendingSet
     * @param   serializationPath The path to the serialized version of the LendingSet
     */
    public void setSerializationPath(String serializationPath) {
        this.serializationPath = serializationPath;
    }

    /**
     * @brief   Get all the lendings in the collection as a list
     * @return  A list of all the lendings in the collection
     */
    public List<Lending> getAllLendingsAsList() {
        return new ArrayList<>(lendingSet);
    }

    /**
     * @brief   Loads a `LendingSet` from a serialized version on disk
     * @details If the DB file has changed since the last serialization, the `LendingSet` will be rebuilt from the DB
     *          file. Otherwise, it will be loaded as is. This includes re-hydrating the Index and the DB object itself.
     *          (This is done to avoid rebuilding the DB's internal Cache from scratch)
     *
     * @param   serializationPath The path to the serialized `LendingSet`
     * @param   DBPath The path to the DB file
     * @return  The loaded `LendingSet` object
     */
    public static LendingSet loadFromSerialized(String serializationPath, String DBPath) {
        // TODO: Implement
        return null;
    }

    /**
     * @brief   Rebuilds the `LendingSet` from the DB file
     * @param   DBPath The path to the DB file
     */
    public void rebuildFromDB(String DBPath) {
        // TODO: Implement
    }

    /**
     * @brief   Adds a Lending to the collection. If the Lending already exists (based on lending ID), it is edited.
     * @param   lending The Lending object to add or edit.
     */
    public void addOrEditLending(Lending lending){
        if (lendingSet.contains(lending)) {
            lendingSet.remove(lending);
            lendingSet.add(lending);
        } else {
            lendingSet.add(lending);
        }
    }

    /**
     * @brief   Removes a Lending from the LendingSet.
     * @param   lendingID The ID of the lending to remove.
     */
    public void removeLending(int lendingID){
        // TODO: implement
    }

    /**
     * @brief   Gets a Lending by its ID.
     *
     * @param   id The ID of the Lending to retrieve.
     * @return  The Lending with the specified ID, null otherwise.
     */
    public Lending getLending(int id){
        // TODO: implement
        return null;
    }

    /**
     * @brief   Gets all Lendings of a specific User.
     *
     * @param   user The User whose Lendings to retrieve.
     * @return  A collection of Lendings associated with the specified User.
     */
    public Set<Lending> getByUser(User user){
        // TODO: implement
        return null;
    }

    /**
     * @brief   Gets all Lendings of a specific Book.
     *
     * @param   book The Book whose Lendings to retrieve.
     * @return  A collection of Lendings associated with the specified Book.
     */
    public Set<Lending> getByBook(Book book){
        // TODO: implement
        return null;
    }

    /**
     * @brief   Perform search on the indexed lendings with ranking
     *
     * @param   rawQuery The raw search query
     * @return  A list of lendings matching the search query, ranked by relevance
     */
    public List<Lending> search(String rawQuery) {
        // TODO: Implement search
        return null;
    }

    /**
     * @brief   Perform search on the indexed lendings without ranking
     *
     * @param   rawQuery The raw search query
     * @return  A list of lendings matching the search query
     */
    private List<Lending> rawSearch(String rawQuery) {
        // TODO: Implement search
        return null;
    }

    /**
     * @brief   Calculate the relevance score of a lending for a given search query
     *
     * @param   lending The lending to calculate the score for
     * @param   rawQuery The raw search query
     * @return  The relevance score of the lending
     */
    private int calculateScore(Lending lending, String rawQuery) {
        // TODO: Implement score calculation
        return 0;
    }

    /**
     * @brief   Synchronizes the current state of the LendingSet to the DB and serialized file on write operations
     */
    private void syncOnWrite() {
        // TODO: Implement
    }

    /**
     * @brief   Saves the current state of the LendingSet to a serialized file on disk
     */
    private void saveToSerialized() {
        // TODO: Implement
    }

    /**
     * @brief   Returns a string representation of the LendingSet.
     * @return  A string representation of the LendingSet.
     */
    @Override
    public String toString() {
        // TODO: implement
        return "";
    }
}