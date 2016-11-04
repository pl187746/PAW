package pl.iis.paw.trello.exception;

public class LabelNotFoundException extends NotFoundException {

	private static final long serialVersionUID = -2937171177461068517L;

	public LabelNotFoundException(String message) {
        super(message);
    }

    public LabelNotFoundException(Long labelId) {
        super("Label with id " + labelId + " was not found");
    }

}
