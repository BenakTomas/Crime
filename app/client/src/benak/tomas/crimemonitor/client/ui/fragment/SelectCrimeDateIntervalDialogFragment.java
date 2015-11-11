package benak.tomas.crimemonitor.client.ui.fragment;

import java.lang.reflect.Field;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import benak.tomas.crimemonitor.client.exception.StartDatePickerCannotBeConfiguredException;
import benak.tomas.crimemonitor.library.utils.CalendarUtils;

import benak.tomas.crimemonitor.R;

// Prozatim bude jeden picker a druhy konec intervalu bude fixne dopocitan (12 mesicu dohromady).
// V budoucnu mozna manualne nastavitelny pocet zobrazenych mesicu (max. hodnota 12?, min. 3 mesice?), nebo
// ctvrtleti a pololeti.
// Layout asi stejny pro uzky i siroky displej.
/**
 * SelectCrimeDateIntervalDialogFragment is a class used to display a crime data interval selection dialog.
 * <p>
 * The user selects a month and a year. Those values are used for the initial year and month in a crime data interval.
 * The terminal year and month are calculated from the initial year and month by adding 12 months to the initial month.
 * <p>
 * The selected interval is reported back to the {@code SelectCrimeDateIntervalDialogListener}, typically a disguised
 * parent activity class that needs to select the crime data interval.
 * <p>
 * The helper {@code SelectCrimeDateIntervalDialogFragmentExceptionHandler} listener is used to report the exceptions
 * that occur in this fragment back to the parent activity.
 * 
 * @see SelectCrimeDateIntervalDialogListener
 * @see SelectCrimeDateIntervalDialogFragmentExceptionHandler
 * 
 * @author Tom
 */
public class SelectCrimeDateIntervalDialogFragment extends DialogFragment
{
	/**
	 * the fixed crime data interval length in months
	 */
	private final int CRIME_INTERVAL_MONTH_LENGTH = 12;
	
	/**
	 * SelectCrimeDateIntervalDialogFragmentExceptionHandler is a helper interface used to report the exceptions in {@code SelectCrimeDateIntervalDialogFragment} back to the parent activity.
	 * 
	 * @author Tom
	 */
	public interface SelectCrimeDateIntervalDialogFragmentExceptionHandler
	{
		/**
		 * Called to report an {@code Exception} to be handled.
		 * 
		 * @param e the {@code Exception} to be handled
		 */
		public void handleSelectCrimeDateIntervalDialogException(Exception e);
	}

	/**
	 * the reference to the exception handler for this fragment
	 */
	private SelectCrimeDateIntervalDialogFragmentExceptionHandler	mExceptionHandler;

	/**
	 * the reference to object that listens for the crime data interval selection changes
	 */
	private SelectCrimeDateIntervalDialogListener					mListener;
	
	/**
	 * the initial year of the crime data interval
	 */
	private int														mStartYear;
	/**
	 * the initial month of the crime data interval
	 */
	private int														mStartMonth;
	/**
	 * the terminal year of the crime data interval.
	 * <p>Not used.
	 */
	private int														mEndYear;
	/**
	 * the terminal month of the crime data interval.
	 * <p>Not used.
	 */
	private int														mEndMonth;

	public SelectCrimeDateIntervalDialogFragment()
	{
	}
	
	/**
	 * Constructs a new {@code SelectCrimeDateIntervalDialogFragment} using a provider initial year and month of the crime data interval.
	 * 
	 * @param startYear the initial year of the crime data interval
	 * @param startMonth the initial month of the crime data interval
	 */
	public SelectCrimeDateIntervalDialogFragment(int startYear, int startMonth)
	{
		mStartYear = startYear;
		mStartMonth = startMonth;
	}
	
	/**
	 * SelectCrimeDateIntervalDialogListener is a helper interface used to report the crime data interval selection change to a parent activity.
	 * 
	 * @author Tom
	 */
	public interface SelectCrimeDateIntervalDialogListener
	{
		/**
		 * Called to notify the listener that the crime data interval has changed.
		 * <p>The crime data interval boundaries are reported in parameter values.
		 * 
		 * @param startYear the selected initial year for the crime data interval
		 * @param startMonth the selected initial month for the crime data interval
		 * @param endYear the selected terminal year for the crime data interval
		 * @param endMonth the selected terminal month for the crime data interval
		 */
		public void onCrimeDateIntervalSelected(int startYear, int startMonth, int endYear, int endMonth);
	}

