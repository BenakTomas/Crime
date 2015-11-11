package benak.tomas.crimemonitor.client.exception;

import java.net.SocketTimeoutException;

/**
 * An exception class used to represent the crime data loading timeout.
 * <p>
 * When a connect or read timeout for a crime data loading connection occurs,
 * this exception is thrown to represent that state as a crime data loading error condition.
 * 
 * @author Tom
 */
public class DataLoadingTaskTimeoutExceededException extends DataLoadingTaskException
{
	/**
	 * the helper identifier used to identify this particular version of class for the serialization purposes
	 */
	private static final long	serialVersionUID	= 42L;

	/**
	 * Constructs a new {@code DataLoadingTaskTimeoutExceededException} using a provided
	 * exception as the cause for this exception to be thrown.
	 * 
	 * @param e the original exception, that caused this exception to be thrown
	 */
	public DataLoadingTaskTimeoutExceededException(SocketTimeoutException e)
	{
		super("Timeout expired for data loading task.", e);
	}

}
