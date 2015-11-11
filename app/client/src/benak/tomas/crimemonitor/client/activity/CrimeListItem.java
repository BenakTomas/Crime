package benak.tomas.crimemonitor.client.activity;


/**
 * CrimeListItem is a class used to logically represent the item in a list of crimes.
 * <p>The list of crimes is displayed on the crime list activity page.
 * The list is displayed in a list view using the {@code CrimeListAdapter}.
 * 
 * @author Tom
 * 
 * @see CrimeListAdapter
 * @see CrimesListActivity
 *
 */
public class CrimeListItem
{
	/**
	 * the TSK crime identifier
	 */
	private final int mTsk;
	/**
	 * the crime name
	 */
	private final String mCrimeName;
	/**
	 * the number of committed crimes
	 */
	private final int mValue;
	
	/**
	 * The constructor.
	 * <p>
	 * The crime list item is constructed using the passed values for all of the member fields.
	 * 
	 * @param tsk the TSK identifier
	 * @param crimeName the crime item
	 * @param value the number of committed crimes
	 */
	public CrimeListItem(final int tsk, final String crimeName, final int value)
	{
		if(tsk <= 0)
			throw new IllegalArgumentException("invalid 'tsk' param value");
		
		if(value < 0)
			throw new IllegalArgumentException("'value' cannot be smaller than 0.");
		
		if(crimeName == null)
			throw new IllegalArgumentException("crimeName cannot be null");
		
		mTsk = tsk;
		mCrimeName = crimeName;
		mValue = value;
	}

	/**
	 * Returns the crime name for this crime list item.
	 * 
	 * @return the crime name for this crime list item
	 */
	public final String getCrimeName()
	{
		return this.mCrimeName;
	}
	
	/**
	 * Returns the number of committed crimes for this crime list item.
	 * 
	 * @return the number of committed crimes for this crime list item
	 */
	public final int getValue()
	{
		return this.mValue;
	}

	/**
	 * Returns the TSK identifier for this crime list item.
	 * 
	 * @return the TSK identifier for this crime list item
	 */
	public final int getTsk()
	{
		return this.mTsk;
	}
}
