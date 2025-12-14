/**
 * @file User.java
 * @brief This file contains the definition of the User class, which represents Library User.
 * @author Giovanni Orsini
 * @author Daniele Pepe
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
public class User implements Comparable<User>, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String surname;
    private String email;
    private int borrowedBooksCount;
    private int borrowedBooksEverCount;

    private static final String FIELD_SEPARATOR = "\u001C";
    public static final int MAX_BORROWED_BOOKS = 3;

    /**
     * @brief   Default constructor for utility purposes
     */
    public User(){

    }

    /**
     * @brief   Constructs a new `User` object
     *
     * @pre     Email field should be valid
     * @post    A new user is created
     *
     * @param   id      User's unique ID (Alphanumeric string between 5 and 16 characters)
     * @param   name    User's name
     * @param   surname User's surname
     * @param   email   User's email
     *
     * @throws UserDataNotValidException If a `User` is being initialized with an invalid ID/Email, or if `name` or
     *                                   `surname` are null or empty
     */
    public User(String id, String name, String surname, String email) {
        if (!User.isValidID(id))
            throw new UserDataNotValidException("Invalid ID: " + id);

        if (!User.isValidEmail(email))
            throw new UserDataNotValidException("Invalid email: " + email);

        if (name == null || name.trim().isEmpty())
            throw new UserDataNotValidException("Name cannot be null or empty");

        if (surname == null || surname.trim().isEmpty())
            throw new UserDataNotValidException("Surname cannot be null or empty");
        
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.borrowedBooksCount = 0;
        this.borrowedBooksEverCount = 0;
    }

    /**
     * @brief   `static` method to check whether a String is a correct User ID
     * @details An ID is valid if it is an alphanumeric string of length between 5 and 16 characters, allowing the user
     *          to insert IDs from a short matrix code to longer Tax IDs.
     *
     * @param   id The String to check
     * @return  `true` if the User ID is valid, `false` otherwise
     */
    public static boolean isValidID(String id) {
        String idPattern = "^[\\p{L}\\d]{5,16}$";
        return (id != null && id.matches(idPattern));
    }

    /**
     * @brief   `static` method to check whether a String is a correct Email
     * @details An Email is valid if it follows the standard email format. The regex used is based on an implementation
     *          by Jan Goyvaerts that can be found at <a href="https://www.regular-expressions.info/email.html">this</a>
     *          website. It mostly follows RFC 5322 (excluding some really uncommon edge cases) which specifies syntax
     *          for email addresses. Is follows RFC 1035 to specify the length limits (RFC 1035 specifies details for
     *          the implementation of domain names).
     *
     * @param   email The String to check
     * @return  `true` if the Email is valid, `false` otherwise
     */
    public static boolean isValidEmail(String email) {
        String emailPattern = "\\A(?=[a-z0-9@.!#$%&'*+/=?^_`{|}~-]{6,254}\\z)" +
                              "(?=[a-z0-9.!#$%&'*+/=?^_`{|}~-]{1,64}@)" +
                              "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@" +
                              "(?:(?=[a-z0-9-]{1,63}\\.)[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+" +
                              "(?=[a-z0-9-]{1,63}\\z)[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\z";
        return (email != null && email.matches(emailPattern));
    }

    /**
     * @brief   Method to check whether the User can borrow more books
     *
     * @return  `true` if the User can borrow more books, `false` otherwise
     */
    public boolean canBorrow() {
        return this.borrowedBooksCount < MAX_BORROWED_BOOKS;
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
     * @throws  UserDataNotValidException if the provided name is null or empty
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty())
            throw new UserDataNotValidException("Name cannot be null or empty");
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
     * @throws  UserDataNotValidException if the provided surname is null or empty
     */
    public void setSurname(String surname) {
        if (surname == null || surname.trim().isEmpty())
            throw new UserDataNotValidException("Surname cannot be null or empty");
        this.surname = surname;
    }

    public String getFullName() {
        return surname + " " + name;
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
        if (borrowedBooksCount < 0 || borrowedBooksCount > MAX_BORROWED_BOOKS)
            throw new UserDataNotValidException("Borrowed books count must be between 0 and " + MAX_BORROWED_BOOKS);

        this.borrowedBooksCount = borrowedBooksCount;
    }

    /**
     * @brief   Increments the number of currently borrowed books by the User
     * @throws  UserDataNotValidException if the User is trying to borrow more than the allowed maximum
     * @return  The new number of currently borrowed books by the User
     */
    public int incrementBorrowedBooksCount() {
        if (this.borrowedBooksCount >= MAX_BORROWED_BOOKS)
            throw new UserDataNotValidException("Cannot borrow more than " + MAX_BORROWED_BOOKS + " books.");

        this.borrowedBooksEverCount++;
        return ++this.borrowedBooksCount;
    }

    /**
     * @brief   Decrements the number of currently borrowed books by the User
     * @throws  UserDataNotValidException if the User is trying to return a book when having 0 borrowed books
     * @return  The new number of currently borrowed books by the User
     */
    public int decrementBorrowedBooksCount() {
        if (this.borrowedBooksCount <= 0)
            throw new UserDataNotValidException("Cannot have less than 0 borrowed books.");

        return --this.borrowedBooksCount;
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
     * @throws  UserDataNotValidException if the count is negative or less than current borrowed books
     */
    public void setBorrowedBooksEverCount(int borrowedBooksEverCount) {
        if (borrowedBooksEverCount < 0)
            throw new UserDataNotValidException("Borrowed books ever count cannot be negative.");
        if (borrowedBooksEverCount < this.borrowedBooksCount)
            throw new UserDataNotValidException("Borrowed books ever count cannot be less than current borrowed books.");
        this.borrowedBooksEverCount = borrowedBooksEverCount;
    }

    /**
     * @brief Overrides `equals` method to compare Users based on their unique ID.
     * @param obj The object to compare with.
     * @return `true` if the Users have the same ID, `false` otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User other = (User) obj;
        return id.equals(other.id);
    }

    /**
     * @brief   Compares this User with another User based on their surnames, and names if surnames are equal. They are
     *          considered equal if they have the same ID.
     * @param   other The other User to compare with.
     * @return  A negative integer, zero, or a positive integer as this User is less than, equal to, or greater than the
     *          specified User.
     */
    @Override
    public int compareTo(User other) {
        if (this.equals(other)) {
            return 0;
        }

        if (this.surname.equalsIgnoreCase(other.surname)) {
            return this.name.compareToIgnoreCase(other.name);
        }

        return this.surname.compareToIgnoreCase(other.surname);
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
     * @brief   Creates a User object from its string representation used for DB reads.
     * @details The string representation format is "'ID'\u001C'Name'\u001C'Surname'\u001C'Email'\u001C'BorrowedBooksCount'\u001C'BorrowedBooksEverCount'".
     *
     * @param   userStr The string representation of the User from the DB.
     * @return  The User object.
     */
    public static User fromDBString(String userStr) {
        String[] fields = userStr.split(FIELD_SEPARATOR);
        User user = new User(fields[0], fields[1], fields[2], fields[3]);
        user.setBorrowedBooksCount(Integer.parseInt(fields[4]));
        user.setBorrowedBooksEverCount(Integer.parseInt(fields[5]));
        return user;
    }

    /**
     * @brief   Get a string containing only the searchable info of the user
     * @details This includes id, name, surname and email. The string representation format is "'ID''Name''Surname''Email'".
     *
     * @return  A string containing the searchable info of the user.
     */
    public String toSearchableString() {
        return getId().strip().toLowerCase() +
               getName().strip().toLowerCase() +
               getSurname().strip().toLowerCase() +
               getEmail().strip().toLowerCase();
        }

    /**
     * @brief   Returns a string representation of the User used for DB writes.
     * @details The string representation format is "'ID'\u001C'Name'\u001C'Surname'\u001C'Email'\u001C'BorrowedBooksCount'\u001C'BorrowedBooksEverCount'".
     *
     * @return  A string representation of the User for DB writes.
     */
    public String toDBString() {
        return getId() + FIELD_SEPARATOR +
               getName() + FIELD_SEPARATOR +
               getSurname() + FIELD_SEPARATOR +
               getEmail() + FIELD_SEPARATOR +
               getBorrowedBooksCount() + FIELD_SEPARATOR +
               getBorrowedBooksEverCount();
    }

    /**
     * @brief   Overrides `toString` method to provide a readable representation of the User.
     * @return  A string representation of the User.
     */
    @Override
    public String toString() {
        return "User:\n" +
                "\tid='" + getId() + "'\n" +
                "\tname='" + getName() + "'\n" +
                "\tsurname='" + getSurname() + "'\n" +
                "\temail='" + getEmail() + "'\n" +
                "\tborrowedBooksCount=" + getBorrowedBooksCount() + "\n" +
                "\tborrowedBooksEverCount=" + getBorrowedBooksEverCount() + "\n";
    }
}
