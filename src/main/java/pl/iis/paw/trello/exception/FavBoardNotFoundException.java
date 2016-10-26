package pl.iis.paw.trello.exception;

public class FavBoardNotFoundException extends NotFoundException {

	private static final long serialVersionUID = -6481501132921680169L;

	public FavBoardNotFoundException(String message) {
        super(message);
    }

    public FavBoardNotFoundException(Long favBoardId) {
        super("FavBoard with id " + favBoardId + " was not found");
    }
    
}
