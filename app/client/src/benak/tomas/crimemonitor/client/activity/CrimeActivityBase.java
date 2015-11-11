package benak.tomas.crimemonitor.client.activity;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import benak.tomas.crimemonitor.R;
import benak.tomas.crimemonitor.client.activity.navigation.NavigationContext;
import benak.tomas.crimemonitor.client.activity.navigation.NavigationQuery;
import benak.tomas.crimemonitor.client.dataloading.handler.CrimeServiceQueryContext;
import benak.tomas.crimemonitor.client.dataloading.params.DateIntervalParams;
import benak.tomas.crimemonitor.client.dataloading.params.KodUtvaruParams;
import benak.tomas.crimemonitor.client.dataloading.params.LocationParams;
import benak.tomas.crimemonitor.client.dataloadings.DataLoadingFragment;
import benak.tomas.crimemonitor.client.exception.ConfigurationErrorException;
import benak.tomas.crimemonitor.client.exception.DataLoadingTaskException;
import benak.tomas.crimemonitor.client.exception.DataLoadingTaskTimeoutExceededException;
import benak.tomas.crimemonitor.client.exception.IllegalActivityIdException;
import benak.tomas.crimemonitor.client.service.CrimeServiceQueryContextProvider;
import benak.tomas.crimemonitor.client.ui.fragment.SelectCrimeDateIntervalDialogFragment;
import benak.tomas.crimemonitor.client.ui.fragment.SelectCrimeDateIntervalDialogFragment.SelectCrimeDateIntervalDialogFragmentExceptionHandler;
import benak.tomas.crimemonitor.client.ui.fragment.SelectCrimeDateIntervalDialogFragment.SelectCrimeDateIntervalDialogListener;
import benak.tomas.crimemonitor.client.utils.FieldNames;
import benak.tomas.crimemonitor.client.utils.Utils;
import benak.tomas.crimemonitor.library.UtvarAndIntervalBasedCrimeSummary;
import benak.tomas.crimemonitor.library.exception.QueryConfigInternalErrorException;
import benak.tomas.crimemonitor.library.exception.TypedQueryInternalErrorException;
import benak.tomas.crimemonitor.library.query.parameter.QueryParameters;
import benak.tomas.crimemonitor.library.query.resultProjector.QueryResultProjector;

/**
 * CrimeActivityBase is the abstract base class for all the activities that need
 * to handle the crime data. ds dadada
 * 
 * <p>
 * This activity tracks and displays the current department code and name. It
 * displays them in the its title.
 * </p>
 * 
 * <p>
 * It receives notifications about the current department code and name.
 * </p>
 * 
 * <p>
 * It is able to request a reload of the department code and name for the
 * current location.
 * </p>
 * 
 * <p>
 * It is able to request a reload of the crime data for the current department.
 * It is able to request a reload of the crime data for the crime data interval
 * and the current department. The user usually selects the crime data interval
 * and requests the department code reload.
 * </p>
 * 
 * <p>
 * It is able to receive the department and crime data loading callbacks and
 * handle errors, the loading cancellation, empty result. It also can display
 * the returned data in an abstract way. The subclasses define the actual crime
 * data type and how to display it.
 * </p>
 * 
 * <p>
 * The activity is able to react to the background tasks lifecycle events like
 * the task creation and ending, and display and hide the progres bar when there
 * is an instance of a background task running. It also locks the appropriate UI
 * components when there is any background task running.
 * </p>
 * 
 * <p>
 * When a callback method of {@link CrimeDataLoadingListener} is called, the
 * activity decides whether to display a progress bar and lock the appropriate
 * UI components like options menu items.
 * </p>
 * 
 * <p>
 * On data loading error it displays the alert dialog with the error details and
 * logs the error.
 * </p>
 * 
 * <p>
 * When an empty result is returned, the special view for the empty data is
 * displayed.
 * </p>
 * 
 * <p>
 * When the user clicks the back button, the activity stops all of the data
 * loading tasks.
 * </p>
 * 
 * <p>
 * The activity offers the options menu with the links to other data loading
 * activities.
 * </p>
 * 
 * <p>
 * The activity maintains its state and the currently loaded department code,
 * name and crime data. It saves it in the case of the forced stop by the system
 * when the activity might get killed due to the system's need to free the
 * resources. It can restore the saved state afterwards if it had been
 * previously saved. It can read the configuration parameters from the config
 * file, like CrimeMonitor service base URL, initial crime data interval,
 * department code and name.
 * </p>
 * 
 * <p>
 * It transfers the needed parameters to other activities through the
 * {@link Intent} instance that are to be launched from this activity. It can
 * read the passed parameters from the passed {@code Intent} instance.
 * </p>
 * 
 * <p>
 * It enables the user to display the crime data interval selection dialog and
 * request the crime data load for the selected crime data interval.
 * </p>
 * 
 * <p>
 * For department and crime data loading it uses a dedicated instance of
 * {@link CrimeDataLoadingFragment}. The activity uses this fragment with the
 * calls to its {@link CrimeDataLoadingFragment.refreshDataIfLocationHasChanged}
 * and {@link CrimeDataLoadingFragment.refreshDataForInterval} to request the
 * department and crime data loading. It then receives the callbacks as a
 * {@link KodUtvaruLoadingListener} and {@link CrimeDataLoadingListener}. It can
 * register the fragment to start listening for location updates with
 * {@link CrimeDataLoadingFragment.requestLocationUpdates} and stop the
 * listening with {@link CrimeDataLoadingFragment.removeUpdates}.
 * 
 * </p>
 * 
 * @author Tom
 * 
 * @param <CrimeData>
 * 
 * @see CrimeDataLoadingFragment
 * @see CrimeDataLoadingListener
 * @see KodUtvaruLoadingListener
 * @see SelectCrimeDateIntervalDialogListener
 * @see SelectCrimeDateIntervalDialogFragmentExceptionHandler
 * @see UtvarAndIntervalBasedCrimeSummary
 */
