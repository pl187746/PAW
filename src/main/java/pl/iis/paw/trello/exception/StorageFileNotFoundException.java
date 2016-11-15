package pl.iis.paw.trello.exception;

public class StorageFileNotFoundException extends NotFoundException {

    public StorageFileNotFoundException() { }

    public StorageFileNotFoundException(String message) {
        super(message);
    }
}
