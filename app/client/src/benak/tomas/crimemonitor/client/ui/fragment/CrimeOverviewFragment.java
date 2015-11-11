package benak.tomas.crimemonitor.client.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TextView;
import benak.tomas.crimemonitor.R;
import benak.tomas.crimemonitor.client.activity.CrimeToplistItem;
import benak.tomas.crimemonitor.client.activity.handler.OnCrimeSelectedListener;
import benak.tomas.crimemonitor.client.ui.view.CrimesOverviewPieChart;
import benak.tomas.crimemonitor.library.CrimeSummary;
import benak.tomas.crimemonitor.library.CrimesSummary;

/**
 * CrimeOverviewFragment is a UI fragment class used to display the overview of all committed crimes.
 * <p>The overview is calculated from the given crimes summary, which encapsulates the
 * data for all the committed crimes for the particular department and crime data interval.
 * <p>
 * The crime overview consists of
 * <ul>
 * <li>list view with clickable items and with
 * <li>a pie chart, that visualizes the toplist in proportion to the number of committed crimes of the toplist items
 * <br /><br />
 * <p>The toplist displays the first {@link #DISPLAY_CRIMES_NUMBER} most common crimes (also called "top crimes") and the "other crimes" item, aggregating
 * all of the remaining crimes. 
 * 
 * <p>This fragment reports the requests to display the complete crime list to its parent activity
 * via the helper interface {@code OnShowCrimeListListener}.
 * <p>When the used selects the particular top crime, the fragment notifies its parent activity via
 * the
 *    
 * @author Tom
 * 
 * @see CrimesSummary
 * @see benak.tomas.crimemonitor.client.activity.handler.client.activity.handler.OnShowCrimeListListener.OnShowCrimeListListener
 */
public class CrimeOverviewFragment extends Fragment
{
	/**
	 * the colors used for the top crimes pie chart slices
	 */
	private final int[]					CRIME_COLORS		= { Color.RED,
			Color.BLUE, Color.GREEN, Color.YELLOW, Color.BLACK, Color.MAGENTA };
	/**
	 * the color used for the "other crimes" pie chart slice
	 */
	private final int					OTHER_CRIMES_COLOR	= Color.DKGRAY;
	/**
	 * the number of top crimes to display in both the toplist and pie chart 
	 */
	private final int					DISPLAY_CRIMES_NUMBER;
	{
		DISPLAY_CRIMES_NUMBER = CRIME_COLORS.length;
	}

	/**
	 * the TSK identifier value used to identify the "other crimes" i
	 */
	private final int					OTHER_CRIMES_TSK	= 10000;

	/**
	 * the main content view of this fragment
	 */
	private View						mContentView;
	/**
	 * the view used to display the pie chart
	 */
	private CrimesOverviewPieChart		mPieChart;
	/**
	 * the linear layout view container used to group the toplist items in a list view fashion
	 */
	private LinearLayout				mToplist;
	/**
	 * the button used to enable the user to request the complete crime list display
	 */
	private Button						mShowCrimeListButton;

	/**
	 * the reference to the "crime selected" listener object, that listens for the crime selection change.
	 * <p>In this implementation, the parent activity acts as the listener.
	 */
	private OnCrimeSelectedListener		mCrimeSelectedListener;
	/**
	 * the reference to the "show crime list" listener object, that listens for the request to display the complete crime list.
	 * <p>In this implementation, the parent activity acts as the listener.
	 */
	private benak.tomas.crimemonitor.client.activity.handler.OnShowCrimeListListener		mOnShowCrimeListListener;
	
	/**
	 * the list of crime items to display in both the toplist and the pie chart
	 */
	private ArrayList<CrimeToplistItem> mToplistItems = new ArrayList<CrimeToplistItem>();

