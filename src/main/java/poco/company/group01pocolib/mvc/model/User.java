/**
 * @file User.java
 * @brief This file contains the definition of the User class, which represents Library User .
 * @author Giovanni Orsini
 */


package poco.company.group01pocolib.mvc.model;

import poco.company.group01pocolib.exceptions.UserDataNotValidException;

import java.io.Serial;
import java.io.Serializable;


/**
 * @class   User
 * @brief   Represents a Library User
 * @details Each User has a unique ID, a name, a surname, an email, a number of currently borrowed books, and a total
 *          number of books borrowed ever. The ID and email must be of valid format for the object to be created. The
 *          class can be serialized for persistence.
 */
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String surname;
    private String email;
    private int borrowedBooksCount;
    private int borrowedBooksEverCount;

    private static final String FIELD_SEPARATOR = "\u001C";
    private static final int MAX_BORROWED_BOOKS = 3;

    /**
     * @brief   Constructs a new `User` object
     *
     * @pre     `id` and email fields should be valid
     * @post    A new user is created
     *
     * @param   id      User's unique ID    TODO: define format
     * @param   name    User's name
     * @param   surname User's surname
     * @param   email   User's email.
     *
     * @throws UserDataNotValidException if a User is being initialized with an invalid ID/Email
     */
    public User(String id, String name, String surname, String email) {
        if (!User.isValidID(id))
            throw new UserDataNotValidException("Invalid ID: " + id);

        if (!User.isValidEmail(email))
            throw new UserDataNotValidException("Invalid email: " + email);

        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    /**
     * @brief   `static` method to check whether a String is a correct User ID
     *
     * @param   id The String to check
     * @return  `true` if the User ID is valid, `false` otherwise
     */
    public static boolean isValidID(String id) {
        // TODO: implement
        return false;
    }

    /**
     * @brief   `static` method to check whether a String is a correct Email
     *
     * @param   email The String to check
     * @return  `true` if the Email is valid, `false` otherwise
     */
    public static boolean isValidEmail(String email) {
        // TODO: implement
        return false;
    }

    /**
     * @brief   Method to check whether the User can borrow more books
     *
     * @return  `true` if the User can borrow more books, `false` otherwise
     */
    public boolean canBorrow() {
        // TODO: implement
        return false;
    }

    /**
     * @brief   Gets the ID of the User
     * @return  The ID of the User
     */
    public String getId() {
        return id;
    }

    /**
     * @brief   Sets the ID of the User
     * @param   id The ID to set
     * @throws  UserDataNotValidException if the provided ID is not valid
     */
    public void setId(String id) {
        if (!User.isValidID(id))
            throw new UserDataNotValidException("Invalid ID: " + id);

        this.id = id;
    }

    /**
     * @brief   Gets the name of the User
     * @return  The name of the User
     */
    public String getName() {
        return name;
    }

    /**
     * @brief   Sets the name of the User
     * @param   name The name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @brief   Gets the surname of the User
     * @return  The surname of the User
     */
    public String getSurname() {
        return surname;
    }

    /**
     * @brief   Sets the surname of the User
     * @param   surname The surname to set
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * @brief   Gets the email of the User
     * @return  The email of the User
     */
    public String getEmail() {
        return email;
    }

    /**
     * @brief   Sets the email of the User
     * @param   email The email to set
     * @throws  UserDataNotValidException if the provided email is not valid
     */
    public void setEmail(String email) {
        if (!User.isValidEmail(email))
            throw new UserDataNotValidException("Invalid email: " + email);

        this.email = email;
    }

    /**
     * @brief   Gets the number of currently borrowed books by the User
     * @return  The number of currently borrowed books by the User
     */
    public int getBorrowedBooksCount() {
        return borrowedBooksCount;
    }

    /**
     * @brief   Sets the number of currently borrowed books by the User
     * @param   borrowedBooksCount The number of currently borrowed books to set
     */
    public void setBorrowedBooksCount(int borrowedBooksCount) {
        this.borrowedBooksCount = borrowedBooksCount;
    }

    /**
     * @brief   Gets the total number of books ever borrowed by the User
     * @return  The total number of books ever borrowed by the User
     */
    public int getBorrowedBooksEverCount() {
        return borrowedBooksEverCount;
    }

    /**
     * @brief   Sets the total number of books ever borrowed by the User
     * @param   borrowedBooksEverCount The total number of books ever borrowed to set
     */
    public void setBorrowedBooksEverCount(int borrowedBooksEverCount) {
        this.borrowedBooksEverCount = borrowedBooksEverCount;
    }

    /**
     * @brief   Overrides `hashCode` method to use the user ID as unique identifier.
     * @return  The hash code of the User.
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /**
     * @brief   Creates a User object from its string representation (typically used for DB reads).
     * @details The string representation format is TODO: implement.
     *
     * @param   userStr The string representation of the User.
     * @return  The User object.
     */
    public static User fromString(String userStr) {
        // TODO: implement
        return null;
    }

    /**
     * @brief   Get a string containing only the searchable info of the user
     * @details This includes id, name, surname and email. The string representation format is TODO: implement.
     *
     * @return  A string containing the searchable info of the book
     */
    public String toSearchableString() {
        // TODO: Implement
        return "";
    }

    /**
     * @brief   Returns a string representation of the User (typically used for DB writes).
     * @details The string representation format is TODO: implement.
     *
     * @return  A string representation of the User.
     */
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return super.toString();
    }
}
