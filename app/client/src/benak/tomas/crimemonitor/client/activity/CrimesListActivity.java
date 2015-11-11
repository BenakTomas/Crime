package benak.tomas.crimemonitor.client.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import benak.tomas.crimemonitor.R;
import benak.tomas.crimemonitor.client.activity.handler.OnCrimeSelectedListener;
import benak.tomas.crimemonitor.client.dataloading.params.TskParams;
import benak.tomas.crimemonitor.client.exception.IllegalActivityIdException;
import benak.tomas.crimemonitor.library.CrimeSummary;
import benak.tomas.crimemonitor.library.CrimesList;
import benak.tomas.crimemonitor.library.binding.binder.ParameterBinder;
import benak.tomas.crimemonitor.library.exception.BindingException;
import benak.tomas.crimemonitor.library.exception.TypedQueryInternalErrorException;
import benak.tomas.crimemonitor.library.query.parameter.QueryParameters;
import benak.tomas.crimemonitor.library.query.resultProjector.QueryResultProjector;

import com.google.gson.Gson;

/**
 * CrimesListActivity is the activity class to display the list of all crimes.
 * <p>The list is displayed for a given department and a crime data interval.
 * <p>The crime data type used to hold the information for the statistics is {@link CrimesList}.
 * 
 * @author Tom
 *
 */
public class CrimesListActivity extends CrimeActivityBase<CrimesList> implements OnCrimeSelectedListener
{
	/**
	 * the string key for a TSK crime identifier intent extra
	 */
	private final String	mTsk_IntentKey = "mTsk";
	
	/**
	 * the mapping of a TSK identifier value to the crime summary for the crime identified by that particular TSK identifier
	 */
	private SparseArray<CrimeSummary>	mCrimeTskMap			= new SparseArray<CrimeSummary>();
	
	/**
	 * the list view used to display the crime list items
	 * <p>See {@link CrimeListAdapter} and {@link CrimeListItem} to get the details about the data
	 * that are displayed in the list and about the creation of crime list items. 
	 */
	private ListView mList;
	
	@Override
	protected void setupViewReferences()
	{
		super.setupViewReferences();
		// nastav odkazy na nadpis zebricku a zebricek
		mList = (ListView) this.findViewById(R.id.crime_list_listview);
	}
	
	/**
	 * Sets up the views for this activity at the activity startup.
	 * <p>
	 * The {@link #mList} list view is set up listen for its {@code OnItemClickListener} and handle it.
	 */
	@Override
	protected void setupViews()
	{
		super.setupViews();
		
		// set up the crime list view adapter
		mList.setAdapter(new CrimeListAdapter(this));
		// set up the on item click listener
		mList.setOnItemClickListener(new OnItemClickListener()
		{
			// call back to the activity
			private OnCrimeSelectedListener	mListener	= CrimesListActivity.this;

			/**
			 * Called when an item of the {@code mList} is clicked to notify the parent activity about
			 * the crime item selected. 
			 */
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int itemPosition, long itemId)
			{
				// get the adapter for the mList
				CrimeListAdapter crimeListAdapter = (CrimeListAdapter) mList
						.getAdapter();
				// get the selected item position within the adapter's inner item collection
				CrimeListItem selectedItem = crimeListAdapter
						.getItem(itemPosition);

				// notify the activity that a crime item has been selected
				mListener.onCrimeSelected(selectedItem.getTsk());
			}
		});
	}
	
	@Override
	protected int getLayoutResourceId()
	{
		return R.layout.activity_crimes_list;
	}

	@Override
	protected int getOptionsMenuResourceId()
	{
		return R.menu.crime_list_actionbar_actions;
	}

	@Override
	protected void collapseNormalLayout()
	{
		mList.setVisibility(View.GONE);
	}

	@Override
	protected void showNormalLayout()
	{
		mList.setVisibility(View.VISIBLE);
	}
	
	/**
	 * Displays the crime list.
	 * <p>The data to be displayed are retrieved from the currently loaded crime list data.
	 * 
	 * @see CrimeListAdapter
	 * @see CrimeListItem
	 */
	@Override
	protected void displayCustomCrimeViews()
	{
		// nastav mapovani TSK na trestne ciny
		this.setupTskMap(mActivityState.getCrimeData());
		
		// get the currently loaded crime list items
		CrimeSummary[] crimeSummaryItems = mActivityState.getCrimeData().getCrimeItems();
		
		// prepare the list of crime list items
		List<CrimeListItem> crimeListItems = new ArrayList<CrimeListItem>(crimeSummaryItems.length);
		
		// for each CrimeSummary create a crime list item
		for(CrimeSummary crimeSummary : crimeSummaryItems)
			crimeListItems.add(new CrimeListItem(crimeSummary.getTsk(), crimeSummary.getCrimeName(), crimeSummary.getCrimeNumber()));
		
		// insert the crime list item list into the adapter for the crime list
		CrimeListAdapter crimeListAdapter = (CrimeListAdapter)mList.getAdapter();
		crimeListAdapter.setNotifyOnChange(false);
		crimeListAdapter.clear();
		crimeListAdapter.addAll(crimeListItems);
		crimeListAdapter.notifyDataSetChanged();
	}
	
	/**
	 * Sets up the TSK identifier to CrimeSummary mapping.
	 * 
	 * @param crimeData the crime list with the crime items that is used to extract the mapping information from
	 * 
	 * @see #mCrimeTskMap
	 */
	protected void setupTskMap(CrimesList crimeData)
	{
		mCrimeTskMap.clear();
		for (CrimeSummary crimeSummary : crimeData.getCrimeItems())
			mCrimeTskMap.put(crimeSummary.getTsk(), crimeSummary);
	}

	// TODO: move to superclass (common ancestor of CrimeListActivity and CrimeSummaryActivity)
	/**
	 * Called when a crime item is selected in the crime list view.
	 * <p>
	 * The TSK identifier and the crime name is retrieved from a selected item and the user
	 * is navigated to the crime detail activity for the selected crime.
	 * 
	 * @see CrimeDetailActivity
	 */
	@Override
	public void onCrimeSelected(int tsk)
	{
		final CrimeSummary selectedCrimeSummary = mCrimeTskMap.get(tsk);

		try
		{
			this.navigate(R.id.crime_action_detail,
					this.getKodUtvaruParams(),
					this.getDateIntervalParams(),
					new TskParams(selectedCrimeSummary.getTsk()),
					new QueryParameters()
					{
						@Override
						public void bind(ParameterBinder binder) throws BindingException
						{
							binder.bind("mCrimeName", selectedCrimeSummary.getCrimeName());
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
	protected String getServiceMethodName()
	{
		return "getCrimeList";
	}

	@Override
	protected QueryResultProjector<String, CrimesList> getResultProjector()
	{
		return new QueryResultProjector<String, CrimesList>()
		{
			@Override
			public CrimesList projectResult(String sourceResult)
			{
				return new Gson().fromJson(sourceResult, CrimesList.class);
			}
		};
	}
}
