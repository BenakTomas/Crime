package benak.tomas.crimemonitor.library;

import java.io.Serializable;

/**
 * A class used to provide the crime statistics for a particular kind of crime.
 * <p>
 * A kind of crime is identified by a TSK identifier.
 * <p>
 * The statistics provided are:
 * <ul>
 * <li>the calculated number of cases when a crime of the given kind occurred
 * <li>the 
 * <li>crime count monthly histogram (see {@link CrimeMonthHistogramObservation} for a more detailed definition
 * of a crime count monthly histogram)
 * 
 * @author Tom
 */
public class CrimeSummaryDetailed extends CrimesSummaryBasic implements
		Serializable
{
	/**
	 * the helper identifier used to identify this particular version of class for the serialization purposes
	 */
	private static final long						serialVersionUID	= 42L;
	
	/**
	 * the TSK identifier of the crime
	 */
	private final int								mTsk;
	/**
	 * the crime name
	 */
	private final String							mCrimeName;
	/**
	 * the crime count monthly histogram for the crime
	 */
	private final CrimeMonthHistogramObservation[]	mMonthHistogram;
	
	/**
	 * Constructs a new {@code CrimeSummaryDetailed} using a provided 
	 * @param tsk
	 * @param crimeName
	 * @param startYear
	 * @param startMonth
	 * @param endYear
	 * @param endMonth
	 * @param crimeNumber
	 * @param crimeIndex
	 * @param objasnenost
	 * @param trend
	 * @param kodUtvaruSpachaniNeboZjisteni
	 * @param nazevUtvaruSpachaniNeboZjisteni
	 * @param monthHistogram
	 */
	public CrimeSummaryDetailed(int tsk, String crimeName,
			int startYear, int startMonth,
			int endYear, int endMonth,
			float crimeIndex, float objasnenost,
			CrimeMeasureTrend trend,
			String kodUtvaruSpachaniNeboZjisteni, String nazevUtvaruSpachaniNeboZjisteni,
			final CrimeMonthHistogramObservation[] monthHistogram)
	{
		super(crimeIndex, objasnenost, trend, kodUtvaruSpachaniNeboZjisteni,nazevUtvaruSpachaniNeboZjisteni, startYear, startMonth, endYear, endMonth);
		
		if(tsk < 0)
			throw new IllegalArgumentException("tsk is invalid");
		if(crimeName == null)
			throw new NullPointerException("crimeName");
		if(crimeIndex != Float.NaN && (crimeIndex < 0 || crimeIndex > 1))
			throw new IllegalArgumentException(String.format("0<= crimeIndex <= 1 must hold for crimeIndex. Current value is %.20f", crimeIndex));
		if(objasnenost < 0 || objasnenost > 1)
			throw new IllegalArgumentException("0<= objasnenost <= 1 must hold for objasnenost");
		if(monthHistogram == null)
			throw new NullPointerException("monthHistogram");
		
		// TODO: kontrola vstupu
		mTsk = tsk;
		mCrimeName = crimeName;
		mMonthHistogram = monthHistogram;
	}

	/**
	 * Returns a crime count monthly histogram for the crime.
	 * 
	 * @return the crime count monthly histogram for the crime
	 */
	public CrimeMonthHistogramObservation[] getMonthHistogram()
	{
		return mMonthHistogram;
	}

	/**
	 * Returns the TSK identifier of the crime.
	 * 
	 * @return the TSK identifier of the crime
	 */
	public final int getTsk()
	{
		return this.mTsk;
	}

	/**
	 * Returns the name of the crime.
	 * 
	 * @return the name of the crime
	 */
	public final String getCrimeName()
	{
		return this.mCrimeName;
	}
}
