package benak.tomas.crimemonitor.client.exception;

/**
 * An exception class used to represent an error that occurred when
 * loading the crime related data.
 * <p>
 * This class is used to report an error that occurred when for example
 * the department code could not be loaded because the connection encountered
 * the connection or read timeout.
 * <p>
 * The detailed description is enclosed in the message part of this exception
 * as well as in the cause part where the original exception is stored.
 * 
 * @author Tom
 */
public class DataLoadingTaskException extends Exception
{
	/**
	 * the helper identifier used to identify this particular version of class for the serialization purposes
	 */
	private static final long	serialVersionUID	= 42L;
	
	/**
	 * the detailed error message
	 */
	private final String mMessage;
	/**
	 * the original exception, that caused this exception to be thrown
	 */
	private final Throwable mCause;
	
	/**
	 * Constructs a new {@code DataLoadingTaskException} using a provided detailed message
	 * and an original exception.
	 * <p>
	 * The original exception is considered the cause for this exception.
	 * 
	 * @param message the detailed error information message for this exception
	 * @param cause the original exception, that caused this exception to be thrown
	 */
	public DataLoadingTaskException(String message, Throwable cause)
	{
		mMessage = message;
		mCause = cause;
	}
	
	@Override
	public Throwable getCause()
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
