package benak.tomas.crimemonitor.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.servlet.ServletContext;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import benak.tomas.crimemonitor.library.CrimeMeasureTrend;
import benak.tomas.crimemonitor.library.CrimeMonthHistogramObservation;
import benak.tomas.crimemonitor.library.CrimeSummary;
import benak.tomas.crimemonitor.library.CrimeSummaryDetailed;
import benak.tomas.crimemonitor.library.CrimeUtvarDetail;
import benak.tomas.crimemonitor.library.CrimesList;
import benak.tomas.crimemonitor.library.CrimesSummary;
import benak.tomas.crimemonitor.library.UtvarKodANazev;
import benak.tomas.crimemonitor.library.binding.binder.ParameterBinder;
import benak.tomas.crimemonitor.library.exception.BindingException;
import benak.tomas.crimemonitor.library.exception.QueryConfigInternalErrorException;
import benak.tomas.crimemonitor.library.exception.TypedQueryInternalErrorException;
import benak.tomas.crimemonitor.library.query.parameter.QueryParameters;
import benak.tomas.crimemonitor.library.query.resultProjector.QueryResultProjector;
import benak.tomas.crimemonitor.library.utils.CalendarUtils;
import benak.tomas.crimemonitor.service.dto.IndexAObjasnenost;
import benak.tomas.crimemonitor.service.exception.CrimeResourceProviderInternalErrorException;
import benak.tomas.crimemonitor.service.exception.CrimeServiceConfigurationErrorException;
import benak.tomas.crimemonitor.service.exception.CrimeServiceInternalErrorException;
import benak.tomas.crimemonitor.service.exception.InvalidCrimeServiceResourcePathException;
import benak.tomas.crimemonitor.service.exception.TypedSparqlQueryException;
import benak.tomas.crimemonitor.service.exception.UnsupportedQueryNameException;
import benak.tomas.crimemonitor.service.handler.CrimeServiceResourceProvider;
import benak.tomas.crimemonitor.service.query.KodUtvaruForPositionQuery;
import benak.tomas.crimemonitor.service.query.TypedSparqlQuery;
import benak.tomas.crimemonitor.service.query.config.DbConfig;
import benak.tomas.crimemonitor.service.query.config.QueryConfig.QueryName;
import benak.tomas.crimemonitor.service.query.config.SparqlConfig;
import benak.tomas.crimemonitor.service.query.parameter.DateIntervalQueryParameters;
import benak.tomas.crimemonitor.service.utils.TextUtils;

import com.google.gson.GsonBuilder;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.shared.PrefixMapping;

// TODO
// 1) hodnoty do konfiguracniho souboru (defaultni hodnoty nebo spadnout?)
// 3) refaktorizace: kod pro sumar a detail zlocinu je redundantni a jsou tam celky,
//	  ktere by mohly byt sdileny.
// 4) loading PostgreSQL driveru: V konstruktoru?
// 6) pouzite prefixy v SPARQL dotazech: zavest pojmenovane konstanty?
// 7) osetrit vyjimecne situace: nebezi PostgreSQL nebo SPARQL endpoint

/**
 * A class used to handle the service requests.
 * 
 * @author Tom
 * 
 *         notes: RUIAN RDF store Crime RDF store global prefix mappings
 * 
 *         <p>
 *         Implementation note: every public API method returns a JSON
 *         serialization of one of the crime statistics data holder types.
 * 
 *         <p>
 *         Crime statistics data holder types are:
 *         <ul>
 *         <li>{@linkplain CrimesSummary}
 *         <li>{@linkplain CrimeSummaryDetailed}
 *         <li>{@linkplain CrimesList}
 *         <li>{@linkplain CrimeUtvarDetail}
 *         <li>{@linkplain UtvarKodANazev}
 *         </ul>
 * 
 *         <h1>Configuration</h1>
 *         Loading configuration, exceptions etc.
 * 
 *         <h1>Error reporting</h1> Internal errors etc.
 * 
 *         <p>
 *         Crime count monthly histogram: rename?
 * 
 * @see CrimesSummary
 * @see CrimeSummaryDetailed
 * @see CrimesList
 * @see CrimeUtvarDetail
 * @see UtvarKodANazev
 * 
 */
