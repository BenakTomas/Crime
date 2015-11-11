package benak.tomas.crimemonitor.client.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TextView;
import benak.tomas.crimemonitor.R;
import benak.tomas.crimemonitor.library.CrimeUtvarDetail;
import benak.tomas.crimemonitor.library.query.resultProjector.QueryResultProjector;

import com.google.gson.Gson;

/**
 * UtvarDetailActivity is the activity class used to display the police department detailed info.
 * <p>As in every {@code CrimeActivityBase}, the department info is displayed for the current location
 * and loaded in background by a loading fragment on demand or in reponse to the location changes.
 * <p>
 * The activity displays the following information about the police department:
 * <ul>
 * <li>the police department full name
 * <li>the list of names of all municipalities that are "covered" by the given police department
 * <br><br>
 * <p>
 * A municipality is "covered", if the territory of the police department intersects with territory of the
 * given municipality.
 * 
 * @author Tom
 *
 * @see CrimeUtvarDetail
 * @see CrimeActivityBase
 * @see UtvarDetailLoadingFragment
 */
public class UtvarDetailActivity extends CrimeActivityBase<CrimeUtvarDetail>
{
	/**
	 * the label for the {@link #mNazvyObciView}
	 */
	private TextView		mNazvyObciLabel;
	/**
	 * the view displaying the list of names of the covered municipalities
	 */
	private LinearLayout	mNazvyObciView;

	@Override
	protected void setupViewReferences()
	{
		super.setupViewReferences();

		mNazvyObciLabel = (TextView) this
				.findViewById(R.id.crime_utvar_detail_nazvy_obci_label);
		mNazvyObciView = (LinearLayout) this
				.findViewById(R.id.crime_utvar_detail_nazvy_obci);
	}

	@Override
	protected int getLayoutResourceId()
	{
		return R.layout.activity_utvar_detail;
	}

	@Override
	protected int getOptionsMenuResourceId()
	{
		return R.menu.crime_utvar_detail_action_bar_actions;
	}

	/**
	 * Displays the department full name and refreshes the covered municipalities names list.
	 */
	@Override
	protected void displayCustomCrimeViews()
	{
		//mNazevUtvaruView.setText(mNazevUtvaru);
		
		mNazvyObciView.removeAllViews();
		
		int nazevObceViewPosition = 1;
		
		for(String nazevObce : mActivityState.getCrimeData().getNazvyPokryvajicichObci())
		{
			View nazevObceView = this.getObecView(nazevObceViewPosition++, nazevObce);
			
			LinearLayout.LayoutParams toplistItemLayoutParams = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			//toplistItemLayoutParams.setMargins(left, top, right, bottom)
			nazevObceView.setLayoutParams(toplistItemLayoutParams);
			
			mNazvyObciView.addView(nazevObceView);
		}
		
		mNazvyObciView.requestLayout();
	}

	@Override
	protected void collapseNormalLayout()
	{
		//mNazevUtvaruLabel.setVisibility(View.GONE);
		//mNazevUtvaruView.setVisibility(View.GONE);
		mNazvyObciLabel.setVisibility(View.GONE);
		mNazvyObciView.setVisibility(View.GONE);
	}

	@Override
	protected void showNormalLayout()
	{
		//mNazevUtvaruLabel.setVisibility(View.VISIBLE);
		//mNazevUtvaruView.setVisibility(View.VISIBLE);
		mNazvyObciLabel.setVisibility(View.VISIBLE);
		mNazvyObciView.setVisibility(View.VISIBLE);
	}

	@Override
	protected CharSequence getTitleForCrimeData()
	{
		return this.getNazevUtvaruStringForSubtitle();
	}
	
	@Override
	protected String getSubtitleForCrimeData()
	{
		return this
				.getString(R.string.crime_utvar_detail_action_bar_custom_subtitle);
	}
	
	/**
	 * Creates the view for a municipality item in the list of the covered municipalities.
	 * <p>
	 * The background color differs according to the position being an even or odd number.
	 * This creates the list with the items that alternate their background color.
	 * 
	 * @param position the position of a municipality item to create the view for
	 * @param nazevObce the municipality item (simply the municipality name) to create the view for
	 * 
	 * @return the view for a municipality item in the list of the covered municipalities
	 */
	private View getObecView(int position, String nazevObce)
	{
		// get the inflater
		LayoutInflater inflater = (LayoutInflater) this.getLayoutInflater();

		// inflate the toplist row view
		View rowView = inflater.inflate(R.layout.crime_utvar_detail_obce_list_row, mNazvyObciView, false);

		// nastav text
		TextView nazevObceView = (TextView) rowView
				.findViewById(R.id.crime_utvar_detail_list_row_nazev_obce);
		nazevObceView.setText(nazevObce);
		
		if(position % 2 == 0)
		{
			nazevObceView.setBackgroundColor(this.getResources().getColor(R.color.crime_utvar_detail_obce_list_row_even_row_background_color));
					//this.getResources().getColor(R.color.crime_utvar_detail_obce_list_row_even_row_background_color));
		}

		return rowView;
	}

	@Override
	protected String getServiceMethodName()
	{
		return "getUtvarDetail";
	}

	@Override
	protected QueryResultProjector<String, CrimeUtvarDetail> getResultProjector()
	{
		return new QueryResultProjector<String, CrimeUtvarDetail>()
		{			
			@Override
			public CrimeUtvarDetail projectResult(String sourceResult)
			{
				return new Gson().fromJson(sourceResult, CrimeUtvarDetail.class);
			}
		};
	}
}
