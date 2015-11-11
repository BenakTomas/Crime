package benak.tomas.crimemonitor.service.query.config;

import java.util.Properties;

import benak.tomas.crimemonitor.library.exception.QueryConfigInternalErrorException;
import benak.tomas.crimemonitor.service.exception.CrimeResourceProviderInternalErrorException;
import benak.tomas.crimemonitor.service.exception.InvalidCrimeServiceResourcePathException;
import benak.tomas.crimemonitor.service.handler.CrimeServiceResourceProvider;

public final class SparqlConfig extends QueryConfig
{
	private String mDefaultGraphUri;

	public SparqlConfig(Properties configuration,
			CrimeServiceResourceProvider resourceProvider) throws QueryConfigInternalErrorException
	{
		super(configuration, resourceProvider);
	}

	public String getDefaultGraphUri()
	{
		return mDefaultGraphUri;
	}

	@Override
	protected void loadConfiguration(Properties configuration) throws QueryConfigInternalErrorException
	{
		super.loadConfiguration(configuration);

		try
		{
			this.addQueryIdToTextMapping(QueryName.CRIME_DETAIL,
					"crimeDetail_Path", configuration);
			this.addQueryIdToTextMapping(QueryName.CRIME_DETAIL,
					"crimeDetail_Path", configuration);
			this.addQueryIdToTextMapping(QueryName.CRIME_MAX_INDEX,
					"crimeMaxIndex_Path", configuration);
			this.addQueryIdToTextMapping(
					QueryName.CRIME_COUNT_HISTOGRAM_DETAIL,
					"crimeDetailHistogram_Path", configuration);
			this.addQueryIdToTextMapping(QueryName.CRIME_TOPLIST,
					"crimeToplist_Path", configuration);
			this.addQueryIdToTextMapping(QueryName.INDEX_A_OBJASNENOST_SUMMARY,
					"indexAObjasnenost_Path", configuration);
			this.addQueryIdToTextMapping(QueryName.TOTAL_MAX_CRIME_INDEX,
					"totalMaxCrimeIndex_Path", configuration);
			this.addQueryIdToTextMapping(
					QueryName.CRIME_COUNT_HISTOGRAM_SUMMARY,
					"crimesMonthHistogramForInterval_Path", configuration);
			this.addQueryIdToTextMapping(QueryName.CRIME_LIST,
					"crimeList_Path", configuration);
			this.addQueryIdToTextMapping(QueryName.POKRYTE_OBCE,
					"pokryteObce_Path", configuration);
			this.addQueryIdToTextMapping(QueryName.NAZEV_UTVARU,
					"nazevUtvaru_Path", configuration);
		}
		catch (InvalidCrimeServiceResourcePathException
				| CrimeResourceProviderInternalErrorException e)
		{
			e.printStackTrace();
			throw new QueryConfigInternalErrorException("Cannot load query text.", e);
		}

		mDefaultGraphUri = configuration.getProperty("defaultGraphUri");
	}
}