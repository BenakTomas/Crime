package benak.tomas.crimemonitor.client.service;

import benak.tomas.crimemonitor.client.dataloading.handler.CrimeServiceQueryContext;
import benak.tomas.crimemonitor.library.query.config.QueryConfigBase;
import benak.tomas.crimemonitor.library.service.CrimeMonitorServiceProvider;

public interface CrimeServiceQueryContextProvider
{
	CrimeServiceQueryContext getCrimeServiceQueryContext();
}