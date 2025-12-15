/**
 * @file UserSet.java
 * @brief This file contains the definition of the UserSet class, which represents a collection of userSet
 * @author Francesco Marino
 * @author Daniele Pepe
 * @author Giovanni Orsini
 */

package poco.company.group01pocolib.mvc.model;

import poco.company.group01pocolib.db.DB;
import poco.company.group01pocolib.db.omnisearch.Index;
import poco.company.group01pocolib.db.omnisearch.Search;
import poco.company.group01pocolib.db.omnisearch.Search.*;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

import static java.lang.Math.abs;
import static poco.company.group01pocolib.db.omnisearch.Search.distance;

/**
 * @class   UserSet
 * @brief   A collection of userSet that acts as the real data interface for the main controller to interact with.
 * @details This class is the real data model for userSet for the MVC structure of this project. It allows object
 *          permanence between sessions through serialization, and allows searching through an indexed DB of books.
 *          <br><br>
 *          The fact that the collection of userSet is essentially memorized in three different data structures (the Set,
 *          the DB file, and the Index) may seem like a waste of resources, but it allows for some serious optimization
 *          of search operations, as well as a mechanism to recover the data in the case of the application being run on
 *          a different machine or after an update that may have corrupted the serialized data. (This is due to
 *          serialization requiring the structure of all classes involved to remain the same between sessions)
 */