public abstract class CrimeActivityBase<CrimeData extends UtvarAndIntervalBasedCrimeSummary & Serializable>
		extends Activity implements SelectCrimeDateIntervalDialogListener,
		SelectCrimeDateIntervalDialogFragmentExceptionHandler,
		CrimeServiceQueryContextProvider, LocationListener
{
	private long							 MINIMUM_LOCATION_REFRESH_INTERVAL	 = 30 * 1000;
	private float							MINIMUM_LOCATION_CHANGE_DISTANCE	  = 50.0f;

	protected ProgressBar					mProgressBar;
	protected View						   mNoDataFoundView;
	protected TextView					   mDataLoadingErrorView;
	private Menu							 mOptionsMenu;

	private final String					 mSelectCrimeIntervalDialogFragmentTag = "SelectCrimeDateIntervalDialogFragment";
	private final String					 mCrimeDataLoadingFragment_Tag		 = "mCrimeDataLoadingFragment_Tag";

	protected DataLoadingFragment<CrimeData> mCrimeDataLoadingFragment;

	protected Properties					 mConfig;
	protected NavigationContext			  mNavigationConfig;
	protected CrimeServiceQueryContext	   mCrimeServiceQueryContext;
	private String						   mLocationProviderId;
	private Location						 mCurrentLocation;

	protected int							mStartYear;
	protected int							mStartMonth;
	protected int							mEndYear;
	protected int							mEndMonth;

	protected CrimeData					  mCrimeData;

	public NavigationContext getNavigationConfig()
	{
		return mNavigationConfig;
	}

	public Properties getConfig()
	{
		return mConfig;
	}

	public static final String  mKodyUtvaruNaNazvy_InstanceStateKey = "mKodyUtvaruNaNazvy";

	private static final String CONFIG_FILE_NAME					= "config.properties";

	/**
	 * Override of the standard {@link Activity#onCreate} lifecycle callback.
	 * <p>
	 * If there is no instance state to be restored, the activity reads its
	 * state with {@link #readInstanceState}. If a
	 * {@link ConfigurationErrorException} is thrown during the instance state
	 * reading, it is caught and the situation is considered unrecoverable and
	 * the activity finishes.
	 * </p>
	 * 
	 * <p>
	 * If there is instance state to be restored, the activity tries to restore
	 * it with {@link #restoreInstanceStateVariables}. If a
	 * {@link ConfigurationErrorException} is thrown during the instance state
	 * reading, it is caught and the situation is considered unrecoverable and
	 * the activity finishes.
	 * </p>
	 * 
	 * <p>
	 * Then the content view is set, the views are set up, the crime data
	 * loading fragment is set up.
	 * </p>
	 * 
	 * @param savedInstanceState
	 *            the bundle with the saved activity's dynamic state
	 * 
	 * @see ConfigurationErrorException
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		// load the config file
		this.initConfig();

		// load the instance state
		try
		{
			if (savedInstanceState == null)
			{
				if (this.isLauncherActivity())
				{
					this.readDefaultInstanceStateFromConfigFile();
				} else
				{
					this.readInstanceStateFromIntent();
				}
			} else
			{
				this.restoreInstanceState(savedInstanceState);
			}
		}
		catch (ConfigurationErrorException e)
		{
			// Log the exception.
			Log.e("readInstanceState",
					"An error occured when reading instance state. See the details in the system log.",
					e);
			// Display a user friendly message about an application crash
			// and
			// terminat the activity.
			this.announceErrorAndFinish("Application encountered an error and will terminate. See system logs for further details.");
			return;
		}

		// setup the department code and crime data loading respective fragments
		this.setupLocationUpdates();
		this.setupCrimeDataLoadingFragment();

		// set up navigation context
		this.setupNavigation();

		// set up the context for queries against the Crime service
		this.setupCrimeServiceQueryContext();

		// set the content view
		this.setContentView(this.getLayoutResourceId());

		// setup view references
		this.setupViewReferences();

		// setup views
		this.setupViews();

		// update UI
		this.updateUIStateAccordingToCurrentState();
	}

	protected void setupLocationUpdates()
	{
		this.initLocationProvider();
	}

	private boolean isLauncherActivity()
	{
		Intent intent = this.getIntent();

		return intent.getCategories().contains(
				"android.intent.category.LAUNCHER");
	}

	protected void initConfig()
	{
		InputStream configFileInputStream = null;
		mConfig = new Properties();

		// Read from the /assets directory
		try
		{
			configFileInputStream = this.getAssets().open(CONFIG_FILE_NAME);
			// load it
			mConfig.load(configFileInputStream);
		}
		catch (IOException e)
		{
			Log.e("initConfig", "Configuration could not be loaded.", e);
			throw new ConfigurationErrorException();
		}
		finally
		{
			if (configFileInputStream != null)
			{
				try
				{
					configFileInputStream.close();
				}
				catch (IOException e)
				{
					// na co se pouziva Logger?
					Log.w("initConfig", "Input stream could not be closed.", e);
				}
			}
		}
	}

	protected void setupNavigation()
	{
		try
		{
			mNavigationConfig = new NavigationContext(this.getConfig());
		}
		catch (QueryConfigInternalErrorException e)
		{
			Log.e("setupNavigation",
					"Navigation configuration could not be loaded.", e);
			throw new ConfigurationErrorException();
		}
	}

	protected void setupCrimeServiceQueryContext()
	{
		try
		{
			mCrimeServiceQueryContext = new CrimeServiceQueryContext(
					this.getConfig());
		}
		catch (QueryConfigInternalErrorException e)
		{
			Log.e("setupCrimeServiceQueryContext",
					"Crime service query configuration could not be loaded.", e);
			throw new ConfigurationErrorException();
		}
	}

	private void announceErrorAndFinish(final String errorMessage)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setMessage(errorMessage).setPositiveButton("OK",
				new DialogInterface.OnClickListener()
				{
					private final Activity mActivityToClose = CrimeActivityBase.this;

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
						mActivityToClose.finish();
					}
				});

		builder.show();
	}

	/**
	 * Reads the default instance state for this activity from the config file.
	 * 
	 * <p>
	 * The CrimeMonitor servuce base URL is loaded from the config file, other
	 * instance state variables are assigned hard-coded values.
	 * </p>
	 * 
	 * @param configFile
	 *            the {@link Properties} instance as a handle to the config file
	 * 
	 * @throws ConfigurationErrorException
	 *             if {@link #mServiceBaseUrl} could not be loaded
	 */
	protected void readDefaultInstanceStateFromConfigFile()
			throws ConfigurationErrorException
	{
		mStartYear = 2008;
		mStartMonth = 1;
		mEndYear = 2012;
		mEndMonth = 11;
		mCrimeData = null;
	}

	/**
	 * Tries to read the instance state for this activity from the
	 * {@link Intent} extras.
	 * 
	 * @param extras
	 *            the {@link Bundle} with the extras to load the instance state
	 *            variables from
	 * 
	 * @throws ConfigurationErrorException
	 *             if {@link #mServiceBaseUrl} could not be loaded
	 */
	protected void readInstanceStateFromIntent()
			throws ConfigurationErrorException
	{
		Bundle extras = this.getIntent().getExtras();

		this.readIntentExtras(extras);
	}

	protected void readIntentExtras(Bundle extras)
	{
		mStartYear = extras.getInt(FieldNames.START_YEAR);
		mStartMonth = extras.getInt(FieldNames.START_MONTH);
		mEndYear = extras.getInt(FieldNames.END_YEAR);
		mEndMonth = extras.getInt(FieldNames.END_MONTH);
		mCrimeData = null;
	}

	protected void refreshCrimeData()
	{
		try
		{
			mCrimeDataLoadingFragment
					.loadData(this.getCrimeDataLoadingParams());
		}
		catch (DataLoadingTaskException e)
		{
			e.printStackTrace();
			Log.e("refreshCrimeData",
					"Data loading exception occured when loading data.", e);
		}
	}

	protected QueryParameters[] getCrimeDataLoadingParams()
	{
		List<QueryParameters> crimeDataLoadingParameters = new ArrayList<QueryParameters>();

		crimeDataLoadingParameters.add(this.getDateIntervalParams());
		crimeDataLoadingParameters.add(this.getParamsForCurrentLocation());
		if (this.isCrimeDataLoaded())
			crimeDataLoadingParameters
					.add(this.getParamsForCrimeDataLocation());

		return (QueryParameters[]) crimeDataLoadingParameters.toArray();
	}

	private LocationParams getParamsForCurrentLocation()
	{
		return LocationParams.getInstance(this.getCurrentLocation(),
				FieldNames.CURRENT_LOCATION_LATITUDE,
				FieldNames.CURRENT_LOCATION_LONGITUDE);
	}

	private LocationParams getParamsForCrimeDataLocation()
	{
		CrimeData crimeData = this.getCrimeData();
		Location crimeDataLocation = new Location(mLocationProviderId);

		crimeDataLocation.setLatitude(crimeData.getLatitude());
		crimeDataLocation.setLongitude(crimeData.getLongitude());

		return LocationParams.getInstance(crimeDataLocation,
				FieldNames.CRIME_DATA_LOCATION_LATITUDE,
				FieldNames.CRIME_DATA_LOCATION_LONGITUDE);
	}

	protected void handleEmptyDataLoaded(QueryParameters... forParams)
	{
		this.updateUIStateAccordingToCurrentState();

		final String message = String.format(Locale.US, "Empty data returned.");
		Log.w("handleEmptyDataLoaded", message);
	}

	public void handleDataLoadingCancelled(QueryParameters... forParams)
	{
		this.updateUIStateAccordingToCurrentState();

		Toast.makeText(this, this.getString(R.string.data_loading_cancelled),
				Toast.LENGTH_SHORT).show();
	}

	public void handleDataLoadingError(Exception e,
			QueryParameters... forParams)
	{
		this.updateUIStateAccordingToCurrentState();

		Location location = QueryParameters.getFirstParametersOfType(forParams,
				LocationParams.class).getLocation();

		// vytvor hlasku pro uzivatele
		String userDetailMessage = this
				.getKodUtvaruErrorNotificationMessageText(e, location);

		// zobraz chybovou hlasku v okenku
		AlertDialog.Builder alertDialogBuilder = new Builder(this);
		alertDialogBuilder
				.setTitle(R.string.data_loading_alert_dialog_error_title)
				.setMessage(userDetailMessage)
				.setPositiveButton("OK", new OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
					}
				}).create().show();

		this.vibrate();
	}

	/**
	 * Sets up the crime data loading fragment.
	 */
	private void setupCrimeDataLoadingFragment()
	{
		FragmentManager fManager = this.getFragmentManager();

		mCrimeDataLoadingFragment = (DataLoadingFragment<CrimeData>) fManager
				.findFragmentByTag(mCrimeDataLoadingFragment_Tag);

		if (mCrimeDataLoadingFragment == null)
		{
			mCrimeDataLoadingFragment = this.createCrimeDataLoadingFragment();

			fManager.beginTransaction()
					.add(mCrimeDataLoadingFragment,
							mCrimeDataLoadingFragment_Tag).commit();
		}
	}

	protected abstract String getServiceMethodName();

	protected abstract QueryResultProjector<String, CrimeData> getResultProjector();

	/**
	 * Abstract method creating the crime data loading fragment instance for
	 * this activity.
	 * 
	 * @return the created {@link CrimeDataLoadingFragment} instance for this
	 *         activity
	 */
	protected DataLoadingFragment<CrimeData> createCrimeDataLoadingFragment()
	{
		return new DataLoadingFragment<CrimeData>()
		{
			@Override
			protected String getServiceMethodName()
			{
				return CrimeActivityBase.this.getServiceMethodName();
			}

			@Override
			protected QueryResultProjector<String, CrimeData> getResultProjector()
			{
				return CrimeActivityBase.this.getResultProjector();
			}

			@Override
			public void onTaskCreated()
			{
				super.onTaskCreated();

				// TODO co udelat? 
				CrimeActivityBase.this.updateUIStateAccordingToCurrentState();
			}

			@Override
			public void onTaskStarted()
			{
				super.onTaskStarted();
				CrimeActivityBase.this.updateUIStateAccordingToCurrentState();
			}

			@Override
			public void onDataLoaded(CrimeData result,
					QueryParameters... forParams)
			{
				super.onDataLoaded(result, forParams);

				CrimeActivityBase.this.setCrimeData(result);
				CrimeActivityBase.this.updateUIStateAccordingToCurrentState();
			}

			@Override
			public void onEmptyDataLoaded(QueryParameters... forParams)
			{
				super.onEmptyDataLoaded(forParams);

				CrimeActivityBase.this.handleEmptyDataLoaded(forParams);
			}

			@Override
			public void onDataLoadingCancelled(QueryParameters... forParams)
			{
				super.onDataLoadingCancelled(forParams);

				CrimeActivityBase.this.handleDataLoadingCancelled(forParams);
			}

			@Override
			public void onDataLoadingError(Exception e,
					QueryParameters... forParams)
			{
				super.onDataLoadingError(e, forParams);

				CrimeActivityBase.this.handleDataLoadingError(e, forParams);
			}
		};
	}

	protected void setCrimeData(CrimeData result)
	{
		// TODO Auto-generated method stub
		
	}

	/**
	 * Gets the resource id for the content view for this activity.
	 * 
	 * @return the resource id for the content view for this activity
	 */
	protected abstract int getLayoutResourceId();

	/**
	 * Gets the resource id for the {@link ProgressBar} view for this activity.
	 * 
	 * @return the resource id for the {@code ProgressBar} view for this
	 *         activity.
	 */
	protected int getProgressBarResourceId()
	{
		return R.id.crime_progress_bar;
	}

	/**
	 * Gets the resource id for the "no data found" view for this activity.
	 * 
	 * @return the resource id for the "no data found" view for this activity
	 */
	protected int getNoDataFoundResourceId()
	{
		return R.id.crime_no_data_found_view;
	}

	/**
	 * Gets the resource id for the options menu for this activity.
	 * 
	 * @return the resource id for the options menu for this activity
	 */
	protected abstract int getOptionsMenuResourceId();

	/**
	 * Sets up the view references for this activity.
	 * <p>
	 * This method looks the views up by their resource ids and stores the
	 * references.
	 * </p>
	 */
	protected void setupViewReferences()
	{
		mProgressBar = (ProgressBar) this.findViewById(this
				.getProgressBarResourceId());
		mNoDataFoundView = (TextView) this.findViewById(this
				.getNoDataFoundResourceId());
		mDataLoadingErrorView = (TextView) this.findViewById(this
				.getDataLoadingErrorViewResourceId());
	}

	/**
	 * Not used.
	 * 
	 * @return
	 */
	protected int getDataLoadingErrorViewResourceId()
	{
		return R.id.crime_data_loading_error_view;
	}

	/**
	 * Sets up the views for this activity.
	 * <p>
	 * Some views are hidden, the action bar is set up.
	 * </p>
	 */
	protected void setupViews()
	{
		mProgressBar.setVisibility(View.GONE);
		mNoDataFoundView.setVisibility(View.GONE);

		this.getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	/**
	 * Handles the exception thrown by the
	 * {@link SelectCrimeDateIntervalDialogFragment}.
	 * 
	 * @param e
	 *            the exception to be handled by this activity
	 */
	@Override
	public void handleSelectCrimeDateIntervalDialogException(Exception e)
	{
		// create the exception message text
		String exceptionMessage = String
				.format("Exception caught from SelectCrimeIntervalDialog. Detailed message follows: %s",
						e.getMessage());

		// log the exception
		Log.e("selectcrimeintervaldialog", exceptionMessage, e);

		String userFriendlyMessage = "Application encountered an error. You cannot pick the time interval for the crime data.";
		// display the user friendly message to the user
		Toast.makeText(this, userFriendlyMessage, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Updates the options menu according to the current running state of the
	 * background tasks.
	 * <p>
	 * If there is an instance of the department data loading task or an
	 * instance of the crime data loading task, some options menu items get
	 * disabled.
	 * </p>
	 * 
	 * @param menu
	 *            the options menu instance for this activity
	 */
	protected void updateOptionsMenuLockStateDueToDataLoading(Menu menu)
	{
		boolean doLockUI = mCrimeDataLoadingFragment.isTaskRunningOrPending();

		// zamkni refresh tlacitko
		MenuItem refreshMenuItem = menu.findItem(R.id.crime_action_refresh);

		if (refreshMenuItem != null)
			refreshMenuItem.setEnabled(!doLockUI);

		// zamkni tlacitko pro zmenu intervalu
		MenuItem setIntervalMenuItem = menu
				.findItem(R.id.crime_action_select_interval);
		if (setIntervalMenuItem != null)
			setIntervalMenuItem.setEnabled(!doLockUI);
	}

	/**
	 * Vibrates the phone for 300 ms.
	 */
	private void vibrate()
	{
		Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(300);
	}

	/**
	 * Displays the currently loaded crime data.
	 * <p>
	 * The custom views for displaying the crime data are shown, the title is
	 * refreshed and the custom views are filled with the loaded crime data.
	 * </p>
	 */
	private void displayCrimeData()
	{
		// collapse the "no data found" view
		this.collapseNoDataFoundLayout();

		// schovej error layout
		this.collapseErrorLayout();

		// show the normal layout
		this.showNormalLayout();

		// display the title
		this.refreshTitle();

		// display custom data
		this.displayCustomCrimeViews();
	}

	/**
	 * Displays the crime data in custom views.
	 */
	protected abstract void displayCustomCrimeViews();

	/**
	 * Stores the loaded crime data.
	 * <p>
	 * The crime data is stored, the crime data interval, the department code
	 * and name for the current crime data is also stored.
	 * </p>
	 * 
	 * @param result
	 */
	protected void storeResult(CrimeData result)
	{

	}

	/**
	 * Not used.
	 * 
	 * @param startYear
	 * @param startMonth
	 * @param endYear
	 * @param endMonth
	 * @param kodUtvaru
	 * @param e
	 * @return
	 */
	private String getErrorViewMessage(int startYear, int startMonth,
			int endYear, int endMonth, String kodUtvaru, Exception e)
	{
		return this.getString(R.string.crime_data_loading_error_view_text,
				this.getCrimeDataLoadingErrorCauseString(e));
	}

	/**
	 * Not used.
	 */
	private void displayErrorData()
	{
		// collapse the "no data found" view
		this.collapseNoDataFoundLayout();

		// collapse the normal layout
		this.collapseNormalLayout();

		// display the title
		this.refreshTitle();

		// show the error layout
		this.showErrorLayout();
	}

	/**
	 * Not used.
	 */
	protected void showErrorLayout()
	{
		mDataLoadingErrorView.setVisibility(View.VISIBLE);
	}

	/**
	 * Not used.
	 */
	protected void collapseErrorLayout()
	{
		mDataLoadingErrorView.setVisibility(View.GONE);
	}

	/**
	 * Called when the users selects the crime data interval.
	 * <p>
	 * When this method is called, {@link CrimeDataLoadingFragment} is requested
	 * to load the crime data for the given interval.
	 * </p>
	 * 
	 * @param startYear
	 *            the selected initial year of the crime data interval
	 * @param startMonth
	 *            the selected initial month of the crime data interval
	 * @param endYear
	 *            the selected terminal year of the crime data interval
	 * @param endMonth
	 *            the selected terminal month of the crime data interval
	 */
	@Override
	public void onCrimeDateIntervalSelected(int startYear, int startMonth,
			int endYear, int endMonth)
	{
		this.setInterval(startYear, startMonth, endYear, endMonth);

		// zavolej refresh
		this.refreshCrimeData();
	}

	private void setInterval(int startYear, int startMonth, int endYear,
			int endMonth)
	{
		// TODO
	}

	/**
	 * Shows the crime data interval selection dialog.
	 */
	private void showSelectCrimeDateIntervalDialog()
	{
		DialogFragment dialog = new SelectCrimeDateIntervalDialogFragment(
				this.getStartYear(), this.getEndMonth());

		dialog.show(this.getFragmentManager(),
				mSelectCrimeIntervalDialogFragmentTag);
	}

	protected String getEmptyResultNotificationMessageText()
	{
		return "No data exists for current search criteria";
	}

	/**
	 * Returns the crime data loading error notification message.
	 * 
	 * @param startYear
	 *            the initial year of the crime data interval
	 * @param startMonth
	 *            the initial month of the crime data interval
	 * @param endYear
	 *            the terminal year of the crime data interval
	 * @param endMonth
	 *            the terminal month of the crime data interval
	 * @param kodUtvaru
	 *            the department code string
	 * @param e
	 *            the exception instance with the detailed error info
	 * 
	 * @return the crime data loading error notification message
	 */
	protected String getCrimeDataErrorNotificationMessageText(int startYear,
			int startMonth, int endYear, int endMonth, String kodUtvaru,
			Exception e)
	{
		final CharSequence causeDescription = this
				.getCrimeDataLoadingErrorCauseString(e);

		return this.getString(R.string.crime_data_loading_error_text, this
				.getCrimeData().getNazevUtvaru(), Utils
				.getCrimeIntervalBoundString(startYear, startMonth), Utils
				.getCrimeIntervalBoundString(endYear, endMonth),
				causeDescription);
	}

	/**
	 * Returns the textual description of the cause of a crime data loading
	 * error.
	 * 
	 * @param e
	 *            the exception instance with the detailed error info
	 * 
	 * @return the textual description of the cause of a crime data loading
	 *         error
	 */
	private CharSequence getCrimeDataLoadingErrorCauseString(Exception e)
	{
		if (e instanceof DataLoadingTaskTimeoutExceededException)
		{
			return this.getString(R.string.data_loading_error_timeout);
		} else
		{
			return this.getString(R.string.data_loading_error_other);
		}
	}

	/**
	 * Hides the views used to display the crime data.
	 */
	protected abstract void collapseNormalLayout();

	/**
	 * Hides the "no data found" view.
	 */
	protected void collapseNoDataFoundLayout()
	{
		mNoDataFoundView.setVisibility(View.GONE);
	}

	/**
	 * Shows the views used to display the crime data.
	 */
	protected abstract void showNormalLayout();

	/**
	 * Shows the "no data found" view.
	 */
	protected void showNoDataFoundLayout()
	{
		mNoDataFoundView.setVisibility(View.VISIBLE);
	}

	/**
	 * Refreshes the title for this activity using the loaded crime data.
	 */
	private void refreshTitle()
	{
		ActionBar actionBar = this.getActionBar();

		actionBar.setTitle(this.getTitleForCrimeData());
		actionBar.setSubtitle(this.getSubtitleForCrimeData());
	}

	/**
	 * Returns the string to be used as an ActionBar title.
	 * <p>
	 * Usually this is taken from the loaded crime data, but the default is to
	 * use this activity's title.
	 * </p>
	 * 
	 * @return the string to be used as an ActionBar title
	 */
	protected CharSequence getTitleForCrimeData()
	{
		return this.getTitle();
	}

	/**
	 * Returns the string to be used as an ActionBar subtitle.
	 * <p>
	 * Usually this is taken from the loaded crime data. The default is to use
	 * the department name and the selected crime data interval.
	 * </p>
	 * 
	 * @return the string to be used as an ActionBar subtitle
	 */
	protected CharSequence getSubtitleForCrimeData()
	{
		return String.format("%s, %s", this.getNazevUtvaruStringForSubtitle(),
				benak.tomas.crimemonitor.client.utils.Utils
						.getCrimeIntervalString(this.getStartYear(),
								this.getStartMonth(), this.getEndYear(),
								this.getEndMonth()));
	}

	/**
	 * Returns the string to be used in the place of the department name.
	 * <p>
	 * If the department name is loaded, it is used. Otherwise the predefined
	 * text is used.
	 * </p>
	 * 
	 * @return the string to be used in the place of the department name
	 */
	protected CharSequence getNazevUtvaruStringForSubtitle()
	{
		if (this.isCrimeDataLoaded())
			return this.getCrimeData().getNazevUtvaru();
		else
			return this.getString(R.string.no_department_loaded);
	}

	/**
	 * Creates the options menu for this activity.
	 * <p>
	 * The created menu reference is stored for the later use, for example to
	 * lock some of its items.
	 * </p>
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater menuInflater = this.getMenuInflater();

		// nahraj do menu definici tlacitek action baru
		// TODO: Jak se pozna, co pujde do menu aktivity a co do action baru?
		menuInflater.inflate(this.getOptionsMenuResourceId(), menu);

		// a nechej predka taky pridat neco do menu
		super.onCreateOptionsMenu(menu);

		// store the menu reference
		mOptionsMenu = menu;
		// do display the options menu
		return true;
	}

	/**
	 * Updates the UI according to the current running state of the department
	 * code and crime data loading task.
	 */
	private void updateUILockStateDueToDataLoading()
	{
		this.updateOptionsMenuLockStateDueToDataLoading();
	}

	/**
	 * Updates the options menu according to the current running state of the
	 * department code and crime data loading task.
	 */
	private void updateOptionsMenuLockStateDueToDataLoading()
	{
		if (mOptionsMenu != null)
			this.onPrepareOptionsMenu(mOptionsMenu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		// call the default implementation
		boolean displayMenu = super.onPrepareOptionsMenu(menu);

		// update the menu
		this.updateOptionsMenuLockStateDueToDataLoading(menu);

		// return whether to display the menu
		return displayMenu;
	}

	// TODO: zaved abstraktni IDcka menu items
	/**
	 * Handles the options menu item selection.
	 * <p>
	 * As a result, either the crime is refreshed, the crime data interval
	 * selection dialog is displayed or the used is navigated to a different
	 * activity to display an alternative crime statistics view.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.crime_action_refresh:

			this.refreshCrimeData();

			return true;

		case R.id.crime_action_select_interval:

			// show the crime data interval selection dialog
			this.showSelectCrimeDateIntervalDialog();

			return true;

		case R.id.crime_action_detail:
		case R.id.crime_action_summary:
		case R.id.crime_action_list:
		case R.id.crime_action_utvar_detail:

			try
			{
				this.navigate(item.getItemId(), this.getDateIntervalParams());
			}
			catch (TypedQueryInternalErrorException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IllegalActivityIdException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	protected QueryParameters getDateIntervalParams()
	{
		return DateIntervalParams.getInstance(this.getStartYear(),
				this.getStartMonth(), this.getEndYear(), this.getEndMonth());
	}

	protected void navigate(int activityId, QueryParameters... params)
			throws TypedQueryInternalErrorException, IllegalActivityIdException
	{
		NavigationQuery.navigate(activityId, this, this.getNavigationConfig(),
				params);
	}

	/**
	 * Standard {@link Activity#onStart} override.
	 * <p>
	 * When called, decides whether to display the loaded data, or an empty
	 * result view, or an empty screen if no data had been loaded yet.
	 * </p>
	 * 
	 * <p>
	 * {@link CrimeDataLoadingFragment} is requested to reload the department
	 * code and name for the current location.
	 * </p>
	 */
	@Override
	protected void onStart()
	{
		super.onStart();

		// TODO podle toho, jaky je stav aktivity, zobraz data, pripadne dej
		// pokyn k refreshi
	}

	/**
	 * Restores the instance state previously saved.
	 * 
	 * @param savedInstanceState
	 *            the {@link Bundle} with the saved instance state
	 * 
	 * @throws ConfigurationErrorException
	 *             if some of the instance state variables could not be loaded
	 */
	protected void restoreInstanceState(Bundle savedInstanceState)
			throws ConfigurationErrorException
	{
		CrimeData crimeDataRestored = null;
		if (savedInstanceState.containsKey(FieldNames.CRIME_DATA))
		{
			crimeDataRestored = (CrimeData) savedInstanceState
					.getSerializable(FieldNames.CRIME_DATA);
		}

		mStartYear = savedInstanceState.getInt(FieldNames.START_YEAR);
		mStartMonth = savedInstanceState.getInt(FieldNames.START_MONTH);
		mEndYear = savedInstanceState.getInt(FieldNames.END_YEAR);
		mEndMonth = savedInstanceState.getInt(FieldNames.END_MONTH);
		mCrimeData = crimeDataRestored;
	}

	/**
	 * Standard callback override of {@link Activity#onSaveInstanceState}.
	 * <p>
	 * Saves the instance state for this activity as well as the loaded crime
	 * data.
	 * </p>
	 * 
	 * @param outState
	 *            the {@link Bundle} with the saved state
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);

		outState.putInt(FieldNames.START_YEAR, this.getStartYear());
		outState.putInt(FieldNames.START_MONTH, this.getStartMonth());
		outState.putInt(FieldNames.END_YEAR, this.getEndYear());
		outState.putInt(FieldNames.END_MONTH, this.getEndMonth());

		CrimeData crimeDataToSave = this.getCrimeData();
		if (crimeDataToSave != null)
		{
			outState.putSerializable(FieldNames.CRIME_DATA, crimeDataToSave);
		}
	}

	/**
	 * Displays the initial blank screen when no data has been loaded yet.
	 */
	private void displayInitialData()
	{
		this.collapseNoDataFoundLayout();
		this.collapseErrorLayout();
		this.collapseNormalLayout();
		this.refreshTitle();
	}

	/**
	 * Displays the "no data view" and refreshes the activity's title.
	 */
	private void displayEmptyData()
	{
		// schovej normalni custom views
		this.collapseNormalLayout();

		// schovej error layout
		this.collapseErrorLayout();

		// display the text with "no data found"
		this.showNoDataFoundLayout();

		// refresh the title
		this.refreshTitle();
	}

	/**
	 * Standard {@link Activity#onResume} override.
	 * <p>
	 * Listening for location updates is turned on for
	 * {@link CrimeDataLoadingFragment}.
	 * </p>
	 */
	@Override
	protected void onResume()
	{
		super.onResume();

		// Zapni location updates na fragmentu
		this.getLocationManager().requestLocationUpdates(mLocationProviderId,
				MINIMUM_LOCATION_REFRESH_INTERVAL,
				MINIMUM_LOCATION_CHANGE_DISTANCE, this);
	}

	/**
	 * Standard {@link Activity#onPause} override.
	 * <p>
	 * Listening for location updates is turned off for
	 * {@link CrimeDataLoadingFragment}. Background tasks in
	 * {@code CrimeDataLoadingFragment} are cancelled.
	 * </p>
	 */
	@Override
	protected void onPause()
	{
		super.onPause();

		// Vypni location updates na fragmentu
		this.getLocationManager().removeUpdates(this);

		// Stopni tasky, protoze aktivita finisuje, nebo je jen prekryta jinou
		// (potom dostane onRestart a onStart a onResume, nebo rovnou onResume
		// po
		// stisku tlacitka back z prekryvajici aktivity)
		if (!this.isChangingConfigurations())
			this.cancelTasks();
	}

	protected void updateUIStateAccordingToCurrentState()
	{
		if(this.isDataLoading())
			;
		
		// refresh the progress bar
		this.refreshProgressBar();

		// refresh the UI lock state
		this.updateUILockStateDueToDataLoading();
	}

	private void refreshProgressBar()
	{
		if (this.isDataLoading())
			mProgressBar.setVisibility(View.VISIBLE);
		else
			mProgressBar.setVisibility(View.GONE);
	}

	protected String getKodUtvaruErrorNotificationMessageText(Exception e,
			Location forLocation)
	{
		final String causeDescription;

		if (e instanceof DataLoadingTaskTimeoutExceededException)
		{
			causeDescription = this
					.getString(R.string.data_loading_error_timeout);
		} else
		{
			causeDescription = this
					.getString(R.string.data_loading_error_other);
		}

		return this.getString(R.string.kod_utvaru_loading_error_text,
				forLocation.getLatitude(), forLocation.getLongitude(),
				causeDescription);
	}

	/**
	 * Standard {@link Activity#onBackPressed} override.
	 * <p>
	 * When this method is called and any background task is running in
	 * {@link CrimeDataLoadingFragment}, it is cancelled.
	 * </p>
	 */
	@Override
	public void onBackPressed()
	{
		if (this.isDataLoading())
		{
			// cancel s ThreadInterrupt?
			mCrimeDataLoadingFragment.cancelTask();
		} else
		{
			super.onBackPressed();
		}
	}

	/**
	 * Cancels all the tasks in {@link CrimeDataLoadingFragment} and notifies
	 * the user.
	 */
	private void cancelTasks()
	{
		if (mCrimeDataLoadingFragment.isTaskRunningOrPending())
		{
			mCrimeDataLoadingFragment.cancelTask();
			Toast.makeText(this,
					this.getString(R.string.crime_data_loading_cancel_pending),
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Standard {@link Activity#onStop} override.
	 * <p>
	 * When this callback is called and it is not due to a configuration change,
	 * the {@link CrimeDataLoadingFragment} tasks are cancelled.
	 * </p>
	 */
	@Override
	protected void onStop()
	{
		// TODO Auto-generated method stub
		super.onStop();

		// Pokud se nemeni konfigurace, ale aktivita ze zastavuje (neni
		// viditelna pro uzivatele, nebo ji system ukoncuje pro nedostatek
		// zdroju), zastav tasky.
		if (!this.isChangingConfigurations())
			this.cancelTasks();
	}

	/**
	 * Standard {@link Activity#onDestroy} override.
	 * <p>
	 * When this callback is called and it is not due to a configuration change,
	 * the {@link CrimeDataLoadingFragment} tasks are cancelled.
	 * </p>
	 */
	@Override
	protected void onDestroy()
	{
		super.onDestroy();

		// zastav tasky
		if (!this.isChangingConfigurations())
			this.cancelTasks();
	}

	@Override
	public CrimeServiceQueryContext getCrimeServiceQueryContext()
	{
		return mCrimeServiceQueryContext;
	}

	/**
	 * Tries to select an appropriate location provider and obtain an initial
	 * location. The location providers are iterated in the following order:
	 * {@link LocationManager.GPS_PROVIDER GPS},
	 * {@link LocationManager.NETWORK_PROVIDER network provider},
	 * {@link LocationManager.PASSIVE_PROVIDER passive location provider}. If
	 * the GPS provider is not on, the user is prompted in
	 * {@link #promptUserToTurnOnGPSAndWiFi()} to turn it on. The first provider
	 * that is on is selected as a location provider and stored to
	 * {@link #mLocationProviderId}. The initial location is stored in
	 * {@link #mCurrentLocation} after being retrieved from any of the iterated
	 * providers.
	 */
	private void initLocationProvider()
	{
		String selectedLocationProviderId = null;
		Location initialLocation = null;
		// zkontroluj, jestli je zapnuty GPS a jestli ne, pozadej o jeho zapnuti
		LocationManager locationManager = (LocationManager) this
				.getSystemService(android.content.Context.LOCATION_SERVICE);

		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
		{
			this.promptUserToTurnOnGPSAndWiFi();
		}

		// Pokud stale neni povolen, bude se pouzivat network provider
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
		{
			selectedLocationProviderId = LocationManager.GPS_PROVIDER;
			initialLocation = locationManager
					.getLastKnownLocation(selectedLocationProviderId);
		}

		if (initialLocation == null
				&& locationManager
						.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
		{
			initialLocation = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

			if (selectedLocationProviderId == null)
			{
				selectedLocationProviderId = LocationManager.NETWORK_PROVIDER;
			}
		} else if (initialLocation == null)
		{
			initialLocation = locationManager
					.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

			if (selectedLocationProviderId == null)
			{
				selectedLocationProviderId = LocationManager.PASSIVE_PROVIDER;
			}
		}

		// nastav location providera a aktualni polohu
		mLocationProviderId = selectedLocationProviderId;
		mCurrentLocation = initialLocation;
	}

	/**
	 * Prompts the user to turn on the GPS location service.
	 */
	private void promptUserToTurnOnGPSAndWiFi()
	{
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
		final String message = this
				.getString(R.string.crime_summary_enable_gps_and_wifi_prompt_string);

		builder.setMessage(message)
				.setPositiveButton("OK", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface d, int id)
					{
						CrimeActivityBase.this
								.startActivity(new Intent(action));
						d.dismiss();
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface d, int id)
							{
								d.cancel();
							}
						});
		builder.create().show();
	}

	private LocationManager getLocationManager()
	{
		return (LocationManager) this
				.getSystemService(android.content.Context.LOCATION_SERVICE);
	}

	@Override
	public void onLocationChanged(Location location)
	{
		this.setCurrentLocation(location);

		if (!this.isDataLoading())
		{
			if (this.isCrimeDataLoaded())
			{
				if (this.isCurrentLocationFarFromDataLocation())
					this.refreshCrimeData();
			} else
				this.refreshCrimeData();
		}
	}

	protected boolean isCurrentLocationFarFromDataLocation()
	{
		CrimeData crimeData = this.getCrimeData();
		Location currentLocation = this.getCurrentLocation();
		float results[] = new float[3];

		Location.distanceBetween(crimeData.getLatitude(),
				crimeData.getLongitude(), currentLocation.getLatitude(),
				currentLocation.getLongitude(), results);

		float distance = results[0];

		return distance >= MINIMUM_LOCATION_CHANGE_DISTANCE;
	}

	private CrimeData getCrimeData()
	{
		return mCrimeData;
	}

	private boolean isCrimeDataLoaded()
	{
		return this.getCrimeData() != null;
	}

	private boolean isDataLoading()
	{
		return this.getCrimeDataLoadingFragment().isTaskRunningOrPending();
	}

	/**
	 * Not used.
	 */
	@Override
	public void onProviderDisabled(String arg0)
	{
	}

	/**
	 * Not used.
	 */
	@Override
	public void onProviderEnabled(String provider)
	{
	}

	/**
	 * Not used.
	 */
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras)
	{
	}

	protected Location getCurrentLocation()
	{
		return mCurrentLocation;
	}

	protected void setCurrentLocation(Location location)
	{
		mCurrentLocation = location;
	}

	protected DataLoadingFragment<CrimeData> getCrimeDataLoadingFragment()
	{
		return this.mCrimeDataLoadingFragment;
	}

	protected int getStartYear()
	{
		return this.mStartYear;
	}

	protected void setStartYear(int startYear)
	{
		this.mStartYear = startYear;
	}

	protected int getStartMonth()
	{
		return this.mStartMonth;
	}

	protected void setStartMonth(int startMonth)
	{
		this.mStartMonth = startMonth;
	}

	protected int getEndYear()
	{
		return this.mEndYear;
	}

	protected void setEndYear(int endYear)
	{
		this.mEndYear = endYear;
	}

	protected int getEndMonth()
	{
		return this.mEndMonth;
	}

	protected void setEndMonth(int endMonth)
	{
		this.mEndMonth = endMonth;
	}
}
