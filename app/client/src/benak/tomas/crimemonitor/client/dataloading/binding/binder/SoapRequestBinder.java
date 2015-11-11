package benak.tomas.crimemonitor.client.dataloading.binding.binder;

import java.util.Calendar;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import benak.tomas.crimemonitor.library.binding.binder.TypedParameterBinder;
import benak.tomas.crimemonitor.library.exception.BindingException;

public class SoapRequestBinder extends TypedParameterBinder<SoapObject>
{
	@Override
	public void bind(String parameterName, int parameterValue) throws BindingException
	{
		this.addPropertyToRequest(parameterName, PropertyInfo.INTEGER_CLASS, parameterValue, mBindingObject);
	}

	@Override
	public void bind(String parameterName, float parameterValue) throws BindingException
	{
		this.addPropertyToRequest(parameterName, Float.class, parameterValue, mBindingObject);
	}

	@Override
	public void bind(String parameterName, String parameterValue) throws BindingException
	{
		this.addPropertyToRequest(parameterName, PropertyInfo.STRING_CLASS, parameterValue, mBindingObject);
	}

	@Override
	public void bind(String parameterName, Calendar parameterValue) throws BindingException
	{
		throw new BindingException("Type given type is not supported to be bound.", null, parameterName, parameterValue);
	}

	protected void addPropertyToRequest(String parameterName, Object parameterType,
			Object parameterValue, SoapObject parametrizedObject) {
		
		PropertyInfo propInfo = new PropertyInfo();
		propInfo.setName(parameterName);
		propInfo.setType(parameterType);
	    propInfo.setValue(parameterValue);
	    
	    parametrizedObject.addProperty(propInfo);
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
