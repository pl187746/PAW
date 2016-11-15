package pl.iis.paw.trello.exception;

public class StorageException extends RuntimeException {

    public StorageException() { }

    public StorageException(String message) {
        super(message);
    }
}
