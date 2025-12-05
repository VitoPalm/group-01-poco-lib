package poco.company.group01pocolib.exceptions;

public class UserDataNotValidException extends RuntimeException {
    public UserDataNotValidException() {
        super();
    }

    public UserDataNotValidException(String message) {
        super(message);
    }
}
