/**
 * @file Book.java
 * @brief This file contains the definition of the Book class, which represents a Book in the library.
 * @author Daniele Pepe
 * @author Francesco Marino
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
    private List<String> authors;  
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
    public Book(String title, List<String> authors, String isbn, int year, int copies) {
        if (!isValidIsbn(isbn)) {
            throw new BookDataNotValidException("Invalid ISBN: " + isbn);
        }
        this.title = title;
        this.authors = new ArrayList<>(authors);
        this.isbn = isbn;
        this.year = year;
        this.copies = copies;
        this.copiesLent = 0;
        this.timesLent = 0;
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
    public List<String> getAuthors() {
        return authors;
    }

    /**
     * @brief   Gets the authors of the Book as a single `String`, with authors separated by semicolons.
     * @return A `String` containing the authors of the Book.
     */
    public String getAuthorsString(){
        return String.join(";", authors);
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
    public void setAuthors(String authorsStr) {
        this.authors = new ArrayList<>();
        String[] authorArray = authorsStr.split(";");

        for (String author : authorArray) {
            authors.add(author.trim());
        }
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
     * @brief   Validates the given ISBN.
     *
     * @param   isbn The ISBN to validate.
     * @return  @c true if the ISBN is valid, @c false otherwise.
     */
    public static boolean isValidIsbn(String isbn) {
        String cleanIsbn = isbn.replace("-", "");

        //Check ISBN with 10-digit format 
        if (cleanIsbn.length() == 10 && cleanIsbn.matches("[0-9]{10}")) {
            int sum = 0;
            //Logic to validate checksum of the ISBN
            for (int i = 0; i < 10; i++) {
                int digit = Character.getNumericValue(cleanIsbn.charAt(i));
                sum += digit * (10 - i); 
            }
            return sum % 11 == 0;
        }
        
        //Check ISBN with 13-digit format
        if (cleanIsbn.length() == 13 && cleanIsbn.matches("[0-9]{13}")) {
            int sum = 0;
            for (int i = 0; i < 13; i++){
                int digit = Character.getNumericValue(cleanIsbn.charAt(i));
                sum += (i % 2 == 0) ? digit : digit * 3;
            }
            return sum % 10 == 0;
        }
        
        return false;
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
     * @brief   Sets the number of copies currently lent out of the Book.
     * @param copiesLent The number of copies currently lent out.
     */
    public void setCopiesLent(int copiesLent) {
        this.copiesLent = copiesLent;
    }

    /**
     * @brief   Gets the number of copies currently lent out of the Book.
     * @return  The number of copies currently lent out.
     */
    public int getCopiesLent() {
        return copiesLent;
    }

    /**
     * @brief   Sets the number of times the Book has been lent.
     * @param timesLent The number of times the Book has been lent.
     */
    public void setTimesLent(int timesLent) {
        this.timesLent = timesLent;
    }

    /**
     * @brief   Gets the number of times the Book has been lent.
     * @return  The number of times the Book has been lent.
     */
    public int getTimesLent() {
        return timesLent;
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
     * @brief   Overrides `equals` to compare Books based on their ISBN.
     * @param   obj The object to compare with.
     * @return  true if the Books have the same ISBN, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Book other = (Book) obj;
        return isbn.equals(other.isbn);
    }

    /**
     * @brief   Creates a Book object from its string representation (typically used for DB reads).
     * @details The string representation format is Title␜Authors␜ISBN␜Year␜Copies.
     * 
     *
     * @param   bookStr The string representation of the Book.
     * @return  The Book object.
     */
    public static Book fromDBString(String bookStr) {
       String[] fields = bookStr.split("\u001C");
    
        Book book = new Book(
            fields[0], 
            List.of(fields[1].split(";")), 
            fields[2], 
            Integer.parseInt(fields[3]), 
            Integer.parseInt(fields[4])
        );
    
        return book;
    }

    /**
     * @brief   Get a string containing only the searchable info of the book
     * @details This includes title, authors, isbn, year, and copies. The string representation format is "title+authors+isbn+year+copies".
     *
     * @return  A string containing the searchable info of the book
     */
    public String toSearchableString() {
        return getTitle().strip().toLowerCase() +
               getAuthorsString().strip().toLowerCase() +
               getIsbn().strip().toLowerCase() +
               getYear() + getCopies();
    }

    /**
     * @brief   Returns a string representation of the Book (typically used for DB writes).
     * @details The string representation format is Title␜Authors␜ISBN␜Year␜Copies.
     *
     * @return  A string representation of the Book;
     */

    public String toDBString() {
        return getTitle() + FIELD_SEPARATOR +
               getAuthorsString() + FIELD_SEPARATOR +
               getIsbn() + FIELD_SEPARATOR +
               getYear() + FIELD_SEPARATOR +
               getCopies();
    }

    /**
     * @brief   Overrides toString method to provide a readable representation of the Book.
     * @return  A string representation of the Book.
     */
    @Override
    public String toString() {
        return "Book:\n" +
                "\ttitle='" + getTitle() + "\n" +
                "\tauthors='" + getAuthorsString() + "\n" +
                "\tisbn='" + getIsbn() + "\n" +
                "\tyear=" + getYear() + "\n" +
                "\tcopies=" + getCopies() + "\n";
    }
}