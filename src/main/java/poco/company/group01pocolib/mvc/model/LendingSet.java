/**
 * @file LendingSet.java
 * @brief This file contains the definition of the LendingSet class, which represents a collection of Lendings.
 * @author Daniele Pepe
 */
package poco.company.group01pocolib.mvc.model;

import java.time.LocalDate;
import java.util.Collection;

/**
 * @class LendingSet
 * @brief Represents a collection of Lendings.
 */
public class LendingSet {
    private Collection<Lending> lendings;
    /**
     * @brief Constructs a new LendingSet object.
     */
    public LendingSet(){
        // TODO: implement
    }

    /** @brief Adds a Lending to the LendingSet.
     * @param lending The Lending to add.
    */
    public void add(Lending lending){
        // TODO: implement
    }

    /** @brief Removes a Lending from the LendingSet.
     * @param lending The Lending to remove.
    */
    public void remove(Lending lending){
        // TODO: implement
    }

    /** @brief Gets a Lending by its ID.
     * @param id The ID of the Lending to retrieve.
     * @return The Lending with the specified ID, or null if not found.
     */
    public Lending getById(int id){
        //TODO: implement
        return null;
    }

    /** @brief Gets all Lendings of a specific User. 
     * @param user The User whose Lendings to retrieve.
     * @return A collection of Lendings associated with the specified User.
     */
    public Collection<Lending> getByUser(User user){
        // TODO: implement
        return null;
    }

    /** @brief Gets all Lendings of a specific Book.
     * @param book The Book whose Lendings to retrieve.
     * @return A collection of Lendings associated with the specified Book.
     */
    public Collection<Lending> getByBook(Book book){
        // TODO: implement
        return null;
    }

    /**
     * @brief Gets all Lendings sorted by return date.
     * @param returnDate The return date to filter Lendings.
     * @return A collection of Lendings sorted by return date.
     */
    public Collection<Lending> getLendingsByReturnDate(LocalDate returnDate){
        //TODO: implement
        return null;
    }

    /** @brief Returns a list of Lendings sorted by return date.
     * @return A list of Lendings sorted by return date.
    */
    public List<Lending> sortedLendingsByReturnDate() {
        // TODO: implement
        return null;
    }

    
    /** @brief Returns a string representation of the LendingSet.
     * @return A string representation of the LendingSet.
    */
    @Override
    public String toString() {
        // TODO: implement
        return "";
    }

}