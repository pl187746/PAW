package pl.iis.paw.trello.exception;

public class CardListNotFoundException extends NotFoundException {

	private static final long serialVersionUID = 7647232019736235239L;

	public CardListNotFoundException(String message) {
        super(message);
    }

    public CardListNotFoundException(Long listId) {
        super("List with id " + listId + " was not found");
    }
    
}
