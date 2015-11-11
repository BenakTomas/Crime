package benak.tomas.crimemonitor.client.activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import benak.tomas.crimemonitor.R;
import benak.tomas.crimemonitor.client.activity.handler.OnCrimeSelectedListener;
import benak.tomas.crimemonitor.client.activity.handler.OnShowCrimeListListener;
import benak.tomas.crimemonitor.client.activity.navigation.NavigationQuery;
import benak.tomas.crimemonitor.client.dataloading.params.TskParams;
import benak.tomas.crimemonitor.client.exception.IllegalActivityIdException;
import benak.tomas.crimemonitor.client.ui.fragment.BasicCrimeInfoFragment;
import benak.tomas.crimemonitor.client.ui.fragment.CrimeOverviewFragment;
import benak.tomas.crimemonitor.library.CrimeSummary;
import benak.tomas.crimemonitor.library.CrimesSummary;
import benak.tomas.crimemonitor.library.binding.binder.ParameterBinder;
import benak.tomas.crimemonitor.library.exception.BindingException;
import benak.tomas.crimemonitor.library.exception.TypedQueryInternalErrorException;
import benak.tomas.crimemonitor.library.query.parameter.QueryParameters;
import benak.tomas.crimemonitor.library.query.resultProjector.QueryResultProjector;

import com.google.gson.Gson;

/**
 * CrimeSummaryActivity is the activity class used to display the summary of all
 * crimes.
 * <p>
 * For a given department and a crime data interval this activity displays the
 * summary information about all committed crimes, like
 * <ul>
 * <li>the toplist of five most common crimes including a pie chart view
 * visualizing the related data
 * <li>crime index
 * <li>crime solved ratio
 * <li>crime trend
 * <p>
 * The toplist and the pie chart display is handled by
 * {@link CrimeOverviewFragment}, crime index, crime solved ratio and crime
 * trend is displayed by {@link BasicCrimeInfoFragment}.
 * <p>
 * The crime data type used to hold the information for the statistics is
 * {@link CrimesSummary}.
 * 
 * @author Tom
 * 
 */
