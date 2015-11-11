package benak.tomas.crimemonitor.library;

import java.io.Serializable;

/**
 * A base class associating the contained crime statistics with a particular crime data interval.
 * <p>
 * This is just a base class, containing no crime statistics, only the police department data along
 * with the crime data interval.
 * It acts as a logical abstract base class for other classes that need to associate their
 * crime statistics with a police department and a crime data interval.
 */
public class CrimeSummaryWithUtvarAndIntervalBase extends CrimeSummaryWithUtvarBase implements UtvarAndIntervalBasedCrimeSummary, Serializable
{
	/**
	 * the helper identifier used to identify this particular version of class for the serialization purposes
	 */
	private static final long	serialVersionUID	= 42L;
	
	/**
	 * the initial year of the crime data interval
	 */
	protected final int mStartYear;
	/**
	 * the initial month of the crime data interval
	 */
	protected final int mStartMonth;
	/**
	 * the terminal year of the crime data interval
	 */
	protected final int mEndYear;
	/**
	 * the terminal month of the crime data interval
	 */
	protected final int mEndMonth;
	
	/**
	 * Constructs a new {@code CrimeSummaryWithUtvarAndIntervalBase} using a provided
	 * police department code and name and a crime data interval.
	 * 
	 * @param kodUtvaruSpachaniNeboZjisteni the police department code
	 * @param nazevUtvaruSpachaniNeboZjisteni the police department name
	 * @param startYear the initial year of the crime data interval
	 * @param startMonth the initial month of the crime data interval
	 * @param endYear the terminal year of the crime data interval
	 * @param endtMonth the terminal month of the crime data interval
	 */
	public CrimeSummaryWithUtvarAndIntervalBase(String kodUtvaruSpachaniNeboZjisteni, String nazevUtvaruSpachaniNeboZjisteni,
			int startYear, int startMonth, int endYear, int endtMonth)
	{
		super(kodUtvaruSpachaniNeboZjisteni, nazevUtvaruSpachaniNeboZjisteni);
		
		this.mStartYear = startYear;
		this.mStartMonth = startMonth;
		this.mEndYear = endYear;
		this.mEndMonth = endtMonth;
	}

	/**
	 * Returns the initial year of the crime data interval associated with the contained crime statistics.
	 * 
	 * @return the initial year of the crime data interval associated with the contained crime statistics
	 */
	public int getStartYear()
	{
		return this.mStartYear;
	}

	/**
	 * Returns the initial month of the crime data interval associated with the contained crime statistics.
	 * 
	 * @return the initial month of the crime data interval associated with the contained crime statistics
	 */
	public int getStartMonth()
	{
		return this.mStartMonth;
	}

	/**
	 * Returns the terminal year of the crime data interval associated with the contained crime statistics.
	 * 
	 * @return the terminal year of the crime data interval associated with the contained crime statistics
	 */
	public int getEndYear()
	{
		return this.mEndYear;
	}

	/**
	 * Returns the terminal month of the crime data interval associated with the contained crime statistics.
	 * 
	 * @return the terminal month of the crime data interval associated with the contained crime statistics
	 */
	public int getEndMonth()
	{
		return this.mEndMonth;
	}
}
