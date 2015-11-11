package benak.tomas.crimemonitor.client.binding.binder;

import java.util.Calendar;

import android.content.Intent;
import benak.tomas.crimemonitor.library.binding.binder.TypedParameterBinder;
import benak.tomas.crimemonitor.library.exception.BindingException;

public class IntentBundleBinder extends TypedParameterBinder<Intent>
{
	@Override
	public void bind(String parameterName, int parameterValue) throws BindingException
	{
		mBindingObject.putExtra(parameterName, parameterValue);
	}

	@Override
	public void bind(String parameterName, float parameterValue) throws BindingException
	{
		mBindingObject.putExtra(parameterName, parameterValue);
	}

	@Override
	public void bind(String parameterName, String parameterValue) throws BindingException
	{
		mBindingObject.putExtra(parameterName, parameterValue);
	}

	@Override
	public void bind(String parameterName, Calendar parameterValue) throws BindingException
	{
		mBindingObject.putExtra(parameterName, parameterValue);
	}

	@Override
	public void bind(String arg0, double arg1) throws BindingException
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void bind(String arg0, boolean arg1) throws BindingException
	{
		// TODO Auto-generated method stub
		
	}

}
