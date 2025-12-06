/**
 * @file UserDataNotValidException.java
 * @brief This file contains the definition of the UserDataNotValidException class.
 * @author Giovanni Orsini
 * @date 5 December 2025
 */

package poco.company.group01pocolib.exceptions;

/**
 * @class UserDataNotValidException
 * @author Giovanni Orsini
 * @brief This exception is thrown when an attempt to set an incorrect ID or Email for a {@link poco.company.group01pocolib.mvc.model.User User} is detected
 */
public class UserDataNotValidException extends RuntimeException {
    /**
     * @brief Default constructor  
     */
    public UserDataNotValidException() {
        super();
    }

    /**
     * @brief Constructor with a message
     * @param msg The message describing the error and the invalid value.
     */
    public UserDataNotValidException(String message) {
        super(message);
    }
}
