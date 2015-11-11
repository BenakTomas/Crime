package benak.tomas.crimemonitor.library.binding.binder;

import benak.tomas.crimemonitor.library.binding.BindingParameters;
import benak.tomas.crimemonitor.library.exception.BindingException;

public abstract class TypedParameterBinder<TBindingObject> extends
		ParameterBinder
{
	protected TBindingObject mBindingObject;

	public <TParams extends BindingParameters> void bindToObject(
			TParams params, TBindingObject bindingObject) throws BindingException
	{
		mBindingObject = bindingObject;
		params.bind(this);
	}
}
