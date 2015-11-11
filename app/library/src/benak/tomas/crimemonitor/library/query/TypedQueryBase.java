package benak.tomas.crimemonitor.library.query;

import benak.tomas.crimemonitor.library.binding.binder.TypedParameterBinder;
import benak.tomas.crimemonitor.library.exception.BindingException;
import benak.tomas.crimemonitor.library.exception.TypedQueryInternalErrorException;
import benak.tomas.crimemonitor.library.query.config.QueryConfigBase;
import benak.tomas.crimemonitor.library.query.parameter.QueryParameters;
import benak.tomas.crimemonitor.library.query.resultProjector.QueryResultProjector;

public abstract class TypedQueryBase<TConfig extends QueryConfigBase, TResult, TStatement, TStatementResult>
{
	protected String										  mQueryText;
	protected TConfig										 mConfig;

	protected TypedParameterBinder<TStatement>				mParameterBinder;
	protected QueryResultProjector<TStatementResult, TResult> mResultProjector;

	public TypedQueryBase(TConfig config) throws TypedQueryInternalErrorException
	{
		if (config == null)
			throw new NullPointerException("config");

		mConfig = config;

		this.init();
	}

	protected void init() throws TypedQueryInternalErrorException
	{
		mParameterBinder = this.createParameterBinder();
		mResultProjector = this.createResultProjector();
	}

	protected abstract TypedParameterBinder<TStatement> createParameterBinder();

	protected abstract QueryResultProjector<TStatementResult, TResult> createResultProjector();

	protected void bindParametersToQueryString(TStatement statement,
			QueryParameters ... params) throws BindingException
	{
		for (QueryParameters param : params)
		{
			mParameterBinder.bindToObject(param, statement);
		}
	}

	protected abstract TStatement createStatement();
}
