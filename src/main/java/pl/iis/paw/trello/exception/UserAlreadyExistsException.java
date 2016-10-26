package pl.iis.paw.trello.exception;

public class UserAlreadyExistsException extends RuntimeException {

    private Field field;

    public enum Field {
        Login("login exists"),
        Email("email exists"),
        All("login and email exists");

        private String errorMessage;

        Field(String message) {
            this.errorMessage = message;
        }

        @Override
        public String toString() {
            return errorMessage;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }

    public UserAlreadyExistsException(String identifier, Field field) {
        super("User " + identifier + " already exists");
        this.field = field;
    }

    @Override
    public String getMessage() {
        return field.getErrorMessage();
    }
}
