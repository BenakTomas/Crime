package benak.tomas.crimemonitor.client.binding.binder;

import java.util.Calendar;

import android.os.Bundle;
import benak.tomas.crimemonitor.library.binding.binder.TypedParameterBinder;
import benak.tomas.crimemonitor.library.exception.BindingException;

public class BundleExtrasBinder extends TypedParameterBinder<Bundle>
{
	public BundleExtrasBinder(final Bundle bundle)
	{
		if(bundle == null)
			throw new IllegalArgumentException("bundle cannot be null");
		
		mBindingObject = bundle;
	}
	@Override
	public void bind(String parameterName, int parameterValue)
			throws BindingException
	{
		mBindingObject.putInt(parameterName, parameterValue);
	}

	@Override
	public void bind(String parameterName, float parameterValue)
			throws BindingException
	{
		mBindingObject.putFloat(parameterName, parameterValue);
	}

	@Override
	public void bind(String parameterName, String parameterValue)
			throws BindingException
	{
		mBindingObject.putString(parameterName, parameterValue);
	}

	@Override
	public void bind(String parameterName, Calendar parameterValue)
			throws BindingException
	{
		mBindingObject.putSerializable(parameterName, parameterValue);
	}
	
	//@Override
	public void bind(String parameterName, boolean parameterValue)
			throws BindingException
	{
		mBindingObject.putBoolean(parameterName, parameterValue);
	}
	@Override
	public void bind(String arg0, double arg1) throws BindingException
	{
		// TODO Auto-generated method stub
		
	}
}
