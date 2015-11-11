package benak.tomas.crimemonitor.service.exception;

public class InvalidSparqlEndpointIdException extends IllegalArgumentException {

	public InvalidSparqlEndpointIdException(String id) {
		super(id);
	}

}
