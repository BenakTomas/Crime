package benak.tomas.crimemonitor.client.dataloading.handler;

import java.util.Properties;

import benak.tomas.crimemonitor.client.utils.FieldNames;
import benak.tomas.crimemonitor.library.exception.QueryConfigInternalErrorException;
import benak.tomas.crimemonitor.library.query.config.QueryConfigBase;
import benak.tomas.crimemonitor.library.service.CrimeMonitorServiceProvider;

public class CrimeServiceQueryContext extends QueryConfigBase
{
	protected String mServiceUrl;
	protected String mTargetNamespace;
	
	public CrimeServiceQueryContext(Properties configuration)
			throws QueryConfigInternalErrorException
	{
		super(configuration);
	}

	@Override
	protected void loadConfiguration(Properties configuration)
			throws QueryConfigInternalErrorException
	{
		mServiceUrl = configuration.getProperty(FieldNames.SERVICE_URL);
		mTargetNamespace = configuration.getProperty(FieldNames.SERVICE_TARGET_NAMESPACE);
	}
	
	public String getServiceUrl()
	{
		return mServiceUrl;
	}

	public String getTargetNamespace()
	{
		return mTargetNamespace;
	}

}
