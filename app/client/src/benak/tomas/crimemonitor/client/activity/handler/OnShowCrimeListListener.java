package benak.tomas.crimemonitor.client.activity.handler;

/**
 * The interface used to report the request to display the complete crime list
 * to the parent activity.
 * <p>
 * The parent activity must implement this interface to get the notifications.
 * If it does not, the runtime ClassCastException is thrown.
 * 
 * @author Tom
 * 
 */
public interface OnShowCrimeListListener
{
	/**
	 * Called to deliver the request to display the complete crime list.
	 */
	void showCrimeList();
}
