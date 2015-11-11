package benak.tomas.crimemonitor.client.activity.handler;

/**
 * Allows to report and listen for the crime data interval selection change.
 * 
 * <p>
 * This implementation: This interface is used by a parent activity and its crime data interval dialog.
 * Parent activity acts as a listener and the dialog acts as a notifier.
 *   
 * @author Tom
 */
public interface OnCrimeIntervalSelected {
	/**
	 * Called to report the selected crime data interval.
	 * <p>
	 * The newly selected values for the crime data interval are passed as the parameters of this
	 * method.
	 * 
	 * @param startYear the initial year if the selected crime data interval
	 * @param startMonth the initial month if the selected crime data interval
	 * @param endYear the terminal year if the selected crime data interval
	 * @param endMonth the terminal month if the selected crime data interval
	 */
	public void onCrimeIntervalSelected(int startYear, int startMonth, int endYear, int endMonth);
}