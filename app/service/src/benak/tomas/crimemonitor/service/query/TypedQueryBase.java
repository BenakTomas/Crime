package benak.tomas.crimemonitor.service.query;

import benak.tomas.crimemonitor.library.exception.QueryConfigInternalErrorException;
import benak.tomas.crimemonitor.library.exception.TypedQueryInternalErrorException;
import benak.tomas.crimemonitor.library.query.TypedQuerySync;
import benak.tomas.crimemonitor.service.exception.UnsupportedQueryNameException;
import benak.tomas.crimemonitor.service.query.config.QueryConfig;
import benak.tomas.crimemonitor.service.query.config.QueryConfig.QueryName;

public abstract class TypedQueryBase<TConfig extends QueryConfig, TResult, TStatement, TStatementResult> extends
		TypedQuerySync<TConfig, TResult, TStatement, TStatementResult>
{
	public TypedQueryBase(TConfig config)
			throws TypedQueryInternalErrorException
	{
		super(config);
	}

	protected void init() throws TypedQueryInternalErrorException
	{
		try
		{
			mQueryText = mConfig.getQueryText(this.getQueryId());
		}
		catch (UnsupportedQueryNameException
				| QueryConfigInternalErrorException e)
		{
			e.printStackTrace();
			throw new TypedQueryInternalErrorException("Cannot load query text from configuration.", e);
		}
		
		mParameterBinder = this.createParameterBinder();
		mResultProjector = this.createResultProjector();
	}
	
	protected abstract QueryName getQueryId();
}
