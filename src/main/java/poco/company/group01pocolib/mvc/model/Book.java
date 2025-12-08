/**
 * @file Book.java
 * @brief This file contains the definition of the Book class, which represents a Book in the library.
 * @author Daniele Pepe
 */
package poco.company.group01pocolib.mvc.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import poco.company.group01pocolib.exceptions.BookDataNotValidException;

/**
 * @class   Book
 * @brief   Represents a Book in the library.
 * @details Each Book has a title, a list of authors, an ISBN, a release year, a number of copies owned, a number of
 *          copies currently lent out, a number for the amount of times the Book has been lent, and the line index for
 *          the DB file. The ISBN must be valid for the Book to be created. The book can be serialized for persistence.
 */
public class Book implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String title;
    private ArrayList<String> authors;
    private String isbn;
    private int year;
    private int copies;
    private int copiesLent;
    private int timesLent;

    private static final String FIELD_SEPARATOR = "\u001C";

    /**
     * @brief   Constructs a new Book object. (it won't be created if the ISBN is not valid)
     *
     * @param   title   The title of the Book.
     * @param   authors The authors of the Book.
     * @param   isbn    The ISBN of the Book.
     * @param   year    The release year of the Book.
     * @param   copies  The number of copies available of the Book.
     *
     * @throws  BookDataNotValidException   if the provided ISBN is not valid.
     */
    public Book(String title, ArrayList<String> authors, String isbn, int year, int copies) {
        if (!isValidIsbn(isbn)) {
            throw new BookDataNotValidException("Invalid ISBN: " + isbn);
        }
        this.title = title;
        this.authors = authors;
        this.isbn = isbn;
        this.year = year;
        this.copies = copies;
        this.copiesLent = 0;
        this.timesLent = 0;
    }

    /**
     * @brief   Validates the given ISBN.
     *
     * @param   isbn The ISBN to validate.
     * @return  @c true if the ISBN is valid, @c false otherwise.
     */
    public static boolean isValidIsbn(String isbn) {
        // TODO: Implement ISBN validation logic
        return false;
    }

    /**
     * @brief   Gets the title of the Book.
     * @return  The title of the Book.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @brief   Sets the title of the Book.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @brief   Gets the List of authors of the Book.
     * @return  The list of authors of the Book.
     */
    public ArrayList<String> getAuthors() {
        return authors;
    }

    /**
     * @brief   Sets the list of authors of the Book using a @c List of strings.
     */
    public void setAuthors(ArrayList<String> authors) {
        this.authors = authors;
    }

    /**
     * @brief   Sets the list of authors of the Book using a single `String` with authors separated by semicolons.
     */
    public void setAuthorsFromString(String authorsStr) {
        // TODO: Implement
    }

    /**
     * @brief   Gets the ISBN of the Book.
     * @return  The ISBN of the Book.
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * @brief   Sets the ISBN of the Book.
     * @throws  BookDataNotValidException if the provided ISBN is not valid.
     */
    public void setIsbn(String isbn) {
        if (!isValidIsbn(isbn)) {
            throw new BookDataNotValidException("Invalid ISBN: " + isbn);
        }
        this.isbn = isbn;
    }

    /**
     * @brief   Gets the release year of the Book.
     * @return  The release year of the Book.
     */
    public int getYear() {
        return year;
    }

    /**
     * @brief   Sets the release year of the Book.
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * @brief   Gets the number of copies available of the Book.
     * @return  The number of copies available of the Book.
     */
    public int getCopies() {
        return copies;
    }

    /**
     * @brief   Sets the number of copies available of the Book.
     *
     * @param   copies The number of copies to set.
     * @throws  IllegalArgumentException if the number of copies is negative.
     */
    public void setCopies(int copies) {
        if (copies < 0) {
            throw new IllegalArgumentException("Number of copies cannot be negative.");
        }
        this.copies = copies;
    }

    /**
     * @brief   Increases the number of copies by one.
     */
    public void addCopy() {
        this.copies += 1;
    }

    /**
     * @brief   Decreases the number of copies by one.
     * @throws  IllegalStateException if there are no copies available to remove.
     */
    public void removeCopy() {
        if(this.copies > 0) this.copies -= 1;
        else throw new IllegalStateException("No copies available to remove.");
    }

    /**
     * @brief Overrides `hashCode` to use ISBN as unique identifier.
     * @return The hash code of the Book.
     */
    @Override
    public int hashCode() {
        return isbn.hashCode();
    }

    /**
     * @brief   Creates a Book object from its string representation (typically used for DB reads).
     * @details The string representation format is TODO: implement.
     *
     * @param   bookStr The string representation of the Book.
     * @return  The Book object.
     */
    public static Book fromString(String bookStr) {
        // TODO: Implement
        return null;
    }

    /**
     * @brief   Get a string containing only the searchable info of the book
     * @details This includes title, authors, isbn and year. The string representation format is TODO: implement.
     *
     * @return  A string containing the searchable info of the book
     */
    public String toSearchableString() {
        // TODO: Implement
        return "";
    }

    /**
     * @brief   Returns a string representation of the Book (typically used for DB writes).
     * @details The string representation format is TODO: implement.
     *
     * @return  A string representation of the Book;
     */
    @Override
    public String toString() {
        // TODO: Implement a proper toString method
        return "";
    }
}