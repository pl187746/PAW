package pl.iis.paw.trello.exception;

public class AttachmentNotFoundException extends NotFoundException {

    public AttachmentNotFoundException() {
    }

    public AttachmentNotFoundException(String message) {
        super(message);
    }

    public AttachmentNotFoundException(Long attachmentId) {
        super("Attachment with id " + attachmentId + " was not found");
    }
}
