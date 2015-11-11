package benak.tomas.crimemonitor.client.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import benak.tomas.crimemonitor.R;

/**
 * CrimeListAdapter is an {@code Adapter} for the list of crimes.
 * <p>
 * This class holds the collection of crime list items.
 * It creates the view for a given crime list item.
 * The crime list item type is {@code CrimeListItem}.
 * @author Tom
 *
 */
public class CrimeListAdapter extends ArrayAdapter<CrimeListItem> {

	/**
	 * the helper context.
	 * <p>The context is used to obtain various system services for the internal use of this adapter. 
	 */
	private final Context mContext;
	
	/**
	 * The constructor.
	 * <p>
	 * An instance of context is passed for this adapter.
	 * 
	 * @param context the context for the internal use
	 */
	public CrimeListAdapter(Context context)
	{
		super(context, R.layout.crime_list_row);
		
		if(context == null)
			throw new NullPointerException("context");
		
		this.mContext = context;
	}
	
	/**
	 * Creates the view displaying a crime list item at a given position.
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View rowView;
		if(convertView == null)
			rowView = inflater.inflate(R.layout.crime_list_row, parent, false);
		else
			rowView = convertView;
		
		// nacti aktualne vykreslovanou item
		CrimeListItem item = this.getItem(position);
		
		// nastav poradi zlocinu
		TextView crimeRankView = (TextView) rowView.findViewById(R.id.crime_list_row_crime_rank);
		crimeRankView.setText(String.format("%d", this.getCrimeRankFromPosition(position)));
		
		// nastav popisek zlocinu
		TextView crimeNameView = (TextView) rowView.findViewById(R.id.crime_list_row_crime_name);
		crimeNameView.setText(item.getCrimeName());
		
		// nastav hodnotu
		TextView crimeNumberView = (TextView) rowView.findViewById(R.id.crime_list_row_crime_number);
		crimeNumberView.setText(Integer.valueOf(item.getValue()).toString());
		
		return rowView;
	}
	
	/**
	 * For an index starting from zero this method returns the value of an index starting from one.
	 * 
	 * @param position the 0-starting index value 
	 * @return the equivalent 1-starting index value 
	 */
	private int getCrimeRankFromPosition(int position)
	{
		return position + 1;
	}
}
