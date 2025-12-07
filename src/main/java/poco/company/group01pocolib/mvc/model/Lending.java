/**
 * @file Lending.java
 * @brief This file contains the definition of the Lending class, which represents a Lending in the library.
 * @author Daniele Pepe
 */
package poco.company.group01pocolib.mvc.model;

import java.time.LocalDate;
/**
 * @class Lending
 * @brief Represents a Lending in the library.
 */
public class Lending {
    private Book book;
    private User user;
    private LocalDate returnDate;
    static private int lendingCounter = 0;
    private int lendingId;
    private boolean returned;
    /**
     * @brief Constructs a new Lending object.
     * @param book The Book being lent.
     * @param user The User who is borrowing the Book.
     * @param returnDate The date by which the Book should be returned.
     */
    public Lending (Book book, User user, LocalDate returnDate) {
        this.book = book;
        this.user = user;
        this.returnDate = returnDate;
        this.lendingId = lendingCounter++;
        this.returned = false;
        //TODO : implement lending logic
    }
    /** @brief Gets the Book being lent.
     * @return The Book being lent.
    */
    public Book getBook() {return book;}
    
    /** @brief Gets the User who is borrowing the Book.
     * @return The User who is borrowing the Book.
    */
    public User getUser() {return user;}
    /** @brief Gets the return date for the Book.
     * @return The return date for the Book.
    */
    public LocalDate getReturnDate() {return returnDate;}
    /** @brief Gets the unique ID of the Lending.
     * @return The unique ID of the Lending.
    */
    public int getLendingId() {return lendingId;}
    /** @brief Checks if the Book has been returned.
     * @return true if the Book has been returned, false otherwise.
    */
    public boolean isReturned() {return returned;}
    /** @brief Sets the return date for the Book.
     * @param returnDate The new return date for the Book.
    */
    public void setReturnDate(LocalDate returnDate) {this.returnDate = returnDate;}
    /** @brief Sets the User who is borrowing the Book.
     * @param user The new User who is borrowing the Book.
    */
    public void setUser(User user) {this.user = user;}
    /** @brief Sets the Book being lent.
     * @param book The new Book being lent.
    */
    public void setBook(Book book) {this.book = book;}
    /** @brief Marks the Book as returned.
    */
    public void setReturned() {this.returned = true;}
    /** @brief Marks the Book as not returned.
    */
    public void setNotReturned() {this.returned = false;}
}
