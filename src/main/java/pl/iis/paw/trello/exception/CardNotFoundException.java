package pl.iis.paw.trello.exception;

public class CardNotFoundException extends NotFoundException {

	private static final long serialVersionUID = -2807384835920670609L;

	public CardNotFoundException(String message) {
        super(message);
    }

    public CardNotFoundException(Long cardId) {
        super("Card with id " + cardId + " was not found");
    }
    
}
