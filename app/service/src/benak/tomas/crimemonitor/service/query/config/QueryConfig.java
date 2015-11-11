package benak.tomas.crimemonitor.service.query.config;

import java.nio.charset.Charset;
import java.util.Hashtable;
import java.util.Properties;

import benak.tomas.crimemonitor.library.exception.QueryConfigInternalErrorException;
import benak.tomas.crimemonitor.library.query.config.QueryConfigBase;
import benak.tomas.crimemonitor.service.exception.CrimeResourceProviderInternalErrorException;
import benak.tomas.crimemonitor.service.exception.InvalidCrimeServiceResourcePathException;
import benak.tomas.crimemonitor.service.exception.UnsupportedQueryNameException;
import benak.tomas.crimemonitor.service.handler.CrimeServiceResourceProvider;

public abstract class QueryConfig extends QueryConfigBase
{
	protected CrimeServiceResourceProvider mResourceProvider;
	private Charset mQueriesCharset;
	protected Hashtable<QueryName, String> mQueryIdToTextMapping;
	{
		mQueryIdToTextMapping = new Hashtable<QueryName, String>();
	}
	
	public enum QueryName
	{
		CRIME_DETAIL,
		CRIME_MAX_INDEX,
		CRIME_TOPLIST,
		INDEX_A_OBJASNENOST_SUMMARY,
		INDEX_A_OBJASNENOST_DETAIL,
		TOTAL_MAX_CRIME_INDEX,
		CRIME_COUNT_HISTOGRAM_SUMMARY,
		CRIME_COUNT_HISTOGRAM_DETAIL,
		CRIME_LIST,
		POKRYTE_OBCE,
		NAZEV_UTVARU,
		NAZEV_TRESTNEHO_CINU,
		KOD_UTVARU_FOR_POSITION
	}
	
	
	public QueryConfig(Properties configuration, CrimeServiceResourceProvider serviceProvider) throws QueryConfigInternalErrorException
	{
		super(configuration);
		
		mResourceProvider = (CrimeServiceResourceProvider)serviceProvider;
	}
	
	protected void loadConfiguration(Properties configuration) throws QueryConfigInternalErrorException
	{
		mQueriesCharset = Charset.forName(configuration.getProperty("queriesCharset", "UTF8"));
	}

	public String getQueryText(QueryName queryName) throws UnsupportedQueryNameException, QueryConfigInternalErrorException
	{
		if(!mQueryIdToTextMapping.containsKey(queryName))
			throw new UnsupportedQueryNameException(queryName);

		return mQueryIdToTextMapping.get(queryName);
	}
	
	protected void addQueryIdToTextMapping(final QueryName queryName, final String queryPathConfigKey, final Properties configuration) throws InvalidCrimeServiceResourcePathException, CrimeResourceProviderInternalErrorException
	{
		mQueryIdToTextMapping.put(queryName, mResourceProvider.getResourceAsString(configuration.getProperty(queryPathConfigKey), mQueriesCharset));
	}
}
