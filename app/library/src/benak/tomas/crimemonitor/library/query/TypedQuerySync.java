package benak.tomas.crimemonitor.library.query;

import benak.tomas.crimemonitor.library.exception.BindingException;
import benak.tomas.crimemonitor.library.exception.TypedQueryInternalErrorException;
import benak.tomas.crimemonitor.library.query.config.QueryConfigBase;
import benak.tomas.crimemonitor.library.query.parameter.QueryParameters;

public abstract class TypedQuerySync<TConfig extends QueryConfigBase, TResult, TStatement, TStatementResult>
	extends TypedQueryBase<TConfig, TResult, TStatement, TStatementResult>
{
	public TypedQuerySync(TConfig config)
			throws TypedQueryInternalErrorException
	{
		super(config);
	}
	
	public TResult execute(QueryParameters ... params) throws TypedQueryInternalErrorException
	{
		TStatement statement = this.createStatement();

		try
		{
			this.bindParametersToQueryString(statement, params);
		}
		catch (BindingException e)
		{
			e.printStackTrace();
			throw new TypedQueryInternalErrorException("An error occurred when trying to bind the given parameters to the query statement", e);
		}

		TStatementResult results = this.executeStatement(statement);

		return mResultProjector.projectResult(results);
	}
	
	protected abstract TStatementResult executeStatement(TStatement statement);
}
