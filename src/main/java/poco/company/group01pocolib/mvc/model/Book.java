/**
 * @file Book.java
 * @brief This file contains the definition of the Book class, which represents a Book in the library.
 * @author Daniele Pepe
 * @date 5 December 2025
 * @version 0.1
 */
package poco.company.group01pocolib.mvc.model;

import java.util.List;

import poco.company.group01pocolib.exceptions.BookDataNotValidException;
/**
 * @class Book
 * @brief Represents a Book in the library.
 */
public class Book {
    private String title;
    private List<String> authors;
    private String isbn;            ///< Each Book has his own valid ISBN. A Book can only be created if this value is valid
    private int year;
    private int copies;
    /**
     * @brief Constructs a new Book object.
     * @param title The title of the Book.
     * @param authors The authors of the Book.
     * @param isbn The ISBN of the Book.
     * @param year The release year of the Book.
     * @param copies The number of copies available of the Book.
     * @throws BookDataNotValidException if the provided ISBN is not valid.
     */
    public Book(String title, List<String> authors, String isbn, int year, int copies) {
        if (!isValidIsbn(isbn)) {
            throw new BookDataNotValidException("Invalid ISBN: " + isbn);
        }
        this.title = title;
        this.authors = authors;
        this.isbn = isbn;
        this.year = year;
        this.copies = copies;
    }

    /**
     * @brief Validates the given ISBN.
     * @param isbn The ISBN to validate.
     * @return true if the ISBN is valid, false otherwise.
     */
    public static boolean isValidIsbn(String isbn) {
        // TODO: Implement ISBN validation logic
        return false;
    }
    /** @brief Gets the title of the Book. 
     * @return The title of the Book.
    */
    public String getTitle() {return title;}
    /** @brief Gets the authors of the Book. 
     * @return The authors of the Book.
    */
    public List<String> getAuthors() {return authors;}
    /** @brief Gets the ISBN of the Book. 
     * @return The ISBN of the Book.
    */
    public String getIsbn() {return isbn;}
    /** @brief Gets the release year of the Book. 
     * @return The release year of the Book.
    */
    public int getYear() {return year;}
    /** @brief Gets the number of copies available of the Book. 
     * @return The number of copies available of the Book.
    */
    public int getCopies() {return copies;}
    /** @brief Sets the number of copies available of the Book.
     * @param copies The number of copies to set.
     * @throws IllegalArgumentException if the number of copies is negative.
    */
    public void setCopies(int copies) {
        if (copies < 0) {
            throw new IllegalArgumentException("Number of copies cannot be negative.");
        }    
        this.copies = copies;
    }
    /** @brief Increases the number of copies by one. */
    public void addCopy() {this.copies += 1;}
    /** @brief Decreases the number of copies by one.
     * @throws IllegalStateException if there are no copies available to remove.
    */
    public void removeCopy() {
        if(this.copies > 0) this.copies -= 1;
        else throw new IllegalStateException("No copies available to remove.");
    }
    /** @brief Sets the title of the Book. */
    public void setTitle(String title) {this.title = title;}
    /** @brief Sets the authors of the Book. */
    public void setAuthors(List<String> authors) {this.authors = authors;}
    /** @brief Sets the ISBN of the Book.
     * @throws BookDataNotValidException if the provided ISBN is not valid.
    */
    public void setIsbn(String isbn) {
        if (!isValidIsbn(isbn)) {
            throw new BookDataNotValidException("Invalid ISBN: " + isbn);
        }
        this.isbn = isbn;
    }
    /** @brief Sets the release year of the Book. */
    public void setYear(int year) {this.year = year;}

    /** @brief Returns a string representation of the Book. 
     * @return A string representation of the Book in the format TODO: implement.
    */
    @Override
    public String toString() {
        // TODO: Implement a proper toString method
        return "";
    }
    
}