package benak.tomas.crimemonitor.library;

import java.io.Serializable;

/**
 * A class used to represent a crime ranking for all kinds of crimes.
 * <p>
 * The crime ranking is an ordered list of crime items.
 * <p>
 * The crime item is a {@code CrimeSummary}.
 * It contains the TSK identifier of a crime kind, the crime kind's name
 * and the number of committed crimes for that particular kind.
 * <p>
 * The crime items in the ranking are ordered by the committed crimes count in the descending order.
 * 
 * @author Tom
 *
 * @see CrimeSummary
 */
public class CrimesList extends CrimeSummaryWithUtvarAndIntervalBase implements
		Serializable
{
	/**
	 * the helper identifier used to identify this particular version of class for the serialization purposes
	 */
	private static final long		serialVersionUID	= 42L;

	/**
	 * the ordered list of crime items
	 */
	private final CrimeSummary[]	mCrimeItems;

	/**
	 * Constructs a new {@code CrimesList} for a given department and crime data interval,
	 * using a provided ordered list of crime items.
	 * 
	 * @param kodUtvaruSpachaniNeboZjisteni the department code
	 * @param nazevUtvaruSpachaniNeboZjisteni the department name
	 * @param startYear the crime data interval initial year
	 * @param startMonth the crime data interval initial month
	 * @param endYear the crime data interval terminal year
	 * @param endMonth the crime data interval terminal month
	 * @param crimeItems the ordered list of crime items
	 */
	public CrimesList(String kodUtvaruSpachaniNeboZjisteni, String nazevUtvaruSpachaniNeboZjisteni, int startYear,
			int startMonth, int endYear, int endMonth, CrimeSummary[] crimeItems)
	{
		super(kodUtvaruSpachaniNeboZjisteni, nazevUtvaruSpachaniNeboZjisteni,
				startYear, startMonth, endYear, endMonth);

		mCrimeItems = crimeItems;
	}

	/**
	 * Returns the ordered list of crime items.
	 * <p>
	 * The crime items are ordered by the committed crimes count in the descending order.
	 * 
	 * @return the ordered list of crime items
	 */
	public CrimeSummary[] getCrimeItems()
	{
		return mCrimeItems;
	}
}
