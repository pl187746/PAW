package pl.iis.paw.trello.exception;

import java.io.IOException;

public class StorageException extends RuntimeException {

    public StorageException() { }

    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, IOException e) {
        super(message, e);
    }
}
