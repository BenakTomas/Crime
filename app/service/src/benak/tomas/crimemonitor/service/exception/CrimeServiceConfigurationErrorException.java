package benak.tomas.crimemonitor.service.exception;

public class CrimeServiceConfigurationErrorException extends Exception
{
	private static final long	serialVersionUID	= 42L;
	
	private final Exception mCause;
	private final String mMessage;
	
	public CrimeServiceConfigurationErrorException()
	{
		mCause = null;
		mMessage = null;
	}
	
	public CrimeServiceConfigurationErrorException(String message, Exception cause)
	{
		mCause = cause;
		mMessage = message;
	}
	
	@Override
	public synchronized Throwable getCause()
	{
		if(mCause != null)
				return mCause;
		else
			return super.getCause();
	}
	
	@Override
	public String getMessage()
	{
		if(mMessage != null)
			return mMessage;
		else
			return super.getMessage();
	}
}
