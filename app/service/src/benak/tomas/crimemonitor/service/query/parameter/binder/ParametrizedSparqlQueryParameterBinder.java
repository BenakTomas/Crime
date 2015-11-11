package benak.tomas.crimemonitor.service.query.parameter.binder;

import java.util.Calendar;

import benak.tomas.crimemonitor.library.binding.binder.TypedParameterBinder;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.query.ParameterizedSparqlString;

public class ParametrizedSparqlQueryParameterBinder extends
		TypedParameterBinder<ParameterizedSparqlString>
{
	@Override
	public void bind(String parameterName, int parameterValue) {
		// TODO Auto-generated method stub
		mBindingObject.setLiteral(parameterName, parameterValue);
	}

	@Override
	public void bind(String parameterName, float parameterValue) {
		// TODO Auto-generated method stub
		mBindingObject.setLiteral(parameterName, parameterValue);
	}

	@Override
	public void bind(String parameterName, String parameterValue) {
		// TODO Auto-generated method stub
		mBindingObject.setLiteral(parameterName, parameterValue, XSDDatatype.XSDstring);
	}

	@Override
	public void bind(String parameterName, Calendar parameterValue) {
		// TODO Auto-generated method stub
		mBindingObject.setLiteral(parameterName, parameterValue);
	}
}
