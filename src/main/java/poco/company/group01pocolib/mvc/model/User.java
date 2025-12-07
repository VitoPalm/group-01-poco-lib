/**
 * @file User.java
 * @brief This file contains the definition of the User class, which represents Library User .
 * @author Giovanni Orsini
 */


package poco.company.group01pocolib.mvc.model;

import poco.company.group01pocolib.exceptions.UserDataNotValidException;


/**
 * @class User
 * @brief Represents a Library User 
 */
public class User {
    private String id;          ///< Each User has his own valid ID. A user can only be created if this value is valid
    private String name;
    private String surname;
    private String email;       ///< Each User has his own valid Email. A user can only be created if this value is valid


    /**
     * @brief Constructs a new User object
     * @pre id and email fields should be valid
     * @post A new user is created
     * @param id 
     * @param name User's name
     * @param surname User's surname
     * @param email User's email.
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
     * @brief `static` method to check whether a String is a correct User ID
     * @param id The String to check
     * @return `True` if the User ID is valid
     * @todo implement, update doxy
     */
    public static boolean isValidID(String id) {
        // TODO: implement
        return false;
    }

    public static boolean isValidEmail(String email) {
        // TODO: implement
        return false;
    }

    public boolean canBorrow() {
        // TODO: implement
        return false;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }
    
    public String getEmail() {
        return email;
    }

    public void setId(String id) {
        if (!User.isValidID(id))
            throw new UserDataNotValidException("Invalid ID: " + id);

        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        if (!User.isValidEmail(email))
            throw new UserDataNotValidException("Invalid email: " + email);

        this.email = email;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return super.toString();
    }

    //TODO: Add methods overrides to ensure the correct functionality of the UserSet

}
