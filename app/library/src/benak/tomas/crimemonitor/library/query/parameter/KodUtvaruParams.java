package benak.tomas.crimemonitor.library.query.parameter;

import benak.tomas.crimemonitor.library.binding.binder.ParameterBinder;
import benak.tomas.crimemonitor.library.exception.BindingException;

public abstract class KodUtvaruParams extends QueryParameters
{
	private final String mKodUtvaruParameterName;
	
	public KodUtvaruParams(final String kodUtvaruParameterName)
	{
		if(kodUtvaruParameterName == null)
			throw new IllegalArgumentException("'kodUtvaruParameterName' cannot be null");
		
		mKodUtvaruParameterName = kodUtvaruParameterName;
	}
	
	@Override
	public void bind(ParameterBinder binder) throws BindingException
	{
		binder.bind(mKodUtvaruParameterName, this.getKodUtvaru());
	}

	protected abstract String getKodUtvaru();
}
