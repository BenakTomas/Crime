package benak.tomas.crimemonitor.library;

import java.io.Serializable;


/**
 * A class used to provide an information about the territorial units covered by a given police department.
 * <p>
 * The police department has a territory.
 * A covered territorial unit is a territorial unit, that has a territory, which intersects with
 * the territory of the given department.
 * <p>
 * Current implementation: Currently only one kind of covered territorial units is supported: municipalities. 
 * 
 * <p>
 * The only information provided about the covered territorial unit is its name.
 * 
 * @author Tom
 *
 */
public class CrimeUtvarDetail extends CrimeSummaryWithUtvarAndIntervalBase
		implements Serializable
{
	/**
	 * the helper identifier used to identify this particular version of class for the serialization purposes
	 */
	private static final long	serialVersionUID	= 42L;

	/**
	 * the list of names of the covered municipalities
	 */
	private final String[]		mNazvyPokryvajicichObci;

	/**
	 * Constructs a new {@code CrimeUtvarDetail} for a given department code and name and a crime data
	 * interval, using the provided list of names of the covered municipalities.
	 * 
	 * @param kodUtvaruSpachaniNeboZjisteni the department code
	 * @param nazevUtvaruSpachaniNeboZjisteni the department name
	 * @param startYear the given crime data interval initial year
	 * @param startMonth the given crime data interval initial month
	 * @param endYear the given crime data interval terminal year
	 * @param endMonth the given crime data interval terminal month
	 * @param nazvyPokryvajicichObci the list of names of the covered municipalities 
	 */
	public CrimeUtvarDetail(String kodUtvaruSpachaniNeboZjisteni, String nazevUtvaruSpachaniNeboZjisteni, int startYear,
			int startMonth, int endYear, int endMonth,
			String[] nazvyPokryvajicichObci)
	{
		super(kodUtvaruSpachaniNeboZjisteni, nazevUtvaruSpachaniNeboZjisteni,
				startYear, startMonth, endYear, endMonth);

		mNazvyPokryvajicichObci = nazvyPokryvajicichObci;
	}

	/**
	 * Returns the list of names of the covered municipalities.
	 * 
	 * @return the list of names of the covered municipalities
	 */
	public String[] getNazvyPokryvajicichObci()
	{
		return mNazvyPokryvajicichObci;
	}
}
