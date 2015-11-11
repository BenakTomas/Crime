package benak.tomas.crimemonitor.service.query.parameter;

import java.util.Calendar;

import benak.tomas.crimemonitor.library.binding.binder.ParameterBinder;
import benak.tomas.crimemonitor.library.exception.BindingException;
import benak.tomas.crimemonitor.library.query.parameter.QueryParameters;

public class DateIntervalQueryParameters extends QueryParameters
{
	private final Calendar mStartDate;
	private final Calendar mEndDate;
	
	public DateIntervalQueryParameters(Calendar startDate, Calendar endDate)
	{
		mStartDate = startDate;
		mEndDate = endDate;
	}

	@Override
	public void bind(ParameterBinder binder)
			throws BindingException
	{
		binder.bind("parStartDate_ValuePlaceholder", mStartDate);
		binder.bind("parEndDate_ValuePlaceholder", mEndDate);
	}
	
	public Calendar getStartDate()
	{
		return mStartDate;
	}

	public Calendar getEndDate()
	{
		return mEndDate;
	}
}
