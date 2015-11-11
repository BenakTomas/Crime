package benak.tomas.crimemonitor.service.exception;

public class InvalidNamedGraphIdException extends IllegalArgumentException {

	public InvalidNamedGraphIdException(String namedGraphId) {
		super(namedGraphId);
	}

}