public class UserSet implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Set<User> userSet;
    private Index<User> userIndex;
    private DB userDB;

    private User dummy;     ///< This attribute is used for fast searches based on ID

    private String lastKnownDBHash;
    private String DBPath;
    private String serializationPath;

    /**
     * @brief   Default constructor only initializes an empty collection of User elements
     */
    public UserSet(){
        this.userSet = new HashSet<>();
        this.userIndex = new Index<>();

        this.dummy = new User("12345", "Max", "Verstappen",  "v@l.id");
    }

    /**
     * @brief   Get the user set
     * @return  The set of users in the collection
     */
    public Set<User> getUserSet() {
        return userSet;
    }

    /**
     * @brief   Set the userSet Set
     * @param   userSet The Set of users to set userSet to
     */
    public void setUserSet(Set<User> userSet) {
        this.userSet = userSet;
    }

    /**
     * @brief   Get the user index
     * @return  The index of users in the collection
     */
    public Index<User> getUserIndex() {
        return userIndex;
    }

    /**
     * @brief   Set the user index
     * @param   userIndex The Index of users to set userIndex to
     */
    public void setUserIndex(Index<User> userIndex) {
        this.userIndex = userIndex;
    }

    /**
     * @brief   Get the user DB
     * @return  The DB of users in the collection
     */
    public DB getUserDB() {
        return userDB;
    }

    /**
     * @brief   Set the user DB
     * @param   userDB The DB of users to set userDB to
     */
    public void setUserDB(DB userDB) {
        this.userDB = userDB;
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
        this.lastKnownDBHash = this.userDB.updateAndGetDBFileHash();
    }

    /**
     * @brief   Get the DB path
     * @return  The path to the DB file
     */
    public String getDBPath() {
        return DBPath;
    }

    /**
     * @brief   Set the DB path
     * @param   DBPath The path to the DB file
     */
    public void setDBPath(String DBPath) {
        this.DBPath = DBPath;
    }

    /**
     * @brief   Get the path to the serialized version of the UserSet
     * @return  The path to the serialized version of the UserSet
     */
    public String getSerializationPath() {
        return serializationPath;
    }

    /**
     * @brief   Set to the path of the serialized version of the UserSet
     * @param   serializationPath The path to the serialized version of the UserSet
     */
    public void setSerializationPath(String serializationPath) {
        this.serializationPath = serializationPath;
    }

    /**
     * @brief   Get all the users in the collection as a List
     * @return  A `List` containing all the users in the collection
     */
    public List<User> getListOfUsers(){
        return new ArrayList<>(userSet);
    }

    /**
     * @brief   Loads a `UserSet` from a serialized version on disk
     * @details If the DB file has changed since the last serialization, the `UserSet` will be rebuilt from the DB
     *          file. Otherwise, it will be loaded as is. This includes re-hydrating the Index and the DB object itself.
     *          (This is done to avoid rebuilding the DB's internal Cache from scratch)
     *
     * @param   serializationPath The path to the serialized `UserSet`
     * @param   DBPath The path to the DB file
     * @return  The loaded `UserSet` object
     */
    public static UserSet loadFromSerialized(String serializationPath, String DBPath) {
        Object obj;
        UserSet userSet = null;

        // Attempt to read the serialized UserSet from disk
        try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(serializationPath)))) {
        
            obj = in.readObject();
            userSet = (UserSet) obj;
        // If any error occurs during deserialization, rebuild the UserSet from the DB
        } catch (IOException | ClassNotFoundException e) {
            userSet = new UserSet();
            userSet.setDBPath(DBPath);
            userSet.setSerializationPath(serializationPath);
            userSet.rebuildFromDB(DBPath);
            return userSet;
        }

        // Create a new DB object using the provided DB path
        DB currentDB = new DB(DBPath);
        String currentDBHash = currentDB.forceHashOnFile();

        // Check if the DB file has changed since the last serialization by comparing hashes
        if (currentDBHash.equals(userSet.getLastKnownDBHash())) {
            userSet.setDBPath(DBPath);
            userSet.setSerializationPath(serializationPath);
            userSet.setUserDB(currentDB);
            return userSet;
        } else {
            userSet.setDBPath(DBPath);
            userSet.setSerializationPath(serializationPath);
            userSet.rebuildFromDB(DBPath);
        }

        return userSet;
    }

    /**
     * @brief   Rebuilds the `UserSet` from the DB file
     * @param   DBPath The path to the DB file
     */
    public void rebuildFromDB(String DBPath) {
        // Check if file exists at specified path
        File dbFile = new File(DBPath);
        if (!dbFile.exists()) {
            // If the file does not exist, create it, initialize an empty DB, and UserSet
            try {
                Files.createDirectories(dbFile.getParentFile().toPath());
                dbFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.userDB = new DB(DBPath);
            this.userSet = new HashSet<>();
            this.userIndex = new Index<>();
            return;
        }

        // Initialize the DB object for rebuilding
        this.userDB = new DB(DBPath);

        // Clear in-memory data structures before reloading
        this.userSet.clear();
        this.userIndex = new Index<>(); 
        int i = 0;
        String line;

        // Iterate through each line in the DB file and parse it into a User object
        while ((line = this.userDB.readNthLine(i)) != null) {
            try {
                User user = User.fromDBString(line);
                
                this.userSet.add(user);
                this.userIndex.add(user.toSearchableString(), user);

            } catch (Exception e) {
                e.printStackTrace();
            }
            i++;
        }

        updateLastKnownDBHash();
    }

    /**
     * @brief   Adds a user to the collection. If the user already exists (based on ID), it is edited.
     * @param   user The user to add
     * @warning **Overwriting an object cannot be undone**: Be sure to call this method after checking if the object
     *          {@link poco.company.group01pocolib.mvc.model.UserSet#isStored isStored()} when only wishing to add a new
     *          User to the UserSet.
     */
    public void addOrEditUser(User user){
        // Removes the user if it already exists
        userSet.remove(user);
        userIndex.remove(user);
        
        // Adds the user (new or updated)
        userSet.add(user);
        userIndex.add(user.toSearchableString(), user);
        
        // Syncs the changes to DB and serialized file
        syncOnWrite();
    }

    /**
     * @brief   Removes a user from the set
     * @param   id The ID of the user to remove from the set
     */
    public void removeUser(String id){
        
        dummy.setId(id);
        
        // Removes the user from the set and index
        userSet.remove(dummy);
        userIndex.remove(dummy);
        
        // Syncs the changes to DB and serialized file
        syncOnWrite();
    }

    /**
     * @brief   Gets a user by its related ID
     * @param   id The ID of the user to search for
     * @return  The found user or null if the user is not found
     */
    public User getUser(String id) {
        dummy.setId(id);

        for (User user : userSet) {
            if (user.equals(dummy)) {
                return user;
            }
        }
        return null;
    }

    /**
     * @brief   Checks whether a user is already registered in the Set
     * @details The search is performed on the Collection used to store the UserSet. Two users are
     *          {@link poco.company.group01pocolib.mvc.model.User#equals equal} when they have the same unique
     *          identifier (`id`).
     * 
     * @param   user The user to search 
     * @return  Returns `true` if a result is found for the user's unique identifier
     * @author  Giovanni Orsini
     */
    public boolean isStored(User user) {
        return userSet.contains(user);
    }

    /**
     * @brief   Checks whether a user is already registered in the Set
     * @details The search is performed on the Collection used to store the UserSet. Two users are
     *          {@link poco.company.group01pocolib.mvc.model.Book#equals equal} when they have the same unique
     *          identifier (`id`).
     * 
     * @param   id The unique identifier of the user to search
     * @return  Returns `true` if a result is found for the user's unique identifier
     * @author  Giovanni Orsini
     */
    public boolean isStored(String id) {
        dummy.setId(id);
        return userSet.contains(dummy);
    }


    /**
     * @brief   Perform search on the indexed users with ranking
     *
     * @param   rawQuery The raw search query
     * @return  A list of users matching the search query, ranked by relevance
     */
    public List<SearchResult<User>> search(String rawQuery) {
        List<SearchResult<User>> rawSearchResults = Search.search(rawQuery, userIndex);

        if (rawSearchResults == null) return null;

        List<SearchResult<User>> rankedResults = new ArrayList<>();

        for (SearchResult<User> result : rawSearchResults) {
            int score = calculateScore(result.item, rawQuery) + result.hits*5;
            if (score >= 0) {
                rankedResults.add(new SearchResult<>(result.item, score));
            }
        }

        Collections.sort(rankedResults);

        // Expensive calculations for further ranking adjustments on best results
        for (int i = 0; i < rankedResults.size() && i < 20; i++) {
            String searchableString = rankedResults.get(i).item.toSearchableString();
            String processedQuery = rawQuery.trim();

            int distance = distance(searchableString, processedQuery);
            distance -= abs(processedQuery.length() - searchableString.length());

            // The higher the distance, the lower the match
            rankedResults.get(i).hits -= distance * 2;
        }

        // Only re-sort top 20 results after distance adjustment
        Collections.sort(rankedResults.subList(0, Math.min(20, rankedResults.size())));

        return rankedResults;
    }

    /**
     * @brief   Calculates relevance score of a user for a given search query (just used for exact matches of fields).
     *
     * @param   user The user to calculate the score for
     * @param   rawQuery The raw search query
     * @return  The relevance score of the user
     */
    public static int calculateScore(User user, String rawQuery) {
        if (user == null || rawQuery == null || rawQuery.isEmpty()) {
            return-1;
        }

        int score = 0;

        if (user.getId().equals(rawQuery))
            score += 1000;
        if (user.getName().equals(rawQuery))
            score += 500;
        if (user.getSurname().equals(rawQuery))
            score += 500;
        if (user.getEmail().equals(rawQuery))
            score += 200;

        return score;
    }

    private void syncOnWrite() {
        // Clear the DB file
        userDB.clear();
            
        // Write all users to the DB file
        for (User user : userSet) {
            userDB.appendLine(user.toDBString());
        }
        
        // Update the hash after writing to DB
        updateLastKnownDBHash();
        
        // Save the serialized version
        saveToSerialized();
    }

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
            e.printStackTrace();
        }
    }

    /**
     * @brief   Used to get number of entries in the current Set
     * @return  `userSet.size()`
     * @author  Giovanni Orsini
     */
    public int size() {
        return userSet.size();
    }

    /**
     * @brief   Returns a string representation of the UserSet.
     * @return  A string representation of the UserSet.
     */
    @Override
    public String toString() {
        StringBuffer buff = new StringBuffer();

        for (User user : userSet) {
            buff.append(user.toString()).append("\n");
        }
        
        return buff.toString();        
    }
}
