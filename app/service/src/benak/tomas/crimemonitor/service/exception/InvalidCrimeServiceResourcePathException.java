package benak.tomas.crimemonitor.service.exception;

// TODO move to CrimeServiceResourceProvider?
public class InvalidCrimeServiceResourcePathException extends Exception
{
	private final String mResourcePath;

	public InvalidCrimeServiceResourcePathException(String path)
	{
		super("Specified path for the resource is invalid.");

		if (path == null)
			throw new IllegalArgumentException("'path' cannot be null");

		mResourcePath = path;
	}

	public String getResourcePath()
	{
		return mResourcePath;
	}

}
