package benak.tomas.crimemonitor.client.activity;

import android.app.FragmentManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import benak.tomas.crimemonitor.R;
import benak.tomas.crimemonitor.client.exception.ConfigurationErrorException;
import benak.tomas.crimemonitor.client.ui.fragment.BasicCrimeInfoFragment;
import benak.tomas.crimemonitor.client.ui.fragment.CrimeMonthHistogramFragment;
import benak.tomas.crimemonitor.client.utils.FieldNames;
import benak.tomas.crimemonitor.library.CrimeSummaryDetailed;
import benak.tomas.crimemonitor.library.query.resultProjector.QueryResultProjector;

import com.google.gson.Gson;

/**
 * CrimeDetailActivity is the activity class to display the detailed statistics for a given crime.
 * <p>
 * The crime data type used to hold the information for the statistics is {@link CrimeSummaryDetailed}.
 * <p>
 * The given crime is identified by its TSK identifier.
 * @author Tom
 *
 */
public class CrimeDetailActivity extends
		CrimeActivityBase<CrimeSummaryDetailed>
{
	/**
	 * the TSK identifier of the given crime.
	 * <p> See the overview for a more detailed explanation of the term.
	 */
	private int							mTsk;
	/**
	 * the name of the given crime
	 * <p>The name of the given crime is displayed in the activity title.
	 */
	private String						mCrimeName;
	/**
	 * the intent extra key for {@link #mCrimeName}
	 */
	public static final String			mCrimeName_IntentKey				= "mCrimeName";
	/**
	 * the instance state key for {@link #mCrimeName} to use in {@code onSaveInstanceState} and {@code onRestoreInstanceState} 
	 */
	public static final String			mCrimeName_InstanceStateKey			= "mCrimeName";
	/**
	 * the instance state key for {@link #mTsk} to use in {@code onSaveInstanceState} and {@code onRestoreInstanceState} 
	 */
	public static final String			mTsk_InstanceStateKey				= "mTsk";
	/**
	 * the intent extra key for {@link #mTsk}
	 */
	public static final String			mTsk_IntentKey						= "mTsk";

	/**
	 * the fragment with the basic crime info for the given crime
	 */
	private BasicCrimeInfoFragment		mBasicCrimeInfoFragment;
	/**
	 * the fragment with the monthly histogram for the given crime
	 */
	private CrimeMonthHistogramFragment	mCrimeMonthHistogramFragment;

	/**
	 * Sets up the references to the used views by looking them using their resource IDs.
	 */
	@Override
	protected void setupViewReferences()
	{
		super.setupViewReferences();

		FragmentManager fManager = this.getFragmentManager();

		mBasicCrimeInfoFragment = (BasicCrimeInfoFragment) fManager
				.findFragmentById(R.id.basic_crime_info_fragment);

		mCrimeMonthHistogramFragment = (CrimeMonthHistogramFragment) fManager
				.findFragmentById(R.id.crime_month_histogram_fragment);
	}

	@Override
	protected void readIntentExtras(Bundle extras)
	{
		super.readIntentExtras(extras);

		mCrimeName = extras.getString(FieldNames.CRIME_NAME);
		mTsk = extras.getInt(FieldNames.TSK);
	}

	@Override
	protected void storeResult(CrimeSummaryDetailed result)
	{
		super.storeResult(result);

		// uloz si nazev trestneho cinu
		mTsk = result.getTsk();
		mCrimeName = result.getCrimeName();
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		
		outState.putString(FieldNames.CRIME_NAME, mCrimeName);
		outState.putInt(FieldNames.TSK, mTsk);
	}
	
	@Override
	protected void restoreInstanceState(Bundle savedInstanceState)
	{
		super.restoreInstanceState(savedInstanceState);
		
		mTsk = savedInstanceState.getInt(FieldNames.TSK);
		mCrimeName = savedInstanceState.getString(FieldNames.CRIME_NAME);
	}
	
	/**
	 * Gets the action bar subtitle using the currently loaded crime data.
	 */
	@Override
	protected CharSequence getSubtitleForCrimeData()
	{
		return String.format("%s: %s", mCrimeName,
				benak.tomas.crimemonitor.client.utils.Utils
						.getCrimeIntervalString(mActivityState.getStartYear(), mActivityState.getEndMonth(),
								mActivityState.getEndYear(), mActivityState.getEndMonth()));
	}

	@Override
	protected int getLayoutResourceId()
	{
		return R.layout.activity_crime_detail;
	}

	@Override
	protected int getOptionsMenuResourceId()
	{
		return R.menu.crime_detail_actionbar_actions;
	}

	@Override
	protected void displayCustomCrimeViews()
	{
		// zobraz basic crime info
		mBasicCrimeInfoFragment.onCrimeDataLoaded(mActivityState.getCrimeData());

		// zobraz column chart
		mCrimeMonthHistogramFragment.displayHistogram(mActivityState.getCrimeData());
	}

	@Override
	protected void collapseNormalLayout()
	{
		mBasicCrimeInfoFragment.getView().setVisibility(View.GONE);
		mCrimeMonthHistogramFragment.getView().setVisibility(View.GONE);
	}

	@Override
	protected void showNormalLayout()
	{
		mBasicCrimeInfoFragment.getView().setVisibility(View.VISIBLE);
		mCrimeMonthHistogramFragment.getView().setVisibility(View.VISIBLE);
	}

	@Override
	protected CharSequence getTitleForCrimeData()
	{
		return mCrimeName;
	}

	@Override
	protected String getServiceMethodName()
	{
		return "getCrimeDetail";
	}

	@Override
	protected QueryResultProjector<String, CrimeSummaryDetailed> getResultProjector()
	{
		return new QueryResultProjector<String, CrimeSummaryDetailed>()
		{
			@Override
			public CrimeSummaryDetailed projectResult(String sourceResult)
			{
				return new Gson().fromJson(sourceResult,
						CrimeSummaryDetailed.class);
			}
		};
	}
}