	/**
	 * Stores the references to the fragment listeners.
	 * <p>The parent activity acts is used to act as both of the listeners.
	 */
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);

		// obtain the "crime selected" listener
		mCrimeSelectedListener = (OnCrimeSelectedListener) activity;
		// obtain the "show crime list" listener
		mOnShowCrimeListListener = (benak.tomas.crimemonitor.client.activity.handler.OnShowCrimeListListener) activity;
	}

	/**
	 * Creates the content view for this fragment and sets up the references to the views used.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		// create the content view
		mContentView = inflater.inflate(R.layout.crime_overview_fragment,
				container, false);

		// setup view references
		this.setupViewReferences();

		// return the created view
		return mContentView;
	}

	/**
	 * Sets up the views used in this fragment after the parent activity is started. 
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		// setup views (action handlers etc.)
		this.setupViews();
	}

	/**
	 * Sets up the view references for this fragment.
	 * <p>
	 * This method looks the views up by their resource ids and stores the
	 * references.
	 * </p>
	 */
	private void setupViewReferences()
	{
		mPieChart = (CrimesOverviewPieChart) mContentView
				.findViewById(R.id.crime_overview_piechart);
		mToplist = (LinearLayout) mContentView
				.findViewById(R.id.crime_overview_toplist);
		mShowCrimeListButton = (Button) mContentView
				.findViewById(R.id.crime_overview_show_crime_list_button);
	}

	/**
	 * Sets up the views used in this fragment.
	 * <p>Various event listeners are set on the views. 
	 */
	private void setupViews()
	{
		// nastav chovani pri vyberu polozky
		// mToplist.setAdapter(new CrimeToplistAdapter(this.getActivity()));
		// mToplist.setOnItemClickListener(new OnItemClickListener()
		// {
		// private OnCrimeSelectedListener mListener = mCrimeSelectedListener;
		//
		// @Override
		// public void onItemClick(AdapterView<?> arg0, View arg1,
		// int itemPosition, long itemId)
		// {
		// CrimeToplistAdapter toplistAdapter = (CrimeToplistAdapter) mToplist
		// .getAdapter();
		// CrimeToplistItem selectedItem = toplistAdapter
		// .getItem(itemPosition);
		//
		// mListener.onCrimeSelected(selectedItem.getTsk());
		// }
		// });

		mShowCrimeListButton.setOnClickListener(new OnClickListener()
		{
			benak.tomas.crimemonitor.client.activity.handler.OnShowCrimeListListener	mListener	= mOnShowCrimeListListener;

			@Override
			public void onClick(View v)
			{
				mListener.showCrimeList();
			}
		});
	}

	/**
	 * Displays the crime summary in the toplist and in the pie chart.
	 * 
	 * @param crimeData the crime summary data that is to be displayed by this fragment
	 */
	public void loadCrimeOverview(CrimesSummary crimeData)
	{
		CrimeSummary[] crimeSummaries = crimeData.getCrimeSummaries();

		final int numberOfLoadedCrimes = crimeSummaries.length;
		final boolean useRestItem = numberOfLoadedCrimes > DISPLAY_CRIMES_NUMBER;
		final int numberOfDisplayedCrimes = Math.min(DISPLAY_CRIMES_NUMBER,
				numberOfLoadedCrimes);
		final int maxCrimeIndexToDisplay = numberOfDisplayedCrimes - 1;

		// crime overview items
		ArrayList<CrimeToplistItem> crimeOverviewItems = new ArrayList<CrimeToplistItem>();

		// vytvor seznam pro zobrazeni
		for (int i = 0; i < maxCrimeIndexToDisplay; ++i)
		{
			CrimeSummary crimeSummary = crimeSummaries[i];
			crimeOverviewItems.add(new CrimeToplistItem(crimeSummary.getTsk(),
					crimeSummary.getCrimeName(), crimeSummary.getCrimeNumber(),
					CRIME_COLORS[i]));
		}

		CrimeToplistItem lastItem;

		if (!useRestItem)
		{
			CrimeSummary lastCrimeSummaryItem = crimeSummaries[maxCrimeIndexToDisplay];
			lastItem = new CrimeToplistItem(lastCrimeSummaryItem.getTsk(),
					lastCrimeSummaryItem.getCrimeName(),
					lastCrimeSummaryItem.getCrimeNumber(),
					CRIME_COLORS[maxCrimeIndexToDisplay]);
		}
		else
		{
			int otherCrimesNumberSum = 0;
			for (int i = maxCrimeIndexToDisplay; i < numberOfLoadedCrimes; ++i)
			{
				CrimeSummary restCrimeSummary = crimeSummaries[i];
				otherCrimesNumberSum += restCrimeSummary.getCrimeNumber();
			}

			final String otherCrimesLabel = this.getResources().getString(
					R.string.crime_summary_toplist_other_item_label);
			lastItem = new CrimeToplistItem(OTHER_CRIMES_TSK, otherCrimesLabel,
					otherCrimesNumberSum, OTHER_CRIMES_COLOR);
		}

		// pridat polozku "ostatni", pokud jeste nejake trestne ciny zbyvaji
		crimeOverviewItems.add(lastItem);

		// nastav ho k zobrazeni pro pie chart view
		mPieChart.setCrimeItems(crimeOverviewItems
				.toArray(new CrimeToplistItem[0]));

		// set the current items collection
		mToplistItems = crimeOverviewItems;
		// remove all views
		mToplist.removeAllViews();

		// pridej polozky do toplistu
		for (CrimeToplistItem toplistItem : crimeOverviewItems)
		{
			View toplistItemView = this.getView(toplistItem);
			
			LinearLayout.LayoutParams toplistItemLayoutParams = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			//toplistItemLayoutParams.setMargins(left, top, right, bottom)
			toplistItemView.setLayoutParams(toplistItemLayoutParams);
			
			mToplist.addView(toplistItemView);
			
			toplistItemView.setOnClickListener(new OnClickListener()
			{
				private OnCrimeSelectedListener mListener = mCrimeSelectedListener;
				@Override
				public void onClick(View toplistItemView)
				{
					// get the index of a toplist item view
					int toplistItemIndex = mToplist.indexOfChild(toplistItemView);
					// this index corresponds to the index of a toplist item in the mToplistItems collection
					CrimeToplistItem selectedItem = mToplistItems.get(toplistItemIndex);
					// call the listener
					mListener.onCrimeSelected(selectedItem.getTsk());
				}
			});
			
			final Context onTouchListenerContext = this.getActivity();
			
			toplistItemView.setOnTouchListener(new OnTouchListener()
			{
				private final Context mContext = onTouchListenerContext;
				@SuppressLint("ClickableViewAccessibility")
				@Override
				public boolean onTouch(View v, MotionEvent event)
				{
					int action = event.getAction();
					
					switch(action)
					{
						case MotionEvent.ACTION_DOWN:
						case MotionEvent.ACTION_HOVER_ENTER:
							v.setBackgroundColor(mContext.getResources().getColor(R.color.crime_summary_toplist_row_selected_background_color));
							//v.setBackgroundColor(1714664933);
							break;
							
						case MotionEvent.ACTION_UP:
							v.setBackgroundColor(mContext.getResources().getColor(R.color.app_background_color));
							break;
							
						case MotionEvent.ACTION_CANCEL:
						case MotionEvent.ACTION_HOVER_EXIT:
							v.setBackgroundColor(mContext.getResources().getColor(R.color.app_background_color));
							break;
					}
					
					return false;
				}
			});
		}
		
		mToplist.requestLayout();
	}

	/**
	 * Creates the view used to represent a toplist item in the toplist view.
	 * 
	 * @param item the toplist item to create the view for
	 * @return the view used to represent the given toplist item in the toplist view
	 */
	public View getView(CrimeToplistItem item)
	{
		// get the inflater
		LayoutInflater inflater = (LayoutInflater) this.getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// inflate the toplist row view
		View rowView = inflater.inflate(R.layout.crime_summary_toplist_row, mToplist, false);

		// nacti color view
		ImageView colorView = (ImageView) rowView
				.findViewById(R.id.crime_summary_toplist_row_color);
		// vyber barvu podle aktualne vybrane item
		// TODO: co velikost color?
		colorView.setBackgroundColor(item.getColor());

		// nastav text
		TextView crimeNameView = (TextView) rowView
				.findViewById(R.id.crime_summary_toplist_row_crime_name);
		crimeNameView.setText(item.getCrimeName());

		// nastav hodnotu
		TextView crimeNumberView = (TextView) rowView
				.findViewById(R.id.crime_summary_toplist_row_crime_number);
		crimeNumberView.setText(Integer.valueOf(item.getValue()).toString());

		return rowView;
	}
}
