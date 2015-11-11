package benak.tomas.crimemonitor.client.dataloading.params;

import benak.tomas.crimemonitor.library.binding.binder.ParameterBinder;
import benak.tomas.crimemonitor.library.exception.BindingException;
import benak.tomas.crimemonitor.library.query.parameter.QueryParameters;

public class TskParams extends QueryParameters
{
	private final int mTsk;
	
	public TskParams(int tsk)
	{
		mTsk = tsk;
	}

	@Override
	public void bind(ParameterBinder binder) throws BindingException
	{
		binder.bind("mTsk", mTsk);
	}

}
