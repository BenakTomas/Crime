package benak.tomas.crimemonitor.client.activity.handler;

/**
 * Allows to report and listen for the crime selection change.
 * 
 * <p>
 * When a particular crime is selected, an implementing class is notified
 * and provided with a TSK identifier of the selected crime. 
 *   
 * @author Tom
 */
public interface OnCrimeSelectedListener
{
	/**
	 * Called to report the selected crime.
	 * 
	 * @param tsk the TSK identifier of the selected crime
	 */
	public void onCrimeSelected(int tsk);
}
