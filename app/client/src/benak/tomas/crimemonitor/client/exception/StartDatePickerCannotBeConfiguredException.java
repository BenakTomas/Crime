package benak.tomas.crimemonitor.client.exception;

/**
 * An exception class used to represent a date picker configuration error.
 * <p>
 * When trying to configure the DatePicker not to show its day picker part,
 * an exception might occur. This situation is recognized as an error state
 * and reported using this exception class.
 * 
 * @author Tom
 */
public class StartDatePickerCannotBeConfiguredException extends Exception
{
	/**
	 * the helper identifier used to identify this particular version of class for the serialization purposes
	 */
	private static final long	serialVersionUID	= 42L;

	/**
	 * Constructs a new {@code StartDatePickerCannotBeConfiguredException} using a provided
	 * exception as the cause for this exception to be thrown.
	 * 
	 *  @param e the original exception, that caused this exception to be thrown
	 */
	public StartDatePickerCannotBeConfiguredException(Exception e)
	{
	}
}
