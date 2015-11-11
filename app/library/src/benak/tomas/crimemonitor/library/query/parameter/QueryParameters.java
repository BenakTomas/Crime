package benak.tomas.crimemonitor.library.query.parameter;

import benak.tomas.crimemonitor.library.binding.BindingParameters;

public abstract class QueryParameters implements BindingParameters
{
	public static <TParams> TParams getFirstParametersOfType(final QueryParameters[] params, final Class<TParams> instanceType)
	{
		for(int i = 0; i < params.length; ++i)
		{
			if(params[i].getClass().equals(instanceType))
				return instanceType.cast(params[i]);
		}
		
		return null;	
	}
}
