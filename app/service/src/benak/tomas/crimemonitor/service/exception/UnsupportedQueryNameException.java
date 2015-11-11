package benak.tomas.crimemonitor.service.exception;

import benak.tomas.crimemonitor.service.query.config.QueryConfig.QueryName;

public class UnsupportedQueryNameException extends Exception
{
	private final QueryName mQueryName;
	
	public UnsupportedQueryNameException(QueryName queryName)
	{
		super("Query name used is not supported");
		
		if(queryName == null)
			throw new IllegalArgumentException("'queryName' cannot be null");
		
		mQueryName = queryName;
	}

	public QueryName getQueryName()
	{
		return mQueryName;
	}

}
