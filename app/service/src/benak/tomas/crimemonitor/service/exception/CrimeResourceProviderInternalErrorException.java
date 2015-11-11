package benak.tomas.crimemonitor.service.exception;

import java.io.IOException;

//TODO move to CrimeServiceResourceProvider?
public class CrimeResourceProviderInternalErrorException extends Exception
{

	public CrimeResourceProviderInternalErrorException(final String message,
			final IOException cause)
	{
		super(message, cause);
	}

}
