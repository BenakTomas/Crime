package benak.tomas.crimemonitor.service.exception;

public class InvalidSparqlQueryIdException extends IllegalArgumentException {

	public InvalidSparqlQueryIdException(String id) {
		super(id);
	}

}