public class CrimeSummaryActivity extends CrimeActivityBase<CrimesSummary>
		implements OnCrimeSelectedListener, OnShowCrimeListListener
{

	/**
	 * the mapping of a TSK identifier value to the crime summary for the crime
	 * identified by that particular TSK identifier.
	 * <p>
	 * It is used to resolve crime item selected by the user and identified by
	 * the TSK to the related crime summary object describing the summary
	 * information for that particular crime. The information retrived this way
	 * is used when navigating to that particular crime detail acitivity.
	 */
	private SparseArray<CrimeSummary> mCrimeTskMap	 = new SparseArray<CrimeSummary>();
	/**
	 * the special TSK crime identifier value used to denote the category of
	 * "other crimes"
	 * <p>
	 * Other crimes are all the crimes that are currently not in the top N
	 * crimes displayed. It is essentially a hack to identify the special
	 * toplist item that is not meant to be used to navigate the user to the
	 * selected crime detail activity page.
	 */
	private final int				 OTHER_CRIMES_TSK = 10000;

	/**
	 * the string key for a TSK crime identifier intent extra
	 */
	private final String			  mTsk_IntentKey   = "mTsk";

	/**
	 * the fragment used to display the crime toplist view and the related pie
	 * chart visualizing that toplist data
	 */
	private CrimeOverviewFragment	 mCrimeOverviewFragment;

	/**
	 * the fragment used to display the basic crime statistics (crime index,
	 * crime solved ratio, crime trend)
	 */
	private BasicCrimeInfoFragment	mBasicCrimeInfoFragment;

	@Override
	protected void setupViewReferences()
	{
		super.setupViewReferences();

		FragmentManager fManager = this.getFragmentManager();

		mBasicCrimeInfoFragment = (BasicCrimeInfoFragment) fManager
				.findFragmentById(R.id.basic_crime_info_fragment);

		mCrimeOverviewFragment = (CrimeOverviewFragment) fManager
				.findFragmentById(R.id.crime_overview_fragment);
	}

	/**
	 * Called to refresh the displayed crime statistics.
	 * 
	 * @param crimeData
	 *            the crime summary data to display
	 */
	private void reloadCharts(CrimesSummary crimeData)
	{
		mBasicCrimeInfoFragment.onCrimeDataLoaded(crimeData);

		mCrimeOverviewFragment.loadCrimeOverview(crimeData);
	}

	/**
	 * Sets up the TSK identifier to CrimeSummary mapping.
	 * 
	 * @param crimeData
	 *            the crime list with the crime items that is used to extract
	 *            the mapping information from
	 * 
	 * @see #mCrimeTskMap
	 */
	private void setupTskMap(CrimesSummary crimeData)
	{
		mCrimeTskMap.clear();
		for (CrimeSummary crimeSummary : crimeData.getCrimeSummaries())
			mCrimeTskMap.put(crimeSummary.getTsk(), crimeSummary);
	}

	// TODO: move to superclass (common ancestor of CrimeListActivity and
	// CrimeSummaryActivity)
	/**
	 * Called when a crime item is selected in the crime list view.
	 * <p>
	 * The TSK identifier and the crime name is retrieved from a selected item
	 * and the user is navigated to the crime detail activity for the selected
	 * crime.
	 * <p>
	 * The special case is handled by this method when the user selects the
	 * "OTHER" crime item. In this case the user is not navigated to the
	 * selected crime detail activity, the user selection (usually by a click)
	 * is ignored.
	 * 
	 * @see CrimeDetailActivity
	 */
	@Override
	public void onCrimeSelected(int tsk)
	{
		if (tsk == OTHER_CRIMES_TSK)
			return;

		final CrimeSummary selectedCrimeSummary = mCrimeTskMap.get(tsk);

		try
		{
			this.navigate(R.id.crime_action_detail, this.getKodUtvaruParams(),
					this.getDateIntervalParams(),
					new TskParams(selectedCrimeSummary.getTsk()),
					new QueryParameters()
					{
						@Override
						public void bind(ParameterBinder binder)
								throws BindingException
						{
							binder.bind("mCrimeName",
									selectedCrimeSummary.getCrimeName());
						}
					});
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
	}

	@Override
	protected int getLayoutResourceId()
	{
		return R.layout.activity_crime_summary_final;
	}

	@Override
	protected int getOptionsMenuResourceId()
	{
		return R.menu.crime_summary_actionbar_actions;
	}

	@Override
	protected void displayCustomCrimeViews()
	{
		this.setupTskMap(mActivityState.getCrimeData());

		// nahraj data do grafu prehledu vsech trestnych cinu
		this.reloadCharts(mActivityState.getCrimeData());
	}

	@Override
	protected void collapseNormalLayout()
	{
		mBasicCrimeInfoFragment.getView().setVisibility(View.GONE);
		mCrimeOverviewFragment.getView().setVisibility(View.GONE);
	}

	@Override
	protected void showNormalLayout()
	{
		mBasicCrimeInfoFragment.getView().setVisibility(View.VISIBLE);
		mCrimeOverviewFragment.getView().setVisibility(View.VISIBLE);
	}

	@Override
	protected CharSequence getTitleForCrimeData()
	{
		return this.getTitle();
	}

	/**
	 * Called as a request for navigation to the crime list activity.
	 * <p>
	 * This is called by {@code CrimeOverviewFragment} when the user clicks the
	 * contained "show complete crime list" button.
	 * 
	 * @see CrimeOverviewFragment
	 */
	@Override
	public void showCrimeList()
	{
		try
		{
			this.navigate(R.id.crime_action_list, this.getKodUtvaruParams(),
					this.getDateIntervalParams());
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
	}

	@Override
	protected String getServiceMethodName()
	{
		return "getCrimeSummary";
	}

	@Override
	protected QueryResultProjector<String, CrimesSummary> getResultProjector()
	{
		return new QueryResultProjector<String, CrimesSummary>()
		{
			@Override
			public CrimesSummary projectResult(String sourceResult)
			{
				return new Gson().fromJson(sourceResult, CrimesSummary.class);
			}
		};
	}
}
