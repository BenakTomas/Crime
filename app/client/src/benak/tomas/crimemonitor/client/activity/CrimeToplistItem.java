package benak.tomas.crimemonitor.client.activity;

/**
 * CrimeToplistItem is a class used to logically represent a toplist item in a crime toplist.
 * <p>The toplist item holds information about
 * <ul>
 * <li>the TSK crime identifier
 * <li>the crime name
 * <li>the number of committed crimes
 * <li>the item color (used in the view displaying this item)
 * 
 * @author Tom
 *
 */
public class CrimeToplistItem
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
	 * the color associated with this toplist item.
	 * <p>This color is used in the related view class displaying this toplist item.
	 */
	private final int mColor;
	
	/**
	 * The constructor.
	 * <p>
	 * The toplist item is constructed using the passed values for all of the member fields.
	 * 
	 * @param tsk the TSK identifier
	 * @param crimeName the crime item
	 * @param value the number of committed crimes 
	 * @param color the associated color
	 */
	public CrimeToplistItem(final int tsk, final String crimeName, final int value, final int color)
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
		mColor = color;
	}

	/**
	 * Returns the crime name for this toplist item.
	 * 
	 * @return the crime name for this toplist item
	 */
	public final String getCrimeName()
	{
		return this.mCrimeName;
	}
	
	/**
	 * Returns the number of committed crimes for this toplist item.
	 * 
	 * @return the number of committed crimes for this toplist item
	 */
	public final int getValue()
	{
		return this.mValue;
	}

	/**
	 * Returns the associated color for this toplist item.
	 * 
	 * @return the associated color for this toplist item
	 */
	public final int getColor()
	{
		return this.mColor;
	}

	/**
	 * Returns the TSK identifier for this toplist item.
	 * 
	 * @return the TSK identifier for this toplist item
	 */
	public final int getTsk()
	{
		return this.mTsk;
	}
}
