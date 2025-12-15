/**
 * @file Book.java
 * @brief This file contains the definition of the Book class, which represents a Book in the library.
 * @author Daniele Pepe
 * @author Francesco Marino
 * @author Giovanni Orsini
 */
package poco.company.group01pocolib.mvc.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import poco.company.group01pocolib.exceptions.BookDataNotValidException;

import static poco.company.group01pocolib.db.omnisearch.Index.NGRAM_SIZE;
import static poco.company.group01pocolib.db.omnisearch.Index.PADDING_CHAR;

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
    private int copiesAvailable;
    private int copiesLent;
    private int timesLent;

    private static final String FIELD_SEPARATOR = "\u001C";


    /**
     * @brief   Constructs a new Book object.
     *
     * @param   title   The title of the Book.
     * @param   authors The authors of the Book.
     * @param   isbn    The ISBN of the Book.
     * @param   year    The release year of the Book.
     * @param   copiesAvailable  The number of copies available of the Book.
     * 
     * @throws  BookDataNotValidException if the number of copies is negative.
     */
    public Book(String title, List<String> authors, String isbn, int year, int copiesAvailable) {
        if (copiesAvailable < 0)
            throw new BookDataNotValidException("Cannot create book with fewer than 0 copies.");

        this.title = title;
        this.authors = new ArrayList<>(authors);
        this.isbn = isbn;
        this.year = year;
        this.copiesAvailable = copiesAvailable;
        this.copiesLent = 0;
        this.timesLent = 0;
    }

    public Book(String identifier) {
        this.isbn = identifier;
    }

    /**
     * @brief   Constructs a new Book object. That takes authors as a single `String` separated by "; ".
     *
     * @param   title   The title of the Book.
     * @param   authors The String of semicolon separated authors of the Book.
     * @param   isbn    The  ISBN of the Book.
     * @param   year    The release year of the Book.
     * @param   copiesAvailable  The number of copies available of the Book.
     * 
     * @throws  BookDataNotValidException if the number of copies is negative.
     */
    public Book(String title, String authors, String isbn, int year, int copiesAvailable) {
        if (copiesAvailable < 0)
            throw new BookDataNotValidException("Cannot create book with fewer than 0 copies.");

        this.title = title;
        this.setAuthors(authors);
        this.isbn = isbn;
        this.year = year;
        this.copiesAvailable = copiesAvailable;
        this.copiesLent = 0;
        this.timesLent = 0;
    }

    /**
     * @brief Default constructor for utility purposes only.
     */
    public Book() {
        
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
        return String.join("; ", authors);
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
        String[] authorArray = authorsStr.split("; ");

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
     */
    public void setIsbn(String isbn) {
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
     * @brief   Sets the number of copies currently lent out of the Book.
     * @param   copiesLent The number of copies currently lent out.
     */
    public void setCopiesLent(int copiesLent) {
        this.copiesLent = copiesLent;
    }

    /**
     * @brief   Increments the number of copies currently lent out for the Book by one. Also increments the times lent.
     * @throws  BookDataNotValidException If all copies are already lent out.
     * @return  The new number of copies currently lent out.
     */
    public int incrementCopiesLent() {
        if (this.copiesLent < this.copiesAvailable) {
            this.timesLent++;

            return ++(this.copiesLent);
        }
        else throw new BookDataNotValidException("All copies are already lent out.");
    }

    /**
     * @brief   Decrements the number of copies currently lent out for the Book by one.
     * @throws  BookDataNotValidException If no copies are currently lent out.
     * @return  The new number of copies currently lent out.
     */
    public int decrementCopiesLent() {
        if (this.copiesLent > 0) return --this.copiesLent;
        else throw new BookDataNotValidException("No copies are currently lent out.");
    }

    /**
     * @brief   Gets the number of copies currently lent for the Book.
     * @return  The number of copies currently lent out.
     */
    public int getCopiesLent() {
        return copiesLent;
    }

    /**
     * @brief   Sets the number of times the Book has been lent.
     * @param   timesLent The number of times the Book has been lent.
     * @throws  BookDataNotValidException if the number of times lent is negative or less than copies currently lent.
     */
    public void setTimesLent(int timesLent) {
        if (timesLent >= 0 && timesLent >= this.copiesLent) this.timesLent = timesLent;
        else throw new BookDataNotValidException("Times lent cannot be negative or less than copies currently lent.");
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
    public int getCopiesAvailable() {
        return copiesAvailable;
    }

    /**
     * @brief   Sets the number of copies available of the Book.
     *
     * @param   copiesAvailable The number of copies to set.
     * @throws  BookDataNotValidException if the number of copies is negative.
     */
    public void setCopiesAvailable(int copiesAvailable) {
        if (copiesAvailable < 0)
            throw new BookDataNotValidException("Cannot create book with fewer than 0 copies.");

        this.copiesAvailable = copiesAvailable;
    }

    /**
     * @brief   Increases the number of copies by one.
     */
    public void addCopy() {
        this.copiesAvailable += 1;
    }

    /**
     * @brief   Decreases the number of copies by one.
     * @throws  IllegalStateException if there are no copies available to remove.
     */
    public void removeCopy() {
        if(this.copiesAvailable > 0) this.copiesAvailable -= 1;
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
     * @details The string representation format is Title␜Authors␜ISBN␜Year␜CopiesAvailable␜CopiesLent␜TimesLent.
     * 
     *
     * @param   bookStr The string representation of the Book.
     * @return  The Book object.
     */
    public static Book fromDBString(String bookStr) {
       String[] fields = bookStr.split("\u001C");
    
        Book book = new Book(
            fields[0], 
            List.of(fields[1].split("; ")), 
            fields[2], 
            Integer.parseInt(fields[3]), 
            Integer.parseInt(fields[4])
        );

        book.setCopiesLent(Integer.parseInt(fields[5]));
        book.setTimesLent(Integer.parseInt(fields[6]));
    
        return book;
    }

    /**
     * @brief   Generates a string containing only the searchable info of the book.
     * @details The searchable info includes the title, authors, and ISBN, each padded to a fixed minimum length defined
     *          by NGRAM_SIZE.
     *
     * @return  A string containing the searchable info of the book
     */
    public String toSearchableString() {
        StringBuilder output = new StringBuilder();

        int length = isbn.strip().length();

        output.append(isbn.strip());
        while (length < NGRAM_SIZE) {
            output.append(PADDING_CHAR);
            length++;
        }

        length = title.strip().length();
        output.append(title.strip());
        while (length < NGRAM_SIZE) {
            output.append(PADDING_CHAR);
            length++;
        }

        length = getAuthorsString().strip().length();
        output.append(getAuthorsString().strip());
        while (length < NGRAM_SIZE) {
            output.append(PADDING_CHAR);
            length++;
        }

        length = Integer.valueOf(year).toString().length();
        output.append(year);
        while (length < NGRAM_SIZE) {
            output.append(PADDING_CHAR);
            length++;
        }

        return output.toString();
    }

    /**
     * @brief   Returns a string representation of the Book (typically used for DB writes).
     * @details The string representation format is Title␜Authors␜ISBN␜Year␜CopiesAvailable␜CopiesLent␜TimesLent.
     *
     * @return  A string representation of the Book;
     */

    public String toDBString() {
        return getTitle() + FIELD_SEPARATOR +
               getAuthorsString() + FIELD_SEPARATOR +
               getIsbn() + FIELD_SEPARATOR +
               getYear() + FIELD_SEPARATOR +
               getCopiesAvailable() + FIELD_SEPARATOR +
               getCopiesLent() + FIELD_SEPARATOR +
               getTimesLent();
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
                "\tcopies=" + getCopiesAvailable() + "\n";
    }
}