/**
 * @file BookDataNotValidException.java
 * @brief This file contains the definition of the IsbnNotValidException class.
 * @author Francesco Marino, Giovanni Orsini
 * @date 6 December 2025
 */

package poco.company.group01pocolib.exceptions;

/**
 * @class BookDataNotValidException
 * @author Francesco Marino
 * @brief This exception is thrown when an attempt to set an incorrect ISBN for a {@link poco.company.group01pocolib.mvc.model.Book Book} is detected  
 */
public class BookDataNotValidException extends RuntimeException {
    /**
     * @brief Default constructor  
     */
    public BookDataNotValidException(){
        super();
    }

    /**
     * @brief Constructor with a message
     * @param msg The message describing the error and the invalid value.
     */
    public BookDataNotValidException(String msg){
        super(msg);
    }
}
