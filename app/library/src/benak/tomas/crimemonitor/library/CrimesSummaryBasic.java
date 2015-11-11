package benak.tomas.crimemonitor.library;

import java.io.Serializable;

/**
 * A base class used to hold some basic crime statistics related to a particular department and crime data interval.
 * <p>
 * The included base crime statistics are:
 * <ul>
 * <li>the crime index
 * <li>the crime solved ratio
 * <li>the crime trend
 * </ul>
 * 
 * <p>
 * Whenever we talk about the crimes or committed crimes, we mean the crimes
 * that are associated with the given police department and the given crime data interval.
 * 
 * <h1>Crime index</h1>
 * <p>Crime index is a number from [0,1].
 * To calculate it we need to get the the crimes per citizen number.
 * <br />
 * There is {@code p} citizens living in the territory of the given police department.
 * <br />
 * There is {@code c} number of committed crimes.
 * <br />
 * Then {@code c_p} is "crimes per capita" for the given department and the crime data interval
 * and is calculated using {@code c_p = (c / p)}.
 * 
 * <p>
 * The crimes per capita is calculated for every police department and the given crime data interval.
 * The maximum value is {@code c_p_max}.
 * 
 * <p>Now the crime index value {@code ci} is calculated by norming the {@code c_p} by {@code c_p_max}
 * as {@code ci = (c_p / c_p_max)}.
 * 
 * <h1>Crime solved ratio</h1>
 * <p>
 * This is the ratio between the number of the solved crimes and the total number of committed crimes.
 * 
 * <h1>Crime trend</h1>
 * <p>
 * Crime trend is a measure used to describe the fact, whether the crime counts observed during the
 * months of the crime data interval tend to go up, are stable, or tend to go down.
 * <br />
 * The value of this measure is intended to give a hint about what the crime count might be (mainly) in future.
 * <h2>The calculation method</h2>
 * Crime trend is calculated for the series of observations related to a particular month, also called
 * the crime count monthly histogram.
 * <br />
 * See {@link CrimeMonthHistogramObservation} for the definition of the term.
 * <p>
 * Simple linear regression is used to approximate the histogram graph and
 * the resulting crime trend value is determined by the slope of the approximating
 * line.
 * For some {@code e > 0} and the slope {@code s} we determine the trend value as follows:
 * <ul>
 * <li>{@code s > e}: {@link CrimeMeasureTrend#Up}
 * <li>{@code -e <= s} and {@code s <= e  }: {@link CrimeMeasureTrend#Stable}
 * <li>{@code s < -e}: {@link CrimeMeasureTrend#Down}
 * 
 * @author Tom
 *
 * @see CrimeMeasureTrend
 * @see CrimeMonthHistogramObservation
 * @see CrimeSummaryDetailed a class providing a crime count monthly histogram
 */
public class CrimesSummaryBasic extends CrimeSummaryWithUtvarAndIntervalBase implements Serializable
{
	/**
	 * the helper identifier used to identify this particular version of class for the serialization purposes
	 */
	private static final long	serialVersionUID	= 42L;
	
	/**
	 * the crime index
	 */
	private final float mCrimeIndex;
	/**
	 * the crime solved ratio
	 */
	private final float mObjasnenost;
	/**
	 * the crime trend
	 */
	private final CrimeMeasureTrend mTrend;	// trend of the measure of the committed crimes
	
	/**
	 * Constructs a new {@code CrimesSummaryBasic} using the provided values for crime index,
	 * crime solved ratio and crime trend along with the provided department code and name and the crime
	 * data interval.
	 * 
	 * @param crimeIndex the crime index value
	 * @param objasnenost the crime solved ratio value
	 * @param trend the crime trend value
	 * @param kodUtvaruSpachaniNeboZjisteni the police department code associated with the statistics
	 * @param nazevUtvaruSpachaniNeboZjisteni the police department name associated with the statistics
	 * @param startYear the initial year of the crime data interval associated with the statistics
	 * @param startMonth the initial month of the crime data interval associated with the statistics
	 * @param endYear the terminal year of the crime data interval associated with the statistics
	 * @param endtMonth the terminal month of the crime data interval associated with the statistics
	 */
	public CrimesSummaryBasic(float crimeIndex, float objasnenost,
			CrimeMeasureTrend trend, String kodUtvaruSpachaniNeboZjisteni, String nazevUtvaruSpachaniNeboZjisteni,
			int startYear,
			int startMonth, int endYear, int endtMonth)
	{
		super(kodUtvaruSpachaniNeboZjisteni, nazevUtvaruSpachaniNeboZjisteni,
				startYear, startMonth, endYear, endtMonth);
		
		mCrimeIndex = crimeIndex;
		mObjasnenost = objasnenost;
		mTrend = trend;
	}

	/**
	 * Returns the value of the crime index for the given department and crime data interval.
	 * 
	 * @return the value of the crime index for the given department and crime data interval
	 */
	public final float getCrimeIndex()
	{
		return mCrimeIndex;
	}

	/**
	 * Returns the crimes solved ratio for the given department and crime data interval.
	 * 
	 * @return the crimes solved ratio for the given department and crime data interval
	 */
	public final float getObjasnenost()
	{
		return mObjasnenost;
	}

	/**
	 * Returns the crime trend value for the given department and crime data interval.
	 * 
	 * @return the crime trend value for the given department and crime data interval
	 */
	public final CrimeMeasureTrend getTrend()
	{
		return mTrend;
	}
}
