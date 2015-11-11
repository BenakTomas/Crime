package benak.tomas.crimemonitor.library.binding.binder;

import java.util.Calendar;

import benak.tomas.crimemonitor.library.exception.BindingException;

public abstract class ParameterBinder
{
    public abstract void bind(String parameterName, int parameterValue) throws BindingException;

    public abstract void bind(String parameterName, float parameterValue) throws BindingException;
    
    public abstract void bind(String parameterName, double parameterValue) throws BindingException;

    public abstract void bind(String parameterName, String parameterValue) throws BindingException;
    
    public abstract void bind(String parameterName, boolean parameterValue) throws BindingException;

    public abstract void bind(String parameterName, Calendar parameterValue) throws BindingException;
}
