package pl.iis.paw.trello.exception;

public class TeamNotFoundException extends NotFoundException {

	private static final long serialVersionUID = -4763254530761227130L;

	public TeamNotFoundException(String message) {
        super(message);
    }

    public TeamNotFoundException(Long teamId) {
        super("Team with id " + teamId + " was not found");
    }
   
}
