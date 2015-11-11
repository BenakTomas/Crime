package benak.tomas.crimemonitor.client.dataloading.params;

import benak.tomas.crimemonitor.library.binding.binder.ParameterBinder;
import benak.tomas.crimemonitor.library.exception.BindingException;

public class DateIntervalParams extends benak.tomas.crimemonitor.library.query.parameter.DateIntervalParams
{
	private final static String START_YEAR_PARAMETER_NAME = "startYear";
	private final static String START_MONTH_PARAMETER_NAME = "startMonth";
	private final static String END_YEAR_PARAMETER_NAME = "endYear";
	private final static String END_MONTH_PARAMETER_NAME = "endMonth";
	
	public DateIntervalParams(int startYear, int startMonth, int endYear,
			int endMonth)
	{
		super(startYear, startMonth, endYear, endMonth);
	}

	@Override
	protected void bindStartDate(ParameterBinder binder) throws BindingException
	{
		binder.bind(START_YEAR_PARAMETER_NAME, this.getStartYear());
		binder.bind(START_MONTH_PARAMETER_NAME, this.getStartMonth());
	}

	@Override
	protected void bindEndDate(ParameterBinder binder) throws BindingException
	{
		binder.bind(END_YEAR_PARAMETER_NAME, this.getEndYear());
		binder.bind(END_MONTH_PARAMETER_NAME, this.getEndMonth());
	}
	
	public static DateIntervalParams getInstance(int startYear, int startMonth, int endYear,
			int endMonth)
	{
		return new DateIntervalParams(startYear, startMonth, endYear, endMonth);
	}
}
