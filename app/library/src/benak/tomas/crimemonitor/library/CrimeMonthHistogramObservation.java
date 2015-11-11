package benak.tomas.crimemonitor.library;

import java.io.Serializable;

/**
 * A class used to represent an observation of the number of committed crimes
 * during a particular month in a particular year.
 * <p>
 * The time ordered series of objects of this type constitutes a so called
 * crime count monthly histogram.
 * <p>
 * The crime count monthly histogram is a histogram, where for each month there is
 * an observation of the number of the crimes committed in thar month.
 * The months in the histogram are ordered chronologically.
 * There is a starting and ending month and there are no observation gaps between them.
 * 
 * @author Tom
 */
public class CrimeMonthHistogramObservation implements
Serializable
{
	/**
	 * the helper identifier used to identify this particular version of class for the serialization purposes
	 */
	private static final long	serialVersionUID	= 42L;
	
	/**
	 * the year when the observation was taken
	 */
	private final int	mYear;
	/**
	 * the month within the year when the observation was taken
	 */
	private final int	mMonth;
	/**
	 * the measured number of committed crimes
	 */
	private final int	mCrimeCount;

	/**
	 * Constructs a new {@code CrimeMonthHistogramObservation} using a provided year, month
	 * and a number of committed crimes.
	 * 
	 * @param year the year number
	 * @param month the month number
	 * @param crimeCount the number of committed crimes
	 */
	public CrimeMonthHistogramObservation(int year, int month, int crimeCount)
	{
		if (year < 0)
			throw new IllegalArgumentException(
					"year must be a valid year value");

		if (month < 0 || month > 11)
			throw new IllegalArgumentException(
					"month must be a valid month value from 1 to 12");

		if (crimeCount < 0)
			throw new IllegalArgumentException(
					"crimeCount must not be smaller than 0");

		mYear = year;
		mMonth = month;
		mCrimeCount = crimeCount;
	}

	/**
	 * Returns the number of the year when this observations was taken.
	 * 
	 * @return the number of the year when this observations was taken
	 */
	public final int getYear()
	{
		return mYear;
	}

	/**
	 * Returns the number of the month when this observations was taken.
	 * 
	 * @return the number of the month when this observations was taken
	 */
	public final int getMonth()
	{
		return mMonth;
	}

	/**
	 * Returns the number of the crimes that have been committed during the observed month and year. 
	 * 
	 * @return the number of the crimes that have been committed during the observed month and year
	 */
	public final int getCrimeCount()
	{
		return mCrimeCount;
	}
}
