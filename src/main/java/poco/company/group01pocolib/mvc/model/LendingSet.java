/**
 * @file    LendingSet.java
 * @brief   This file contains the definition of the LendingSet class, which represents a collection of Lendings.
 * @author  Daniele Pepe
 */
package poco.company.group01pocolib.mvc.model;

import poco.company.group01pocolib.db.DB;
import poco.company.group01pocolib.db.omnisearch.Index;
import poco.company.group01pocolib.db.omnisearch.Search.*;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @class   LendingSet
 * @brief   A collection of lendings that acts as the real data interface for the main controller to interact with.
 * @details This class is the actual data model for lendings for the MVC structure of this project. It allows object
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

    private Lending dummy;         ///< Used in methods as a dummy object for the `contains()` method of the Collection

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
     * @param   DBPath  The path to the DB file
     * @param   bookSet The BookSet to link
     * @param   userSet The UserSet to link
     * @return  The loaded `LendingSet` object
     * @author  Giovanni Orsini
     */
    public static LendingSet loadFromSerialized(String serializationPath, String DBPath, BookSet bookSet, UserSet userSet) {
        Object obj;
        LendingSet lendingSet = null;

        // Attempt to read the serialized LendingSet from disk
        try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(serializationPath)))) {

            obj = in.readObject();
            lendingSet = (LendingSet) obj;

        // If any error occurs during deserialization, rebuild the LendingSet from the DB
        } catch (IOException | ClassNotFoundException e) {
            lendingSet = new LendingSet();
            lendingSet.setDBPath(DBPath);
            lendingSet.rebuildFromDB(DBPath, bookSet, userSet);
            return lendingSet;
        }

        // Create a new DB object using the provided DB path
        DB currentDB = new DB(DBPath);
        String currentDBHash = currentDB.forceHashOnFile();

        // Check if the DB file has changed since the last serialization by comparing hashes
        if (currentDBHash.equals(lendingSet.getLastKnownDBHash())) {
            lendingSet.setDBPath(DBPath);
            lendingSet.setLendingDB(currentDB);
            return lendingSet;
        } else {
            lendingSet.setDBPath(DBPath);
            lendingSet.rebuildFromDB(DBPath, bookSet, userSet);
        }

        return lendingSet;
    }

    /**
     * @brief   Rebuilds the `LendingSet` from the DB file
     *
     * @param   DBPath  The path to the DB file
     * @param   bookSet The bookset to link
     * @param   userSet The userset to link
     * @author  Giovanni Orsini
     */
    public void rebuildFromDB(String DBPath, BookSet bookSet, UserSet userSet) {
        // Check if file exists at specified path
        File dbFile = new File(DBPath);
        if (!dbFile.exists()) {
            // If the file does not exist, create it, initialize an empty DB, and LendingSet
            try {
                // Only create parent directories if they don't exist
                File parentFile = dbFile.getParentFile();
                if (parentFile != null) {
                    Files.createDirectories(parentFile.toPath());
                }
                dbFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.lendingDB = new DB(DBPath);
            this.lendingSet = new HashSet<>();
            this.lendingIndex = new Index<>();
            return;
        }

        //Initialize the DB object for rebuilding
        this.lendingDB = new DB(DBPath);

        // Clear in-memory data structures before reloading
        this.lendingSet.clear();
        this.lendingIndex = new Index<>();

        int i = 0;
        String line;

        // Iterate through each line in the DB file and parse it into a Lending object
        while ((line = this.lendingDB.readNthLine(i)) != null) {
            try {

                Lending lending = Lending.fromDBString(line, bookSet, userSet);

                this.lendingSet.add(lending);
                this.lendingIndex.add(lending.toSearchableString(), lending);

            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            i++;
        }

        // Update the hash to indicate that we are synchronized with the DB
        updateLastKnownDBHash();
    }

    /**
     * @brief   Adds a Lending to the collection. If the Lending already exists (based on lending ID), it is edited.
     * @param   lending The Lending object to add or edit.
     */
    public void addOrEditLending(Lending lending){
        // Removes the lending if it already exists
        lendingSet.remove(lending);
        lendingIndex.remove(lending);

        // Adds the lending (new or updated)
        lendingSet.add(lending);
        lendingIndex.add(lending.toSearchableString(), lending);

        // Syncs the changes to DB and serialized file
        syncOnWrite();
    }

    /**
     * @brief   Removes a Lending from the LendingSet.
     * @param   lending The Lending to remove.
     */
    public void removeLending(Lending lending){
        lendingSet.remove(lending);
        lendingIndex.remove(lending);

        // Syncs the changes to DB and serialized file
        syncOnWrite();
    }

    /**
     * @brief   Gets a Lending by its ID.
     *
     * @param   id The ID of the Lending to retrieve.
     * @return  The Lending with the specified ID, null otherwise.
     */
    public Lending getLending(int id){
        // TODO: Implement using a more efficient method than linear search (pls let me use HashMap)
        for (Lending lending : lendingSet) {
            if (lending.getLendingId() == id) {
                return lending;
            }
        }
        return null;
    }

    /**
     * @brief   Perform search on the indexed lendings with ranking
     *
     * @param   rawQuery The raw search query
     * @return  A list of lendings matching the search query, ranked by relevance
     */
    public List<SearchResult<Lending>> search(String rawQuery) {
        // TODO: Implement search
        // Temporary implementation: return empty list instead of null to avoid NullPointerException
        return new ArrayList<>();
    }

    /**
     * @brief   Perform search on the indexed lendings without ranking
     *
     * @param   rawQuery The raw search query
     * @return  A list of lendings matching the search query
     */
    private List<SearchResult<Lending>> rawSearch(String rawQuery) {
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
     * @author  Giovanni Orsini
     */
    private void syncOnWrite() {
        // Clear the DB file
        lendingDB.clear();

        // Write all lendings to the DB file
        for (Lending lending : lendingSet) {
            lendingDB.appendLine(lending.toDBString());
        }

        // Update the hash after writing to DB
        updateLastKnownDBHash();

        // Save the serialized version
        saveToSerialized();
    }

    /**
     * @brief   Saves the current state of the LendingSet to a serialized file on disk
     * @author  Giovanni Orsini
     */
    private void saveToSerialized() {
        if (serializationPath == null || serializationPath.isEmpty()) {
            return;
        }
        // Check if file exists at specified path
        File serializedFile = new File(serializationPath);
        if (!serializedFile.exists()) {
            // If the file or the folder structure does not exist, create it
            try {
                // Only create parent directories if they don't exist
                File parentFile = serializedFile.getParentFile();
                if (parentFile != null) {
                    Files.createDirectories(parentFile.toPath());
                }
                serializedFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(serializationPath)))) {
            out.writeObject(this);

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * @brief   Used to get number of entries in the current Set
     * @return  `lendingSet.size()`
     * @author  Giovanni Orsini
     */
    public int size() {
        return lendingSet.size();
    }

    /**
     * @brief   Returns a string representation of the LendingSet.
     * @return  A string representation of the LendingSet.
     * @author  Giovanni Orsini
     */
    @Override
    public String toString() {
        StringBuffer buff = new StringBuffer();

        for (Lending lending : lendingSet) {
            buff.append(lending.toString()).append("\n");
        }

        return buff.toString();
    }
}