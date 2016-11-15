package pl.iis.paw.trello.exception;

/**
 * Created by Krystian on 2016-11-05.
 */
public class CommentNotFoundException extends NotFoundException {

    private static final long serialVersionUID = 1272218797180737538L;

    public CommentNotFoundException(String message) {
        super(message);
    }

    public CommentNotFoundException(Long commentId) {
        super("Comment with id: " + commentId + " was not found");
    }
}