	/**
	 * Stores the references to the listeners.
	 * <p>
	 * The parent activity acts as both of the listeners.
	 * 
	 * @param activity the parent activity
	 */
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);

		try
		{
			mListener = (SelectCrimeDateIntervalDialogListener) activity;
			mExceptionHandler = (SelectCrimeDateIntervalDialogFragmentExceptionHandler) activity;
		}
		catch (ClassCastException e)
		{
			throw new ClassCastException(
					"Activity must implement interfaces.");
		}
	}

	/**
	 * Creates the dialog to be displayed withing this fragment.
	 * <p>
	 * See the description for this class for the details about this dialog
	 * design and behavior.
	 * <p>Technically, there is a hack, that hides the day picker part of the
	 * used {@code DatePicker}. This hack is implemented in {@link #hideDayOfMonthOnPicker}.
	 */
	@SuppressLint("InflateParams")
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
    	LayoutInflater inflater = this.getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        
        View dialogView = inflater.inflate(R.layout.select_date_interval_dialog, null);
        final DatePicker startDatePicker = (DatePicker)dialogView.findViewById(R.id.select_date_interval_startDate);
        try
		{
			this.hideDayOfMonthOnPicker(startDatePicker);
		}
		catch (StartDatePickerCannotBeConfiguredException e)
		{
			mExceptionHandler.handleSelectCrimeDateIntervalDialogException(e);
			return null;
		}
        
        // nezobrazuj jako kalendar
        startDatePicker.setCalendarViewShown(false);
        
        // nastav datum a handler zmeny data
        startDatePicker.init(CalendarUtils.getYearForJavaCalendar(mStartYear), CalendarUtils.getMonthForJavaCalendar(mStartMonth), 1, new OnDateChangedListener()
		{
			@Override
			public void onDateChanged(DatePicker view, int year, int monthOfYear,
					int dayOfMonth)
			{
				// TODO Auto-generated method stub
				
			}
		});
        
        builder
        	.setView(dialogView)
        	.setTitle(R.string.display_crime_detail_set_interval)
        	.setPositiveButton("OK", new OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					// TODO: kontrola, ze je datum "do" vetsi nebo rovno nez datum "od"
					
					int javaCalendarStartYear = startDatePicker.getYear();
					int javaCalendarStartMonth = startDatePicker.getMonth();
					
					Calendar cal = Calendar.getInstance();
					CalendarUtils.setJavaCalendarYearAndMonthForJavaCalendar(javaCalendarStartYear, javaCalendarStartMonth, cal);
					cal.add(Calendar.MONTH, CRIME_INTERVAL_MONTH_LENGTH);
					
					int startYear = CalendarUtils.getYearFromJavaCalendar(javaCalendarStartYear);
					int startMonth = CalendarUtils.getMonthFromJavaCalendar(javaCalendarStartMonth);
					int endYear = CalendarUtils.getYearFromJavaCalendar(cal);
					int endMonth = CalendarUtils.getMonthFromJavaCalendar(cal);
					
					// uzivatel vybral interval, je nutne notifikovat aktivitu
					mListener.onCrimeDateIntervalSelected(startYear, startMonth, endYear, endMonth);
				}
			})
			.setNegativeButton("Cancel", new OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					SelectCrimeDateIntervalDialogFragment.this.getDialog().cancel();
				}
			});
        // Create the AlertDialog object and return it
        return builder.create();
    }

	/**
	 * The "hack" used to hide the day picker part of the {@code DatePicker}.
	 * 
	 * @param startDatePicker the date picker for which to hide its day picker part
	 * 
	 * @throws StartDatePickerCannotBeConfiguredException if an exception occurs when trying to locate the right day picker field in the date picker
	 */
	private void hideDayOfMonthOnPicker(DatePicker startDatePicker)
			throws StartDatePickerCannotBeConfiguredException
	{
		try
		{
			Field f[] = startDatePicker.getClass().getDeclaredFields();
			for (Field field : f)
			{
				if (field.getName().equals("mDayPicker")
						|| field.getName().equals("mDaySpinner"))
				{
					field.setAccessible(true);
					Object yearPicker = new Object();
					yearPicker = field.get(startDatePicker);
					((View) yearPicker).setVisibility(View.GONE);
				}
			}
		}
		catch (Exception e)
		{
			throw new StartDatePickerCannotBeConfiguredException(e);
		}
	}
}
