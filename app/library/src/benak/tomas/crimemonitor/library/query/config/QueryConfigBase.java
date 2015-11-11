package benak.tomas.crimemonitor.library.query.config;

import java.util.Properties;

import benak.tomas.crimemonitor.library.exception.QueryConfigInternalErrorException;
import benak.tomas.crimemonitor.library.service.CrimeMonitorServiceProvider;

public abstract class QueryConfigBase
{
	public QueryConfigBase(Properties configuration) throws QueryConfigInternalErrorException
	{
		if(configuration == null)
			throw new IllegalArgumentException("'configuration' cannot be null");
		
		this.loadConfiguration(configuration);
	}
	
	protected abstract void loadConfiguration(Properties configuration) throws QueryConfigInternalErrorException;
}
