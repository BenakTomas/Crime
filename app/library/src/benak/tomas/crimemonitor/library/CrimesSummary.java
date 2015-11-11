package benak.tomas.crimemonitor.library;

import java.io.Serializable;

/**
 * A class used to provide the statistics for all crimes for a particular police
 * department and crime data interval.
 * 
 * <p>
 * All of the contained statistics relate to the given police department and the given
 * crime data interval.
 * 
 * <p>
 * The statistics provided are:
 * <ul>
 * <li>the crime index
 * <li>the crime solved ratio
 * <li>the crime trend
 * <li>the logical chart of crime counts for each crime kind (TSK)
 * </ul>
 * 
 * <p>
 * The chart of crime counts for each crime kind is a series of {@code CrimeSummary} observations
 * for each crime kind.
 * Each observation includes the TSK identifier, the crime's name and the number of committed crimes
 * of that kind.
 * 
 * @author Tom
 * 
 * @see CrimeSummary
 */
public class CrimesSummary extends CrimesSummaryBasic implements Serializable
{
	/**
	 * the helper identifier used to identify this particular version of class
	 * for the serialization purposes
	 */
	private static final long		serialVersionUID	= 42L;

	/**
	 * the series of crime count observations for each crime kind
	 */
	private final CrimeSummary[]	mCrimeSummaries;

	/**
	 * Constructs a new {@code CrimesSummary} for a given police department
	 * and crime data interval, using the provided values for crimes solved ratio,
	 * crime index, crime trend and the provided series of observations of crime counts for each crime kind.
	 * 
	 * @param kodUtvaruSpachaniNeboZjisteni the given police department code
	 * @param nazevUtvaruSpachaniNeboZjisteni the given police department name
	 * @param startYear the given crime data interval initial year
	 * @param startMonth the given crime data interval initial month
	 * @param endYear the given crime data interval terminal year
	 * @param endMonth the given crime data interval terminal month
	 * @param objasnenost the crimes solved ratio value
	 * @param crimeIndex the crime index value
	 * @param trend the crime trend value
	 * @param summaries the series of observations of crime counts for each crime kind
	 */
	public CrimesSummary(String kodUtvaruSpachaniNeboZjisteni,
			String nazevUtvaruSpachaniNeboZjisteni, int startYear,
			int startMonth, int endYear, int endMonth, float objasnenost,
			float crimeIndex, CrimeMeasureTrend trend, CrimeSummary[] summaries)
	{
		super(crimeIndex, objasnenost, trend, kodUtvaruSpachaniNeboZjisteni,
				nazevUtvaruSpachaniNeboZjisteni, startYear, startMonth,
				endYear, endMonth);

		this.mCrimeSummaries = summaries;
	}

	/**
	 * Returns the series of observations of crime counts for each crime kind.
	 * 
	 * @return the series of observations of crime counts for each crime kind
	 */
	public CrimeSummary[] getCrimeSummaries()
	{
		return mCrimeSummaries;
	}
}
