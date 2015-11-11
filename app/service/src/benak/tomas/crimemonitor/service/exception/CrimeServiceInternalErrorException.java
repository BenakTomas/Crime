package benak.tomas.crimemonitor.service.exception;


public class CrimeServiceInternalErrorException extends Exception
{

	public CrimeServiceInternalErrorException(final String message)
	{
		super(message);
	}

	public CrimeServiceInternalErrorException(final String message,
			final Exception cause)
	{
		super(message, cause);
	}
	
}