@WebService(endpointInterface = "benak.tomas.crimemonitor.service.CrimeServiceJaxWS", portName = "CrimeServiceJaxWSPort", serviceName = "CrimeServiceJaxWSService", targetNamespace = "http://service.crimemonitor.tomas.benak")
public class CrimeServiceImpl implements CrimeService,
		CrimeServiceResourceProvider
{
	@Resource
	private WebServiceContext   mWebServiceContext;
	/**
	 * the path to the web service config file
	 */
	private final String		CONFIG_FILE_PATH	= "/WEB-INF/config/config.properties";
	private Properties		  mConfig;
	private SparqlConfig		mSparqlConfig;
	private ServletContext	  mServletContext;
	private DbConfig			mCrimeSpatialDbConfig;
	/**
	 * the value used as a "close surrounding" of zero, used in the crime trend
	 * calculation algorithm
	 */
	private static final double CRIME_TREND_EPSILON = 0.001;

	/**
	 * Constructs a new instance of {@code CrimeService}.
	 * <p>
	 * The PostgreSQL JDBC driver is loaded at this point.
	 * 
	 * @throws ClassNotFoundException
	 *             if the JDBC driver class hasn't been found
	 */
	public CrimeServiceImpl()
	{

	}

	@PostConstruct
	private void onPostConstruct() throws ClassNotFoundException
	{
		Class.forName("org.postgresql.Driver");
	}

	@PreDestroy
	private void onPreDestroy()
	{

	}

	/**
	 * Returns a string representation of the crime summary for a given
	 * department and crime data interval.
	 * <p>
	 * The crime summary data holder type is {@link CrimesSummary}.
	 * <p>
	 * If there are no crimes for the given department and crime data interval,
	 * null is returned to be considered an an empty result.
	 * 
	 * @param kodUtvaru
	 *            the department code
	 * @param startYear
	 *            the crime data interval initial year
	 * @param startMonth
	 *            the crime data interval initial month
	 * @param endYear
	 *            the crime data interval terminal year
	 * @param endMonth
	 *            the crime data interval terminal month
	 * 
	 * @return a string representation of the crime summary statistics for a
	 *         given department and crime data interval
	 * 
	 * @throws CrimeServiceConfigurationErrorException
	 *             if the configuration could not be loaded
	 * @throws TypedQueryInternalErrorException
	 * @throws CrimeServiceInternalErrorException
	 *             if an internal error occurred
	 */
	// @WebMethod
	public String getCrimeSummary(final String kodUtvaru, final int startYear,
			final int startMonth, final int endYear, final int endMonth)
			throws CrimeServiceConfigurationErrorException,
			CrimeServiceInternalErrorException
	{
		// initialize the context variables
		this.initializeServletContext();
		// load the configuration
		this.loadConfiguration();
		this.loadSparqlConfig();

		// prepare the query parameters
		QueryParameters historyDateParams = this
				.getCrimeHistoryDateParams(Calendar.getInstance());
		QueryParameters kodUtvaruParams = this
				.getCrimeKodUtvaruParams(kodUtvaru);
		DateIntervalQueryParameters dateIntervalParams = this
				.getCrimeDateIntervalQueryParams(startYear, startMonth,
						endYear, endMonth);

		// get the requested data
		String nazevUtvaru;
		try
		{
			nazevUtvaru = this.getNazevUtvaru(historyDateParams,
					kodUtvaruParams);
		}
		catch (TypedQueryInternalErrorException e)
		{
			e.printStackTrace();
			throw new CrimeServiceInternalErrorException(
					"An error occurred when trying to get the department name.",
					e);
		}

		List<CrimeSummary> crimeToplist;
		try
		{
			crimeToplist = this.getCrimeList(historyDateParams,
					kodUtvaruParams, dateIntervalParams);
		}
		catch (TypedQueryInternalErrorException e)
		{
			e.printStackTrace();
			throw new CrimeServiceInternalErrorException(
					"An error occurred when trying to get the crimes toplist.",
					e);
		}

		IndexAObjasnenost indexAObjasnenost;
		try
		{
			indexAObjasnenost = this.getIndexAObjasnenost(QueryName.INDEX_A_OBJASNENOST_SUMMARY, historyDateParams,
					dateIntervalParams);
		}
		catch (TypedQueryInternalErrorException e)
		{
			e.printStackTrace();
			throw new CrimeServiceInternalErrorException(
					"An error occurred when trying to get the crime index and crime solved ratio.",
					e);
		}

		CrimeMeasureTrend trend;
		try
		{
			CrimeMonthHistogramObservation[] histogram = this.getCrimeTimeSeriesForUtvar(QueryName.CRIME_COUNT_HISTOGRAM_SUMMARY, dateIntervalParams, historyDateParams, kodUtvaruParams);

			// If no data is loaded as a time series for the given department and
			// crime data interval,
			// it is considered to be an error at this point.
			if (histogram == null)
				throw new CrimeServiceInternalErrorException(
						"There is no time series available for the crime trend calculation.");
			
			trend = this.calculateCrimeTrend(histogram);
		}
		catch (TypedQueryInternalErrorException e)
		{
			e.printStackTrace();
			throw new CrimeServiceInternalErrorException(
					"An error occurred when trying to calculate the crime trend.",
					e);
		}

		// construct the return object
		CrimesSummary summary = new CrimesSummary(kodUtvaru, nazevUtvaru,
				startYear, startMonth, endYear, endMonth,
				indexAObjasnenost.getObjasnenost(),
				indexAObjasnenost.getIndex(), trend,
				crimeToplist.toArray(new CrimeSummary[0]));

		// return the return object serialized to a JSON string
		return new GsonBuilder().serializeSpecialFloatingPointValues().create()
				.toJson(summary, CrimesSummary.class);
	}

	private DateIntervalQueryParameters getCrimeDateIntervalQueryParams(int startYear,
			int startMonth, int endYear, int endMonth)
	{
		return this.getCrimeDateIntervalQueryParams(
				CalendarUtils.getCalendar(startYear, startMonth),
				CalendarUtils.getCalendar(endYear, endMonth));
	}

	private DateIntervalQueryParameters getCrimeDateIntervalQueryParams(
			final Calendar startDate, final Calendar endDate)
	{
		return new DateIntervalQueryParameters(startDate, endDate);
	}

	private QueryParameters getCrimeKodUtvaruParams(final String kodUtvaru)
	{
		return new QueryParameters()
		{
			@Override
			public void bind(ParameterBinder binder)
					throws BindingException
			{
				binder.bind("parIdUtvaru_ValuePlaceholder", kodUtvaru);
			}
		};
	}

	private QueryParameters getCrimeHistoryDateParams(Calendar historyDate)
	{
		return this.getHistoryDateParams("parHistoryDate_ValuePlaceholder",
				historyDate);
	}

	private QueryParameters getHistoryDateParams(
			final String historyDateParameterName, final Calendar historyDate)
	{
		return new QueryParameters()
		{
			@Override
			public void bind(ParameterBinder binder)
					throws BindingException
			{
				binder.bind(historyDateParameterName, historyDate);
			}
		};
	}

	private void loadCrimeSpatialDbConfig() throws CrimeServiceConfigurationErrorException
	{
		try
		{
			mCrimeSpatialDbConfig = new DbConfig(mConfig, this)
			{
				@Override
				protected void loadConfiguration(Properties configuration) throws QueryConfigInternalErrorException
				{
					super.loadConfiguration(configuration);
					mQueryIdToTextMapping.put(QueryName.KOD_UTVARU_FOR_POSITION,
							configuration.getProperty("kodUtvaruForPosition_Path"));
				}

				@Override
				protected String loadJdbcUrl(Properties configuration)
				{
					return configuration.getProperty("crimeSpatialDb_jdbcUrl");
				}

				@Override
				protected Properties loadConnectionProperties(
						Properties configuration)
				{
					Properties connectionProperties = new Properties();

					connectionProperties.setProperty("user",
							configuration.getProperty("crimeSpatialDb_login"));
					connectionProperties.setProperty("password",
							configuration.getProperty("crimeSpatialDb_password"));
					connectionProperties.setProperty("charSet",
							configuration.getProperty("crimeSpatialDb_password"));

					return connectionProperties;
				}
			};
		}
		catch (QueryConfigInternalErrorException e)
		{
			e.printStackTrace();
			throw new CrimeServiceConfigurationErrorException("Could not load query configuration for the Crime Spatial Database.", e);
		}
	}

	
	
	private IndexAObjasnenost getIndexAObjasnenost(final QueryName queryName, QueryParameters ... params) throws TypedQueryInternalErrorException
	{
		return new TypedSparqlQuery<IndexAObjasnenost>(mSparqlConfig)
		{
			@Override
			protected QueryResultProjector<ResultSet, IndexAObjasnenost> createResultProjector()
			{
				return new QueryResultProjector<ResultSet, IndexAObjasnenost>()
				{
					@Override
					public IndexAObjasnenost projectResult(
							ResultSet sourceResult)
					{
						if (sourceResult.hasNext())
						{
							QuerySolution solution = sourceResult.next();
							return new IndexAObjasnenost(solution.getLiteral(
									"index").getFloat(), solution.getLiteral(
									"objasnenost").getFloat());
						} else
							return null;
					}
				};
			}

			@Override
			protected QueryName getQueryId()
			{
				return queryName;
			}
		}.execute(params);
	}
	
	private List<CrimeSummary> getCrimeList(QueryParameters historyDateParams,
			QueryParameters kodUtvaruParams, QueryParameters dateIntervalParams)
			throws TypedQueryInternalErrorException
	{
		return new TypedSparqlQuery<List<CrimeSummary>>(mSparqlConfig)
		{
			@Override
			protected QueryResultProjector<ResultSet, List<CrimeSummary>> createResultProjector()
			{
				return new QueryResultProjector<ResultSet, List<CrimeSummary>>()
				{

					@Override
					public List<CrimeSummary> projectResult(
							ResultSet sourceResult)
					{
						// priprav si seznam trestnych cinu
						ArrayList<CrimeSummary> crimeSummaries = new ArrayList<CrimeSummary>();

						// priprav si konstanty pro projektovane vystupni
						// promenne
						final String crimeNameLiteralName = "crimeName", tskNotationLiteralName = "tskNotation", crimeNumberLiteralNumber = "crimeNumber";

						// projdi vysledek a nacti polozky zebricku
						while (sourceResult.hasNext())
						{
							QuerySolution qs = sourceResult.next();

							final int tsk = qs.getLiteral(
									tskNotationLiteralName).getInt();
							final int crimeNumber = qs.getLiteral(
									crimeNumberLiteralNumber).getInt();
							final String crimeName = qs.getLiteral(
									crimeNameLiteralName).getString();

							CrimeSummary crimeSummary = new CrimeSummary(tsk,
									crimeName, crimeNumber);

							crimeSummaries.add(crimeSummary);
						}

						return crimeSummaries;
					}
				};
			}

			@Override
			protected QueryName getQueryId()
			{
				return QueryName.CRIME_TOPLIST;
			}
		}.execute(historyDateParams, kodUtvaruParams, dateIntervalParams);
	}

	private void loadSparqlConfig() throws CrimeServiceConfigurationErrorException
	{
		try
		{
			mSparqlConfig = new SparqlConfig(mConfig, this);
		}
		catch (QueryConfigInternalErrorException e)
		{
			e.printStackTrace();
			throw new CrimeServiceConfigurationErrorException("Could not load query configuration for SPARQL queries.", e);
		}
	}

	private void initializeServletContext()
	{
		mServletContext = (ServletContext) mWebServiceContext
				.getMessageContext().get(MessageContext.SERVLET_CONTEXT);
	}

	/**
	 * Returns a crimes count time series for a given department and crime data
	 * interval.
	 * <p>
	 * A crime count time series observation is created using a month of the
	 * given crime data interval as the time point and using the respective
	 * total crime count for that month as a time series observation value.
	 * 
	 * @param kodUtvaru
	 *            the department code
	 * @param startYear
	 *            the crime data interval initial year
	 * @param startMonth
	 *            the crime data interval initial month
	 * @param endYear
	 *            the crime data interval terminal year
	 * @param endMonth
	 *            the crime data interval terminal month
	 * @param context
	 *            the injected servlet context helper object
	 * 
	 * @return a crimes count time series for a given department and crime data
	 *         interval; null is meant to indicate an empty result
	 * @throws TypedQueryInternalErrorException
	 */
	private CrimeMonthHistogramObservation[] getCrimeTimeSeriesForUtvar(final QueryName queryName, final DateIntervalQueryParameters dateIntervalParams, QueryParameters ... params)
			throws TypedQueryInternalErrorException
	{
		QueryParameters[] allParams = new QueryParameters[1 + params.length];
		allParams[0] = dateIntervalParams;
		System.arraycopy(params, 0, allParams, 1, params.length);
		
		return new TypedSparqlQuery<CrimeMonthHistogramObservation[]>(mSparqlConfig)
		{
			@Override
			protected QueryResultProjector<ResultSet, CrimeMonthHistogramObservation[]> createResultProjector()
			{
				return new QueryResultProjector<ResultSet, CrimeMonthHistogramObservation[]>()
				{

					@Override
					public CrimeMonthHistogramObservation[] projectResult(ResultSet sourceResult)
					{
						List<CrimeMonthHistogramObservation> monthObservationsList = new ArrayList<CrimeMonthHistogramObservation>();

						// @formatter:off
						final String datumSpachaniYear_VariableName = "datumSpachaniYear", datumSpachaniMonth_VariableName = "datumSpachaniMonth", crimeNumber_VariableName = "crimeNumber";
						// @formatter:on

						// Prepare to walk through the months of the given crime data interval.
						// Start with the month preceding the first month of the crime data
						// interval.
						Calendar currentCal = Calendar.getInstance();
						CalendarUtils.setYearAndMonthForJavaCalendar(
								dateIntervalParams.getStartDate().get(Calendar.YEAR), dateIntervalParams.getStartDate()
										.get(Calendar.MONTH), currentCal);
						currentCal.add(Calendar.MONTH, -1);

						// Set the upper bound for the crime data interval.
						// For technical reasons, this is set to be the month succeeding the
						// last month of the crime data interval.
						final Calendar endCal = Calendar.getInstance();
						CalendarUtils.setYearAndMonthForJavaCalendar(
								dateIntervalParams.getEndDate().get(Calendar.YEAR), dateIntervalParams.getEndDate()
										.get(Calendar.MONTH), endCal);
						endCal.add(Calendar.MONTH, 1);

						// Iterate through all of the months of the crime data interval.
						while (sourceResult.hasNext())
						{
							// Read the current solution.
							QuerySolution qs = sourceResult.next();

							// Read the year, month and the number of committed crimes.
							int year = CalendarUtils.getYearFromSparql(qs.getLiteral(
									datumSpachaniYear_VariableName).getInt());
							int month = CalendarUtils.getMonthFromSparql(qs.getLiteral(
									datumSpachaniMonth_VariableName).getInt());
							int crimeCount = qs.getLiteral(crimeNumber_VariableName).getInt();

							// Fill in the empty observations in between the most recently
							// processed
							// month and the current month.
							this.addEmptyObservations(currentCal, year, month,
									monthObservationsList);

							// Add the histogram observation for the current month.
							monthObservationsList.add(new CrimeMonthHistogramObservation(year,
									month, crimeCount));

							// Increment the month counter.
							currentCal.add(Calendar.MONTH, 1);
						}

						// Fill in the empty observations between the last processed month
						// and the terminating month of the crime data interval.
						this.addEmptyObservations(currentCal, endCal.get(Calendar.YEAR),
								endCal.get(Calendar.MONTH), monthObservationsList);

						// Return the results.
						return monthObservationsList
								.toArray(new CrimeMonthHistogramObservation[monthObservationsList
										.size()]);
					}
					
					private void addEmptyObservations(Calendar currentCal, int year, int month,
							List<CrimeMonthHistogramObservation> monthObservationsList)
					{
						// Gets the number of months between the month given by the currentCal
						// and the month given by the year and month.
						// If this number is n, for the first (n - 1) months there is no
						// histogram data.
						// Those observations get the observed value of 0.
						final int monthsStep = CalendarUtils.getMonthsBetweenDates(currentCal, year,
								month);
						final int emptyMonthsCount = monthsStep - 1;

						// For a month for which I have no histogram observations data,
						// set the observed value to be 0, which means there have been no crimes
						// observed
						// for that month.
						for (int i = 0; i < emptyMonthsCount; ++i)
						{
							currentCal.add(Calendar.MONTH, 1);
							monthObservationsList.add(new CrimeMonthHistogramObservation(
									CalendarUtils.getYearFromJavaCalendar(currentCal),
									CalendarUtils.getMonthFromJavaCalendar(currentCal), 0));
						}
					}
				};
			}

			@Override
			protected QueryName getQueryId()
			{
				return queryName;
			}
		}.execute(allParams);
	}

	/**
	 * Returns a string representation of the information about the department
	 * code and name for a given GPS location.
	 * <p>
	 * The department code and name data holder type is {@link UtvarKodANazev}.
	 * 
	 * @param longitude
	 *            the GPS longitude
	 * @param latitude
	 *            the GPS latitude
	 * @param context
	 *            the injected servlet context helper object
	 * 
	 * @return a string representation of the information about the department
	 *         code and name for a given GPS location.
	 * 
	 * @throws CrimeServiceInternalErrorException
	 *             if an internal error occurred
	 * @throws CrimeServiceConfigurationErrorException
	 *             if the configuration could not be loaded
	 * @throws BindingException
	 * @throws QueryConfigConfigurationException
	 * @throws UnsupportedQueryNameException
	 * @throws TypedSparqlQueryException
	 */
	// @WebMethod
	public String getKodANazevUtvaruForPosition(final float longitude,
			final float latitude)
			throws CrimeServiceConfigurationErrorException,
			CrimeServiceInternalErrorException
	{
		this.initializeServletContext();

		this.loadConfiguration();
		this.loadSparqlConfig();
		this.loadCrimeSpatialDbConfig();

		QueryParameters historyParams = this.getHistoryDateParams(
				"historyDate", Calendar.getInstance());
		QueryParameters gpsLocationParams = new QueryParameters()
		{
			@Override
			public void bind(ParameterBinder binder)
					throws BindingException
			{
				binder.bind("longitude", longitude);
				binder.bind("latitude", latitude);
			}
		};

		// Get the department code for the given GPS position.
		String kodUtvaru;
		try
		{
			kodUtvaru = this.getKodUtvaruForPosition(historyParams,
					gpsLocationParams);
		}
		catch (TypedQueryInternalErrorException e)
		{
			e.printStackTrace();
			throw new CrimeServiceInternalErrorException(
					"An error occurred when trying to get the department code for the given location.",
					e);
		}

		QueryParameters kodUtvaruParams = this
				.getCrimeKodUtvaruParams(kodUtvaru);
		// Get the department name for the given code.
		String nazevUtvaru;
		try
		{
			nazevUtvaru = this.getNazevUtvaru(historyParams, kodUtvaruParams);
		}
		catch (TypedQueryInternalErrorException e)
		{
			e.printStackTrace();
			throw new CrimeServiceInternalErrorException(
					"An error occurred when trying to get the department name.",
					e);
		}

		// Serialize the result data holder using GSON, serializing
		// special float values (NaN etc.).
		return new GsonBuilder()
				.serializeSpecialFloatingPointValues()
				.create()
				.toJson(new UtvarKodANazev(kodUtvaru, nazevUtvaru),
						UtvarKodANazev.class);
	}

	/**
	 * Tries to load the configuration from the config file.
	 * 
	 * @param context
	 *            the injected servlet context helper object
	 * 
	 * @throws CrimeServiceConfigurationErrorException
	 *             if there is no config file or the config file could not be
	 *             read
	 */
	private void loadConfiguration()
			throws CrimeServiceConfigurationErrorException
	{
		// Open an input stream for the config file.
		InputStream configIs = mServletContext
				.getResourceAsStream(CONFIG_FILE_PATH);

		// If the resulting stream is null, there is no config file present in
		// the service WAR.
		if (configIs == null)
			throw new CrimeServiceConfigurationErrorException();

		// Prepare the properties reader.
		mConfig = new Properties();

		try
		{
			// Load the properties reader with the config file input stream.
			mConfig.load(configIs);
		}
		catch (IOException e)
		{
			// If an IO exception occurs, log it and throw an encapsulating
			// exception.
			final String message = "An error occured when trying to load the properties file.";
			mServletContext
					.log("An error occured when trying to load the properties file.",
							e);
			throw new CrimeServiceConfigurationErrorException(message, e);
		}
		finally
		{
			// Try to perform a resources cleanup. Log any exceptions.
			try
			{
				configIs.close();
			}
			catch (IOException e)
			{
				mServletContext.log(
						"Could not close the config file input stream", e);
			}
		}
	}

	/**
	 * Returns a department code for a given GPS position.
	 * 
	 * @param longitude
	 *            a longitude value of a given GPS position
	 * @param latitude
	 *            a latitude value of a given GPS position
	 * @param context
	 *            the injected servlet context helper object
	 * 
	 * @return a department code for a given GPS position
	 * @throws TypedQueryInternalErrorException
	 */
	private String getKodUtvaruForPosition(QueryParameters historyDateParams,
			QueryParameters locationParams)
			throws TypedQueryInternalErrorException
	{
		return new KodUtvaruForPositionQuery(mCrimeSpatialDbConfig).execute(
				historyDateParams, locationParams);
	}

	/**
	 * Returns a string representation of the crime detail for a given kind of
	 * crime, police department and crime data interval.
	 * <p>
	 * The crime detail data holder type is {@link CrimeSummaryDetailed}.
	 * <p>
	 * The kind of crime is identified by a TSK identifier.
	 * <p>
	 * If there have been no crimes committed of a given kind for the given
	 * department and crime data interval, null is returned to be considered an
	 * an empty result.
	 * 
	 * @param tsk
	 *            the TSK identifier of a kind of crime
	 * @param kodUtvaru
	 *            the department code
	 * @param startYear
	 *            the crime data interval initial year
	 * @param startMonth
	 *            the crime data interval initial month
	 * @param endYear
	 *            the crime data interval terminal year
	 * @param endMonth
	 *            the crime data interval terminal month
	 * @param context
	 *            the injected servlet context helper object
	 * 
	 * @return a string representation of the crime detail for a given crime
	 *         kind, police department and crime data interval
	 * 
	 * @throws CrimeServiceConfigurationErrorException
	 *             if the configuration could not be loaded
	 * @throws CrimeServiceInternalErrorException
	 *             if an internal error occurred
	 */
	// @WebMethod
	public String getCrimeDetail(final int tsk, final String kodUtvaru, final int startYear,
			final int startMonth, final int endYear, final int endMonth) throws CrimeServiceConfigurationErrorException, CrimeServiceInternalErrorException
	{
		// initialize the context variables
		this.initializeServletContext();
		// load the needed configuration
		this.loadConfiguration();
		this.loadSparqlConfig();

		// prepare the parameters
		QueryParameters historyDateParams = this
				.getCrimeHistoryDateParams(Calendar.getInstance());
		QueryParameters kodUtvaruParams = this
				.getCrimeKodUtvaruParams(kodUtvaru);
		DateIntervalQueryParameters dateIntervalParams = this
				.getCrimeDateIntervalQueryParams(startYear, startMonth,
						endYear, endMonth);
		QueryParameters tskParams = this.getCrimeTskParams(tsk);

		// nacti nazev utvaru
		String nazevUtvaru;
		try
		{
			nazevUtvaru = this.getNazevUtvaru(historyDateParams,
					kodUtvaruParams);
		}
		catch (TypedQueryInternalErrorException e)
		{
			e.printStackTrace();
			throw new CrimeServiceInternalErrorException(
					"An error occurred when trying to get the department name.",
					e);
		}

		// nacti nazev trestneho cinu
		final String nazevTrestnehoCinuDleTsk = this.getNazevTrestnehoCinu(historyDateParams, dateIntervalParams, tskParams);
		
		// TODO include tsk
		IndexAObjasnenost indexAObjasnenost;
		try
		{
			indexAObjasnenost = this.getIndexAObjasnenost(QueryName.INDEX_A_OBJASNENOST_DETAIL, historyDateParams, dateIntervalParams, tskParams);
		}
		catch (TypedQueryInternalErrorException e)
		{
			// rintStackTrace();
			throw new CrimeServiceInternalErrorException(
					"An error occurred when trying to get the crime name.",
					e);
		}
		
		// nacti histogram
		CrimeMonthHistogramObservation[] histogram;
		try
		{
			histogram = this.getCrimeTimeSeriesForUtvar(QueryName.CRIME_COUNT_HISTOGRAM_SUMMARY, dateIntervalParams, historyDateParams, kodUtvaruParams);
		}
		catch (TypedQueryInternalErrorException e)
		{
			e.printStackTrace();
			throw new CrimeServiceInternalErrorException(
					"An error occurred when trying to get the histogram of crime counts by month.",
					e);
		}

		// If no data is loaded as a time series for the given department and
		// crime data interval,
		// it is considered to be an error at this point.
		if (histogram == null)
			throw new CrimeServiceInternalErrorException(
					"There is no time series available for the crime trend calculation.");
		
		CrimeMeasureTrend crimesTrend = this.calculateCrimeTrend(histogram);
								
		
		final CrimeSummaryDetailed crimeDetail = new CrimeSummaryDetailed(tsk,
				nazevTrestnehoCinuDleTsk, startYear, startMonth, endYear, endMonth,
				indexAObjasnenost.getIndex(), indexAObjasnenost.getObjasnenost(), crimesTrend,
				kodUtvaru, nazevUtvaru, histogram);

		return new GsonBuilder().serializeSpecialFloatingPointValues().create()
				.toJson(crimeDetail, CrimeSummaryDetailed.class);
	}

	private String getNazevTrestnehoCinu(QueryParameters historyDateParams,
			QueryParameters dateIntervalParams, QueryParameters tskParams)
	{
		// TODO Auto-generated method stub
		return "TODO";
	}

	private QueryParameters getCrimeTskParams(final int tsk)
	{
		return new QueryParameters()
		{
			@Override
			public void bind(ParameterBinder binder)
					throws BindingException
			{
				binder.bind("parTskId_ValuePlaceholder", tsk);
			}
		};
	}

	/**
	 * Returns a series of points in the 2-dimensional space, ordered by their
	 * x-coordinate, extracting it from the provided collection of histogram
	 * observations.
	 * 
	 * @param monthHistogram
	 *            the crime count monthly histogram to extract the point series
	 *            from
	 * 
	 * @return a series of points in the 2-dimensional space, ordered by their
	 *         x-coordinate
	 */
	private double[][] getCrimeObservationsFromHistogram(
			CrimeMonthHistogramObservation[] monthHistogram)
	{
		final int histogramObservationsCount = monthHistogram.length;

		double[][] crimeObservations = new double[histogramObservationsCount][];

		// Iterate through histogram observations.
		for (int i = 0; i < histogramObservationsCount; ++i)
		{
			CrimeMonthHistogramObservation histogramObservation = monthHistogram[i];

			// From each observation extract the timestamp from the year and
			// month of the observation,
			// as well as the observation value.
			// The resulting pair of doubles represents a point in a
			// two-dimensional plane of reals.
			// The timestamp is the x-coordinate and the observation value is
			// the y-coordinate.
			crimeObservations[i] = new double[] {
					CalendarUtils.getTimeInMillisForCalendar(
							histogramObservation.getYear(),
							histogramObservation.getMonth()),
					histogramObservation.getCrimeCount() };
		}

		return crimeObservations;
	}

	private CrimeMeasureTrend calculateCrimeTrend(CrimeMonthHistogramObservation[] histogram)
	{
		double[][] crimeTimeSeriesForDepartment = this.getCrimeObservationsFromHistogram(histogram);

		// Use simple linear regression to draw a line approximating the point
		// series.
		// This line's slope is then used to determine the crime trend.
		SimpleRegression regression = new SimpleRegression(true);
		regression.addData(crimeTimeSeriesForDepartment);

		// Get the line's slope.
		double slopeRounded = regression.getSlope();

		// If the slope is greater than the defined EPSILON, the crime count is
		// tending to go up.
		if (slopeRounded > CRIME_TREND_EPSILON)
			return CrimeMeasureTrend.Up;
		else
		// If the slope is smaller than the defined EPSILON, the crime count
		// is tending to go down.
		if (slopeRounded < CRIME_TREND_EPSILON)
			return CrimeMeasureTrend.Down;
		else
			// Othewise the crime count is considered to be stable over the
			// given period of time.
			return CrimeMeasureTrend.Stable;
	}
	
	/**
	 * Returns a string representation of the ranking of all crimes for a given
	 * department and crime data interval.
	 * <p>
	 * The crime ranking data holder type is {@link CrimesSummary}.
	 * <p>
	 * If there are no crimes for the given department and crime data interval,
	 * null is returned to be considered an an empty result.
	 * 
	 * @param kodUtvaru
	 *            the department code
	 * @param startYear
	 *            the crime data interval initial year
	 * @param startMonth
	 *            the crime data interval initial month
	 * @param endYear
	 *            the crime data interval terminal year
	 * @param endMonth
	 *            the crime data interval terminal month
	 * @param context
	 *            the injected servlet context helper object
	 * 
	 * @return a string representation of the ranking of all crimes for a given
	 *         department and crime data interval
	 * 
	 * @throws CrimeServiceConfigurationErrorException
	 *             if the configuration could not be loaded
	 * @throws CrimeServiceInternalErrorException
	 *             if an internal error occurred
	 */
	// @WebMethod
	public String getCrimeList(String kodUtvaru, int startYear, int startMonth,
			int endYear, int endMonth)
			throws CrimeServiceConfigurationErrorException,
			CrimeServiceInternalErrorException
	{
		this.initializeServletContext();
		// nacti konfiguraci
		this.loadConfiguration();
		this.loadSparqlConfig();

		QueryParameters historyDateParams = this
				.getCrimeHistoryDateParams(Calendar.getInstance());
		QueryParameters kodUtvaruParams = this
				.getCrimeKodUtvaruParams(kodUtvaru);

		// nacti nazev utvaru
		String nazevUtvaruSpachaniNeboZjisteni;
		try
		{
			nazevUtvaruSpachaniNeboZjisteni = this.getNazevUtvaru(
						historyDateParams, kodUtvaruParams);
		}
		catch (TypedQueryInternalErrorException e)
		{
			e.printStackTrace();
			throw new CrimeServiceInternalErrorException(
					"An error occurred when trying to get the department name.",
					e);
		}

		QueryParameters dateIntervalParams = this
				.getCrimeDateIntervalQueryParams(startYear, startMonth,
						endYear, endMonth);
		// priprav si seznam trestnych cinu
		List<CrimeSummary> crimeSummaries;
		try
		{
			crimeSummaries = this.getCrimeList(historyDateParams,
						kodUtvaruParams, dateIntervalParams);
		}
		catch (TypedQueryInternalErrorException e)
		{
			e.printStackTrace();
			throw new CrimeServiceInternalErrorException(
					"An error occurred when trying to get the list of crimes.",
					e);
		}
		

		CrimeSummary[] crimeItems = crimeSummaries
				.toArray(new CrimeSummary[crimeSummaries.size()]);

		return new GsonBuilder()
				.serializeSpecialFloatingPointValues()
				.create()
				.toJson(new CrimesList(kodUtvaru,
						nazevUtvaruSpachaniNeboZjisteni, startYear, startMonth,
						endYear, endMonth, crimeItems), CrimesList.class);
	}

	/**
	 * Returns the name of the police department identified by a given
	 * department code.
	 * 
	 * @param kodUtvaruSpachaniNeboZjisteni
	 *            the department code
	 * @param context
	 *            the injected servlet context helper object
	 *            http://marketplace.eclipse
	 *            .org/marketplace-client-intro?mpc_install=1403812
	 * @return the name of the police department identified by a given
	 *         department code
	 * @throws TypedQueryInternalErrorException
	 */
	private String getNazevUtvaru(QueryParameters historyDateParams,
			QueryParameters kodUtvaruParams)
			throws TypedQueryInternalErrorException
	{
		return new TypedSparqlQuery<String>(mSparqlConfig)
		{
			@Override
			protected QueryResultProjector<ResultSet, String> createResultProjector()
			{
				return new QueryResultProjector<ResultSet, String>()
				{

					@Override
					public String projectResult(ResultSet sourceResult)
					{
						// priprav si konstanty s nazvy vystupnich parametru
						final String nazevUtvaru_VariableName = "utvarNazev";

						String nazevUtvaru = null;

						// nacti a vrat vysledek
						if (sourceResult.hasNext())
						{
							QuerySolution qs = sourceResult.next();
							nazevUtvaru = qs.getLiteral(
									nazevUtvaru_VariableName).getString();
						}

						return nazevUtvaru;
					}
				};
			}

			@Override
			protected QueryName getQueryId()
			{
				return QueryName.NAZEV_UTVARU;
			}

		}.execute(historyDateParams, kodUtvaruParams);
	}

	/**
	 * Returns a string representation of the detailed department info.
	 * <p>
	 * The detailed department info data holder type is {@link CrimeUtvarDetail}.
	 * <p>
	 * If the information about the covered municipalities can't be loaded,
	 * {@code CrimeServiceInternalErrorException} is thrown.
	 * 
	 * @param kodUtvaru
	 *            the department code
	 * @param startYear
	 *            not used; the crime data interval initial year
	 * @param startMonth
	 *            not used; the crime data interval initial month
	 * @param endYear
	 *            not used; the crime data interval terminal year
	 * @param endMonth
	 *            not used; the crime data interval terminal month
	 * @param context
	 *            the injected servlet context helper object
	 * 
	 * @return a string representation of the detailed department info
	 * 
	 * @throws CrimeServiceConfigurationErrorException
	 *             if the configuration could not be loaded
	 * @throws CrimeServiceInternalErrorException
	 *             if an internal error occurred
	 */
	// @WebMethod
	public String getUtvarDetail(String kodUtvaru, int startYear,
			int startMonth, int endYear, int endMonth) throws CrimeServiceInternalErrorException, CrimeServiceConfigurationErrorException
	{
		this.initializeServletContext();
		// nacti konfiguraci
		this.loadConfiguration();
		this.loadSparqlConfig();

		// priprav parametry pro dotazy
		QueryParameters historyDateParams = this
				.getCrimeHistoryDateParams(Calendar.getInstance());
		QueryParameters kodUtvaruParams = this
				.getCrimeKodUtvaruParams(kodUtvaru);
		QueryParameters dateIntervalParams = this
				.getCrimeDateIntervalQueryParams(startYear, startMonth,
						endYear, endMonth);

		// nacti nazev utvaru
		String nazevUtvaru;
		try
		{
			nazevUtvaru = this.getNazevUtvaru(historyDateParams,
						kodUtvaruParams);
		}
		catch (TypedQueryInternalErrorException e)
		{
			e.printStackTrace();
			throw new CrimeServiceInternalErrorException(
					"An error occurred when trying to get the department name.",
					e);
		}

		String[] nazvyObci;
		try
		{
			nazvyObci = this.getPokryteObce(historyDateParams, kodUtvaruParams);
		}
		catch (TypedQueryInternalErrorException e)
		{
			e.printStackTrace();
			throw new CrimeServiceInternalErrorException(
					"An error occurred when trying to get the list of covered municipalities.",
					e);
		}

		// Pokud nejsou vysledky, je to chyba a skonci vyjimkou.
		if (nazvyObci.length == 0)
			throw new CrimeServiceInternalErrorException("No covered municipalities had been found for the given department.");

		return new GsonBuilder()
				.serializeSpecialFloatingPointValues()
				.create()
				.toJson(new CrimeUtvarDetail(kodUtvaru, nazevUtvaru, startYear,
						startMonth, endYear, endMonth, nazvyObci));
	}

	private String[] getPokryteObce(QueryParameters historyDateParams,
			QueryParameters kodUtvaruParams) throws TypedQueryInternalErrorException
	{
		return new TypedSparqlQuery<String[]>(mSparqlConfig)
		{
			@Override
			protected PrefixMapping getPrefixMappings()
			{
				return super
						.getPrefixMappings()
						.setNsPrefix("ruian",
								"http://ruian.linked.opendata.cz/ontology/")
						.setNsPrefix("s", "http://schema.org/");
			}

			@Override
			protected QueryResultProjector<ResultSet, String[]> createResultProjector()
			{
				return new QueryResultProjector<ResultSet, String[]>()
				{
					@Override
					public String[] projectResult(ResultSet sourceResult)
					{
						List<String> nazvyObci = new ArrayList<String>();

						final String nazevObce_VariableName = "nazevObce";

						while (sourceResult.hasNext())
						{
							QuerySolution qs = sourceResult.next();
							nazvyObci.add(qs.getLiteral(nazevObce_VariableName)
									.getString());
						}

						return nazvyObci.toArray(new String[0]);
					}
				};
			}

			@Override
			protected QueryName getQueryId()
			{
				return QueryName.POKRYTE_OBCE;
			}
		}.execute(historyDateParams, kodUtvaruParams);
	}

	@Override
	public String getResourceAsString(String path, Charset charset)
			throws InvalidCrimeServiceResourcePathException,
			CrimeResourceProviderInternalErrorException
	{
		this.initializeServletContext();

		InputStream is = mServletContext.getResourceAsStream(path);
		if (is == null)
			throw new InvalidCrimeServiceResourcePathException(path);

		byte[] resourceAsByteArray = null;

		try
		{
			resourceAsByteArray = TextUtils.readFile(is);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			throw new CrimeResourceProviderInternalErrorException(
					"An exception occurred when trying to read the given resource.",
					e);
		}

		return new String(resourceAsByteArray, charset);
	}
}
