/**
 * @file BookSet.java
 * @brief This file contains the definition of BookSet class
 * @author Francesco Marino
 * @date 6 December 2025
 */
package poco.company.group01pocolib.mvc.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @class BookSet
 * @brief Represents a collection of books
 */
public class BookSet {

    private Map<String, Book> bookSet;
    
    /**
     * @brief Default constructor
     * Initializes a new empty collection of books
     */
    public BookSet(){
        this.bookSet = new HashMap<>();
    }

    /**
     * @brief Adds a book to collection
     * @param book The book object to add
     * @throws IllegalArgumentException if the book to add is null 
     */
    public void addBook(Book book){
        //TODO: Implement book add
    }

    /**
     * @brief Remove a book from the collection
     * @param isbn The ISBN of the book to remove
     */
    public void removeBook(String isbn){
        //TODO: Implement book remove
    } 

    /**
     * @brief Get a book from its ISBN
     * @param isbn The ISBN of the book to get
     * @return The Book object if found, null otherwise
     */
    public Book getBook(String isbn){
        //TODO: Implement book getter
        return null;
    }
}