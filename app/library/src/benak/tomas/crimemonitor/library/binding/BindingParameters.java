package benak.tomas.crimemonitor.library.binding;

import benak.tomas.crimemonitor.library.exception.BindingException;
import benak.tomas.crimemonitor.library.binding.binder.ParameterBinder;

public interface BindingParameters
{
	void bind(ParameterBinder binder) throws BindingException;
}
