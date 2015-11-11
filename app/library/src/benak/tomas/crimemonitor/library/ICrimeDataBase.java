package benak.tomas.crimemonitor.library;

/**
 * A base interface for providing a department code and name.
 * <p>
 * To an implementing class, this interface adds the ability to
 * associate the crime statistics with a particular police department.
 *  
 * @author Tom
 */
public interface ICrimeDataBase
{
	// location
	public double getLatitude();
	public double getLongitude();
	// previous location
	public Double getLastLatitude();
	public Double getLastLongitude();
	
	// interval
	public int getStartYear();
	public int getStartMonth();
	public int getEndYear();
	public int getEndMonth();
	
	public String getNazevUtvaru();
}
