package benak.tomas.crimemonitor.library;

import java.io.Serializable;

/**
 * A class used to hold the information about a code and name
 * for a particular police department.
 * <p>
 * In effect, it provides the information, that a given police department has a particular code
 * and vice versa.
 * <p>
 * This class is used as to hold the information of the department code and name request.
 * The department code and name request has a GPS location parameter and this class contains
 * the data for the department that has the given GPS point inside of its territory.
 * @author Tom
 */
public class UtvarKodANazev extends CrimeSummaryWithUtvarBase implements Serializable
{
	/**
	 * the helper identifier used to identify this particular version of class for the serialization purposes
	 */
	private static final long	serialVersionUID	= 42L;
	
	/**
	 * Constructs a new {@code UtvarKodANazev} using a provided police department
	 * code and name.
	 * 
	 * @param kod the department code
	 * @param nazev the department name
	 */
	public UtvarKodANazev(String kod, String nazev)
	{
		super(kod, nazev);
	}
	
	public boolean hasDifferentCodeThan(final UtvarKodANazev other)
	{
		return this.getKodUtvaru().equals(other.getKodUtvaru());
	}
}
