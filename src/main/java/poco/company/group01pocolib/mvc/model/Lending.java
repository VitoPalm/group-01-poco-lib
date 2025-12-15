/**
 * @file Lending.java
 * @brief This file contains the definition of the Lending class, which represents a Lending in the library.
 * @author Daniele Pepe
 * @author Giovanni Orsini
 */
package poco.company.group01pocolib.mvc.model;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import poco.company.group01pocolib.exceptions.*;

import static poco.company.group01pocolib.db.omnisearch.Index.NGRAM_SIZE;
import static poco.company.group01pocolib.db.omnisearch.Index.PADDING_CHAR;

/**
 * @class   Lending
 * @brief   Represents a Lending in the library.
 * @details Each Lending has a unique ID, a book being lent, a user who is borrowing the book, a return date,
 *          and a status indicating whether the book has been returned. The lending ID is auto-incremented for each new
 *          lending created. The class can be serialized for persistence.
 */
public class Lending implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int lendingId;
    private Book book;
    private User user;
    private LocalDate returnDate;
    static private int lendingCounter = 0;
    private boolean returned;

    private static final String FIELD_SEPARATOR = "\u001C";

    /**
     * @brief   Constructs a new Lending object.
     * @details Increments the lending counter to assign a unique ID to the new Lending, and updates the book and user
     *          statistics accordingly. This second update can throw exceptions if the user or book limits are exceeded.
     *
     * @param   book        The Book being lent.
     * @param   user        The User who is borrowing the Book.
     * @param   returnDate  The date by which the Book should be returned.
     *
     * @throws  BookDataNotValidException If trying to lend a book that has no available copies.
     * @throws  UserDataNotValidException If the user has reached the maximum number of borrowed books.
     */
    public Lending(Book book, User user, LocalDate returnDate) {
        this.book = book;
        this.user = user;
        this.returnDate = returnDate;
        this.lendingId = ++lendingCounter;
        this.returned = false;
    }

    /**
     * @brief   Constructs a new Lending object. No checks are performed on anything.
     *
     * @param   lendingID   The unique ID of the Lending.
     * @param   book        The Book being lent.
     * @param   user        The User who is borrowing the Book.
     * @param   returnDate  The date by which the Book should be returned.
     * @param   returned    The status indicating whether the Book has been returned.
     *
     * @warning This constructor should only be used when reading from the DB.
     */
    public Lending(int lendingID, Book book, User user, LocalDate returnDate, boolean returned) {
        this.lendingId = lendingID;
        this.book = book;
        this.user = user;
        this.returnDate = returnDate;
        this.returned = returned;
    }

    /**
     * @brief   Gets the Book being lent.
     * @return  The Book being lent.
     */
    public Book getBook() {
        return book;
    }

    /**
     * @brief   Sets the Book being lent.
     * @param   book The new Book being lent.
     */
    public void setBook(Book book) {
        this.book = book;
    }

    /**
     * @brief   Gets the User who is borrowing the Book.
     * @return  The User who is borrowing the Book.
     */
    public User getUser() {
        return user;
    }

    /**
     * @brief   Sets the User who is borrowing the Book.
     * @param   user The new User who is borrowing the Book.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @brief   Gets the return date for the Book.
     * @return  The return date for the Book.
     */
    public LocalDate getReturnDate() {
        return returnDate;
    }

    /**
     * @brief   Sets the return date for the Book.
     * @param   returnDate The new return date for the Book.
     */
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    /**
     * @brief   Gets the unique ID of the Lending.
     * @return  The unique ID of the Lending.
     */
    public int getLendingId() {
        return lendingId;
    }

    /**
     * @brief   Sets the ID of the Lending. Should only be used in very rare circumstances.
     * @param   lendingId New ID of the Lending.
     */
    public void setLendingId(int lendingId) {
        this.lendingId = lendingId;
    }

    /**
     * @brief   Checks if the Book has been returned.
     * @return  true if the Book has been returned, false otherwise.
     */
    public boolean isReturned() {
        return returned;
    }

    /**
     * @brief   Marks the Book as returned.
     */
    public void setReturned() {
        if (!this.returned) {
            this.returned = true;
            this.user.decrementBorrowedBooksCount();
            this.book.returnCopy();
        }
    }

    /**
     * @brief   Marks the Book as not returned.
     */
    public void setNotReturned() {
        if (this.returned) {
            this.returned = false;
            this.user.incrementBorrowedBooksCount();
        }
    }

    /**
     * @brief   Overrides `hashCode` method to use the lending ID as unique identifier.
     * @return  The hash code of the Lending.
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(lendingId);
    }

    /**
     * @brief   Overrides `equals` method to compare Lending objects based on their lending ID.
     * @param   obj The object to compare with.
     * @return  true if the Lending objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Lending other = (Lending) obj;
        return lendingId == other.lendingId;
    }

    /**
     * @brief   Creates a Lending object from its string representation, used for DB reads.
     * @details The string representation format is "ID␜BookISBN␜UserID␜ReturnDate␜isReturned".
     * 
     * @todo    Test if the new lending is created successfully
     * @param   lendingStr The string representation of the Lending.
     * @throws  IllegalArgumentException If the string format is incorrect.
     * @return  A Lending object created from the string representation.
     * @author  Giovanni Orsini
     * @author  Daniele Pepe
     */
    public static Lending fromDBString(String lendingStr, BookSet bookSet, UserSet userSet) {
        if(lendingStr == null || lendingStr.isEmpty()) {
            return null;
        }

        String[] fields = lendingStr.split(FIELD_SEPARATOR);

        if (fields.length != 5) {
            throw new IllegalArgumentException("Wrong format for Lending DB string");
        }

        return new Lending(Integer.parseInt(fields[0]),         // lendingId
                           bookSet.getBook(fields[1]),          // book
                           userSet.getUser(fields[2]),          // user
                           LocalDate.parse(fields[3]),          // returnDate
                           Boolean.parseBoolean(fields[4])      // returned
        );
    }

    /**
     * @brief   Get a string containing only the searchable info of the lending
     * @details The searchable version of the string will include the searchable info of the book and user, plus the
     *          return date, and the id of the lending.
     *
     * @return  A string containing the searchable info of the lending
     * @author  Giovanni Orsini
     */
    public String toSearchableString() {
        StringBuilder output = new StringBuilder();

        int length = Integer.valueOf(lendingId).toString().length();

        output.append(lendingId);
        while (length < NGRAM_SIZE) {
            output.append(PADDING_CHAR);
            length++;
        }

        length = returnDate.toString().length();
        output.append(returnDate);
        while (length < NGRAM_SIZE) {
            output.append(PADDING_CHAR);
            length++;
        }

        output.append(book.toSearchableString());
        output.append(user.toSearchableString());

        return output.toString();
    }

    /**
     * @brief   Returns a string representation of the Lending.
     * @details The string representation format is "'ID'␜'BookISBN'␜'UserID'␜'ReturnDate'␜'isReturned'".
     * 
     * @return  A string representation of the Lending.
     * @author  Giovanni Orsini
     */
    public String toDBString() {
        return getLendingId() + FIELD_SEPARATOR +
               getBook().getIsbn() + FIELD_SEPARATOR + 
               getUser().getId() + FIELD_SEPARATOR +
               getReturnDate() + FIELD_SEPARATOR + 
               isReturned();
    }

    /**
     * @brief   Overrides toString method to provide a readable representation of the Lending.
     * @return  A string representation of the Lending.
     * @author  Giovanni Orsini
     */
    @Override
    public String toString() {
        return "Lending:\n" +
                "\tid='" + getLendingId() + "'\n" +
                "\tbook='" + getBook().getIsbn() + "' '" + getBook().getTitle() + "'\n" +
                "\tuser='" + getUser().getId() + "' '" + getUser().getName() + " " + getUser().getSurname() + "'\n" +
                "\treturn_date="  + getReturnDate() + "\n" +
                "\treturned=" + isReturned();
    }
}
