package benak.tomas.crimemonitor.library;

import java.io.Serializable;

/**
 * A class used to record an information how many times a given crime has been committed.
 * <p>
 * The given crime is identified by its TSK identifier.
 * <br />
 * As an additional description, the crime's name is stored.
 * 
 * @author Tom
 */
public class CrimeSummary implements Serializable
{
	/**
	 * the helper identifier used to identify this particular version of class for the serialization purposes
	 */
	private static final long serialVersionUID = 42L;
	
	/**
	 * the TSK identifier of the crime
	 */
	private final int mTsk;
	/**
	 * the crime name
	 */
	private final String mCrimeName;
	/**
	 * the committed crimes count for the given crime
	 */
	private final int mCrimeNumber;
	
	/**
	 * Constructs a new {@code CrimeSummary} for a particular crime
	 * identified by the provided TSK identifier.
	 * <p>
	 * The crime's name and committed crimes count is stored for the given crime.
	 * 
	 * @param tsk the TSK identifier of the given crime
	 * @param crimeName the name of the given crime
	 * @param crimeNumber the committed crimes count for the given crime
	 */
	public CrimeSummary(int tsk, String crimeName, int crimeNumber)
	{
		this.mTsk = tsk;
		this.mCrimeName = crimeName;
		this.mCrimeNumber = crimeNumber;
	}
	
	/**
	 * Returns the name of the given crime.
	 * 
	 * @return the name of the given crime
	 */
	public String getCrimeName() {
		return mCrimeName;
	}

	/**
	 * Returns the TSK identifier of the given crime.
	 * 
	 * @return the TSK identifier of the given crime
	 */
	public int getTsk() {
		return mTsk;
	}

	/**
	 * Returns the committed crimes count for the given crime.
	 * 
	 * @return the committed crimes count for the given crime
	 */
	public int getCrimeNumber()
	{
		return mCrimeNumber;
	}
}
