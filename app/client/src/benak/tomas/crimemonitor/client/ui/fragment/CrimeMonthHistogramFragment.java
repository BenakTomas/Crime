package benak.tomas.crimemonitor.client.ui.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import benak.tomas.crimemonitor.client.ui.view.CrimeMonthHistogramObservationView;
import benak.tomas.crimemonitor.library.CrimeMonthHistogramObservation;
import benak.tomas.crimemonitor.library.CrimeSummaryDetailed;

import benak.tomas.crimemonitor.R;

/**
 * CrimeMonthHistogramFragment is a UI fragment class used to display the crime count monthly histogram.
 * <p>
 * The crime count monthly histogram is a histogram recording the number of committed crimes per each
 * month of a given crime data interval.
 * <p>
 * The histogram is displayed horizontally as a series of vertical columns views.
 * {@code CrimeMonthHistogramObservationView} is used as a column view class.
 * <p>
 * Each column view contains a colored bar and a label.
 * The label contains the number of committed crimes.
 * The height of the bar is in proportion to the displayed number of committed crimes.
 * The columns are colored in such a way, that all the columns displaying the data for a month in the particular
 * year share the same color.
 * 
 * @see CrimeMonthHistogramObservationView
 * @see CrimeMonthHistogramObservation
 * 
 * @author Tom
 */
public class CrimeMonthHistogramFragment extends Fragment
{
	/**
	 * the content view for this fragment
	 */
	private View mContentView;
	/**
	 * the view group container for the crime month histogram column views
	 */
	private LinearLayout mBarChart;
	
	/**
	 * Creates the content view for this fragment and sets up the references to the views used.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		// create the content view
		mContentView = inflater.inflate(R.layout.crime_month_histogram_fragment, container, false);
		
		// set up view references
		this.setupViewReferences();
		
		// return the created content view
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
		mBarChart = (LinearLayout) mContentView.findViewById(R.id.crime_month_histogram_bar_chart);
	}
	
	/**
	 * Displays the histogram using the provided {@code CrimeSummaryDetailed} detail data.
	 * 
	 * @param data the crime detail data to extract the histogram from
	 * 
	 * @see CrimeSummaryDetailed
	 * @see CrimeMonthHistogramObservation
	 * @see CrimeMonthHistogramObservationView
	 */
	public void displayHistogram(CrimeSummaryDetailed data)
	{
		// vypocti maximalni hodnotu v histogramu
		int maxCrimeCount = 0;
		for (CrimeMonthHistogramObservation observation : data
				.getMonthHistogram())
			maxCrimeCount = Math
					.max(maxCrimeCount, observation.getCrimeCount());

		// odstran vsechny dosavadni views (sloupecky)
		mBarChart.removeAllViews();
		
		final int firstYearColor = Color.GRAY;
		final int secondYearColor = Color.BLUE;
		
		int precedingYear;
		int barColor = firstYearColor;
		CrimeMonthHistogramObservation[] observations = data.getMonthHistogram();
		CrimeMonthHistogramObservation firstObservation = observations[0];
		precedingYear = firstObservation.getYear();
//		CrimeMonthHistogramObservationView firstView = new CrimeMonthHistogramObservationView(
//				this.getActivity(), new CrimeMonthHistogramObservation(
//						firstObservation.getYear(), firstObservation.getMonth(),
//						firstObservation.getCrimeCount()), maxCrimeCount, firstYearColor);
		
		// vytvor nove sloupecky
		for (int i = 0; i < observations.length; ++i)
		{
			CrimeMonthHistogramObservation observation = observations[i];
		
			int currentYear = observation.getYear();
			if(currentYear != precedingYear)
				barColor = secondYearColor;
			
			CrimeMonthHistogramObservationView view = new CrimeMonthHistogramObservationView(
					this.getActivity(), new CrimeMonthHistogramObservation(
							observation.getYear(), observation.getMonth(),
							observation.getCrimeCount()), maxCrimeCount, barColor);
			
			
			LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.MATCH_PARENT);
			// nastav margin
			lParams.setMargins(5, 0, 5, 0);
			view.setLayoutParams(lParams);
			mBarChart.addView(view);
		}
		
		// pozadej o layout sloupecku
		mBarChart.requestLayout();
	}
}
