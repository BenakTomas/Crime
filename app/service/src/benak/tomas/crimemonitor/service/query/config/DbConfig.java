package benak.tomas.crimemonitor.service.query.config;

import java.util.Properties;

import benak.tomas.crimemonitor.library.exception.QueryConfigInternalErrorException;
import benak.tomas.crimemonitor.service.handler.CrimeServiceResourceProvider;

public abstract class DbConfig extends QueryConfig
{
	private String	 mJdbcUrl;
	private Properties mConnectionProperties;

	public DbConfig(Properties configuration,
			CrimeServiceResourceProvider resourceProvider) throws QueryConfigInternalErrorException
	{
		super(configuration, resourceProvider);
	}

	public Properties getConnectionProperties()
	{
		return mConnectionProperties;
	}

	public String getJdbcUrl()
	{
		return mJdbcUrl;
	}

	@Override
	protected void loadConfiguration(Properties configuration) throws QueryConfigInternalErrorException
	{
		super.loadConfiguration(configuration);

		mJdbcUrl = this.loadJdbcUrl(configuration);
		mConnectionProperties = this.loadConnectionProperties(configuration);
	}

	protected abstract Properties loadConnectionProperties(
			Properties configuration);

	protected abstract String loadJdbcUrl(Properties configuration);
}
