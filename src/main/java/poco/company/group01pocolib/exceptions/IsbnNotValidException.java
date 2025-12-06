/**
 * @file IsbnNotValidException.java
 * @brief This file contains the definition of the IsbnNotValidException class.
 * @author Francesco Marino
 * @date 6 December 2025
 */

package poco.company.group01pocolib.exceptions;

/**
 * @class IsbnNotValidException
 * @brief This exception is thrown when an invalid ISBN is detected  
 */
public class IsbnNotValidException extends RuntimeException {
    
    /**
     * @brief Default constructor  
     */
    public IsbnNotValidException(){
        super();
    }

    /**
     * @brief Constructor with a message
     * @param msg The message describing the error and the invalid ISBN value.
     */
    public IsbnNotValidException(String msg){
        super(msg);
    }
}
