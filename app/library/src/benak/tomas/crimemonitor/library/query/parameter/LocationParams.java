package benak.tomas.crimemonitor.library.query.parameter;

import java.util.Locale;

import benak.tomas.crimemonitor.library.binding.binder.ParameterBinder;
import benak.tomas.crimemonitor.library.exception.BindingException;

public abstract class LocationParams extends QueryParameters
{
	private final String mLatitudeParameterName;
	private final String mLongitudeParameterName;
	
	public LocationParams(final String latitudeParameterName, final String longitudeParameterName)
	{
		if(latitudeParameterName == null)
			throw new IllegalArgumentException("'latitudeParameterName' cannot be null");
		if(longitudeParameterName == null)
			throw new IllegalArgumentException("'longitudeParameterName' cannot be null");
		
		mLatitudeParameterName = latitudeParameterName;
		mLongitudeParameterName = longitudeParameterName;
	}
	
	@Override
	public void bind(ParameterBinder binder) throws BindingException
	{
		binder.bind(mLatitudeParameterName, this.getLatitude());
		binder.bind(mLongitudeParameterName, this.getLongitude());
	}

	protected abstract double getLongitude();
	protected abstract double getLatitude();
	
	@Override
	public String toString()
	{
		return String.format(Locale.US, "location(latitude: %f, longitude: %f",
				this.getLatitude(), this.getLongitude());
	}
}
