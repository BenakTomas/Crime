package benak.tomas.crimemonitor.library.query;

import benak.tomas.crimemonitor.library.exception.TypedQueryInternalErrorException;
import benak.tomas.crimemonitor.library.query.config.QueryConfigBase;

public abstract class TypedQueryAsync<TConfig extends QueryConfigBase, TResult, TStatement, TStatementResult>
		extends TypedQueryBase<TConfig, TResult, TStatement, TStatementResult>
{

	public TypedQueryAsync(TConfig config)
			throws TypedQueryInternalErrorException
	{
		super(config);
	}
}
