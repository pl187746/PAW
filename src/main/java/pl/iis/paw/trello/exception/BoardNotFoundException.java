package pl.iis.paw.trello.exception;

public class BoardNotFoundException extends NotFoundException {

	private static final long serialVersionUID = -5651823546030602024L;

	public BoardNotFoundException(String message) {
        super(message);
    }

    public BoardNotFoundException(Long boardId) {
        super("Board with id " + boardId + " was not found");
    }
    
}
