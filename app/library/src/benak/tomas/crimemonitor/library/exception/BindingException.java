package benak.tomas.crimemonitor.library.exception;

public class BindingException extends Exception
{
    private final String mParameterName;
    private final Object mParameterValue;
    
    public BindingException(String message, Throwable cause, String parameterName, Object parameterValue)
    {
        super(message, cause);
        
        mParameterName = parameterName;
        mParameterValue = parameterValue;
    }

    public String getParameterName()
    {
        return mParameterName;
    }

	public Object getParameterValue()
	{
		return mParameterValue;
	}
}
