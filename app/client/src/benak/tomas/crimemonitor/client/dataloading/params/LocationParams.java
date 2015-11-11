package benak.tomas.crimemonitor.client.dataloading.params;

import android.location.Location;

public class LocationParams extends benak.tomas.crimemonitor.library.query.parameter.LocationParams
{
	private final static String LATITUDE_PARAMETER_NAME = "latitude";
	private final static String LONGITUDE_PARAMETER_NAME = "longitude";

	private final Location mLocation;
	
	public LocationParams(Location location)
	{
		this(location, LATITUDE_PARAMETER_NAME, LONGITUDE_PARAMETER_NAME);
	}

	public LocationParams(final Location location, final String latitudeParameterName, final String longitudeParameterName)
	{
		super(latitudeParameterName, longitudeParameterName);
		
		if(location == null)
			throw new IllegalArgumentException("'location' cannot be null");
		
		mLocation = location;
	}
	
	public Location getLocation()
	{
		return mLocation;
	}

	public static LocationParams getInstance(final Location location)
	{
		return new LocationParams(location);
	}
	
	public static LocationParams getInstance(final Location location,  final String latitudeParameterName, final String longitudeParameterName)
	{
		return new LocationParams(location, latitudeParameterName, longitudeParameterName);
	}

	@Override
	protected double getLongitude()
	{
		return mLocation.getLongitude();
	}

	@Override
	protected double getLatitude()
	{
		return mLocation.getLatitude();
	}
}
