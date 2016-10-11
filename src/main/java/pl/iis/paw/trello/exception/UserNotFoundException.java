package pl.iis.paw.trello.exception;

public class UserNotFoundException extends NotFoundException {

	private static final long serialVersionUID = -5131500150931084053L;

	public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(Long userId) {
        super("User with id " + userId + " was not found");
    }
}
