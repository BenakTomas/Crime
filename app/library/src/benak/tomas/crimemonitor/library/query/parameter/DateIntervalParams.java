package benak.tomas.crimemonitor.library.query.parameter;

import benak.tomas.crimemonitor.library.binding.binder.ParameterBinder;
import benak.tomas.crimemonitor.library.exception.BindingException;

public abstract class DateIntervalParams extends QueryParameters
{
	protected final int mStartYear;
	protected final int mStartMonth;
	protected final int mEndYear;
	protected final int mEndMonth;

	public DateIntervalParams(final int startYear, final int startMonth,
			final int endYear, final int endMonth)
	{
		mStartYear = startYear;
		mStartMonth = startMonth;
		mEndYear = endYear;
		mEndMonth = endMonth;
	}

	@Override
	public void bind(ParameterBinder binder) throws BindingException
	{
		this.bindStartDate(binder);
		this.bindEndDate(binder);
	}

	protected abstract void bindEndDate(ParameterBinder binder) throws BindingException;

	protected abstract void bindStartDate(ParameterBinder binder) throws BindingException;

	protected int getStartYear()
	{
		return mStartYear;
	}

	protected int getStartMonth()
	{
		return mStartMonth;
	}

	protected int getEndYear()
	{
		return mEndYear;
	}

	protected int getEndMonth()
	{
		return mEndMonth;
	}
}
