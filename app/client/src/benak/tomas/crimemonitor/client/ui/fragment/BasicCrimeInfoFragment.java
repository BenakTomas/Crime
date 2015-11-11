package benak.tomas.crimemonitor.client.ui.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import benak.tomas.crimemonitor.client.ui.view.CrimeIndexCompactView;
import benak.tomas.crimemonitor.client.ui.view.ObjasnenostView;
import benak.tomas.crimemonitor.client.ui.view.TrendView;
import benak.tomas.crimemonitor.library.CrimesSummaryBasic;

import benak.tomas.crimemonitor.R;

/**
 * BasicCrimeInfoFragment is a UI fragment class used to display
 * the basic crime info.
 * <p>Basic crime info is
 * <ul>
 * <li>crime index
 * <li>crime trend
 * <li>crime solved ratio
 * 
 * <p>This fragment is reused across the activities
 * that need to display the basic crime info.
 * 
 * @author Tom
 *
 */
public class BasicCrimeInfoFragment extends Fragment
{
	/**
	 * the fragment's content view
	 */
	private View mContentView;
	/**
	 * the crime trend view
	 */
	private TrendView mTrendView;
	/**
	 * the crime solved ratio view
	 */
	private ObjasnenostView mObjasnenostView;
	/**
	 * the crime index view
	 */
	private CrimeIndexCompactView mIndexView;
	
	/**
	 * The empty constructor.
	 * <p>Used by the fragment manager for the fragment instantiation when the fragment is being restored.
	 */
	public BasicCrimeInfoFragment()
	{
	}
	
	/**
	 * Standard {@link Fragment#onCreateView} override.
	 * <p>
	 * Creates the content view for the fragment and sets up
	 * the view references.
	 * </p>
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		// inflate the content viewgroup
		mContentView = inflater.inflate(R.layout.basic_crime_info_fragment_layout, container, false);
		
		// store the references
		this.setupViewReferences();
		
		// return the inflated content view
		return mContentView;
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
		mTrendView = (TrendView) mContentView.findViewById(R.id.trend);
		mObjasnenostView = (ObjasnenostView) mContentView.findViewById(R.id.objasnenost);
		mIndexView = (CrimeIndexCompactView) mContentView.findViewById(R.id.index);
	}
	
	/**
	 * Called to display the basic crime info for the given crime data.
	 * 
	 * @param crimesSummary the crime data to display the basic crime info for
	 */
	public void onCrimeDataLoaded(CrimesSummaryBasic crimesSummary)
	{
		mTrendView.setTrend(crimesSummary.getTrend());
		mObjasnenostView.setObjasneno(crimesSummary.getObjasnenost());
		mIndexView.setIndex(crimesSummary.getCrimeIndex());
	}
}
