package pl.iis.paw.trello.exception;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String identifier) {
        super("User " + identifier + " already exists");
    }
}
