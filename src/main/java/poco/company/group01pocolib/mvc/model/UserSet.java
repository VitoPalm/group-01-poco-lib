/**
 * @file UserSet.java
 * @brief This file contains the definition of the UserSet class, which represents a collection of users
 * @author Francesco Marino
 * @author Daniele Pepe
 */

package poco.company.group01pocolib.mvc.model;

import java.util.HashSet;
import java.util.Set;
import java.util.List;

/**
 * @class UserSet
 * @brief Represents a collection of User objects 
 */
public class UserSet {
    private Set<User> users;

    /**
     * @brief Default constructor
     * Initializes a new empty Set of User elements 
     */
    public UserSet(){
        this.users = new HashSet<>();
    }

    /**
     * @brief Adds a user to the set
     * @param user The user to add to the set
     */
    public void addUser(User user){
        //TODO: Implement add user method
    }

    /**
     * @brief Removes a user from the set
     * @param user The user to remove from the set
     */
    public void removeUser(User user){
        //TODO: Implement remove user method
    }

    /**
     * @brief Gets a user by its related ID
     * @param id The ID of the user to search for
     * @return The found user or null if the user is not found
     */
    public User getUserById(String id){
        //TODO: Implement get by id
        return null;
    }

    /**
     * @brief Sorts the Users lexicographically by surname and name
     * @return A list of users sorted by surname, then name
     */
    public List<User> sortedUsersByName(){
        // TODO: Implement
        return null;
    }

    /**
     * @brief Sorts the Users by their ID
     * @return A list of users sorted by ID
     */
    public List<User> sortedUsersById(){
        // TODO: Implement
        return null;
    }
}
