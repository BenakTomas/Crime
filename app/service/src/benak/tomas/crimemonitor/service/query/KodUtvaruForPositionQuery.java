package benak.tomas.crimemonitor.service.query;

import java.sql.CallableStatement;
import java.sql.Types;

import benak.tomas.crimemonitor.library.exception.BindingException;
import benak.tomas.crimemonitor.library.exception.TypedQueryInternalErrorException;
import benak.tomas.crimemonitor.library.query.resultProjector.QueryResultProjector;
import benak.tomas.crimemonitor.service.query.config.DbConfig;
import benak.tomas.crimemonitor.service.query.config.QueryConfig.QueryName;

public class KodUtvaruForPositionQuery extends
		TypedCallableStatementDbQuery<String>
{

	public KodUtvaruForPositionQuery(DbConfig config) throws TypedQueryInternalErrorException
	{
		super(config);
	}

	protected void init() throws TypedQueryInternalErrorException
	{
		super.init();
		
		mParameterNamesToOrdinals.put("longitude", 1);
		mParameterNamesToOrdinals.put("latitude", 2);
		mParameterNamesToOrdinals.put("historyDate", 3);
		mParameterNamesToOrdinals.put("utvar", 4);
	}

	@Override
	protected void registerOutParameters(CallableStatement statement)
			throws BindingException
	{
		this.registerOutParameter("utvar", Types.CHAR, statement);
	}

	@Override
	protected QueryResultProjector<CallableStatement, String> createResultProjector()
	{
		return new QueryResultProjector<CallableStatement, String>()
		{
			@Override
			public String projectResult(CallableStatement sourceResult)
			{
				return KodUtvaruForPositionQuery.this
						.getStringOutParameterValue("utvar", sourceResult);
			}
		};
	}

	@Override
	protected QueryName getQueryId()
	{
		return QueryName.KOD_UTVARU_FOR_POSITION;
	}

}
