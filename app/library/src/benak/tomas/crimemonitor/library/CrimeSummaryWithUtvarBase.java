package benak.tomas.crimemonitor.library;

import java.io.Serializable;

/**
 * A base class associating the contained crime statistics with a particular police department and code.
 * <p>
 * This is just a base class, containing no crime statistics, only the police department data.
 * It acts as a logical abstract base class for other classes that need to associate their
 * crime statistics with a police department.
 * 
 * @author Tom
 */
public class CrimeSummaryWithUtvarBase implements ICrimeDataBase, Serializable
{
	/**
	 * the helper identifier used to identify this particular version of class for the serialization purposes
	 */
	private static final long	serialVersionUID	= 42L;
	
	/**
	 * the police department code
	 */
	protected final String mKodUtvaruSpachaniNeboZjisteni;
	/**
	 * the police department name
	 */
	protected final String mNazevUtvaruSpachaniNeboZjisteni;
	
	protected final float mLatitude;
	protected final float mLongitude;
	
	protected final float mLastLatitude;
	protected final float mLastLongitude;
	
	
	/**
	 * Constructs a new {@code CrimeSummaryWithUtvarBase} using a provided police
	 * department code and name.
	 * 
	 * @param kodUtvaruSpachaniNeboZjisteni the police department code
	 * @param nazevUtvaruSpachaniNeboZjisteni the police department name
	 */
	public CrimeSummaryWithUtvarBase(String kodUtvaruSpachaniNeboZjisteni, String nazevUtvaruSpachaniNeboZjisteni)
	{
		mKodUtvaruSpachaniNeboZjisteni = kodUtvaruSpachaniNeboZjisteni;
		mNazevUtvaruSpachaniNeboZjisteni = nazevUtvaruSpachaniNeboZjisteni;
	}

	/**
	 * Returns the police department code associated with the contained crime statistics.
	 * 
	 * @return the police department code associated with the contained crime statistics
	 */
	@Override
	public String getKodUtvaru()
	{
		return mKodUtvaruSpachaniNeboZjisteni;
	}
	
	/**
	 * Returns the police department name associated with the contained crime statistics.
	 * 
	 * @return the police department name associated with the contained crime statistics
	 */
	@Override
	public String getNazevUtvaru()
	{
		return mNazevUtvaruSpachaniNeboZjisteni;
	}
}
